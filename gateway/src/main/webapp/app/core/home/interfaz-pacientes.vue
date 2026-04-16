<script setup lang="ts">
import Swal from 'sweetalert2'
import { ref, watch } from 'vue'
import { usePacienteSearch } from '@/entities/pacientems/paciente/paciente-search'
import SearchModule from '@/intefaz/searchModule.vue'
import SearchModule2 from '@/intefaz/searchModule2.vue'
import SidebarModule from '@/intefaz/sidebarModule.vue'
import Modulos from '@/intefaz/modulos.vue'
import TablaModule from '@/intefaz/tablaModule.vue'

// Extraemos todo lo que nos da tu composable
const { searchQuery, resultados, estaCargando, error, buscarPorEcu } = usePacienteSearch()

// 🔥 FUNCIÓN MEJORADA
const manejarBusqueda = async () => {

  // ⚠️ VALIDAR CAMPO VACÍO
  if (!searchQuery.value.trim()) {
    Swal.fire({
      title: 'Campo vacío',
      text: 'Escribe un nombre o ECU para buscar',
      icon: 'warning',
      confirmButtonColor: '#3085d6'
    })
    return
  }

  // 🔥 LOADER DIRECTO
  Swal.fire({
    title: 'Buscando...',
    text: 'Consultando en la base de datos',
    allowOutsideClick: false,
    didOpen: () => {
      Swal.showLoading()
    }
  })

  await buscarPorEcu()

  Swal.close()

  // 🔥 RESULTADO FINAL
  if (resultados.value.length > 0) {
    Swal.fire({
      title: 'Paciente encontrado',
      icon: 'success',
      timer: 1500,
      showConfirmButton: false
    })
  } else {
    Swal.fire({
      title: 'Paciente no encontrado',
      text: 'Verifica los datos ingresados',
      icon: 'error'
    })
  }
}
// 🔥 CONTROL DE VISTAS
const mostrarBuscador = ref(true)
const mostrarClipboard = ref(false)

watch(mostrarBuscador, (val) => {
  if (val) {
    resultados.value.splice(0)
  }
})
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

        <p v-if="estaCargando" class="status-text status-area">
          Buscando en la base de datos...
        </p>

        <p v-else-if="error" class="status-text status-area" style="color: #ff4d4f;">
          {{ error }}
        </p>

        <p v-else-if="resultados.length === 0" class="status-text status-area">
          Esperando búsqueda de paciente...
        </p>

        <p v-else class="status-text status-area" style="color: #52c41a;">
          Paciente localizado.
        </p>
      </div>

      <!-- 📋 SEARCH CLIPBOARD -->
      <div v-show="mostrarClipboard" class="search-section glass-sidebar">
        <searchModule2 
          :key="'clipboard-' + mostrarClipboard"
          v-model="searchQuery"
          :mostrarHeader="mostrarClipboard"
        />
      </div>

    </aside>

    <main class="right-panel-workspace">
      <section class="work-area" v-show="resultados.length > 0">
        <div class="paciente-data-preview">
          <modulos :paciente="resultados[0]" />
          <tablaModule :paciente="resultados[0]" />
        </div>
      </section>  
    </main>
  </div>
</template>

<style scoped src="../../../content/css/main.css"></style>