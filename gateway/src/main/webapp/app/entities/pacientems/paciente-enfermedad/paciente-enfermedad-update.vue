<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsPacienteEnfermedad.home.createOrEditLabel"
          data-cy="PacienteEnfermedadCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="pacienteEnfermedad.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="pacienteEnfermedad.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.fechaDiagnostico')"
              for="paciente-enfermedad-fechaDiagnostico"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="paciente-enfermedad-fechaDiagnostico"
                  v-model="v$.fechaDiagnostico.$model"
                  name="fechaDiagnostico"
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
                id="paciente-enfermedad-fechaDiagnostico"
                data-cy="fechaDiagnostico"
                type="text"
                class="form-control"
                name="fechaDiagnostico"
                :class="{ valid: !v$.fechaDiagnostico.$invalid, invalid: v$.fechaDiagnostico.$invalid }"
                v-model="v$.fechaDiagnostico.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaDiagnostico.$anyDirty && v$.fechaDiagnostico.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaDiagnostico.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.estado')"
              for="paciente-enfermedad-estado"
            ></label>
            <input
              type="text"
              class="form-control"
              name="estado"
              id="paciente-enfermedad-estado"
              data-cy="estado"
              :class="{ valid: !v$.estado.$invalid, invalid: v$.estado.$invalid }"
              v-model="v$.estado.$model"
            />
            <div v-if="v$.estado.$anyDirty && v$.estado.$invalid">
              <small class="form-text text-danger" v-for="error of v$.estado.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.notas')"
              for="paciente-enfermedad-notas"
            ></label>
            <input
              type="text"
              class="form-control"
              name="notas"
              id="paciente-enfermedad-notas"
              data-cy="notas"
              :class="{ valid: !v$.notas.$invalid, invalid: v$.notas.$invalid }"
              v-model="v$.notas.$model"
            />
            <div v-if="v$.notas.$anyDirty && v$.notas.$invalid">
              <small class="form-text text-danger" v-for="error of v$.notas.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.paciente')"
              for="paciente-enfermedad-paciente"
            ></label>
            <select
              class="form-control"
              id="paciente-enfermedad-paciente"
              data-cy="paciente"
              name="paciente"
              v-model="pacienteEnfermedad.paciente"
            >
              <option :value="null"></option>
              <option
                :value="
                  pacienteEnfermedad.paciente && pacienteOption.id === pacienteEnfermedad.paciente.id
                    ? pacienteEnfermedad.paciente
                    : pacienteOption
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
              v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.enfermedad')"
              for="paciente-enfermedad-enfermedad"
            ></label>
            <select
              class="form-control"
              id="paciente-enfermedad-enfermedad"
              data-cy="enfermedad"
              name="enfermedad"
              v-model="pacienteEnfermedad.enfermedad"
            >
              <option :value="null"></option>
              <option
                :value="
                  pacienteEnfermedad.enfermedad && enfermedadOption.id === pacienteEnfermedad.enfermedad.id
                    ? pacienteEnfermedad.enfermedad
                    : enfermedadOption
                "
                v-for="enfermedadOption in enfermedads"
                :key="enfermedadOption.id"
              >
                {{ enfermedadOption.nombre }}
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
<script lang="ts" src="./paciente-enfermedad-update.component.ts"></script>
