import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MedicamentoService from './medicamento.service';
import { type IMedicamento } from '@/shared/model/pacientems/medicamento.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MedicamentoDetails',
  setup() {
    const medicamentoService = inject('medicamentoService', () => new MedicamentoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const medicamento: Ref<IMedicamento> = ref({});

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

    return {
      alertService,
      medicamento,

      previousState,
      t$: useI18n().t,
    };
  },
});
