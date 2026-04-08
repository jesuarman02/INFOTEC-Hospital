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

        <!-- BOTÓN TEMPORAL PARA PROBAR EL MODAL DE PACIENTE -->
        <div class="icon-item" @click="abrirModalPrueba" style="background-color: #ffeeba; border-radius: 8px;">
           <span style="font-size: 1.5rem;">🧑‍⚕️</span>
        </div>

        <!-- 🚀 NUEVO BOTÓN TEMPORAL PARA PROBAR EL MODAL DE DIRECCIÓN -->
        <div class="icon-item" @click="abrirModalDireccion" style="background-color: #cff4fc; border-radius: 8px; margin-top: 10px;">
           <span style="font-size: 1.5rem;">📍</span>
        </div>

      </div>
    </nav>

    <!-- MODAL OCULTO DE PACIENTES -->
    <PacienteModal 
      v-model:visible="mostrarModalPaciente" 
      @save="simularGuardado" 
    />

    <!-- 🚀 NUEVO MODAL OCULTO DE DIRECCIÓN -->
    <DireccionModal 
      v-model:visible="mostrarModalDireccion" 
      @saved="simularGuardadoDireccion" 
    />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
// Importamos los componentes
import PacienteModal from '@/shared/modals/PacienteModal.vue'
import DireccionModal from '@/shared/modals/DireccionModal.vue' // 🚀 Importamos el nuevo modal

export default defineComponent({
  name: 'SidebarModule',
  
  components: {
    PacienteModal,
    DireccionModal // 🚀 Lo registramos
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
    const mostrarModalDireccion = ref(false) // 🚀 Estado para el modal de dirección

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

    // 🚀 Función para abrir el modal de dirección
    const abrirModalDireccion = () => {
      mostrarModalDireccion.value = true
    }

    const simularGuardado = (datosDelPaciente: any) => {
      console.log("¡Éxito! El modal mandó estos datos listos para enviar a Java:", datosDelPaciente)
      alert("Revisa la consola (F12) para ver los datos capturados.")
    }

    // 🚀 Función para simular cuando el modal de dirección termina
    const simularGuardadoDireccion = () => {
      console.log("¡El modal de dirección finalizó su guardado con éxito!")
    }

    return {
      irCalendario,
      mostrarModalPaciente,
      abrirModalPrueba,
      simularGuardado,
      // 🚀 Exponemos las nuevas variables a la vista
      mostrarModalDireccion,
      abrirModalDireccion,
      simularGuardadoDireccion
    }
  }
})
</script>

<style scoped src="../../content/css/navbar.css"></style>