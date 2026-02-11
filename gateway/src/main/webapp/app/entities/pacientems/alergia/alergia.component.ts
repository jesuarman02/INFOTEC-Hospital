import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import AlergiaService from './alergia.service';
import { type IAlergia } from '@/shared/model/pacientems/alergia.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Alergia',
  setup() {
    const { t: t$ } = useI18n();
    const alergiaService = inject('alergiaService', () => new AlergiaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const alergias: Ref<IAlergia[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveAlergias = async () => {
      isFetching.value = true;
      try {
        const res = await alergiaService().retrieve();
        alergias.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveAlergias();
    };

    onMounted(async () => {
      await retrieveAlergias();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IAlergia) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeAlergia = async () => {
      try {
        await alergiaService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientemsAlergia.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveAlergias();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      alergias,
      handleSyncList,
      isFetching,
      retrieveAlergias,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeAlergia,
      t$,
    };
  },
});
