<template>
  <div>
    <h2 id="page-heading" data-cy="PacienteMedicamentoHeading">
      <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.home.title')" id="paciente-medicamento-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PacienteMedicamentoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-paciente-medicamento"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && pacienteMedicamentos && pacienteMedicamentos.length === 0">
      <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="pacienteMedicamentos && pacienteMedicamentos.length > 0">
      <table class="table table-striped" aria-describedby="pacienteMedicamentos">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('dosis')">
              <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.dosis')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dosis'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('frecuencia')">
              <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.frecuencia')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'frecuencia'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaInicio')">
              <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.fechaInicio')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaInicio'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaFin')">
              <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.fechaFin')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaFin'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('activo')">
              <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.activo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('paciente.id')">
              <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.paciente')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paciente.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('medicamento.nombre')">
              <span v-text="t$('gatewayApp.pacientemsPacienteMedicamento.medicamento')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'medicamento.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pacienteMedicamento in pacienteMedicamentos" :key="pacienteMedicamento.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PacienteMedicamentoView', params: { pacienteMedicamentoId: pacienteMedicamento.id } }">{{
                pacienteMedicamento.id
              }}</router-link>
            </td>
            <td>{{ pacienteMedicamento.dosis }}</td>
            <td>{{ pacienteMedicamento.frecuencia }}</td>
            <td>{{ pacienteMedicamento.fechaInicio }}</td>
            <td>{{ pacienteMedicamento.fechaFin }}</td>
            <td>{{ pacienteMedicamento.activo }}</td>
            <td>
              <div v-if="pacienteMedicamento.paciente">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: pacienteMedicamento.paciente.id } }">{{
                  pacienteMedicamento.paciente.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="pacienteMedicamento.medicamento">
                <router-link :to="{ name: 'MedicamentoView', params: { medicamentoId: pacienteMedicamento.medicamento.id } }">{{
                  pacienteMedicamento.medicamento.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PacienteMedicamentoView', params: { pacienteMedicamentoId: pacienteMedicamento.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PacienteMedicamentoEdit', params: { pacienteMedicamentoId: pacienteMedicamento.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(pacienteMedicamento)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="gatewayApp.pacientemsPacienteMedicamento.delete.question"
          data-cy="pacienteMedicamentoDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-pacienteMedicamento-heading"
          v-text="t$('gatewayApp.pacientemsPacienteMedicamento.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-pacienteMedicamento"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePacienteMedicamento()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="pacienteMedicamentos && pacienteMedicamentos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./paciente-medicamento.component.ts"></script>
