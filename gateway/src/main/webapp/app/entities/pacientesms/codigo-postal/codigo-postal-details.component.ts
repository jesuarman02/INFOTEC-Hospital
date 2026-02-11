import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CodigoPostalService from './codigo-postal.service';
import { type ICodigoPostal } from '@/shared/model/pacientesms/codigo-postal.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CodigoPostalDetails',
  setup() {
    const codigoPostalService = inject('codigoPostalService', () => new CodigoPostalService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const codigoPostal: Ref<ICodigoPostal> = ref({});

    const retrieveCodigoPostal = async codigoPostalId => {
      try {
        const res = await codigoPostalService().find(codigoPostalId);
        codigoPostal.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.codigoPostalId) {
      retrieveCodigoPostal(route.params.codigoPostalId);
    }

    return {
      alertService,
      codigoPostal,

      previousState,
      t$: useI18n().t,
    };
  },
});
