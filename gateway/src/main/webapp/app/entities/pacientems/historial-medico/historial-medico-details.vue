<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="historialMedico">
        <h2 class="jh-entity-heading" data-cy="historialMedicoDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsHistorialMedico.detail.title')"></span> {{ historialMedico.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsHistorialMedico.antecedentesQuirurgicos')"></span>
          </dt>
          <dd>
            <span>{{ historialMedico.antecedentesQuirurgicos }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsHistorialMedico.esquemaVacunacion')"></span>
          </dt>
          <dd>
            <span>{{ historialMedico.esquemaVacunacion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsHistorialMedico.habitos')"></span>
          </dt>
          <dd>
            <span>{{ historialMedico.habitos }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsHistorialMedico.observacionesGenerales')"></span>
          </dt>
          <dd>
            <span>{{ historialMedico.observacionesGenerales }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="historialMedico.id"
          :to="{ name: 'HistorialMedicoEdit', params: { historialMedicoId: historialMedico.id } }"
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

<script lang="ts" src="./historial-medico-details.component.ts"></script>
