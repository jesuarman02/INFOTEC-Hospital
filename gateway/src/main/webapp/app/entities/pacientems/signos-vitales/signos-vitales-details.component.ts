import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SignosVitalesService from './signos-vitales.service';
import { useDateFormat } from '@/shared/composables';
import { type ISignosVitales } from '@/shared/model/pacientems/signos-vitales.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SignosVitalesDetails',
  setup() {
    const dateFormat = useDateFormat();
    const signosVitalesService = inject('signosVitalesService', () => new SignosVitalesService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const signosVitales: Ref<ISignosVitales> = ref({});

    const retrieveSignosVitales = async signosVitalesId => {
      try {
        const res = await signosVitalesService().find(signosVitalesId);
        signosVitales.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.signosVitalesId) {
      retrieveSignosVitales(route.params.signosVitalesId);
    }

    return {
      ...dateFormat,
      alertService,
      signosVitales,

      previousState,
      t$: useI18n().t,
    };
  },
});
