<template>
  <div class="tabla-module-container">
    <div class="card border-0 shadow-none bg-transparent">
      
      <div class="card-header bg-transparent pb-0 border-bottom-0">
        <ul class="nav custom-tabs flex-nowrap overflow-auto">
          <li class="nav-item">
            <a class="nav-link text-nowrap" 
               :class="{ 'active': tabActual === 'datos' }"
               @click="tabActual = 'datos'" style="cursor: pointer;">
               <i class="fas fa-user-circle mr-2"></i> Datos del Paciente
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-nowrap" 
               :class="{ 'active': tabActual === 'medicos' }"
               @click="tabActual = 'medicos'" style="cursor: pointer;">
               <i class="fas fa-file-medical-alt mr-2"></i> Historial Médico
            </a>
          </li>
        </ul>
      </div>

      <div class="card-body p-2 p-md-4 bg-transparent mt-3">
        
        <div v-if="tabActual === 'datos'" class="fade-in">
          <div class="container-fluid p-0">
            
            <div class="card mb-4 section-card shadow-sm border-0 rounded-lg custom-card">
              <div class="card-header bg-white border-bottom-0 pt-4 pb-0 text-uppercase font-weight-bold text-theme">
                <i class="fas fa-id-card mr-2 fa-lg"></i> Datos Generales
              </div>
              <div class="card-body">
                <div class="row">
                  <div class="col-12 col-sm-6 col-md-3 mb-4">
                    <small class="text-muted d-block mb-1"><i class="fas fa-fingerprint text-theme-light mr-1"></i> CURP</small>
                    <strong class="text-dark">{{ paciente?.curp || 'N/A' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-4">
                    <small class="text-muted d-block mb-1"><i class="fas fa-user text-theme-light mr-1"></i> Nombre(s)</small>
                    <strong class="text-dark">{{ paciente?.nombre || 'N/A' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-4">
                    <small class="text-muted d-block mb-1"><i class="fas fa-user-tag text-theme-light mr-1"></i> Primer Apellido</small>
                    <strong class="text-dark">{{ paciente?.apellidoPaterno || 'N/A' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-4">
                    <small class="text-muted d-block mb-1"><i class="fas fa-users text-theme-light mr-1"></i> Segundo Apellido</small>
                    <strong class="text-dark">{{ paciente?.apellidoMaterno || 'N/A' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-calendar-day text-theme-light mr-1"></i> Fecha de Nacimiento</small>
                    <strong class="text-dark">{{ paciente?.fechaNacimiento || 'N/A' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-birthday-cake text-theme-light mr-1"></i> Edad</small>
                    <strong class="text-dark badge bg-light text-dark px-2 py-1 border">{{ calcularEdad(paciente?.fechaNacimiento) || 'N/A' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-venus-mars text-theme-light mr-1"></i> Sexo / Género</small>
                    <strong class="text-dark">{{ paciente?.sexo || 'N/A' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-globe-americas text-theme-light mr-1"></i> Nacionalidad / Edo Civil</small>
                    <strong class="text-dark"><i class="fas fa-flag text-muted mr-1"></i>{{ paciente?.nacionalidad || 'N/A' }} <span class="text-muted mx-1">|</span> <i class="fas fa-ring text-muted mr-1"></i>{{ paciente?.estadoCivil || 'N/A' }}</strong>
                  </div>
                </div>
              </div>
            </div>

            <div class="card mb-4 section-card shadow-sm border-0 rounded-lg custom-card">
              <div class="card-header bg-white border-bottom-0 pt-4 pb-0 text-uppercase font-weight-bold text-theme">
                <i class="fas fa-map-marked-alt mr-2 fa-lg"></i> Dirección y Contacto
              </div>
              <div class="card-body">
                <div class="row">
                  <div class="col-12 col-md-6 mb-4">
                    <small class="text-muted d-block mb-1"><i class="fas fa-route text-theme-light mr-1"></i> Calle y Número</small>
                    <strong class="text-dark">
                      {{ paciente?.direccion?.vialidad || '' }} 
                      {{ paciente?.direccion?.nombreVialidad || 'No registrada' }} 
                      {{ paciente?.direccion?.numExterior || '' }} 
                      {{ paciente?.direccion?.numInterior ? 'Int. ' + paciente?.direccion?.numInterior : '' }}
                    </strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-4">
                    <small class="text-muted d-block mb-1"><i class="fas fa-map-signs text-theme-light mr-1"></i> Colonia</small>
                    <strong class="text-dark">{{ paciente?.direccion?.codigoPostalInfo?.asentamiento || 'No registrada' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-4">
                    <small class="text-muted d-block mb-1"><i class="fas fa-mail-bulk text-theme-light mr-1"></i> Código Postal</small>
                    <strong class="text-dark">{{ paciente?.direccion?.codigoPostalInfo?.codigo || 'No registrada' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-4 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-city text-theme-light mr-1"></i> Alcaldía / Municipio</small>
                    <strong class="text-dark">{{ paciente?.direccion?.codigoPostalInfo?.municipio || 'No registrada' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-4 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-map text-theme-light mr-1"></i> Estado</small>
                    <strong class="text-dark">{{ paciente?.direccion?.codigoPostalInfo?.estado || 'No registrada' }}</strong>
                  </div>
                  <div class="col-12 col-md-4 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-phone-alt text-theme-light mr-1"></i> Teléfono</small>
                    <strong class="text-dark">{{ paciente?.direccion?.telefono || 'No registrado' }}</strong>
                  </div>
                </div>
              </div>
            </div>

            <div class="card mb-4 section-card shadow-sm border-0 rounded-lg custom-card">
              <div class="card-header bg-white border-bottom-0 pt-4 pb-0 d-flex flex-column flex-sm-row justify-content-between align-items-start align-items-sm-center">
                <span class="text-uppercase font-weight-bold text-theme mb-2 mb-sm-0"><i class="fas fa-briefcase mr-2 fa-lg"></i> Resumen Socioeconómico</span>
                <button class="btn btn-sm btn-theme rounded-pill px-3 shadow-sm" 
                        @click="mostrarModalSocioeconomico = true" 
                        :disabled="!paciente?.tieneInfoSocioeconomica">
                  <span v-if="paciente?.tieneInfoSocioeconomica"><i class="fas fa-eye mr-1"></i> Ver Expediente</span>
                  <span v-else><i class="fas fa-ban mr-1"></i> Sin datos</span>
                </button>
              </div>
              <div class="card-body">
                <div v-if="paciente?.tieneInfoSocioeconomica" class="row">
                  <div class="col-12 col-sm-6 col-md-3 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-user-tie text-theme-light mr-1"></i> Ocupación Actual</small>
                    <strong class="text-dark">{{ paciente.resumenSocioeconomico?.ocupacion || 'N/A' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-user-graduate text-theme-light mr-1"></i> Grado de Estudios</small>
                    <strong class="text-dark">{{ paciente.resumenSocioeconomico?.gradoEstudios || 'N/A' }}</strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-money-bill-wave text-success mr-1"></i> Ingreso Mensual</small>
                    <strong class="text-dark">
                      {{ paciente.resumenSocioeconomico?.ingresoMensual !== 'N/A' ? '$' + paciente.resumenSocioeconomico?.ingresoMensual : 'N/A' }}
                    </strong>
                  </div>
                  <div class="col-12 col-sm-6 col-md-3 mb-3">
                    <small class="text-muted d-block mb-1"><i class="fas fa-clinic-medical text-theme-light mr-1"></i> Afiliación Médica</small>
                    <strong class="text-dark">{{ paciente.resumenSocioeconomico?.afiliacion || 'N/A' }}</strong>
                  </div>
                </div>
                <p v-else class="text-muted mb-0 font-italic"><i class="fas fa-info-circle mr-1"></i> El paciente no cuenta con un estudio socioeconómico registrado.</p>
              </div>
            </div>

            <div v-if="paciente?.infoSocioeconomica" class="row">
              <div class="col-12 mb-3">
                <div class="card section-card shadow-sm border-0 rounded-lg h-100">
                  <div class="card-header bg-white border-bottom-0 pt-4 pb-0 text-uppercase font-weight-bold text-theme">
                    <i class="fas fa-home mr-2 fa-lg"></i> Datos de Vivienda
                  </div>
                  <div class="card-body">
                    <div class="row">
                      <div class="col-12 col-sm-4 mb-3">
                        <small class="text-muted d-block mb-1"><i class="fas fa-building text-theme-light mr-1"></i> Tipo de Vivienda</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.tipoVivienda || 'N/A' }}</strong>
                      </div>
                      <div class="col-12 col-sm-4 mb-3">
                        <small class="text-muted d-block mb-1"><i class="fas fa-hammer text-theme-light mr-1"></i> Material</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.materialVivienda || 'N/A' }}</strong>
                      </div>
                      <div class="col-12 col-sm-4 mb-3">
                        <small class="text-muted d-block mb-1"><i class="fas fa-bed text-theme-light mr-1"></i> Cuartos / Habitantes</small>
                        <strong class="text-dark"><i class="fas fa-door-open text-muted mr-1"></i>{{ paciente?.infoSocioeconomica?.numeroHabitaciones || '0' }} / <i class="fas fa-users text-muted mx-1"></i>{{ paciente?.infoSocioeconomica?.numeroHabitantes || '0' }}</strong>
                      </div>
                      <div class="col-12">
                        <small class="text-muted d-block mb-1"><i class="fas fa-faucet text-theme-light mr-1"></i> Servicios Disponibles</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.serviciosDisponibles || 'N/A' }}</strong>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-12 col-md-6 mb-3">
                <div class="card h-100 section-card shadow-sm border-0 rounded-lg">
                  <div class="card-header bg-white border-bottom-0 pt-4 pb-0 text-uppercase font-weight-bold text-theme">
                    <i class="fas fa-coins mr-2 fa-lg"></i> Economía y Empleo
                  </div>
                  <div class="card-body">
                    <small class="text-muted d-block mb-1"><i class="fas fa-id-badge text-theme-light mr-1"></i> Ocupación / Empleo</small>
                    <strong class="text-dark d-block mb-3">{{ paciente?.infoSocioeconomica?.ocupacionActual || 'N/A' }}</strong>
                    
                    <small class="text-muted d-block mb-1"><i class="fas fa-wallet text-success mr-1"></i> Ingreso Personal / Hogar</small>
                    <strong class="text-dark d-block mb-3"><i class="fas fa-user text-muted mr-1"></i>${{ paciente?.infoSocioeconomica?.ingresoMensual || '0' }} <span class="text-muted mx-1">|</span> <i class="fas fa-home text-muted mr-1"></i>${{ paciente?.infoSocioeconomica?.ingresoMensualHogar || '0' }}</strong>
                    
                    <small class="text-muted d-block mb-1"><i class="fas fa-child text-theme-light mr-1"></i> Dependientes</small>
                    <strong class="text-dark d-block">{{ paciente?.infoSocioeconomica?.personasDependientes || '0' }} personas</strong>
                  </div>
                </div>
              </div>

              <div class="col-12 col-md-6 mb-3">
                <div class="card h-100 section-card shadow-sm border-0 rounded-lg">
                  <div class="card-header bg-white border-bottom-0 pt-4 pb-0 text-uppercase font-weight-bold text-theme">
                    <i class="fas fa-graduation-cap mr-2 fa-lg"></i> Educación y Salud
                  </div>
                  <div class="card-body">
                    <small class="text-muted d-block mb-1"><i class="fas fa-book text-theme-light mr-1"></i> Grado de Estudios</small>
                    <strong class="text-dark d-block mb-3">{{ paciente?.infoSocioeconomica?.gradoMaximoEstudios || 'N/A' }}</strong>
                    
                    <small class="text-muted d-block mb-1"><i class="fas fa-hospital text-theme-light mr-1"></i> Institución Médica</small>
                    <strong class="text-dark d-block mb-3">{{ paciente?.infoSocioeconomica?.institucionMedica || 'N/A' }}</strong>
                    
                    <small class="text-muted d-block mb-1"><i class="fas fa-bus text-theme-light mr-1"></i> Transporte y Traslado</small>
                    <strong class="text-dark d-block">{{ paciente?.infoSocioeconomica?.medioTransporte || 'N/A' }} <span class="badge badge-light border ml-1"><i class="fas fa-stopwatch text-muted mr-1"></i>{{ paciente?.infoSocioeconomica?.tiempoTraslado || '0' }} min</span></strong>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
            
        <div v-if="tabActual === 'medicos'" class="fade-in">
          <div class="container-fluid p-0">
            
            <div v-if="paciente?.datosMedicos">
              
              <div class="card mb-4 section-card shadow-sm border-0 rounded-lg custom-card">
                <div class="card-header bg-white border-bottom-0 pt-4 pb-0 text-uppercase font-weight-bold text-theme">
                  <i class="fas fa-heartbeat mr-2 fa-lg"></i> Signos y Biométricos
                </div>
                <div class="card-body">
                  <div class="row">
                    <div class="col-6 col-md-3 mb-3">
                      <small class="text-muted d-block text-uppercase mb-1"><i class="fas fa-ruler-vertical text-theme-light mr-1"></i> Altura</small>
                      <strong class="text-dark h5">{{ paciente.datosMedicos.biometricos?.altura || '-' }} cm</strong>
                    </div>
                    <div class="col-6 col-md-3 mb-3">
                      <small class="text-muted d-block text-uppercase mb-1"><i class="fas fa-weight text-theme-light mr-1"></i> Peso</small>
                      <strong class="text-dark h5">{{ paciente.datosMedicos.biometricos?.peso || '-' }} kg</strong>
                    </div>
                    <div class="col-6 col-md-3 mb-3">
                      <small class="text-muted d-block text-uppercase mb-1"><i class="fas fa-calculator text-theme-light mr-1"></i> IMC</small>
                      <span class="badge bg-dark text-white px-3 py-2 shadow-sm" style="font-size: 0.9rem;">
                        {{ paciente.datosMedicos.biometricos?.imc || '-' }}
                      </span>
                    </div>
                    <div class="col-6 col-md-3 mb-3">
                      <small class="text-muted d-block text-uppercase mb-1"><i class="fas fa-tint text-danger mr-1"></i> Tipo Sangre</small>
                      <span class="badge bg-danger text-white px-3 py-2 shadow-sm" style="font-size: 0.9rem;">
                        {{ paciente.datosMedicos.biometricos?.grupoSanguineo || '' }}{{ paciente.datosMedicos.biometricos?.factorRh || '' }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card mb-4 section-card shadow-sm border-0 rounded-lg custom-card">
                <div class="card-header bg-white border-bottom-0 pt-4 pb-0 text-uppercase font-weight-bold text-theme">
                  <i class="fas fa-notes-medical mr-2 fa-lg"></i> Antecedentes Clínicos
                </div>
                <div class="card-body">
                  <div class="row">
                    
                    <div class="col-12 col-md-6 mb-4">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-allergies text-warning mr-1"></i> Alergias</small>
                      <div v-if="paciente.datosMedicos.alergias?.tiene && paciente.datosMedicos.alergias?.detalles?.length > 0" class="p-3 bg-light rounded-lg h-100 border-left-warning shadow-sm">
                        <div v-for="(alergia, index) in paciente.datosMedicos.alergias.detalles" :key="index" class="mb-3 border-bottom pb-2">
                          <strong class="text-dark d-block mb-1"><i class="fas fa-exclamation-triangle text-warning mr-1"></i> {{ alergia.tipoAlergia || 'ALERGIA' }} <span v-if="alergia.sustanciaAlergia" class="text-muted font-weight-normal">({{ alergia.sustanciaAlergia }})</span></strong>
                          <span class="text-muted small"><i class="fas fa-head-side-cough mr-1"></i> Reacciones:</span> <span class="text-dark small">{{ alergia.reaccionesAlergia ? (Array.isArray(alergia.reaccionesAlergia) ? alergia.reaccionesAlergia.join(', ') : alergia.reaccionesAlergia) : 'N/A' }}</span><br>
                          <span class="text-muted small"><i class="fas fa-tachometer-alt mr-1"></i> Gravedad:</span> <span class="badge bg-warning text-dark ml-1">{{ alergia.gravedadAlergia }}</span><br>
                          <span class="text-muted small"><i class="fas fa-first-aid mr-1"></i> Tratamiento:</span> <span class="text-dark small">{{ alergia.tratamientoAlergia }}</span>
                        </div>
                      </div>
                      <div v-else class="p-3 bg-light rounded-lg h-100 text-muted font-italic d-flex align-items-center"><i class="fas fa-check-circle text-success mr-2"></i> No reporta alergias.</div>
                    </div>

                    <div class="col-12 col-md-6 mb-4">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-lungs-virus text-info mr-1"></i> Enfermedades Crónicas</small>
                      <div v-if="paciente.datosMedicos.enfermedadesCronicas?.tiene && paciente.datosMedicos.enfermedadesCronicas?.detalles?.length > 0" class="p-3 bg-light rounded-lg h-100 border-left-info shadow-sm">
                        <div v-for="(enf, index) in paciente.datosMedicos.enfermedadesCronicas.detalles" :key="index" class="mb-3 border-bottom pb-2">
                          <strong class="text-dark d-block mb-1"><i class="fas fa-virus mr-1 text-info"></i> {{ enf.nombre || 'Enfermedad' }} <span class="badge bg-info text-white ml-1">{{ enf.estado }}</span></strong>
                          <span class="text-muted small"><i class="fas fa-stethoscope mr-1"></i> Diagnóstico:</span> <span class="text-dark small">{{ enf.diagnostico || 'N/A' }}</span><br>
                          <span class="text-muted small"><i class="fas fa-pills mr-1"></i> Tratamiento:</span> <span class="text-dark small">{{ enf.tratamiento || 'N/A' }}</span>
                        </div>
                      </div>
                      <div v-else class="p-3 bg-light rounded-lg h-100 text-muted font-italic d-flex align-items-center"><i class="fas fa-check-circle text-success mr-2"></i> El paciente no reporta enf. crónicas.</div>
                    </div>

                    <div class="col-12 mb-4">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-procedures text-theme mr-1"></i> Cirugías Previas</small>
                      <div v-if="paciente.datosMedicos.cirugiasPrevias?.tiene && paciente.datosMedicos.cirugiasPrevias?.detalles?.length > 0" class="p-3 bg-light rounded-lg border-left-theme shadow-sm">
                        <div class="row">
                          <div v-for="(cirugia, index) in paciente.datosMedicos.cirugiasPrevias.detalles" :key="index" class="col-12 col-md-6 mb-3">
                            <strong class="text-dark d-block mb-1"><i class="fas fa-scalpel text-theme mr-1"></i> {{ cirugia.nombre }} <span class="badge badge-light border ml-1"><i class="fas fa-calendar-alt text-muted mr-1"></i>{{ cirugia.anio || 'Año desc.' }}</span></strong>
                            <span class="text-muted small ml-1"><i class="fas fa-exclamation-circle mr-1"></i> Complicaciones:</span> <span class="text-dark small">{{ cirugia.huboComplicaciones }}</span><br>
                            <span class="text-muted small ml-1"><i class="fas fa-notes-medical mr-1"></i> Motivo:</span> <span class="text-dark small">{{ cirugia.motivo || 'N/A' }}</span>
                          </div>
                        </div>
                      </div>
                      <div v-else class="p-3 bg-light rounded-lg text-muted font-italic d-flex align-items-center"><i class="fas fa-check-circle text-success mr-2"></i> No se reportan cirugías previas.</div>
                    </div>
                    
                    <div class="col-12 col-md-6 mb-3">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-microscope text-secondary mr-1"></i> Personales Patológicos</small>
                      <div v-if="paciente.datosMedicos.antecedentesPatologicos?.tiene && paciente.datosMedicos.antecedentesPatologicos?.detalles?.length > 0" class="p-3 bg-light rounded-lg h-100 shadow-sm border-left-secondary">
                        <ul class="pl-0 mb-0 text-dark list-unstyled">
                          <li v-for="(pat, index) in paciente.datosMedicos.antecedentesPatologicos.detalles" :key="index" class="mb-2 border-bottom pb-1">
                            <i class="fas fa-disease text-theme mr-2"></i> {{ pat.enfermedad }} <span v-if="pat.anio" class="badge badge-light border float-right">{{ pat.anio }}</span>
                          </li>
                        </ul>
                      </div>
                      <div v-else class="p-3 bg-light rounded-lg h-100 text-muted font-italic d-flex align-items-center"><i class="fas fa-check-circle text-success mr-2"></i> Sin antecedentes patológicos previos.</div>
                    </div>

                    <div class="col-12 col-md-6 mb-3">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-users text-secondary mr-1"></i> Familiares Hereditarios</small>
                      <div v-if="paciente.datosMedicos.antecedentesFamiliares?.tiene && paciente.datosMedicos.antecedentesFamiliares?.detalles?.length > 0" class="p-3 bg-light rounded-lg h-100 shadow-sm border-left-secondary">
                        <ul class="pl-0 mb-0 text-dark list-unstyled">
                          <li v-for="(fam, index) in paciente.datosMedicos.antecedentesFamiliares.detalles" :key="index" class="mb-2 border-bottom pb-1">
                            <i class="fas fa-dna text-theme mr-2"></i> {{ fam.enfermedad }} <span class="badge bg-secondary text-white float-right"><i class="fas fa-user-friends mr-1"></i>{{ fam.parentezco }}</span>
                          </li>
                        </ul>
                      </div>
                      <div v-else class="p-3 bg-light rounded-lg h-100 text-muted font-italic d-flex align-items-center"><i class="fas fa-check-circle text-success mr-2"></i> Sin antecedentes familiares de importancia.</div>
                    </div>

                  </div>
                </div>
              </div>

              <div class="card mb-4 section-card shadow-sm border-0 rounded-lg custom-card">
                <div class="card-header bg-white border-bottom-0 pt-4 pb-0 text-uppercase font-weight-bold text-theme">
                  <i class="fas fa-walking mr-2 fa-lg"></i> Hábitos y Consumo
                </div>
                <div class="card-body">
                  <div class="row text-center text-sm-left">
                    <div class="col-6 col-md-3 mb-4 mb-md-0 border-md-right">
                      <small class="text-muted d-block mb-1"><i class="fas fa-smoking mr-1"></i> Tabaco</small>
                      <strong :class="paciente.datosMedicos.habitos?.tabaco !== 'No fuma' ? 'text-danger' : 'text-dark'" class="d-block">
                        {{ paciente.datosMedicos.habitos?.tabaco || 'No fuma' }}
                      </strong>
                      <small class="text-muted d-block mt-1"><i class="fas fa-redo-alt mr-1"></i> {{ paciente.datosMedicos.habitos?.tabacoFrecuencia || 'N/A' }}</small>
                    </div>
                    <div class="col-6 col-md-3 mb-4 mb-md-0 border-md-right">
                      <small class="text-muted d-block mb-1"><i class="fas fa-wine-glass-alt mr-1"></i> Alcohol</small>
                      <strong class="text-dark d-block">{{ paciente.datosMedicos.habitos?.alcohol || 'No' }}</strong>
                      <small class="text-muted d-block mt-1"><i class="fas fa-redo-alt mr-1"></i> {{ paciente.datosMedicos.habitos?.alcoholFrecuencia || 'N/A' }}</small>
                    </div>
                    <div class="col-6 col-md-3 mb-3 mb-md-0 border-md-right">
                      <small class="text-muted d-block mb-1"><i class="fas fa-cannabis mr-1"></i> Drogas</small>
                      <strong :class="paciente.datosMedicos.habitos?.drogas !== 'No' ? 'text-danger' : 'text-dark'" class="d-block">
                        {{ paciente.datosMedicos.habitos?.drogas || 'No' }}
                      </strong>
                      <small class="text-muted d-block mt-1"><i class="fas fa-pills mr-1"></i> {{ paciente.datosMedicos.habitos?.drogasTipo || 'N/A' }}</small>
                    </div>
                    <div class="col-6 col-md-3 mb-3 mb-md-0">
                      <small class="text-muted d-block mb-1"><i class="fas fa-shield-virus mr-1"></i> Vacunación</small>
                      <strong class="text-dark d-block"><i class="fas fa-syringe text-muted mr-1"></i> {{ paciente.datosMedicos.habitos?.esquemaVacunacion || 'Desconoce' }}</strong>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card mb-4 section-card shadow-sm border-0 rounded-lg custom-card">
                <div class="card-header bg-white border-bottom-0 pt-4 pb-0 text-uppercase font-weight-bold text-theme">
                  <i class="fas fa-prescription-bottle-alt mr-2 fa-lg"></i> Tratamiento y Observaciones
                </div>
                <div class="card-body">
                  <div class="row">
                    
                    <div class="col-12 col-md-6 mb-4">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-pills text-success mr-1"></i> Medicamentos Actuales</small>
                      <div v-if="paciente.datosMedicos.medicamentosActuales?.tiene && paciente.datosMedicos.medicamentosActuales?.detalles?.length > 0" class="p-3 bg-light rounded-lg h-100 shadow-sm border-left-success">
                        <div v-for="(med, index) in paciente.datosMedicos.medicamentosActuales.detalles" :key="index" class="mb-3 border-bottom pb-2">
                          <strong class="text-dark d-block mb-1"><i class="fas fa-tablets text-success mr-1"></i> {{ med.nombre }}</strong>
                          <span class="text-muted small ml-1"><i class="fas fa-notes-medical mr-1"></i> Motivo:</span> <span class="text-dark small">{{ med.motivo || 'N/A' }}</span><br>
                          <span class="text-muted small ml-1"><i class="fas fa-clock mr-1"></i> Frec:</span> <span class="text-dark small">{{ med.frecuencia || 'N/A' }}</span>
                        </div>
                      </div>
                      <div v-else class="p-3 bg-light rounded-lg h-100 text-muted font-italic d-flex align-items-center"><i class="fas fa-check-circle text-success mr-2"></i> No toma medicamentos actualmente.</div>
                    </div>

                    <div class="col-12 col-md-6 mb-4">
                      <small class="text-muted d-block mb-2 text-uppercase font-weight-bold"><i class="fas fa-user-md text-theme mr-1"></i> Notas del Médico</small>
                      <div class="p-4 bg-light rounded-lg h-100 shadow-sm border-left-theme">
                        <p class="text-dark mb-0 font-weight-medium" style="white-space: pre-wrap; line-height: 1.6;">
                          <em v-if="!paciente.datosMedicos.observacionesGenerales" class="text-muted"><i class="fas fa-info-circle mr-1"></i> Sin observaciones registradas en este expediente.</em>
                          <span v-else><i class="fas fa-quote-left text-muted mr-2 opacity-50"></i>{{ paciente.datosMedicos.observacionesGenerales }}</span>
                        </p>
                      </div>
                    </div>

                  </div>
                </div>
              </div>

            </div>
            
            <div v-else class="text-center py-5">
              <i class="fas fa-folder-open text-theme mb-3 opacity-50" style="font-size: 4rem;"></i>
              <p class="text-muted h5 mt-2">Cargando expediente médico...</p>
            </div>
            
          </div>
        </div>

      </div> 
    </div>

    <div v-if="mostrarModalSocioeconomico" class="modal-overlay">
      <div class="modal-content shadow-lg p-0 rounded-lg" style="width: 100%; max-width: 700px; background: white;">
        <div class="card border-0 mb-0">
          <div class="card-header d-flex justify-content-between align-items-center bg-theme text-white rounded-top px-4 py-3">
            <h6 class="mb-0 font-weight-bold"><i class="fas fa-folder-open mr-2"></i> Expediente Socioeconómico Completo</h6>
            <button class="btn btn-sm text-white" @click="mostrarModalSocioeconomico = false">
              <i class="fas fa-times fa-lg"></i>
            </button>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive" style="max-height: 60vh; overflow-y: auto;">
              <table class="table table-sm table-hover mb-0">
                <thead class="bg-light sticky-top shadow-sm">
                  <tr>
                    <th class="py-3 px-4 text-theme text-uppercase border-0" style="font-size: 0.85rem;"><i class="fas fa-question-circle mr-1"></i> Pregunta</th>
                    <th class="py-3 px-4 text-theme text-uppercase border-0" style="font-size: 0.85rem;"><i class="fas fa-check-circle mr-1"></i> Respuesta</th>
                    <th class="py-3 px-4 text-theme text-uppercase border-0" style="font-size: 0.85rem;"><i class="fas fa-align-left mr-1"></i> Detalles</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in paciente?.expedienteCompleto" :key="item.id">
                    <td class="font-weight-bold text-dark px-4 py-3 align-middle border-bottom" style="font-size: 0.9rem;">{{ item.pregunta }}</td>
                    <td class="px-4 py-3 align-middle border-bottom">
                      <span class="badge bg-theme text-white px-2 py-1">{{ item.respuesta || 'N/A' }}</span>
                    </td>
                    <td class="text-muted px-4 py-3 align-middle border-bottom" style="font-size: 0.9rem;">{{ item.respuestaAbierta || '-' }}</td>
                  </tr>
                </tbody>
              </table>
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
<style scoped>
/* ==========================================================================
   TEMA INSTITUCIONAL LOCAL (Guinda #5c1830)
   ========================================================================== */
.text-theme { color: #5c1830 !important; }
.text-theme-light { color: #8a3a52 !important; }
.bg-theme { background-color: #5c1830 !important; }

/* Botones Guinda */
.btn-theme {
  background-color: #5c1830;
  color: white;
  border: none;
  transition: all 0.2s ease;
}
.btn-theme:hover, .btn-theme:focus {
  background-color: #431122;
  color: white;
  transform: translateY(-2px);
}
.btn-theme:disabled { background-color: #8a5768; opacity: 0.7; }

/* Tarjetas y Layout Responsivo */
.custom-card {
  border-top: 4px solid #5c1830 !important;
  transition: box-shadow 0.2s ease;
}
.custom-card:hover {
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important;
}

/* Decoraciones laterales de las tarjetas internas */
.border-left-theme { border-left: 4px solid #5c1830 !important; }
.border-left-warning { border-left: 4px solid #ffc107 !important; }
.border-left-info { border-left: 4px solid #17a2b8 !important; }
.border-left-success { border-left: 4px solid #28a745 !important; }
.border-left-secondary { border-left: 4px solid #6c757d !important; }

/* Estilos para las Tabs (Pestañas) - scrollable en móviles */
.custom-tabs {
  border-bottom: 2px solid #e9ecef;
  flex-wrap: nowrap;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}
.custom-tabs::-webkit-scrollbar { display: none; }
.custom-tabs .nav-link {
  color: #6c757d;
  font-weight: 600;
  border: none;
  border-bottom: 3px solid transparent;
  padding: 1rem 1.5rem;
  transition: all 0.2s ease;
  white-space: nowrap;
}
.custom-tabs .nav-link:hover {
  color: #5c1830;
  background-color: transparent;
}
.custom-tabs .nav-link.active {
  color: #5c1830;
  background-color: transparent;
  border-bottom: 3px solid #5c1830;
}

/* Bordes responsivos para grids */
@media (min-width: 768px) {
  .border-md-right { border-right: 1px solid #dee2e6 !important; }
}

/* Animación de fade */
.fade-in { animation: fadeIn 0.3s ease-in; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(5px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Modal Responsivo */
.modal-overlay {
  position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
  background: rgba(15, 23, 42, 0.65); backdrop-filter: blur(5px);
  display: flex; justify-content: center; align-items: center; z-index: 9999;
  padding: 1rem;
}
.modal-content {
  animation: aparecerModal 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  max-height: 90vh; overflow-y: hidden; /* El scroll lo hace el table-responsive */
  display: flex; flex-direction: column;
}
@keyframes aparecerModal {
  from { transform: scale(0.95); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

/* Fix text break en móviles para emails o descripciones largas */
.text-wrap-mobile { word-break: break-word; }
</style>