import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import PacienteEnfermedadService from './paciente-enfermedad.service';
import { type IPacienteEnfermedad } from '@/shared/model/pacientems/paciente-enfermedad.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteEnfermedad',
  setup() {
    const { t: t$ } = useI18n();
    const pacienteEnfermedadService = inject('pacienteEnfermedadService', () => new PacienteEnfermedadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const pacienteEnfermedads: Ref<IPacienteEnfermedad[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrievePacienteEnfermedads = async () => {
      isFetching.value = true;
      try {
        const res = await pacienteEnfermedadService().retrieve();
        pacienteEnfermedads.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrievePacienteEnfermedads();
    };

    onMounted(async () => {
      await retrievePacienteEnfermedads();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IPacienteEnfermedad) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removePacienteEnfermedad = async () => {
      try {
        await pacienteEnfermedadService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientemsPacienteEnfermedad.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrievePacienteEnfermedads();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      pacienteEnfermedads,
      handleSyncList,
      isFetching,
      retrievePacienteEnfermedads,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removePacienteEnfermedad,
      t$,
    };
  },
});
