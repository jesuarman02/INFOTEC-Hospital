<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsDireccion.home.createOrEditLabel"
          data-cy="DireccionCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsDireccion.home.createOrEditLabel')"
        ></h2>

        <div class="card mb-4 mt-3" v-if="!direccion.id">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Paso 1: Identificar al Paciente</h5>
          </div>
          <div class="card-body">
            <div class="form-group mb-0">
              <label class="form-control-label">Ingrese el ECU del paciente</label>
              <div class="input-group">
                <input 
                  type="text" 
                  class="form-control" 
                  v-model="ecuSearchString" 
                  placeholder="Ej: 12345" 
                  @input="ecuSearchString = ecuSearchString.replace(/\\D/g, '')"
                  @keyup.enter.prevent="buscarPaciente()" 
                  :readonly="pacienteEncontrado !== null"
                />
                <div class="input-group-append">
                  <button class="btn btn-primary" type="button" @click="buscarPaciente()" :disabled="isSearchingEcu || !ecuSearchString || pacienteEncontrado !== null">
                    <font-awesome-icon icon="search" v-if="!isSearchingEcu"></font-awesome-icon>
                    <font-awesome-icon icon="sync" spin v-if="isSearchingEcu"></font-awesome-icon>
                    Buscar
                  </button>
                </div>
              </div>
            </div>
            
            <div v-if="pacienteEncontrado" class="alert alert-success mt-3 mb-0">
              <font-awesome-icon icon="check-circle"></font-awesome-icon>
              <strong> Paciente seleccionado:</strong> 
              {{ pacienteEncontrado.nombre }} {{ pacienteEncontrado.apellidoPaterno }} {{ pacienteEncontrado.apellidoMaterno }}
            </div>
          </div>
        </div>

        <div v-if="pacienteEncontrado || direccion.id">
          <div class="form-group" v-if="direccion.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="direccion.id" readonly />
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
              @input="onInputUpper($event, v$.nombreVialidad)"
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
              @input="onInputUpper($event, v$.numExterior)"
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
              @input="onInputUpper($event, v$.numInterior)"
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
              placeholder="Ej: 5512345678"
              maxlength="10"
              :class="{ 'is-invalid': v$.telefono.$error }"
              v-model="direccion.telefono"
        @input="direccion.telefono = (direccion.telefono ?? '').replace(/\D/g, '')"              
        @blur="v$.telefono.$touch()"
            />
            <div class="invalid-feedback" v-if="v$.telefono.$error">
              El teléfono es obligatorio y debe tener exactamente 10 dígitos numéricos.
            </div>
          </div>

          <hr />
          <div class="form-group mt-4">
            <label class="form-control-label">Código Postal</label>
            <div class="input-group">
              <input 
                type="text" 
                class="form-control" 
                placeholder="Ej: 01000" 
                v-model="cpSearchString"
                maxlength="5"
                @input="cpSearchString = cpSearchString.replace(/\\D/g, '')"
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
        
        <div class="mt-4">
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving || (!pacienteEncontrado && !direccion.id)"
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