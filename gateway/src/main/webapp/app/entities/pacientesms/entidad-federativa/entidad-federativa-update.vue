<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientesmsEntidadFederativa.home.createOrEditLabel"
          data-cy="EntidadFederativaCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientesmsEntidadFederativa.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="entidadFederativa.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="entidadFederativa.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsEntidadFederativa.clave')"
              for="entidad-federativa-clave"
            ></label>
            <input
              type="text"
              class="form-control"
              name="clave"
              id="entidad-federativa-clave"
              data-cy="clave"
              :class="{ valid: !v$.clave.$invalid, invalid: v$.clave.$invalid }"
              v-model="v$.clave.$model"
              required
            />
            <div v-if="v$.clave.$anyDirty && v$.clave.$invalid">
              <small class="form-text text-danger" v-for="error of v$.clave.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsEntidadFederativa.nombre')"
              for="entidad-federativa-nombre"
            ></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="entidad-federativa-nombre"
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
              v-text="t$('gatewayApp.pacientesmsEntidadFederativa.abreviatura')"
              for="entidad-federativa-abreviatura"
            ></label>
            <input
              type="text"
              class="form-control"
              name="abreviatura"
              id="entidad-federativa-abreviatura"
              data-cy="abreviatura"
              :class="{ valid: !v$.abreviatura.$invalid, invalid: v$.abreviatura.$invalid }"
              v-model="v$.abreviatura.$model"
            />
            <div v-if="v$.abreviatura.$anyDirty && v$.abreviatura.$invalid">
              <small class="form-text text-danger" v-for="error of v$.abreviatura.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./entidad-federativa-update.component.ts"></script>
