<template>
  <div>
    <h2 id="page-heading" data-cy="TipoVialidadHeading">
      <span v-text="t$('gatewayApp.pacientesmsTipoVialidad.home.title')" id="tipo-vialidad-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientesmsTipoVialidad.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'TipoVialidadCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-tipo-vialidad"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientesmsTipoVialidad.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && tipoVialidads && tipoVialidads.length === 0">
      <span v-text="t$('gatewayApp.pacientesmsTipoVialidad.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="tipoVialidads && tipoVialidads.length > 0">
      <table class="table table-striped" aria-describedby="tipoVialidads">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientesmsTipoVialidad.nombre')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tipoVialidad in tipoVialidads" :key="tipoVialidad.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TipoVialidadView', params: { tipoVialidadId: tipoVialidad.id } }">{{
                tipoVialidad.id
              }}</router-link>
            </td>
            <td>{{ tipoVialidad.nombre }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TipoVialidadView', params: { tipoVialidadId: tipoVialidad.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TipoVialidadEdit', params: { tipoVialidadId: tipoVialidad.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(tipoVialidad)"
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
          id="gatewayApp.pacientesmsTipoVialidad.delete.question"
          data-cy="tipoVialidadDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-tipoVialidad-heading" v-text="t$('gatewayApp.pacientesmsTipoVialidad.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-tipoVialidad"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeTipoVialidad()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./tipo-vialidad.component.ts"></script>
