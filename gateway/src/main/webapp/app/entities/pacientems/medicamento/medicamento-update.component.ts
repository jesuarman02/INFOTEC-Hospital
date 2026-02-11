import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MedicamentoService from './medicamento.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IMedicamento, Medicamento } from '@/shared/model/pacientems/medicamento.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MedicamentoUpdate',
  setup() {
    const medicamentoService = inject('medicamentoService', () => new MedicamentoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const medicamento: Ref<IMedicamento> = ref(new Medicamento());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMedicamento = async medicamentoId => {
      try {
        const res = await medicamentoService().find(medicamentoId);
        medicamento.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.medicamentoId) {
      retrieveMedicamento(route.params.medicamentoId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      ingredienteActivo: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      presentacion: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
    };
    const v$ = useVuelidate(validationRules, medicamento as any);
    v$.value.$validate();

    return {
      medicamentoService,
      alertService,
      medicamento,
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
      if (this.medicamento.id) {
        this.medicamentoService()
          .update(this.medicamento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsMedicamento.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.medicamentoService()
          .create(this.medicamento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsMedicamento.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
