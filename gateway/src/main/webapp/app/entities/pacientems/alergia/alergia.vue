<template>
  <div>
    <h2 id="page-heading" data-cy="AlergiaHeading">
      <span v-text="t$('gatewayApp.pacientemsAlergia.home.title')" id="alergia-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientemsAlergia.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'AlergiaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-alergia"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientemsAlergia.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && alergias && alergias.length === 0">
      <span v-text="t$('gatewayApp.pacientemsAlergia.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="alergias && alergias.length > 0">
      <table class="table table-striped" aria-describedby="alergias">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsAlergia.nombre')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsAlergia.descripcion')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="alergia in alergias" :key="alergia.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AlergiaView', params: { alergiaId: alergia.id } }">{{ alergia.id }}</router-link>
            </td>
            <td>{{ alergia.nombre }}</td>
            <td>{{ alergia.descripcion }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AlergiaView', params: { alergiaId: alergia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AlergiaEdit', params: { alergiaId: alergia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(alergia)"
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
          id="gatewayApp.pacientemsAlergia.delete.question"
          data-cy="alergiaDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-alergia-heading" v-text="t$('gatewayApp.pacientemsAlergia.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-alergia"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeAlergia()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./alergia.component.ts"></script>
