import { computed, defineComponent, inject, ref, type Ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';
// Importamos los validadores necesarios
import { required, minLength, maxLength, numeric } from '@vuelidate/validators';

import DireccionService from './direccion.service';
import { useValidation } from '@/shared/composables/validation';
import { useAlertService } from '@/shared/alert/alert.service';

import TipoVialidadService from '@/entities/pacientesms/tipo-vialidad/tipo-vialidad.service';
import { type ITipoVialidad } from '@/shared/model/pacientesms/tipo-vialidad.model';

import CodigoPostalService from '@/entities/pacientesms/codigo-postal/codigo-postal.service';
import { type ICodigoPostal } from '@/shared/model/pacientesms/codigo-postal.model';

import { type IDireccion, Direccion } from '@/shared/model/pacientems/direccion.model';

import PacienteService from '../paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DireccionUpdate',
  setup() {
    const { t: t$ } = useI18n();
    const route = useRoute();
    const router = useRouter();
    const direccionService = inject('direccionService', () => new DireccionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const direccion: Ref<IDireccion> = ref(new Direccion());

    const tipoVialidadService = inject('tipoVialidadService', () => new TipoVialidadService());
    const tipoVialidads: Ref<ITipoVialidad[]> = ref([]);

    const codigoPostalService = inject('codigoPostalService', () => new CodigoPostalService());
    
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const ecuSearchString = ref('');
    const pacienteEncontrado: Ref<IPaciente | null> = ref(null);
    const isSearchingEcu = ref(false);

    const cpSearchString = ref(''); 
    const coloniasOptions: Ref<ICodigoPostal[]> = ref([]); 
    const municipioDisplay = ref(''); 
    const estadoDisplay = ref('');    
    const isSearchingCP = ref(false);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    // MODIFICACIÓN: Reglas estrictas de validación
    const validationRules = {
      nombreVialidad: { required }, 
      numExterior: { required },    
      numInterior: {}, // Opcional
      telefono: { 
        required,
        numeric,
        minLength: minLength(10),
        maxLength: maxLength(10)
      },
      tipoVialidad: { required },   
      codigoPostalInfo: { required }, 
    };
    const v$ = useVuelidate(validationRules, direccion as any);
    //const { resolveJsonI18nKey } = useValidation();

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

    const searchByZipCode = async () => {
      const cpValue = cpSearchString.value;

      if (!cpValue || cpValue.length !== 5) {
        return; 
      }

      const cpNumber = parseInt(cpValue, 10);
      if (isNaN(cpNumber) || cpNumber < 1000 || cpNumber > 99998) {
        alertService.showError('Código Postal inválido.');
        coloniasOptions.value = [];
        return;
      }

      isSearchingCP.value = true;
      coloniasOptions.value = []; 

      try {
        const res = await codigoPostalService().retrieveByCP(cpValue);
        coloniasOptions.value = res.data;
        
        if (coloniasOptions.value.length > 0) {
           alertService.showSuccess('Datos del Código Postal cargados correctamente.');
           if (coloniasOptions.value.length === 1) {
             direccion.value.codigoPostalInfo = coloniasOptions.value[0];
             updateLocationDetails();
           }
        } else {
           alertService.showInfo('No se encontraron colonias asociadas a este Código Postal.');
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      } finally {
        isSearchingCP.value = false;
      }
    };
    // Función para forzar mayúsculas mientras el usuario escribe
    const onInputUpper = (event: Event, field: any) => {
      const target = event.target as HTMLInputElement | null;
      if (!target) return;
      field.$model = target.value.toUpperCase();
    };

    watch(cpSearchString, (newVal) => {
      if (newVal && newVal.length === 5) {
        searchByZipCode();
      }
      if (newVal && newVal.length < 5) {
        coloniasOptions.value = [];
        municipioDisplay.value = '';
        estadoDisplay.value = '';
        direccion.value.codigoPostalInfo = null;
      }
    });

    const updateLocationDetails = () => {
      const selectedCP = direccion.value.codigoPostalInfo;
      if (selectedCP) {
        municipioDisplay.value = selectedCP.municipio || '';
        estadoDisplay.value = selectedCP.estado || '';
      } else {
        municipioDisplay.value = '';
        estadoDisplay.value = '';
      }
    };

    const retrieveDireccion = async (direccionId: number) => {      
      try {
        const res = await direccionService().find(direccionId);
        direccion.value = res;
        
        if (direccion.value.codigoPostalInfo) {
           cpSearchString.value = direccion.value.codigoPostalInfo.codigo || '';
           municipioDisplay.value = direccion.value.codigoPostalInfo.municipio || '';
           estadoDisplay.value = direccion.value.codigoPostalInfo.estado || '';
           coloniasOptions.value = [direccion.value.codigoPostalInfo];
        }
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    const initRelationships = async () => {
      try {
        const resTipoVialidad = await tipoVialidadService().retrieve();
        tipoVialidads.value = resTipoVialidad.data;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    initRelationships();

    const previousState = () => router.go(-1);

      const save = async () => {
      const isFormValid = await v$.value.$validate();
      if (!isFormValid) {
        alertService.showError('Por favor verifica los campos requeridos en rojo.');
        return;
      }
      // --- INICIO CAMBIO S/N ---
      if (!direccion.value.numInterior || direccion.value.numInterior.trim() === '') {
        direccion.value.numInterior = 'S/N';
      }
      // --- FIN CAMBIO S/N ---

      isSaving.value = true;
      try {

        if (direccion.value.tipoVialidad) {
          (direccion.value as any).tipoVialidadId = direccion.value.tipoVialidad.id;
        }
        if (direccion.value.codigoPostalInfo) {
          (direccion.value as any).codigoPostalInfoId = direccion.value.codigoPostalInfo.id;
        }

        let direccionGuardada;

        // --- 2. GUARDAR LA DIRECCIÓN ---
        if (direccion.value.id) {
          direccionGuardada = await direccionService().update(direccion.value);
        } else {
          direccionGuardada = await direccionService().create(direccion.value);
        }

// --- INICIO MAGIA DE VINCULACIÓN CORREGIDA ---
        // Aseguramos que tengamos el paciente y la dirección creada exitosamente con un ID
        if (pacienteEncontrado.value && direccionGuardada && direccionGuardada.id) {
          // IMPORTANTE: Solo le pasamos un objeto con el puro ID. Así Java no se confunde.
          pacienteEncontrado.value.direccion = { id: direccionGuardada.id };
          
          // Guardamos la relación en el paciente
          await pacienteService().update(pacienteEncontrado.value);
        }
        // --- FIN MAGIA DE VINCULACIÓN ---

        previousState();
        alertService.showSuccess('¡Dirección guardada y vinculada al paciente correctamente!');
      } catch (error: any) {
        alertService.showHttpError(error.response);
      } finally {
        isSaving.value = false;
      }
    };

    if (route.params?.direccionId) {
      retrieveDireccion(Number(route.params.direccionId));
    }

    return {
      direccion,
      tipoVialidads,
      coloniasOptions, 
      cpSearchString,  
      municipioDisplay, 
      estadoDisplay,    
      searchByZipCode,  
      updateLocationDetails, 
      isSearchingCP,
      
      ecuSearchString,
      pacienteEncontrado,
      isSearchingEcu,
      buscarPaciente,

      isSaving,
      currentLanguage,
      previousState,
      save,
      t$,
      v$,
      onInputUpper,
    };
  },
});