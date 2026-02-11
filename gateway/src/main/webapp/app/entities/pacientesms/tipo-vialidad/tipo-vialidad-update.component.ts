import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TipoVialidadService from './tipo-vialidad.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ITipoVialidad, TipoVialidad } from '@/shared/model/pacientesms/tipo-vialidad.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TipoVialidadUpdate',
  setup() {
    const tipoVialidadService = inject('tipoVialidadService', () => new TipoVialidadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tipoVialidad: Ref<ITipoVialidad> = ref(new TipoVialidad());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTipoVialidad = async tipoVialidadId => {
      try {
        const res = await tipoVialidadService().find(tipoVialidadId);
        tipoVialidad.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tipoVialidadId) {
      retrieveTipoVialidad(route.params.tipoVialidadId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
    };
    const v$ = useVuelidate(validationRules, tipoVialidad as any);
    v$.value.$validate();

    return {
      tipoVialidadService,
      alertService,
      tipoVialidad,
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
      if (this.tipoVialidad.id) {
        this.tipoVialidadService()
          .update(this.tipoVialidad)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientesmsTipoVialidad.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.tipoVialidadService()
          .create(this.tipoVialidad)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientesmsTipoVialidad.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
