<template>
  <div class="row justify-content-center">
    <div class="col-10">
      <div v-if="paciente">
        <h2 class="mb-3" data-cy="pacienteDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsPaciente.detail.title')"></span> {{ paciente.id }}
        </h2>
        <hr />

<div class="card mb-4 shadow-sm">
          <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center" 
               style="cursor: pointer;" 
               @click="mostrarDatosPersonales = !mostrarDatosPersonales">
            <h5 class="mb-0"><font-awesome-icon icon="user"></font-awesome-icon> Datos Personales</h5>
            <font-awesome-icon :icon="mostrarDatosPersonales ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
          </div>
          
          <div class="card-body" v-show="mostrarDatosPersonales">
            <dl class="row mb-0">
              <dt class="col-sm-5"><span v-text="t$('gatewayApp.pacientemsPaciente.ecu')"></span></dt>
              <dd class="col-sm-7">{{ paciente.ecu }}</dd>

              <dt class="col-sm-5"><span v-text="t$('gatewayApp.pacientemsPaciente.nombre')"></span></dt>
              <dd class="col-sm-7">{{ paciente.nombre }}</dd>

              <dt class="col-sm-5"><span v-text="t$('gatewayApp.pacientemsPaciente.apellidoPaterno')"></span></dt>
              <dd class="col-sm-7">{{ paciente.apellidoPaterno }}</dd>

              <dt class="col-sm-5"><span v-text="t$('gatewayApp.pacientemsPaciente.apellidoMaterno')"></span></dt>
              <dd class="col-sm-7">{{ paciente.apellidoMaterno }}</dd>

              <dt class="col-sm-5"><span v-text="t$('gatewayApp.pacientemsPaciente.sexo')"></span></dt>
              <dd class="col-sm-7">{{ paciente.sexo === 'M' ? 'Hombre' : (paciente.sexo === 'F' ? 'Mujer' : paciente.sexo) }}</dd>

              <dt class="col-sm-5" v-if="paciente.sexo === 'F'"><span>Embarazo</span></dt>
              <dd class="col-sm-7" v-if="paciente.sexo === 'F'">{{ paciente.embarazo }}</dd>

              <dt class="col-sm-5"><span v-text="t$('gatewayApp.pacientemsPaciente.nacionalidad')"></span></dt>
              <dd class="col-sm-7">{{ paciente.nacionalidad }}</dd>

              <dt class="col-sm-5"><span v-text="t$('gatewayApp.pacientemsPaciente.fechaNacimiento')"></span></dt>
              <dd class="col-sm-7">{{ paciente.fechaNacimiento }}</dd>

              <dt class="col-sm-5"><span v-text="t$('gatewayApp.pacientemsPaciente.estadoCivil')"></span></dt>
              <dd class="col-sm-7">{{ paciente.estadoCivil }}</dd>

              <dt class="col-sm-5"><span v-text="t$('gatewayApp.pacientemsPaciente.curp')"></span></dt>
              <dd class="col-sm-7">{{ paciente.curp }}</dd>
            </dl>
          </div>
        </div>

        <div class="card mb-4 shadow-sm">
          <div class="card-header bg-info text-white d-flex justify-content-between align-items-center" 
               style="cursor: pointer;" 
               @click="mostrarDatosContacto = !mostrarDatosContacto">
            <h5 class="mb-0"><font-awesome-icon icon="map-marker-alt"></font-awesome-icon> Datos de Contacto y Ubicación</h5>
            <font-awesome-icon :icon="mostrarDatosContacto ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
          </div>
          
          <div class="card-body" v-show="mostrarDatosContacto">
            <div v-if="paciente.direccion && paciente.direccion.id">
              <dl class="row mb-0">
                <dt class="col-sm-5">Calle y Número</dt>
                <dd class="col-sm-7">
                  {{ paciente.direccion.tipoVialidad ? paciente.direccion.tipoVialidad.nombre : '' }} 
                  {{ paciente.direccion.nombreVialidad }} No. {{ paciente.direccion.numExterior }}
                  <span v-if="paciente.direccion.numInterior && paciente.direccion.numInterior !== 'S/N'"> Int. {{ paciente.direccion.numInterior }}</span>
                </dd>

                <dt class="col-sm-5">Colonia</dt>
                <dd class="col-sm-7">
                  {{ paciente.direccion.codigoPostalInfo ? paciente.direccion.codigoPostalInfo.asentamiento : '' }}
                </dd>

                <dt class="col-sm-5">Municipio / Alcaldía</dt>
                <dd class="col-sm-7">
                  {{ paciente.direccion.codigoPostalInfo ? paciente.direccion.codigoPostalInfo.municipio : '' }}
                </dd>

                <dt class="col-sm-5">Estado</dt>
                <dd class="col-sm-7">
                  {{ paciente.direccion.codigoPostalInfo ? paciente.direccion.codigoPostalInfo.estado : '' }}
                </dd>

                <dt class="col-sm-5">Código Postal</dt>
                <dd class="col-sm-7">
                  {{ paciente.direccion.codigoPostalInfo ? paciente.direccion.codigoPostalInfo.codigo : '' }}
                </dd>

                <dt class="col-sm-5">Teléfono</dt>
                <dd class="col-sm-7">{{ paciente.direccion.telefono }}</dd>
              </dl>
            </div>
            <div v-else class="alert alert-warning mb-0">
              <font-awesome-icon icon="exclamation-triangle"></font-awesome-icon> Aún no hay una dirección registrada para este paciente.
            </div>
          </div>
        </div>

        <div class="mt-3">
          <button type="submit" v-on:click.prevent="previousState()" class="btn btn-secondary" data-cy="entityDetailsBackButton">
            <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
          </button>
          <router-link v-if="paciente.id" :to="{ name: 'PacienteEdit', params: { pacienteId: paciente.id } }" custom v-slot="{ navigate }">
            <button @click="navigate" class="btn btn-primary">
              <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
            </button>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./paciente-details.component.ts"></script>