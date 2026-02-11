import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PacienteEnfermedadService from './paciente-enfermedad.service';
import { type IPacienteEnfermedad } from '@/shared/model/pacientems/paciente-enfermedad.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteEnfermedadDetails',
  setup() {
    const pacienteEnfermedadService = inject('pacienteEnfermedadService', () => new PacienteEnfermedadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const pacienteEnfermedad: Ref<IPacienteEnfermedad> = ref({});

    const retrievePacienteEnfermedad = async pacienteEnfermedadId => {
      try {
        const res = await pacienteEnfermedadService().find(pacienteEnfermedadId);
        pacienteEnfermedad.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteEnfermedadId) {
      retrievePacienteEnfermedad(route.params.pacienteEnfermedadId);
    }

    return {
      alertService,
      pacienteEnfermedad,

      previousState,
      t$: useI18n().t,
    };
  },
});
