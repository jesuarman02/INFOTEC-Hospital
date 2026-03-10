<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="direccion">
        <h2 class="mb-3" data-cy="direccionDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsDireccion.detail.title')"></span> {{ direccion.id }}
        </h2>
        <hr />

        <dl class="row mb-0">
          
          <dt class="col-sm-5">
            <span>Paciente (ECU)</span>
          </dt>
          <dd class="col-sm-7">
            <div v-if="direccion.pacienteId">
              <router-link
                :to="{ name: 'PacienteView', params: { pacienteId: direccion.pacienteId } }"
                class="font-weight-bold text-primary"
              >
                {{ direccion.pacienteEcu }} - {{ direccion.pacienteNombre }} {{ direccion.pacienteApellidoPaterno }}
              </router-link>
            </div>
            <div v-else>
              <span class="badge badge-warning">Sin Asignar</span>
            </div>
          </dd>

          <dt class="col-sm-5">
            <span v-text="t$('gatewayApp.pacientemsDireccion.nombreVialidad')"></span>
          </dt>
          <dd class="col-sm-7">
            <span>{{ direccion.nombreVialidad }}</span>
          </dd>

          <dt class="col-sm-5">
            <span v-text="t$('gatewayApp.pacientemsDireccion.numExterior')"></span>
          </dt>
          <dd class="col-sm-7">
            <span>{{ direccion.numExterior }}</span>
          </dd>

          <dt class="col-sm-5">
            <span v-text="t$('gatewayApp.pacientemsDireccion.numInterior')"></span>
          </dt>
          <dd class="col-sm-7">
            <span>{{ direccion.numInterior }}</span>
          </dd>

          <dt class="col-sm-5">
            <span v-text="t$('gatewayApp.pacientemsDireccion.telefono')"></span>
          </dt>
          <dd class="col-sm-7">
            <span>{{ direccion.telefono }}</span>
          </dd>

          <dt class="col-sm-5">
            <span v-text="t$('gatewayApp.pacientemsDireccion.tipoVialidad')"></span>
          </dt>
          <dd class="col-sm-7">
            <div v-if="direccion.tipoVialidad">
              <router-link :to="{ name: 'TipoVialidadView', params: { tipoVialidadId: direccion.tipoVialidad.id } }">
                {{ direccion.tipoVialidad.nombre }}
              </router-link>
            </div>
          </dd>

          <dt class="col-sm-5">
            <span v-text="t$('gatewayApp.pacientemsDireccion.codigoPostalInfo')"></span>
          </dt>
          <dd class="col-sm-7">
            <div v-if="direccion.codigoPostalInfo">
              <router-link :to="{ name: 'CodigoPostalView', params: { codigoPostalId: direccion.codigoPostalInfo.id } }">
                {{ direccion.codigoPostalInfo.codigo }}
              </router-link>
            </div>
          </dd>
        </dl>
        
        <div class="mt-4">
          <button type="submit" @click.prevent="previousState()" class="btn btn-secondary" data-cy="entityDetailsBackButton">
            <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
          </button>
          <router-link
            v-if="direccion.id"
            :to="{ name: 'DireccionEdit', params: { direccionId: direccion.id } }"
            custom
            v-slot="{ navigate }"
          >
            <button @click="navigate" class="btn btn-primary">
              <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
            </button>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./direccion-details.component.ts"></script>