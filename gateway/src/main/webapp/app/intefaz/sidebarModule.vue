<template>
  <div class="sidebar-wrapper">
    <nav class="sidebar-icons">
      <div class="icon-group">

        <div class="icon-item" @click="$emit('toggle-search')">
          <img src="/content/images/md-del-usuario.svg" class="sidebar-svg" alt="Médico" />
        </div>
        
        <div class="icon-item" @click="irCalendario">
          <img src="/content/images/calendario.svg" class="sidebar-svg" alt="Calendario" />
        </div>

        <div class="icon-item" @click="$emit('update:mostrarSubirArchivos', true)">
          <img src="/content/images/carpeta.svg" class="sidebar-svg" alt="Expedientes" />
        </div>

        <div class="icon-item" @click="abrirModalPrueba" style="background-color: #ffeeba; border-radius: 8px; margin-top: 20px;">
           <span style="font-size: 1.5rem;">🧑‍⚕️</span>
        </div>

        <div class="icon-item" @click="abrirModalDireccion" style="background-color: #cff4fc; border-radius: 8px; margin-top: 10px;">
           <span style="font-size: 1.5rem;">📍</span>
        </div>

        <div class="icon-item" @click="abrirModalWizard" style="background-color: #d1e7dd; border-radius: 8px; margin-top: 10px;">
           <span style="font-size: 1.5rem;">📋</span>
        </div>

      </div>
    </nav>

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
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
// Importamos los componentes
import PacienteModal from '@/shared/modals/PacienteModal.vue'
import DireccionModal from '@/shared/modals/DireccionModal.vue'
// 🚀 Importamos el nuevo Asistente Wizard
import InfoSocioeconomicaWizardModal from '@/shared/modals/info-socioeconomica-wizard.vue'

export default defineComponent({
  name: 'SidebarModule',
  
  components: {
    PacienteModal,
    DireccionModal,
    InfoSocioeconomicaWizardModal // 🚀 Lo registramos
  },

  props: {
    mostrarHeader: Boolean,
    mostrarSubirArchivos: Boolean
  },
  
  emits: ['toggle-search', 'update:mostrarSubirArchivos'],
  
  setup(props, { emit }) {
    const router = useRouter()
    const route = useRoute()
    
    // Variables reactivas para controlar si los modales se ven o no
    const mostrarModalPaciente = ref(false)
    const mostrarModalDireccion = ref(false)
    const mostrarModalWizard = ref(false) // 🚀 Estado para el Asistente

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

    // 🚀 Función para abrir el Asistente Wizard
    const abrirModalWizard = () => {
      mostrarModalWizard.value = true
    }

    const simularGuardado = (datosDelPaciente: any) => {
      console.log("¡Éxito! El modal mandó estos datos listos para enviar a Java:", datosDelPaciente)
      alert("Revisa la consola (F12) para ver los datos capturados.")
    }

    const simularGuardadoDireccion = () => {
      console.log("¡El modal de dirección finalizó su guardado con éxito!")
    }

    // 🚀 Función para simular cuando el Asistente termina
    const simularGuardadoWizard = () => {
      console.log("¡El estudio socioeconómico finalizó y se guardó con éxito!")
    }

    return {
      irCalendario,
      mostrarModalPaciente,
      abrirModalPrueba,
      simularGuardado,
      mostrarModalDireccion,
      abrirModalDireccion,
      simularGuardadoDireccion,
      // 🚀 Exponemos las nuevas variables a la vista
      mostrarModalWizard,
      abrirModalWizard,
      simularGuardadoWizard
    }
  }
})
</script>

<style scoped src="../../content/css/navbar.css"></style>