import { ref, defineComponent, inject, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import useVuelidate from '@vuelidate/core';
import { required } from '@vuelidate/validators';

import PacienteService from '../paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistorialMedicoUpdate',
  setup() {
    const { t: t$ } = useI18n();
    
    const alertService = inject('alertService', () => useAlertService(), true);
    const historialMedicoService = inject('historialMedicoService') as any;
    const pacienteService = inject('pacienteService', () => new PacienteService()); 

    const ecuSearchString = ref('');
    const isSearchingEcu = ref(false);
    const pacienteEncontrado: Ref<IPaciente | null> = ref(null);
    const isSaving = ref(false);

    const historialMedico = ref<any>({
      id: null,
      pacienteId: null,
      altura: null, 
      peso: null,
      imc: null,
      grupoSanguineo: null,
      factorRh: null,
      
      tieneAlergias: null,
      tipoAlergia: null,
      sustanciaAlergia: '',
      reaccionesAlergia: [], 
      gravedadAlergia: null,
      tratamientoAlergia: null,
      ultimoEpisodioAlergia: null,
      
      padeceCronicas: null,
      enfermedadesCronicas: '', 
      
      haTenidoCirugias: null,
      cirugiasPrevias: '', 

      tomaMedicamentos: null,
      medicamentosActuales: '',

      tieneFamiliares: null,
      antecedentesFamiliares: '',

      tienePatologicos: null,
      antecedentesPatologicos: '',

      antecedentesNoPatologicos: '',
      antecedentesGinecoObstetricos: '',
      padecimientos: '',
      
      // Los dejaremos para satisfacer a la BD, pero la UI usará 'formHabitos'
      consumoTabaco: false,
      consumoAlcohol: false,
      consumoDrogas: false,
      vacunas: '',
      discapacidad: '',
      observacionesGenerales: '',
      fechaRegistroHistorial: new Date()
    });

    // NUEVO OBJETO: Controlará la interfaz de Hábitos
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
          const resHistorial = await historialMedicoService().retrieve();
          const yaTieneHistorial = resHistorial.data.some((historial: any) => 
            (historial.paciente && historial.paciente.id === encontrado.id) || 
            historial.pacienteId === encontrado.id 
          );

          if (yaTieneHistorial) {
            alertService.showError(`El paciente con ECU ${ecuNumerico} ya tiene un Historial Médico. Vaya a la lista si desea editarlo.`);
          } else {
            pacienteEncontrado.value = encontrado;
            alertService.showSuccess('Paciente cargado. Puede comenzar a registrar el historial médico.');
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

    const limpiarDecimalesYCalcularIMC = (event: Event, campo: string) => {
      const target = event.target as HTMLInputElement;
      if (target) {
        let valorLimpio = target.value.replace(/\D/g, '');
        if (Number(valorLimpio) > 250) valorLimpio = '250';
        historialMedico.value[campo] = valorLimpio;
        target.value = valorLimpio;
        calcularIMC();
      }
    };

    const limpiarNumerosYCalcularIMC = (event: Event, campo: string) => {
      const target = event.target as HTMLInputElement;
      if (target) {
        let valorLimpio = target.value.replace(/[^0-9.]/g, '').replace(/(\..*?)\..*/g, '$1');
        if (Number(valorLimpio) > 350) valorLimpio = '350';
        historialMedico.value[campo] = valorLimpio;
        target.value = valorLimpio;
        calcularIMC();
      }
    };

    const calcularIMC = () => {
      const pesoStr = historialMedico.value.peso?.toString() || '0';
      const alturaStr = historialMedico.value.altura?.toString() || '0';
      const peso = parseFloat(pesoStr);
      const alturaCm = parseFloat(alturaStr);
      if (peso > 0 && alturaCm > 0) {
        const alturaMts = alturaCm / 100;
        const calculo = peso / (alturaMts * alturaMts);
        historialMedico.value.imc = calculo.toFixed(2);
      } else {
        historialMedico.value.imc = null;
      }
    };

    const limpiarAlergias = () => {
      if (historialMedico.value.tieneAlergias === false) {
        historialMedico.value.tipoAlergia = null;
        historialMedico.value.sustanciaAlergia = '';
        historialMedico.value.reaccionesAlergia = [];
        historialMedico.value.gravedadAlergia = null;
        historialMedico.value.tratamientoAlergia = null;
        historialMedico.value.ultimoEpisodioAlergia = null;
      }
    };

    const listaEnfermedades = ref<any[]>([]);
    const agregarEnfermedad = () => listaEnfermedades.value.push({ tipo: null, nombre: '', diagnostico: '', estado: null, tratamiento: '', medicamentos: '', medico: '', hospital: '', observaciones: '' });
    const eliminarEnfermedad = (index: number) => listaEnfermedades.value.splice(index, 1);
    const manejarCambioCronicas = () => {
      if (historialMedico.value.padeceCronicas === 'SI') { if (listaEnfermedades.value.length === 0) agregarEnfermedad(); } 
      else { listaEnfermedades.value = []; }
    };

    const listaCirugias = ref<any[]>([]);
    const agregarCirugia = () => listaCirugias.value.push({ tipo: null, nombre: '', anio: '', motivo: '', huboComplicaciones: 'NO', descripcionComplicaciones: '' });
    const eliminarCirugia = (index: number) => listaCirugias.value.splice(index, 1);
    const manejarCambioCirugias = () => {
      if (historialMedico.value.haTenidoCirugias === 'SI') { if (listaCirugias.value.length === 0) agregarCirugia(); } 
      else { listaCirugias.value = []; }
    };

    const listaMedicamentos = ref<any[]>([]);
    const agregarMedicamento = () => listaMedicamentos.value.push({ nombre: '', motivo: '', frecuencia: null });
    const eliminarMedicamento = (index: number) => listaMedicamentos.value.splice(index, 1);
    const manejarCambioMedicamentos = () => {
      if (historialMedico.value.tomaMedicamentos === 'SI') { if (listaMedicamentos.value.length === 0) agregarMedicamento(); } 
      else { listaMedicamentos.value = []; }
    };

    const listaFamiliares = ref<any[]>([]);
    const agregarFamiliar = () => listaFamiliares.value.push({ enfermedad: null, parentezco: null });
    const eliminarFamiliar = (index: number) => listaFamiliares.value.splice(index, 1);
    const manejarCambioFamiliares = () => {
      if (historialMedico.value.tieneFamiliares === 'SI') { if (listaFamiliares.value.length === 0) agregarFamiliar(); } 
      else { listaFamiliares.value = []; }
    };

    const listaPatologicos = ref<any[]>([]);
    const agregarPatologico = () => listaPatologicos.value.push({ enfermedad: '', anio: '' });
    const eliminarPatologico = (index: number) => listaPatologicos.value.splice(index, 1);
    const manejarCambioPatologicos = () => {
      if (historialMedico.value.tienePatologicos === 'SI') { if (listaPatologicos.value.length === 0) agregarPatologico(); } 
      else { listaPatologicos.value = []; }
    };

    const previousState = () => {
      window.history.back();
    };

    const save = async () => {
      isSaving.value = true;

      // EMPAQUETADO GENERAL DE LISTAS
      const empaquetar = (condicion: string, lista: any[], variableBd: string, textoVacio: string) => {
        if (condicion === 'SI' && lista.length > 0) {
          historialMedico.value[variableBd] = JSON.stringify(lista);
        } else if (condicion === 'NO') {
          historialMedico.value[variableBd] = textoVacio;
        } else {
          historialMedico.value[variableBd] = null;
        }
      };

      empaquetar(historialMedico.value.padeceCronicas, listaEnfermedades.value, 'enfermedadesCronicas', 'Ninguna');
      empaquetar(historialMedico.value.haTenidoCirugias, listaCirugias.value, 'cirugiasPrevias', 'Ninguna');
      empaquetar(historialMedico.value.tomaMedicamentos, listaMedicamentos.value, 'medicamentosActuales', 'Ninguno');
      empaquetar(historialMedico.value.tieneFamiliares, listaFamiliares.value, 'antecedentesFamiliares', 'Ninguno');
      empaquetar(historialMedico.value.tienePatologicos, listaPatologicos.value, 'antecedentesPatologicos', 'Ninguno');

      // --- TRUCO MAESTRO: EMPAQUETADO DE HÁBITOS, VACUNAS Y DISCAPACIDAD ---
      
      // 1. Convertimos tus selecciones a booleanos para satisfacer a la Base de Datos
      historialMedico.value.consumoTabaco = (formHabitos.value.tabaco === 'Fuma actualmente');
      historialMedico.value.consumoAlcohol = (formHabitos.value.alcohol === 'Ocasional' || formHabitos.value.alcohol === 'Regular');
      historialMedico.value.consumoDrogas = (formHabitos.value.drogas === 'Sí');

      // 2. Generamos un resumen enriquecido para no perder la información vital
      let resumenHabitos = `TABACO: ${formHabitos.value.tabaco}`;
      if (formHabitos.value.tabaco === 'Fuma actualmente') resumenHabitos += ` (${formHabitos.value.tabacoFrecuencia})`;
      
      resumenHabitos += ` | ALCOHOL: ${formHabitos.value.alcohol}`;
      if (formHabitos.value.alcohol !== 'No') resumenHabitos += ` (${formHabitos.value.alcoholFrecuencia})`;
      
      resumenHabitos += ` | DROGAS: ${formHabitos.value.drogas}`;
      if (formHabitos.value.drogas === 'Sí') resumenHabitos += ` (${formHabitos.value.drogasTipo}, ${formHabitos.value.drogasFrecuencia})`;
      
      // 3. Lo metemos limpio y formateado al inicio del campo Antecedentes No Patologicos
      const textoPrevio = historialMedico.value.antecedentesNoPatologicos ? `\n\nOtros antecedentes anotados:\n${historialMedico.value.antecedentesNoPatologicos}` : '';
      historialMedico.value.antecedentesNoPatologicos = resumenHabitos + textoPrevio;

      // 4. Vacunas y Discapacidad tienen sus propias columnas de texto
      historialMedico.value.vacunas = `Esquema: ${formHabitos.value.esquemaVacunacion}. Recientes/Importantes: ${formHabitos.value.vacunasRecientes || 'Ninguna especificiada'}`;
      historialMedico.value.discapacidad = formHabitos.value.tieneDiscapacidad === 'Sí' ? formHabitos.value.tipoDiscapacidad : 'Ninguna';

      try {
        if (historialMedico.value.id) {
          await historialMedicoService().update(historialMedico.value); 
        } else {
          if (pacienteEncontrado.value) {
            historialMedico.value.pacienteId = pacienteEncontrado.value.id;
          }
          await historialMedicoService().create(historialMedico.value); 
        }
        previousState();
      } catch (error: any) {
        console.error("Error guardando el historial", error);
        alertService.showHttpError(error.response);
      } finally {
        isSaving.value = false;
      }
    };

    return {
      t$, v$, ecuSearchString, isSearchingEcu, pacienteEncontrado, historialMedico, formHabitos, isSaving, buscarPaciente,
      limpiarDecimalesYCalcularIMC, limpiarNumerosYCalcularIMC, calcularIMC, limpiarAlergias,
      
      listaEnfermedades, agregarEnfermedad, eliminarEnfermedad, manejarCambioCronicas,
      listaCirugias, agregarCirugia, eliminarCirugia, manejarCambioCirugias,
      listaMedicamentos, agregarMedicamento, eliminarMedicamento, manejarCambioMedicamentos,
      listaFamiliares, agregarFamiliar, eliminarFamiliar, manejarCambioFamiliares,
      listaPatologicos, agregarPatologico, eliminarPatologico, manejarCambioPatologicos,

      save, previousState
    };
  }
});