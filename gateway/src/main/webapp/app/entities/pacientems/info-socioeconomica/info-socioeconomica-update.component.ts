import { computed, defineComponent, inject, ref, type Ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';
import { required, numeric, minValue, maxValue, minLength, maxLength, requiredIf } from '@vuelidate/validators';

import InfoSocioeconomicaService from './info-socioeconomica.service';
import { type IInfoSocioeconomica, InfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';
import { useAlertService } from '@/shared/alert/alert.service';

import PacienteService from '../paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InfoSocioeconomicaUpdate',
  setup() {
    const route = useRoute();
    const router = useRouter();
    const alertService = inject('alertService', () => useAlertService(), true);

    const infoSocioeconomicaService = inject('infoSocioeconomicaService', () => new InfoSocioeconomicaService());
    const pacienteService = inject('pacienteService', () => new PacienteService());

    const infoSocioeconomica: Ref<Record<string, any>> = ref({});
    const isSaving = ref(false);
    const isEditing = ref(false); // <-- Saber si estamos creando o editando

    // Diccionario para guardar los IDs de las filas que ya existen en la Base de Datos
    const idsGuardados: Ref<Record<string, number>> = ref({});

    // Estado de la Búsqueda
    const ecuSearchString = ref('');
    const pacienteEncontrado: Ref<IPaciente | null> = ref(null);
    const isSearchingEcu = ref(false);

    Object.assign(infoSocioeconomica.value, {
      jefeHogar: null,
      tieneTelefono: null,
      padeceEnfermedad: null,
      hablaLenguaIndigena: null,
      hablaEspanol: null,
      consideraIndigena: null,
      sabeLeerEscribir: null,
      estudia: null,
      esJubiladoPensionado: null,
    });

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

    const totalHabitantes = computed(() => {
      const h = Number(infoSocioeconomica.value.numHombres) || 0;
      const m = Number(infoSocioeconomica.value.numMujeres) || 0;
      return h + m;
    });

    const limpiarTelefonos = () => {
      infoSocioeconomica.value.numeroFijo = null;
      infoSocioeconomica.value.numeroRecados = null;
      infoSocioeconomica.value.numeroCelular = null;
    };

    const limpiarInputNumerico = (event: Event, campo: string) => {
      const target = event.target as HTMLInputElement;
      if (target) {
        const valorLimpio = target.value.replace(/\D/g, '');
        infoSocioeconomica.value[campo] = valorLimpio;
        target.value = valorLimpio;
      }
    };

    const limpiarEnfermedad = () => (infoSocioeconomica.value.enfermedadCual = null);
    const limpiarLengua = () => (infoSocioeconomica.value.lenguaIndigenaCual = null);
    const limpiarGrupoIndigena = () => (infoSocioeconomica.value.grupoIndigenaCual = null);
    const limpiarPension = () => (infoSocioeconomica.value.montoPension = null);

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
          const resInfo = await infoSocioeconomicaService().retrieve();
          const yaTieneInfo = resInfo.data.some((info: any) => info.pacienteId === encontrado.id);

          if (yaTieneInfo) {
            alertService.showError(
              `El paciente con ECU ${ecuNumerico} ya tiene un estudio socioeconómico. Vaya a la lista si desea editarlo.`,
            );
          } else {
            pacienteEncontrado.value = encontrado;
            alertService.showSuccess('Paciente cargado. Puede comenzar el cuestionario.');
          }
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
      if (isEditing.value) return; // Si estamos editando, no dejamos que limpien la búsqueda
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
      padeceEnfermedad: { required },
      enfermedadCual: { required: requiredIf(() => infoSocioeconomica.value.padeceEnfermedad === true) },
      institucionMedica: { required },
      hablaLenguaIndigena: { required },
      lenguaIndigenaCual: { required: requiredIf(() => infoSocioeconomica.value.hablaLenguaIndigena === true) },
      hablaEspanol: { required },
      consideraIndigena: { required },
      grupoIndigenaCual: { required: requiredIf(() => infoSocioeconomica.value.consideraIndigena === true) },
      sabeLeerEscribir: { required },
      estudia: { required },
      condicionLaboral: { required },
      gradoMaximoEstudios: { required },
      mayorFuenteIngreso: { required },
      tiempoEmpleado: { required },
      ingresoMensual: { required, numeric, minValue: minValue(0), maxValue: maxValue(100000) },
      esJubiladoPensionado: { required },
      montoPension: {
        required: requiredIf(() => infoSocioeconomica.value.esJubiladoPensionado === true),
        numeric,
        minValue: minValue(0),
        maxValue: maxValue(100000),
      },
      numeroHabitaciones: { required, numeric, minValue: minValue(0), maxValue: maxValue(20) },
      cuartosDormir: { required, numeric, minValue: minValue(0), maxValue: maxValue(20) },
      materialVivienda: { required },
      materialTecho: { required },
      materialParedes: { required },
      tipoBano: { required },
      tipoDrenaje: { required },
      manejoBasura: { required },
      fuenteLuzElectrica: { required },
      combustibleCocina: { required },
      tenenciaVivienda: { required },
    };

    const v$ = useVuelidate(validationRules, infoSocioeconomica as any);
    const previousState = () => router.go(-1);

    // --- 🔮 MAGIA 1: DESEMPAQUETAR (Modo Edición) ---
    const cargarEstudioParaEditar = async (idFilaClickeada: number) => {
      isEditing.value = true;
      try {
        // 1. Buscamos la fila para saber de qué paciente es
        const resFila = await infoSocioeconomicaService().find(idFilaClickeada);
        const pid = resFila.pacienteId;

        // Le aseguramos a TypeScript que pid existe sí o sí
        if (!pid) return;

        // 2. Cargamos los datos del paciente en la cabecera
        const resPac = await pacienteService().find(pid);
        pacienteEncontrado.value = resPac;
        ecuSearchString.value = String(resPac.ecu);

        // 3. Descargamos TODAS las filas y filtramos las de este paciente
        const resAll = await infoSocioeconomicaService().retrieve();
        const filasDelPaciente = resAll.data.filter((item: any) => item.pacienteId === pid);

        // Diccionario de traducción: BD -> Formulario
        const mapaPreguntas: Record<string, string> = {
          DOCUMENTO_IDENTIDAD: 'documentoIdentidad',
          TIPO_VIVIENDA: 'tipoVivienda',
          NUM_HOMBRES: 'numHombres',
          NUM_MUJERES: 'numMujeres',
          JEFE_HOGAR: 'jefeHogar',
          TIENE_TELEFONO: 'tieneTelefono',
          NUMERO_FIJO: 'numeroFijo',
          NUMERO_RECADOS: 'numeroRecados',
          NUMERO_CELULAR: 'numeroCelular',
          PADECE_ENFERMEDAD: 'padeceEnfermedad',
          INSTITUCION_MEDICA: 'institucionMedica',
          HABLA_LENGUA_INDIGENA: 'hablaLenguaIndigena',
          HABLA_ESPANOL: 'hablaEspanol',
          CONSIDERA_INDIGENA: 'consideraIndigena',
          SABE_LEER_ESCRIBIR: 'sabeLeerEscribir',
          ESTUDIA: 'estudia',
          GRADO_MAXIMO_ESTUDIOS: 'gradoMaximoEstudios',
          CONDICION_LABORAL: 'condicionLaboral',
          MAYOR_FUENTE_INGRESO: 'mayorFuenteIngreso',
          TIEMPO_EMPLEADO: 'tiempoEmpleado',
          INGRESO_MENSUAL: 'ingresoMensual',
          ES_JUBILADO_PENSIONADO: 'esJubiladoPensionado',
          NUMERO_HABITACIONES: 'numeroHabitaciones',
          CUARTOS_DORMIR: 'cuartosDormir',
          MATERIAL_VIVIENDA: 'materialVivienda',
          MATERIAL_TECHO: 'materialTecho',
          MATERIAL_PAREDES: 'materialParedes',
          TIPO_BANO: 'tipoBano',
          TIPO_DRENAJE: 'tipoDrenaje',
          MANEJO_BASURA: 'manejoBasura',
          FUENTE_LUZ_ELECTRICA: 'fuenteLuzElectrica',
          COMBUSTIBLE_COCINA: 'combustibleCocina',
          TENENCIA_VIVIENDA: 'tenenciaVivienda',
        };

        const booleanos = [
          'JEFE_HOGAR',
          'TIENE_TELEFONO',
          'PADECE_ENFERMEDAD',
          'HABLA_LENGUA_INDIGENA',
          'HABLA_ESPANOL',
          'CONSIDERA_INDIGENA',
          'SABE_LEER_ESCRIBIR',
          'ESTUDIA',
          'ES_JUBILADO_PENSIONADO',
        ];
        const abiertas: Record<string, string> = {
          PADECE_ENFERMEDAD: 'enfermedadCual',
          HABLA_LENGUA_INDIGENA: 'lenguaIndigenaCual',
          CONSIDERA_INDIGENA: 'grupoIndigenaCual',
          ES_JUBILADO_PENSIONADO: 'montoPension',
        };

        // 4. Inyectamos los datos en la pantalla
        filasDelPaciente.forEach((item: any) => {
          if (item.pregunta) {
            idsGuardados.value[item.pregunta] = item.id;

            const campoForm = mapaPreguntas[item.pregunta];
            if (campoForm) {
              if (booleanos.includes(item.pregunta)) {
                infoSocioeconomica.value[campoForm] = item.respuesta === 'SI';
              } else {
                infoSocioeconomica.value[campoForm] = item.respuesta;
              }
            }
            if (abiertas[item.pregunta]) {
              infoSocioeconomica.value[abiertas[item.pregunta]] = item.respuestaAbierta;
            }

            // MAGIA INVERSA DE TELÉFONOS: Cortamos la cadena y llenamos las cajitas
            if (item.pregunta === 'TIENE_TELEFONO' && item.respuestaAbierta) {
              const partes = item.respuestaAbierta.split(', ');
              partes.forEach((parte: string) => {
                if (parte.includes('Fijo:')) infoSocioeconomica.value.numeroFijo = parte.replace('Fijo:', '').trim();
                if (parte.includes('Recados:')) infoSocioeconomica.value.numeroRecados = parte.replace('Recados:', '').trim();
                if (parte.includes('Celular:')) infoSocioeconomica.value.numeroCelular = parte.replace('Celular:', '').trim();
              });
            }
          }
        });
      } catch (err) {
        console.error('Error cargando estudio:', err);
      }
    };

    onMounted(() => {
      // Si entramos desde el botón "Editar", la URL trae un ID
      if (route.params.infoSocioeconomicaId) {
        cargarEstudioParaEditar(Number(route.params.infoSocioeconomicaId));
      }
    });

    // --- 🔮 MAGIA 2: RE-EMPAQUETAR (Guardar) ---
    const save = async () => {
      v$.value.$touch();
      if (v$.value.$invalid) {
        alertService.showError('Por favor, complete todos los campos obligatorios correctamente.');
        return;
      }

      if (!pacienteEncontrado.value || !pacienteEncontrado.value.id) {
        alertService.showError('Primero debe buscar y seleccionar un paciente (ECU).');
        return;
      }

      isSaving.value = true;

      try {
        const pacienteId = pacienteEncontrado.value.id;
        const form = infoSocioeconomica.value as any;
        const paquetitosAEnviar: any[] = [];

        const empaquetar = (pregunta: string, respuesta: any, respuestaAbierta: any = null) => {
          if (typeof respuesta === 'boolean') respuesta = respuesta ? 'SI' : 'NO';

          if (respuesta !== null && respuesta !== undefined && respuesta !== '') {
            paquetitosAEnviar.push({
              id: idsGuardados.value[pregunta] || undefined, // Si ya existía, le ponemos su ID para que sea un UPDATE
              pacienteId: pacienteId,
              pregunta: pregunta,
              respuesta: String(respuesta).trim(),
              respuestaAbierta: respuestaAbierta ? String(respuestaAbierta).trim() : null,
            });
          }
        };

        // Empacamos todas las variables de la pantalla
        empaquetar('DOCUMENTO_IDENTIDAD', form.documentoIdentidad);
        empaquetar('TIPO_VIVIENDA', form.tipoVivienda);
        empaquetar('NUM_HOMBRES', form.numHombres);
        empaquetar('NUM_MUJERES', form.numMujeres);
        empaquetar('JEFE_HOGAR', form.jefeHogar);

        // --- MAGIA DE TELÉFONOS ---
        // Armamos un arreglo solo con los teléfonos que el paciente sí dio
        const telefonosArray = [];
        if (form.numeroFijo) telefonosArray.push(`Fijo: ${form.numeroFijo}`);
        if (form.numeroRecados) telefonosArray.push(`Recados: ${form.numeroRecados}`);
        if (form.numeroCelular) telefonosArray.push(`Celular: ${form.numeroCelular}`);

        // Lo convertimos a una sola línea de texto separada por comas (o null si está vacío)
        const telefonosTexto = telefonosArray.length > 0 ? telefonosArray.join(', ') : null;
        empaquetar('TIENE_TELEFONO', form.tieneTelefono, telefonosTexto);
        empaquetar('PADECE_ENFERMEDAD', form.padeceEnfermedad, form.enfermedadCual);
        empaquetar('INSTITUCION_MEDICA', form.institucionMedica);
        empaquetar('HABLA_LENGUA_INDIGENA', form.hablaLenguaIndigena, form.lenguaIndigenaCual);
        empaquetar('HABLA_ESPANOL', form.hablaEspanol);
        empaquetar('CONSIDERA_INDIGENA', form.consideraIndigena, form.grupoIndigenaCual);
        empaquetar('SABE_LEER_ESCRIBIR', form.sabeLeerEscribir);
        empaquetar('ESTUDIA', form.estudia);
        empaquetar('GRADO_MAXIMO_ESTUDIOS', form.gradoMaximoEstudios);
        empaquetar('CONDICION_LABORAL', form.condicionLaboral);
        empaquetar('MAYOR_FUENTE_INGRESO', form.mayorFuenteIngreso);
        empaquetar('TIEMPO_EMPLEADO', form.tiempoEmpleado);
        empaquetar('INGRESO_MENSUAL', form.ingresoMensual);
        empaquetar('ES_JUBILADO_PENSIONADO', form.esJubiladoPensionado, form.montoPension);
        empaquetar('NUMERO_HABITACIONES', form.numeroHabitaciones);
        empaquetar('CUARTOS_DORMIR', form.cuartosDormir);
        empaquetar('MATERIAL_VIVIENDA', form.materialVivienda);
        empaquetar('MATERIAL_TECHO', form.materialTecho);
        empaquetar('MATERIAL_PAREDES', form.materialParedes);
        empaquetar('TIPO_BANO', form.tipoBano);
        empaquetar('TIPO_DRENAJE', form.tipoDrenaje);
        empaquetar('MANEJO_BASURA', form.manejoBasura);
        empaquetar('FUENTE_LUZ_ELECTRICA', form.fuenteLuzElectrica);
        empaquetar('COMBUSTIBLE_COCINA', form.combustibleCocina);
        empaquetar('TENENCIA_VIVIENDA', form.tenenciaVivienda);

        // 🚀 ENVÍO MASIVO AL BACKEND
        const promesasDeGuardado = paquetitosAEnviar.map(paquete => {
          // Si el paquete trae un ID, es porque ya existía -> UPDATE
          if (paquete.id) {
            return infoSocioeconomicaService().update(paquete);
          } else {
            // Si no trae ID, es una pregunta que antes dejaron vacía y hoy la llenaron -> CREATE
            return infoSocioeconomicaService().create(paquete);
          }
        });

        await Promise.all(promesasDeGuardado);

        alertService.showSuccess('El cuestionario socioeconómico se actualizó correctamente.');
        previousState();
      } catch (error: any) {
        alertService.showHttpError(error.response);
      } finally {
        isSaving.value = false;
      }
    };

    return {
      infoSocioeconomica,
      ecuSearchString,
      pacienteEncontrado,
      isSearchingEcu,
      buscarPaciente,
      limpiarBusqueda,
      nombreCompleto,
      edadCalculada,
      formatoSexo,
      totalHabitantes,
      limpiarTelefonos,
      limpiarInputNumerico,
      limpiarEnfermedad,
      limpiarLengua,
      limpiarGrupoIndigena,
      limpiarPension,
      isSaving,
      isEditing, // <-- Exportamos la variable
      previousState,
      save,
      v$,
    };
  },
});
