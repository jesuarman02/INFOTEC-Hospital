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
            v-model="searchQuery" 
            @keyup.enter="buscarPorEcu" 
            placeholder="ECU, Nombre del Paciente"
            class="minimal-input"
          />
        </div>
      </div>

    </div>
  </transition>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';


export default defineComponent({
    name: 'SerchModule',
    // Definimos las propiedades que recibimos del padre
    props: {
      modelValue: String, // Este es el texto de búsqueda (searchQuery)
      mostrarHeader: { type: Boolean, default: true }
    },
    // Definimos los eventos que enviamos al padre
    emits: ['update:modelValue', 'buscar'],
   setup(props, { emit }) {
    // Función para manejar el cambio de texto de forma segura
    const alCambiarInput = (event: Event) => {
        const target = event.target as HTMLInputElement;
        if (target) {
            emit('update:modelValue', target.value);
        }
    };

    return { alCambiarInput };
}
});
</script>
<style scoped src="../../content/css/searchbar.css"></style>