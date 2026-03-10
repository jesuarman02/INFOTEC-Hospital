import { ref, defineComponent, inject, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import useVuelidate from '@vuelidate/core';
import { required } from '@vuelidate/validators';

// Importamos el servicio y modelo del paciente tal como lo tienes en dirección
import PacienteService from '../paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistorialMedicoUpdate',
  setup() {
    const { t: t$ } = useI18n();
    
    // Inyectamos los servicios correctamente
    const alertService = inject('alertService', () => useAlertService(), true);
    const historialMedicoService = inject('historialMedicoService') as any;
    const pacienteService = inject('pacienteService', () => new PacienteService()); 

    // Estado del buscador
    const ecuSearchString = ref('');
    const isSearchingEcu = ref(false);
    const pacienteEncontrado: Ref<IPaciente | null> = ref(null);
    const isSaving = ref(false);

    // Entidad Historial Medico
    const historialMedico = ref<any>({
      id: null,
      pacienteId: null,
      altura: null,
      peso: null,
      imc: null,
      grupoSanguineo: null,
      factorRh: null,
      alergias: '',
      padecimientos: '',
      enfermedadesCronicas: '',
      cirugiasPrevias: '',
      medicamentosActuales: '',
      antecedentesFamiliares: '',
      antecedentesPatologicos: '',
      antecedentesNoPatologicos: '',
      antecedentesGinecoObstetricos: '',
      consumoTabaco: false,
      consumoAlcohol: false,
      consumoDrogas: false,
      vacunas: '',
      discapacidad: '',
      observacionesGenerales: '',
      fechaRegistroHistorial: new Date()
    });

    // Validaciones básicas
    const rules = {
      altura: { required },
      peso: { required }
    };
    const v$ = useVuelidate(rules, historialMedico);

    // Función corregida: usa retrieve() y filtra por ECU igual que en dirección
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
          historialMedico.value.pacienteId = encontrado.id; // Vincular historial al paciente
          alertService.showSuccess(`Paciente encontrado correctamente.`);
        } else {
          alertService.showError('No se encontró ningún paciente registrado con ese número de ECU.');
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      } finally {
        isSearchingEcu.value = false;
      }
    };

    // Calcular IMC automáticamente
    const calcularIMC = () => {
      const pesoStr = historialMedico.value.peso?.toString() || '0';
      const alturaStr = historialMedico.value.altura?.toString() || '0';
      
      const peso = parseFloat(pesoStr);
      const altura = parseFloat(alturaStr);

      if (peso > 0 && altura > 0) {
        const calculo = peso / (altura * altura);
        historialMedico.value.imc = calculo.toFixed(2);
      } else {
        historialMedico.value.imc = null;
      }
    };

    const previousState = () => {
      window.history.back();
    };

    const save = async () => {
      isSaving.value = true;
      try {
        if (historialMedico.value.id) {
          await historialMedicoService().update(historialMedico.value); // Asumiendo que también requiere paréntesis
        } else {
          await historialMedicoService().create(historialMedico.value); // Asumiendo que también requiere paréntesis
        }
        previousState();
      } catch (error: any) {
        console.error("Error guardando el historial", error);
      } finally {
        isSaving.value = false;
      }
    };

    return {
      t$,
      v$,
      ecuSearchString,
      isSearchingEcu,
      pacienteEncontrado,
      historialMedico,
      isSaving,
      buscarPaciente,
      calcularIMC,
      save,
      previousState
    };
  }
});