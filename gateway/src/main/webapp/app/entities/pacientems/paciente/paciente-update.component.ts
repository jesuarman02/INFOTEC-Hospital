import { type Ref, computed, defineComponent, inject, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import PacienteService from './paciente.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IPaciente, Paciente } from '@/shared/model/pacientems/paciente.model';

// EXPRESIÓN REGULAR: Solo letras mayúsculas, minúsculas, acentos y espacios.
const soloLetrasRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
const validadorSoloLetras = (value: string | null | undefined) => {
  if (!value) return true; 
  return soloLetrasRegex.test(value);
};

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteUpdate',
  setup() {
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paciente: Ref<IPaciente> = ref(new Paciente());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePaciente = async (pacienteId: number) => {
      try {
        const res = await pacienteService().find(pacienteId);
        paciente.value = res;
      } catch (error) {
        alertService.showHttpError((error as any).response);
      }
    };

    if (route.params?.pacienteId) {
      retrievePaciente(Number(route.params.pacienteId));
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();

    // Forzar mayúsculas con protección target null
    const onInputUpper = (event: Event, field: any) => {
      const target = event.target as HTMLInputElement | null;
      if (!target) return;
      field.$model = target.value.toUpperCase();
    };

    const validationRules = {
      ecu: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 60 }).toString(), 60),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 2 }).toString(), 2),
        soloLetras: validadorSoloLetras,
      },
      apellidoPaterno: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 60 }).toString(), 60),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 2 }).toString(), 2),
        soloLetras: validadorSoloLetras,
      },
      apellidoMaterno: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 60 }).toString(), 60),
        soloLetras: validadorSoloLetras,
      },
      sexo: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      embarazo: {},
      nacionalidad: {},
      fechaNacimiento: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      estadoCivil: {},
      curp: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 18 }).toString(), 18),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 18 }).toString(), 18),
      }
    };

    const v$ = useVuelidate(validationRules, paciente as any);
    v$.value.$validate();

    // --- WATCHERS ---

// Vigilamos los cambios en el Sexo
    watch(() => paciente.value.sexo, (nuevoSexo) => {
      if (nuevoSexo === 'H') { // <-- Cambiado a M (Masculino)
        paciente.value.embarazo = 'N/A';
      } else if (nuevoSexo === 'M') { // <-- Cambiado a F (Femenino)
        if (paciente.value.embarazo === 'N/A' || !paciente.value.embarazo) {
          paciente.value.embarazo = ''; 
        }
      }
    });

    watch(() => paciente.value.nacionalidad, (nuevaNacionalidad) => {
      if (nuevaNacionalidad === 'EXTRANJERA') {
        // Generamos un CURP falso y ÚNICO usando la fecha (milisegundos)
        // 'EXTXX' + 13 números de la fecha = 18 caracteres exactos
        paciente.value.curp = 'EXTXX' + Date.now().toString();
      } else if (nuevaNacionalidad === 'MEXICANA') {
        // Si regresa a mexicana, limpiamos el curp falso
        if (paciente.value.curp && paciente.value.curp.startsWith('EXTXX')) {
          paciente.value.curp = '';
        }
      }
    });

    return {
      pacienteService,
      alertService,
      paciente,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
      onInputUpper,
    };
  },

  methods: {
  save(): void {
      // --- INICIO DEL BLINDAJE DE EMBARAZO ---
      if (this.paciente.sexo === 'H') { // <-- Cambiado a M (Masculino)
        this.paciente.embarazo = 'N/A'; // Forzamos N/A si es hombre
      } else if (this.paciente.sexo === 'M' && (!this.paciente.embarazo || this.paciente.embarazo === 'N/A')) { // <-- Cambiado a F (Femenino)
        // Si es mujer y lo dejó vacío, lanzamos alerta y cancelamos el guardado
        this.alertService.showError('Por favor, indique si la paciente está embarazada.');
        return; 
      }
      // --- FIN DEL BLINDAJE ---

      // Validación Vuelidate
      this.v$.$validate();

      if (this.v$.$invalid) {
        this.alertService.showError('Por favor, revise los campos en rojo. Faltan datos o tienen un formato incorrecto.');
        return;
      }

      this.isSaving = true;

      // --- UPDATE ---
      if (this.paciente.id) {
        this.pacienteService()
          .update(this.paciente)
          .then(() => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('¡Los datos del paciente se han actualizado correctamente!');
          })
          .catch(error => {
            this.isSaving = false;
            const errorData = JSON.stringify(error.response?.data || {}).toLowerCase();

            if (errorData.includes('curp') || errorData.includes('constraint')) {
              this.alertService.showError('Error: El CURP ingresado es incorrecto o ya se encuentra registrado en el sistema.');
            } else {
              this.alertService.showHttpError(error.response);
            }
          });

        return;
      }

      // --- CREATE ---
      this.pacienteService()
        .create(this.paciente)
        .then(() => {
          this.isSaving = false;
          this.previousState();
          this.alertService.showSuccess('¡Paciente registrado con éxito!');
        })
        .catch(error => {
          this.isSaving = false;
          const errorData = JSON.stringify(error.response?.data || {}).toLowerCase();

          if (errorData.includes('curp') || errorData.includes('constraint')) {
            this.alertService.showError('Error: El CURP ingresado es incorrecto o ya se encuentra registrado en el sistema.');
          } else {
            this.alertService.showHttpError(error.response);
          }
        });
    },
  },
});