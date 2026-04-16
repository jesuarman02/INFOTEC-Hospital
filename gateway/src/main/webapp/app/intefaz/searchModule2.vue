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

      <!-- 🔥 BOTONES REDISEÑADOS -->
      <div class="acciones-container">

        <div class="accion-card amarillo" @click="abrirModalPrueba">
          <span class="icono">🧑‍⚕️</span>
          <p>Nuevo Paciente</p>
        </div>
        
        <div class="accion-card azul" @click="abrirModalDireccion">
          <span class="icono">📍</span>
          <p>Dirección</p>
        </div>
        
        <div class="accion-card verde" @click="abrirModalWizard">
          <span class="icono">📋</span>
          <p>Estudio</p>
        </div>

      </div>
      <div class="step-progress">
        <div class="step" :class="{active: progreso.paciente}">
            <div class="circle">1</div>
            <span>Paciente</span>
        </div>
        <div class="line" :class="{active : progreso.paciente}"></div>

        <div class="step" :class="{active :progreso.direccion}">
          <div class="circle">2</div>
          <span>Direccion</span>
        </div>

        <div class="line" :class="{active: progreso.direccion}"></div>

        <div class="step" :class="{active :progreso.estudio}">
          <div class="circle">3</div>
            <span> Estudio</span>
        </div>
      </div>
      

      <!-- 🔥 MODALES -->
      <PacienteModal 
        v-model:visible="mostrarModalPaciente" 
        @save="simularGuardado" 
      />

      <DireccionModal 
        v-model:visible="mostrarModalDireccion" 
        @saved="simularGuardadoDireccion" 
      />

      <InfoSocioeconomicaWizardModal 
        v-model:visible="mostrarModalWizard" 
        @saved="simularGuardadoWizard" 
      />

    </div>
  </transition>
</template>

<script setup>
import { ref } from 'vue'

import PacienteModal from '@/shared/modals/PacienteModal.vue'
import DireccionModal from '@/shared/modals/DireccionModal.vue'
import InfoSocioeconomicaWizardModal from '@/shared/modals/info-socioeconomica-wizard.vue'

const mostrarHeader = ref(true)
const valorLocal = ref('')

const mostrarModalPaciente = ref(false)
const mostrarModalDireccion = ref(false)
const mostrarModalWizard = ref(false)

const progreso = ref({
  paciente: false,
  direccion: false,
  estudio: false
})

const abrirModalPrueba = () => {
  mostrarModalPaciente.value = true
}

const abrirModalDireccion = () => {
  mostrarModalDireccion.value = true
}

const abrirModalWizard = () => {
  mostrarModalWizard.value = true
}

const simularGuardado = (datos) => {
  progreso.value.paciente = true
  console.log("Paciente:", datos)
}

const simularGuardadoDireccion = () => {
  progreso.value.direccion = true
  console.log("Dirección guardada")
}

const simularGuardadoWizard = () => {
  progreso.value.estudio = true
  console.log("Wizard completado")
}
</script>

<style scoped src="../../content/css/searchbar2.css"></style>