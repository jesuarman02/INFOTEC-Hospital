<template>
  <div>
    <h2 id="page-heading" data-cy="CitaHeading">
      <span v-text="t$('gatewayApp.pacientemsCita.home.title')" id="cita-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientemsCita.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CitaCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-cita">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientemsCita.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && citas && citas.length === 0">
      <span v-text="t$('gatewayApp.pacientemsCita.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="citas && citas.length > 0">
      <table class="table table-striped" aria-describedby="citas">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('ecu')">
              <span v-text="t$('gatewayApp.pacientemsCita.ecu')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ecu'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span v-text="t$('gatewayApp.pacientemsCita.nombre')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('apellidoPaterno')">
              <span v-text="t$('gatewayApp.pacientemsCita.apellidoPaterno')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'apellidoPaterno'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('apellidoMaterno')">
              <span v-text="t$('gatewayApp.pacientemsCita.apellidoMaterno')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'apellidoMaterno'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaHora')">
              <span v-text="t$('gatewayApp.pacientemsCita.fechaHora')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaHora'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('motivo')">
              <span v-text="t$('gatewayApp.pacientemsCita.motivo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'motivo'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cita in citas" :key="cita.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CitaView', params: { citaId: cita.id } }">{{ cita.id }}</router-link>
            </td>
            <td>{{ cita.ecu }}</td>
            <td>{{ cita.nombre }}</td>
            <td>{{ cita.apellidoPaterno }}</td>
            <td>{{ cita.apellidoMaterno }}</td>
            <td>{{ formatDateShort(cita.fechaHora) || '' }}</td>
            <td>{{ cita.motivo }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CitaView', params: { citaId: cita.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CitaEdit', params: { citaId: cita.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(cita)"
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
        <span id="gatewayApp.pacientemsCita.delete.question" data-cy="citaDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-cita-heading" v-text="t$('gatewayApp.pacientemsCita.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-cita"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCita()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="citas && citas.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cita.component.ts"></script>
