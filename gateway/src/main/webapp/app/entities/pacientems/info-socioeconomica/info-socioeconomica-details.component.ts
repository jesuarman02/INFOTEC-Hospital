import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import InfoSocioeconomicaService from './info-socioeconomica.service';
import PacienteService from '../paciente/paciente.service';
import { type IInfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InfoSocioeconomicaDetails',
  setup() {
    const infoSocioeconomicaService = inject('infoSocioeconomicaService', () => new InfoSocioeconomicaService());
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const estudioCompleto: Ref<IInfoSocioeconomica[]> = ref([]);
    const pacienteNombre = ref('');

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

    const retrieveInfoSocioeconomica = async (infoSocioeconomicaId: any) => {
      try {
        // 1. Buscamos el registro al que le dimos clic para saber de qué paciente es
        const res = await infoSocioeconomicaService().find(infoSocioeconomicaId);
        const pid = res.pacienteId;

        if (pid) {
          // 2. Buscamos el nombre y ECU del paciente
          try {
            const resPac = await pacienteService().find(pid);
            pacienteNombre.value = `${resPac.ecu} - ${resPac.nombre || ''} ${resPac.apellidoPaterno || ''}`.trim();
          } catch (e) {
            pacienteNombre.value = `ID: ${pid}`;
          }

          // 3. Descargamos TODO y filtramos solo las preguntas de este paciente
          const resAll = await infoSocioeconomicaService().retrieve();
          let filtrados = resAll.data.filter((item: any) => item.pacienteId === pid);

          // 4. Los ordenamos bonito como en el formulario
          filtrados.sort((a: any, b: any) => {
            let indiceA = ordenCorrecto.indexOf(a.pregunta || '');
            let indiceB = ordenCorrecto.indexOf(b.pregunta || '');
            if (indiceA === -1) indiceA = 999;
            if (indiceB === -1) indiceB = 999;
            return indiceA - indiceB;
          });

          estudioCompleto.value = filtrados;
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params.infoSocioeconomicaId) {
      retrieveInfoSocioeconomica(route.params.infoSocioeconomicaId);
    }

    return {
      alertService,
      estudioCompleto,
      pacienteNombre,
      previousState,
      t$: useI18n().t,
    };
  },
});