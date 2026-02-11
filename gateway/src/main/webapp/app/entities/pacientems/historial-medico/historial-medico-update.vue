<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsHistorialMedico.home.createOrEditLabel"
          data-cy="HistorialMedicoCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsHistorialMedico.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="historialMedico.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="historialMedico.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsHistorialMedico.antecedentesQuirurgicos')"
              for="historial-medico-antecedentesQuirurgicos"
            ></label>
            <input
              type="text"
              class="form-control"
              name="antecedentesQuirurgicos"
              id="historial-medico-antecedentesQuirurgicos"
              data-cy="antecedentesQuirurgicos"
              :class="{ valid: !v$.antecedentesQuirurgicos.$invalid, invalid: v$.antecedentesQuirurgicos.$invalid }"
              v-model="v$.antecedentesQuirurgicos.$model"
            />
            <div v-if="v$.antecedentesQuirurgicos.$anyDirty && v$.antecedentesQuirurgicos.$invalid">
              <small class="form-text text-danger" v-for="error of v$.antecedentesQuirurgicos.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsHistorialMedico.esquemaVacunacion')"
              for="historial-medico-esquemaVacunacion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="esquemaVacunacion"
              id="historial-medico-esquemaVacunacion"
              data-cy="esquemaVacunacion"
              :class="{ valid: !v$.esquemaVacunacion.$invalid, invalid: v$.esquemaVacunacion.$invalid }"
              v-model="v$.esquemaVacunacion.$model"
            />
            <div v-if="v$.esquemaVacunacion.$anyDirty && v$.esquemaVacunacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.esquemaVacunacion.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsHistorialMedico.habitos')"
              for="historial-medico-habitos"
            ></label>
            <input
              type="text"
              class="form-control"
              name="habitos"
              id="historial-medico-habitos"
              data-cy="habitos"
              :class="{ valid: !v$.habitos.$invalid, invalid: v$.habitos.$invalid }"
              v-model="v$.habitos.$model"
            />
            <div v-if="v$.habitos.$anyDirty && v$.habitos.$invalid">
              <small class="form-text text-danger" v-for="error of v$.habitos.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsHistorialMedico.observacionesGenerales')"
              for="historial-medico-observacionesGenerales"
            ></label>
            <textarea
              class="form-control"
              name="observacionesGenerales"
              id="historial-medico-observacionesGenerales"
              data-cy="observacionesGenerales"
              :class="{ valid: !v$.observacionesGenerales.$invalid, invalid: v$.observacionesGenerales.$invalid }"
              v-model="v$.observacionesGenerales.$model"
            ></textarea>
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
<script lang="ts" src="./historial-medico-update.component.ts"></script>
