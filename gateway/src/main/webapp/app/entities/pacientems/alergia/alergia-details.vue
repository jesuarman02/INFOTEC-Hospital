<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="alergia">
        <h2 class="jh-entity-heading" data-cy="alergiaDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsAlergia.detail.title')"></span> {{ alergia.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsAlergia.nombre')"></span>
          </dt>
          <dd>
            <span>{{ alergia.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsAlergia.descripcion')"></span>
          </dt>
          <dd>
            <span>{{ alergia.descripcion }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="alergia.id" :to="{ name: 'AlergiaEdit', params: { alergiaId: alergia.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./alergia-details.component.ts"></script>
