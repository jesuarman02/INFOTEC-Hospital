<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pacienteEnfermedad">
        <h2 class="jh-entity-heading" data-cy="pacienteEnfermedadDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.detail.title')"></span> {{ pacienteEnfermedad.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.fechaDiagnostico')"></span>
          </dt>
          <dd>
            <span>{{ pacienteEnfermedad.fechaDiagnostico }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.estado')"></span>
          </dt>
          <dd>
            <span>{{ pacienteEnfermedad.estado }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.notas')"></span>
          </dt>
          <dd>
            <span>{{ pacienteEnfermedad.notas }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.paciente')"></span>
          </dt>
          <dd>
            <div v-if="pacienteEnfermedad.paciente">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: pacienteEnfermedad.paciente.id } }">{{
                pacienteEnfermedad.paciente.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.enfermedad')"></span>
          </dt>
          <dd>
            <div v-if="pacienteEnfermedad.enfermedad">
              <router-link :to="{ name: 'EnfermedadView', params: { enfermedadId: pacienteEnfermedad.enfermedad.id } }">{{
                pacienteEnfermedad.enfermedad.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="pacienteEnfermedad.id"
          :to="{ name: 'PacienteEnfermedadEdit', params: { pacienteEnfermedadId: pacienteEnfermedad.id } }"
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

<script lang="ts" src="./paciente-enfermedad-details.component.ts"></script>
