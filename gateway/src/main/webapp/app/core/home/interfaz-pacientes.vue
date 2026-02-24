<template>
  <div class="d-flex app-container">
    <div class="sidebar-icons d-flex flex-column align-items-center py-4">
      <div class="icon-wrapper active mb-4">
        <img src="/content/images/md-del-usuario.svg" class="sidebar-svg" alt="Médico" />
      </div>
      <div class="icon-wrapper mb-4">
        <img src="/content/images/calendario.svg" class="sidebar-svg" alt="Calendario" />
      </div>
      <div class="icon-wrapper mb-4">
        <img src="/content/images/carpeta.svg" class="sidebar-svg" alt="Expedientes" />
      </div>
    </div>

    <div class="sidebar-list p-3 border-right">
      <h5 class="title-pacientes mt-2 mb-3">{{ $t('interfaz-pacientes.patients') }}</h5>

      <div class="search-container mb-4">
        <div class="input-group fancy-search">
          <div class="input-group-prepend">
            <span class="input-group-text bg-transparent border-0 pl-3">
              <font-awesome-icon icon="search" class="text-muted search-icon" />
            </span>
          </div>
          <input 
  type="text" 
  class="form-control border-0 bg-transparent search-input" 
  placeholder="ECU, Nombre del Paciente" 
  @keyup.enter="realizarBusqueda" 
/>
        </div>
      </div>
    </div>

    <div class="main-content flex-grow-1 p-4">
      <div v-if="busquedaRealizada" class="row h-100">
        <div class="col-md-5 d-flex flex-column">

          <div class="doctor-header d-flex align-items-center justify-content-between mb-2 px-2">
            <div class="doctor-name">{{ $t('interfaz-pacientes.dr') }}</div>
            <div class="specialty-name text-center">Especialidad Médica</div>
          </div>

          <div class="card dashboard-card flex-grow-1 mb-4 shadow-sm custom-grey-card">
            <div class="card-body d-flex flex-column align-items-center justify-content-center text-center">
              <h2 class="font-weight-bold mb-3">ECU</h2>
              <h4 class="mb-2">NOMBRE</h4>
              <h5 class="mb-2 text-muted">EDAD</h5>
              <h5 class="mb-2 text-muted">SEXO</h5>
              <h5 class="mb-2 text-muted">DIRECCION</h5>
              <h5 class="mb-0 text-muted">TELEFONO</h5>
            </div>
          </div>

          <div class="card dashboard-card mb-3 shadow-sm custom-grey-card" style="height: 120px">
            <div class="card-body d-flex align-items-center justify-content-around">
              <div class="vital-icon-box">
                <img src="/content/images/regla-vertical.svg" class="vital-svg" alt="Talla" />
              </div>
              <div class="vital-icon-box">
                <img src="/content/images/escala.svg" class="vital-svg" alt="Peso" />
              </div>
              <div class="vital-icon-box">
                <img src="/content/images/corazon.svg" class="vital-svg" alt="Cardio" />
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-7 d-flex flex-column">

          <!-- BOTON AGREGAR PACIENTE -->
          <div class="d-flex justify-content-end mb-2">
            <button class="btn agregar-paciente-btn">Agregar Paciente</button>
          </div>

          <div class="card dashboard-card mb-4 shadow-sm custom-grey-card flex-fill">
            <div class="card-body d-flex align-items-center justify-content-center">
              <h3 class="font-weight-bold text-uppercase">Causa de Ingreso</h3>
            </div>
          </div>

          <div class="card dashboard-card mb-4 shadow-sm custom-grey-card flex-fill">
            <div class="card-body d-flex align-items-center justify-content-center">
              <h3 class="font-weight-bold text-uppercase">Estatus del Paciente</h3>
            </div>
          </div>

          <div class="card dashboard-card mb-3 shadow-sm custom-grey-card flex-fill">
            <div class="card-body d-flex align-items-center justify-content-center">
              <h3 class="font-weight-bold text-uppercase">Tratamientos</h3>
            </div>
          </div>
        </div>
        <div>
          <b-card no-body>
            <b-tabs card>
              <b-tab title="Datos del Paciente" active>
                <b-card-text>datos como su curp etc</b-card-text>
              </b-tab>
              <b-tab title="Datos Medicos">
                <b-card-text>como altura, peso, alergias</b-card-text>
              </b-tab>
              <b-tab title="Diagnostico">
                <b-card-text>datoos de sus diagnósticos clinicos</b-card-text>
              </b-tab>
              <b-tab title="Datos del medico">
                <b-card-text>datos del médico que atendió al paciente</b-card-text>
              </b-tab>
            </b-tabs>
          </b-card>
        </div>
      </div>
      <div v-else class="d-flex flex-column align-items-center justify-content-center h-100 text-muted">
    <font-awesome-icon icon="search" size="4x" class="mb-3 opacity-20" />
    p-3000-0-0: Realice una búsqueda para ver los detalles del paciente.
  </div>
    </div> 
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'; // Importamos ref

export default defineComponent({
  name: 'InterfazPacientes',
  setup() {
    // Variable para controlar si se muestra la info
    const busquedaRealizada = ref(false);

    // Función para activar la vista al presionar Enter o buscar
    const realizarBusqueda = () => {
      busquedaRealizada.value = true;
    };

    return {
      busquedaRealizada,
      realizarBusqueda
    };
  },
});
</script>

<style scoped>
/* =========================================
   ESTILOS GENERALES
   ========================================= */
.app-container {
  height: calc(100vh - 60px);
  overflow: hidden;
  background-color: rgb(255, 255, 255); /* Fondo blanco general */
}

/* =========================================
   SIDEBARS (Estilos previos conservados)
   ========================================= */
.sidebar-icons {
  width: 80px;
  background-color: #611232;
  flex-shrink: 0;
}

.icon-wrapper {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  opacity: 0.7;
}

.icon-wrapper:hover {
  background-color: rgba(255, 255, 255, 0.1);
  opacity: 1;
}

.sidebar-svg {
  width: 24px;
  height: 24px;
  filter: invert(1);
  transition: transform 0.2s;
}

.icon-wrapper:hover .sidebar-svg {
  transform: scale(1.1);
}

.icon-wrapper.active {
  background-color: #eecbc4;
  opacity: 1;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.icon-wrapper.active .sidebar-svg {
  filter: none;
}

.sidebar-list {
  width: 300px;
  background-color: rgb(255, 255, 255);
  display: flex;
  flex-direction: column;
  border-right: 1px solid #e0e0e0;
}

.title-pacientes {
  font-weight: 800;
  letter-spacing: 1px;
  color: #333;
  font-family: 'Arial Black', sans-serif;
}

.fancy-search {
  background-color: #f1f3f5;
  border-radius: 25px;
  padding: 5px;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  border: 1px solid transparent;
}

.fancy-search:focus-within {
  background-color: #ffffff;
  border-color: #eecbc4;
  box-shadow: 0 4px 12px rgba(97, 18, 50, 0.15);
  transform: translateY(-2px);
}

.search-input {
  box-shadow: none !important;
  font-size: 0.9rem;
  color: #495057;
}

.search-icon {
  transition: color 0.3s;
}

.fancy-search:focus-within .search-icon {
  color: #611232 !important;
}

/* =========================================
   DASHBOARD CARDS (Nuevo Diseño)
   ========================================= */
.main-content {
  overflow-y: auto;
  background-color: #ffffff; /* Aseguramos fondo limpio */
}

/* Tarjetas Grises estilo MOC */
.custom-grey-card {
  background-color: #e6e6e6; /* Color gris claro del MOC */
  border: none; /* Sin bordes negros */
  border-radius: 15px; /* Bordes redondeados */
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.custom-grey-card:hover {
  transform: translateY(-3px); /* Efecto elevación al pasar mouse */
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1) !important;
}

/* NUEVOS ESTILOS */
.doctor-header {
  font-weight: 100;
  color: #333;
}

.doctor-name {
  font-size: 32px;
  font-weight: 700;
  margin-left: 45px;
}

.specialty-name {
  font-size: 32px;
  font-weight: 1000;
  margin-right: -350px;
}

.agregar-paciente-btn {
  background-color: #611232;
  color: white;
  border-radius: 90px;
  padding: 15px 48px;
  font-weight: 5600;
}

.agregar-paciente-btn:hover {
  background-color: #7a1a40;
}

/* Contenedor de iconos de signos vitales */
.vital-icon-box {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.vital-svg {
  width: 40px;
  height: 40px;
}

/* Tipografía de los bloques */
.dashboard-card h2,
.dashboard-card h3,
.dashboard-card h4,
.dashboard-card h5 {
  color: #333;
  font-family: 'Arial', sans-serif;
}
</style>
