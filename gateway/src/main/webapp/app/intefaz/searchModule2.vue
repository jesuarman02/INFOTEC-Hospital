<template>
  <transition name="fade">
    <div v-show="mostrarHeader" class="search-wrapper">

      <h5 class="title-pacientes mt-2 mb-3">
        REGISTRO DE PACIENTES
      </h5>

      <!-- INPUT -->
      <div class="search-container mb-4">
        <div class="input-group fancy-search">

          <div class="input-group-prepend">
            <span class="input-group-text bg-transparent border-0 pl-3">
              <font-awesome-icon icon="clipboard" class="text-muted search-icon" />
            </span>
          </div>

          <input 
            type="text" 
            v-model="valorLocal"
            placeholder="Buscar o registrar paciente..."
            class="minimal-input"
          />
        </div>
      </div>

      <!-- 🔥 ICONOS CENTRADOS -->
      <div class="iconos-container">
        <img src="/content/images/doctor.svg" class="icono-item" />
        <img src="/content/images/calendario.svg" class="icono-item" />
        <img src="/content/images/carpeta.svg" class="icono-item" />
        <img src="/content/images/atom.svg" class="icono-item" />
      </div>

      <!-- 🔥 BARRA DE PROGRESO -->
      <div class="progress-container">
        <div class="progress-bar"></div>
      </div>
        <p class="loading-text">Procesando información...</p>
    </div>
  </transition>
</template>

<script setup>
import { ref } from 'vue'

const mostrarHeader = ref(true)
const valorLocal = ref('')
</script>

<style scoped src="../../content/css/searchbar2.css"></style>