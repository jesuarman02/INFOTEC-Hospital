import { type Ref, defineComponent, inject, onMounted, ref, computed } from 'vue';
import { useI18n } from 'vue-i18n';

import InfoSocioeconomicaService from './info-socioeconomica.service';
import { type IInfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';
import { useAlertService } from '@/shared/alert/alert.service';
import PacienteService from '../paciente/paciente.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InfoSocioeconomica',
  setup() {
    const { t: t$ } = useI18n();
    const infoSocioeconomicaService = inject('infoSocioeconomicaService', () => new InfoSocioeconomicaService());
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const infoSocioeconomicas: Ref<IInfoSocioeconomica[]> = ref([]);
    const pacientes: Ref<any[]> = ref([]); 
    const isFetching = ref(false);

    const ordenCorrecto = [
      'DOCUMENTO_IDENTIDAD', 'TIPO_VIVIENDA', 'NUM_HOMBRES', 'NUM_MUJERES', 'JEFE_HOGAR',
      'TIENE_TELEFONO', 'NUMERO_FIJO', 'NUMERO_RECADOS', 'NUMERO_CELULAR',
      'PADECE_ENFERMEDAD', 'INSTITUCION_MEDICA', 'HABLA_LENGUA_INDIGENA',
      'HABLA_ESPANOL', 'CONSIDERA_INDIGENA', 'SABE_LEER_ESCRIBIR', 'ESTUDIA',
      'GRADO_MAXIMO_ESTUDIOS', 'CONDICION_LABORAL', 'MAYOR_FUENTE_INGRESO',
      'TIEMPO_EMPLEADO', 'INGRESO_MENSUAL', 'ES_JUBILADO_PENSIONADO',
      'NUMERO_HABITACIONES', 'CUARTOS_DORMIR', 'MATERIAL_VIVIENDA',
      'MATERIAL_TECHO', 'MATERIAL_PAREDES', 'TIPO_BANO', 'TIPO_DRENAJE',
      'MANEJO_BASURA', 'FUENTE_LUZ_ELECTRICA', 'COMBUSTIBLE_COCINA', 'TENENCIA_VIVIENDA'
    ];

    const clear = () => {};

    const retrieveInfoSocioeconomicas = async () => {
      isFetching.value = true;
      try {
        const resPacientes = await pacienteService().retrieve();
        pacientes.value = resPacientes.data;

        const res = await infoSocioeconomicaService().retrieve();
        let datosSocio = res.data;

        datosSocio.sort((a: any, b: any) => {
          if (a.pacienteId !== b.pacienteId) {
            return (a.pacienteId || 0) - (b.pacienteId || 0);
          }
          let indiceA = ordenCorrecto.indexOf(a.pregunta || '');
          let indiceB = ordenCorrecto.indexOf(b.pregunta || '');
          if (indiceA === -1) indiceA = 999;
          if (indiceB === -1) indiceB = 999;
          return indiceA - indiceB;
        });

        infoSocioeconomicas.value = datosSocio;
      } catch (err: any) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const formatoEcuPaciente = (pacienteId: number | undefined) => {
      if (!pacienteId) return '';
      const pacienteEncontrado = pacientes.value.find(p => p.id === pacienteId);
      if (pacienteEncontrado) {
        return `${pacienteEncontrado.ecu} - ${pacienteEncontrado.nombre || ''} ${pacienteEncontrado.apellidoPaterno || ''}`.trim();
      }
      return pacienteId; 
    };

    const handleSyncList = () => {
      retrieveInfoSocioeconomicas();
    };

    onMounted(async () => {
      await retrieveInfoSocioeconomicas();
    });

 const removeId: Ref<number | null> = ref(null); 
    const idsAEliminar: Ref<number[]> = ref([]); // <-- Aquí guardaremos las 30 IDs
    const removeEntity = ref<any>(null);
    
    // Le pasamos todo el grupo del paciente
    const prepareRemoveEstudio = (grupo: any) => {
      removeId.value = grupo.pacienteId; 
      idsAEliminar.value = grupo.respuestas.map((r: any) => r.id); // Guardamos la lista completa
      removeEntity.value.show();
    };
    
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    
    const removeInfoSocioeconomica = async () => {
      try {
        if (idsAEliminar.value.length > 0) {
            // Disparamos un borrado masivo con Promise.all (rápido y limpio)
            const promesasDeBorrado = idsAEliminar.value.map(id => infoSocioeconomicaService().delete(id));
            await Promise.all(promesasDeBorrado);

            alertService.showInfo('Estudio socioeconómico eliminado por completo.', { variant: 'danger' });
            removeId.value = null;
            idsAEliminar.value = [];
            retrieveInfoSocioeconomicas();
            closeDialog();
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    // --- NUEVA LÓGICA DE AGRUPACIÓN Y ACORDEÓN ---
    const pacientesExpandidos = ref<Record<number, boolean>>({});

    const togglePaciente = (pacienteId: number) => {
      pacientesExpandidos.value[pacienteId] = !pacientesExpandidos.value[pacienteId];
    };

    // Agrupamos la lista gigante en pequeños "paquetes" por Paciente
    const infoAgrupada = computed(() => {
      const grupos: Record<number, any> = {};
      infoSocioeconomicas.value.forEach(item => {
        if (!item.pacienteId) return;
        if (!grupos[item.pacienteId]) {
          grupos[item.pacienteId] = {
            pacienteId: item.pacienteId,
            ecuFormateado: formatoEcuPaciente(item.pacienteId),
            respuestas: []
          };
        }
        grupos[item.pacienteId].respuestas.push(item);
      });
      return Object.values(grupos);
    });
    // ---------------------------------------------

    return {
      infoSocioeconomicas,
      handleSyncList,
      isFetching,
      retrieveInfoSocioeconomicas,
      clear,
      removeId,
      removeEntity,
      prepareRemoveEstudio,
      closeDialog,
      removeInfoSocioeconomica,
      formatoEcuPaciente,
      
      // Exportamos nuestras nuevas variables para usarlas en el HTML
      infoAgrupada,
      pacientesExpandidos,
      togglePaciente,
      
      t$,
    };
  },
});