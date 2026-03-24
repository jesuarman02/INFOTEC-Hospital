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

      </div>
    </nav>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useRouter } from 'vue-router'
import { defineEmits } from 'vue'


export default defineComponent({
  name: 'SidebarModule',

  props: {
    mostrarHeader: Boolean,
    mostrarSubirArchivos: Boolean
  },
  
  setup() {
    const emit = defineEmits(['toggle-search']);

    const router = useRouter()

    const irCalendario = () => {
      router.push('/calendario')
    }

    return {
      irCalendario
    }
  }
})
</script>

<style scoped src="../../content/css/navbar.css"></style>