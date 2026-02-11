<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="signosVitales">
        <h2 class="jh-entity-heading" data-cy="signosVitalesDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsSignosVitales.detail.title')"></span> {{ signosVitales.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.fechaRegistro')"></span>
          </dt>
          <dd>
            <span v-if="signosVitales.fechaRegistro">{{ formatDateLong(signosVitales.fechaRegistro) }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaCardiaca')"></span>
          </dt>
          <dd>
            <span>{{ signosVitales.frecuenciaCardiaca }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.tensionArterial')"></span>
          </dt>
          <dd>
            <span>{{ signosVitales.tensionArterial }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaRespiratoria')"></span>
          </dt>
          <dd>
            <span>{{ signosVitales.frecuenciaRespiratoria }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.temperatura')"></span>
          </dt>
          <dd>
            <span>{{ signosVitales.temperatura }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.saturacionOxigeno')"></span>
          </dt>
          <dd>
            <span>{{ signosVitales.saturacionOxigeno }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.peso')"></span>
          </dt>
          <dd>
            <span>{{ signosVitales.peso }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.estatura')"></span>
          </dt>
          <dd>
            <span>{{ signosVitales.estatura }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.imc')"></span>
          </dt>
          <dd>
            <span>{{ signosVitales.imc }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.paciente')"></span>
          </dt>
          <dd>
            <div v-if="signosVitales.paciente">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: signosVitales.paciente.id } }">{{
                signosVitales.paciente.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="signosVitales.id"
          :to="{ name: 'SignosVitalesEdit', params: { signosVitalesId: signosVitales.id } }"
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

<script lang="ts" src="./signos-vitales-details.component.ts"></script>
