import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import HistorialMedicoService from './historial-medico.service';
import { type IHistorialMedico } from '@/shared/model/pacientems/historial-medico.model';
import useDataUtils from '@/shared/data/data-utils.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistorialMedico',
  setup() {
    const { t: t$ } = useI18n();
    const dataUtils = useDataUtils();
    const historialMedicoService = inject('historialMedicoService', () => new HistorialMedicoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const historialMedicos: Ref<IHistorialMedico[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveHistorialMedicos = async () => {
      isFetching.value = true;
      try {
        const res = await historialMedicoService().retrieve();
        historialMedicos.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveHistorialMedicos();
    };

    onMounted(async () => {
      await retrieveHistorialMedicos();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IHistorialMedico) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeHistorialMedico = async () => {
      try {
        await historialMedicoService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientemsHistorialMedico.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveHistorialMedicos();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      historialMedicos,
      handleSyncList,
      isFetching,
      retrieveHistorialMedicos,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeHistorialMedico,
      t$,
      ...dataUtils,
    };
  },
});
