import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import HistorialMedicoService from './historial-medico.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IHistorialMedico } from '@/shared/model/pacientems/historial-medico.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistorialMedicoDetails',
  setup() {
    const historialMedicoService = inject('historialMedicoService', () => new HistorialMedicoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const historialMedico: Ref<IHistorialMedico> = ref({});

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

    return {
      alertService,
      historialMedico,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});
