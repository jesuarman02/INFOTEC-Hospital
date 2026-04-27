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
    const signosVitales: Ref<ISignosVitales> = ref({} as ISignosVitales);

    const retrieveSignosVitales = async (signosVitalesId: any) => {
      try {
        const res = await signosVitalesService().find(signosVitalesId);
        signosVitales.value = res;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.signosVitalesId) {
      retrieveSignosVitales(route.params.signosVitalesId);
    }

    // 🔥 FUNCIONES DE DISEÑO (Colores y Textos) 🔥
    const getColorSaturacion = (spo2: number | null | undefined) => {
      if (spo2 === null || spo2 === undefined) return '';
      if (spo2 < 90) return 'text-danger font-weight-bold'; // Hipoxemia severa
      if (spo2 >= 90 && spo2 <= 94) return 'text-warning font-weight-bold'; // Hipoxemia leve
      return 'text-success'; // Normal
    };

    const getBgColorDolor = (valor: number | null | undefined) => {
      if (valor === null || valor === undefined) return 'bg-secondary text-white';
      if (valor === 0) return 'bg-success text-white';
      if (valor >= 1 && valor <= 3) return 'bg-info text-white';
      if (valor >= 4 && valor <= 6) return 'bg-warning text-dark';
      if (valor >= 7 && valor <= 9) return 'bg-danger text-white';
      if (valor === 10) return 'bg-dark text-white';
      return '';
    };

    const getTextColorDolor = (valor: number | null | undefined) => {
      if (valor === null || valor === undefined) return 'text-muted';
      if (valor === 0) return 'text-success';
      if (valor >= 1 && valor <= 3) return 'text-info';
      if (valor >= 4 && valor <= 6) return 'text-warning';
      if (valor >= 7 && valor <= 9) return 'text-danger';
      if (valor === 10) return 'text-dark';
      return '';
    };

    const getTextoDolor = (valor: number | null | undefined) => {
      if (valor === null || valor === undefined) return 'No evaluado';
      if (valor === 0) return 'Sin dolor';
      if (valor >= 1 && valor <= 3) return 'Dolor Leve';
      if (valor >= 4 && valor <= 6) return 'Dolor Moderado';
      if (valor >= 7 && valor <= 9) return 'Dolor Intenso';
      if (valor === 10) return 'Dolor Insuperable';
      return '';
    };

    return {
      ...dateFormat,
      alertService,
      signosVitales,
      previousState,
      t$: useI18n().t,
      // Retornamos las funciones para que el HTML pueda usarlas
      getColorSaturacion,
      getBgColorDolor,
      getTextColorDolor,
      getTextoDolor
    };
  },
});