import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import TipoVialidadService from './tipo-vialidad.service';
import { type ITipoVialidad } from '@/shared/model/pacientesms/tipo-vialidad.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TipoVialidad',
  setup() {
    const { t: t$ } = useI18n();
    const tipoVialidadService = inject('tipoVialidadService', () => new TipoVialidadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const tipoVialidads: Ref<ITipoVialidad[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveTipoVialidads = async () => {
      isFetching.value = true;
      try {
        const res = await tipoVialidadService().retrieve();
        tipoVialidads.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveTipoVialidads();
    };

    onMounted(async () => {
      await retrieveTipoVialidads();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ITipoVialidad) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeTipoVialidad = async () => {
      try {
        await tipoVialidadService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientesmsTipoVialidad.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveTipoVialidads();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      tipoVialidads,
      handleSyncList,
      isFetching,
      retrieveTipoVialidads,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeTipoVialidad,
      t$,
    };
  },
});
