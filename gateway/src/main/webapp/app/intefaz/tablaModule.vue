<template>
  <div class="card shadow-sm border-0">
    
    <div class="card-header bg-white pb-0">
      <ul class="nav nav-tabs border-bottom-0">
        <li class="nav-item">
          <a class="nav-link text-secondary" style="cursor: pointer;" 
             :class="{ 'active font-weight-bold text-danger': tabActual === 'datos' }" 
             @click="tabActual = 'datos'">Datos del Paciente</a>
        </li>
        <li class="nav-item">
          <a class="nav-link text-secondary" style="cursor: pointer;" 
             :class="{ 'active font-weight-bold text-danger': tabActual === 'medicos' }" 
             @click="tabActual = 'medicos'">Datos Médicos</a>
        </li>
        <li class="nav-item">
          <a class="nav-link text-secondary" style="cursor: pointer;" 
             :class="{ 'active font-weight-bold text-danger': tabActual === 'diagnostico' }" 
             @click="tabActual = 'diagnostico'">Diagnóstico</a>
        </li>
        <li class="nav-item">
          <a class="nav-link text-secondary" style="cursor: pointer;" 
             :class="{ 'active font-weight-bold text-danger': tabActual === 'medico_responsable' }" 
             @click="tabActual = 'medico_responsable'">Datos del médico</a>
        </li>
      </ul>
    </div>

    <div class="card-body bg-light p-4">
      
      <div v-if="tabActual === 'datos'">
        <div class="container-fluid p-0">
          
          <div class="card border-light shadow-sm mb-4">
            <div class="card-body">
              <h5 class="text-danger mb-4 border-bottom pb-2">Datos Generales</h5>
              <div class="d-flex flex-wrap">
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">CURP</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.curp || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Nombre(s)</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.nombre || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Primer Apellido</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.apellidoPaterno || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Segundo Apellido</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.apellidoMaterno || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Fecha de Nacimiento</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.fechaNacimiento || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Edad</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ calcularEdad(paciente?.fechaNacimiento) }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Sexo / Género</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.sexo || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Nacionalidad</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.nacionalidad || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Estado Civil</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.estadoCivil || 'N/A' }}</span>
                </div>
              </div>
            </div>
          </div>

         <div class="card border-light shadow-sm mb-4">
            <div class="card-body">
              <h5 class="text-danger mb-4 border-bottom pb-2">Dirección y Contacto</h5>
              <div class="d-flex flex-wrap">
                
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Calle y Número</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">
                    {{ paciente?.direccion?.vialidad || '' }} 
                    {{ paciente?.direccion?.nombreVialidad || 'No registrada' }} 
                    {{ paciente?.direccion?.numExterior || '' }} 
                    {{ paciente?.direccion?.numInterior ? 'Int. ' + paciente?.direccion?.numInterior : '' }}
                  </span>
                </div>
                
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Colonia</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.direccion?.codigoPostalInfo?.asentamiento || 'No registrada' }}</span>
                </div>
                
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Alcaldía / Municipio</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.direccion?.codigoPostalInfo?.municipio || 'No registrada' }}</span>
                </div>
                
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Estado</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.direccion?.codigoPostalInfo?.estado || 'No registrada' }}</span>
                </div>
                
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Código Postal</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.direccion?.codigoPostalInfo?.codigo || 'No registrada' }}</span>
                </div>
                
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Teléfono</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.direccion?.telefono || 'No registrado' }}</span>
                </div>

              </div>
            </div>
          </div>

          <div class="card border-light shadow-sm mb-4">
            <div class="card-body">
              <h5 class="text-danger mb-4 border-bottom pb-2">Información Socioeconómica</h5>
              <p class="text-muted mb-0"><em>Sección pendiente de integrar con el microservicio correspondiente.</em></p>
            </div>
          </div>

        </div>
      </div>

      <div v-if="tabActual === 'medicos'">
        <div class="card border-light shadow-sm mb-4">
          <div class="card-body">
            <h5 class="text-danger mb-4 border-bottom pb-2">DATOS MÉDICOS Y ALERTAS</h5>
            <p class="text-muted mb-0"><em>Datos clínicos pendientes de integrar.</em></p>
          </div>
        </div>
      </div>

      <div v-if="tabActual === 'diagnostico'">
        <div class="p-2">
          <p class="text-muted"><em>Historial de diagnósticos en desarrollo.</em></p>
        </div>
      </div>

      <div v-if="tabActual === 'medico_responsable'">
        <div class="card border-light shadow-sm mb-4">
          <div class="card-body">
            <h5 class="text-danger mb-4 border-bottom pb-2">PERSONAL MÉDICO RESPONSABLE</h5>
            <div class="d-flex flex-wrap">
              <div class="data-item mr-5 mb-4">
                <label class="text-muted small d-block font-weight-bold">MÉDICO ENCARGADO</label>
                <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.medicoEncargado || 'Sin asignar' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'; // ¡Importante traer el ref para las pestañas!

// 0. Variable reactiva que controla la pestaña visible
const tabActual = ref('datos');

// 1. Ahora recibimos UN paciente (igual que en modulos.vue)
const props = defineProps({
  paciente: {
    type: Object,
    required: false, // Lo ponemos false por si aún no buscamos a nadie
    default: () => ({})
  }
});

// 2. Función para calcular la edad automáticamente a partir de la fecha
const calcularEdad = (fechaNacimiento: string | undefined) => {
  if (!fechaNacimiento) return 'N/A';
  
  const hoy = new Date();
  const cumpleanos = new Date(fechaNacimiento);
  let edad = hoy.getFullYear() - cumpleanos.getFullYear();
  const m = hoy.getMonth() - cumpleanos.getMonth();
  
  if (m < 0 || (m === 0 && hoy.getDate() < cumpleanos.getDate())) {
    edad--;
  }
  
  return isNaN(edad) ? 'N/A' : `${edad} años`;
};
</script>

<style scoped src="../../content/css/tablas.css"></style>