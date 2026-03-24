import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SignosVitalesService from './signos-vitales.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import { type ISignosVitales, SignosVitales } from '@/shared/model/pacientems/signos-vitales.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SignosVitalesUpdate',
  setup() {
    const signosVitalesService = inject('signosVitalesService', () => new SignosVitalesService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const signosVitales: Ref<ISignosVitales> = ref(new SignosVitales());
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const pacientes: Ref<IPaciente[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();
    const previousState = () => router.go(-1);

    const retrieveSignosVitales = async (signosVitalesId: number) => {
      try {
        const res = await signosVitalesService().find(signosVitalesId);
        if (res.fechaRegistro) {
          res.fechaRegistro = new Date(res.fechaRegistro);
        }
        signosVitales.value = res;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.signosVitalesId) {
      retrieveSignosVitales(Number(route.params.signosVitalesId as string));
    }

    const initRelationships = () => {
      pacienteService()
        .retrieve()
        .then(res => {
          pacientes.value = res.data;
        });
    };
    initRelationships();

    // 💥 SOLUCIÓN 1: Búsqueda en vivo del paciente por ECU
    const pacienteEncontrado = computed(() => {
      if (!signosVitales.value.pacienteEcu) return null;
      return pacientes.value.find(p => p.ecu === signosVitales.value.pacienteEcu) || null;
    });

    // 💥 SOLUCIÓN 3: Adiós al error de updateInstantField. Manejo nativo de fechas.
    const fechaRegistroLocal = computed({
      get: () => {
        if (!signosVitales.value.fechaRegistro) return '';
        const date = new Date(signosVitales.value.fechaRegistro);
        const offset = date.getTimezoneOffset() * 60000;
        const localDate = new Date(date.getTime() - offset);
        return localDate.toISOString().slice(0, 16);
      },
      set: (val: string) => {
        signosVitales.value.fechaRegistro = val ? new Date(val) : undefined;
      }
    });

    const { t: t$ } = useI18n();
    const validations = useValidation();
    
    // 💥 SOLUCIÓN 2: Validaciones estrictas
    const validationRules = {
      fechaRegistro: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      frecuenciaCardiaca: {
        min: validations.minValue('Mínimo 0', 0),
        max: validations.maxValue('Máximo 300', 300),
      },
      tensionArterial: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 10 }).toString(), 10),
      },
      frecuenciaRespiratoria: {
        min: validations.minValue('Mínimo 0', 0),
        max: validations.maxValue('Máximo 100', 100),
      },
      temperatura: {
        min: validations.minValue('Mínimo 20', 20),
        max: validations.maxValue('Máximo 45', 45),
      },
      saturacionOxigeno: {
        min: validations.minValue('Mínimo 0', 0),
        max: validations.maxValue('Máximo 100', 100),
      },
      glucosa: {
        min: validations.minValue('Mínimo 0', 0),
        max: validations.maxValue('Máximo 1000', 1000),
      },
      dolor: {
        min: validations.minValue('Mínimo 0', 0),
        max: validations.maxValue('Máximo 10', 10),
      },
      pacienteEcu: {},
      tipo: {},
      personal: {},
      estadoConciencia: {},
      observaciones: {},
      paciente: {},
    };
    
    const v$ = useVuelidate(validationRules, signosVitales as any);
    v$.value.$validate();

    return {
      signosVitalesService,
      alertService,
      signosVitales,
      previousState,
      isSaving,
      currentLanguage,
      pacientes,
      pacienteEncontrado,
      fechaRegistroLocal,
      v$,
      t$,
    };
  },
  methods: {
    save(): void {
      this.isSaving = true;

      // Al guardar, asignamos los datos del paciente encontrado al payload
      if (this.pacienteEncontrado) {
        this.signosVitales.pacienteNombre = this.pacienteEncontrado.nombre;
        this.signosVitales.pacienteApellidoPaterno = this.pacienteEncontrado.apellidoPaterno;
        this.signosVitales.paciente = { id: this.pacienteEncontrado.id };
      }

      if (this.signosVitales.id) {
        this.signosVitalesService()
          .update(this.signosVitales)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsSignosVitales.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.signosVitalesService()
          .create(this.signosVitales)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsSignosVitales.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});