import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import HistorialMedicoService from './historial-medico.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IHistorialMedico } from '@/shared/model/pacientems/historial-medico.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistorialMedicoDetails',
  setup() {
    const historialMedicoService = inject('historialMedicoService', () => new HistorialMedicoService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const dataUtils = useDataUtils();
    const route = useRoute();
    const router = useRouter();

    const historialMedico: Ref<IHistorialMedico> = ref({});

    const retrieveHistorialMedico = async (historialMedicoId: number) => {
      try {
        const res = await historialMedicoService().find(historialMedicoId);
        historialMedico.value = res;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.historialMedicoId) {
      retrieveHistorialMedico(Number(route.params.historialMedicoId));
    }

    // FUNCIÓN MÁGICA: Para que los JSON no se vean feos en la vista
    const parsearJSON = (valor: string | null | undefined) => {
      if (!valor) return [];
      try {
        const data = JSON.parse(valor);
        return Array.isArray(data) ? data : [data];
      } catch (e) {
        return [{ error: 'Texto simple', contenido: valor }];
      }
    };

    // Para el objeto de Biométricos que es un JSON único, no una lista
    const obtenerBiometricos = (valor: string | null | undefined) => {
      if (!valor) return {};
      try {
        return JSON.parse(valor);
      } catch (e) {
        return {};
      }
    };

    // ... (código existente de buscar el historial) ...

    // Lee JSONs planos como Biométricos y Hábitos
    const obtenerJsonPlano = (jsonStr: string | null | undefined) => {
      if (!jsonStr) return {};
      try { return JSON.parse(jsonStr); } catch (e) { return {}; }
    };

    // Lee las listas que estructuramos con el helper { tiene: boolean, detalles: [] }
    const parsearLista = (jsonStr: string | null | undefined) => {
      if (!jsonStr) return { tiene: false, detalles: [] };
      try { 
        const parsed = JSON.parse(jsonStr);
        // Compatibilidad por si acaso es un arreglo directo
        if (Array.isArray(parsed)) return { tiene: parsed.length > 0, detalles: parsed };
        return parsed; 
      } catch (e) { 
        return { tiene: false, detalles: [] }; 
      }
    };

    const previousState = () => router.go(-1);

return {
      alertService,
      historialMedico,
      obtenerJsonPlano, // <-- IMPORTANTE QUE ESTÉN AQUÍ
      parsearLista,     // <-- IMPORTANTE QUE ESTÉN AQUÍ
      ...dataUtils,
      previousState,
      t$: useI18n().t,
    };
  },
});