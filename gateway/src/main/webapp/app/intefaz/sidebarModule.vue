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

        <!-- BOTÓN TEMPORAL PARA PROBAR EL MODAL -->
        <div class="icon-item" @click="abrirModalPrueba" style="background-color: #ffeeba; border-radius: 8px;">
           <span style="font-size: 1.5rem;">🧑‍⚕️</span>
        </div>

      </div>
    </nav>

    <!-- AQUÍ VIVE NUESTRO NUEVO MODAL OCULTO -->
    <PacienteModal 
      v-model:visible="mostrarModalPaciente" 
      @save="simularGuardado" 
    />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'
// Importamos el componente que acabamos de crear
import PacienteModal from '@/shared/modals/PacienteModal.vue'

export default defineComponent({
  name: 'SidebarModule',
  
  components: {
    PacienteModal
  },

  props: {
    mostrarHeader: Boolean,
    mostrarSubirArchivos: Boolean
  },
  
  // Declaramos los emits correctamente para el setup tradicional
  emits: ['toggle-search', 'update:mostrarSubirArchivos'],
  
  setup(props, { emit }) {
    const router = useRouter()
    
    // Variable reactiva que controla si el modal se ve o no
    const mostrarModalPaciente = ref(false)

    const irCalendario = () => {
      router.push('/calendario')
    }

    const abrirModalPrueba = () => {
      mostrarModalPaciente.value = true
    }

    // Función que se ejecuta cuando el modal emite el evento 'save'
    const simularGuardado = (datosDelPaciente: any) => {
      console.log("¡Éxito! El modal mandó estos datos listos para enviar a Java:", datosDelPaciente)
      alert("Revisa la consola (F12) para ver los datos capturados.")
    }

    return {
      irCalendario,
      mostrarModalPaciente,
      abrirModalPrueba,
      simularGuardado
    }
  }
})
</script>

<style scoped src="../../content/css/navbar.css"></style>