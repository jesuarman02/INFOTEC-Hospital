<template>
  <div>
    <h2 id="page-heading">
      <span>Historiales Médicos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching" />
          <span>Refrescar Lista</span>
        </button>

        <router-link :to="{ name: 'HistorialMedicoCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="plus" />
            <span>Crear Nuevo Historial Médico</span>
          </button>
        </router-link>
      </div>
    </h2>

    <br />

    <div v-if="!isFetching && historialMedicos.length === 0" class="alert alert-warning">
      No se encontraron historiales médicos registrados.
    </div>

    <div class="table-responsive" v-if="historialMedicos.length > 0">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Paciente (ECU y Nombre)</th>
            <th>Observaciones Generales</th>
            <th>Estado</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="historial in historialMedicos" :key="historial.id">
            <td>
              <router-link :to="{ name: 'HistorialMedicoView', params: { historialMedicoId: historial.id }}">
                {{ historial.id }}
              </router-link>
            </td>

            <td>
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: historial.pacienteId } }" class="font-weight-bold text-primary" style="text-decoration: none;" v-if="historial.pacienteEcu">
                {{ historial.pacienteEcu }} - {{ historial.pacienteNombre }} {{ historial.pacienteApellidoPaterno }}
              </router-link>
              <span v-else class="badge badge-warning">Sin Asignar</span>
            </td>

            <td>
              <div class="text-truncate" style="max-width: 300px;" :title="historial.observacionesGenerales || ''">
                {{ historial.observacionesGenerales || 'Sin observaciones' }}
              </div>
            </td>
            <td>
              <span class="badge" 
                :class="{
                  'badge-success': historial.estado === 'COMPLETO', 
                  'badge-warning': historial.estado === 'INCOMPLETO',
                  'badge-secondary': !historial.estado || historial.estado === 'VACIO'
                }">
                {{ historial.estado || 'VACIO' }}
              </span>
            </td>

            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'HistorialMedicoView', params: { historialMedicoId: historial.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm">
                    <font-awesome-icon icon="eye" />
                    <span class="d-none d-md-inline">Ver Expediente</span>
                  </button>
                </router-link>

                <router-link :to="{ name: 'HistorialMedicoEdit', params: { historialMedicoId: historial.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm">
                    <font-awesome-icon icon="pencil-alt" />
                    <span class="d-none d-md-inline">Editar</span>
                  </button>
                </router-link>

                <b-button @click="prepareRemove(historial)" variant="danger" class="btn btn-sm" v-b-modal.removeEntity>
                  <font-awesome-icon icon="times" />
                  <span class="d-none d-md-inline">Eliminar</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>Confirmar eliminación</template>
      <div class="modal-body">
        ¿Está seguro de eliminar este historial médico?
      </div>
      <template #modal-footer>
        <button class="btn btn-secondary" @click="closeDialog()">Cancelar</button>
        <button class="btn btn-danger" @click="removeHistorialMedico()">Eliminar</button>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./historial-medico.component.ts"></script>