import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import InfoSocioeconomicaService from './info-socioeconomica.service';
import { type IInfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InfoSocioeconomicaDetails',
  setup() {
    const infoSocioeconomicaService = inject('infoSocioeconomicaService', () => new InfoSocioeconomicaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const infoSocioeconomica: Ref<IInfoSocioeconomica> = ref({});

    const retrieveInfoSocioeconomica = async infoSocioeconomicaId => {
      try {
        const res = await infoSocioeconomicaService().find(infoSocioeconomicaId);
        infoSocioeconomica.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.infoSocioeconomicaId) {
      retrieveInfoSocioeconomica(route.params.infoSocioeconomicaId);
    }

    return {
      alertService,
      infoSocioeconomica,

      previousState,
      t$: useI18n().t,
    };
  },
});
