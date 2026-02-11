import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import EntidadFederativaService from './entidad-federativa.service';
import { type IEntidadFederativa } from '@/shared/model/pacientesms/entidad-federativa.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EntidadFederativaDetails',
  setup() {
    const entidadFederativaService = inject('entidadFederativaService', () => new EntidadFederativaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const entidadFederativa: Ref<IEntidadFederativa> = ref({});

    const retrieveEntidadFederativa = async entidadFederativaId => {
      try {
        const res = await entidadFederativaService().find(entidadFederativaId);
        entidadFederativa.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.entidadFederativaId) {
      retrieveEntidadFederativa(route.params.entidadFederativaId);
    }

    return {
      alertService,
      entidadFederativa,

      previousState,
      t$: useI18n().t,
    };
  },
});
