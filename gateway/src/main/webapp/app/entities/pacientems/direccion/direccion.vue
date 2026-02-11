<template>
  <div>
    <h2 id="page-heading" data-cy="DireccionHeading">
      <span v-text="t$('gatewayApp.pacientemsDireccion.home.title')" id="direccion-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientemsDireccion.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'DireccionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-direccion"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientemsDireccion.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && direccions && direccions.length === 0">
      <span v-text="t$('gatewayApp.pacientemsDireccion.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="direccions && direccions.length > 0">
      <table class="table table-striped" aria-describedby="direccions">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsDireccion.nombreVialidad')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsDireccion.numExterior')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsDireccion.numInterior')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsDireccion.telefono')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsDireccion.tipoVialidad')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsDireccion.codigoPostalInfo')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="direccion in direccions" :key="direccion.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DireccionView', params: { direccionId: direccion.id } }">{{ direccion.id }}</router-link>
            </td>
            <td>{{ direccion.nombreVialidad }}</td>
            <td>{{ direccion.numExterior }}</td>
            <td>{{ direccion.numInterior }}</td>
            <td>{{ direccion.telefono }}</td>
            <td>
              <div v-if="direccion.tipoVialidad && direccion.tipoVialidad.id">
                <router-link :to="{ name: 'TipoVialidadView', params: { tipoVialidadId: direccion.tipoVialidad.id } }">{{
                  direccion.tipoVialidad.nombre
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="direccion.codigoPostalInfo && direccion.codigoPostalInfo.id">
                <router-link :to="{ name: 'CodigoPostalView', params: { codigoPostalId: direccion.codigoPostalInfo.id } }">{{
                  direccion.codigoPostalInfo.codigo
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DireccionView', params: { direccionId: direccion.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DireccionEdit', params: { direccionId: direccion.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(direccion)"
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
          id="gatewayApp.pacientemsDireccion.delete.question"
          data-cy="direccionDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-direccion-heading" v-text="t$('gatewayApp.pacientemsDireccion.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-direccion"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeDireccion()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./direccion.component.ts"></script>