<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="codigoPostal">
        <h2 class="jh-entity-heading" data-cy="codigoPostalDetailsHeading">
          <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.detail.title')"></span> {{ codigoPostal.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.codigo')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.codigo }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.asentamiento')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.asentamiento }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.tipoAsentamiento')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.tipoAsentamiento }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.municipio')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.municipio }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.estado')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.estado }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.ciudad')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.ciudad }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.codigoPostalAdministracion')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.codigoPostalAdministracion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveEstado')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.claveEstado }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveOficina')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.claveOficina }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveCP')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.claveCP }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveTipoAsentamiento')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.claveTipoAsentamiento }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveMunicipio')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.claveMunicipio }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.idAsentamientoCons')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.idAsentamientoCons }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.zona')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.zona }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveCiudad')"></span>
          </dt>
          <dd>
            <span>{{ codigoPostal.claveCiudad }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="codigoPostal.id"
          :to="{ name: 'CodigoPostalEdit', params: { codigoPostalId: codigoPostal.id } }"
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

<script lang="ts" src="./codigo-postal-details.component.ts"></script>
