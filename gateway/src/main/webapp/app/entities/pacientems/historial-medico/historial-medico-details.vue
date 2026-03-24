<template>
  <div class="row justify-content-center">
    <div class="col-12 col-md-11">
      <div v-if="historialMedico && historialMedico.id">
        <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
          <h2 class="jh-entity-heading mb-0">
            <font-awesome-icon icon="file-medical" class="text-primary"/> Expediente Clínico - ID: {{ historialMedico.id }}
          </h2>
          <div>
            <button type="button" @click="previousState()" class="btn btn-secondary mr-2">
              <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Volver</span>
            </button>
            <router-link :to="{ name: 'HistorialMedicoEdit', params: { historialMedicoId: historialMedico.id } }" custom v-slot="{ navigate }">
              <button @click="navigate" class="btn btn-primary">
                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Editar Expediente</span>
              </button>
            </router-link>
          </div>
        </div>

        <div class="card mb-3 shadow-sm border-primary" v-if="historialMedico.pacienteId">
          <div class="card-header bg-primary text-white font-weight-bold">
            <font-awesome-icon icon="user" /> Información del Paciente
          </div>
          <div class="card-body row py-2">
            <div class="col-md-3">
              <strong>ECU:</strong> 
              <span v-if="historialMedico.pacienteEcu" class="badge badge-primary ml-3" style="font-size: 0.6em; vertical-align: middle;">
              ECU: {{ historialMedico.pacienteEcu }} - {{ historialMedico.pacienteNombre }} {{ historialMedico.pacienteApellidoPaterno }}
          </span>            </div>
            <div class="col-md-6">
              <strong>Nombre:</strong> 
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: historialMedico.pacienteId } }">
                {{ historialMedico.pacienteNombre }} {{ historialMedico.pacienteApellidoPaterno }}
              </router-link>
            </div>
          </div>
        </div>

        <div class="card mb-3 shadow-sm border-info">
          <div class="card-header bg-info text-white font-weight-bold">
            <font-awesome-icon icon="stethoscope" /> Signos y Biométricos
          </div>
          <div class="card-body py-2">
            <div class="row" v-if="obtenerJsonPlano(historialMedico.datosBiometricosSanguineos)">
              <div class="col-md-2"><strong>Altura:</strong> {{ obtenerJsonPlano(historialMedico.datosBiometricosSanguineos).altura }} cm</div>
              <div class="col-md-2"><strong>Peso:</strong> {{ obtenerJsonPlano(historialMedico.datosBiometricosSanguineos).peso }} kg</div>
              <div class="col-md-2"><strong>IMC:</strong> <span class="badge badge-info">{{ obtenerJsonPlano(historialMedico.datosBiometricosSanguineos).imc }}</span></div>
              
              <div class="col-md-4">
                <strong>Grupo Sanguíneo:</strong> 
                <span class="badge badge-danger ml-1" style="font-size: 1em;">
                  {{ obtenerJsonPlano(historialMedico.datosBiometricosSanguineos).grupoSanguineo }}{{ obtenerJsonPlano(historialMedico.datosBiometricosSanguineos).factorRh }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="col-md-6">
            <div class="card mb-3 shadow-sm border-danger h-100">
              <div class="card-header bg-danger text-white font-weight-bold">Alergias</div>
              <div class="card-body">
                <ul v-if="parsearLista(historialMedico.alergias).tiene" class="mb-0 list-unstyled">
                  <li v-for="(item, idx) in parsearLista(historialMedico.alergias).detalles" :key="idx" class="mb-3 border-bottom pb-2">
                    <h6 class="font-weight-bold mb-1">{{ item.tipoAlergia }} / {{ item.sustanciaAlergia }}</h6>
                    <div style="font-size: 0.9em; line-height: 1.4;">
                      <strong>Reacciones:</strong> {{ item.reaccionesAlergia?.join(', ') || 'N/A' }} <br>
                      <strong>Gravedad:</strong> <span class="badge badge-warning">{{ item.gravedadAlergia }}</span> <br>
                      <strong>Tratamiento Habitual:</strong> {{ item.tratamientoAlergia || 'N/A' }} <br>
                      <strong>Último Episodio:</strong> {{ item.ultimoEpisodioAlergia || 'No especificado' }}
                    </div>
                  </li>
                </ul>
                <div v-else class="text-muted font-italic">El paciente no reporta alergias.</div>
              </div>
            </div>
          </div>

          <div class="col-md-6">
            <div class="card mb-3 shadow-sm border-warning h-100">
              <div class="card-header bg-warning text-dark font-weight-bold">
                <font-awesome-icon icon="heartbeat"></font-awesome-icon> Enfermedades Crónicas
              </div>
              <div class="card-body">
                <ul v-if="parsearLista(historialMedico.enfermedadesCronicas).tiene" class="mb-0 list-unstyled">
                  <li v-for="(item, idx) in parsearLista(historialMedico.enfermedadesCronicas).detalles" :key="idx" class="mb-3 border-bottom pb-2">
                    
                    <h6 class="font-weight-bold mb-1 text-primary">
                      {{ item.nombre || 'Enfermedad no especificada' }} 
                      <span class="text-muted" style="font-size: 0.85em;">({{ item.tipo || 'Sin clasificación' }})</span>
                    </h6>
                    
                    <div style="font-size: 0.9em; line-height: 1.5;">
                      <div class="row">
                        <div class="col-sm-6">
                          <strong>Diagnóstico:</strong> {{ item.diagnostico || 'N/A' }}
                        </div>
                        <div class="col-sm-6">
                          <strong>Estado:</strong> 
                          <span class="badge" :class="{'badge-success': item.estado === 'Controlada' || item.estado === 'En remisión', 'badge-warning': item.estado === 'Parcialmente controlada' || item.estado === 'En tratamiento', 'badge-danger': item.estado === 'No controlada', 'badge-secondary': !item.estado || item.estado === 'null'}">
                            {{ item.estado && item.estado !== 'null' ? item.estado : 'No especificado' }}
                          </span>
                        </div>
                      </div>

                      <div class="mt-1">
                        <strong>Tratamiento:</strong> {{ item.tratamiento || 'Ninguno' }}
                      </div>
                      
                      <div class="mt-1">
                        <strong>Medicamentos:</strong> {{ item.medicamentos || 'No registra' }}
                      </div>

                      <div class="mt-1 text-muted" v-if="item.medico || item.hospital">
                        <small>
                          <font-awesome-icon icon="user-md"></font-awesome-icon> Tratante: {{ item.medico || 'No especificado' }} 
                          <span v-if="item.hospital">| Clínica: {{ item.hospital }}</span>
                        </small>
                      </div>

                      <div class="mt-2 p-2 bg-light rounded border" v-if="item.observaciones">
                        <strong>Observaciones:</strong> <br>
                        <span style="white-space: pre-wrap;">{{ item.observaciones }}</span>
                      </div>
                    </div>

                  </li>
                </ul>
                <div v-else class="text-muted font-italic">El paciente no reporta o desconoce tener enfermedades crónicas.</div>
              </div>
            </div>
          </div>

          <div class="col-md-6">
            <div class="card mb-3 shadow-sm h-100" style="border-color: #17a2b8;">
              <div class="card-header text-white font-weight-bold" style="background-color: #17a2b8;">
                <font-awesome-icon icon="procedures"></font-awesome-icon> Cirugías Previas
              </div>
              <div class="card-body">
                <ul v-if="parsearLista(historialMedico.cirugiasPrevias).tiene" class="mb-0 list-unstyled">
                  <li v-for="(item, idx) in parsearLista(historialMedico.cirugiasPrevias).detalles" :key="idx" class="mb-3 border-bottom pb-2">
                    
                    <h6 class="font-weight-bold mb-1" style="color: #17a2b8;">
                      {{ item.nombre || 'Procedimiento no especificado' }} 
                      <span class="text-muted" style="font-size: 0.85em;" v-if="item.anio">(Año: {{ item.anio }})</span>
                    </h6>
                    
                    <div style="font-size: 0.9em; line-height: 1.5;">
                      <strong>Tipo:</strong> {{ item.tipo && item.tipo !== 'null' ? item.tipo : 'No especificado' }} <br>
                      <strong>Motivo:</strong> {{ item.motivo || 'N/A' }}
                      
                      <div class="mt-2" v-if="item.huboComplicaciones === 'SI'">
                        <span class="badge badge-danger text-uppercase mb-1">
                          <font-awesome-icon icon="exclamation-triangle"></font-awesome-icon> Hubo complicaciones
                        </span>
                        <div class="p-2 bg-light border border-danger rounded text-danger" style="font-size: 0.95em;">
                          {{ item.descripcionComplicaciones || 'Complicación no descrita' }}
                        </div>
                      </div>
                      <div class="mt-1 text-success font-weight-bold" v-else-if="item.huboComplicaciones === 'NO'" style="font-size: 0.85em;">
                        <font-awesome-icon icon="check-circle"></font-awesome-icon> Sin complicaciones reportadas
                      </div>
                    </div>

                  </li>
                </ul>
                <div v-else class="text-muted font-italic">No se reportan cirugías previas.</div>
              </div>
            </div>
          </div>

          <div class="col-md-6">
            <div class="card mb-3 shadow-sm border-success h-100">
              <div class="card-header bg-success text-white font-weight-bold">
                <font-awesome-icon icon="pills"></font-awesome-icon> Medicamentos Actuales
              </div>
              <div class="card-body">
                <ul v-if="parsearLista(historialMedico.medicamentosActuales).tiene" class="mb-0 list-unstyled">
                  <li v-for="(item, idx) in parsearLista(historialMedico.medicamentosActuales).detalles" :key="idx" class="mb-3 border-bottom pb-2">
                    
                    <h6 class="font-weight-bold mb-1 text-success">
                      {{ item.nombre || 'Medicamento no especificado' }}
                    </h6>
                    
                    <div style="font-size: 0.9em; line-height: 1.5;">
                      <strong>Motivo:</strong> {{ item.motivo || 'No especificado' }} <br>
                      
                      <div class="mt-1">
                        <strong>Frecuencia:</strong> 
                        <span class="badge badge-light border ml-1">
                          <font-awesome-icon icon="clock" class="text-secondary"></font-awesome-icon>
                          {{ item.frecuencia && item.frecuencia !== 'null' ? item.frecuencia : 'No especificada' }}
                        </span>
                      </div>
                    </div>

                  </li>
                </ul>
                <div v-else class="text-muted font-italic">No toma medicamentos actualmente.</div>
              </div>
            </div>
          </div>

          <div class="col-md-6">
            <div class="card mb-3 shadow-sm border-primary h-100">
              <div class="card-header bg-primary text-white font-weight-bold">
                <font-awesome-icon icon="users"></font-awesome-icon> Antecedentes Familiares
              </div>
              <div class="card-body">
                <ul v-if="parsearLista(historialMedico.antecedentesFamiliaresHereditarios).tiene" class="mb-0 list-unstyled">
                  <li v-for="(item, idx) in parsearLista(historialMedico.antecedentesFamiliaresHereditarios).detalles" :key="idx" class="mb-3 border-bottom pb-2">
                    
                    <h6 class="font-weight-bold mb-1 text-primary">
                      <font-awesome-icon icon="dna" class="text-secondary mr-1"></font-awesome-icon> 
                      {{ item.enfermedad && item.enfermedad !== 'null' ? item.enfermedad : 'Enfermedad no especificada' }}
                    </h6>
                    
                    <div style="font-size: 0.9em; line-height: 1.5;">
                      <strong>Familiar afectado:</strong> 
                      {{ item.parentezco && item.parentezco !== 'null' ? item.parentezco : 'No especificado' }}
                    </div>

                  </li>
                </ul>
                <div v-else class="text-muted font-italic">Sin antecedentes familiares de importancia.</div>
              </div>
            </div>
          </div>
          
          <div class="col-md-6">
            <div class="card mb-3 shadow-sm border-dark h-100">
              <div class="card-header bg-dark text-white font-weight-bold">
                <font-awesome-icon icon="file-medical"></font-awesome-icon> Antecedentes Personales Patológicos
              </div>
              <div class="card-body">
                <ul v-if="parsearLista(historialMedico.antecedentesPersonalesPatologicos).tiene" class="mb-0 list-unstyled">
                  <li v-for="(item, idx) in parsearLista(historialMedico.antecedentesPersonalesPatologicos).detalles" :key="idx" class="mb-3 border-bottom pb-2">
                    
                    <h6 class="font-weight-bold mb-1 text-dark">
                      {{ item.enfermedad || 'Enfermedad no especificada' }}
                    </h6>
                    
                    <div style="font-size: 0.9em; line-height: 1.5;" class="text-muted">
                      <font-awesome-icon icon="calendar-alt" class="mr-1"></font-awesome-icon>
                      <strong>Año aproximado:</strong> 
                      {{ item.anio || 'No especificado' }}
                    </div>

                  </li>
                </ul>
                <div v-else class="text-muted font-italic">Sin antecedentes patológicos previos.</div>
              </div>
            </div>
          </div>

          <div class="card mb-3 shadow-sm bg-light border-secondary">
            <div class="card-header bg-secondary text-white font-weight-bold">
              <font-awesome-icon icon="smoking"></font-awesome-icon> Hábitos, Consumo y Otros
            </div>
            <div class="card-body py-3" v-for="habitos in [obtenerJsonPlano(historialMedico.habitosConsumoOtros) || {}]" :key="'habitos'">
              <div class="row">
                
                <div class="col-md-4 mb-3">
                  <h6 class="font-weight-bold text-secondary border-bottom pb-1">Tabaco</h6>
                  <div style="font-size: 0.95em;">
                    <span :class="{'text-danger font-weight-bold': habitos.tabaco === 'Fuma actualmente'}">
                      {{ habitos.tabaco || 'No especificado' }}
                    </span>
                    <div v-if="habitos.tabaco === 'Fuma actualmente' && habitos.tabacoFrecuencia !== 'null'" class="text-muted mt-1 small">
                      <strong>Frecuencia:</strong> {{ habitos.tabacoFrecuencia }}
                    </div>
                  </div>
                </div>

                <div class="col-md-4 mb-3">
                  <h6 class="font-weight-bold text-secondary border-bottom pb-1">Alcohol</h6>
                  <div style="font-size: 0.95em;">
                    <span :class="{'text-warning font-weight-bold': habitos.alcohol === 'Regular', 'text-dark': habitos.alcohol === 'Ocasional'}">
                      {{ habitos.alcohol || 'No especificado' }}
                    </span>
                    <div v-if="(habitos.alcohol === 'Ocasional' || habitos.alcohol === 'Regular') && habitos.alcoholFrecuencia !== 'null'" class="text-muted mt-1 small">
                      <strong>Frecuencia:</strong> {{ habitos.alcoholFrecuencia }}
                    </div>
                  </div>
                </div>

                <div class="col-md-4 mb-3">
                  <h6 class="font-weight-bold text-secondary border-bottom pb-1">Drogas</h6>
                  <div style="font-size: 0.95em;">
                    <span :class="{'text-danger font-weight-bold': habitos.drogas === 'Sí'}">
                      {{ habitos.drogas || 'No especificado' }}
                    </span>
                    <div v-if="habitos.drogas === 'Sí'" class="text-muted mt-1 small">
                      <strong>Tipo:</strong> {{ habitos.drogasTipo || 'N/A' }} <br>
                      <strong>Frecuencia:</strong> {{ habitos.drogasFrecuencia !== 'null' ? habitos.drogasFrecuencia : 'N/A' }}
                    </div>
                  </div>
                </div>

                <div class="col-md-6 mb-2">
                  <h6 class="font-weight-bold text-secondary border-bottom pb-1">Vacunación</h6>
                  <div style="font-size: 0.95em;">
                    <span :class="{'text-success font-weight-bold': habitos.esquemaVacunacion === 'Completo', 'text-warning font-weight-bold': habitos.esquemaVacunacion === 'Incompleto'}">
                      Esquema: {{ habitos.esquemaVacunacion || 'No especificado' }}
                    </span>
                    <div v-if="habitos.vacunasRecientes" class="text-muted mt-1 small">
                      <strong>Recientes:</strong> {{ habitos.vacunasRecientes }}
                    </div>
                  </div>
                </div>

                <div class="col-md-6 mb-2">
                  <h6 class="font-weight-bold text-secondary border-bottom pb-1">Discapacidad</h6>
                  <div style="font-size: 0.95em;">
                    <span :class="{'text-info font-weight-bold': habitos.tieneDiscapacidad === 'Sí'}">
                      {{ habitos.tieneDiscapacidad || 'No especificado' }}
                    </span>
                    <div v-if="habitos.tieneDiscapacidad === 'Sí' && habitos.tipoDiscapacidad !== 'null'" class="text-muted mt-1 small">
                      <strong>Tipo:</strong> {{ habitos.tipoDiscapacidad }}
                    </div>
                  </div>
                </div>

              </div>
            </div>
          </div>

</div>
        <div class="card mb-4 shadow-sm">
          <div class="card-header bg-secondary text-white font-weight-bold">Observaciones Médicas Generales</div>
          <div class="card-body">
            <p style="white-space: pre-line;">{{ historialMedico.observacionesGenerales || 'No se registraron observaciones.' }}</p>
          </div>
        </div>
      </div>
      
      <div v-else>
        <div class="text-center mt-5">
          <div class="spinner-border text-primary" role="status"></div>
          <p class="mt-2">Cargando expediente...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./historial-medico-details.component.ts"></script>