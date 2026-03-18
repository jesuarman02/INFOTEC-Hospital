<script setup lang="ts">
import { ref } from 'vue';
import { usePacienteSearch } from '@/entities/pacientems/paciente/paciente-search';
import SearchModule from '@/intefaz/searchModule.vue';
import SidebarModule from '@/intefaz/sidebarModule.vue';
import Modulos from '@/intefaz/modulos.vue';
import TablaModule from '@/intefaz/tablaModule.vue';

// Extraemos todo lo que nos da tu composable
const { searchQuery, resultados, estaCargando, error, buscarPorEcu } = usePacienteSearch();

// Ejecuta la búsqueda
const manejarBusqueda = async () => {
  await buscarPorEcu();
};
</script>

<template>
  <div class="interface-container">
    <aside class="left-panel-glass">
      <sidebarModule />
      
      <div class="search-section glass-sidebar">
        <searchModule v-model="searchQuery" @buscar="manejarBusqueda" />
        
        <p v-if="estaCargando" class="status-text status-area">Buscando en la base de datos...</p>
        <p v-else-if="error" class="status-text status-area" style="color: #ff4d4f;">{{ error }}</p>
        <p v-else-if="resultados.length === 0" class="status-text status-area">Esperando búsqueda de paciente...</p>
        <p v-else class="status-text status-area" style="color: #52c41a;">Paciente localizado.</p>
      </div>
    </aside>

    <main class="right-panel-workspace">
      <section class="work-area" v-if="resultados.length > 0">
         <div class="paciente-data-preview">
           <modulos :paciente="resultados[0]" />
           <tablaModule :paciente="resultados[0]" />
         </div>
      </section>  
    </main>
  </div>
</template>

<style scoped src="../../../content/css/main.css"></style>