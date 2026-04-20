<template>
  <div class="sidebar-wrapper">
    <nav class="sidebar-icons">
      <div class="icon-group">

        <div class="icon-item icon-yellow" @click="$emit('toggle-search')" title="Buscar Paciente">
          <img src="/content/images/md-del-usuario.svg" class="sidebar-svg" />
        </div>
        
        <div class="icon-item icon-blue" @click="$emit('toggle-clipboard')" title="Asignaciones">
          <img src="/content/images/clipboard.svg" class="sidebar-svg"/>
        </div>

        <div class="icon-item icon-blue" @click="irCalendario" title="Calendario">
          <img src="/content/images/calendario.svg" class="sidebar-svg" />
        </div>
        
        <div class="icon-item icon-green" @click="$emit('update:mostrarSubirArchivos', true)" title="Expedientes">
          <img src="/content/images/carpeta.svg" class="sidebar-svg" />
        </div>
        
      </div>
    </nav>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useRouter, useRoute } from 'vue-router'

export default defineComponent({
  name: 'SidebarModule',
  props: { mostrarHeader: Boolean, mostrarSubirArchivos: Boolean },
  emits: ['toggle-search', 'update:mostrarSubirArchivos', 'toggle-nuevo', 'toggle-clipboard'],
  
  setup(props, { emit }) {
    const router = useRouter(); 
    const route = useRoute();

    const irCalendario = () => { route.path === '/calendario' ? router.back() : router.push('/calendario') }

    return { irCalendario }
  }
})
</script>

<style scoped src="../../content/css/navbar.css"></style>