<template>
  <div class="sidebar-wrapper">
    <nav class="sidebar-icons">
      <div class="icon-group">

        <div class="icon-item icon-yellow" @click="$emit('toggle-search')" title="Buscar Paciente">
          <img src="/content/images/md-del-usuario.svg" class="sidebar-svg" />
        </div>
        
        <div class="icon-item icon-blue" @click="irCalendario" title="Calendario">
          <img src="/content/images/calendario.svg" class="sidebar-svg" />
        </div>
        
        <div class="icon-item icon-green" @click="$emit('update:mostrarSubirArchivos', true)" title="Expedientes">
          <img src="/content/images/carpeta.svg" class="sidebar-svg" />
        </div>
        
        <div class="icon-item icon-blue" @click="$emit('toggle-clipboard')" title="Asignaciones">
          <img src="/content/images/clipboard.svg" class="sidebar-svg"/>
        </div>
        
        <div class="icon-separator"></div>

        <div class="icon-item icon-yellow" @click="abrirModalPrueba" title="Modal Paciente">
          <span class="emoji-icon">🧑‍⚕️</span>
        </div>
        
        <div class="icon-item icon-blue" @click="abrirModalDireccion" title="Modal Dirección">
          <span class="emoji-icon">📍</span>
        </div>
        
        <div class="icon-item icon-green" @click="abrirModalWizard" title="Estudio Socioeconómico">
          <span class="emoji-icon">📋</span>
        </div>

      </div>
    </nav>

    <PacienteModal v-model:visible="mostrarModalPaciente" @save="simularGuardado" />
    <DireccionModal v-model:visible="mostrarModalDireccion" @saved="simularGuardadoDireccion" />
    <InfoSocioeconomicaWizardModal v-model:visible="mostrarModalWizard" @saved="simularGuardadoWizard" />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import PacienteModal from '@/shared/modals/PacienteModal.vue'
import DireccionModal from '@/shared/modals/DireccionModal.vue'
import InfoSocioeconomicaWizardModal from '@/shared/modals/info-socioeconomica-wizard.vue'

export default defineComponent({
  name: 'SidebarModule',
  components: { PacienteModal, DireccionModal, InfoSocioeconomicaWizardModal },
  props: { mostrarHeader: Boolean, mostrarSubirArchivos: Boolean },
  emits: ['toggle-search', 'update:mostrarSubirArchivos', 'toggle-nuevo', 'toggle-clipboard'],
  
  setup(props, { emit }) {
    const router = useRouter(); const route = useRoute();
    const mostrarModalPaciente = ref(false);
    const mostrarModalDireccion = ref(false);
    const mostrarModalWizard = ref(false);

    const irCalendario = () => { route.path === '/calendario' ? router.back() : router.push('/calendario') }
    const abrirModalPrueba = () => mostrarModalPaciente.value = true;
    const abrirModalDireccion = () => mostrarModalDireccion.value = true;
    const abrirModalWizard = () => mostrarModalWizard.value = true;

    const simularGuardado = (datos: any) => console.log("Guardado:", datos);
    const simularGuardadoDireccion = () => console.log("Dirección guardada");
    const simularGuardadoWizard = () => console.log("Socioeconómico guardado");

    return {
      irCalendario, mostrarModalPaciente, abrirModalPrueba, simularGuardado,
      mostrarModalDireccion, abrirModalDireccion, simularGuardadoDireccion,
      mostrarModalWizard, abrirModalWizard, simularGuardadoWizard
    }
  }
})
</script>

<style scoped src="../../content/css/navbar.css"></style>