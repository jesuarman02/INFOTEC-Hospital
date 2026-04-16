<template>
  <div class="sidebar-wrapper">
    <nav class="sidebar-icons">
      <div class="icon-group">

                <div class="icon-item icon-item-custom icon-yellow" @click="$emit('toggle-search')">
          <img src="/content/images/md-del-usuario.svg" class="sidebar-svg" />
        </div>
          
          <div class="icon-item icon-item-custom icon-blue" @click="irCalendario">
            <img src="/content/images/calendario.svg" class="sidebar-svg" />
          </div>
          
          <div class="icon-item icon-item-custom icon-green" @click="$emit('update:mostrarSubirArchivos', true)">
            <img src="/content/images/carpeta.svg" class="sidebar-svg" />
          </div>
          
          <div class="icon-item icon-item-custom icon-blue" @click="$emit('toggle-clipboard')">
            <img src="/content/images/clipboard.svg" class="sidebar-svg"/>
          </div>
          
          <!-- Los que ya tenías -->
          <div class="icon-item icon-item-custom icon-yellow" @click="abrirModalPrueba">
            <span style="font-size: 1.5rem;">🧑‍⚕️</span>
          </div>
          
          <div class="icon-item icon-item-custom icon-blue" @click="abrirModalDireccion">
            <span style="font-size: 1.5rem;">📍</span>
          </div>
          
          <div class="icon-item icon-item-custom icon-green" @click="abrirModalWizard">
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
import InfoSocioeconomicaWizardModal from '@/shared/modals/info-socioeconomica-wizard.vue'

export default defineComponent({
  name: 'SidebarModule',
  
  components: {
    PacienteModal,
    DireccionModal,
    InfoSocioeconomicaWizardModal
  },

  props: {
    mostrarHeader: Boolean,
    mostrarSubirArchivos: Boolean
  },
  
  // 🔥 Fusionamos todos los "emits" (Los de la nube y los tuyos)
  emits: [
    'toggle-search', 
    'update:mostrarSubirArchivos', 
    'toggle-nuevo', 
    'toggle-clipboard'
  ],
  
  setup(props, { emit }) {
    const router = useRouter()
    const route = useRoute()
    
    // Variables reactivas para controlar si los modales se ven o no
    const mostrarModalPaciente = ref(false)
    const mostrarModalDireccion = ref(false)
    const mostrarModalWizard = ref(false) 

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
      mostrarModalWizard,
      abrirModalWizard,
      simularGuardadoWizard
    }
  }
}) // Eliminé la coma extra que había quedado al final aquí
</script>

<style scoped src="../../content/css/navbar.css"></style>