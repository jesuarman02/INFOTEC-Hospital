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
    
    // Variables para la Búsqueda Inteligente
    const cpSearchString = ref(''); 
    const coloniasOptions: Ref<ICodigoPostal[]> = ref([]); 
    const municipioDisplay = ref(''); 
    const estadoDisplay = ref('');    
    const isSearchingCP = ref(false);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    // MODIFICACIÓN: Reglas de validación más estrictas
    const validationRules = {
      nombreVialidad: { required }, // Sugerido: que sea obligatorio
      numExterior: { required },    // Sugerido
      numInterior: {},
      telefono: {},
      tipoVialidad: { required },   // REQUERIDO: Para validar que seleccionen vialidad
      codigoPostalInfo: { required }, // REQUERIDO: Para asegurar que elijan colonia
    };
    const v$ = useVuelidate(validationRules, direccion as any);
    const { resolveJsonI18nKey } = useValidation();

    // MODIFICACIÓN: Función de búsqueda mejorada con validaciones
    const searchByZipCode = async () => {
      const cpValue = cpSearchString.value;

      // 1. Validar longitud y formato
      if (!cpValue || cpValue.length !== 5) {
        return; // No hacemos nada si no tiene 5 dígitos (el watcher lo controla)
      }

      // 2. Validación de Rango (México: 01000 - 99998)
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
           // 3. Alerta de éxito al encontrar datos
           alertService.showSuccess('Datos del Código Postal cargados correctamente.');
           
           // Opcional: Si solo hay una colonia, seleccionarla automáticamente
           if (coloniasOptions.value.length === 1) {
             direccion.value.codigoPostalInfo = coloniasOptions.value[0];
             updateLocationDetails();
           }
        } else {
           alertService.showInfo('No se encontraron colonias asociadas a este Código Postal.');
        }
      } catch (error) {
        alertService.showHttpError(error.response);
      } finally {
        isSearchingCP.value = false;
      }
    };

    // MODIFICACIÓN: Watcher para búsqueda automática
    watch(cpSearchString, (newVal) => {
      // Si el usuario escribe exactamente 5 dígitos, disparamos la búsqueda
      if (newVal && newVal.length === 5) {
        searchByZipCode();
      }
      // Si el usuario borra y tiene menos de 5, limpiamos las opciones
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

    const retrieveDireccion = async direccionId => {
      try {
        const res = await direccionService().find(direccionId);
        direccion.value = res;
        
        if (direccion.value.codigoPostalInfo) {
           cpSearchString.value = direccion.value.codigoPostalInfo.codigo || '';
           municipioDisplay.value = direccion.value.codigoPostalInfo.municipio || '';
           estadoDisplay.value = direccion.value.codigoPostalInfo.estado || '';
           coloniasOptions.value = [direccion.value.codigoPostalInfo];
        }
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const initRelationships = async () => {
      try {
        const resTipoVialidad = await tipoVialidadService().retrieve();
        tipoVialidads.value = resTipoVialidad.data;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    initRelationships();

    const previousState = () => router.go(-1);

    const save = async () => {
      // Validamos el formulario antes de guardar
      const isFormValid = await v$.value.$validate();
      if (!isFormValid) {
        alertService.showError('Por favor verifica los campos requeridos.');
        return;
      }

      isSaving.value = true;
      try {
        if (direccion.value.id) {
          await direccionService().update(direccion.value);
        } else {
          await direccionService().create(direccion.value);
        }
        previousState();
        alertService.showSuccess(t$('pacientemsApp.pacientemsDireccion.created', { param: direccion.value.id }).toString());
      } catch (error) {
        alertService.showHttpError(error.response);
      } finally {
        isSaving.value = false;
      }
    };

    if (route.params?.direccionId) {
      retrieveDireccion(route.params.direccionId);
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
      isSaving,
      currentLanguage,
      previousState,
      save,
      t$,
      v$, // Retornamos el validador
    };
  },
});