import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import DireccionService from './direccion.service';
import { type IDireccion } from '@/shared/model/pacientems/direccion.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DireccionDetails',
  setup() {
    const direccionService = inject('direccionService', () => new DireccionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const direccion: Ref<IDireccion> = ref({});

    const retrieveDireccion = async direccionId => {
      try {
        const res = await direccionService().find(direccionId);
        direccion.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.direccionId) {
      retrieveDireccion(route.params.direccionId);
    }

    return {
      alertService,
      direccion,

      previousState,
      t$: useI18n().t,
    };
  },
});
