import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PacienteMedicamentoService from './paciente-medicamento.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import MedicamentoService from '@/entities/pacientems/medicamento/medicamento.service';
import { type IMedicamento } from '@/shared/model/pacientems/medicamento.model';
import { type IPacienteMedicamento, PacienteMedicamento } from '@/shared/model/pacientems/paciente-medicamento.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteMedicamentoUpdate',
  setup() {
    const pacienteMedicamentoService = inject('pacienteMedicamentoService', () => new PacienteMedicamentoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const pacienteMedicamento: Ref<IPacienteMedicamento> = ref(new PacienteMedicamento());

    const pacienteService = inject('pacienteService', () => new PacienteService());

    const pacientes: Ref<IPaciente[]> = ref([]);

    const medicamentoService = inject('medicamentoService', () => new MedicamentoService());

    const medicamentos: Ref<IMedicamento[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePacienteMedicamento = async pacienteMedicamentoId => {
      try {
        const res = await pacienteMedicamentoService().find(pacienteMedicamentoId);
        pacienteMedicamento.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteMedicamentoId) {
      retrievePacienteMedicamento(route.params.pacienteMedicamentoId);
    }

    const initRelationships = () => {
      pacienteService()
        .retrieve()
        .then(res => {
          pacientes.value = res.data;
        });
      medicamentoService()
        .retrieve()
        .then(res => {
          medicamentos.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      dosis: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      frecuencia: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      fechaInicio: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      fechaFin: {},
      activo: {},
      paciente: {},
      medicamento: {},
    };
    const v$ = useVuelidate(validationRules, pacienteMedicamento as any);
    v$.value.$validate();

    return {
      pacienteMedicamentoService,
      alertService,
      pacienteMedicamento,
      previousState,
      isSaving,
      currentLanguage,
      pacientes,
      medicamentos,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.pacienteMedicamento.id) {
        this.pacienteMedicamentoService()
          .update(this.pacienteMedicamento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsPacienteMedicamento.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.pacienteMedicamentoService()
          .create(this.pacienteMedicamento)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsPacienteMedicamento.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
