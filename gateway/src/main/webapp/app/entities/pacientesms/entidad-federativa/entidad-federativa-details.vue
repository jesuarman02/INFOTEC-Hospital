<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="entidadFederativa">
        <h2 class="jh-entity-heading" data-cy="entidadFederativaDetailsHeading">
          <span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.detail.title')"></span> {{ entidadFederativa.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.clave')"></span>
          </dt>
          <dd>
            <span>{{ entidadFederativa.clave }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.nombre')"></span>
          </dt>
          <dd>
            <span>{{ entidadFederativa.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.abreviatura')"></span>
          </dt>
          <dd>
            <span>{{ entidadFederativa.abreviatura }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="entidadFederativa.id"
          :to="{ name: 'EntidadFederativaEdit', params: { entidadFederativaId: entidadFederativa.id } }"
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

<script lang="ts" src="./entidad-federativa-details.component.ts"></script>
