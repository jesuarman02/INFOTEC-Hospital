<template>
  <div class="sidebar-wrapper">
    <nav class="sidebar-icons">
      <div class="icon-group">

        <!-- PACIENTES -->
        <div class="icon-item" @click="$emit('toggle-search')">
          <img src="/content/images/md-del-usuario.svg" class="sidebar-svg" alt="Médico" />
        </div>
        
        <div class="icon-item" @click="irCalendario">
          <img src="/content/images/calendario.svg" class="sidebar-svg" alt="Calendario" />
        </div>

        <!-- EXPEDIENTES -->
        <div class="icon-item" @click="$emit('update:mostrarSubirArchivos', true)">
          <img src="/content/images/carpeta.svg" class="sidebar-svg" alt="Expedientes" />
        </div>

        <!-- CLIPBOARD -->
        <div class="icon-item" @click="$emit('toggle-clipboard')">
          <img src="/content/images/clipboard.svg" class="sidebar-svg"/>
        </div>

        <!-- BOTÓN TEMPORAL PACIENTE -->
        <div class="icon-item" @click="abrirModalPrueba" style="background-color: #ffeeba; border-radius: 8px;">
           <span style="font-size: 1.5rem;">🧑‍⚕️</span>
        </div>

        <!-- BOTÓN TEMPORAL DIRECCIÓN -->
        <div class="icon-item" @click="abrirModalDireccion" style="background-color: #cff4fc; border-radius: 8px; margin-top: 10px;">
           <span style="font-size: 1.5rem;">📍</span>
        </div>

      </div>
    </nav>

    <!-- MODAL PACIENTE -->
    <PacienteModal 
      v-model:visible="mostrarModalPaciente" 
      @save="simularGuardado" 
    />

    <!-- MODAL DIRECCIÓN -->
    <DireccionModal 
      v-model:visible="mostrarModalDireccion" 
      @saved="simularGuardadoDireccion" 
    />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import PacienteModal from '@/shared/modals/PacienteModal.vue'
import DireccionModal from '@/shared/modals/DireccionModal.vue'

export default defineComponent({
  name: 'SidebarModule',

  components: {
    PacienteModal,
    DireccionModal
  },

  props: {
    mostrarHeader: Boolean,
    mostrarSubirArchivos: Boolean
  },

  emits: [
    'toggle-search',
    'toggle-nuevo',
    'update:mostrarSubirArchivos',
    'toggle-clipboard'
  ],

  setup(props, { emit }) {
    const router = useRouter()
    const route = useRoute()

    const mostrarModalPaciente = ref(false)
    const mostrarModalDireccion = ref(false)

    const irCalendario = () => {
      if (route.path === '/calendario'){
        router.back()
      } else {
        router.push('/calendario')
      }
    }

    const abrirModalPrueba = () => {
      mostrarModalPaciente.value = true
    }

    const abrirModalDireccion = () => {
      mostrarModalDireccion.value = true
    }

    const simularGuardado = (datosDelPaciente: any) => {
      console.log("¡Éxito! Datos:", datosDelPaciente)
      alert("Revisa la consola (F12)")
    }

    const simularGuardadoDireccion = () => {
      console.log("Modal de dirección guardado correctamente")
    }

    return {
      irCalendario,
      mostrarModalPaciente,
      abrirModalPrueba,
      simularGuardado,
      mostrarModalDireccion,
      abrirModalDireccion,
      simularGuardadoDireccion
    }
  }
})
</script>

<style scoped src="../../content/css/navbar.css"></style>