<script setup lang="ts">
import { ref } from 'vue';
import { usePacienteSearch } from '@/entities/pacientems/paciente/paciente-search';
import SearchModule from '@/intefaz/searchModule.vue';
import SidebarModule from '@/intefaz/sidebarModule.vue';
import Modulos from '@/intefaz/modulos.vue';
import TablaModule from '@/intefaz/tablaModule.vue';
const { searchQuery, resultados, buscarPorEcu } = usePacienteSearch();

// Función que se ejecuta cuando el buscador termina
const manejarBusqueda = async () => {
  await buscarPorEcu();
  // El resultado queda en la variable 'resultados'
};
</script>

<template>
  <div class="interface-container">
    <aside class="left-panel-glass">
      <sidebarModule />
      <div class="search-section, glass-sidebar">

        <searchModule />
        <p class="status-text, status-area">Esperando búsqueda de paciente...</p>
      </div>
    </aside>

    <main class="right-panel-workspace">
      <section class="work-area">
        <modulos />
      </section>
      <section class="table-scroll-section">
        <tablaModule /> </section>

      </main>
  </div>
</template>
<style scoped src="../../../content/css/main.css"></style>