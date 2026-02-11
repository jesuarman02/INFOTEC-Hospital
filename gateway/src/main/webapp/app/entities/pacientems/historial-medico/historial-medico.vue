<template>
  <div>
    <h2 id="page-heading" data-cy="HistorialMedicoHeading">
      <span v-text="t$('gatewayApp.pacientemsHistorialMedico.home.title')" id="historial-medico-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientemsHistorialMedico.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'HistorialMedicoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-historial-medico"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientemsHistorialMedico.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && historialMedicos && historialMedicos.length === 0">
      <span v-text="t$('gatewayApp.pacientemsHistorialMedico.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="historialMedicos && historialMedicos.length > 0">
      <table class="table table-striped" aria-describedby="historialMedicos">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsHistorialMedico.antecedentesQuirurgicos')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsHistorialMedico.esquemaVacunacion')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsHistorialMedico.habitos')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsHistorialMedico.observacionesGenerales')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="historialMedico in historialMedicos" :key="historialMedico.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'HistorialMedicoView', params: { historialMedicoId: historialMedico.id } }">{{
                historialMedico.id
              }}</router-link>
            </td>
            <td>{{ historialMedico.antecedentesQuirurgicos }}</td>
            <td>{{ historialMedico.esquemaVacunacion }}</td>
            <td>{{ historialMedico.habitos }}</td>
            <td>{{ historialMedico.observacionesGenerales }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'HistorialMedicoView', params: { historialMedicoId: historialMedico.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'HistorialMedicoEdit', params: { historialMedicoId: historialMedico.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(historialMedico)"
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
          id="gatewayApp.pacientemsHistorialMedico.delete.question"
          data-cy="historialMedicoDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-historialMedico-heading"
          v-text="t$('gatewayApp.pacientemsHistorialMedico.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-historialMedico"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeHistorialMedico()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./historial-medico.component.ts"></script>
