<template>
  <transition name="fade">
    <div v-show="mostrarHeader">
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
            @keyup.enter="emit('buscar')" 
            placeholder="ECU, Nombre del Paciente"
            class="minimal-input"
          />
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  // CAMBIO CLAVE: En Vue 2/Compat se llama "value", no "modelValue"
  value: { 
    type: String,
    default: ''
  },
  mostrarHeader: {
    type: Boolean,
    default: true
  }
});

// CAMBIO CLAVE: En Vue 2/Compat se emite "input"
const emit = defineEmits(['input', 'buscar']); 

const valorLocal = computed({
  get() {
    return props.value; 
  },
  set(nuevoValor) {
    emit('input', nuevoValor); 
  }
});
</script>
<style scoped src="../../content/css/searchbar.css"></style>