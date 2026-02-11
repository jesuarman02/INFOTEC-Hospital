<template>
  <div>
    <h2 id="page-heading" data-cy="PacienteEnfermedadHeading">
      <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.home.title')" id="paciente-enfermedad-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PacienteEnfermedadCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-paciente-enfermedad"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && pacienteEnfermedads && pacienteEnfermedads.length === 0">
      <span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="pacienteEnfermedads && pacienteEnfermedads.length > 0">
      <table class="table table-striped" aria-describedby="pacienteEnfermedads">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.fechaDiagnostico')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.estado')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.notas')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.paciente')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.enfermedad')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="pacienteEnfermedad in pacienteEnfermedads" :key="pacienteEnfermedad.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PacienteEnfermedadView', params: { pacienteEnfermedadId: pacienteEnfermedad.id } }">{{
                pacienteEnfermedad.id
              }}</router-link>
            </td>
            <td>{{ pacienteEnfermedad.fechaDiagnostico }}</td>
            <td>{{ pacienteEnfermedad.estado }}</td>
            <td>{{ pacienteEnfermedad.notas }}</td>
            <td>
              <div v-if="pacienteEnfermedad.paciente">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: pacienteEnfermedad.paciente.id } }">{{
                  pacienteEnfermedad.paciente.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="pacienteEnfermedad.enfermedad">
                <router-link :to="{ name: 'EnfermedadView', params: { enfermedadId: pacienteEnfermedad.enfermedad.id } }">{{
                  pacienteEnfermedad.enfermedad.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PacienteEnfermedadView', params: { pacienteEnfermedadId: pacienteEnfermedad.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PacienteEnfermedadEdit', params: { pacienteEnfermedadId: pacienteEnfermedad.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(pacienteEnfermedad)"
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
          id="gatewayApp.pacientemsPacienteEnfermedad.delete.question"
          data-cy="pacienteEnfermedadDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-pacienteEnfermedad-heading"
          v-text="t$('gatewayApp.pacientemsPacienteEnfermedad.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-pacienteEnfermedad"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePacienteEnfermedad()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./paciente-enfermedad.component.ts"></script>
