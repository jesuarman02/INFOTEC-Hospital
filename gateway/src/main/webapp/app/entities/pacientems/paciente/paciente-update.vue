<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsPaciente.home.createOrEditLabel"
          data-cy="PacienteCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsPaciente.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="paciente.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="paciente.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.ecu')" for="paciente-ecu"></label>
            <input
              type="number"
              class="form-control"
              name="ecu"
              id="paciente-ecu"
              data-cy="ecu"
              :class="{ valid: !v$.ecu.$invalid, invalid: v$.ecu.$invalid }"
              v-model.number="v$.ecu.$model"
              required
            />
            <div v-if="v$.ecu.$anyDirty && v$.ecu.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ecu.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.nombre')" for="paciente-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="paciente-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPaciente.apellidoPaterno')"
              for="paciente-apellidoPaterno"
            ></label>
            <input
              type="text"
              class="form-control"
              name="apellidoPaterno"
              id="paciente-apellidoPaterno"
              data-cy="apellidoPaterno"
              :class="{ valid: !v$.apellidoPaterno.$invalid, invalid: v$.apellidoPaterno.$invalid }"
              v-model="v$.apellidoPaterno.$model"
              required
            />
            <div v-if="v$.apellidoPaterno.$anyDirty && v$.apellidoPaterno.$invalid">
              <small class="form-text text-danger" v-for="error of v$.apellidoPaterno.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPaciente.apellidoMaterno')"
              for="paciente-apellidoMaterno"
            ></label>
            <input
              type="text"
              class="form-control"
              name="apellidoMaterno"
              id="paciente-apellidoMaterno"
              data-cy="apellidoMaterno"
              :class="{ valid: !v$.apellidoMaterno.$invalid, invalid: v$.apellidoMaterno.$invalid }"
              v-model="v$.apellidoMaterno.$model"
            />
            <div v-if="v$.apellidoMaterno.$anyDirty && v$.apellidoMaterno.$invalid">
              <small class="form-text text-danger" v-for="error of v$.apellidoMaterno.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.sexo')" for="paciente-sexo"></label>
            <select
              class="form-control"
              name="sexo"
              :class="{ valid: !v$.sexo.$invalid, invalid: v$.sexo.$invalid }"
              v-model="v$.sexo.$model"
              id="paciente-sexo"
              data-cy="sexo"
              required
            >
              <option v-for="sexo in sexoValues" :key="sexo" :value="sexo" :label="t$('gatewayApp.Sexo.' + sexo)">{{ sexo }}</option>
            </select>
            <div v-if="v$.sexo.$anyDirty && v$.sexo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sexo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.nacionalidad')" for="paciente-nacionalidad"></label>
            <input
              type="text"
              class="form-control"
              name="nacionalidad"
              id="paciente-nacionalidad"
              data-cy="nacionalidad"
              :class="{ valid: !v$.nacionalidad.$invalid, invalid: v$.nacionalidad.$invalid }"
              v-model="v$.nacionalidad.$model"
            />
            <div v-if="v$.nacionalidad.$anyDirty && v$.nacionalidad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nacionalidad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPaciente.fechaNacimiento')"
              for="paciente-fechaNacimiento"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="paciente-fechaNacimiento"
                  v-model="v$.fechaNacimiento.$model"
                  name="fechaNacimiento"
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
                id="paciente-fechaNacimiento"
                data-cy="fechaNacimiento"
                type="text"
                class="form-control"
                name="fechaNacimiento"
                :class="{ valid: !v$.fechaNacimiento.$invalid, invalid: v$.fechaNacimiento.$invalid }"
                v-model="v$.fechaNacimiento.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaNacimiento.$anyDirty && v$.fechaNacimiento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaNacimiento.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.estadoCivil')" for="paciente-estadoCivil"></label>
            <input
              type="text"
              class="form-control"
              name="estadoCivil"
              id="paciente-estadoCivil"
              data-cy="estadoCivil"
              :class="{ valid: !v$.estadoCivil.$invalid, invalid: v$.estadoCivil.$invalid }"
              v-model="v$.estadoCivil.$model"
            />
            <div v-if="v$.estadoCivil.$anyDirty && v$.estadoCivil.$invalid">
              <small class="form-text text-danger" v-for="error of v$.estadoCivil.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.curp')" for="paciente-curp"></label>
            <input
              type="text"
              class="form-control"
              name="curp"
              id="paciente-curp"
              data-cy="curp"
              :class="{ valid: !v$.curp.$invalid, invalid: v$.curp.$invalid }"
              v-model="v$.curp.$model"
              required
            />
            <div v-if="v$.curp.$anyDirty && v$.curp.$invalid">
              <small class="form-text text-danger" v-for="error of v$.curp.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.direccion')" for="paciente-direccion"></label>
            <select class="form-control" id="paciente-direccion" data-cy="direccion" name="direccion" v-model="paciente.direccion">
              <option :value="null"></option>
              <option
                :value="paciente.direccion && direccionOption.id === paciente.direccion.id ? paciente.direccion : direccionOption"
                v-for="direccionOption in direccions"
                :key="direccionOption.id"
              >
                {{ direccionOption.nombreVialidad }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPaciente.infoSocioeconomica')"
              for="paciente-infoSocioeconomica"
            ></label>
            <select
              class="form-control"
              id="paciente-infoSocioeconomica"
              data-cy="infoSocioeconomica"
              name="infoSocioeconomica"
              v-model="paciente.infoSocioeconomica"
            >
              <option :value="null"></option>
              <option
                :value="
                  paciente.infoSocioeconomica && infoSocioeconomicaOption.id === paciente.infoSocioeconomica.id
                    ? paciente.infoSocioeconomica
                    : infoSocioeconomicaOption
                "
                v-for="infoSocioeconomicaOption in infoSocioeconomicas"
                :key="infoSocioeconomicaOption.id"
              >
                {{ infoSocioeconomicaOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPaciente.historialGeneral')"
              for="paciente-historialGeneral"
            ></label>
            <select
              class="form-control"
              id="paciente-historialGeneral"
              data-cy="historialGeneral"
              name="historialGeneral"
              v-model="paciente.historialGeneral"
            >
              <option :value="null"></option>
              <option
                :value="
                  paciente.historialGeneral && historialMedicoOption.id === paciente.historialGeneral.id
                    ? paciente.historialGeneral
                    : historialMedicoOption
                "
                v-for="historialMedicoOption in historialMedicos"
                :key="historialMedicoOption.id"
              >
                {{ historialMedicoOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPaciente.entidadNacimiento')"
              for="paciente-entidadNacimiento"
            ></label>
            <select
              class="form-control"
              id="paciente-entidadNacimiento"
              data-cy="entidadNacimiento"
              name="entidadNacimiento"
              v-model="paciente.entidadNacimiento"
            >
              <option :value="null"></option>
              <option
                :value="
                  paciente.entidadNacimiento && entidadFederativaOption.id === paciente.entidadNacimiento.id
                    ? paciente.entidadNacimiento
                    : entidadFederativaOption
                "
                v-for="entidadFederativaOption in entidadFederativas"
                :key="entidadFederativaOption.id"
              >
                {{ entidadFederativaOption.nombre }}
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
<script lang="ts" src="./paciente-update.component.ts"></script>
