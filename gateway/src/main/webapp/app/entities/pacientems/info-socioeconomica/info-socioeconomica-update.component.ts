import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';
// Agregamos todas las validaciones matemáticas y de longitud que usaremos
import { required, numeric, minValue, maxValue, minLength, maxLength, requiredIf } from '@vuelidate/validators';

import InfoSocioeconomicaService from './info-socioeconomica.service';
import { type IInfoSocioeconomica, InfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';
import { useAlertService } from '@/shared/alert/alert.service';

// Importamos el servicio del paciente para la búsqueda
import PacienteService from '../paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InfoSocioeconomicaUpdate',
  setup() {
    const route = useRoute();
    const router = useRouter();
    const alertService = inject('alertService', () => useAlertService(), true);
    
    // Servicios
    const infoSocioeconomicaService = inject('infoSocioeconomicaService', () => new InfoSocioeconomicaService());
    const pacienteService = inject('pacienteService', () => new PacienteService());

    // Estado principal
    const infoSocioeconomica: Ref<IInfoSocioeconomica> = ref(new InfoSocioeconomica());
    const isSaving = ref(false);

    // Forzamos los booleanos a null para que los radio buttons inicien desmarcados
    infoSocioeconomica.value.tieneTelefono = null as any;
    infoSocioeconomica.value.padeceEnfermedad = null as any; // <--- AGREGA ESTA LÍNEA
    infoSocioeconomica.value.hablaLenguaIndigena = null as any;
    infoSocioeconomica.value.hablaEspanol = null as any;
    infoSocioeconomica.value.consideraIndigena = null as any;
    infoSocioeconomica.value.sabeLeerEscribir = null as any;
    infoSocioeconomica.value.estudia = null as any;
    infoSocioeconomica.value.esJubiladoPensionado = null as any;

    // Estado de la Búsqueda
    const ecuSearchString = ref('');
    const pacienteEncontrado: Ref<IPaciente | null> = ref(null);
    const isSearchingEcu = ref(false);

    // --- MAGIA PARA EL BLOQUE 1 (Datos calculados y de solo lectura) ---
    const nombreCompleto = computed(() => {
      if (!pacienteEncontrado.value) return '';
      const p = pacienteEncontrado.value;
      return `${p.nombre || ''} ${p.apellidoPaterno || ''} ${p.apellidoMaterno || ''}`.trim();
    });

    const edadCalculada = computed(() => {
      if (!pacienteEncontrado.value || !pacienteEncontrado.value.fechaNacimiento) return '';
      const birthDate = new Date(pacienteEncontrado.value.fechaNacimiento);
      const today = new Date();
      let age = today.getFullYear() - birthDate.getFullYear();
      const m = today.getMonth() - birthDate.getMonth();
      if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }
      return `${age} años`;
    });

    const formatoSexo = computed(() => {
      const sexo = pacienteEncontrado.value?.sexo;
      if (sexo === 'M') return 'Hombre';
      if (sexo === 'F') return 'Mujer';
      return sexo || '';
    });
    // -------------------------------------------------------------------

    // --- MAGIA PARA LOS BLOQUES 4, 5 Y 6 ---
    const totalHabitantes = computed(() => {
      // Convertimos a número y evitamos los NaN si está vacío
      const h = Number(infoSocioeconomica.value.numHombres) || 0;
      const m = Number(infoSocioeconomica.value.numMujeres) || 0;
      return h + m;
    });

    // Esta función limpia los campos si el usuario se arrepiente y le da a "No"
// Esta función limpia los campos si el usuario se arrepiente y le da a "No"
    const limpiarTelefonos = () => {
      infoSocioeconomica.value.numeroFijo = null;
      infoSocioeconomica.value.numeroRecados = null;
      infoSocioeconomica.value.numeroCelular = null;
    };

    // --- NUEVA FUNCIÓN PARA LIMPIAR LETRAS ---
    const limpiarInputNumerico = (event: Event, campo: string) => {
      // Le decimos a TypeScript: "Confía en mí, esto es un input de texto"
      const target = event.target as HTMLInputElement;
      if (target) {
        const valorLimpio = target.value.replace(/\D/g, '');
        (infoSocioeconomica.value as any)[campo] = valorLimpio;
        target.value = valorLimpio; // Fuerza a la pantalla a borrar la letra
      }
    };

        const limpiarEnfermedad = () => {
      infoSocioeconomica.value.enfermedadCual = null;
    };
    const limpiarLengua = () => {
      infoSocioeconomica.value.lenguaIndigenaCual = null;
    };

    const limpiarGrupoIndigena = () => {
      infoSocioeconomica.value.grupoIndigenaCual = null;
    };

    const limpiarPension = () => {
      infoSocioeconomica.value.montoPension = null;
    };
    // Función para limpiar la caja de texto si cambia a "No"

    // ------------------------------------------

    // Búsqueda del paciente
    const buscarPaciente = async () => {
      if (!ecuSearchString.value) return;
      isSearchingEcu.value = true;
      pacienteEncontrado.value = null;

      try {
        const res = await pacienteService().retrieve();
        const pacientes = res.data;
        const ecuNumerico = parseInt(ecuSearchString.value, 10);
        
        const encontrado = pacientes.find((p: IPaciente) => p.ecu === ecuNumerico);
        
        if (encontrado) {
          pacienteEncontrado.value = encontrado;
          alertService.showSuccess('Paciente cargado. Puede comenzar el cuestionario.');
        } else {
          alertService.showError('No se encontró ningún paciente con ese ECU.');
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      } finally {
        isSearchingEcu.value = false;
      }
    };

    const limpiarBusqueda = () => {
      ecuSearchString.value = '';
      pacienteEncontrado.value = null;
    };

    const validationRules = {
      documentoIdentidad: { required },
      tipoVivienda: { required },
      numHombres: { required, numeric, minValue: minValue(0), maxValue: maxValue(20) },
      numMujeres: { required, numeric, minValue: minValue(0), maxValue: maxValue(20) },
      jefeHogar: { required },
      tieneTelefono: { required },
      numeroFijo: { numeric, minLength: minLength(10), maxLength: maxLength(10) },
      numeroRecados: { numeric, minLength: minLength(10), maxLength: maxLength(10) },
      numeroCelular: { numeric, minLength: minLength(10), maxLength: maxLength(10) },

      // Bloque 7 y 8
      padeceEnfermedad: { required },
      enfermedadCual: {
        required: requiredIf(() => infoSocioeconomica.value.padeceEnfermedad === true)
      },
      institucionMedica: { required },

      // Bloques 9, 10 y 11 
      hablaLenguaIndigena: { required },
      lenguaIndigenaCual: {
        required: requiredIf(() => infoSocioeconomica.value.hablaLenguaIndigena === true)
      },
      hablaEspanol: { required },
      consideraIndigena: { required },
      grupoIndigenaCual: {
        required: requiredIf(() => infoSocioeconomica.value.consideraIndigena === true)
      },

      // Bloques 12 y 13
      sabeLeerEscribir: { required },
      estudia: { required },

      // Bloques 14 y 15
      condicionLaboral: { required },
      gradoMaximoEstudios: { required },

      // Bloques 16 y 17
      mayorFuenteIngreso: { required },
      tiempoEmpleado: { required },

      // Bloques 18 y 19
      ingresoMensual: { required, numeric, minValue: minValue(0), maxValue: maxValue(100000) },
      esJubiladoPensionado: { required },
      montoPension: {
        required: requiredIf(() => infoSocioeconomica.value.esJubiladoPensionado === true),
        numeric, 
        minValue: minValue(0), 
        maxValue: maxValue(100000)
      },

      // Bloques 20, 21 y 22
      numeroHabitaciones: { required, numeric, minValue: minValue(0), maxValue: maxValue(20) },
      cuartosDormir: { required, numeric, minValue: minValue(0), maxValue: maxValue(20) },
      materialVivienda: { required },

      // Bloques 23 y 24
      materialTecho: { required },
      materialParedes: { required },

      // Bloques 25 y 26
      tipoBano: { required },
      tipoDrenaje: { required },

      // Bloques 27 y 28
      manejoBasura: { required },
      fuenteLuzElectrica: { required },

      // Bloques 29 y 30
      combustibleCocina: { required },
      tenenciaVivienda: { required },

    };


    const v$ = useVuelidate(validationRules, infoSocioeconomica as any);

    const previousState = () => router.go(-1);

    const save = async () => {
      // Dejamos el cascarón de guardar para más adelante
      console.log("Guardando información...");
    };

    return {
      infoSocioeconomica,
      
      // Variables de Búsqueda
      ecuSearchString,
      pacienteEncontrado,
      isSearchingEcu,
      buscarPaciente,
      limpiarBusqueda,

      // Computed para el Bloque 1
      nombreCompleto,
      edadCalculada,
      formatoSexo,

      // Exportamos las nuevas funciones y variables
      totalHabitantes,
      limpiarTelefonos,
      limpiarInputNumerico,
      limpiarEnfermedad,

      limpiarLengua,
      limpiarGrupoIndigena,

      limpiarPension,
      

      isSaving,
      previousState,
      save,
      v$,
    };
  },
});