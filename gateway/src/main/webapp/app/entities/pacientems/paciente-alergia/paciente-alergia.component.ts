import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import PacienteAlergiaService from './paciente-alergia.service';
import { type IPacienteAlergia } from '@/shared/model/pacientems/paciente-alergia.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteAlergia',
  setup() {
    const { t: t$ } = useI18n();
    const pacienteAlergiaService = inject('pacienteAlergiaService', () => new PacienteAlergiaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const pacienteAlergias: Ref<IPacienteAlergia[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrievePacienteAlergias = async () => {
      isFetching.value = true;
      try {
        const res = await pacienteAlergiaService().retrieve();
        pacienteAlergias.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrievePacienteAlergias();
    };

    onMounted(async () => {
      await retrievePacienteAlergias();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IPacienteAlergia) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removePacienteAlergia = async () => {
      try {
        await pacienteAlergiaService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientemsPacienteAlergia.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrievePacienteAlergias();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      pacienteAlergias,
      handleSyncList,
      isFetching,
      retrievePacienteAlergias,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removePacienteAlergia,
      t$,
    };
  },
});
