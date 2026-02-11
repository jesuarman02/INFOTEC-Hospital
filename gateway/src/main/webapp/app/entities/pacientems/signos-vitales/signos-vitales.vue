<template>
  <div>
    <h2 id="page-heading" data-cy="SignosVitalesHeading">
      <span v-text="t$('gatewayApp.pacientemsSignosVitales.home.title')" id="signos-vitales-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientemsSignosVitales.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SignosVitalesCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-signos-vitales"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientemsSignosVitales.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && signosVitales && signosVitales.length === 0">
      <span v-text="t$('gatewayApp.pacientemsSignosVitales.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="signosVitales && signosVitales.length > 0">
      <table class="table table-striped" aria-describedby="signosVitales">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaRegistro')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.fechaRegistro')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaRegistro'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('frecuenciaCardiaca')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaCardiaca')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'frecuenciaCardiaca'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('tensionArterial')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.tensionArterial')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tensionArterial'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('frecuenciaRespiratoria')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaRespiratoria')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'frecuenciaRespiratoria'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('temperatura')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.temperatura')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'temperatura'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('saturacionOxigeno')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.saturacionOxigeno')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'saturacionOxigeno'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('peso')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.peso')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'peso'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('estatura')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.estatura')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estatura'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('imc')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.imc')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'imc'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('paciente.id')">
              <span v-text="t$('gatewayApp.pacientemsSignosVitales.paciente')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paciente.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="signosVitales in signosVitales" :key="signosVitales.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SignosVitalesView', params: { signosVitalesId: signosVitales.id } }">{{
                signosVitales.id
              }}</router-link>
            </td>
            <td>{{ formatDateShort(signosVitales.fechaRegistro) || '' }}</td>
            <td>{{ signosVitales.frecuenciaCardiaca }}</td>
            <td>{{ signosVitales.tensionArterial }}</td>
            <td>{{ signosVitales.frecuenciaRespiratoria }}</td>
            <td>{{ signosVitales.temperatura }}</td>
            <td>{{ signosVitales.saturacionOxigeno }}</td>
            <td>{{ signosVitales.peso }}</td>
            <td>{{ signosVitales.estatura }}</td>
            <td>{{ signosVitales.imc }}</td>
            <td>
              <div v-if="signosVitales.paciente">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: signosVitales.paciente.id } }">{{
                  signosVitales.paciente.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SignosVitalesView', params: { signosVitalesId: signosVitales.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SignosVitalesEdit', params: { signosVitalesId: signosVitales.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(signosVitales)"
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
          id="gatewayApp.pacientemsSignosVitales.delete.question"
          data-cy="signosVitalesDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-signosVitales-heading" v-text="t$('gatewayApp.pacientemsSignosVitales.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-signosVitales"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSignosVitales()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="signosVitales && signosVitales.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./signos-vitales.component.ts"></script>
