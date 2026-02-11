<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsEnfermedad.home.createOrEditLabel"
          data-cy="EnfermedadCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsEnfermedad.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="enfermedad.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="enfermedad.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsEnfermedad.nombre')" for="enfermedad-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="enfermedad-nombre"
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
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsEnfermedad.tipo')" for="enfermedad-tipo"></label>
            <input
              type="text"
              class="form-control"
              name="tipo"
              id="enfermedad-tipo"
              data-cy="tipo"
              :class="{ valid: !v$.tipo.$invalid, invalid: v$.tipo.$invalid }"
              v-model="v$.tipo.$model"
            />
            <div v-if="v$.tipo.$anyDirty && v$.tipo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsEnfermedad.codigoCIE')" for="enfermedad-codigoCIE"></label>
            <input
              type="text"
              class="form-control"
              name="codigoCIE"
              id="enfermedad-codigoCIE"
              data-cy="codigoCIE"
              :class="{ valid: !v$.codigoCIE.$invalid, invalid: v$.codigoCIE.$invalid }"
              v-model="v$.codigoCIE.$model"
            />
            <div v-if="v$.codigoCIE.$anyDirty && v$.codigoCIE.$invalid">
              <small class="form-text text-danger" v-for="error of v$.codigoCIE.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./enfermedad-update.component.ts"></script>
