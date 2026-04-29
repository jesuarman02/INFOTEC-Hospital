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

