<template>
  <div class="tabla-module-container">
    <div class="card border-0 shadow-none bg-transparent">
      
      <div class="card-header bg-transparent pb-0">
        <ul class="nav nav-tabs border-0">
          <li class="nav-item">
            <a class="nav-link" 
               :class="{ 'active': tabActual === 'medicos' }"
               @click="tabActual = 'medicos'" style="cursor: pointer;">
               <i class="fas fa-file-medical-alt mr-1"></i> Historial Médico
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" 
               :class="{ 'active': tabActual === 'datos' }"
               @click="tabActual = 'datos'" style="cursor: pointer;">
               <i class="fas fa-user-circle mr-1"></i> Datos del Paciente
            </a>
          </li>
        </ul>
      </div>

      <div class="card-body p-4 bg-transparent">
        
        <div v-if="tabActual === 'datos'" class="fade-in">
          <div class="container-fluid p-0">
            
            <div class="card mb-4 section-card">
              <div class="card-header text-uppercase font-weight-bold">
                <i class="fas fa-id-card text-primary mr-2"></i> Datos Generales
              </div>
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
              <div class="card-header text-uppercase font-weight-bold">
                <i class="fas fa-map-marker-alt text-danger mr-2"></i> Dirección y Contacto
              </div>
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
                <span class="text-uppercase font-weight-bold"><i class="fas fa-briefcase text-success mr-2"></i> Resumen Socioeconómico</span>
                <button class="btn btn-sm btn-outline-secondary rounded-pill px-3" 
                        @click="mostrarModalSocioeconomico = true" 
                        :disabled="!paciente?.tieneInfoSocioeconomica">
                  <span v-if="paciente?.tieneInfoSocioeconomica"><i class="fas fa-eye mr-1"></i> Ver Expediente</span>
                  <span v-else><i class="fas fa-ban mr-1"></i> Sin datos</span>
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
                <h6 class="mb-0 text-primary font-weight-bold"><i class="fas fa-folder-open mr-2"></i> Expediente Socioeconómico Completo</h6>
                <button class="btn btn-sm btn-link text-danger text-decoration-none" @click="mostrarModalSocioeconomico = false">
                  Cerrar <i class="fas fa-times ml-1"></i>
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
                  <div class="card-header text-uppercase font-weight-bold"><i class="fas fa-home text-info mr-2"></i> Datos de Vivienda</div>
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
                  <div class="card-header text-uppercase font-weight-bold"><i class="fas fa-coins text-warning mr-2"></i> Economía y Empleo</div>
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
                  <div class="card-header text-uppercase font-weight-bold"><i class="fas fa-graduation-cap text-secondary mr-2"></i> Educación y Salud</div>
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
          <div class="container-fluid p-0">
            
            <div v-if="paciente?.datosMedicos">
              
              <div class="card mb-4 section-card border-light shadow-sm">
                <div class="card-header bg-white text-uppercase font-weight-bold d-flex justify-content-between align-items-center">
                  <div><i class="fas fa-heartbeat text-danger mr-2"></i> Signos y Biométricos</div>
                </div>
                <div class="card-body">
                  <div class="row">
                    <div class="col-md-3 col-sm-6 mb-3">
                      <small class="text-muted d-block text-uppercase"><i class="fas fa-ruler-vertical mr-1"></i> Altura</small>
                      <strong class="text-dark h5">{{ paciente.datosMedicos.biometricos?.altura || '-' }} cm</strong>
                    </div>
                    <div class="col-md-3 col-sm-6 mb-3">
                      <small class="text-muted d-block text-uppercase"><i class="fas fa-weight mr-1"></i> Peso</small>
                      <strong class="text-dark h5">{{ paciente.datosMedicos.biometricos?.peso || '-' }} kg</strong>
                    </div>
                    <div class="col-md-3 col-sm-6 mb-3">
                      <small class="text-muted d-block text-uppercase"><i class="fas fa-calculator mr-1"></i> IMC</small>
                      <span class="badge bg-dark text-white px-3 py-2" style="font-size: 0.9rem;">
                        {{ paciente.datosMedicos.biometricos?.imc || '-' }}
                      </span>
                    </div>
                    <div class="col-md-3 col-sm-6 mb-3">
                      <small class="text-muted d-block text-uppercase"><i class="fas fa-tint mr-1 text-danger"></i> Grupo Sanguíneo</small>
                      <span class="badge bg-danger text-white px-3 py-2" style="font-size: 0.9rem;">
                        {{ paciente.datosMedicos.biometricos?.grupoSanguineo || '' }}{{ paciente.datosMedicos.biometricos?.factorRh || '' }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card mb-4 section-card border-light shadow-sm">
                <div class="card-header bg-white text-uppercase font-weight-bold">
                  <i class="fas fa-notes-medical text-primary mr-2"></i> Antecedentes y Condiciones Clínicas
                </div>
                <div class="card-body">
                  <div class="row">
                    
                    <div class="col-md-6 mb-4">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-allergies text-warning mr-1"></i> Alergias</small>
                      <div v-if="paciente.datosMedicos.alergias?.tiene && paciente.datosMedicos.alergias?.detalles?.length > 0" class="p-3 bg-light rounded h-100 border">
                        <div v-for="(alergia, index) in paciente.datosMedicos.alergias.detalles" :key="index" class="mb-3 border-bottom pb-2">
                          <strong class="text-dark d-block mb-1">{{ alergia.tipoAlergia || 'ALERGIA' }} <span v-if="alergia.sustanciaAlergia" class="text-muted font-weight-normal">({{ alergia.sustanciaAlergia }})</span></strong>
                          <span class="text-muted small">Reacciones:</span> <span class="text-dark small">{{ alergia.reaccionesAlergia ? (Array.isArray(alergia.reaccionesAlergia) ? alergia.reaccionesAlergia.join(', ') : alergia.reaccionesAlergia) : 'N/A' }}</span><br>
                          <span class="text-muted small">Gravedad:</span> <span class="badge bg-warning text-dark ml-1">{{ alergia.gravedadAlergia }}</span><br>
                          <span class="text-muted small">Tratamiento:</span> <span class="text-dark small">{{ alergia.tratamientoAlergia }}</span><br>
                          <span class="text-muted small">Último Episodio:</span> <span class="text-dark small">{{ alergia.ultimoEpisodioAlergia || 'Desconocida' }}</span>
                        </div>
                      </div>
                      <div v-else class="p-3 bg-light rounded h-100 text-muted font-italic border">No reporta alergias.</div>
                    </div>

                    <div class="col-md-6 mb-4">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-lungs-virus text-info mr-1"></i> Enfermedades Crónicas</small>
                      <div v-if="paciente.datosMedicos.enfermedadesCronicas?.tiene && paciente.datosMedicos.enfermedadesCronicas?.detalles?.length > 0" class="p-3 bg-light rounded h-100 border">
                        <div v-for="(enf, index) in paciente.datosMedicos.enfermedadesCronicas.detalles" :key="index" class="mb-3 border-bottom pb-2">
                          <strong class="text-dark d-block mb-1">{{ enf.nombre || 'Enfermedad' }} <span class="badge bg-info text-white ml-1">{{ enf.estado }}</span></strong>
                          <span class="text-muted small">Diagnóstico:</span> <span class="text-dark small">{{ enf.diagnostico || 'N/A' }}</span> | 
                          <span class="text-muted small">Tipo:</span> <span class="text-dark small">{{ enf.tipo || 'N/A' }}</span><br>
                          <span class="text-muted small">Tratamiento:</span> <span class="text-dark small">{{ enf.tratamiento || 'N/A' }}</span><br>
                          <span v-if="enf.hospital" class="text-muted small">Hospital:</span> <span v-if="enf.hospital" class="text-dark small">{{ enf.hospital }}</span>
                        </div>
                      </div>
                      <div v-else class="p-3 bg-light rounded h-100 text-muted font-italic border">El paciente no reporta enfermedades crónicas.</div>
                    </div>

                    <div class="col-md-12 mb-4">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-procedures text-secondary mr-1"></i> Cirugías Previas</small>
                      <div v-if="paciente.datosMedicos.cirugiasPrevias?.tiene && paciente.datosMedicos.cirugiasPrevias?.detalles?.length > 0" class="p-3 bg-light rounded border">
                        <div class="row">
                          <div v-for="(cirugia, index) in paciente.datosMedicos.cirugiasPrevias.detalles" :key="index" class="col-md-6 mb-2">
                            <strong class="text-dark"><i class="fas fa-syringe text-muted mr-1"></i> {{ cirugia.nombre }}</strong> <span class="text-muted small">({{ cirugia.anio || 'Año desc.' }})</span><br>
                            <span class="text-muted small ml-4">Tipo:</span> <span class="text-dark small">{{ cirugia.tipo }}</span> | 
                            <span class="text-muted small">Complicaciones:</span> <span class="text-dark small">{{ cirugia.huboComplicaciones }}</span><br>
                            <span class="text-muted small ml-4">Motivo:</span> <span class="text-dark small">{{ cirugia.motivo || 'N/A' }}</span>
                          </div>
                        </div>
                      </div>
                      <div v-else class="p-3 bg-light rounded text-muted font-italic border">No se reportan cirugías previas.</div>
                    </div>
                    
                    <div class="col-md-6 mb-3">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-microscope text-secondary mr-1"></i> Personales Patológicos</small>
                      <div v-if="paciente.datosMedicos.antecedentesPatologicos?.tiene && paciente.datosMedicos.antecedentesPatologicos?.detalles?.length > 0" class="p-3 bg-light rounded h-100 border">
                        <ul class="pl-3 mb-0 text-dark">
                          <li v-for="(pat, index) in paciente.datosMedicos.antecedentesPatologicos.detalles" :key="index">
                            {{ pat.enfermedad }} <span  v-if="pat.anio">{{ pat.anio }}</span>
                          </li>
                        </ul>
                      </div>
                      <div v-else class="p-3 bg-light rounded h-100 text-muted font-italic border">Sin antecedentes patológicos previos.</div>
                    </div>

                    <div class="col-md-6 mb-3">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-users text-secondary mr-1"></i> Familiares Hereditarios</small>
                      <div v-if="paciente.datosMedicos.antecedentesFamiliares?.tiene && paciente.datosMedicos.antecedentesFamiliares?.detalles?.length > 0" class="p-3 bg-light rounded h-100 border">
                        <ul class="pl-3 mb-0 text-dark">
                          <li v-for="(fam, index) in paciente.datosMedicos.antecedentesFamiliares.detalles" :key="index">
                            {{ fam.enfermedad }} <span class="text-muted small font-italic">- {{ fam.parentezco }}</span>
                          </li>
                        </ul>
                      </div>
                      <div v-else class="p-3 bg-light rounded h-100 text-muted font-italic border">Sin antecedentes familiares de importancia.</div>
                    </div>

                  </div>
                </div>
              </div>

              <div class="card mb-4 section-card border-light shadow-sm">
                <div class="card-header bg-white text-uppercase font-weight-bold">
                  <i class="fas fa-child text-secondary mr-2"></i> Hábitos y Consumo
                </div>
                <div class="card-body">
                  <div class="row text-center text-md-left">
                    <div class="col-6 col-md-3 mb-3 border-right">
                      <small class="text-muted d-block mb-1"><i class="fas fa-smoking mr-1"></i> Tabaco</small>
                      <strong :class="paciente.datosMedicos.habitos?.tabaco !== 'No fuma' ? 'text-danger' : 'text-dark'">
                        {{ paciente.datosMedicos.habitos?.tabaco || 'No fuma' }}
                      </strong>
                      <small class="text-muted d-block mt-1">Frecuencia: {{ paciente.datosMedicos.habitos?.tabacoFrecuencia || 'N/A' }}</small>
                    </div>
                    <div class="col-6 col-md-3 mb-3 border-right">
                      <small class="text-muted d-block mb-1"><i class="fas fa-wine-glass-alt mr-1"></i> Alcohol</small>
                      <strong class="text-dark">{{ paciente.datosMedicos.habitos?.alcohol || 'No' }}</strong>
                      <small class="text-muted d-block mt-1">Frecuencia: {{ paciente.datosMedicos.habitos?.alcoholFrecuencia || 'N/A' }}</small>
                    </div>
                    <div class="col-6 col-md-3 mb-3 border-right">
                      <small class="text-muted d-block mb-1"><i class="fas fa-cannabis mr-1"></i> Drogas</small>
                      <strong :class="paciente.datosMedicos.habitos?.drogas !== 'No' ? 'text-danger' : 'text-dark'">
                        {{ paciente.datosMedicos.habitos?.drogas || 'No' }}
                      </strong>
                      <small class="text-muted d-block mt-1">Tipo: {{ paciente.datosMedicos.habitos?.drogasTipo || 'N/A' }}</small>
                    </div>
                    <div class="col-6 col-md-3 mb-3">
                      <small class="text-muted d-block mb-1"><i class="fas fa-shield-virus mr-1"></i> Vacunación</small>
                      <strong class="text-dark">{{ paciente.datosMedicos.habitos?.esquemaVacunacion || 'Desconoce' }}</strong>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card mb-4 section-card border-light shadow-sm">
                <div class="card-header bg-white text-uppercase font-weight-bold">
                  <i class="fas fa-prescription-bottle-alt text-success mr-2"></i> Tratamiento y Observaciones
                </div>
                <div class="card-body">
                  <div class="row">
                    
                    <div class="col-md-6 mb-3">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-pills text-success mr-1"></i> Medicamentos Actuales</small>
                      <div v-if="paciente.datosMedicos.medicamentosActuales?.tiene && paciente.datosMedicos.medicamentosActuales?.detalles?.length > 0" class="p-3 bg-light rounded h-100 border">
                        <div v-for="(med, index) in paciente.datosMedicos.medicamentosActuales.detalles" :key="index" class="mb-2">
                          <strong class="text-dark d-block"><i class="fas fa-tablets text-muted mr-1"></i> {{ med.nombre }}</strong>
                          <span class="text-muted small ml-4">Motivo:</span> <span class="text-dark small">{{ med.motivo || 'N/A' }}</span> | 
                          <span class="text-muted small">Frecuencia:</span> <span class="text-dark small">{{ med.frecuencia || 'N/A' }}</span>
                        </div>
                      </div>
                      <div v-else class="p-3 bg-light rounded h-100 text-muted font-italic border">No toma medicamentos actualmente.</div>
                    </div>

                    <div class="col-md-6 mb-3">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-file-signature text-primary mr-1"></i> Notas del Médico</small>
                      <div class="p-3 bg-light rounded h-100 border">
                        <p class="text-dark mb-0" style="white-space: pre-wrap; line-height: 1.6;">
                          <em v-if="!paciente.datosMedicos.observacionesGenerales" class="text-muted">Sin observaciones registradas en este expediente.</em>
                          <span v-else>{{ paciente.datosMedicos.observacionesGenerales }}</span>
                        </p>
                      </div>
                    </div>

                  </div>
                </div>
              </div>

            </div>
            
            <div v-else class="text-center py-5">
              <i class="fas fa-folder-open text-muted mb-3" style="font-size: 3rem;"></i>
              <p class="text-muted h5">Cargando datos médicos o no hay información registrada.</p>
            </div>
            
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