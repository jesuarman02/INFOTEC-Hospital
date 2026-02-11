<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="medicamento">
        <h2 class="jh-entity-heading" data-cy="medicamentoDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsMedicamento.detail.title')"></span> {{ medicamento.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsMedicamento.nombre')"></span>
          </dt>
          <dd>
            <span>{{ medicamento.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsMedicamento.ingredienteActivo')"></span>
          </dt>
          <dd>
            <span>{{ medicamento.ingredienteActivo }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsMedicamento.presentacion')"></span>
          </dt>
          <dd>
            <span>{{ medicamento.presentacion }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="medicamento.id"
          :to="{ name: 'MedicamentoEdit', params: { medicamentoId: medicamento.id } }"
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

<script lang="ts" src="./medicamento-details.component.ts"></script>
