import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import HistorialMedicoService from './historial-medico.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { HistorialMedico, type IHistorialMedico } from '@/shared/model/pacientems/historial-medico.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistorialMedicoUpdate',
  setup() {
    const historialMedicoService = inject('historialMedicoService', () => new HistorialMedicoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const historialMedico: Ref<IHistorialMedico> = ref(new HistorialMedico());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveHistorialMedico = async historialMedicoId => {
      try {
        const res = await historialMedicoService().find(historialMedicoId);
        historialMedico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.historialMedicoId) {
      retrieveHistorialMedico(route.params.historialMedicoId);
    }

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      antecedentesQuirurgicos: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 500 }).toString(), 500),
      },
      esquemaVacunacion: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      habitos: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      observacionesGenerales: {},
    };
    const v$ = useVuelidate(validationRules, historialMedico as any);
    v$.value.$validate();

    return {
      historialMedicoService,
      alertService,
      historialMedico,
      previousState,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.historialMedico.id) {
        this.historialMedicoService()
          .update(this.historialMedico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsHistorialMedico.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.historialMedicoService()
          .create(this.historialMedico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsHistorialMedico.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
