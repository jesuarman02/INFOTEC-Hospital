<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsPacienteAlergia.home.createOrEditLabel"
          data-cy="PacienteAlergiaCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsPacienteAlergia.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="pacienteAlergia.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="pacienteAlergia.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteAlergia.fechaDeteccion')"
              for="paciente-alergia-fechaDeteccion"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="paciente-alergia-fechaDeteccion"
                  v-model="v$.fechaDeteccion.$model"
                  name="fechaDeteccion"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="paciente-alergia-fechaDeteccion"
                data-cy="fechaDeteccion"
                type="text"
                class="form-control"
                name="fechaDeteccion"
                :class="{ valid: !v$.fechaDeteccion.$invalid, invalid: v$.fechaDeteccion.$invalid }"
                v-model="v$.fechaDeteccion.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteAlergia.reaccion')"
              for="paciente-alergia-reaccion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="reaccion"
              id="paciente-alergia-reaccion"
              data-cy="reaccion"
              :class="{ valid: !v$.reaccion.$invalid, invalid: v$.reaccion.$invalid }"
              v-model="v$.reaccion.$model"
            />
            <div v-if="v$.reaccion.$anyDirty && v$.reaccion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.reaccion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteAlergia.gravedad')"
              for="paciente-alergia-gravedad"
            ></label>
            <select
              class="form-control"
              name="gravedad"
              :class="{ valid: !v$.gravedad.$invalid, invalid: v$.gravedad.$invalid }"
              v-model="v$.gravedad.$model"
              id="paciente-alergia-gravedad"
              data-cy="gravedad"
              required
            >
              <option
                v-for="gradoAlergia in gradoAlergiaValues"
                :key="gradoAlergia"
                :value="gradoAlergia"
                :label="t$('gatewayApp.GradoAlergia.' + gradoAlergia)"
              >
                {{ gradoAlergia }}
              </option>
            </select>
            <div v-if="v$.gravedad.$anyDirty && v$.gravedad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.gravedad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteAlergia.paciente')"
              for="paciente-alergia-paciente"
            ></label>
            <select
              class="form-control"
              id="paciente-alergia-paciente"
              data-cy="paciente"
              name="paciente"
              v-model="pacienteAlergia.paciente"
            >
              <option :value="null"></option>
              <option
                :value="
                  pacienteAlergia.paciente && pacienteOption.id === pacienteAlergia.paciente.id ? pacienteAlergia.paciente : pacienteOption
                "
                v-for="pacienteOption in pacientes"
                :key="pacienteOption.id"
              >
                {{ pacienteOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteAlergia.alergia')"
              for="paciente-alergia-alergia"
            ></label>
            <select class="form-control" id="paciente-alergia-alergia" data-cy="alergia" name="alergia" v-model="pacienteAlergia.alergia">
              <option :value="null"></option>
              <option
                :value="
                  pacienteAlergia.alergia && alergiaOption.id === pacienteAlergia.alergia.id ? pacienteAlergia.alergia : alergiaOption
                "
                v-for="alergiaOption in alergias"
                :key="alergiaOption.id"
              >
                {{ alergiaOption.nombre }}
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
<script lang="ts" src="./paciente-alergia-update.component.ts"></script>
