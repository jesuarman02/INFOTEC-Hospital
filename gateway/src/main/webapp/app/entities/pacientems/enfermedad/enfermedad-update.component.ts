import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import EnfermedadService from './enfermedad.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Enfermedad, type IEnfermedad } from '@/shared/model/pacientems/enfermedad.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EnfermedadUpdate',
  setup() {
    const enfermedadService = inject('enfermedadService', () => new EnfermedadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const enfermedad: Ref<IEnfermedad> = ref(new Enfermedad());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEnfermedad = async enfermedadId => {
      try {
        const res = await enfermedadService().find(enfermedadId);
        enfermedad.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.enfermedadId) {
      retrieveEnfermedad(route.params.enfermedadId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      tipo: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 20 }).toString(), 20),
      },
      codigoCIE: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 10 }).toString(), 10),
      },
    };
    const v$ = useVuelidate(validationRules, enfermedad as any);
    v$.value.$validate();

    return {
      enfermedadService,
      alertService,
      enfermedad,
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
      if (this.enfermedad.id) {
        this.enfermedadService()
          .update(this.enfermedad)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsEnfermedad.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.enfermedadService()
          .create(this.enfermedad)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsEnfermedad.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
