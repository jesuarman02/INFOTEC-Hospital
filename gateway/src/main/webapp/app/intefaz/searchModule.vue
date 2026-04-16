<template>
  <transition name="fade-slide">
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
            placeholder="Ingresa un ECU" 
            class="minimal-input"
          />
        </div>

        <p class="ecu-link mt-3" @click="mostrarModal = true">
          No conozco mi ECU
        </p>
      </div>

    </div>
  </transition>

  <transition name="fade-scale">
    <div v-if="mostrarModal" class="modal-overlay" @click.self="mostrarModal = false">
      <div class="modal-content shadow-lg border-0 rounded-lg p-4">

        <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
          <h5 class="modal-title text-danger m-0 font-weight-bold">Buscar ECU</h5>
          <button class="btn btn-sm btn-light rounded-circle" @click="mostrarModal = false">✕</button>
        </div>

        <div class="form-group mb-3">
          <input class="form-control fancy-input" v-model="nombre" placeholder="Nombre(s)" />
        </div>
        <div class="form-group mb-3">
          <input class="form-control fancy-input" v-model="apellidoPaterno" placeholder="Apellido Paterno" />
        </div>
        <div class="form-group mb-4">
          <input class="form-control fancy-input" v-model="apellidoMaterno" placeholder="Apellido Materno" />
        </div>

        <button class="btn btn-danger btn-block rounded-pill shadow-sm mb-3 anim-btn" @click="buscarEcu">
          Encontrar
        </button>

        <transition name="fade">
          <div v-if="ecuEncontrado" class="alert alert-info text-center rounded-lg mt-2">
            Tu ECU es: <strong style="font-size: 1.2rem;">{{ ecuEncontrado }}</strong>
          </div>
        </transition>

      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';

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

const mostrarModal = ref(false);
const nombre = ref('');
const apellidoPaterno = ref('');
const apellidoMaterno = ref('');
const ecuEncontrado = ref('');

const buscarEcu = () => {
  if (!nombre.value || !apellidoPaterno.value || !apellidoMaterno.value) {
    ecuEncontrado.value = 'Completa todos los campos';
    return;
  }
  ecuEncontrado.value = '123456';
};
</script>

<style scoped src="../../content/css/searchbar.css"></style>