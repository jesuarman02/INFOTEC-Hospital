import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PacienteMedicamentoService from './paciente-medicamento.service';
import { type IPacienteMedicamento } from '@/shared/model/pacientems/paciente-medicamento.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteMedicamentoDetails',
  setup() {
    const pacienteMedicamentoService = inject('pacienteMedicamentoService', () => new PacienteMedicamentoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const pacienteMedicamento: Ref<IPacienteMedicamento> = ref({});

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

    return {
      alertService,
      pacienteMedicamento,

      previousState,
      t$: useI18n().t,
    };
  },
});
