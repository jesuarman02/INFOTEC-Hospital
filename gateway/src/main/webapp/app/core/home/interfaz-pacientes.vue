<script setup lang="ts">
import Swal from 'sweetalert2'
import { ref, watch } from 'vue'
import { usePacienteSearch } from '@/entities/pacientems/paciente/paciente-search'
import SearchModule from '@/intefaz/searchModule.vue'
import SearchModule2 from '@/intefaz/searchModule2.vue'
import SidebarModule from '@/intefaz/sidebarModule.vue'
import Modulos from '@/intefaz/modulos.vue'
import TablaModule from '@/intefaz/tablaModule.vue'

const { searchQuery, resultados, estaCargando, error, buscarPorEcu } = usePacienteSearch()

const manejarBusqueda = async () => {
  if (!searchQuery.value.trim()) {
    Swal.fire({ title: 'Campo vacío', text: 'Escribe un nombre o ECU para buscar', icon: 'warning', confirmButtonColor: '#5c1830' })
    return
  }

  Swal.fire({
    title: 'Buscando...',
    text: 'Consultando en la base de datos',
    allowOutsideClick: false,
    didOpen: () => { Swal.showLoading() }
  })

  await buscarPorEcu()
  Swal.close()

  if (resultados.value.length > 0) {
    Swal.fire({ title: 'Paciente encontrado', icon: 'success', timer: 1500, showConfirmButton: false })
  } else {
    Swal.fire({ title: 'Paciente no encontrado', text: 'Verifica los datos ingresados', icon: 'error' })
  }
}

const mostrarBuscador = ref(true)
const mostrarClipboard = ref(false)

watch(mostrarBuscador, (val) => {
  if (val) resultados.value.splice(0)
})
</script>

<template>
  <div class="interface-container">
    
    <sidebarModule 
      @toggle-search="() => { mostrarBuscador = true; mostrarClipboard = false; resultados.splice(0) }"
      @toggle-clipboard="() => { mostrarClipboard = true; mostrarBuscador = false }"
    />

    <main class="right-panel-workspace">
      
      <div v-show="mostrarBuscador && resultados.length === 0" class="search-section-centered">
        <searchModule
          :key="'buscador-' + mostrarBuscador" 
          v-model="searchQuery" 
          @buscar="manejarBusqueda"
          @ocultar-header="mostrarBuscador = false"
        />
        <p v-if="estaCargando" class="text-muted mt-3 font-weight-bold text-center">
          <span class="spinner-border spinner-border-sm mr-2"></span> Buscando en la base de datos...
        </p>
      </div>

      <div v-show="mostrarClipboard && resultados.length === 0" class="search-section-centered">
        <searchModule2 
          :key="'clipboard-' + mostrarClipboard"
          v-model="searchQuery"
          :mostrarHeader="mostrarClipboard"
          @ir-buscar="mostrarBuscador = true; mostrarClipboard = false"
        />
      </div>

     <section class="work-area" v-show="resultados.length > 0">
  <div class="paciente-data-preview">
    
    <div class="columna-izquierda">
      <modulos :paciente="resultados[0]" />
    </div>

    <div class="columna-derecha">
      <tablaModule :paciente="resultados[0]" />
    </div>

  </div>
</section>

    </main>
  </div>
</template>

<style scoped src="../../../content/css/main.css"></style>

<style scoped>
.interface-container {
  width: 100vw;
  position: relative;
  left: 50%;
  right: 50%;
  margin-left: -50vw;
  margin-right: -50vw;
  display: flex;
  min-height: calc(100vh - 70px);
  background-color: #f4f6f9; 
}

.right-panel-workspace {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  
  /* 🔥 LA SOLUCIÓN AL CORTE 🔥 */
  justify-content: flex-start; /* Inicia el contenido desde arriba SIEMPRE */
  
  padding-left: 100px; /* Protege el espacio del sidebar */
  min-height: 100%;
}

.search-section-centered {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  
  /* 🔥 EL TRUCO PARA EL BUSCADOR 🔥 */
  flex-grow: 1; /* Empuja el buscador al centro cuando está solo */
  min-height: calc(100vh - 150px); 
  padding: 2rem;
}

.work-area {
  width: 100%;
  height: auto; 
  flex-grow: 1;
  padding: 2rem; /* Le da aire a la interfaz por los lados y por arriba */
}

/* 📱 MODO RESPONSIVO PARA LA ESTRUCTURA */
@media (max-width: 1024px) {
  .right-panel-workspace {
    padding-left: 0; 
    padding-top: 90px; 
    justify-content: flex-start; 
  }
  .search-section-centered {
    min-height: auto; /* En celular no forzamos el alto completo */
    margin-top: 2rem;
  }
}
</style>