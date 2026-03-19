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
<table class="table" aria-describedby="infoSocioeconomicas">
        <thead>
          <tr>
            <th scope="row"><span>ID Registro</span></th>
            <th scope="row"><span>Pregunta</span></th>
            <th scope="row"><span>Respuesta</span></th>
            <th scope="row"><span>Respuesta Abierta</span></th>
            <th scope="row" class="text-right"><span>Acciones</span></th>
          </tr>
        </thead>
        
        <tbody v-for="grupo in infoAgrupada" :key="'grupo-' + grupo.pacienteId">
          
          <tr class="table-primary">
            <td colspan="4" class="font-weight-bold align-middle">
              <span class="text-primary" style="font-size: 1.1em;">
                Paciente: {{ grupo.ecuFormateado }}
              </span>
              <span class="badge badge-info ml-3">{{ grupo.respuestas.length }} respuestas registradas</span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'InfoSocioeconomicaView', params: { infoSocioeconomicaId: grupo.respuestas[0].id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                
                <router-link :to="{ name: 'InfoSocioeconomicaEdit', params: { infoSocioeconomicaId: grupo.respuestas[0].id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                
                <b-button @click="prepareRemoveEstudio(grupo)" variant="danger" class="btn btn-sm" v-b-modal.removeEntity>
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
                
                <button class="btn btn-sm btn-outline-primary font-weight-bold ml-2" @click="togglePaciente(grupo.pacienteId)">
                  {{ pacientesExpandidos[grupo.pacienteId] ? 'Ocultar' : 'Detalles' }}
                </button>
              </div>
            </td>
          </tr>

          <tr v-if="pacientesExpandidos[grupo.pacienteId]" v-for="infoSocioeconomica in grupo.respuestas" :key="infoSocioeconomica.id">
            <td>
              <router-link :to="{ name: 'InfoSocioeconomicaView', params: { infoSocioeconomicaId: infoSocioeconomica.id } }">
                {{ infoSocioeconomica.id }}
              </router-link>
            </td>
            <td>{{ infoSocioeconomica.pregunta }}</td>
            <td>{{ infoSocioeconomica.respuesta }}</td>
            <td>{{ infoSocioeconomica.respuestaAbierta }}</td>
            <td></td>
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