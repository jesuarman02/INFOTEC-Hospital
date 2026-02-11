<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsPacienteMedicamento.home.createOrEditLabel"
          data-cy="PacienteMedicamentoCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsPacienteMedicamento.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="pacienteMedicamento.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="pacienteMedicamento.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteMedicamento.dosis')"
              for="paciente-medicamento-dosis"
            ></label>
            <input
              type="text"
              class="form-control"
              name="dosis"
              id="paciente-medicamento-dosis"
              data-cy="dosis"
              :class="{ valid: !v$.dosis.$invalid, invalid: v$.dosis.$invalid }"
              v-model="v$.dosis.$model"
            />
            <div v-if="v$.dosis.$anyDirty && v$.dosis.$invalid">
              <small class="form-text text-danger" v-for="error of v$.dosis.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteMedicamento.frecuencia')"
              for="paciente-medicamento-frecuencia"
            ></label>
            <input
              type="text"
              class="form-control"
              name="frecuencia"
              id="paciente-medicamento-frecuencia"
              data-cy="frecuencia"
              :class="{ valid: !v$.frecuencia.$invalid, invalid: v$.frecuencia.$invalid }"
              v-model="v$.frecuencia.$model"
            />
            <div v-if="v$.frecuencia.$anyDirty && v$.frecuencia.$invalid">
              <small class="form-text text-danger" v-for="error of v$.frecuencia.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteMedicamento.fechaInicio')"
              for="paciente-medicamento-fechaInicio"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="paciente-medicamento-fechaInicio"
                  v-model="v$.fechaInicio.$model"
                  name="fechaInicio"
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
                id="paciente-medicamento-fechaInicio"
                data-cy="fechaInicio"
                type="text"
                class="form-control"
                name="fechaInicio"
                :class="{ valid: !v$.fechaInicio.$invalid, invalid: v$.fechaInicio.$invalid }"
                v-model="v$.fechaInicio.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaInicio.$anyDirty && v$.fechaInicio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaInicio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteMedicamento.fechaFin')"
              for="paciente-medicamento-fechaFin"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="paciente-medicamento-fechaFin"
                  v-model="v$.fechaFin.$model"
                  name="fechaFin"
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
                id="paciente-medicamento-fechaFin"
                data-cy="fechaFin"
                type="text"
                class="form-control"
                name="fechaFin"
                :class="{ valid: !v$.fechaFin.$invalid, invalid: v$.fechaFin.$invalid }"
                v-model="v$.fechaFin.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteMedicamento.activo')"
              for="paciente-medicamento-activo"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="paciente-medicamento-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPacienteMedicamento.paciente')"
              for="paciente-medicamento-paciente"
            ></label>
            <select
              class="form-control"
              id="paciente-medicamento-paciente"
              data-cy="paciente"
              name="paciente"
              v-model="pacienteMedicamento.paciente"
            >
              <option :value="null"></option>
              <option
                :value="
                  pacienteMedicamento.paciente && pacienteOption.id === pacienteMedicamento.paciente.id
                    ? pacienteMedicamento.paciente
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
              v-text="t$('gatewayApp.pacientemsPacienteMedicamento.medicamento')"
              for="paciente-medicamento-medicamento"
            ></label>
            <select
              class="form-control"
              id="paciente-medicamento-medicamento"
              data-cy="medicamento"
              name="medicamento"
              v-model="pacienteMedicamento.medicamento"
            >
              <option :value="null"></option>
              <option
                :value="
                  pacienteMedicamento.medicamento && medicamentoOption.id === pacienteMedicamento.medicamento.id
                    ? pacienteMedicamento.medicamento
                    : medicamentoOption
                "
                v-for="medicamentoOption in medicamentos"
                :key="medicamentoOption.id"
              >
                {{ medicamentoOption.nombre }}
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
<script lang="ts" src="./paciente-medicamento-update.component.ts"></script>
