<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cita">
        <h2 class="jh-entity-heading" data-cy="citaDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsCita.detail.title')"></span> {{ cita.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsCita.ecu')"></span>
          </dt>
          <dd>
            <span>{{ cita.ecu }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsCita.nombre')"></span>
          </dt>
          <dd>
            <span>{{ cita.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsCita.apellidoPaterno')"></span>
          </dt>
          <dd>
            <span>{{ cita.apellidoPaterno }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsCita.apellidoMaterno')"></span>
          </dt>
          <dd>
            <span>{{ cita.apellidoMaterno }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsCita.fechaHora')"></span>
          </dt>
          <dd>
            <span v-if="cita.fechaHora">{{ formatDateLong(cita.fechaHora) }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsCita.motivo')"></span>
          </dt>
          <dd>
            <span>{{ cita.motivo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="cita.id" :to="{ name: 'CitaEdit', params: { citaId: cita.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cita-details.component.ts"></script>
