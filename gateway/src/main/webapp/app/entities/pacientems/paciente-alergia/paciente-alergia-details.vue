<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pacienteAlergia">
        <h2 class="jh-entity-heading" data-cy="pacienteAlergiaDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.detail.title')"></span> {{ pacienteAlergia.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.fechaDeteccion')"></span>
          </dt>
          <dd>
            <span>{{ pacienteAlergia.fechaDeteccion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.reaccion')"></span>
          </dt>
          <dd>
            <span>{{ pacienteAlergia.reaccion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.gravedad')"></span>
          </dt>
          <dd>
            <span v-text="t$('gatewayApp.GradoAlergia.' + pacienteAlergia.gravedad)"></span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.paciente')"></span>
          </dt>
          <dd>
            <div v-if="pacienteAlergia.paciente">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: pacienteAlergia.paciente.id } }">{{
                pacienteAlergia.paciente.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.alergia')"></span>
          </dt>
          <dd>
            <div v-if="pacienteAlergia.alergia">
              <router-link :to="{ name: 'AlergiaView', params: { alergiaId: pacienteAlergia.alergia.id } }">{{
                pacienteAlergia.alergia.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="pacienteAlergia.id"
          :to="{ name: 'PacienteAlergiaEdit', params: { pacienteAlergiaId: pacienteAlergia.id } }"
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

<script lang="ts" src="./paciente-alergia-details.component.ts"></script>
