import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import EntidadFederativaService from './entidad-federativa.service';
import { type IEntidadFederativa } from '@/shared/model/pacientesms/entidad-federativa.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EntidadFederativa',
  setup() {
    const { t: t$ } = useI18n();
    const entidadFederativaService = inject('entidadFederativaService', () => new EntidadFederativaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const entidadFederativas: Ref<IEntidadFederativa[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveEntidadFederativas = async () => {
      isFetching.value = true;
      try {
        const res = await entidadFederativaService().retrieve();
        entidadFederativas.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveEntidadFederativas();
    };

    onMounted(async () => {
      await retrieveEntidadFederativas();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IEntidadFederativa) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeEntidadFederativa = async () => {
      try {
        await entidadFederativaService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientesmsEntidadFederativa.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveEntidadFederativas();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      entidadFederativas,
      handleSyncList,
      isFetching,
      retrieveEntidadFederativas,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeEntidadFederativa,
      t$,
    };
  },
});
