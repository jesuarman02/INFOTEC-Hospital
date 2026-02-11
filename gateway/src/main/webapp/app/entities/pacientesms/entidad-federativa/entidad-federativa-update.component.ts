import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import EntidadFederativaService from './entidad-federativa.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { EntidadFederativa, type IEntidadFederativa } from '@/shared/model/pacientesms/entidad-federativa.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EntidadFederativaUpdate',
  setup() {
    const entidadFederativaService = inject('entidadFederativaService', () => new EntidadFederativaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const entidadFederativa: Ref<IEntidadFederativa> = ref(new EntidadFederativa());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEntidadFederativa = async entidadFederativaId => {
      try {
        const res = await entidadFederativaService().find(entidadFederativaId);
        entidadFederativa.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.entidadFederativaId) {
      retrieveEntidadFederativa(route.params.entidadFederativaId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      clave: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
      },
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      abreviatura: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 10 }).toString(), 10),
      },
    };
    const v$ = useVuelidate(validationRules, entidadFederativa as any);
    v$.value.$validate();

    return {
      entidadFederativaService,
      alertService,
      entidadFederativa,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.entidadFederativa.id) {
        this.entidadFederativaService()
          .update(this.entidadFederativa)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientesmsEntidadFederativa.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.entidadFederativaService()
          .create(this.entidadFederativa)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientesmsEntidadFederativa.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
