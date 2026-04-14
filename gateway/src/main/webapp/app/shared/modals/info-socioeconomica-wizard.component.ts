import { computed, defineComponent, inject, ref, type Ref, watch } from 'vue';
import { useVuelidate } from '@vuelidate/core';
import { required, numeric, minValue, maxValue, minLength, maxLength, requiredIf } from '@vuelidate/validators';
import Swal from 'sweetalert2';

import InfoSocioeconomicaService from '@/entities/pacientems/info-socioeconomica/info-socioeconomica.service';
import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';

export default defineComponent({
  name: 'InfoSocioeconomicaWizardModal',
  props: { visible: { type: Boolean, required: true } },
  emits: ['update:visible', 'saved'],
  
  setup(props, { emit }) {
    // Servicios
    const infoSocioeconomicaService = inject('infoSocioeconomicaService', () => new InfoSocioeconomicaService());
    const pacienteService = inject('pacienteService', () => new PacienteService());

    // Estado general
    const infoSocioeconomica: Ref<Record<string, any>> = ref({});
    const isSaving = ref(false);
    
    // Controles de Vista (Acordeón)
    const mostrarCuestionario = ref(false);
    const seccionAbierta = ref(1); 

    // Estado Búsqueda
    const ecuSearchString = ref('');
    const pacienteEncontrado: Ref<IPaciente | null> = ref(null);
    const isSearchingEcu = ref(false);

    // Inicializador del formulario
    const initForm = () => {
      infoSocioeconomica.value = {
        jefeHogar: null, tieneTelefono: null, padeceEnfermedad: null, hablaLenguaIndigena: null, 
        hablaEspanol: null, consideraIndigena: null, sabeLeerEscribir: null, estudia: null, esJubiladoPensionado: null,
      };
    };
    initForm();

    // 🚀 VARIABLES COMPUTADAS (Incluyendo totalHabitantes)
    const nombreCompleto = computed(() => pacienteEncontrado.value ? `${pacienteEncontrado.value.nombre || ''} ${pacienteEncontrado.value.apellidoPaterno || ''} ${pacienteEncontrado.value.apellidoMaterno || ''}`.trim() : '');
    
    const edadCalculada = computed(() => {
      if (!pacienteEncontrado.value?.fechaNacimiento) return '';
      const birthDate = new Date(pacienteEncontrado.value.fechaNacimiento);
      const today = new Date();
      let age = today.getFullYear() - birthDate.getFullYear();
      if (today.getMonth() < birthDate.getMonth() || (today.getMonth() === birthDate.getMonth() && today.getDate() < birthDate.getDate())) age--;
      return `${age} años`;
    });
    
    const formatoSexo = computed(() => pacienteEncontrado.value?.sexo === 'M' ? 'Hombre' : pacienteEncontrado.value?.sexo === 'F' ? 'Mujer' : pacienteEncontrado.value?.sexo || '');

    const totalHabitantes = computed(() => {
      const h = Number(infoSocioeconomica.value.numHombres) || 0;
      const m = Number(infoSocioeconomica.value.numMujeres) || 0;
      return h + m;
    });

    // 🚀 FUNCIONES DE LIMPIEZA
    const limpiarTelefonos = () => { infoSocioeconomica.value.numeroFijo = null; infoSocioeconomica.value.numeroRecados = null; infoSocioeconomica.value.numeroCelular = null; };
    const limpiarInputNumerico = (event: Event, campo: string) => {
      const target = event.target as HTMLInputElement;
      if (target) { const valorLimpio = target.value.replace(/\D/g, ''); infoSocioeconomica.value[campo] = valorLimpio; target.value = valorLimpio; }
    };
    const limpiarEnfermedad = () => (infoSocioeconomica.value.enfermedadCual = null);
    const limpiarLengua = () => (infoSocioeconomica.value.lenguaIndigenaCual = null);
    const limpiarGrupoIndigena = () => (infoSocioeconomica.value.grupoIndigenaCual = null);
    const limpiarPension = () => (infoSocioeconomica.value.montoPension = null);
    const limpiarBusqueda = () => { ecuSearchString.value = ''; pacienteEncontrado.value = null; };

    // 🚀 LÓGICA DE AUTO-BÚSQUEDA (DEBOUNCE)
    let ecuTimeout: ReturnType<typeof setTimeout>;
    watch(ecuSearchString, (newVal) => {
      clearTimeout(ecuTimeout);
      if (newVal && newVal.trim() !== '') {
        ecuTimeout = setTimeout(() => { buscarPaciente(); }, 800);
      } else {
        pacienteEncontrado.value = null;
      }
    });

    const buscarPaciente = async () => {
      if (!ecuSearchString.value) return;
      isSearchingEcu.value = true;
      pacienteEncontrado.value = null;

      try {
        const res = await pacienteService().retrieve();
        const ecuNumerico = parseInt(ecuSearchString.value, 10);
        const encontrado = res.data.find((p: IPaciente) => p.ecu === ecuNumerico);

        if (encontrado) {
          const resInfo = await infoSocioeconomicaService().retrieve();
          const yaTieneInfo = resInfo.data.some((info: any) => info.pacienteId === encontrado.id);

          if (yaTieneInfo) {
            Swal.fire({ icon: 'warning', title: 'Paciente con estudio', text: `El paciente con ECU ${ecuNumerico} ya tiene un estudio registrado. Si desea editarlo, hágalo desde la tabla principal.`, confirmButtonColor: '#611232' });
            ecuSearchString.value = '';
          } else {
            pacienteEncontrado.value = encontrado;
            Swal.fire({ icon: 'success', title: 'Paciente Encontrado', showConfirmButton: false, timer: 2000 });
          }
        } else {
          Swal.fire({ icon: 'error', title: 'No encontrado', text: 'No existe un paciente con ese ECU.', confirmButtonColor: '#611232' });
        }
      } catch (error: any) {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Ocurrió un error al consultar el servidor.', confirmButtonColor: '#611232' });
      } finally {
        isSearchingEcu.value = false;
      }
    };

    // 🚀 LÓGICA DE BORRADOR (CACHE LOCALSTORAGE)
    watch(infoSocioeconomica, (newVal) => {
      if (mostrarCuestionario.value && pacienteEncontrado.value) {
        localStorage.setItem(`socio_draft_${pacienteEncontrado.value.ecu}`, JSON.stringify(newVal));
      }
    }, { deep: true });

    const iniciarCuestionario = () => {
      const draftGuardado = localStorage.getItem(`socio_draft_${pacienteEncontrado.value?.ecu}`);
      if (draftGuardado) {
        Swal.fire({
          title: 'Borrador encontrado',
          text: "Tienes un estudio a medias para este paciente. ¿Deseas recuperarlo?",
          icon: 'info', showCancelButton: true, confirmButtonColor: '#611232', cancelButtonColor: '#888',
          confirmButtonText: 'Sí, recuperar', cancelButtonText: 'Empezar de cero'
        }).then((result) => {
          if (result.isConfirmed) {
            infoSocioeconomica.value = JSON.parse(draftGuardado);
          } else {
            localStorage.removeItem(`socio_draft_${pacienteEncontrado.value?.ecu}`);
            initForm();
          }
          mostrarCuestionario.value = true;
          seccionAbierta.value = 1;
        });
      } else {
        mostrarCuestionario.value = true;
        seccionAbierta.value = 1;
      }
    };

    // 🚀 REGLAS DE VALIDACIÓN COMPLETAS
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
      montoPension: { required: requiredIf(() => infoSocioeconomica.value.esJubiladoPensionado === true), numeric, minValue: minValue(0), maxValue: maxValue(100000) },
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
      tenenciaVivienda: { required }
    };
    const v$ = useVuelidate(validationRules, infoSocioeconomica as any);

    const toggleSeccion = (num: number) => {
      seccionAbierta.value = seccionAbierta.value === num ? 0 : num;
    };

    const cerrarModal = () => {
      if (mostrarCuestionario.value) {
        Swal.fire({
          title: '¿Pausar estudio?', text: "Tu progreso se guardará como borrador en tu computadora.", icon: 'warning',
          showCancelButton: true, confirmButtonColor: '#611232', cancelButtonColor: '#888', confirmButtonText: 'Sí, salir', cancelButtonText: 'Continuar editando'
        }).then((result) => {
          if (result.isConfirmed) resetModal();
        });
      } else { resetModal(); }
    };

    const resetModal = () => {
      mostrarCuestionario.value = false; ecuSearchString.value = ''; pacienteEncontrado.value = null;
      initForm(); v$.value.$reset(); emit('update:visible', false);
    };

    // 🚀 GUARDADO REAL A BASE DE DATOS
    const save = async () => {
      v$.value.$touch();
      if (v$.value.$invalid) { 
        Swal.fire({ icon: 'error', text: 'Hay campos inválidos o vacíos. Por favor revisa los acordeones.', confirmButtonColor: '#611232' }); 
        return; 
      }
      isSaving.value = true;

      try {
        const pacienteId = pacienteEncontrado.value!.id;
        const form = infoSocioeconomica.value as any;
        const paquetitosAEnviar: any[] = [];

        const empaquetar = (pregunta: string, respuesta: any, respuestaAbierta: any = null) => {
          if (typeof respuesta === 'boolean') respuesta = respuesta ? 'SI' : 'NO';
          if (respuesta !== null && respuesta !== undefined && respuesta !== '') {
            paquetitosAEnviar.push({ 
              pacienteId: pacienteId, 
              pregunta: pregunta, 
              respuesta: String(respuesta).trim(), 
              respuestaAbierta: respuestaAbierta ? String(respuestaAbierta).trim() : null 
            });
          }
        };

        // Empacamos todas las preguntas
        empaquetar('DOCUMENTO_IDENTIDAD', form.documentoIdentidad); 
        empaquetar('TIPO_VIVIENDA', form.tipoVivienda);
        empaquetar('NUM_HOMBRES', form.numHombres); 
        empaquetar('NUM_MUJERES', form.numMujeres); 
        empaquetar('JEFE_HOGAR', form.jefeHogar);
        
        // Manejo de teléfonos
        const telefonosArray = []; 
        if (form.numeroFijo) telefonosArray.push(`Fijo: ${form.numeroFijo}`); 
        if (form.numeroRecados) telefonosArray.push(`Recados: ${form.numeroRecados}`); 
        if (form.numeroCelular) telefonosArray.push(`Celular: ${form.numeroCelular}`);
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

        // Envío asíncrono al backend
        const promesasDeGuardado = paquetitosAEnviar.map(paquete => infoSocioeconomicaService().create(paquete));
        await Promise.all(promesasDeGuardado);

        // Borramos la caché de borrador si se guardó con éxito
        localStorage.removeItem(`socio_draft_${pacienteEncontrado.value?.ecu}`);
        
        Swal.fire({ icon: 'success', title: '¡Estudio Guardado!', text: 'El cuestionario se ha registrado correctamente.', showConfirmButton: false, timer: 2000 });
        emit('saved');
        resetModal();

      } catch (error: any) { 
        Swal.fire({ icon: 'error', title: 'Error', text: 'Ocurrió un error al guardar en la base de datos.', confirmButtonColor: '#611232' }); 
      } finally { 
        isSaving.value = false; 
      } 
    };

    // 🚀 EXPORTAMOS ABSOLUTAMENTE TODO A LA VISTA
    return {
      infoSocioeconomica, ecuSearchString, pacienteEncontrado, isSearchingEcu,
      mostrarCuestionario, seccionAbierta, toggleSeccion, iniciarCuestionario, cerrarModal,
      buscarPaciente, limpiarBusqueda, nombreCompleto, edadCalculada, formatoSexo,
      totalHabitantes, // <-- Exportado
      limpiarTelefonos, limpiarInputNumerico, limpiarEnfermedad, // <-- Exportados
      limpiarLengua, limpiarGrupoIndigena, limpiarPension, // <-- Exportados
      isSaving, save, v$,
    };
  },
});