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

          <div class="card bg-light mb-4 mt-3">
            <div class="card-body">
              <h5 class="card-title text-primary"><font-awesome-icon icon="user-injured" /> Identificación del Paciente</h5>
              <div class="form-group mb-0">
                <label class="form-control-label font-weight-bold" for="paciente-ecu">Número de ECU</label>
                <input
                  type="number"
                  class="form-control form-control-lg"
                  id="paciente-ecu"
                  v-model.number="signosVitales.pacienteEcu"
                  placeholder="Ingrese el ECU para vincular al paciente..."
                  required
                />
              </div>
              
              <div v-if="pacienteEncontrado" class="alert alert-success mt-3 mb-0">
                <strong>Paciente Encontrado:</strong> {{ pacienteEncontrado.nombre }} {{ pacienteEncontrado.apellidoPaterno }}
              </div>
              <div v-else-if="signosVitales.pacienteEcu" class="alert alert-warning mt-3 mb-0">
                <strong>Atención:</strong> No se encontró ningún paciente con este ECU en la base de datos.
              </div>
            </div>
          </div>

          <hr />
          <h4>Datos de la Toma</h4>

          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsSignosVitales.fechaRegistro')" for="signos-vitales-fechaRegistro"></label>
            <div class="d-flex">
              <input
                id="signos-vitales-fechaRegistro"
                type="datetime-local"
                class="form-control"
                name="fechaRegistro"
                :class="{ valid: !v$.fechaRegistro.$invalid, invalid: v$.fechaRegistro.$invalid }"
                required
                v-model="fechaRegistroLocal"
              />
            </div>
          </div>

          <div class="row">
            <div class="form-group col-md-6">
              <label class="form-control-label" for="signos-vitales-tipo">Tipo de Toma</label>
              <select class="form-control" id="signos-vitales-tipo" v-model="v$.tipo.$model">
                <option value="Ingreso">Ingreso</option>
                <option value="Rutina">Rutina (Piso)</option>
                <option value="Urgencia">Urgencia</option>
                <option value="Alta">Alta Médica</option>
              </select>
            </div>

            <div class="form-group col-md-6">
              <label class="form-control-label" for="signos-vitales-personal">Personal Responsable</label>
              <input type="text" class="form-control" id="signos-vitales-personal" v-model="v$.personal.$model" placeholder="Ej. Dra. Martínez" />
            </div>
          </div>

          <hr />
          <h4>Mediciones Vitales</h4>

          <div class="row">
            <div class="form-group col-md-6">
              <label class="form-control-label" v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaCardiaca')"></label>
              <div class="input-group">
                <input type="number" min="0" max="300" class="form-control" v-model.number="v$.frecuenciaCardiaca.$model" />
                <div class="input-group-append"><span class="input-group-text">lpm</span></div>
              </div>
            </div>

            <div class="form-group col-md-6">
              <label class="form-control-label" v-text="t$('gatewayApp.pacientemsSignosVitales.tensionArterial')"></label>
              <input type="text" class="form-control" v-model="v$.tensionArterial.$model" placeholder="120/80" />
            </div>

            <div class="form-group col-md-6">
              <label class="form-control-label" v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaRespiratoria')"></label>
              <div class="input-group">
                <input type="number" min="0" max="100" class="form-control" v-model.number="v$.frecuenciaRespiratoria.$model" />
                <div class="input-group-append"><span class="input-group-text">rpm</span></div>
              </div>
            </div>

            <div class="form-group col-md-6">
              <label class="form-control-label" v-text="t$('gatewayApp.pacientemsSignosVitales.temperatura')"></label>
              <div class="input-group">
                <input type="number" min="20" max="45" step="0.1" class="form-control" v-model.number="v$.temperatura.$model" />
                <div class="input-group-append"><span class="input-group-text">°C</span></div>
              </div>
            </div>

            <div class="form-group col-md-6">
              <label class="form-control-label" v-text="t$('gatewayApp.pacientemsSignosVitales.saturacionOxigeno')"></label>
              <div class="input-group">
                <input type="number" min="0" max="100" class="form-control" v-model.number="v$.saturacionOxigeno.$model" />
                <div class="input-group-append"><span class="input-group-text">%</span></div>
              </div>
            </div>
            
            <div class="form-group col-md-6">
              <label class="form-control-label">Glucosa</label>
              <div class="input-group">
                <input type="number" min="0" max="1000" class="form-control" v-model.number="v$.glucosa.$model" />
                <div class="input-group-append"><span class="input-group-text">mg/dL</span></div>
              </div>
            </div>
          </div>

          <hr />
          <h4>Evaluación Extra</h4>

          <div class="row">
            <div class="form-group col-md-6">
              <label class="form-control-label">Dolor (Escala 0-10)</label>
              <input type="number" min="0" max="10" class="form-control" v-model.number="v$.dolor.$model" />
            </div>

            <div class="form-group col-md-6">
              <label class="form-control-label">Estado de Conciencia</label>
              <select class="form-control" v-model="v$.estadoConciencia.$model">
                <option value="Alerta">Alerta</option>
                <option value="Responde a Voz">Responde a Voz</option>
                <option value="Responde a Dolor">Responde a Dolor</option>
                <option value="Inconsciente">Inconsciente</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label class="form-control-label">Observaciones</label>
            <textarea class="form-control" rows="3" v-model="v$.observaciones.$model"></textarea>
          </div>

        </div>
        <div>
          <button type="button" id="cancel-save" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            :disabled="v$.$invalid || isSaving || !pacienteEncontrado"
            class="btn btn-primary ml-2"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./signos-vitales-update.component.ts"></script>