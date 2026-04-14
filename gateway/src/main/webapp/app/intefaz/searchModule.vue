<template>
  <!-- HEADER CON TRANSICIÓN -->
  <transition name="fade">
    <div v-show="mostrarHeader" class="search-wrapper">

      <h5 class="title-pacientes mt-2 mb-3">PACIENTES</h5>

      <div class="search-container mb-4">
        <div class="input-group fancy-search">

          <div class="input-group-prepend">
            <span class="input-group-text bg-transparent border-0 pl-3">
              <font-awesome-icon icon="search" class="text-muted search-icon" />
            </span>
          </div>

          <input 
            type="text" 
            v-model="valorLocal"
            @keyup.enter="handleBuscar" 
            placeholder="Buscar por ECU o nombre..."
            class="minimal-input"
          />
        </div>

        <!-- TEXTO -->
        <p class="ecu-link" @click="mostrarModal = true">
          No conozco mi ECU
        </p>
      </div>

    </div>
  </transition>

  <!-- MODAL -->
  <transition name="fade">
    <div v-if="mostrarModal" class="modal-overlay">
      <div class="modal-content">

        <h5 class="modal-title">Buscar ECU</h5>

        <input v-model="nombre" placeholder="Nombre" />
        <input v-model="apellidoPaterno" placeholder="Apellido Paterno" />
        <input v-model="apellidoMaterno" placeholder="Apellido Materno" />

        <button @click="buscarEcu">Encontrar</button>

        <p v-if="ecuEncontrado">
          Tu ECU es: <strong>{{ ecuEncontrado }}</strong>
        </p>

        <button class="close-btn" @click="mostrarModal = false">
          Cerrar
        </button>

      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';

// PROPS
const props = defineProps({
  value: { 
    type: String,
    default: ''
  },
  mostrarHeader: {
    type: Boolean,
    default: true
  }
});

// 🔥 EMITS (agregamos ocultar-header)
const emit = defineEmits(['input', 'buscar', 'ocultar-header']); 

// V-MODEL LOCAL
const valorLocal = computed({
  get() {
    return props.value; 
  },
  set(nuevoValor) {
    emit('input', nuevoValor); 
  }
});

// 🔥 FUNCIÓN ENTER
const handleBuscar = () => {
  if (!valorLocal.value) return; // opcional, evita vacío

  emit('buscar');             // ejecuta búsqueda
  emit('ocultar-header');     // 🔥 oculta el search
};

// MODAL STATES
const mostrarModal = ref(false);

const nombre = ref('');
const apellidoPaterno = ref('');
const apellidoMaterno = ref('');
const ecuEncontrado = ref('');

// FUNCIÓN DE BÚSQUEDA (simulada)
const buscarEcu = () => {
  if (!nombre.value || !apellidoPaterno.value || !apellidoMaterno.value) {
    ecuEncontrado.value = 'Completa todos los campos';
    return;
  }

  ecuEncontrado.value = '123456';
};
</script>

<style scoped src="../../content/css/searchbar.css"></style>