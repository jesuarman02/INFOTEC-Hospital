import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AlergiaService from './alergia.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Alergia, type IAlergia } from '@/shared/model/pacientems/alergia.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AlergiaUpdate',
  setup() {
    const alergiaService = inject('alergiaService', () => new AlergiaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const alergia: Ref<IAlergia> = ref(new Alergia());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAlergia = async alergiaId => {
      try {
        const res = await alergiaService().find(alergiaId);
        alergia.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.alergiaId) {
      retrieveAlergia(route.params.alergiaId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      descripcion: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
    };
    const v$ = useVuelidate(validationRules, alergia as any);
    v$.value.$validate();

    return {
      alergiaService,
      alertService,
      alergia,
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
      if (this.alergia.id) {
        this.alergiaService()
          .update(this.alergia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsAlergia.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.alergiaService()
          .create(this.alergia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsAlergia.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
