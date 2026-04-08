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
    <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
      <h5 class="text-danger mb-0">Información Socioeconómica</h5>
      <button 
        class="btn btn-sm btn-outline-danger" 
        @click="mostrarModalSocioeconomico = true"
        :disabled="!paciente?.tieneInfoSocioeconomica"
      >
        <span v-if="paciente?.tieneInfoSocioeconomica">Ver Expediente Completo</span>
        <span v-else>No hay datos registrados</span>
      </button>
    </div>

    <div v-if="paciente?.tieneInfoSocioeconomica" class="d-flex flex-wrap">
      <div class="data-item mr-5 mb-3">
        <label class="text-muted small d-block mb-1">Ocupación Actual</label>
        <span class="h6 text-dark font-weight-bold">{{ paciente.resumenSocioeconomico?.ocupacion || 'N/A' }}</span>
      </div>
      <div class="data-item mr-5 mb-3">
        <label class="text-muted small d-block mb-1">Grado de Estudios</label>
        <span class="h6 text-dark font-weight-bold">{{ paciente.resumenSocioeconomico?.gradoEstudios || 'N/A' }}</span>
      </div>
      <div class="data-item mr-5 mb-3">
        <label class="text-muted small d-block mb-1">Ingreso Mensual</label>
        <span class="h6 text-dark font-weight-bold">
          {{ paciente.resumenSocioeconomico?.ingresoMensual !== 'N/A' ? '$' + paciente.resumenSocioeconomico?.ingresoMensual : 'N/A' }}
        </span>
      </div>
      <div class="data-item mr-5 mb-3">
        <label class="text-muted small d-block mb-1">Afiliación Médica</label>
        <span class="h6 text-dark font-weight-bold">{{ paciente.resumenSocioeconomico?.afiliacion || 'N/A' }}</span>
      </div>
    </div>
    
    <p v-else class="text-muted mb-0"><em>El paciente no cuenta con un estudio socioeconómico registrado.</em></p>
  </div>
</div>

<div v-if="mostrarModalSocioeconomico" class="card border-danger shadow-sm mb-4">
  <div class="card-header bg-white border-bottom d-flex justify-content-between align-items-center">
    <h5 class="text-danger mb-0">Expediente Socioeconómico Completo</h5>
     <button 
        class="btn btn-sm btn-outline-danger" 
        @click="mostrarModalSocioeconomico = true"
        :disabled="!paciente?.tieneInfoSocioeconomica"
      >
        <span v-if="paciente?.tieneInfoSocioeconomica">Ver Expediente Completo</span>
        <span v-else>No hay datos registrados</span>
      </button>
  </div>
  
  <div class="card-body p-0">
    <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
      <table class="table table-sm table-striped table-hover mb-0">
        <thead class="bg-dark text-white sticky-top">
          <tr>
            <th class="py-2 px-3">Pregunta</th>
            <th class="py-2 px-3">Respuesta</th>
            <th class="py-2 px-3">Detalles / Abierta</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in paciente?.expedienteCompleto" :key="item.id">
            <td class="font-weight-bold text-dark px-3 align-middle">{{ item.pregunta }}</td>
            <td class="px-3 align-middle">
              <span class="badge" style="background-color: #9b59b6; color: white; padding: 6px 10px;">
                {{ item.respuesta || 'N/A' }}
              </span>
            </td>
            <td class="text-muted px-3 align-middle">
              {{ item.respuestaAbierta || '-' }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
        </div> </div> <div v-if="tabActual === 'medicos'">
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
  </div> <div v-if="mostrarModalSocioeconomico" class="modal-overlay" @click.self="mostrarModalSocioeconomico = false">
      <div class="modal-content shadow-lg p-4 rounded-lg" style="max-width: 800px; background: white; max-height: 90vh; overflow-y: auto;">
        
        <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-4">
          <h4 class="text-danger m-0">Expediente Socioeconómico Completo</h4>
          <button class="btn btn-light rounded-circle" @click="mostrarModalSocioeconomico = false">✕</button>
        </div>

        <div v-if="paciente?.infoSocioeconomica" class="row">
          
          <div class="col-12 mb-4">
            <h6 class="text-muted text-uppercase mb-3 font-weight-bold">Datos de Vivienda</h6>
            <div class="row bg-light p-3 rounded mx-0">
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Tipo de Vivienda</small>
                <strong>{{ paciente.infoSocioeconomica.tipoVivienda || 'N/A' }}</strong>
              </div>
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Material</small>
                <strong>{{ paciente.infoSocioeconomica.materialVivienda || 'N/A' }}</strong>
              </div>
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Habitaciones / Habitantes</small>
                <strong>{{ paciente.infoSocioeconomica.numeroHabitaciones || '0' }} / {{ paciente.infoSocioeconomica.numeroHabitantes || '0' }}</strong>
              </div>
              <div class="col-md-12 mb-1">
                <small class="text-muted d-block">Servicios Disponibles</small>
                <strong>{{ paciente.infoSocioeconomica.serviciosDisponibles || 'N/A' }}</strong>
              </div>
            </div>
          </div>

          <div class="col-12 mb-4">
            <h6 class="text-muted text-uppercase mb-3 font-weight-bold">Economía y Empleo</h6>
            <div class="row bg-light p-3 rounded mx-0">
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Ocupación / Empleo</small>
                <strong>{{ paciente.infoSocioeconomica.ocupacionActual || 'N/A' }} ({{ paciente.infoSocioeconomica.tipoEmpleo || 'N/A' }})</strong>
              </div>
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Lugar y Tiempo en Empleo</small>
                <strong>{{ paciente.infoSocioeconomica.lugarTrabajo || 'N/A' }} ({{ paciente.infoSocioeconomica.tiempoEmpleado || 'N/A' }})</strong>
              </div>
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Condición Laboral</small>
                <strong>{{ paciente.infoSocioeconomica.condicionLaboral || 'N/A' }}</strong>
              </div>
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Ingreso Personal / Hogar</small>
                <strong>${{ paciente.infoSocioeconomica.ingresoMensual || '0' }} / ${{ paciente.infoSocioeconomica.ingresoMensualHogar || '0' }}</strong>
              </div>
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Gasto Mensual</small>
                <strong>${{ paciente.infoSocioeconomica.gastoMensual || '0' }}</strong>
              </div>
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Dependientes</small>
                <strong>{{ paciente.infoSocioeconomica.personasDependientes || '0' }} personas</strong>
              </div>
              <div class="col-md-12 mb-1">
                <small class="text-muted d-block">Apoyos Gubernamentales</small>
                <strong>{{ paciente.infoSocioeconomica.apoyosGubernamentales || 'Ninguno' }}</strong>
              </div>
            </div>
          </div>

          <div class="col-12 mb-2">
            <h6 class="text-muted text-uppercase mb-3 font-weight-bold">Educación, Salud y Movilidad</h6>
            <div class="row bg-light p-3 rounded mx-0">
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Grado de Estudios</small>
                <strong>{{ paciente.infoSocioeconomica.gradoMaximoEstudios || 'N/A' }} ({{ paciente.infoSocioeconomica.aniosEstudio || '0' }} años)</strong>
              </div>
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">¿Estudia Actualmente?</small>
                <strong>{{ paciente.infoSocioeconomica.estudia ? 'Sí' : 'No' }}</strong>
              </div>
              <div class="col-md-4 mb-3">
                <small class="text-muted d-block">Institución Médica / Afiliación</small>
                <strong>{{ paciente.infoSocioeconomica.institucionMedica || 'N/A' }} ({{ paciente.infoSocioeconomica.tipoAfiliacion || 'N/A' }})</strong>
              </div>
              <div class="col-md-4 mb-1">
                <small class="text-muted d-block">Transporte y Traslado</small>
                <strong>{{ paciente.infoSocioeconomica.medioTransporte || 'N/A' }} ({{ paciente.infoSocioeconomica.tiempoTraslado || '0' }} min)</strong>
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
// Agrega esto justo debajo de tu variable const tabActual = ref('datos');
const mostrarModalSocioeconomico = ref(false);
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