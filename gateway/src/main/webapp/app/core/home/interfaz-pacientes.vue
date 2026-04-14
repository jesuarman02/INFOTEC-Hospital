<script setup lang="ts">
import { ref } from 'vue';
import { usePacienteSearch } from '@/entities/pacientems/paciente/paciente-search';
import SearchModule from '@/intefaz/searchModule.vue';
import SearchModule2 from '@/intefaz/searchModule2.vue'; // 🔥 AGREGADO
import SidebarModule from '@/intefaz/sidebarModule.vue';
import Modulos from '@/intefaz/modulos.vue';
import TablaModule from '@/intefaz/tablaModule.vue';
import {watch} from 'vue';



// Extraemos todo lo que nos da tu composable
const { searchQuery, resultados, estaCargando, error, buscarPorEcu } = usePacienteSearch();

// Ejecuta la búsqueda
const manejarBusqueda = async () => {
  await buscarPorEcu();
};

// 🔥 CONTROL DE VISTAS
const mostrarBuscador = ref(true);
const mostrarClipboard = ref(false); // 🔥 NUEVO
watch(mostrarBuscador, (val)=>{
  if(val){
    resultados.splice(0);
  }
});


</script>

<template>
  <div class="interface-container">
    <aside class="left-panel-glass">

      <!-- 🔥 SIDEBAR -->
      <sidebarModule 
        @toggle-search="() => { mostrarBuscador = true; mostrarClipboard = false; resultados.splice(0) }"
        @toggle-clipboard="() => { mostrarClipboard = true; mostrarBuscador = false }"
      />

      <!-- 🔍 SEARCH NORMAL -->
      <div v-show="mostrarBuscador" class="search-section glass-sidebar">
        <searchModule
          :key="'buscador-' + mostrarBuscador" 
          v-model="searchQuery" 
          @buscar="manejarBusqueda"
          @ocultar-header="mostrarBuscador = false"
        />

        <p v-if="estaCargando" class="status-text status-area">Buscando en la base de datos...</p>
        <p v-else-if="error" class="status-text status-area" style="color: #ff4d4f;">{{ error }}</p>
        <p v-else-if="resultados.length === 0" class="status-text status-area">Esperando búsqueda de paciente...</p>
        <p v-else class="status-text status-area" style="color: #52c41a;">Paciente localizado.</p>
      </div>

      <!-- 📋 SEARCH CLIPBOARD (🔥 NUEVO) -->
      <div v-show="mostrarClipboard" class="search-section glass-sidebar">
        <searchModule2 
        :key="'clipboard-'+mostrarClipboard"
        v-model="searchQuery"
        :mostrarHeader="mostrarClipboard"
        />
      </div>

    </aside>

    <main class="right-panel-workspace">
      <section class="work-area" v-show="resultados.length > 0">
        <div class="paciente-data-preview">
          <modulos :paciente ="resultados[0]" />
          <tablaModule :paciente="resultados[0]" />
        </div>
      </section>  
    </main>
  </div>
</template>

<style scoped src="../../../content/css/main.css"></style>