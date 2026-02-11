import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AlergiaService from './alergia.service';
import { type IAlergia } from '@/shared/model/pacientems/alergia.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AlergiaDetails',
  setup() {
    const alergiaService = inject('alergiaService', () => new AlergiaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const alergia: Ref<IAlergia> = ref({});

    const retrieveAlergia = async alergiaId => {
      try {
        const res = await alergiaService().find(alergiaId);
        alergia.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.alergiaId) {
      retrieveAlergia(route.params.alergiaId);
    }

    return {
      alertService,
      alergia,

      previousState,
      t$: useI18n().t,
    };
  },
});
