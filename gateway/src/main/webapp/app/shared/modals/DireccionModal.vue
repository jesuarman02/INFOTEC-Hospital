<template>
  <transition name="modal-fade">
    <div v-if="visible" class="custom-modal-overlay">
      <div class="custom-modal-box">
        
        <div class="custom-modal-header">
          <h3 class="title-text">
            <img src="/content/images/pin-map-fill.svg" class="icon-title" /> Registrar Dirección
          </h3>
          <button class="close-btn" @click="cerrarModal">
            <img src="/content/images/x-circle.svg" class="icon-close" />
          </button>
        </div>

        <div class="custom-modal-body">
          <form @submit.prevent="guardar">
            
            <div class="search-section" v-if="!direccion.id && !pacientePreCargado">
              <h5 class="section-title">
                <img src="/content/images/search.svg" class="icon-label" /> Identificación del paciente
              </h5>
              <p class="text-muted" style="margin-top: -8px; font-size: 0.85rem;">Por favor ingresa su ECU</p>
              
              <div class="search-box">
                <div class="input-group">
                  <input 
                    type="text" 
                    class="custom-input" 
                    :class="{ 'rounded-end': !isSearchingEcu && !pacienteEncontrado }"
                    v-model="ecuSearchString" 
                    placeholder="Ej: 12345" 
                    @input="bloquearLetras('ecuSearchString')"
                    :readonly="pacienteEncontrado !== null"
                  />
                  <div class="input-group-append" v-if="isSearchingEcu">
                    <span class="loading-badge"><img src="/content/images/arrow-repeat.svg" class="spinner icon-small" /> Buscando...</span>
                  </div>
                  <button v-if="pacienteEncontrado" type="button" class="custom-btn btn-cancel" style="border-radius: 0 6px 6px 0; margin: 0; padding: 0.55rem 0.8rem;" @click="limpiarPacienteEncontrado">
                    <img src="/content/images/x-circle.svg" class="btn-icon" style="margin: 0;" />
                  </button>
                </div>
              </div>
              
              <div v-if="pacienteEncontrado" class="success-badge slide-down">
                <img src="/content/images/check-circle.svg" class="success-icon" />
                <span><strong>Paciente seleccionado:</strong> {{ pacienteEncontrado.nombre }} {{ pacienteEncontrado.apellidoPaterno }} {{ pacienteEncontrado.apellidoMaterno }}</span>
              </div>
            </div>

            <div class="search-section" style="background-color: #fdf2f5; border-color: #fbcfe8;" v-if="!direccion.id && pacientePreCargado">
               <h5 class="section-title mb-2" style="color: #5c1830;">
                <img src="/content/images/person-vcard.svg" class="icon-label" /> Asignando dirección a:
              </h5>
              <div class="d-flex align-items-center bg-white p-2 rounded border">
                <span class="badge badge-secondary mr-2" style="font-size: 0.9rem;">ECU: {{ pacientePreCargado.ecu }}</span>
                <span class="font-weight-bold">{{ pacientePreCargado.nombreCompleto }}</span>
              </div>
            </div>

            <hr class="divider" v-if="pacienteEncontrado || direccion.id || pacientePreCargado" />

            <div class="formulario-grid" v-if="pacienteEncontrado || pacientePreCargado || direccion.id">              
              <div class="form-group col-span-2" v-show="false">
                <label>ID Dirección</label>
                <input type="text" class="custom-input bg-light" v-model="direccion.id" readonly />
              </div>

              <div class="form-group">
                <label><img src="/content/images/signpost.svg" class="icon-label" /> Tipo Vialidad <span class="text-danger">*</span></label>
                <div class="select-wrapper">
                  <select class="custom-input" v-model="direccion.tipoVialidad">
                    <option :value="null">-- Seleccione --</option>
                    <option v-for="tipo in tipoVialidads" :key="tipo.id" :value="tipo">
                      {{ tipo.nombre }}
                    </option>
                  </select>
                  <img src="/content/images/arrow-down-square.svg" class="select-icon" />
                </div>
                <span v-if="v$.tipoVialidad.$error" class="error-text"><img src="/content/images/exclamation-lg.svg" class="icon-error" /> Obligatorio.</span>
              </div>

              <div class="form-group">
                <label><img src="/content/images/signpost-2.svg" class="icon-label" /> Nombre Vialidad <span class="text-danger">*</span></label>
                <input type="text" class="custom-input" v-model="v$.nombreVialidad.$model" @input="onInputUpper($event, v$.nombreVialidad)" maxlength="100" />
                <span v-if="v$.nombreVialidad.$error" class="error-text"><img src="/content/images/exclamation-lg.svg" class="icon-error" /> Obligatorio (Max. 100).</span>
              </div>

              <div class="form-group">
                <label><img src="/content/images/hash.svg" class="icon-label" /> No. Exterior <span class="text-danger">*</span></label>
                <input type="text" class="custom-input" v-model="v$.numExterior.$model" @input="onInputUpper($event, v$.numExterior)" maxlength="5" />
                <span v-if="v$.numExterior.$error" class="error-text"><img src="/content/images/exclamation-lg.svg" class="icon-error" /> Obligatorio (Max. 5).</span>
              </div>

              <div class="form-group">
                <label><img src="/content/images/hash.svg" class="icon-label" /> No. Interior</label>
                <input type="text" class="custom-input" v-model="direccion.numInterior" @input="onInputUpperDirect($event, 'numInterior')" maxlength="5" placeholder="Dejar vacío para S/N" />
              </div>

              <div class="form-group col-span-2">
                <label><img src="/content/images/telephone-x.svg" class="icon-label" /> Teléfono <span class="text-danger">*</span></label>
                <input 
                  type="text" 
                  class="custom-input" 
                  v-model="v$.telefono.$model" 
                  @input="bloquearLetras('telefono')" 
                  @keydown="bloquearSignosNumericos"
                  maxlength="10" 
                  placeholder="Ej: 5512345678" 
                />
                <span v-if="v$.telefono.$error" class="error-text"><img src="/content/images/exclamation-lg.svg" class="icon-error" /> Exactamente 10 dígitos numéricos.</span>
              </div>

              <hr class="divider col-span-2" />

              <div class="form-group col-span-2">
                <label><img src="/content/images/mailbox-flag.svg" class="icon-label" /> Código Postal <span class="text-danger">*</span></label>
                <div class="input-group">
                  <input type="text" class="custom-input cp-input" :class="{ 'rounded-end': !isSearchingCP }" v-model="cpSearchString" maxlength="5" placeholder="Ej: 01000" @input="bloquearLetras('cpSearchString')" />
                  <div class="input-group-append" v-if="isSearchingCP">
                    <span class="loading-badge"><img src="/content/images/arrow-repeat.svg" class="spinner icon-small" /> Buscando...</span>
                  </div>
                </div>
                <span v-if="coloniasOptions.length > 0 && !isSearchingCP" class="success-text">
                  <img src="/content/images/check-circle.svg" class="icon-small success-icon-filter" />
                  Código postal válido. Seleccione su colonia abajo.
                </span>
              </div>

              <div class="form-group col-span-2">
                <label><img src="/content/images/buildings.svg" class="icon-label" /> Colonia / Asentamiento <span class="text-danger">*</span></label>
                <div class="select-wrapper">
                  <select class="custom-input" v-model="direccion.codigoPostalInfo" @change="updateLocationDetails">
                    <option :value="null">-- Selecciona la Colonia --</option>
                    <option v-for="colonia in coloniasOptions" :key="colonia.id" :value="colonia">
                      {{ colonia.asentamiento }} ({{ colonia.tipoAsentamiento }})
                    </option>
                  </select>
                  <img src="/content/images/arrow-down-square.svg" class="select-icon" />
                </div>
                <span v-if="v$.codigoPostalInfo.$error" class="error-text"><img src="/content/images/exclamation-lg.svg" class="icon-error" /> Seleccione una colonia.</span>
              </div>

              <div class="form-group">
                <label><img src="/content/images/buildings.svg" class="icon-label" /> Municipio / Alcaldía</label>
                <input type="text" class="custom-input bg-light" v-model="municipioDisplay" readonly />
              </div>

              <div class="form-group">
                <label><img src="/content/images/pin-map-fill.svg" class="icon-label" /> Estado</label>
                <input type="text" class="custom-input bg-light" v-model="estadoDisplay" readonly />
              </div>

            </div>
          </form>
        </div>

        <div class="custom-modal-footer">
          <button class="custom-btn btn-cancel" @click="cerrarModal">
            <img src="/content/images/ban.svg" class="btn-icon" /> Cancelar
          </button>
          <button class="custom-btn btn-save" @click="guardar" :disabled="isSaving || (!pacienteEncontrado && !pacientePreCargado && !direccion.id)">
            <span v-if="isSaving" class="d-flex align-center">
              <img src="/content/images/tropical-storm.svg" class="btn-icon spinner" /> Guardando...
            </span>
            <span v-else class="d-flex align-center">
              <img src="/content/images/save.svg" class="btn-icon" /> Guardar Dirección
            </span>
          </button>
        </div>

      </div>
    </div>
  </transition>
</template>

<script lang="ts">
import { defineComponent, ref, watch, inject } from 'vue';
import type { PropType } from 'vue';
import useVuelidate from '@vuelidate/core';
import { required, minLength, maxLength, numeric } from '@vuelidate/validators';
import Swal from 'sweetalert2';

import DireccionService from '@/entities/pacientems/direccion/direccion.service';
import TipoVialidadService from '@/entities/pacientesms/tipo-vialidad/tipo-vialidad.service';
import CodigoPostalService from '@/entities/pacientesms/codigo-postal/codigo-postal.service';
import PacienteService from '@/entities/pacientems/paciente/paciente.service';

export default defineComponent({
  name: 'DireccionModal',
  props: { 
    visible: { type: Boolean, required: true },
    pacientePreCargado: { type: Object as PropType<any>, default: null }
  },
  emits: ['update:visible', 'saved'],
  
  setup(props, { emit }) {
    const direccionService = inject('direccionService', () => new DireccionService());
    const tipoVialidadService = inject('tipoVialidadService', () => new TipoVialidadService());
    const codigoPostalService = inject('codigoPostalService', () => new CodigoPostalService());
    const pacienteService = inject('pacienteService', () => new PacienteService());

    const isSaving = ref(false);
    const isSearchingEcu = ref(false);
    const isSearchingCP = ref(false);

    const tipoVialidads = ref<any[]>([]);
    const coloniasOptions = ref<any[]>([]);
    const ecuSearchString = ref('');
    const cpSearchString = ref('');
    const municipioDisplay = ref('');
    const estadoDisplay = ref('');
    const pacienteEncontrado = ref<any | null>(null);

    const direccion = ref({
      id: null,
      tipoVialidad: null,
      nombreVialidad: '',
      numExterior: '',
      numInterior: '',
      telefono: '',
      codigoPostalInfo: null,
      paciente: null
    } as any);

    const rules = {
      tipoVialidad: { required },
      nombreVialidad: { required, maxLength: maxLength(100) },
      numExterior: { required, maxLength: maxLength(5) },
      telefono: { required, numeric, minLength: minLength(10), maxLength: maxLength(10) },
      codigoPostalInfo: { required }
    };
    const v$ = useVuelidate(rules, direccion);

    const initRelationships = async () => {
      try {
        const res = await tipoVialidadService().retrieve();
        tipoVialidads.value = res.data;
      } catch (error) {
        console.error("Error al cargar vialidades", error);
      }
    };
    initRelationships();

    let ecuTimeout: ReturnType<typeof setTimeout>;

    watch(ecuSearchString, (newVal) => {
      clearTimeout(ecuTimeout);
      if (newVal && newVal.trim() !== '') {
        ecuTimeout = setTimeout(() => {
          buscarPaciente();
        }, 800);
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
        const encontrado = res.data.find((p: any) => p.ecu === ecuNumerico);
        
        if (encontrado) {
          if (encontrado.direccion && encontrado.direccion.id) {
            Swal.fire({ icon: 'warning', title: 'Paciente con dirección', text: `El paciente con ECU ${ecuNumerico} ya tiene una dirección registrada.`, confirmButtonColor: '#611232' });
            ecuSearchString.value = ''; 
          } else {
            pacienteEncontrado.value = encontrado;
            Swal.fire({ icon: 'success', title: 'Paciente Encontrado', text: 'Puede proceder a registrar su dirección.', showConfirmButton: false, timer: 4000 });          
          }
        } else {
          Swal.fire({ icon: 'error', title: 'No encontrado', text: 'No existe un paciente con ese ECU.', confirmButtonColor: '#611232' });
        }
      } catch (error) {
        Swal.fire({ icon: 'error', title: 'Error de servidor', text: 'Ocurrió un error al buscar al paciente.', confirmButtonColor: '#611232' });
      } finally {
        isSearchingEcu.value = false;
      }
    };

    const limpiarPacienteEncontrado = () => {
      pacienteEncontrado.value = null;
      ecuSearchString.value = '';
    };

    watch(cpSearchString, async (newVal) => {
      if (newVal && newVal.length === 5) {
        isSearchingCP.value = true;
        coloniasOptions.value = [];
        try {
          const res = await codigoPostalService().retrieveByCP(newVal);
          coloniasOptions.value = res.data;
          if (coloniasOptions.value.length === 1) {
             direccion.value.codigoPostalInfo = coloniasOptions.value[0];
             updateLocationDetails();
          } else if (coloniasOptions.value.length === 0) {
             Swal.fire({ icon: 'info', title: 'Sin resultados', text: 'No se encontraron colonias para este Código Postal.', confirmButtonColor: '#611232' });
          }
        } catch (error) {
          console.error(error);
        } finally {
          isSearchingCP.value = false;
        }
      } else if (newVal && newVal.length < 5) {
        coloniasOptions.value = [];
        municipioDisplay.value = '';
        estadoDisplay.value = '';
        direccion.value.codigoPostalInfo = null;
      }
    });
    // 🔥 LÓGICA DE PRECArGA PARA EDICIÓN 🔥
    watch(() => props.visible, (newVal) => {
      if (newVal) {
        // Si venimos del Wizard y el paciente ya tiene una dirección vinculada...
        if (props.pacientePreCargado && props.pacientePreCargado.direccion) {
          const dirExistente = props.pacientePreCargado.direccion;
          
          // Llenamos el formulario con los datos que vienen de la BD
          direccion.value = JSON.parse(JSON.stringify(dirExistente));
          
          // Casos especiales: Si el código postal es un objeto, extraemos el código para la búsqueda visual
          if (direccion.value.codigoPostal) {
            cpSearchString.value = direccion.value.codigoPostal.codigo || '';
            // Forzamos la carga de colonias para que el selector no salga vacío
            direccion.value.codigoPostalInfo = direccion.value.codigoPostal;
            updateLocationDetails();
          }
        } else {
          // Si es un paciente nuevo o no tiene dirección, limpiamos todo
          limpiarFormulario();
        }
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

    const bloquearLetras = (campo: 'ecuSearchString' | 'telefono' | 'cpSearchString') => {
      if(campo === 'ecuSearchString') ecuSearchString.value = ecuSearchString.value.replace(/\D/g, '');
      if(campo === 'telefono') direccion.value.telefono = direccion.value.telefono.replace(/\D/g, '');
      if(campo === 'cpSearchString') cpSearchString.value = cpSearchString.value.replace(/\D/g, '');
    };

    const bloquearSignosNumericos = (e: KeyboardEvent) => {
      if (['e', 'E', '+', '-', '.', ','].includes(e.key)) {
        e.preventDefault();
      }
    };

    const onInputUpper = (event: Event, field: any) => {
      const target = event.target as HTMLInputElement;
      if (target) field.$model = target.value.toUpperCase();
    };

    const onInputUpperDirect = (event: Event, field: 'numInterior') => {
      const target = event.target as HTMLInputElement;
      if (target) direccion.value[field] = target.value.toUpperCase();
    };

    const limpiarFormulario = () => {
      direccion.value = { id: null, tipoVialidad: null, nombreVialidad: '', numExterior: '', numInterior: '', telefono: '', codigoPostalInfo: null, paciente: null };
      ecuSearchString.value = '';
      cpSearchString.value = '';
      pacienteEncontrado.value = null;
      coloniasOptions.value = [];
      municipioDisplay.value = '';
      estadoDisplay.value = '';
      v$.value.$reset();
    };

    const hayCambiosSinGuardar = () => {
      return (ecuSearchString.value !== '' || direccion.value.nombreVialidad !== '' || direccion.value.telefono !== '');
    };

    const cerrarModal = () => {
      if (hayCambiosSinGuardar()) {
        Swal.fire({
          title: '¿Cancelar registro?',
          text: "Los datos ingresados se perderán.",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#888',
          cancelButtonColor: '#611232',
          confirmButtonText: 'Sí, salir',
          cancelButtonText: 'Continuar editando'
        }).then((result) => {
          if (result.isConfirmed) {
            limpiarFormulario();
            emit('update:visible', false);
          }
        });
      } else {
        emit('update:visible', false);
      }
    };

    const guardar = async () => {
      v$.value.$touch();
      if (v$.value.$invalid) {
        Swal.fire({ icon: 'error', title: 'Campos incompletos', text: 'Revise los campos marcados en rojo.', confirmButtonColor: '#611232' });
        return;
      }

      if (!direccion.value.numInterior || direccion.value.numInterior.trim() === '') {
        direccion.value.numInterior = 'S/N';
      }

      isSaving.value = true;
      try {
        const pacienteParaVincular = pacienteEncontrado.value || props.pacientePreCargado;

        // 🔥 EL PAYLOAD DEFINITIVO (Misma estructura que el update.component.ts) 🔥
        const payload = {
          ...direccion.value,
          tipoVialidad: direccion.value.tipoVialidad,
          codigoPostalInfo: direccion.value.codigoPostalInfo,
        };

        if (direccion.value.tipoVialidad) {
          payload.tipoVialidadId = direccion.value.tipoVialidad.id;
        }
        if (direccion.value.codigoPostalInfo) {
          payload.codigoPostalInfoId = direccion.value.codigoPostalInfo.id;
        }

        let respuestaDir;
        if (payload.id) {
          respuestaDir = await direccionService().update(payload);
        } else {
          respuestaDir = await direccionService().create(payload);
        }
        
        // Atrapamos la dirección recién guardada
        const resDir: any = respuestaDir;
        const dirGuardada = resDir.data ? resDir.data : resDir;

        // 🔥 Vinculamos al paciente 🔥
        if (pacienteParaVincular && pacienteParaVincular.id && dirGuardada.id) {
          pacienteParaVincular.direccion = { id: dirGuardada.id };
          await pacienteService().update(pacienteParaVincular);
        }

        Swal.fire({ icon: 'success', title: '¡Guardado Exitoso!', text: 'La dirección se guardó y vinculó correctamente.', showConfirmButton: false, timer: 2000 });
        
        emit('saved');
        limpiarFormulario();
        emit('update:visible', false);
      } catch (error) {
        console.error("Error del backend:", error);
        Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo guardar la dirección.', confirmButtonColor: '#611232' });
      } finally {
        isSaving.value = false;
      }
    };

    return { 
      direccion, ecuSearchString, cpSearchString, pacienteEncontrado, isSearchingEcu, isSearchingCP,
      tipoVialidads, coloniasOptions, municipioDisplay, estadoDisplay, isSaving, v$,
      buscarPaciente, limpiarPacienteEncontrado, updateLocationDetails, bloquearLetras, bloquearSignosNumericos, onInputUpper, onInputUpperDirect, cerrarModal, guardar
    };
  }
});
</script>

<style scoped>
/* ESTILOS PREMIUM */
.modal-fade-enter-active, .modal-fade-leave-active { transition: opacity 0.3s ease; }
.modal-fade-enter-from, .modal-fade-leave-to { opacity: 0; }

@keyframes elasticPop {
  0% { transform: scale(0.9); opacity: 0; }
  70% { transform: scale(1.02); opacity: 1; }
  100% { transform: scale(1); opacity: 1; }
}

@keyframes spin { 100% { transform: rotate(360deg); } }
@keyframes slideDown { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }

.custom-modal-overlay {
  position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
  background-color: rgba(20, 20, 20, 0.5); backdrop-filter: blur(5px);
  display: flex; justify-content: center; align-items: center; z-index: 9999;
}

.custom-modal-box {
  background: #ffffff; width: 90%; max-width: 750px; max-height: 85vh; 
  border-radius: 12px; box-shadow: 0 20px 40px -10px rgba(0, 0, 0, 0.3);
  display: flex; flex-direction: column; overflow: hidden; animation: elasticPop 0.3s ease-out;
}

.custom-modal-header {
  background-color: #611232; padding: 1rem 1.5rem; 
  display: flex; justify-content: space-between; align-items: center; border-bottom: 3px solid #4a0d26; 
}

.title-text { color: #ffffff; margin: 0; font-weight: bold; font-size: 1.15rem; display: flex; align-items: center; }
.close-btn { background: rgba(255,255,255,0.1); border: none; width: 30px; height: 30px; border-radius: 50%; cursor: pointer; display: flex; justify-content: center; align-items: center; transition: all 0.2s; }
.close-btn:hover { background: rgba(255,255,255,0.25); transform: rotate(90deg); }

.icon-title, .icon-close { filter: brightness(0) invert(1); width: 20px; height: 20px; margin-right: 8px;}
.icon-close { margin-right: 0; width: 14px; height: 14px; }
.icon-label { width: 15px; height: 15px; margin-right: 6px; vertical-align: text-bottom; opacity: 0.7; }
.icon-error { width: 14px; height: 14px; margin-right: 4px; filter: invert(27%) sepia(91%) saturate(5436%) hue-rotate(345deg) brightness(93%) contrast(97%); }
.btn-icon { width: 16px; height: 16px; margin-right: 6px; }
.spinner { animation: spin 1s linear infinite; }
.icon-small { width: 12px; height: 12px; margin-right: 4px; }

.custom-modal-body { padding: 1.2rem 1.5rem; overflow-y: auto; flex: 1; text-align: left; }

.search-section { background: #f8fafc; padding: 1rem; border-radius: 8px; border: 1px solid #e2e8f0; margin-bottom: 1rem; }
.section-title { font-size: 1rem; font-weight: bold; color: #1e293b; margin-top: 0; margin-bottom: 0.8rem; }
.text-muted { color: #64748b; }
.input-group { display: flex; width: 100%; }
.input-group .custom-input { border-top-right-radius: 0; border-bottom-right-radius: 0; border-right: none; }
.rounded-end { border-top-right-radius: 6px !important; border-bottom-right-radius: 6px !important; border-right: 1px solid #cbd5e1 !important;}

.success-badge { margin-top: 0.8rem; background: #d1e7dd; color: #0f5132; padding: 0.6rem 1rem; border-radius: 6px; display: flex; align-items: center; border: 1px solid #badbcc; }
.success-icon { width: 18px; height: 18px; margin-right: 8px; filter: invert(29%) sepia(43%) saturate(497%) hue-rotate(97deg) brightness(92%) contrast(94%); }
.slide-down { animation: slideDown 0.3s ease-out; }

.divider { border: 0; border-top: 1px solid #cbd5e1; margin: 1.5rem 0; }

.formulario-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 1rem; }
.col-span-2 { grid-column: span 2; }

.form-group { display: flex; flex-direction: column; }
.form-group label { margin-bottom: 0.3rem; font-weight: 700; color: #333; font-size: 0.85rem;}
.text-danger { color: #e63946; }

.custom-input { padding: 0.55rem 0.8rem; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 0.95rem; color: #1e293b; font-weight: 500; background-color: #fff; transition: all 0.2s ease; width: 100%; box-sizing: border-box; }
.custom-input:hover:not([readonly]) { border-color: #94a3b8; }
.custom-input:focus:not([readonly]) { outline: none; border-color: #611232; box-shadow: 0 0 0 3px rgba(97, 18, 50, 0.1); }
.custom-input[readonly], .bg-light { background-color: #e2e8f0; cursor: not-allowed; color: #475569; }
.cp-input { letter-spacing: 2px; font-family: monospace; font-size: 1.05rem; }

.select-wrapper { position: relative; }
select.custom-input { appearance: none; padding-right: 2rem; cursor: pointer; }
.select-icon { position: absolute; right: 12px; top: 50%; transform: translateY(-50%); width: 16px; height: 16px; opacity: 0.6; pointer-events: none; }

.loading-badge { background: #f1f5f9; border: 1px solid #cbd5e1; border-left: none; padding: 0 1rem; display: flex; align-items: center; color: #611232; font-weight: bold; font-size: 0.85rem; border-top-right-radius: 6px; border-bottom-right-radius: 6px; height: 100%; box-sizing: border-box;}

.error-text { color: #e63946; font-size: 0.75rem; margin-top: 0.2rem; font-weight: 600; display: flex; align-items: center; }

.custom-modal-footer { padding: 1rem 1.5rem; background-color: #f8fafc; display: flex; justify-content: flex-end; gap: 1rem; border-top: 1px solid #e2e8f0; }
.custom-btn { padding: 0.6rem 1.2rem; border-radius: 6px; cursor: pointer; font-size: 0.95rem; font-weight: 700; border: none; display: flex; align-items: center; transition: all 0.2s; }
.btn-cancel { background: #94a3b8; color: white; }
.btn-cancel:hover { background: #64748b; }
.btn-save { background: #611232; color: white; }
.btn-save:hover:not(:disabled) { background: #4a0d26; }
.btn-save:disabled { background: #a87b8d; cursor: not-allowed; }
.btn-cancel .btn-icon, .btn-save .btn-icon, .btn-search .btn-icon { filter: brightness(0) invert(1); }
.success-text { color: #0f5132; font-size: 0.8rem; font-weight: 600; display: flex; align-items: center; margin-top: 6px; animation: slideDown 0.3s ease-out; }
.success-icon-filter { filter: invert(29%) sepia(43%) saturate(497%) hue-rotate(97deg) brightness(92%) contrast(94%); }
</style>

<style>
/* 🔥 FUERZA A SWEETALERT A SALIR POR ENCIMA DE CUALQUIER MODAL 🔥 */
.swal2-container {
  z-index: 100000 !important;
}
</style>