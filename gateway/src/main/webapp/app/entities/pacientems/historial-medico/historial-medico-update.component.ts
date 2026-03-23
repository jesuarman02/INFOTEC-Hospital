import { ref, defineComponent, inject, type Ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import useVuelidate from '@vuelidate/core';
import { required } from '@vuelidate/validators';
import { useRoute } from 'vue-router';

import PacienteService from '../paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistorialMedicoUpdate',
  setup(props, { emit }) {
    const { t: t$ } = useI18n();

    const alertService = inject('alertService', () => useAlertService(), true);
    const historialMedicoService = inject('historialMedicoService') as any;
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const route = useRoute();

    const ecuSearchString = ref('');
    const isSearchingEcu = ref(false);
    const pacienteEncontrado: Ref<IPaciente | null> = ref(null);
    const isSaving = ref(false);

    // 🔥 HELPERS JSON
    const toJson = (tiene: boolean | string, detalles: any[]) => {
      const tieneBool = tiene === true || tiene === 'SI';
      return JSON.stringify({
        tiene: tieneBool,
        detalles: tieneBool ? (detalles || []) : [] 
      });
    };

    const fromJson = (json: string | null | undefined) => {
      if (!json) return { tiene: false, detalles: [] };
      try {
        return JSON.parse(json);
      } catch (e) {
        return { tiene: false, detalles: [] };
      }
    };

    // FORM PRINCIPAL (Incluye los campos sueltos de tu HTML)
    const historialMedico = ref<any>({
      id: null,
      
      // Biométricos
      altura: null,
      peso: null,
      imc: null,
      grupoSanguineo: null,
      factorRh: null,

      // Alergias (Campos sueltos de tu HTML)
      tieneAlergias: false,
      tipoAlergia: null,
      sustanciaAlergia: '',
      reaccionesAlergia: [],
      gravedadAlergia: null,
      tratamientoAlergia: null,
      ultimoEpisodioAlergia: null,

      // Banderas de las demás secciones
      padeceCronicas: 'NO',
      haTenidoCirugias: 'NO',
      tomaMedicamentos: 'NO',
      tieneFamiliares: 'NO',
      tienePatologicos: 'NO',

      observacionesGenerales: '',
    });

    // HÁBITOS
    const formHabitos = ref({
      tabaco: 'No fuma',
      tabacoFrecuencia: null,
      alcohol: 'No',
      alcoholFrecuencia: null,
      drogas: 'No',
      drogasTipo: '',
      drogasFrecuencia: null,
      esquemaVacunacion: 'Desconoce',
      vacunasRecientes: '',
      tieneDiscapacidad: 'No',
      tipoDiscapacidad: null
    });

    const rules = {
      altura: { required },
      peso: { required }
    };
    const v$ = useVuelidate(rules, historialMedico);

    // LISTAS DINÁMICAS (Para las otras secciones)
    const listaEnfermedades = ref<any[]>([]);
    const listaCirugias = ref<any[]>([]);
    const listaMedicamentos = ref<any[]>([]);
    const listaFamiliares = ref<any[]>([]);
    const listaPatologicos = ref<any[]>([]);

    // 🔥 FUNCIONES DE UI
    const limpiarAlergias = () => { 
      if (!historialMedico.value.tieneAlergias) {
        historialMedico.value.tipoAlergia = null;
        historialMedico.value.sustanciaAlergia = '';
        historialMedico.value.reaccionesAlergia = [];
        historialMedico.value.gravedadAlergia = null;
        historialMedico.value.tratamientoAlergia = null;
        historialMedico.value.ultimoEpisodioAlergia = null;
      } 
    };
    
    // (Resto de funciones de agregar/quitar de listas)
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
      
      if (data.datosBiometricosSanguineos) {
        const parsed = fromJson(data.datosBiometricosSanguineos); 
        historialMedico.value.altura = parsed.altura || null;
        historialMedico.value.peso = parsed.peso || null;
        historialMedico.value.imc = parsed.imc || null;
        historialMedico.value.grupoSanguineo = parsed.grupoSanguineo || null;
        historialMedico.value.factorRh = parsed.factorRh || null;
      }

      // Desempaquetar Alergias
      if (data.alergias) {
        const parsed = fromJson(data.alergias);
        historialMedico.value.tieneAlergias = parsed.tiene;
        if (parsed.tiene && parsed.detalles.length > 0) {
          const al = parsed.detalles[0]; // Extraemos el primer objeto
          historialMedico.value.tipoAlergia = al.tipoAlergia;
          historialMedico.value.sustanciaAlergia = al.sustanciaAlergia;
          historialMedico.value.reaccionesAlergia = al.reaccionesAlergia || [];
          historialMedico.value.gravedadAlergia = al.gravedadAlergia;
          historialMedico.value.tratamientoAlergia = al.tratamientoAlergia;
          historialMedico.value.ultimoEpisodioAlergia = al.ultimoEpisodioAlergia;
        }
      }

      if (data.enfermedadesCronicas) {
        const parsed = fromJson(data.enfermedadesCronicas);
        historialMedico.value.padeceCronicas = parsed.tiene ? 'SI' : 'NO';
        listaEnfermedades.value = parsed.detalles || [];
      }

      if (data.cirugiasPrevias) {
        const parsed = fromJson(data.cirugiasPrevias);
        historialMedico.value.haTenidoCirugias = parsed.tiene ? 'SI' : 'NO';
        listaCirugias.value = parsed.detalles || [];
      }

      if (data.medicamentosActuales) {
        const parsed = fromJson(data.medicamentosActuales);
        historialMedico.value.tomaMedicamentos = parsed.tiene ? 'SI' : 'NO';
        listaMedicamentos.value = parsed.detalles || [];
      }

      if (data.antecedentesFamiliaresHereditarios) {
        const parsed = fromJson(data.antecedentesFamiliaresHereditarios);
        historialMedico.value.tieneFamiliares = parsed.tiene ? 'SI' : 'NO';
        listaFamiliares.value = parsed.detalles || [];
      }

      if (data.antecedentesPersonalesPatologicos) {
        const parsed = fromJson(data.antecedentesPersonalesPatologicos);
        historialMedico.value.tienePatologicos = parsed.tiene ? 'SI' : 'NO';
        listaPatologicos.value = parsed.detalles || [];
      }

      if (data.habitosConsumoOtros) {
        try { formHabitos.value = JSON.parse(data.habitosConsumoOtros); } catch(e) {}
      }
    };

    onMounted(async () => {
        if (route.params?.historialMedicoId) {
            try {
                const res = await historialMedicoService().find(route.params.historialMedicoId);
                cargarHistorial(res);
            } catch(error) {
                console.error("Error al cargar para edición", error);
            }
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
          pacienteEncontrado.value = encontrado;
          alertService.showSuccess('Paciente cargado.');
        } else {
          alertService.showError('No se encontró ningún paciente.');
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      } finally {
        isSearchingEcu.value = false;
      }
    };

    const calcularIMC = () => {
      const peso = parseFloat(historialMedico.value.peso || '0');
      const alturaMts = parseFloat(historialMedico.value.altura || '0') / 100;
      if (peso > 0 && alturaMts > 0) {
        historialMedico.value.imc = (peso / (alturaMts * alturaMts)).toFixed(2);
      }
    };

    const previousState = () => window.history.back();

    // 🔥 SAVE DEFINITIVO (EMPAQUETADO DE TODOS LOS JSON)
// 🔥 SAVE DEFINITIVO (CON LÓGICA DE ESTADOS)
    const save = async () => {
      isSaving.value = true;
      try {
        const hm = historialMedico.value;

        // 🚨 1. LÓGICA DE EVALUACIÓN DE ESTADO 🚨
        const determinarEstado = () => {
          // A) ¿Está completamente VACÍO?
          const sinBiometricos = !hm.altura && !hm.peso && !hm.grupoSanguineo;
          const sinObservaciones = !hm.observacionesGenerales || hm.observacionesGenerales.trim() === '';
          const listasEnDefault = hm.tieneAlergias === false && 
                                  hm.padeceCronicas === 'NO' && 
                                  hm.haTenidoCirugias === 'NO' && 
                                  hm.tomaMedicamentos === 'NO' && 
                                  hm.tieneFamiliares === 'NO' && 
                                  hm.tienePatologicos === 'NO';

          if (sinBiometricos && sinObservaciones && listasEnDefault) {
            return 'VACIO';
          }

          // B) ¿Está COMPLETO? (Cumple los biométricos básicos y no dejó listas vacías por error)
          const biometricosListos = !!hm.altura && !!hm.peso;
          
          // Verificamos que si marcó "Sí" en algo, realmente haya llenado el detalle
          const alergiasOk = hm.tieneAlergias === false || (hm.tieneAlergias === true && !!hm.sustanciaAlergia);
          const cronicasOk = hm.padeceCronicas !== 'SI' || (hm.padeceCronicas === 'SI' && listaEnfermedades.value.length > 0);
          const cirugiasOk = hm.haTenidoCirugias !== 'SI' || (hm.haTenidoCirugias === 'SI' && listaCirugias.value.length > 0);
          const medicamentosOk = hm.tomaMedicamentos !== 'SI' || (hm.tomaMedicamentos === 'SI' && listaMedicamentos.value.length > 0);
          const familiaresOk = hm.tieneFamiliares !== 'SI' || (hm.tieneFamiliares === 'SI' && listaFamiliares.value.length > 0);
          const patologicosOk = hm.tienePatologicos !== 'SI' || (hm.tienePatologicos === 'SI' && listaPatologicos.value.length > 0);

          const todosLosBloquesOk = alergiasOk && cronicasOk && cirugiasOk && medicamentosOk && familiaresOk && patologicosOk;

          if (biometricosListos && todosLosBloquesOk) {
            return 'COMPLETO';
          }

          // C) Si no está vacío, pero tampoco cumple todo para estar completo...
          return 'INCOMPLETO';
        };

        // 🚨 2. ARMADO DEL PAYLOAD 🚨
        const payload: any = {
          id: hm.id,
          observacionesGenerales: hm.observacionesGenerales || '',
          estado: determinarEstado(), // <-- AQUÍ SE GUARDA EL ESTADO AUTOMÁTICAMENTE
        };

        payload.datosBiometricosSanguineos = JSON.stringify({
          altura: hm.altura,
          peso: hm.peso,
          imc: hm.imc,
          grupoSanguineo: hm.grupoSanguineo,
          factorRh: hm.factorRh
        });

        // EMPAQUETAR ALERGIAS
        let detallesAlergia: any[] = [];
        if (hm.tieneAlergias === true) {
          detallesAlergia = [{
            tipoAlergia: hm.tipoAlergia,
            sustanciaAlergia: hm.sustanciaAlergia,
            reaccionesAlergia: hm.reaccionesAlergia,
            gravedadAlergia: hm.gravedadAlergia,
            tratamientoAlergia: hm.tratamientoAlergia,
            ultimoEpisodioAlergia: hm.ultimoEpisodioAlergia
          }];
        }
        payload.alergias = toJson(hm.tieneAlergias, detallesAlergia);

        // EMPAQUETAR LAS DEMÁS LISTAS
        payload.enfermedadesCronicas = toJson(hm.padeceCronicas, listaEnfermedades.value);
        payload.cirugiasPrevias = toJson(hm.haTenidoCirugias, listaCirugias.value);
        payload.medicamentosActuales = toJson(hm.tomaMedicamentos, listaMedicamentos.value);
        payload.antecedentesFamiliaresHereditarios = toJson(hm.tieneFamiliares, listaFamiliares.value);
        payload.antecedentesPersonalesPatologicos = toJson(hm.tienePatologicos, listaPatologicos.value);
        
        // Hábitos
        payload.habitosConsumoOtros = JSON.stringify(formHabitos.value || {});

        if (pacienteEncontrado.value) {
            payload.pacienteId = pacienteEncontrado.value.id;
            payload.pacienteEcu = pacienteEncontrado.value.ecu;
        // Asegúrate de que las variables se llamen así en tu modelo de paciente
            payload.pacienteNombre = pacienteEncontrado.value.nombre; 
            payload.pacienteApellidoPaterno = pacienteEncontrado.value.apellidoPaterno;
        }

        console.log("Payload a enviar (revisa el campo 'estado'):", payload);

        if (payload.id) {
          await historialMedicoService().update(payload);
        } else {
          await historialMedicoService().create(payload);
        }

        alertService.showSuccess("Historial guardado correctamente con estado: " + payload.estado);
        previousState();
      } catch (error: any) {
        console.error("Error completo:", error);
        alertService.showHttpError(error.response);
      } finally {
        isSaving.value = false;
      }
    };

    return {
      t$, v$, historialMedico, formHabitos, pacienteEncontrado, ecuSearchString, isSearchingEcu, isSaving, 
      buscarPaciente, calcularIMC, save, previousState, cargarHistorial,
      
      listaEnfermedades, agregarEnfermedad, eliminarEnfermedad, manejarCambioCronicas,
      listaCirugias, agregarCirugia, eliminarCirugia, manejarCambioCirugias,
      listaMedicamentos, agregarMedicamento, eliminarMedicamento, manejarCambioMedicamentos,
      listaFamiliares, agregarFamiliar, eliminarFamiliar, manejarCambioFamiliares,
      listaPatologicos, agregarPatologico, eliminarPatologico, manejarCambioPatologicos,
      limpiarAlergias
    };
  }
});