import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PacienteAlergiaService from './paciente-alergia.service';
import { type IPacienteAlergia } from '@/shared/model/pacientems/paciente-alergia.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteAlergiaDetails',
  setup() {
    const pacienteAlergiaService = inject('pacienteAlergiaService', () => new PacienteAlergiaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const pacienteAlergia: Ref<IPacienteAlergia> = ref({});

    const retrievePacienteAlergia = async pacienteAlergiaId => {
      try {
        const res = await pacienteAlergiaService().find(pacienteAlergiaId);
        pacienteAlergia.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteAlergiaId) {
      retrievePacienteAlergia(route.params.pacienteAlergiaId);
    }

    return {
      alertService,
      pacienteAlergia,

      previousState,
      t$: useI18n().t,
    };
  },
});
