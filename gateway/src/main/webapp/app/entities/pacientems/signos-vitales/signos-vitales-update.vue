<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsSignosVitales.home.createOrEditLabel"
          data-cy="SignosVitalesCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsSignosVitales.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="signosVitales.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="signosVitales.id" readonly />
          </div>

          <div class="card bg-light mb-4 mt-3 border-primary shadow-sm">
            <div class="card-body">
              <h5 class="card-title text-primary font-weight-bold"><font-awesome-icon icon="user-injured" /> Identificación del Paciente</h5>
              <div class="form-group mb-0 mt-3">
                <label class="form-control-label font-weight-bold" for="paciente-ecu">Número de ECU</label>
                <input
                  type="number"
                  class="form-control form-control-lg border-primary"
                  id="paciente-ecu"
                  v-model.number="signosVitales.pacienteEcu"
                  placeholder="Ej: 10045"
                  required
                />
              </div>
              
              <div v-if="pacienteEncontrado" class="alert alert-success mt-3 mb-0 d-flex align-items-center shadow-sm">
                <font-awesome-icon icon="check-circle" class="mr-2" style="font-size: 1.5em;"></font-awesome-icon>
                <div>
                  <strong>Paciente Vinculado:</strong><br>
                  {{ pacienteEncontrado.nombre }} {{ pacienteEncontrado.apellidoPaterno }}
                </div>
              </div>
              <div v-else-if="signosVitales.pacienteEcu" class="alert alert-warning mt-3 mb-0 d-flex align-items-center shadow-sm">
                <font-awesome-icon icon="exclamation-triangle" class="mr-2" style="font-size: 1.5em;"></font-awesome-icon>
                <div>
                  <strong>Atención:</strong> No se encontró ningún paciente con el ECU ingresado.
                </div>
              </div>
            </div>
          </div>

          <div class="card mb-4 shadow-sm border-secondary">
            <div class="card-header bg-secondary text-white font-weight-bold">
              <font-awesome-icon icon="clock" class="mr-2"></font-awesome-icon> Datos de la Toma
            </div>
            <div class="card-body">
              <div class="row">
                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold" v-text="t$('gatewayApp.pacientemsSignosVitales.fechaRegistro')" for="signos-vitales-fechaRegistro"></label>
                  <input
                    id="signos-vitales-fechaRegistro"
                    type="datetime-local"
                    class="form-control"
                    name="fechaRegistro"
                    :class="{ valid: !v$.fechaRegistro.$invalid, invalid: v$.fechaRegistro.$invalid }"
                    required
                    v-model="fechaRegistroLocal"
                  />
                  <small class="form-text text-muted">Hora actual autocompletada</small>
                </div>

                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold" for="signos-vitales-tipo">Tipo de Toma</label>
                  <select class="form-control" id="signos-vitales-tipo" v-model="v$.tipo.$model">
                    <option value="Ingreso">Ingreso (Triage)</option>
                    <option value="Rutina">Rutina (Piso/Cama)</option>
                    <option value="Urgencia">Urgencia (Choque)</option>
                    <option value="Alta">Alta Médica</option>
                  </select>
                </div>

                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold" for="signos-vitales-personal">Personal Responsable</label>
                  <input type="text" class="form-control" id="signos-vitales-personal" v-model="v$.personal.$model" placeholder="Ej. Dr. Pérez / Enf. Rodríguez" />
                </div>
              </div>
            </div>
          </div>

          <div class="card mb-4 shadow-sm border-danger">
            <div class="card-header bg-danger text-white font-weight-bold">
              <font-awesome-icon icon="wave-square" class="mr-2"></font-awesome-icon> Mediciones Vitales
            </div>
            <div class="card-body">
              <div class="row">
                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold" v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaCardiaca')"></label>
                  <div class="input-group">
                    <input type="number" min="0" max="300" class="form-control" v-model.number="v$.frecuenciaCardiaca.$model" placeholder="Ej: 75" />
                    <div class="input-group-append"><span class="input-group-text font-weight-bold">lpm</span></div>
                  </div>
                </div>

                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold" v-text="t$('gatewayApp.pacientemsSignosVitales.tensionArterial')"></label>
                  <input type="text" class="form-control text-center font-weight-bold" v-model="v$.tensionArterial.$model" placeholder="Sistólica / Diastólica (Ej: 120/80)" />
                </div>

                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold" v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaRespiratoria')"></label>
                  <div class="input-group">
                    <input type="number" min="0" max="100" class="form-control" v-model.number="v$.frecuenciaRespiratoria.$model" placeholder="Ej: 16" />
                    <div class="input-group-append"><span class="input-group-text font-weight-bold">rpm</span></div>
                  </div>
                </div>

                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold" v-text="t$('gatewayApp.pacientemsSignosVitales.temperatura')"></label>
                  <div class="input-group">
                    <input type="number" min="20" max="45" step="0.1" class="form-control" v-model.number="v$.temperatura.$model" placeholder="Ej: 36.5" />
                    <div class="input-group-append"><span class="input-group-text font-weight-bold">°C</span></div>
                  </div>
                </div>

                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold text-primary" v-text="t$('gatewayApp.pacientemsSignosVitales.saturacionOxigeno')"></label>
                  <div class="input-group border-primary rounded">
                    <input type="number" min="0" max="100" class="form-control" v-model.number="v$.saturacionOxigeno.$model" placeholder="Ej: 98" />
                    <div class="input-group-append"><span class="input-group-text bg-primary text-white font-weight-bold">%</span></div>
                  </div>
                </div>
                
                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold text-warning">Glucosa Capilar</label>
                  <div class="input-group border-warning rounded">
                    <input type="number" min="0" max="1000" class="form-control" v-model.number="v$.glucosa.$model" placeholder="Ej: 90" />
                    <div class="input-group-append"><span class="input-group-text bg-warning text-dark font-weight-bold">mg/dL</span></div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="card mb-4 shadow-sm border-info">
            <div class="card-header bg-info text-white font-weight-bold">
              <font-awesome-icon icon="clipboard-check" class="mr-2"></font-awesome-icon> Evaluación Extra
            </div>
            <div class="card-body">
              
              <div class="form-group mb-4">
                <label class="form-control-label font-weight-bold d-block text-center mb-3">Nivel de Dolor (0 = Sin dolor, 10 = Máximo dolor)</label>
                <div class="d-flex justify-content-between px-3 scale-container">
                  <div class="custom-control custom-radio custom-control-inline mx-0" v-for="n in 11" :key="n-1">
                    <input type="radio" :id="'dolor'+(n-1)" name="escalaDolor" :value="n-1" class="custom-control-input" v-model.number="v$.dolor.$model">
                    <label class="custom-control-label font-weight-bold" :for="'dolor'+(n-1)" :class="getColorDolor(n-1)">{{ n-1 }}</label>
                  </div>
                </div>
                <div class="d-flex justify-content-between px-3 mt-2 text-muted" style="font-size: 0.85em;">
                  <span>Sin dolor</span>
                  <span>Moderado</span>
                  <span>Intenso</span>
                  <span>Insuperable</span>
                </div>
              </div>

              <hr>

              <div class="row mt-4">
                <div class="form-group col-md-4">
                  <label class="form-control-label font-weight-bold">Estado de Conciencia</label>
                  <select class="form-control" v-model="v$.estadoConciencia.$model">
                    <option value="Alerta">Alerta (Consciente y orientado)</option>
                    <option value="Responde a Voz">Responde a Estímulo Verbal</option>
                    <option value="Responde a Dolor">Responde a Estímulo Doloroso</option>
                    <option value="Inconsciente">Inconsciente / Sin respuesta</option>
                  </select>
                </div>

                <div class="form-group col-md-8">
                  <label class="form-control-label font-weight-bold">Observaciones Generales</label>
                  <textarea class="form-control" rows="2" v-model="v$.observaciones.$model" placeholder="Ej: Paciente refiere mareo al levantarse de la cama..."></textarea>
                </div>
              </div>

            </div>
          </div>

        </div>
        <div class="mt-4 mb-5">
          <button type="button" id="cancel-save" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            :disabled="v$.$invalid || isSaving || !pacienteEncontrado"
            class="btn btn-primary ml-2 px-5 font-weight-bold shadow-sm"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts" src="./signos-vitales-update.component.ts"></script>

<style scoped>
/* Evita que los radio buttons se aplasten en pantallas pequeñas */
.scale-container {
  flex-wrap: wrap;
  gap: 10px;
}
.custom-control-label::before, 
.custom-control-label::after {
  width: 1.5rem;
  height: 1.5rem;
  top: -0.2rem;
  left: -2rem;
}
.custom-control-label {
  padding-left: 0.5rem;
  font-size: 1.1rem;
}
.custom-control-inline {
  margin-right: 0.5rem;
}
</style>