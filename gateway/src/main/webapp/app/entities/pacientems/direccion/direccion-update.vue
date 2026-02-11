<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsDireccion.home.createOrEditLabel"
          data-cy="DireccionCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsDireccion.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="direccion.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="direccion.id" readonly />
          </div>

          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsDireccion.nombreVialidad')" for="field_nombreVialidad"></label>
            <input
              type="text"
              class="form-control"
              name="nombreVialidad"
              id="field_nombreVialidad"
              data-cy="nombreVialidad"
              :class="{ 'is-invalid': v$.nombreVialidad.$error }"
              v-model="direccion.nombreVialidad"
              @blur="v$.nombreVialidad.$touch()"
            />
            <div class="invalid-feedback" v-if="v$.nombreVialidad.$error">
              El nombre de la vialidad es obligatorio.
            </div>
          </div>

          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsDireccion.numExterior')" for="field_numExterior"></label>
            <input
              type="text"
              class="form-control"
              name="numExterior"
              id="field_numExterior"
              data-cy="numExterior"
              :class="{ 'is-invalid': v$.numExterior.$error }"
              v-model="direccion.numExterior"
              @blur="v$.numExterior.$touch()"
            />
            <div class="invalid-feedback" v-if="v$.numExterior.$error">
              El número exterior es obligatorio.
            </div>
          </div>

          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsDireccion.numInterior')" for="field_numInterior"></label>
            <input
              type="text"
              class="form-control"
              name="numInterior"
              id="field_numInterior"
              data-cy="numInterior"
              v-model="direccion.numInterior"
            />
          </div>

          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsDireccion.telefono')" for="field_telefono"></label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="field_telefono"
              data-cy="telefono"
              v-model="direccion.telefono"
            />
          </div>

          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsDireccion.tipoVialidad')" for="field_tipoVialidad"></label>
            <select
              class="form-control"
              id="field_tipoVialidad"
              data-cy="tipoVialidad"
              name="tipoVialidad"
              :class="{ 'is-invalid': v$.tipoVialidad.$error }"
              v-model="direccion.tipoVialidad"
              @blur="v$.tipoVialidad.$touch()"
            >
              <option v-bind:value="null">-- Seleccione --</option>
              <option
                v-bind:value="
                  direccion.tipoVialidad && tipoVialidadOption.id === direccion.tipoVialidad.id
                    ? direccion.tipoVialidad
                    : tipoVialidadOption
                "
                v-for="tipoVialidadOption in tipoVialidads"
                :key="tipoVialidadOption.id"
              >
                {{ tipoVialidadOption.nombre }}
              </option>
            </select>
            <div class="invalid-feedback" v-if="v$.tipoVialidad.$error">
              Debe seleccionar un tipo de vialidad.
            </div>
          </div>

          <hr />
          <h4>Datos de Ubicación</h4>
          
          <div class="form-group">
            <label class="form-control-label">Código Postal</label>
            <div class="input-group">
              <input 
                type="text" 
                class="form-control" 
                placeholder="Ej: 01000" 
                v-model="cpSearchString"
                maxlength="5"
                @input="cpSearchString = cpSearchString.replace(/\D/g, '')"
              />
              <div class="input-group-append" v-if="isSearchingCP">
                <span class="input-group-text bg-white border-left-0">
                  <font-awesome-icon icon="sync" spin class="text-primary"></font-awesome-icon>
                </span>
              </div>
            </div>
            <small class="form-text text-muted">
              Escriba los 5 dígitos y la búsqueda se realizará automáticamente.
            </small>
          </div>

          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsDireccion.codigoPostalInfo')" for="field_codigoPostalInfo"></label>
            <select
              class="form-control"
              id="field_codigoPostalInfo"
              data-cy="codigoPostalInfo"
              name="codigoPostalInfo"
              :class="{ 'is-invalid': v$.codigoPostalInfo.$error }"
              v-model="direccion.codigoPostalInfo"
              @change="updateLocationDetails()"
              @blur="v$.codigoPostalInfo.$touch()"
            >
              <option v-bind:value="null">-- Selecciona la Colonia/Asentamiento --</option>
              <option
                v-bind:value="
                  direccion.codigoPostalInfo && codigoPostalOption.id === direccion.codigoPostalInfo.id
                    ? direccion.codigoPostalInfo
                    : codigoPostalOption
                "
                v-for="codigoPostalOption in coloniasOptions"
                :key="codigoPostalOption.id"
              >
                {{ codigoPostalOption.asentamiento }} ({{ codigoPostalOption.tipoAsentamiento }})
              </option>
            </select>
            <div class="invalid-feedback" v-if="v$.codigoPostalInfo.$error">
              Debe seleccionar una colonia válida.
            </div>
          </div>

          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                 <label class="form-control-label">Municipio / Alcaldía</label>
                 <input type="text" class="form-control" v-model="municipioDisplay" readonly style="background-color: #e9ecef;" />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                 <label class="form-control-label">Estado</label>
                 <input type="text" class="form-control" v-model="estadoDisplay" readonly style="background-color: #e9ecef;" />
              </div>
            </div>
          </div>
          <hr />

        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
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

<script lang="ts" src="./direccion-update.component.ts"></script>