<template>
  <div>
    <h2 id="page-heading" data-cy="PacienteAlergiaHeading">
      <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.home.title')" id="paciente-alergia-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PacienteAlergiaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-paciente-alergia"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && pacienteAlergias && pacienteAlergias.length === 0">
      <span v-text="t$('gatewayApp.pacientemsPacienteAlergia.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="pacienteAlergias && pacienteAlergias.length > 0">
      <table class="table table-striped" aria-describedby="pacienteAlergias">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteAlergia.fechaDeteccion')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteAlergia.reaccion')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteAlergia.gravedad')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteAlergia.paciente')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteAlergia.alergia')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pacienteAlergia in pacienteAlergias" :key="pacienteAlergia.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PacienteAlergiaView', params: { pacienteAlergiaId: pacienteAlergia.id } }">{{
                pacienteAlergia.id
              }}</router-link>
            </td>
            <td>{{ pacienteAlergia.fechaDeteccion }}</td>
            <td>{{ pacienteAlergia.reaccion }}</td>
            <td v-text="t$('gatewayApp.GradoAlergia.' + pacienteAlergia.gravedad)"></td>
            <td>
              <div v-if="pacienteAlergia.paciente">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: pacienteAlergia.paciente.id } }">{{
                  pacienteAlergia.paciente.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="pacienteAlergia.alergia">
                <router-link :to="{ name: 'AlergiaView', params: { alergiaId: pacienteAlergia.alergia.id } }">{{
                  pacienteAlergia.alergia.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PacienteAlergiaView', params: { pacienteAlergiaId: pacienteAlergia.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PacienteAlergiaEdit', params: { pacienteAlergiaId: pacienteAlergia.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(pacienteAlergia)"
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
          id="gatewayApp.pacientemsPacienteAlergia.delete.question"
          data-cy="pacienteAlergiaDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-pacienteAlergia-heading"
          v-text="t$('gatewayApp.pacientemsPacienteAlergia.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-pacienteAlergia"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePacienteAlergia()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./paciente-alergia.component.ts"></script>
