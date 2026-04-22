import { ref, defineComponent, inject, type Ref, watch } from 'vue';
import type { PropType } from 'vue'; 
import { useI18n } from 'vue-i18n';
import useVuelidate from '@vuelidate/core';
import { required } from '@vuelidate/validators';
import Swal from 'sweetalert2';

import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import HistorialMedicoService from '@/entities/pacientems/historial-medico/historial-medico.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistorialMedicoModal',
  props: { 
    visible: { type: Boolean, required: true },
    pacientePreCargado: { type: Object as PropType<any>, default: null } 
  },
  emits: ['update:visible', 'saved'],

  setup(props, { emit }) {
    const { t: t$ } = useI18n();

    const historialMedicoService = inject('historialMedicoService', () => new HistorialMedicoService());
    const pacienteService = inject('pacienteService', () => new PacienteService());

    const ecuSearchString = ref('');
    const isSearchingEcu = ref(false);
    const pacienteEncontrado: Ref<IPaciente | null> = ref(null);
    const isSaving = ref(false);

    // 🔥 CONTROLES DEL ACORDEÓN Y VISTA 🔥
    const mostrarCuestionario = ref(false);
    const seccionAbierta = ref(1);
    const toggleSeccion = (num: number) => {
      seccionAbierta.value = seccionAbierta.value === num ? 0 : num;
    };

    // 🔥 HELPERS JSON
    const toJson = (tiene: boolean | string, detalles: any[]) => {
      const tieneBool = tiene === true || tiene === 'SI';
      return JSON.stringify({ tiene: tieneBool, detalles: tieneBool ? (detalles || []) : [] });
    };

    const fromJson = (json: string | null | undefined) => {
      if (!json) return { tiene: false, detalles: [] };
      try { return JSON.parse(json); } catch (e) { return { tiene: false, detalles: [] }; }
    };

    // FORM PRINCIPAL
    const initHistorial = () => {
      return {
        id: null,
        altura: null, peso: null, imc: null, grupoSanguineo: null, factorRh: null,
        tieneAlergias: false, tipoAlergia: null, sustanciaAlergia: '', reaccionesAlergia: [],
        gravedadAlergia: null, tratamientoAlergia: null, ultimoEpisodioAlergia: null,
        padeceCronicas: 'NO', haTenidoCirugias: 'NO', tomaMedicamentos: 'NO',
        tieneFamiliares: 'NO', tienePatologicos: 'NO', observacionesGenerales: '',
      };
    };
    
    const historialMedico = ref<any>(initHistorial());

    const initHabitos = () => ({
      tabaco: 'No fuma', tabacoFrecuencia: null, alcohol: 'No', alcoholFrecuencia: null,
      drogas: 'No', drogasTipo: '', drogasFrecuencia: null, esquemaVacunacion: 'Desconoce',
      vacunasRecientes: '', tieneDiscapacidad: 'No', tipoDiscapacidad: null
    });

    const formHabitos = ref(initHabitos());

    const rules = { altura: { required }, peso: { required } };
    const v$ = useVuelidate(rules, historialMedico);

    // LISTAS DINÁMICAS
    const listaEnfermedades = ref<any[]>([]);
    const listaCirugias = ref<any[]>([]);
    const listaMedicamentos = ref<any[]>([]);
    const listaFamiliares = ref<any[]>([]);
    const listaPatologicos = ref<any[]>([]);

    // 🔥 FUNCIONES DE UI
    const limpiarAlergias = () => { 
      if (!historialMedico.value.tieneAlergias) {
        historialMedico.value.tipoAlergia = null; historialMedico.value.sustanciaAlergia = '';
        historialMedico.value.reaccionesAlergia = []; historialMedico.value.gravedadAlergia = null;
        historialMedico.value.tratamientoAlergia = null; historialMedico.value.ultimoEpisodioAlergia = null;
      } 
    };
    
    const agregarEnfermedad = () => listaEnfermedades.value.push({ nombre: '', diagnostico: '', tipo: '', tratamiento: '' });
    const eliminarEnfermedad = (index: number) => listaEnfermedades.value.splice(index, 1);
    const manejarCambioCronicas = () => { if (historialMedico.value.padeceCronicas === 'SI' && listaEnfermedades.value.length === 0) agregarEnfermedad(); else if (historialMedico.value.padeceCronicas === 'NO') listaEnfermedades.value = []; };

    const agregarCirugia = () => listaCirugias.value.push({ nombre: '', anio: '', motivo: '' });
    const eliminarCirugia = (index: number) => listaCirugias.value.splice(index, 1);
    const manejarCambioCirugias = () => { if (historialMedico.value.haTenidoCirugias === 'SI' && listaCirugias.value.length === 0) agregarCirugia(); else if (historialMedico.value.haTenidoCirugias === 'NO') listaCirugias.value = []; };

    const agregarMedicamento = () => listaMedicamentos.value.push({ nombre: '', motivo: '', frecuencia: null });
    const eliminarMedicamento = (index: number) => listaMedicamentos.value.splice(index, 1);
    const manejarCambioMedicamentos = () => { if (historialMedico.value.tomaMedicamentos === 'SI' && listaMedicamentos.value.length === 0) agregarMedicamento(); else if (historialMedico.value.tomaMedicamentos === 'NO') listaMedicamentos.value = []; };

    const agregarFamiliar = () => listaFamiliares.value.push({ enfermedad: null, parentezco: null });
    const eliminarFamiliar = (index: number) => listaFamiliares.value.splice(index, 1);
    const manejarCambioFamiliares = () => { if (historialMedico.value.tieneFamiliares === 'SI' && listaFamiliares.value.length === 0) agregarFamiliar(); else if (historialMedico.value.tieneFamiliares === 'NO') listaFamiliares.value = []; };

    const agregarPatologico = () => listaPatologicos.value.push({ enfermedad: '', anio: '' });
    const eliminarPatologico = (index: number) => listaPatologicos.value.splice(index, 1);
    const manejarCambioPatologicos = () => { if (historialMedico.value.tienePatologicos === 'SI' && listaPatologicos.value.length === 0) agregarPatologico(); else if (historialMedico.value.tienePatologicos === 'NO') listaPatologicos.value = []; };

    // 🔥 CARGAR DATOS (DESEMPAQUETADO PARA EDICIÓN)
    const cargarHistorial = (data: any) => {
      historialMedico.value.id = data.id;
      historialMedico.value.observacionesGenerales = data.observacionesGenerales;

      if (data.pacienteId) {
        pacienteEncontrado.value = { id: data.pacienteId, ecu: data.pacienteEcu, nombre: data.pacienteNombre, apellidoPaterno: data.pacienteApellidoPaterno };
      }
      
      if (data.datosBiometricosSanguineos) {
        const parsed = fromJson(data.datosBiometricosSanguineos); 
        historialMedico.value.altura = parsed.altura || null; historialMedico.value.peso = parsed.peso || null;
        historialMedico.value.imc = parsed.imc || null; historialMedico.value.grupoSanguineo = parsed.grupoSanguineo || null;
        historialMedico.value.factorRh = parsed.factorRh || null;
      }

      if (data.alergias) {
        const parsed = fromJson(data.alergias);
        historialMedico.value.tieneAlergias = parsed.tiene;
        if (parsed.tiene && parsed.detalles.length > 0) {
          const al = parsed.detalles[0]; 
          historialMedico.value.tipoAlergia = al.tipoAlergia; historialMedico.value.sustanciaAlergia = al.sustanciaAlergia;
          historialMedico.value.reaccionesAlergia = al.reaccionesAlergia || []; historialMedico.value.gravedadAlergia = al.gravedadAlergia;
          historialMedico.value.tratamientoAlergia = al.tratamientoAlergia; historialMedico.value.ultimoEpisodioAlergia = al.ultimoEpisodioAlergia;
        }
      }

      if (data.enfermedadesCronicas) {
        const parsed = fromJson(data.enfermedadesCronicas);
        historialMedico.value.padeceCronicas = parsed.tiene ? 'SI' : 'NO'; listaEnfermedades.value = parsed.detalles || [];
      }

      if (data.cirugiasPrevias) {
        const parsed = fromJson(data.cirugiasPrevias);
        historialMedico.value.haTenidoCirugias = parsed.tiene ? 'SI' : 'NO'; listaCirugias.value = parsed.detalles || [];
      }

      if (data.medicamentosActuales) {
        const parsed = fromJson(data.medicamentosActuales);
        historialMedico.value.tomaMedicamentos = parsed.tiene ? 'SI' : 'NO'; listaMedicamentos.value = parsed.detalles || [];
      }

      if (data.antecedentesFamiliaresHereditarios) {
        const parsed = fromJson(data.antecedentesFamiliaresHereditarios);
        historialMedico.value.tieneFamiliares = parsed.tiene ? 'SI' : 'NO'; listaFamiliares.value = parsed.detalles || [];
      }

      if (data.antecedentesPersonalesPatologicos) {
        const parsed = fromJson(data.antecedentesPersonalesPatologicos);
        historialMedico.value.tienePatologicos = parsed.tiene ? 'SI' : 'NO'; listaPatologicos.value = parsed.detalles || [];
      }

      if (data.habitosConsumoOtros) {
        try { formHabitos.value = JSON.parse(data.habitosConsumoOtros); } catch(e) {}
      }
    };

    // 🔥 LOGICA DE BORRADOR LOCALSTORAGE 🔥
    watch([historialMedico, formHabitos, listaEnfermedades, listaCirugias, listaMedicamentos, listaFamiliares, listaPatologicos], () => {
      // Solo guardamos borrador si estamos viendo el cuestionario, si hay paciente y si NO estamos editando uno ya existente (id == null)
      if (mostrarCuestionario.value && pacienteEncontrado.value && !historialMedico.value.id) {
        const draft = { hm: historialMedico.value, hab: formHabitos.value, enf: listaEnfermedades.value, cir: listaCirugias.value, med: listaMedicamentos.value, fam: listaFamiliares.value, pat: listaPatologicos.value };
        localStorage.setItem(`historial_draft_${pacienteEncontrado.value.ecu}`, JSON.stringify(draft));
      }
    }, { deep: true });

    const iniciarCuestionario = () => {
      const draftGuardado = localStorage.getItem(`historial_draft_${pacienteEncontrado.value?.ecu}`);
      if (draftGuardado && !historialMedico.value.id) {
        Swal.fire({
          title: 'Borrador encontrado',
          text: "¿Deseas recuperar el historial que estabas llenando?",
          icon: 'info', showCancelButton: true, confirmButtonColor: '#611232', cancelButtonColor: '#888',
          confirmButtonText: 'Sí, recuperar', cancelButtonText: 'Empezar de cero'
        }).then((result) => {
          if (result.isConfirmed) {
            const parsed = JSON.parse(draftGuardado);
            historialMedico.value = parsed.hm; formHabitos.value = parsed.hab; listaEnfermedades.value = parsed.enf;
            listaCirugias.value = parsed.cir; listaMedicamentos.value = parsed.med; listaFamiliares.value = parsed.fam; listaPatologicos.value = parsed.pat;
          } else {
            localStorage.removeItem(`historial_draft_${pacienteEncontrado.value?.ecu}`);
          }
          mostrarCuestionario.value = true;
          seccionAbierta.value = 1;
        });
      } else {
        mostrarCuestionario.value = true;
        seccionAbierta.value = 1;
      }
    };


    // 🔥 LA MAGIA: VIGILANTE QUE CONECTA EL MODAL CON EL DASHBOARD 🔥
    watch(() => props.visible, async (newVal) => {
      if (newVal) {
        if (props.pacientePreCargado) {
          pacienteEncontrado.value = props.pacientePreCargado;
          isSaving.value = true;
          try {
            const res = await historialMedicoService().retrieve();
            const historialExistente = res.data.find((hm: any) => hm.pacienteId === props.pacientePreCargado.id);
            if (historialExistente) {
              cargarHistorial(historialExistente);
              mostrarCuestionario.value = true; // Si existe, mostramos el cuestionario directo
              seccionAbierta.value = 1;
            } else {
              historialMedico.value = initHistorial();
              iniciarCuestionario(); // Si es nuevo, revisamos si hay borrador
            }
          } catch (error) {
            console.error("Error al buscar el historial", error);
          } finally {
            isSaving.value = false;
          }
        }
      } else {
        // Al cerrar, limpiar todo a su estado original
        resetModal();
      }
    });

    const buscarPaciente = async () => {
      if (!ecuSearchString.value) return;
      isSearchingEcu.value = true; pacienteEncontrado.value = null;
      try {
        const res = await pacienteService().retrieve();
        const ecuNumerico = parseInt(ecuSearchString.value, 10);
        const encontrado = res.data.find((p: IPaciente) => p.ecu === ecuNumerico);
        if (encontrado) {
          pacienteEncontrado.value = encontrado;
          Swal.fire({ icon: 'success', title: 'Paciente Encontrado', showConfirmButton: false, timer: 2000 });
        } else {
          Swal.fire({ icon: 'error', title: 'No encontrado', text: 'No existe un paciente con ese ECU.', confirmButtonColor: '#611232' });
        }
      } catch (error: any) {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Error al buscar.', confirmButtonColor: '#611232' });
      } finally {
        isSearchingEcu.value = false;
      }
    };

    const calcularIMC = () => {
      const peso = parseFloat(historialMedico.value.peso || '0');
      const alturaMts = parseFloat(historialMedico.value.altura || '0') / 100;
      if (peso > 0 && alturaMts > 0) historialMedico.value.imc = (peso / (alturaMts * alturaMts)).toFixed(2);
    };

    const resetModal = () => {
      mostrarCuestionario.value = false;
      pacienteEncontrado.value = null;
      ecuSearchString.value = '';
      historialMedico.value = initHistorial();
      formHabitos.value = initHabitos();
      listaEnfermedades.value = []; listaCirugias.value = []; listaMedicamentos.value = []; listaFamiliares.value = []; listaPatologicos.value = [];
      v$.value.$reset();
    };

    const cerrarModal = () => {
      if (mostrarCuestionario.value) {
        Swal.fire({
          title: '¿Pausar estudio?', text: "Tu progreso se guardará como borrador.", icon: 'warning',
          showCancelButton: true, confirmButtonColor: '#888', cancelButtonColor: '#611232', 
          confirmButtonText: 'Sí, salir', cancelButtonText: 'Continuar editando'
        }).then((result) => {
          if (result.isConfirmed) {
            resetModal();
            emit('update:visible', false);
          }
        });
      } else {
        resetModal();
        emit('update:visible', false);
      }
    };

    const save = async () => {
      isSaving.value = true;
      try {
        const hm = historialMedico.value;
        const determinarEstado = () => {
          const sinBiometricos = !hm.altura && !hm.peso && !hm.grupoSanguineo;
          const sinObservaciones = !hm.observacionesGenerales || hm.observacionesGenerales.trim() === '';
          const listasEnDefault = hm.tieneAlergias === false && hm.padeceCronicas === 'NO' && hm.haTenidoCirugias === 'NO' && hm.tomaMedicamentos === 'NO' && hm.tieneFamiliares === 'NO' && hm.tienePatologicos === 'NO';
          if (sinBiometricos && sinObservaciones && listasEnDefault) return 'VACIO';
          const biometricosListos = !!hm.altura && !!hm.peso;
          const alergiasOk = hm.tieneAlergias === false || (hm.tieneAlergias === true && !!hm.sustanciaAlergia);
          const cronicasOk = hm.padeceCronicas !== 'SI' || (hm.padeceCronicas === 'SI' && listaEnfermedades.value.length > 0);
          const cirugiasOk = hm.haTenidoCirugias !== 'SI' || (hm.haTenidoCirugias === 'SI' && listaCirugias.value.length > 0);
          const medicamentosOk = hm.tomaMedicamentos !== 'SI' || (hm.tomaMedicamentos === 'SI' && listaMedicamentos.value.length > 0);
          const familiaresOk = hm.tieneFamiliares !== 'SI' || (hm.tieneFamiliares === 'SI' && listaFamiliares.value.length > 0);
          const patologicosOk = hm.tienePatologicos !== 'SI' || (hm.tienePatologicos === 'SI' && listaPatologicos.value.length > 0);
          if (biometricosListos && alergiasOk && cronicasOk && cirugiasOk && medicamentosOk && familiaresOk && patologicosOk) return 'COMPLETO';
          return 'INCOMPLETO';
        };

        const payload: any = { id: hm.id, observacionesGenerales: hm.observacionesGenerales || '', estado: determinarEstado() };
        payload.datosBiometricosSanguineos = JSON.stringify({ altura: hm.altura, peso: hm.peso, imc: hm.imc, grupoSanguineo: hm.grupoSanguineo, factorRh: hm.factorRh });
        let detallesAlergia: any[] = [];
        if (hm.tieneAlergias === true) {
          detallesAlergia = [{ tipoAlergia: hm.tipoAlergia, sustanciaAlergia: hm.sustanciaAlergia, reaccionesAlergia: hm.reaccionesAlergia, gravedadAlergia: hm.gravedadAlergia, tratamientoAlergia: hm.tratamientoAlergia, ultimoEpisodioAlergia: hm.ultimoEpisodioAlergia }];
        }
        payload.alergias = toJson(hm.tieneAlergias, detallesAlergia);
        payload.enfermedadesCronicas = toJson(hm.padeceCronicas, listaEnfermedades.value);
        payload.cirugiasPrevias = toJson(hm.haTenidoCirugias, listaCirugias.value);
        payload.medicamentosActuales = toJson(hm.tomaMedicamentos, listaMedicamentos.value);
        payload.antecedentesFamiliaresHereditarios = toJson(hm.tieneFamiliares, listaFamiliares.value);
        payload.antecedentesPersonalesPatologicos = toJson(hm.tienePatologicos, listaPatologicos.value);
        payload.habitosConsumoOtros = JSON.stringify(formHabitos.value || {});

        if (pacienteEncontrado.value) {
            payload.pacienteId = pacienteEncontrado.value.id; payload.pacienteEcu = pacienteEncontrado.value.ecu;
            payload.pacienteNombre = pacienteEncontrado.value.nombre; payload.pacienteApellidoPaterno = pacienteEncontrado.value.apellidoPaterno;
        }

        if (payload.id) await historialMedicoService().update(payload);
        else await historialMedicoService().create(payload);

        // Limpiar el borrador si el guardado fue exitoso
        localStorage.removeItem(`historial_draft_${pacienteEncontrado.value?.ecu}`);

        Swal.fire({ icon: 'success', title: '¡Guardado Exitoso!', text: `Historial guardado como ${payload.estado}`, showConfirmButton: false, timer: 2000 });
        emit('saved');
        resetModal();
        emit('update:visible', false);
      } catch (error: any) {
        Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo guardar el historial.', confirmButtonColor: '#611232' });
      } finally {
        isSaving.value = false;
      }
    };

    return {
      t$, v$, historialMedico, formHabitos, pacienteEncontrado, ecuSearchString, isSearchingEcu, isSaving, 
      mostrarCuestionario, seccionAbierta, toggleSeccion, iniciarCuestionario,
      buscarPaciente, calcularIMC, save, cerrarModal, cargarHistorial,
      listaEnfermedades, agregarEnfermedad, eliminarEnfermedad, manejarCambioCronicas,
      listaCirugias, agregarCirugia, eliminarCirugia, manejarCambioCirugias,
      listaMedicamentos, agregarMedicamento, eliminarMedicamento, manejarCambioMedicamentos,
      listaFamiliares, agregarFamiliar, eliminarFamiliar, manejarCambioFamiliares,
      listaPatologicos, agregarPatologico, eliminarPatologico, manejarCambioPatologicos, limpiarAlergias
    };
  }
});