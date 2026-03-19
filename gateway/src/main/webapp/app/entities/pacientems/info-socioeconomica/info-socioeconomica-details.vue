<template>
  <div class="row justify-content-center">
    <div class="col-10">
      <div v-if="estudioCompleto && estudioCompleto.length > 0">
        <h2 class="jh-entity-heading" data-cy="infoSocioeconomicaDetailsHeading">
          <span>Detalle del Estudio Socioeconómico</span>
        </h2>
        
        <div class="card mb-4 mt-3 border-primary">
          <div class="card-body text-primary text-center">
            <h4 class="mb-0 font-weight-bold">Paciente: {{ pacienteNombre }}</h4>
          </div>
        </div>

        <div class="table-responsive">
          <table class="table table-striped table-bordered table-hover">
            <thead class="thead-dark">
              <tr>
                <th scope="col" style="width: 40%;">Pregunta</th>
                <th scope="col" style="width: 30%;">Respuesta</th>
                <th scope="col" style="width: 30%;">Respuesta Abierta / Detalles</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in estudioCompleto" :key="item.id">
                <td class="font-weight-bold">{{ item.pregunta }}</td>
                <td>
                  <span class="badge" :class="item.respuesta === 'SI' ? 'badge-success' : item.respuesta === 'NO' ? 'badge-danger' : 'badge-info'" style="font-size: 0.9em;">
                    {{ item.respuesta }}
                  </span>
                </td>
                <td>{{ item.respuestaAbierta || '-' }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info mt-3" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./info-socioeconomica-details.component.ts"></script>