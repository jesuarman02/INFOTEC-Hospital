import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PacienteService from './paciente.service';
// 1. IMPORTAMOS EL SERVICIO DE DIRECCIÓN
import DireccionService from '../direccion/direccion.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteDetails',
  setup() {
    const pacienteService = inject('pacienteService', () => new PacienteService());
    // 2. INYECTAMOS EL SERVICIO PARA PODER USARLO
    const direccionService = inject('direccionService', () => new DireccionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const paciente: Ref<IPaciente> = ref({});
    // Variables para colapsar las tarjetas
    const mostrarDatosPersonales = ref(true);
    const mostrarDatosContacto = ref(true);

     const retrievePaciente = async (pacienteId: number) => {
        try {
        const res = await pacienteService().find(pacienteId);
        paciente.value = res;
        
        // --- 3. MAGIA PARA TRAER LA DIRECCIÓN COMPLETA ---
        if (paciente.value.direccion && paciente.value.direccion.id) {
          // Buscamos la dirección completa usando su ID
          const resDireccion = await direccionService().find(paciente.value.direccion.id);
          // Reemplazamos la dirección "resumida" por la completa
          paciente.value.direccion = resDireccion;
        }
        // --------------------------------------------------

      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteId) {
      retrievePaciente(Number(route.params.pacienteId));
    }

    return {
      alertService,
      paciente,
      previousState,
      t$: useI18n().t,
      mostrarDatosPersonales,
      mostrarDatosContacto,
    };
  },
});