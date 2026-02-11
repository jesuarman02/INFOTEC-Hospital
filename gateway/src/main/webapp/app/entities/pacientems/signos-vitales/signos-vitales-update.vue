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
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsSignosVitales.fechaRegistro')"
              for="signos-vitales-fechaRegistro"
            ></label>
            <div class="d-flex">
              <input
                id="signos-vitales-fechaRegistro"
                data-cy="fechaRegistro"
                type="datetime-local"
                class="form-control"
                name="fechaRegistro"
                :class="{ valid: !v$.fechaRegistro.$invalid, invalid: v$.fechaRegistro.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.fechaRegistro.$model)"
                @change="updateInstantField('fechaRegistro', $event)"
              />
            </div>
            <div v-if="v$.fechaRegistro.$anyDirty && v$.fechaRegistro.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaRegistro.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaCardiaca')"
              for="signos-vitales-frecuenciaCardiaca"
            ></label>
            <input
              type="number"
              class="form-control"
              name="frecuenciaCardiaca"
              id="signos-vitales-frecuenciaCardiaca"
              data-cy="frecuenciaCardiaca"
              :class="{ valid: !v$.frecuenciaCardiaca.$invalid, invalid: v$.frecuenciaCardiaca.$invalid }"
              v-model.number="v$.frecuenciaCardiaca.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsSignosVitales.tensionArterial')"
              for="signos-vitales-tensionArterial"
            ></label>
            <input
              type="text"
              class="form-control"
              name="tensionArterial"
              id="signos-vitales-tensionArterial"
              data-cy="tensionArterial"
              :class="{ valid: !v$.tensionArterial.$invalid, invalid: v$.tensionArterial.$invalid }"
              v-model="v$.tensionArterial.$model"
            />
            <div v-if="v$.tensionArterial.$anyDirty && v$.tensionArterial.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tensionArterial.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaRespiratoria')"
              for="signos-vitales-frecuenciaRespiratoria"
            ></label>
            <input
              type="number"
              class="form-control"
              name="frecuenciaRespiratoria"
              id="signos-vitales-frecuenciaRespiratoria"
              data-cy="frecuenciaRespiratoria"
              :class="{ valid: !v$.frecuenciaRespiratoria.$invalid, invalid: v$.frecuenciaRespiratoria.$invalid }"
              v-model.number="v$.frecuenciaRespiratoria.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsSignosVitales.temperatura')"
              for="signos-vitales-temperatura"
            ></label>
            <input
              type="number"
              class="form-control"
              name="temperatura"
              id="signos-vitales-temperatura"
              data-cy="temperatura"
              :class="{ valid: !v$.temperatura.$invalid, invalid: v$.temperatura.$invalid }"
              v-model.number="v$.temperatura.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsSignosVitales.saturacionOxigeno')"
              for="signos-vitales-saturacionOxigeno"
            ></label>
            <input
              type="number"
              class="form-control"
              name="saturacionOxigeno"
              id="signos-vitales-saturacionOxigeno"
              data-cy="saturacionOxigeno"
              :class="{ valid: !v$.saturacionOxigeno.$invalid, invalid: v$.saturacionOxigeno.$invalid }"
              v-model.number="v$.saturacionOxigeno.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsSignosVitales.peso')" for="signos-vitales-peso"></label>
            <input
              type="number"
              class="form-control"
              name="peso"
              id="signos-vitales-peso"
              data-cy="peso"
              :class="{ valid: !v$.peso.$invalid, invalid: v$.peso.$invalid }"
              v-model.number="v$.peso.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsSignosVitales.estatura')"
              for="signos-vitales-estatura"
            ></label>
            <input
              type="number"
              class="form-control"
              name="estatura"
              id="signos-vitales-estatura"
              data-cy="estatura"
              :class="{ valid: !v$.estatura.$invalid, invalid: v$.estatura.$invalid }"
              v-model.number="v$.estatura.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsSignosVitales.imc')" for="signos-vitales-imc"></label>
            <input
              type="number"
              class="form-control"
              name="imc"
              id="signos-vitales-imc"
              data-cy="imc"
              :class="{ valid: !v$.imc.$invalid, invalid: v$.imc.$invalid }"
              v-model.number="v$.imc.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsSignosVitales.paciente')"
              for="signos-vitales-paciente"
            ></label>
            <select class="form-control" id="signos-vitales-paciente" data-cy="paciente" name="paciente" v-model="signosVitales.paciente">
              <option :value="null"></option>
              <option
                :value="signosVitales.paciente && pacienteOption.id === signosVitales.paciente.id ? signosVitales.paciente : pacienteOption"
                v-for="pacienteOption in pacientes"
                :key="pacienteOption.id"
              >
                {{ pacienteOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./signos-vitales-update.component.ts"></script>
