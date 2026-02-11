import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SignosVitalesService from './signos-vitales.service';
import { useDateFormat, useValidation } from '@/shared/composables';
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

    const retrieveSignosVitales = async signosVitalesId => {
      try {
        const res = await signosVitalesService().find(signosVitalesId);
        res.fechaRegistro = new Date(res.fechaRegistro);
        signosVitales.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.signosVitalesId) {
      retrieveSignosVitales(route.params.signosVitalesId);
    }

    const initRelationships = () => {
      pacienteService()
        .retrieve()
        .then(res => {
          pacientes.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fechaRegistro: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      frecuenciaCardiaca: {},
      tensionArterial: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 10 }).toString(), 10),
      },
      frecuenciaRespiratoria: {},
      temperatura: {},
      saturacionOxigeno: {},
      peso: {},
      estatura: {},
      imc: {},
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
      v$,
      ...useDateFormat({ entityRef: signosVitales }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
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
