import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import EnfermedadService from './enfermedad.service';
import { type IEnfermedad } from '@/shared/model/pacientems/enfermedad.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Enfermedad',
  setup() {
    const { t: t$ } = useI18n();
    const enfermedadService = inject('enfermedadService', () => new EnfermedadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const enfermedads: Ref<IEnfermedad[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveEnfermedads = async () => {
      isFetching.value = true;
      try {
        const res = await enfermedadService().retrieve();
        enfermedads.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveEnfermedads();
    };

    onMounted(async () => {
      await retrieveEnfermedads();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IEnfermedad) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeEnfermedad = async () => {
      try {
        await enfermedadService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientemsEnfermedad.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveEnfermedads();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      enfermedads,
      handleSyncList,
      isFetching,
      retrieveEnfermedads,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeEnfermedad,
      t$,
    };
  },
});
