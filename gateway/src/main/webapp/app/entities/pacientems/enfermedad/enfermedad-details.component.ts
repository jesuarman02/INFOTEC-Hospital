import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import EnfermedadService from './enfermedad.service';
import { type IEnfermedad } from '@/shared/model/pacientems/enfermedad.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EnfermedadDetails',
  setup() {
    const enfermedadService = inject('enfermedadService', () => new EnfermedadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const enfermedad: Ref<IEnfermedad> = ref({});

    const retrieveEnfermedad = async enfermedadId => {
      try {
        const res = await enfermedadService().find(enfermedadId);
        enfermedad.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.enfermedadId) {
      retrieveEnfermedad(route.params.enfermedadId);
    }

    return {
      alertService,
      enfermedad,

      previousState,
      t$: useI18n().t,
    };
  },
});
