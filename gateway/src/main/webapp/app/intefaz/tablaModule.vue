<template>
  <div class="tabla-module-container">
    <div class="card border-0 shadow-none bg-transparent">
      
      <div class="card-header bg-transparent pb-0">
        <ul class="nav nav-tabs border-0">
          <li class="nav-item">
            <a class="nav-link" 
               :class="{ 'active': tabActual === 'medicos' }"
               @click="tabActual = 'medicos'">
               Historial Médico
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" 
               :class="{ 'active': tabActual === 'datos' }"
               @click="tabActual = 'datos'">
               Datos del Paciente
            </a>
          </li>
        </ul>
      </div>

      <div class="card-body p-4 bg-transparent">
        
        <div v-if="tabActual === 'datos'" class="fade-in">
          <div class="container-fluid p-0">
            
            <div class="card mb-4 section-card">
              <div class="card-header text-uppercase">Datos Generales</div>
              <div class="card-body">
                <div class="row">
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">CURP</small>
                    <strong class="text-dark">{{ paciente?.curp || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Nombre(s)</small>
                    <strong class="text-dark">{{ paciente?.nombre || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Primer Apellido</small>
                    <strong class="text-dark">{{ paciente?.apellidoPaterno || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Segundo Apellido</small>
                    <strong class="text-dark">{{ paciente?.apellidoMaterno || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Fecha de Nacimiento</small>
                    <strong class="text-dark">{{ paciente?.fechaNacimiento || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Edad</small>
                    <strong class="text-dark">{{ calcularEdad(paciente?.fechaNacimiento) || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Sexo / Género</small>
                    <strong class="text-dark">{{ paciente?.sexo || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Nacionalidad</small>
                    <strong class="text-dark">{{ paciente?.nacionalidad || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Estado Civil</small>
                    <strong class="text-dark">{{ paciente?.estadoCivil || 'N/A' }}</strong>
                  </div>
                </div>
              </div>
            </div>

            <div class="card mb-4 section-card">
              <div class="card-header text-uppercase">Dirección y Contacto</div>
              <div class="card-body">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <small class="text-muted d-block">Calle y Número</small>
                    <strong class="text-dark">
                      {{ paciente?.direccion?.vialidad || '' }} 
                      {{ paciente?.direccion?.nombreVialidad || 'No registrada' }} 
                      {{ paciente?.direccion?.numExterior || '' }} 
                      {{ paciente?.direccion?.numInterior ? 'Int. ' + paciente?.direccion?.numInterior : '' }}
                    </strong>
                  </div>
                  <div class="col-md-3 mb-3">
                    <small class="text-muted d-block">Colonia</small>
                    <strong class="text-dark">{{ paciente?.direccion?.codigoPostalInfo?.asentamiento || 'No registrada' }}</strong>
                  </div>
                  <div class="col-md-3 mb-3">
                    <small class="text-muted d-block">Código Postal</small>
                    <strong class="text-dark">{{ paciente?.direccion?.codigoPostalInfo?.codigo || 'No registrada' }}</strong>
                  </div>
                  <div class="col-md-4 mb-3">
                    <small class="text-muted d-block">Alcaldía / Municipio</small>
                    <strong class="text-dark">{{ paciente?.direccion?.codigoPostalInfo?.municipio || 'No registrada' }}</strong>
                  </div>
                  <div class="col-md-4 mb-3">
                    <small class="text-muted d-block">Estado</small>
                    <strong class="text-dark">{{ paciente?.direccion?.codigoPostalInfo?.estado || 'No registrada' }}</strong>
                  </div>
                  <div class="col-md-4 mb-3">
                    <small class="text-muted d-block">Teléfono</small>
                    <strong class="text-dark">{{ paciente?.direccion?.telefono || 'No registrado' }}</strong>
                  </div>
                </div>
              </div>
            </div>

            <div class="card mb-4 section-card">
              <div class="card-header d-flex justify-content-between align-items-center">
                <span class="text-uppercase">Resumen Socioeconómico</span>
                <button class="btn btn-sm btn-outline-secondary rounded-pill px-3" 
                        @click="mostrarModalSocioeconomico = true" 
                        :disabled="!paciente?.tieneInfoSocioeconomica">
                  <span v-if="paciente?.tieneInfoSocioeconomica">Ver Expediente</span>
                  <span v-else>Sin datos</span>
                </button>
              </div>
              <div class="card-body">
                <div v-if="paciente?.tieneInfoSocioeconomica" class="row">
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Ocupación Actual</small>
                    <strong class="text-dark">{{ paciente.resumenSocioeconomico?.ocupacion || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Grado de Estudios</small>
                    <strong class="text-dark">{{ paciente.resumenSocioeconomico?.gradoEstudios || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Ingreso Mensual</small>
                    <strong class="text-dark">
                      {{ paciente.resumenSocioeconomico?.ingresoMensual !== 'N/A' ? '$' + paciente.resumenSocioeconomico?.ingresoMensual : 'N/A' }}
                    </strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Afiliación Médica</small>
                    <strong class="text-dark">{{ paciente.resumenSocioeconomico?.afiliacion || 'N/A' }}</strong>
                  </div>
                </div>
                <p v-else class="text-muted mb-0 font-italic">El paciente no cuenta con un estudio socioeconómico registrado.</p>
              </div>
            </div>

            <div v-if="mostrarModalSocioeconomico" class="card mb-4 section-card border-primary">
              <div class="card-header d-flex justify-content-between align-items-center bg-light">
                <h6 class="mb-0 text-primary font-weight-bold">Expediente Socioeconómico Completo</h6>
                <button class="btn btn-sm btn-link text-danger text-decoration-none" @click="mostrarModalSocioeconomico = false">
                  Cerrar ✕
                </button>
              </div>
              <div class="card-body p-0">
                <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                  <table class="table table-sm table-hover mb-0">
                    <thead class="bg-light sticky-top">
                      <tr>
                        <th class="py-2 px-3 text-muted text-uppercase" style="font-size: 0.8rem;">Pregunta</th>
                        <th class="py-2 px-3 text-muted text-uppercase" style="font-size: 0.8rem;">Respuesta</th>
                        <th class="py-2 px-3 text-muted text-uppercase" style="font-size: 0.8rem;">Detalles</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="item in paciente?.expedienteCompleto" :key="item.id">
                        <td class="font-weight-bold text-dark px-3 align-middle" style="font-size: 0.9rem;">{{ item.pregunta }}</td>
                        <td class="px-3 align-middle">
                          <span class="badge bg-secondary text-white">{{ item.respuesta || 'N/A' }}</span>
                        </td>
                        <td class="text-muted px-3 align-middle" style="font-size: 0.9rem;">{{ item.respuestaAbierta || '-' }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

            <div v-if="paciente?.infoSocioeconomica" class="row">
              <div class="col-md-12 mb-3">
                <div class="card section-card">
                  <div class="card-header text-uppercase">Datos de Vivienda</div>
                  <div class="card-body">
                    <div class="row">
                      <div class="col-md-4 mb-3">
                        <small class="text-muted d-block">Tipo de Vivienda</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.tipoVivienda || 'N/A' }}</strong>
                      </div>
                      <div class="col-md-4 mb-3">
                        <small class="text-muted d-block">Material</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.materialVivienda || 'N/A' }}</strong>
                      </div>
                      <div class="col-md-4 mb-3">
                        <small class="text-muted d-block">Habitaciones / Habitantes</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.numeroHabitaciones || '0' }} / {{ paciente?.infoSocioeconomica?.numeroHabitantes || '0' }}</strong>
                      </div>
                      <div class="col-md-12">
                        <small class="text-muted d-block">Servicios Disponibles</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.serviciosDisponibles || 'N/A' }}</strong>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-md-6 mb-3">
                <div class="card h-100 section-card">
                  <div class="card-header text-uppercase">Economía y Empleo</div>
                  <div class="card-body">
                    <small class="text-muted d-block">Ocupación / Empleo</small>
                    <strong class="text-dark d-block mb-3">{{ paciente?.infoSocioeconomica?.ocupacionActual || 'N/A' }}</strong>
                    <small class="text-muted d-block">Ingreso Personal / Hogar</small>
                    <strong class="text-dark d-block mb-3">${{ paciente?.infoSocioeconomica?.ingresoMensual || '0' }} / ${{ paciente?.infoSocioeconomica?.ingresoMensualHogar || '0' }}</strong>
                    <small class="text-muted d-block">Dependientes</small>
                    <strong class="text-dark d-block">{{ paciente?.infoSocioeconomica?.personasDependientes || '0' }} personas</strong>
                  </div>
                </div>
              </div>

              <div class="col-md-6 mb-3">
                <div class="card h-100 section-card">
                  <div class="card-header text-uppercase">Educación y Salud</div>
                  <div class="card-body">
                    <small class="text-muted d-block">Grado de Estudios</small>
                    <strong class="text-dark d-block mb-3">{{ paciente?.infoSocioeconomica?.gradoMaximoEstudios || 'N/A' }}</strong>
                    <small class="text-muted d-block">Institución Médica</small>
                    <strong class="text-dark d-block mb-3">{{ paciente?.infoSocioeconomica?.institucionMedica || 'N/A' }}</strong>
                    <small class="text-muted d-block">Transporte y Traslado</small>
                    <strong class="text-dark d-block">{{ paciente?.infoSocioeconomica?.medioTransporte || 'N/A' }} ({{ paciente?.infoSocioeconomica?.tiempoTraslado || '0' }} min)</strong>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
            
           <div v-if="tabActual === 'medicos'" class="fade-in">
          <div v-if="paciente?.datosMedicos" class="row">
            
            <div class="col-12">
              <div class="callout-card">
                <div class="d-flex align-items-start">
                  <i class="fas fa-stethoscope mr-3 mt-1 callout-icon"></i>
                  <div class="w-100">
                    <span class="callout-title">Signos y Biométricos</span>
                    <div class="row align-items-center text-center text-md-left">
                      <div class="col-md-3 col-sm-6 mb-2 mb-md-0">
                        <small class="text-muted d-block">Altura</small>
                        <strong class="text-dark">{{ paciente.datosMedicos.biometricos?.altura || '-' }} cm</strong>
                      </div>
                      <div class="col-md-3 col-sm-6 mb-2 mb-md-0">
                        <small class="text-muted d-block">Peso</small>
                        <strong class="text-dark">{{ paciente.datosMedicos.biometricos?.peso || '-' }} kg</strong>
                      </div>
                      <div class="col-md-3 col-sm-6 mb-2 mb-md-0">
                        <small class="text-muted d-block">IMC</small>
                        <span class="badge bg-secondary text-white">{{ paciente.datosMedicos.biometricos?.imc || '-' }}</span>
                      </div>
                      <div class="col-md-3 col-sm-6 mb-2 mb-md-0">
                        <small class="text-muted d-block">Grupo Sanguíneo</small>
                        <span class="badge bg-danger text-white">{{ paciente.datosMedicos.biometricos?.grupoSanguineo || '' }}{{ paciente.datosMedicos.biometricos?.factorRh || '' }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-6">
              
              <div class="callout-card">
                <div class="d-flex align-items-start">
                  <i class="fas fa-allergies mr-3 mt-1 callout-icon"></i>
                  <div class="w-100">
                    <span class="callout-title">Alergias</span>
                    <div v-if="paciente.datosMedicos.alergias.tiene && paciente.datosMedicos.alergias.detalles.length > 0">
                      <div v-for="(alergia, index) in paciente.datosMedicos.alergias.detalles" :key="index" class="mb-3">
                        <strong class="text-dark d-block mb-1">{{ alergia.tipoAlergia || 'ALERGIA' }}</strong>
                        <div style="font-size: 0.85rem; line-height: 1.6;">
                          <span class="text-muted">Reacciones:</span> <span class="text-dark">{{ alergia.reaccionesAlergia ? alergia.reaccionesAlergia.join(', ') : 'N/A' }}</span><br>
                          <span class="text-muted">Gravedad:</span> <span class="badge bg-warning text-dark ml-1">{{ alergia.gravedadAlergia }}</span><br>
                          <span class="text-muted">Tratamiento:</span> <span class="text-dark">{{ alergia.tratamientoAlergia }}</span><br>
                          <span class="text-muted">Último Episodio:</span> <span class="text-dark">{{ alergia.ultimoEpisodioAlergia || 'Desconocida' }}</span>
                        </div>
                      </div>
                    </div>
                    <div v-else class="text-muted font-italic" style="font-size: 0.9rem;">No reporta alergias.</div>
                  </div>
                </div>
              </div>

              <div class="callout-card">
                <div class="d-flex align-items-start">
                  <i class="fas fa-procedures mr-3 mt-1 callout-icon"></i>
                  <div class="w-100">
                    <span class="callout-title">Cirugías Previas</span>
                    <p v-if="paciente.datosMedicos.cirugiasPrevias.tiene" class="text-dark mb-0" style="font-size: 0.9rem;">Sí reporta cirugías previas.</p>
                    <p v-else class="text-muted font-italic mb-0" style="font-size: 0.9rem;">No se reportan cirugías previas.</p>
                  </div>
                </div>
              </div>

              <div class="callout-card">
                <div class="d-flex align-items-start">
                  <i class="fas fa-users mr-3 mt-1 callout-icon"></i>
                  <div class="w-100">
                    <span class="callout-title">Antecedentes Familiares</span>
                    <p v-if="paciente.datosMedicos.antecedentesFamiliares.tiene" class="text-dark mb-0" style="font-size: 0.9rem;">Sí reporta antecedentes familiares.</p>
                    <p v-else class="text-muted font-italic mb-0" style="font-size: 0.9rem;">Sin antecedentes familiares de importancia.</p>
                  </div>
                </div>
              </div>

              <div class="callout-card">
                <div class="d-flex align-items-start">
                  <i class="fas fa-smoking mr-3 mt-1 callout-icon"></i>
                  <div class="w-100">
                    <span class="callout-title">Hábitos y Consumo</span>
                    <div class="row text-left mb-2 mx-0">
                      <div class="col-4 px-1">
                        <small class="text-muted d-block mb-1">Tabaco</small>
                        <strong :class="paciente.datosMedicos.habitos?.tabaco === 'Fuma actualmente' ? 'text-danger' : 'text-dark'" style="font-size: 0.85rem; display: block;">
                          {{ paciente.datosMedicos.habitos?.tabaco || 'No fuma' }}
                        </strong>
                        <small class="text-muted" style="font-size: 0.7rem;">Frec.: {{ paciente.datosMedicos.habitos?.tabacoFrecuencia || '-' }}</small>
                      </div>
                      <div class="col-4 px-1">
                        <small class="text-muted d-block mb-1">Alcohol</small>
                        <strong class="text-dark" style="font-size: 0.85rem; display: block;">{{ paciente.datosMedicos.habitos?.alcohol || 'No' }}</strong>
                        <small class="text-muted" style="font-size: 0.7rem;">Frec.: {{ paciente.datosMedicos.habitos?.alcoholFrecuencia || '-' }}</small>
                      </div>
                      <div class="col-4 px-1">
                        <small class="text-muted d-block mb-1">Drogas</small>
                        <strong :class="paciente.datosMedicos.habitos?.drogas === 'Sí' ? 'text-danger' : 'text-dark'" style="font-size: 0.85rem; display: block;">
                          {{ paciente.datosMedicos.habitos?.drogas || 'No' }}
                        </strong>
                        <small class="text-muted d-block" style="font-size: 0.7rem;">Tipo: {{ paciente.datosMedicos.habitos?.drogasTipo || 'N/A' }}</small>
                      </div>
                    </div>
                    <div class="row text-left mx-0 pt-2 border-top">
                      <div class="col-6 px-1 pt-2">
                        <small class="text-muted d-block mb-1">Vacunación</small>
                        <span class="text-dark" style="font-size: 0.8rem;">Esq.: {{ paciente.datosMedicos.habitos?.esquemaVacunacion || 'Desconoce' }}</span>
                      </div>
                      <div class="col-6 px-1 pt-2">
                        <small class="text-muted d-block mb-1">Discapacidad</small>
                        <strong class="text-dark" style="font-size: 0.85rem;">
                          {{ paciente.datosMedicos.habitos?.tieneDiscapacidad || 'No' }}
                        </strong><br>
                        <small class="text-dark" style="font-size: 0.8rem;">Tipo: {{ paciente.datosMedicos.habitos?.tipoDiscapacidad || '-' }}</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

            </div>

            <div class="col-md-6">
              
              <div class="callout-card">
                <div class="d-flex align-items-start">
                  <i class="fas fa-heartbeat mr-3 mt-1 callout-icon"></i>
                  <div class="w-100">
                    <span class="callout-title">Enfermedades Crónicas</span>
                    <div v-if="paciente.datosMedicos.enfermedadesCronicas.tiene && paciente.datosMedicos.enfermedadesCronicas.detalles.length > 0">
                      <div v-for="(enf, index) in paciente.datosMedicos.enfermedadesCronicas.detalles" :key="index">
                        <strong class="text-dark d-block" style="font-size: 0.9rem;">• {{ enf.nombreEnfermedad || 'Enfermedad' }}</strong>
                      </div>
                    </div>
                    <div v-else class="text-muted font-italic" style="font-size: 0.9rem;">El paciente no reporta enfermedades crónicas.</div>
                  </div>
                </div>
              </div>

              <div class="callout-card">
                <div class="d-flex align-items-start">
                  <i class="fas fa-pills mr-3 mt-1 callout-icon"></i>
                  <div class="w-100">
                    <span class="callout-title">Medicamentos Actuales</span>
                    <div v-if="paciente.datosMedicos.medicamentosActuales.tiene && paciente.datosMedicos.medicamentosActuales.detalles.length > 0">
                      <div v-for="(med, index) in paciente.datosMedicos.medicamentosActuales.detalles" :key="index">
                        <strong class="text-dark d-block" style="font-size: 0.9rem;">• {{ med.nombre || 'Medicamento' }}</strong>
                      </div>
                    </div>
                    <div v-else class="text-muted font-italic" style="font-size: 0.9rem;">No toma medicamentos actualmente.</div>
                  </div>
                </div>
              </div>

              <div class="callout-card">
                <div class="d-flex align-items-start">
                  <i class="fas fa-notes-medical mr-3 mt-1 callout-icon"></i>
                  <div class="w-100">
                    <span class="callout-title">Antecedentes Patológicos</span>
                    <p v-if="paciente.datosMedicos.antecedentesPatologicos.tiene" class="text-dark mb-0" style="font-size: 0.9rem;">Sí reporta antecedentes patológicos.</p>
                    <p v-else class="text-muted font-italic mb-0" style="font-size: 0.9rem;">Sin antecedentes patológicos previos.</p>
                  </div>
                </div>
              </div>

              <div class="callout-card">
                <div class="d-flex align-items-start">
                  <svg class="mr-3 mt-1 callout-icon" style="fill: #611232; width: 22px; height: 22px;" viewBox="0 0 24 24">
                     <path d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 1.99 2H18c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z"/>
                  </svg>
                  <div class="w-100">
                    <span class="callout-title">Observaciones Médicas Generales</span>
                    <p class="text-dark mb-0" style="white-space: pre-wrap; font-size: 0.9rem; line-height: 1.6;">
                      <em v-if="!paciente.datosMedicos.observacionesGenerales" class="text-muted">Sin observaciones registradas.</em>
                      <span v-else>{{ paciente.datosMedicos.observacionesGenerales }}</span>
                    </p>
                  </div>
                </div>
              </div>

            </div>

          </div>
          <div v-else class="text-center py-5">
             <p class="text-muted">Cargando datos médicos o no hay información registrada.</p>
          </div>
        </div>

            

          </div>
        </div>

      </div> 
</template>

<script setup lang="ts">
import { ref } from 'vue'; 

// 0. Variables reactivas
const tabActual = ref('datos');
const mostrarModalSocioeconomico = ref(false);

// 1. Props (protegidos contra vacíos)
const props = defineProps({
  paciente: {
    type: Object,
    required: false,
    default: () => ({})
  }
});

// 2. Función segura para calcular la edad
const calcularEdad = (fechaNacimiento: any) => {
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