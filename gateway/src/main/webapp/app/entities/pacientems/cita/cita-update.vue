<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsCita.home.createOrEditLabel"
          data-cy="CitaCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsCita.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="cita.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="cita.id" readonly />
          </div>
          <div class="form-group">
<label class="form-control-label" for="cita-ecu">Número de ECU</label>            <input
              type="number"
              class="form-control"
              name="ecu"
              id="cita-ecu"
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
  <label class="form-control-label" for="cita-nombre">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="cita-nombre"
              data-cy="nombre"
              :readonly="pacienteEncontrado"
              :class="{ 'bg-light text-muted': pacienteEncontrado, 'is-invalid': v$.nombre.$error }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>

          <div class="form-group">
<label class="form-control-label" for="cita-apellidoPaterno">Apellido Paterno</label>
            <input
              type="text"
              class="form-control"
              name="apellidoPaterno"
              id="cita-apellidoPaterno"
              data-cy="apellidoPaterno"
              :readonly="pacienteEncontrado"
              :class="{ 'bg-light text-muted': pacienteEncontrado, 'is-invalid': v$.apellidoPaterno.$error }"
              v-model="v$.apellidoPaterno.$model"
              required
            />
            <div v-if="v$.apellidoPaterno.$anyDirty && v$.apellidoPaterno.$invalid">
              <small class="form-text text-danger" v-for="error of v$.apellidoPaterno.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>

          <div class="form-group">
<label class="form-control-label" for="cita-apellidoMaterno">Apellido Materno</label>
            <input
              type="text"
              class="form-control"
              name="apellidoMaterno"
              id="cita-apellidoMaterno"
              data-cy="apellidoMaterno"
              :readonly="pacienteEncontrado"
              :class="{ 'bg-light text-muted': pacienteEncontrado }"
              v-model="v$.apellidoMaterno.$model"
            />
          </div>
<div class="form-group">
<label class="form-control-label" for="cita-fechaHora">Fecha y Hora de la Cita</label>
            <div class="d-flex">
              <input
                id="cita-fechaHora"
                data-cy="fechaHora"
                type="datetime-local"
                class="form-control"
                name="fechaHora"
                v-model="fechaHoraLocal"
                :class="{ 'is-invalid': v$.fechaHora.$error }"
                required
              />
            </div>
            <div v-if="v$.fechaHora.$anyDirty && v$.fechaHora.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaHora.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsCita.motivo')" for="cita-motivo"></label>
            <input
              type="text"
              class="form-control"
              name="motivo"
              id="cita-motivo"
              data-cy="motivo"
              :class="{ valid: !v$.motivo.$invalid, invalid: v$.motivo.$invalid }"
              v-model="v$.motivo.$model"
              required
            />
            <div v-if="v$.motivo.$anyDirty && v$.motivo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.motivo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
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
<script lang="ts" src="./cita-update.component.ts"></script>
