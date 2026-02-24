import { type Ref, computed, defineComponent, inject, ref } from 'vue';
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
  if (!value) return true; // Si está vacío, la regla "required" se encarga de quejarse
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
        soloLetras: validadorSoloLetras, // <-- NUEVA VALIDACIÓN
      },
      apellidoPaterno: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 60 }).toString(), 60),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 2 }).toString(), 2),
        soloLetras: validadorSoloLetras, // <-- NUEVA VALIDACIÓN
      },
      apellidoMaterno: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 60 }).toString(), 60),
        soloLetras: validadorSoloLetras, // <-- NUEVA VALIDACIÓN
      },
      sexo: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
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
  created(): void {},
  methods: {
    save(): void {
      // 1. Forzar la validación de todo el formulario
      this.v$.$validate();
      
      // 2. Si hay errores, lanzamos la alerta flotante y detenemos el guardado
      if (this.v$.$invalid) {
        this.alertService.showError('Por favor, revise los campos en rojo. Faltan datos o tienen un formato incorrecto.');
        return;
      }

      this.isSaving = true;
      
      // Si todo está bien, lo mandamos a Java
// Si todo está bien, lo mandamos a Java
      if (this.paciente.id) {
        this.pacienteService()
          .update(this.paciente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            // CAMBIO AQUÍ: Mensaje directo en español
            this.alertService.showInfo('¡Los datos del paciente se han actualizado correctamente!');
          })
          // ... (catch se queda igual)
      } else {
        this.pacienteService()
          .create(this.paciente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            // CAMBIO AQUÍ: Mensaje directo en español
            this.alertService.showSuccess('¡Paciente registrado con éxito!');
          })
          // ... (catch se queda igual)
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});