import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import InfoSocioeconomicaService from './info-socioeconomica.service';
import { type IInfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InfoSocioeconomica',
  setup() {
    const { t: t$ } = useI18n();
    const infoSocioeconomicaService = inject('infoSocioeconomicaService', () => new InfoSocioeconomicaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const infoSocioeconomicas: Ref<IInfoSocioeconomica[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveInfoSocioeconomicas = async () => {
      isFetching.value = true;
      try {
        const res = await infoSocioeconomicaService().retrieve();
        infoSocioeconomicas.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveInfoSocioeconomicas();
    };

    onMounted(async () => {
      await retrieveInfoSocioeconomicas();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IInfoSocioeconomica) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeInfoSocioeconomica = async () => {
      try {
        await infoSocioeconomicaService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientemsInfoSocioeconomica.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveInfoSocioeconomicas();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      infoSocioeconomicas,
      handleSyncList,
      isFetching,
      retrieveInfoSocioeconomicas,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeInfoSocioeconomica,
      t$,
    };
  },
});
