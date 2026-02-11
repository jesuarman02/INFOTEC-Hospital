<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientesmsCodigoPostal.home.createOrEditLabel"
          data-cy="CodigoPostalCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientesmsCodigoPostal.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="codigoPostal.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="codigoPostal.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientesmsCodigoPostal.codigo')" for="codigo-postal-codigo"></label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="codigo-postal-codigo"
              data-cy="codigo"
              :class="{ valid: !v$.codigo.$invalid, invalid: v$.codigo.$invalid }"
              v-model="v$.codigo.$model"
              required
            />
            <div v-if="v$.codigo.$anyDirty && v$.codigo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.codigo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.asentamiento')"
              for="codigo-postal-asentamiento"
            ></label>
            <input
              type="text"
              class="form-control"
              name="asentamiento"
              id="codigo-postal-asentamiento"
              data-cy="asentamiento"
              :class="{ valid: !v$.asentamiento.$invalid, invalid: v$.asentamiento.$invalid }"
              v-model="v$.asentamiento.$model"
            />
            <div v-if="v$.asentamiento.$anyDirty && v$.asentamiento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.asentamiento.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.tipoAsentamiento')"
              for="codigo-postal-tipoAsentamiento"
            ></label>
            <input
              type="text"
              class="form-control"
              name="tipoAsentamiento"
              id="codigo-postal-tipoAsentamiento"
              data-cy="tipoAsentamiento"
              :class="{ valid: !v$.tipoAsentamiento.$invalid, invalid: v$.tipoAsentamiento.$invalid }"
              v-model="v$.tipoAsentamiento.$model"
            />
            <div v-if="v$.tipoAsentamiento.$anyDirty && v$.tipoAsentamiento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipoAsentamiento.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.municipio')"
              for="codigo-postal-municipio"
            ></label>
            <input
              type="text"
              class="form-control"
              name="municipio"
              id="codigo-postal-municipio"
              data-cy="municipio"
              :class="{ valid: !v$.municipio.$invalid, invalid: v$.municipio.$invalid }"
              v-model="v$.municipio.$model"
            />
            <div v-if="v$.municipio.$anyDirty && v$.municipio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.municipio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientesmsCodigoPostal.estado')" for="codigo-postal-estado"></label>
            <input
              type="text"
              class="form-control"
              name="estado"
              id="codigo-postal-estado"
              data-cy="estado"
              :class="{ valid: !v$.estado.$invalid, invalid: v$.estado.$invalid }"
              v-model="v$.estado.$model"
            />
            <div v-if="v$.estado.$anyDirty && v$.estado.$invalid">
              <small class="form-text text-danger" v-for="error of v$.estado.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientesmsCodigoPostal.ciudad')" for="codigo-postal-ciudad"></label>
            <input
              type="text"
              class="form-control"
              name="ciudad"
              id="codigo-postal-ciudad"
              data-cy="ciudad"
              :class="{ valid: !v$.ciudad.$invalid, invalid: v$.ciudad.$invalid }"
              v-model="v$.ciudad.$model"
            />
            <div v-if="v$.ciudad.$anyDirty && v$.ciudad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ciudad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.codigoPostalAdministracion')"
              for="codigo-postal-codigoPostalAdministracion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="codigoPostalAdministracion"
              id="codigo-postal-codigoPostalAdministracion"
              data-cy="codigoPostalAdministracion"
              :class="{ valid: !v$.codigoPostalAdministracion.$invalid, invalid: v$.codigoPostalAdministracion.$invalid }"
              v-model="v$.codigoPostalAdministracion.$model"
            />
            <div v-if="v$.codigoPostalAdministracion.$anyDirty && v$.codigoPostalAdministracion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.codigoPostalAdministracion.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveEstado')"
              for="codigo-postal-claveEstado"
            ></label>
            <input
              type="text"
              class="form-control"
              name="claveEstado"
              id="codigo-postal-claveEstado"
              data-cy="claveEstado"
              :class="{ valid: !v$.claveEstado.$invalid, invalid: v$.claveEstado.$invalid }"
              v-model="v$.claveEstado.$model"
            />
            <div v-if="v$.claveEstado.$anyDirty && v$.claveEstado.$invalid">
              <small class="form-text text-danger" v-for="error of v$.claveEstado.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveOficina')"
              for="codigo-postal-claveOficina"
            ></label>
            <input
              type="text"
              class="form-control"
              name="claveOficina"
              id="codigo-postal-claveOficina"
              data-cy="claveOficina"
              :class="{ valid: !v$.claveOficina.$invalid, invalid: v$.claveOficina.$invalid }"
              v-model="v$.claveOficina.$model"
            />
            <div v-if="v$.claveOficina.$anyDirty && v$.claveOficina.$invalid">
              <small class="form-text text-danger" v-for="error of v$.claveOficina.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveCP')" for="codigo-postal-claveCP"></label>
            <input
              type="text"
              class="form-control"
              name="claveCP"
              id="codigo-postal-claveCP"
              data-cy="claveCP"
              :class="{ valid: !v$.claveCP.$invalid, invalid: v$.claveCP.$invalid }"
              v-model="v$.claveCP.$model"
            />
            <div v-if="v$.claveCP.$anyDirty && v$.claveCP.$invalid">
              <small class="form-text text-danger" v-for="error of v$.claveCP.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveTipoAsentamiento')"
              for="codigo-postal-claveTipoAsentamiento"
            ></label>
            <input
              type="text"
              class="form-control"
              name="claveTipoAsentamiento"
              id="codigo-postal-claveTipoAsentamiento"
              data-cy="claveTipoAsentamiento"
              :class="{ valid: !v$.claveTipoAsentamiento.$invalid, invalid: v$.claveTipoAsentamiento.$invalid }"
              v-model="v$.claveTipoAsentamiento.$model"
            />
            <div v-if="v$.claveTipoAsentamiento.$anyDirty && v$.claveTipoAsentamiento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.claveTipoAsentamiento.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveMunicipio')"
              for="codigo-postal-claveMunicipio"
            ></label>
            <input
              type="text"
              class="form-control"
              name="claveMunicipio"
              id="codigo-postal-claveMunicipio"
              data-cy="claveMunicipio"
              :class="{ valid: !v$.claveMunicipio.$invalid, invalid: v$.claveMunicipio.$invalid }"
              v-model="v$.claveMunicipio.$model"
            />
            <div v-if="v$.claveMunicipio.$anyDirty && v$.claveMunicipio.$invalid">
              <small class="form-text text-danger" v-for="error of v$.claveMunicipio.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.idAsentamientoCons')"
              for="codigo-postal-idAsentamientoCons"
            ></label>
            <input
              type="text"
              class="form-control"
              name="idAsentamientoCons"
              id="codigo-postal-idAsentamientoCons"
              data-cy="idAsentamientoCons"
              :class="{ valid: !v$.idAsentamientoCons.$invalid, invalid: v$.idAsentamientoCons.$invalid }"
              v-model="v$.idAsentamientoCons.$model"
            />
            <div v-if="v$.idAsentamientoCons.$anyDirty && v$.idAsentamientoCons.$invalid">
              <small class="form-text text-danger" v-for="error of v$.idAsentamientoCons.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientesmsCodigoPostal.zona')" for="codigo-postal-zona"></label>
            <input
              type="text"
              class="form-control"
              name="zona"
              id="codigo-postal-zona"
              data-cy="zona"
              :class="{ valid: !v$.zona.$invalid, invalid: v$.zona.$invalid }"
              v-model="v$.zona.$model"
            />
            <div v-if="v$.zona.$anyDirty && v$.zona.$invalid">
              <small class="form-text text-danger" v-for="error of v$.zona.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveCiudad')"
              for="codigo-postal-claveCiudad"
            ></label>
            <input
              type="text"
              class="form-control"
              name="claveCiudad"
              id="codigo-postal-claveCiudad"
              data-cy="claveCiudad"
              :class="{ valid: !v$.claveCiudad.$invalid, invalid: v$.claveCiudad.$invalid }"
              v-model="v$.claveCiudad.$model"
            />
            <div v-if="v$.claveCiudad.$anyDirty && v$.claveCiudad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.claveCiudad.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./codigo-postal-update.component.ts"></script>
