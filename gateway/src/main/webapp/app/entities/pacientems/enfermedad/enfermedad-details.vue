<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="enfermedad">
        <h2 class="jh-entity-heading" data-cy="enfermedadDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsEnfermedad.detail.title')"></span> {{ enfermedad.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsEnfermedad.nombre')"></span>
          </dt>
          <dd>
            <span>{{ enfermedad.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsEnfermedad.tipo')"></span>
          </dt>
          <dd>
            <span>{{ enfermedad.tipo }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsEnfermedad.codigoCIE')"></span>
          </dt>
          <dd>
            <span>{{ enfermedad.codigoCIE }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="enfermedad.id"
          :to="{ name: 'EnfermedadEdit', params: { enfermedadId: enfermedad.id } }"
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

<script lang="ts" src="./enfermedad-details.component.ts"></script>
