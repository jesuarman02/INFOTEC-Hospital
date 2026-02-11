<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pacienteMedicamento">
        <h2 class="jh-entity-heading" data-cy="pacienteMedicamentoDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.detail.title')"></span> {{ pacienteMedicamento.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.dosis')"></span>
          </dt>
          <dd>
            <span>{{ pacienteMedicamento.dosis }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.frecuencia')"></span>
          </dt>
          <dd>
            <span>{{ pacienteMedicamento.frecuencia }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.fechaInicio')"></span>
          </dt>
          <dd>
            <span>{{ pacienteMedicamento.fechaInicio }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.fechaFin')"></span>
          </dt>
          <dd>
            <span>{{ pacienteMedicamento.fechaFin }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.activo')"></span>
          </dt>
          <dd>
            <span>{{ pacienteMedicamento.activo }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.paciente')"></span>
          </dt>
          <dd>
            <div v-if="pacienteMedicamento.paciente">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: pacienteMedicamento.paciente.id } }">{{
                pacienteMedicamento.paciente.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.medicamento')"></span>
          </dt>
          <dd>
            <div v-if="pacienteMedicamento.medicamento">
              <router-link :to="{ name: 'MedicamentoView', params: { medicamentoId: pacienteMedicamento.medicamento.id } }">{{
                pacienteMedicamento.medicamento.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="pacienteMedicamento.id"
          :to="{ name: 'PacienteMedicamentoEdit', params: { pacienteMedicamentoId: pacienteMedicamento.id } }"
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

<script lang="ts" src="./paciente-medicamento-details.component.ts"></script>
