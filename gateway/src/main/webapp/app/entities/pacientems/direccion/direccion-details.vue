<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="direccion">
        <h2 class="jh-entity-heading" data-cy="direccionDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsDireccion.detail.title')"></span> {{ direccion.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsDireccion.nombreVialidad')"></span>
          </dt>
          <dd>
            <span>{{ direccion.nombreVialidad }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsDireccion.numExterior')"></span>
          </dt>
          <dd>
            <span>{{ direccion.numExterior }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsDireccion.numInterior')"></span>
          </dt>
          <dd>
            <span>{{ direccion.numInterior }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsDireccion.telefono')"></span>
          </dt>
          <dd>
            <span>{{ direccion.telefono }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsDireccion.tipoVialidad')"></span>
          </dt>
          <dd>
            <div v-if="direccion.tipoVialidad">
              <router-link :to="{ name: 'TipoVialidadView', params: { tipoVialidadId: direccion.tipoVialidad.id } }">{{
                direccion.tipoVialidad.nombre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsDireccion.codigoPostalInfo')"></span>
          </dt>
          <dd>
            <div v-if="direccion.codigoPostalInfo">
              <router-link :to="{ name: 'CodigoPostalView', params: { codigoPostalId: direccion.codigoPostalInfo.id } }">{{
                direccion.codigoPostalInfo.codigo
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
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
</template>

<script lang="ts" src="./direccion-details.component.ts"></script>
