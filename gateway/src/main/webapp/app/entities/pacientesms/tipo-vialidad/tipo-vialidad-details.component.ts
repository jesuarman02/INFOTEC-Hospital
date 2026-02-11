import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import TipoVialidadService from './tipo-vialidad.service';
import { type ITipoVialidad } from '@/shared/model/pacientesms/tipo-vialidad.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TipoVialidadDetails',
  setup() {
    const tipoVialidadService = inject('tipoVialidadService', () => new TipoVialidadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const tipoVialidad: Ref<ITipoVialidad> = ref({});

    const retrieveTipoVialidad = async tipoVialidadId => {
      try {
        const res = await tipoVialidadService().find(tipoVialidadId);
        tipoVialidad.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tipoVialidadId) {
      retrieveTipoVialidad(route.params.tipoVialidadId);
    }

    return {
      alertService,
      tipoVialidad,

      previousState,
      t$: useI18n().t,
    };
  },
});
