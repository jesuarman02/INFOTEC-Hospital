<template>
  <transition name="fade-slide">
    <div v-show="mostrarHeader" class="search-wrapper">
      <h5 class="title-pacientes mt-2 mb-3">PACIENTES</h5>

      <div class="search-container mb-4">
        <div class="input-group fancy-search">
          <div class="input-group-prepend">
            <span class="input-group-text bg-transparent border-0 pl-3">
              <img src="/content/images/search.svg" class="search-icon-svg" />
            </span>
          </div>

          <input 
            type="text" 
            v-model="valorLocal"
            @keyup.enter="handleBuscar" 
            placeholder="Ingresa un ECU"
            class="minimal-input"
          />
        </div>

        <p class="ecu-link mt-3" @click="mostrarModal = true">
          No conozco el ECU
        </p>
      </div>
    </div>
  </transition>

  <transition name="fade-scale">
    <div v-if="mostrarModal" class="modal-overlay" @click.self="cerrarModal">
      <div class="modal-content shadow-lg border-0 rounded-lg p-4">

        <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
          <h5 class="modal-title m-0 font-weight-bold" style="color: #5c1830;">
            <img src="/content/images/search.svg" style="width: 20px; filter: brightness(0) saturate(100%) invert(13%) sepia(50%) saturate(4422%) hue-rotate(320deg) brightness(88%) contrast(98%); margin-right: 8px;"/> 
            Buscar ECU
          </h5>
          <button class="btn btn-sm btn-light rounded-circle font-weight-bold text-muted" @click="cerrarModal">✕</button>
        </div>

        <div v-if="!esExtranjero">
          <div class="input-group mb-1 shadow-sm rounded-lg overflow-hidden border">
            <div class="input-group-prepend">
              <span class="input-group-text bg-light border-0">
                <img src="/content/images/person-vcard.svg" style="width: 22px; opacity: 0.6;" />
              </span>
            </div>
            <input 
              class="form-control border-0 bg-light text-uppercase font-weight-bold" 
              v-model="curp" 
              @input="formatearCurp"
              placeholder="INGRESA EL CURP DEL PACIENTE" 
              maxlength="18"
              style="font-size: 0.95rem; padding-left: 5px;"
            />
          </div>
          
          <div class="d-flex justify-content-between mb-4 mt-2">
            <small class="text-danger font-weight-bold" v-if="curp.length > 0 && !isCurpValid">
              Formato de CURP inválido
            </small>
            <small v-else></small>
            <a href="https://www.gob.mx/curp/" target="_blank" class="small font-weight-bold link-gob" style="text-decoration: underline;">
              ¿No conoces el CURP?
            </a>
          </div>
        </div>

        <div v-else>
          <div class="input-group mb-3 shadow-sm rounded-lg overflow-hidden border">
             <div class="input-group-prepend">
              <span class="input-group-text bg-light border-0">
                <img src="/content/images/user.svg" style="width: 20px; opacity: 0.6;" />
              </span>
            </div>
            <input 
              class="form-control border-0 bg-light text-uppercase font-weight-bold" 
              v-model="nombre" 
              @input="formatearNombre"
              placeholder="NOMBRE(S)" 
              maxlength="50"
            />
          </div>
          
          <div class="input-group mb-4 shadow-sm rounded-lg overflow-hidden border">
            <div class="input-group-prepend">
              <span class="input-group-text bg-light border-0">
                <img src="/content/images/user.svg" style="width: 20px; opacity: 0.6;" />
              </span>
            </div>
            <input 
              class="form-control border-0 bg-light text-uppercase font-weight-bold" 
              v-model="apellidoPaterno" 
              @input="formatearApellido"
              placeholder="APELLIDO PATERNO" 
              maxlength="50"
            />
          </div>
        </div>

        <div class="form-check mb-4 border-top pt-3 text-center">
          <input class="form-check-input" type="checkbox" id="checkExtranjero" v-model="esExtranjero">
          <label class="form-check-label font-weight-bold text-secondary" for="checkExtranjero" style="cursor:pointer;">
            Paciente extranjero (Sin CURP)
          </label>
        </div>

        <div v-if="errorBusqueda" class="alert alert-danger text-center p-2 mb-3 font-weight-bold" style="font-size: 0.85rem;">
          {{ errorBusqueda }}
        </div>

        <button 
          class="btn btn-guinda btn-block rounded-pill shadow-sm mb-2 anim-btn font-weight-bold d-flex justify-content-center align-items-center" 
          @click="buscarEcu"
          :disabled="!isFormValid || cargando">
          
          <span v-if="cargando" class="spinner-border spinner-border-sm mr-2" role="status" aria-hidden="true"></span>
          <img v-else src="/content/images/check-circle.svg" style="width: 18px; filter: brightness(0) invert(1); margin-right: 8px;"/> 
          
          {{ cargando ? 'Buscando...' : 'Encontrar' }}
        </button>

      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { computed, ref, nextTick } from 'vue';
import axios from 'axios';

const props = defineProps({
  value: { type: String, default: '' },
  mostrarHeader: { type: Boolean, default: true }
});

const emit = defineEmits(['input', 'buscar', 'ocultar-header']); 

const valorLocal = computed({
  get() { return props.value; },
  set(nuevoValor) { emit('input', nuevoValor); }
});

const handleBuscar = () => {
  if (!valorLocal.value) return; 
  emit('buscar');             
  emit('ocultar-header');     
};

// ==========================================
// LÓGICA DEL MODAL
// ==========================================
const mostrarModal = ref(false);
const esExtranjero = ref(false);

const curp = ref('');
const nombre = ref('');
const apellidoPaterno = ref('');

const errorBusqueda = ref('');
const cargando = ref(false);

// Regex CURP
const regexCurp = /^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$/;

// Formateadores
const formatearCurp = () => {
  curp.value = curp.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
};

const formatearNombre = () => {
  nombre.value = nombre.value.toUpperCase().replace(/[^A-ZÀ-Ÿ\u00f1\u00d1 ]/g, '').substring(0, 50);
};

const formatearApellido = () => {
  apellidoPaterno.value = apellidoPaterno.value.toUpperCase().replace(/[^A-ZÀ-Ÿ\u00f1\u00d1 ]/g, '').substring(0, 50);
};

const isCurpValid = computed(() => {
  if (curp.value.length === 0) return true; 
  return regexCurp.test(curp.value);
});

const isFormValid = computed(() => {
  if (esExtranjero.value) {
    return nombre.value.trim().length > 0 && apellidoPaterno.value.trim().length > 0;
  } else {
    return curp.value.length === 18 && isCurpValid.value;
  }
});

const cerrarModal = () => {
  mostrarModal.value = false;
  curp.value = '';
  nombre.value = '';
  apellidoPaterno.value = '';
  esExtranjero.value = false;
  errorBusqueda.value = '';
};

// 🔥 LA BÚSQUEDA REAL EN LA BASE DE DATOS 🔥
const buscarEcu = async () => {
  try {
    cargando.value = true;
    errorBusqueda.value = '';
    
    // 🔥 EL ARREGLO: 'services/...' (Sin la diagonal inicial) 🔥
    const res = await axios.get('services/pacientesms/api/pacientes', {
      params: {
        page: 0,
        size: 500,
        sort: 'id,asc'
      }
    });
    
    // 🛡️ PARCHE INTELIGENTE
    let listaPacientes = [];
    if (Array.isArray(res.data)) {
      listaPacientes = res.data;
    } else if (res.data && Array.isArray(res.data.content)) {
      listaPacientes = res.data.content;
    } else {
      console.error("Formato inesperado:", res.data);
      errorBusqueda.value = 'Error: El servidor no devolvió una lista válida.';
      return;
    }

    let pacienteEncontrado = null;

    if (esExtranjero.value) {
      pacienteEncontrado = listaPacientes.find((p: any) => 
        p.nombre?.trim().toUpperCase() === nombre.value.trim() &&
        p.apellidoPaterno?.trim().toUpperCase() === apellidoPaterno.value.trim()
      );
    } else {
      pacienteEncontrado = listaPacientes.find((p: any) => p.curp === curp.value);
    }

    if (pacienteEncontrado && pacienteEncontrado.ecu) {
      valorLocal.value = pacienteEncontrado.ecu.toString();
      cerrarModal();
// 3. 🔥 MAGIA DE VUE: Esperamos a que la interfaz registre el número y disparamos 🔥
      nextTick(() => {
        handleBuscar();
      });
    } else {
      errorBusqueda.value = 'No se encontró ningún paciente con estos datos.';
    }

  } catch (error) {
    console.error("Error en la petición:", error);
    errorBusqueda.value = 'Error al conectar con la base de datos.';
  } finally {
    cargando.value = false;
  }
};
</script>

<style scoped src="../../content/css/searchbar.css"></style>