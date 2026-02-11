<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsMedicamento.home.createOrEditLabel"
          data-cy="MedicamentoCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsMedicamento.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="medicamento.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="medicamento.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsMedicamento.nombre')" for="medicamento-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="medicamento-nombre"
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
              v-text="t$('gatewayApp.pacientemsMedicamento.ingredienteActivo')"
              for="medicamento-ingredienteActivo"
            ></label>
            <input
              type="text"
              class="form-control"
              name="ingredienteActivo"
              id="medicamento-ingredienteActivo"
              data-cy="ingredienteActivo"
              :class="{ valid: !v$.ingredienteActivo.$invalid, invalid: v$.ingredienteActivo.$invalid }"
              v-model="v$.ingredienteActivo.$model"
            />
            <div v-if="v$.ingredienteActivo.$anyDirty && v$.ingredienteActivo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ingredienteActivo.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsMedicamento.presentacion')"
              for="medicamento-presentacion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="presentacion"
              id="medicamento-presentacion"
              data-cy="presentacion"
              :class="{ valid: !v$.presentacion.$invalid, invalid: v$.presentacion.$invalid }"
              v-model="v$.presentacion.$model"
            />
            <div v-if="v$.presentacion.$anyDirty && v$.presentacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.presentacion.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./medicamento-update.component.ts"></script>
