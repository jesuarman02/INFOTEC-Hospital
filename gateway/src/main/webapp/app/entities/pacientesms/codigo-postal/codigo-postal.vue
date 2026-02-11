<template>
  <div>
    <h2 id="page-heading" data-cy="CodigoPostalHeading">
      <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.home.title')" id="codigo-postal-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CodigoPostalCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-codigo-postal"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && codigoPostals && codigoPostals.length === 0">
      <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.home.notFound')"></span>
    </div>
    <div>
      <b-row>
        <label>Búsqueda</label>
        <b-col>
          <b-form-input v-model="cpSelected" id="input-cp" placeholder="Esribe tu Código Postal"></b-form-input>
        </b-col>
        <b-col><b-button @click="findByPostalCode()">Buscar</b-button></b-col>
      </b-row>
      <b-row>&nbsp;</b-row>
    </div>

    <div class="table-responsive" v-if="codigoPostals && codigoPostals.length > 0">
      <table class="table table-striped" aria-describedby="codigoPostals">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('codigo')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.codigo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'codigo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('asentamiento')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.asentamiento')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'asentamiento'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('tipoAsentamiento')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.tipoAsentamiento')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tipoAsentamiento'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('municipio')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.municipio')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'municipio'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('estado')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.estado')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estado'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('ciudad')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.ciudad')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ciudad'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('codigoPostalAdministracion')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.codigoPostalAdministracion')"></span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'codigoPostalAdministracion'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('claveEstado')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveEstado')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'claveEstado'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('claveOficina')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveOficina')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'claveOficina'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('claveCP')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveCP')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'claveCP'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('claveTipoAsentamiento')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveTipoAsentamiento')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'claveTipoAsentamiento'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('claveMunicipio')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveMunicipio')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'claveMunicipio'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('idAsentamientoCons')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.idAsentamientoCons')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'idAsentamientoCons'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('zona')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.zona')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'zona'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('claveCiudad')">
              <span v-text="t$('gatewayApp.pacientesmsCodigoPostal.claveCiudad')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'claveCiudad'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="codigoPostal in codigoPostals" :key="codigoPostal.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CodigoPostalView', params: { codigoPostalId: codigoPostal.id } }">{{
                codigoPostal.id
              }}</router-link>
            </td>
            <td>{{ codigoPostal.codigo }}</td>
            <td>{{ codigoPostal.asentamiento }}</td>
            <td>{{ codigoPostal.tipoAsentamiento }}</td>
            <td>{{ codigoPostal.municipio }}</td>
            <td>{{ codigoPostal.estado }}</td>
            <td>{{ codigoPostal.ciudad }}</td>
            <td>{{ codigoPostal.codigoPostalAdministracion }}</td>
            <td>{{ codigoPostal.claveEstado }}</td>
            <td>{{ codigoPostal.claveOficina }}</td>
            <td>{{ codigoPostal.claveCP }}</td>
            <td>{{ codigoPostal.claveTipoAsentamiento }}</td>
            <td>{{ codigoPostal.claveMunicipio }}</td>
            <td>{{ codigoPostal.idAsentamientoCons }}</td>
            <td>{{ codigoPostal.zona }}</td>
            <td>{{ codigoPostal.claveCiudad }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CodigoPostalView', params: { codigoPostalId: codigoPostal.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CodigoPostalEdit', params: { codigoPostalId: codigoPostal.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(codigoPostal)"
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
          id="gatewayApp.pacientesmsCodigoPostal.delete.question"
          data-cy="codigoPostalDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-codigoPostal-heading" v-text="t$('gatewayApp.pacientesmsCodigoPostal.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-codigoPostal"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCodigoPostal()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="codigoPostals && codigoPostals.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./codigo-postal.component.ts"></script>
