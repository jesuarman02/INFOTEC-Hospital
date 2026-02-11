import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CodigoPostalService from './codigo-postal.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { CodigoPostal, type ICodigoPostal } from '@/shared/model/pacientesms/codigo-postal.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CodigoPostalUpdate',
  setup() {
    const codigoPostalService = inject('codigoPostalService', () => new CodigoPostalService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const codigoPostal: Ref<ICodigoPostal> = ref(new CodigoPostal());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCodigoPostal = async codigoPostalId => {
      try {
        const res = await codigoPostalService().find(codigoPostalId);
        codigoPostal.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.codigoPostalId) {
      retrieveCodigoPostal(route.params.codigoPostalId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      codigo: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
      },
      asentamiento: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      tipoAsentamiento: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      municipio: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      estado: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      ciudad: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      codigoPostalAdministracion: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
      },
      claveEstado: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
      },
      claveOficina: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
      },
      claveCP: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
      },
      claveTipoAsentamiento: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
      },
      claveMunicipio: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
      },
      idAsentamientoCons: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 10 }).toString(), 10),
      },
      zona: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      claveCiudad: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 5 }).toString(), 5),
      },
    };
    const v$ = useVuelidate(validationRules, codigoPostal as any);
    v$.value.$validate();

    return {
      codigoPostalService,
      alertService,
      codigoPostal,
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
      if (this.codigoPostal.id) {
        this.codigoPostalService()
          .update(this.codigoPostal)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientesmsCodigoPostal.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.codigoPostalService()
          .create(this.codigoPostal)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientesmsCodigoPostal.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
