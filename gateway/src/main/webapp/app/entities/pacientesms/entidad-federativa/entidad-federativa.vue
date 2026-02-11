<template>
  <div>
    <h2 id="page-heading" data-cy="EntidadFederativaHeading">
      <span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.home.title')" id="entidad-federativa-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'EntidadFederativaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-entidad-federativa"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && entidadFederativas && entidadFederativas.length === 0">
      <span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="entidadFederativas && entidadFederativas.length > 0">
      <table class="table table-striped" aria-describedby="entidadFederativas">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.clave')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.nombre')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientesmsEntidadFederativa.abreviatura')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="entidadFederativa in entidadFederativas" :key="entidadFederativa.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EntidadFederativaView', params: { entidadFederativaId: entidadFederativa.id } }">{{
                entidadFederativa.id
              }}</router-link>
            </td>
            <td>{{ entidadFederativa.clave }}</td>
            <td>{{ entidadFederativa.nombre }}</td>
            <td>{{ entidadFederativa.abreviatura }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'EntidadFederativaView', params: { entidadFederativaId: entidadFederativa.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'EntidadFederativaEdit', params: { entidadFederativaId: entidadFederativa.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(entidadFederativa)"
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
          id="gatewayApp.pacientesmsEntidadFederativa.delete.question"
          data-cy="entidadFederativaDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-entidadFederativa-heading"
          v-text="t$('gatewayApp.pacientesmsEntidadFederativa.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-entidadFederativa"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeEntidadFederativa()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./entidad-federativa.component.ts"></script>
