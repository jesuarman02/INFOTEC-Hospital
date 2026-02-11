import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import DireccionService from './direccion.service';
import { type IDireccion } from '@/shared/model/pacientems/direccion.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Direccion',
  setup() {
    const { t: t$ } = useI18n();
    const direccionService = inject('direccionService', () => new DireccionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const direccions: Ref<IDireccion[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveDireccions = async () => {
      isFetching.value = true;
      try {
        const res = await direccionService().retrieve();
        direccions.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveDireccions();
    };

    onMounted(async () => {
      await retrieveDireccions();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IDireccion) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeDireccion = async () => {
      try {
        await direccionService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientemsDireccion.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveDireccions();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      direccions,
      handleSyncList,
      isFetching,
      retrieveDireccions,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeDireccion,
      t$,
    };
  },
});
