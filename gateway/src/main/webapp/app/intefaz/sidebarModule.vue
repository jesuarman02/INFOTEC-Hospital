<template>
  <div class="sidebar-wrapper">
    <nav class="sidebar-icons">
      <div class="icon-group">
        <div class="icon-item" @click="$emit('toggleHeader')">
          <img src="/content/images/md-del-usuario.svg" class="sidebar-svg" alt="Médico" />
        </div>
        <div class="icon-item" @click="irCalendario">
          <img src="/content/images/calendario.svg" class="sidebar-svg" alt="Calendario" />
        </div>
        <div class="icon-item" @click="mostrarSubirArchivos = true">
          <img src="/content/images/carpeta.svg" class="sidebar-svg" alt="Expedientes" />
        </div>
      </div>
    </nav>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
    name: 'SidebarModule',
    // Recibimos los estados desde el padre
    props: {
        mostrarHeader: Boolean,
        mostrarSubirArchivos: Boolean
    },
    // Definimos los eventos para actualizar al padre
    emits: ['update:mostrarHeader', 'update:mostrarSubirArchivos'],
    setup() {
        const router = useRouter();

        const irCalendario = () => {
            router.push('/calendario');
        };

        return {
            irCalendario
        };
    }
});
</script>
<style scoped src="../../content/css/navbar.css"></style>