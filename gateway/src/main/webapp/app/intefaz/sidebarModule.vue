<template>
  <div class="sidebar-wrapper">
    <nav class="sidebar-icons">
      <div class="icon-group">

        <!-- PACIENTES -->
        <div class="icon-item" @click="$emit('toggle-search')">
          <img src="/content/images/md-del-usuario.svg" class="sidebar-svg" alt="Médico" />
        </div>

        <!-- CALENDARIO -->
        <div class="icon-item" @click="irCalendario">
          <img src="/content/images/calendario.svg" class="sidebar-svg" alt="Calendario" />
        </div>

        <!-- EXPEDIENTES -->
        <div class="icon-item" @click="$emit('update:mostrarSubirArchivos', true)">
          <img src="/content/images/carpeta.svg" class="sidebar-svg" alt="Expedientes" />
        </div>

        <!-- NUEVO PANEL -->
        <div class="icon-item" @click="$emit('toggle-nuevo')">
          <img src="/content/images/clipboard.svg" class="sidebar-svg" alt="Nuevo" />
        </div>

      </div>
    </nav>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { defineEmits } from 'vue'

export default defineComponent({
  name: 'SidebarModule',

  setup() {
    const emit = defineEmits([
      'toggle-search',
      'toggle-nuevo',
      'update:mostrarSubirArchivos'
    ]);

    const router = useRouter()
    const route = useRoute()

    const irCalendario = () => {
      if (route.path === '/calendario'){
        router.back()
      } else {
        router.push('/calendario')
      }
    }

    return {
      irCalendario
    }
  }
})
</script>

<style scoped src="../../content/css/navbar.css"></style>