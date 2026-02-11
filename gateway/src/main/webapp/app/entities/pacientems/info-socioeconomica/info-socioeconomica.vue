<template>
  <div>
    <h2 id="page-heading" data-cy="InfoSocioeconomicaHeading">
      <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.home.title')" id="info-socioeconomica-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'InfoSocioeconomicaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-info-socioeconomica"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && infoSocioeconomicas && infoSocioeconomicas.length === 0">
      <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="infoSocioeconomicas && infoSocioeconomicas.length > 0">
      <table class="table table-striped" aria-describedby="infoSocioeconomicas">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tipoVivienda')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.materialVivienda')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.numeroHabitaciones')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.numeroHabitantes')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.serviciosDisponibles')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.ingresoMensual')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.ingresoMensualHogar')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.gastoMensual')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.personasDependientes')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.apoyosGubernamentales')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.ocupacionActual')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.condicionLaboral')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tipoEmpleo')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.lugarTrabajo')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tiempoEmpleado')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.gradoMaximoEstudios')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.aniosEstudio')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.estudia')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.institucionMedica')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tipoAfiliacion')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.numeroAfiliacion')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.medioTransporte')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tiempoTraslado')"></span></th>
            <th scope="row"><span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.fechaActualizacion')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="infoSocioeconomica in infoSocioeconomicas" :key="infoSocioeconomica.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'InfoSocioeconomicaView', params: { infoSocioeconomicaId: infoSocioeconomica.id } }">{{
                infoSocioeconomica.id
              }}</router-link>
            </td>
            <td>{{ infoSocioeconomica.tipoVivienda }}</td>
            <td>{{ infoSocioeconomica.materialVivienda }}</td>
            <td>{{ infoSocioeconomica.numeroHabitaciones }}</td>
            <td>{{ infoSocioeconomica.numeroHabitantes }}</td>
            <td>{{ infoSocioeconomica.serviciosDisponibles }}</td>
            <td>{{ infoSocioeconomica.ingresoMensual }}</td>
            <td>{{ infoSocioeconomica.ingresoMensualHogar }}</td>
            <td>{{ infoSocioeconomica.gastoMensual }}</td>
            <td>{{ infoSocioeconomica.personasDependientes }}</td>
            <td>{{ infoSocioeconomica.apoyosGubernamentales }}</td>
            <td>{{ infoSocioeconomica.ocupacionActual }}</td>
            <td>{{ infoSocioeconomica.condicionLaboral }}</td>
            <td>{{ infoSocioeconomica.tipoEmpleo }}</td>
            <td>{{ infoSocioeconomica.lugarTrabajo }}</td>
            <td>{{ infoSocioeconomica.tiempoEmpleado }}</td>
            <td>{{ infoSocioeconomica.gradoMaximoEstudios }}</td>
            <td>{{ infoSocioeconomica.aniosEstudio }}</td>
            <td>{{ infoSocioeconomica.estudia }}</td>
            <td>{{ infoSocioeconomica.institucionMedica }}</td>
            <td>{{ infoSocioeconomica.tipoAfiliacion }}</td>
            <td>{{ infoSocioeconomica.numeroAfiliacion }}</td>
            <td>{{ infoSocioeconomica.medioTransporte }}</td>
            <td>{{ infoSocioeconomica.tiempoTraslado }}</td>
            <td>{{ infoSocioeconomica.fechaActualizacion }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'InfoSocioeconomicaView', params: { infoSocioeconomicaId: infoSocioeconomica.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'InfoSocioeconomicaEdit', params: { infoSocioeconomicaId: infoSocioeconomica.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(infoSocioeconomica)"
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
          id="gatewayApp.pacientemsInfoSocioeconomica.delete.question"
          data-cy="infoSocioeconomicaDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-infoSocioeconomica-heading"
          v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-infoSocioeconomica"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeInfoSocioeconomica()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./info-socioeconomica.component.ts"></script>
