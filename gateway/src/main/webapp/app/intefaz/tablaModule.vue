<template>
  <div class="tabla-module-container">
    <div class="card shadow-sm border-0">
      
      <div class="card-header bg-white pb-0">
        <ul class="nav nav-tabs border-bottom-0">
          <li class="nav-item">
            <a class="nav-link text-secondary" style="cursor: pointer;" 
               :class="{ 'active font-weight-bold': tabActual === 'datos' }"
               :style="tabActual === 'datos' ? 'color: #ffff !important;' : ''"
               @click="tabActual = 'datos'">Datos del Paciente</a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-secondary" style="cursor: pointer;" 
               :class="{ 'active font-weight-bold': tabActual === 'medicos' }"
               :style="tabActual === 'medicos' ? 'color: #ffff !important;' : ''"
               @click="tabActual = 'medicos'">Historial Médico</a>
          </li>
        </ul>
      </div>

      <div class="card-body bg-light p-4">
        
        <div v-if="tabActual === 'datos'">
          <div class="container-fluid p-0">
            
            <div class="card mb-4 shadow-sm border-0" style="border-left: 5px solid #9F2241 !important;">
              <div class="card-header text-white font-weight-bold" style="background-color: #9F2241; padding: 10px 15px;">
                Datos Generales del Paciente
              </div>
              <div class="card-body bg-white p-4">
                <div class="row">
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">CURP</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #9F2241 !important;">{{ paciente?.curp || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Nombre(s)</small>
                    <strong class="text-dark text-uppercase border-left pl-2" style="border-color: #9F2241 !important;">{{ paciente?.nombre || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Primer Apellido</small>
                    <strong class="text-dark text-uppercase border-left pl-2" style="border-color: #9F2241 !important;">{{ paciente?.apellidoPaterno || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Segundo Apellido</small>
                    <strong class="text-dark text-uppercase border-left pl-2" style="border-color: #9F2241 !important;">{{ paciente?.apellidoMaterno || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Fecha de Nacimiento</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #9F2241 !important;">{{ paciente?.fechaNacimiento || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Edad</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #9F2241 !important;">{{ calcularEdad(paciente?.fechaNacimiento) || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Sexo / Género</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #9F2241 !important;">{{ paciente?.sexo || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Nacionalidad</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #9F2241 !important;">{{ paciente?.nacionalidad || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Estado Civil</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #9F2241 !important;">{{ paciente?.estadoCivil || 'N/A' }}</strong>
                  </div>
                </div>
              </div>
            </div>

            <div class="card mb-4 shadow-sm border-0" style="border-left: 5px solid #B38E5D !important;">
              <div class="card-header text-white font-weight-bold" style="background-color: #B38E5D; padding: 10px 15px;">
                Dirección y Contacto
              </div>
              <div class="card-body bg-white p-4">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <small class="text-muted d-block">Calle y Número</small>
                    <strong class="text-dark text-uppercase border-left pl-2" style="border-color: #B38E5D !important;">
                      {{ paciente?.direccion?.vialidad || '' }} 
                      {{ paciente?.direccion?.nombreVialidad || 'No registrada' }} 
                      {{ paciente?.direccion?.numExterior || '' }} 
                      {{ paciente?.direccion?.numInterior ? 'Int. ' + paciente?.direccion?.numInterior : '' }}
                    </strong>
                  </div>
                  <div class="col-md-3 mb-3">
                    <small class="text-muted d-block">Colonia</small>
                    <strong class="text-dark text-uppercase border-left pl-2" style="border-color: #B38E5D !important;">{{ paciente?.direccion?.codigoPostalInfo?.asentamiento || 'No registrada' }}</strong>
                  </div>
                  <div class="col-md-3 mb-3">
                    <small class="text-muted d-block">Código Postal</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #B38E5D !important;">{{ paciente?.direccion?.codigoPostalInfo?.codigo || 'No registrada' }}</strong>
                  </div>
                  <div class="col-md-4 mb-3">
                    <small class="text-muted d-block">Alcaldía / Municipio</small>
                    <strong class="text-dark text-uppercase border-left pl-2" style="border-color: #B38E5D !important;">{{ paciente?.direccion?.codigoPostalInfo?.municipio || 'No registrada' }}</strong>
                  </div>
                  <div class="col-md-4 mb-3">
                    <small class="text-muted d-block">Estado</small>
                    <strong class="text-dark text-uppercase border-left pl-2" style="border-color: #B38E5D !important;">{{ paciente?.direccion?.codigoPostalInfo?.estado || 'No registrada' }}</strong>
                  </div>
                  <div class="col-md-4 mb-3">
                    <small class="text-muted d-block">Teléfono</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #B38E5D !important;">{{ paciente?.direccion?.telefono || 'No registrado' }}</strong>
                  </div>
                </div>
              </div>
            </div>

            <div class="card mb-4 shadow-sm border-0" style="border-left: 5px solid #235B4E !important;">
              <div class="card-header text-white font-weight-bold d-flex justify-content-between align-items-center" style="background-color: #235B4E; padding: 10px 15px;">
                <span>Resumen Socioeconómico</span>
                <button class="btn btn-sm btn-light font-weight-bold" style="color: #235B4E;" @click="mostrarModalSocioeconomico = true" :disabled="!paciente?.tieneInfoSocioeconomica">
                  <span v-if="paciente?.tieneInfoSocioeconomica">Ver Expediente Completo</span>
                  <span v-else class="text-muted">No hay datos registrados</span>
                </button>
              </div>
              <div class="card-body bg-white p-4">
                <div v-if="paciente?.tieneInfoSocioeconomica" class="row">
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Ocupación Actual</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #235B4E !important;">{{ paciente.resumenSocioeconomico?.ocupacion || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Grado de Estudios</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #235B4E !important;">{{ paciente.resumenSocioeconomico?.gradoEstudios || 'N/A' }}</strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Ingreso Mensual</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #235B4E !important;">
                      {{ paciente.resumenSocioeconomico?.ingresoMensual !== 'N/A' ? '$' + paciente.resumenSocioeconomico?.ingresoMensual : 'N/A' }}
                    </strong>
                  </div>
                  <div class="col-md-3 col-sm-6 mb-3">
                    <small class="text-muted d-block">Afiliación Médica</small>
                    <strong class="text-dark border-left pl-2" style="border-color: #235B4E !important;">{{ paciente.resumenSocioeconomico?.afiliacion || 'N/A' }}</strong>
                  </div>
                </div>
                <p v-else class="text-muted mb-0 font-italic">El paciente no cuenta con un estudio socioeconómico registrado.</p>
              </div>
            </div>

            <div v-if="mostrarModalSocioeconomico" class="card border-0 shadow-sm mb-4" style="border-left: 5px solid #235B4E !important;">
              <div class="card-header bg-white border-bottom d-flex justify-content-between align-items-center">
                <h5 class="mb-0" style="color: #235B4E; font-weight: bold;">Expediente Socioeconómico Completo</h5>
                <button class="btn btn-sm btn-light font-weight-bold" style="color: #9F2241;" @click="mostrarModalSocioeconomico = false">
                  Cerrar ✕
                </button>
              </div>
              <div class="card-body p-0">
                <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                  <table class="table table-sm table-striped table-hover mb-0">
                    <thead class="text-white sticky-top" style="background-color: #4A4A49;">
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
                          <span class="badge" style="background-color: #235B4E; color: white; padding: 6px 10px;">
                            {{ item.respuesta || 'N/A' }}
                          </span>
                        </td>
                        <td class="text-muted px-3 align-middle">{{ item.respuestaAbierta || '-' }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

            <div v-if="paciente?.infoSocioeconomica" class="row">
              <div class="col-md-12 mb-3">
                <div class="card shadow-sm border-0" style="border-left: 5px solid #235B4E !important;">
                  <div class="card-header text-white font-weight-bold" style="background-color: #235B4E; padding: 10px 15px;">
                    Datos de Vivienda
                  </div>
                  <div class="card-body bg-white p-3">
                    <div class="row">
                      <div class="col-md-4 mb-2">
                        <small class="text-muted d-block">Tipo de Vivienda</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.tipoVivienda || 'N/A' }}</strong>
                      </div>
                      <div class="col-md-4 mb-2">
                        <small class="text-muted d-block">Material</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.materialVivienda || 'N/A' }}</strong>
                      </div>
                      <div class="col-md-4 mb-2">
                        <small class="text-muted d-block">Habitaciones / Habitantes</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.numeroHabitaciones || '0' }} / {{ paciente?.infoSocioeconomica?.numeroHabitantes || '0' }}</strong>
                      </div>
                      <div class="col-md-12 mt-2">
                        <small class="text-muted d-block">Servicios Disponibles</small>
                        <strong class="text-dark">{{ paciente?.infoSocioeconomica?.serviciosDisponibles || 'N/A' }}</strong>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-md-6 mb-3">
                <div class="card shadow-sm border-0 h-100" style="border-left: 5px solid #235B4E !important;">
                  <div class="card-header text-white font-weight-bold" style="background-color: #235B4E; padding: 10px 15px;">
                    Economía y Empleo
                  </div>
                  <div class="card-body bg-white p-3">
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
                <div class="card shadow-sm border-0 h-100" style="border-left: 5px solid #235B4E !important;">
                  <div class="card-header text-white font-weight-bold" style="background-color: #235B4E; padding: 10px 15px;">
                    Educación y Salud
                  </div>
                  <div class="card-body bg-white p-3">
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

        <div v-if="tabActual === 'medicos'">
          <div v-if="paciente?.datosMedicos" class="row">
            
            <div class="col-12 mb-4">
              <div class="card shadow-sm" style="border: 1px solid #9F2241; border-radius: 4px;">
                <div class="card-header text-white font-weight-bold" style="background-color: #9F2241; padding: 10px 15px; border-bottom: none;">
                  Signos y Biométricos
                </div>
                <div class="card-body bg-white p-3">
                  <div class="row align-items-center">
                    <div class="col-md-3 col-sm-6 mb-2 mb-md-0">
                      <strong class="text-dark">Altura: <span class="font-weight-normal">{{ paciente.datosMedicos.biometricos?.altura || '-' }} cm</span></strong>
                    </div>
                    <div class="col-md-3 col-sm-6 mb-2 mb-md-0">
                      <strong class="text-dark">Peso: <span class="font-weight-normal">{{ paciente.datosMedicos.biometricos?.peso || '-' }} kg</span></strong>
                    </div>
                    <div class="col-md-3 col-sm-6 mb-2 mb-md-0">
                      <strong class="text-dark">IMC: 
                        <span class="badge ml-1" style="background-color: #8E44AD; color: white; padding: 5px 8px;">
                          {{ paciente.datosMedicos.biometricos?.imc || '-' }}
                        </span>
                      </strong>
                    </div>
                    <div class="col-md-3 col-sm-6 mb-2 mb-md-0">
                      <strong class="text-dark">Grupo Sanguíneo: 
                        <span class="badge bg-danger text-white ml-1" style="padding: 5px 8px;">
                          {{ paciente.datosMedicos.biometricos?.grupoSanguineo || '' }}{{ paciente.datosMedicos.biometricos?.factorRh || '' }}
                        </span>
                      </strong>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-md-6">
              
              <div class="card mb-4 shadow-sm" style="border: 1px solid #235B4E; border-radius: 4px;">
                <div class="card-header text-white font-weight-bold" style="background-color: #235B4E; padding: 10px 15px; border-bottom: none;">Alergias</div>
                <div class="card-body p-3 bg-white">
                  <div v-if="paciente.datosMedicos.alergias.tiene && paciente.datosMedicos.alergias.detalles.length > 0">
                    <div v-for="(alergia, index) in paciente.datosMedicos.alergias.detalles" :key="index" class="border-bottom pb-2 mb-2">
                      <strong class="text-uppercase">{{ alergia.tipoAlergia || 'ALERGIA' }} /</strong><br>
                      <small><b>Reacciones:</b> {{ alergia.reaccionesAlergia ? alergia.reaccionesAlergia.join(', ') : 'N/A' }}</small><br>
                      <small><b>Gravedad:</b> <span class="badge" style="background-color: #fd7e14; color: white;">{{ alergia.gravedadAlergia }}</span></small><br>
                      <small><b>Tratamiento Habitual:</b> {{ alergia.tratamientoAlergia }}</small><br>
                      <small><b>Último Episodio:</b> {{ alergia.ultimoEpisodioAlergia || 'Desconocida' }}</small>
                    </div>
                  </div>
                  <div v-else class="text-muted font-italic small p-2">No reporta alergias.</div>
                </div>
              </div>

              <div class="card mb-4 shadow-sm" style="border: 1px solid #9F2241 ; border-radius: 4px;">
                <div class="card-header text-white font-weight-bold" style="background-color: #9F2241; padding: 10px 15px; border-bottom: none;">
                  <i class="fas fa-procedures mr-2"></i> Cirugías Previas
                </div>
                <div class="card-body p-3 bg-white">
                  <p v-if="paciente.datosMedicos.cirugiasPrevias.tiene" class="text-dark small mb-0 p-2">Sí reporta cirugías previas.</p>
                  <p v-else class="text-muted font-italic small mb-0 p-2">No se reportan cirugías previas.</p>
                </div>
              </div>

              <div class="card mb-4 shadow-sm" style="border: 1px solid #B38E5D; border-radius: 4px;">
                <div class="card-header text-white font-weight-bold" style="background-color: #B38E5D; padding: 10px 15px; border-bottom: none;">
                  <i class="fas fa-users mr-2"></i> Antecedentes Familiares
                </div>
                <div class="card-body p-3 bg-white">
                  <p v-if="paciente.datosMedicos.antecedentesFamiliares.tiene" class="text-dark small mb-0 p-2">Sí reporta antecedentes familiares.</p>
                  <p v-else class="text-muted font-italic small mb-0 p-2">Sin antecedentes familiares de importancia.</p>
                </div>
              </div>

              <div class="card mb-4 shadow-sm" style="border: 1px solid #235B4E; border-radius: 4px;">
                <div class="card-header text-white font-weight-bold" style="background-color: #235B4E; padding: 10px 15px; border-bottom: none;">
                  <i class="fas fa-smoking mr-2"></i> Hábitos, Consumo y Otros
                </div>
                <div class="card-body p-3 bg-white">
                  
                  <div class="row text-left border-bottom pb-2 mb-2 mx-0">
                    <div class="col-4 px-1">
                      <small class="text-dark font-weight-bold d-block mb-2 border-bottom pb-1">Tabaco</small>
                      <strong :class="paciente.datosMedicos.habitos?.tabaco === 'Fuma actualmente' ? 'text-danger' : 'text-dark'" style="font-size: 0.85rem; display: block; min-height: 20px;">
                        {{ paciente.datosMedicos.habitos?.tabaco || 'No fuma' }}
                      </strong>
                      <small class="text-muted" style="font-size: 0.75rem;">Frecuencia: <span class="text-dark">{{ paciente.datosMedicos.habitos?.tabacoFrecuencia || '-' }}</span></small>
                    </div>
                    <div class="col-4 px-1">
                      <small class="text-dark font-weight-bold d-block mb-2 border-bottom pb-1">Alcohol</small>
                      <strong class="text-dark" style="font-size: 0.85rem; display: block; min-height: 20px;">{{ paciente.datosMedicos.habitos?.alcohol || 'No' }}</strong>
                      <small class="text-muted" style="font-size: 0.75rem;">Frecuencia: <span class="text-dark">{{ paciente.datosMedicos.habitos?.alcoholFrecuencia || '-' }}</span></small>
                    </div>
                    <div class="col-4 px-1">
                      <small class="text-dark font-weight-bold d-block mb-2 border-bottom pb-1">Drogas</small>
                      <strong :class="paciente.datosMedicos.habitos?.drogas === 'Sí' ? 'text-danger' : 'text-dark'" style="font-size: 0.85rem; display: block; min-height: 20px;">
                        {{ paciente.datosMedicos.habitos?.drogas || 'No' }}
                      </strong>
                      <small class="text-muted" style="font-size: 0.75rem; display: block;">Tipo: <span class="text-dark">{{ paciente.datosMedicos.habitos?.drogasTipo || 'N/A' }}</span></small>
                      <small class="text-muted" style="font-size: 0.75rem;">Frecuencia: <span class="text-dark">{{ paciente.datosMedicos.habitos?.drogasFrecuencia || '-' }}</span></small>
                    </div>
                  </div>

                  <div class="row text-left mx-0 pt-1">
                    <div class="col-6 px-1">
                      <small class="text-dark font-weight-bold d-block mb-2 border-bottom pb-1">Vacunación</small>
                      <small class="text-muted" style="font-size: 0.8rem;">Esquema: <span class="text-dark">{{ paciente.datosMedicos.habitos?.esquemaVacunacion || 'Desconoce' }}</span></small>
                    </div>
                    <div class="col-6 px-1">
                      <small class="text-dark font-weight-bold d-block mb-2 border-bottom pb-1">Discapacidad</small>
                      <strong :style="paciente.datosMedicos.habitos?.tieneDiscapacidad === 'Sí' ? 'color: #8E44AD; font-size: 0.85rem;' : 'color: #343a40; font-size: 0.85rem;'">
                        {{ paciente.datosMedicos.habitos?.tieneDiscapacidad || 'No' }}
                      </strong><br>
                      <small class="text-muted" style="font-size: 0.8rem;">Tipo: <span class="text-dark">{{ paciente.datosMedicos.habitos?.tipoDiscapacidad || '-' }}</span></small>
                    </div>
                  </div>

                </div>
              </div>

            </div>

            <div class="col-md-6">
              
              <div class="card mb-4 shadow-sm" style="border: 1px solid #B38E5D; border-radius: 4px;">
                <div class="card-header text-white font-weight-bold" style="background-color: #B38E5D; padding: 10px 15px; border-bottom: none;">
                  <i class="fas fa-heartbeat mr-2"></i> Enfermedades Crónicas
                </div>
                <div class="card-body p-3 bg-white">
                  <div v-if="paciente.datosMedicos.enfermedadesCronicas.tiene && paciente.datosMedicos.enfermedadesCronicas.detalles.length > 0">
                    <div v-for="(enf, index) in paciente.datosMedicos.enfermedadesCronicas.detalles" :key="index" class="border-bottom pb-2 mb-2">
                      <strong class="text-uppercase">{{ enf.nombreEnfermedad || 'Enfermedad' }}</strong>
                    </div>
                  </div>
                  <div v-else class="text-muted font-italic small p-2">El paciente no reporta o desconoce tener enfermedades crónicas.</div>
                </div>
              </div>

              <div class="card mb-4 shadow-sm" style="border: 1px solid #9F2241; border-radius: 4px;">
                <div class="card-header text-white font-weight-bold" style="background-color: #9F2241; padding: 10px 15px; border-bottom: none;">
                  <i class="fas fa-pills mr-2"></i> Medicamentos Actuales
                </div>
                <div class="card-body p-3 bg-white">
                  <div v-if="paciente.datosMedicos.medicamentosActuales.tiene && paciente.datosMedicos.medicamentosActuales.detalles.length > 0">
                    <div v-for="(med, index) in paciente.datosMedicos.medicamentosActuales.detalles" :key="index" class="border-bottom pb-2 mb-2">
                      <strong class="text-uppercase">{{ med.nombre || 'Medicamento' }}</strong>
                    </div>
                  </div>
                  <div v-else class="text-muted font-italic small p-2">No toma medicamentos actualmente.</div>
                </div>
              </div>

              <div class="card mb-4 shadow-sm" style="border: 1px solid #235B4E; border-radius: 4px;">
                <div class="card-header text-white font-weight-bold" style="background-color: #235B4E; padding: 10px 15px; border-bottom: none;">
                  <i class="fas fa-notes-medical mr-2"></i> Antecedentes Personales Patológicos
                </div>
                <div class="card-body p-3 bg-white">
                  <p v-if="paciente.datosMedicos.antecedentesPatologicos.tiene" class="text-dark small mb-0 p-2">Sí reporta antecedentes patológicos.</p>
                  <p v-else class="text-muted font-italic small mb-0 p-2">Sin antecedentes patológicos previos.</p>
                </div>
              </div>

            </div>

            <div class="col-12 mt-2">
               <div class="card shadow-sm" style="border: 1px solid #9F2241; border-radius: 4px;">
                <div class="card-header text-white font-weight-bold" style="background-color: #9F2241; padding: 10px 15px; border-bottom: none;">
                  Observaciones Médicas Generales
                </div>
                <div class="card-body p-3 bg-white">
                  <p class="text-dark small mb-0 p-2" style="white-space: pre-wrap;">{{ paciente.datosMedicos.observacionesGenerales || 'Sin observaciones registradas.' }}</p>
                </div>
              </div>
            </div>

          </div>
          <div v-else class="text-center py-5">
             <p class="text-muted">Cargando datos médicos o no hay información registrada.</p>
          </div>
        </div>

        <div v-if="tabActual === 'diagnostico'">
          <div class="card mb-4 shadow-sm border-0" style="border-left: 5px solid #4A4A49 !important;">
            <div class="card-header text-white font-weight-bold" style="background-color: #4A4A49; padding: 10px 15px;">
              Diagnóstico Clínico
            </div>
            <div class="card-body text-center py-5 bg-white">
              <h5 class="text-muted mb-2">Sección en Desarrollo</h5>
              <p class="text-muted mb-0"><em>El historial de diagnósticos estará disponible próximamente.</em></p>
            </div>
          </div>
        </div>

        <div v-if="tabActual === 'medico_responsable'">
          <div class="card mb-4 shadow-sm border-0" style="border-left: 5px solid #B38E5D !important;">
             <div class="card-header text-white font-weight-bold" style="background-color: #B38E5D; padding: 10px 15px;">
              Personal Médico Responsable
            </div>
            <div class="card-body bg-white p-4">
              <div class="row">
                <div class="col-md-6 mb-4">
                  <small class="text-muted d-block font-weight-bold">Médico Encargado</small>
                  <strong class="text-dark text-uppercase border-left pl-2" style="border-color: #B38E5D !important;">{{ paciente?.medicoEncargado || 'Sin asignar' }}</strong>
                </div>
              </div>
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