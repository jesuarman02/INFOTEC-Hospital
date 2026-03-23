<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="signosVitales">
        <h2 class="jh-entity-heading mb-4" data-cy="signosVitalesDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsSignosVitales.detail.title')"></span> {{ signosVitales.id }}
          <span v-if="signosVitales.pacienteEcu" class="badge badge-primary ml-3" style="font-size: 0.6em; vertical-align: middle;">
            ECU: {{ signosVitales.pacienteEcu }} - {{ signosVitales.pacienteNombre }} {{ signosVitales.pacienteApellidoPaterno }}
          </span>
        </h2>

        <h4 class="mt-4 border-bottom pb-2">Contexto de la Toma</h4>
        <dl class="row jh-entity-details mb-4">
          <dt class="col-sm-4"><span v-text="t$('gatewayApp.pacientemsSignosVitales.fechaRegistro')"></span></dt>
          <dd class="col-sm-8">
            <span v-if="signosVitales.fechaRegistro">{{ formatDateLong(signosVitales.fechaRegistro) }}</span>
          </dd>

          <dt class="col-sm-4"><span>Tipo de Toma</span></dt>
          <dd class="col-sm-8">
            <span class="badge badge-info">{{ signosVitales.tipo || 'N/A' }}</span>
          </dd>

          <dt class="col-sm-4"><span>Personal Responsable</span></dt>
          <dd class="col-sm-8"><span>{{ signosVitales.personal || 'N/A' }}</span></dd>
        </dl>

        <h4 class="mt-4 border-bottom pb-2">Mediciones Vitales</h4>
        <dl class="row jh-entity-details mb-4">
          <dt class="col-sm-4"><span v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaCardiaca')"></span></dt>
          <dd class="col-sm-8"><span>{{ signosVitales.frecuenciaCardiaca }} <small class="text-muted">lpm</small></span></dd>

          <dt class="col-sm-4"><span v-text="t$('gatewayApp.pacientemsSignosVitales.tensionArterial')"></span></dt>
          <dd class="col-sm-8"><span>{{ signosVitales.tensionArterial }}</span></dd>

          <dt class="col-sm-4"><span v-text="t$('gatewayApp.pacientemsSignosVitales.frecuenciaRespiratoria')"></span></dt>
          <dd class="col-sm-8"><span>{{ signosVitales.frecuenciaRespiratoria }} <small class="text-muted">rpm</small></span></dd>

          <dt class="col-sm-4"><span v-text="t$('gatewayApp.pacientemsSignosVitales.temperatura')"></span></dt>
          <dd class="col-sm-8"><span>{{ signosVitales.temperatura }} <small class="text-muted">°C</small></span></dd>

          <dt class="col-sm-4"><span v-text="t$('gatewayApp.pacientemsSignosVitales.saturacionOxigeno')"></span></dt>
          <dd class="col-sm-8"><span>{{ signosVitales.saturacionOxigeno }} <small class="text-muted">%</small></span></dd>

          <dt class="col-sm-4"><span>Glucosa</span></dt>
          <dd class="col-sm-8"><span>{{ signosVitales.glucosa ? signosVitales.glucosa + ' mg/dL' : '-' }}</span></dd>
        </dl>

        <h4 class="mt-4 border-bottom pb-2">Evaluación Extra</h4>
        <dl class="row jh-entity-details mb-4">
          <dt class="col-sm-4"><span>Dolor (Escala 0-10)</span></dt>
          <dd class="col-sm-8">
            <span v-if="signosVitales.dolor !== null && signosVitales.dolor !== undefined">{{ signosVitales.dolor }} / 10</span>
            <span v-else>-</span>
          </dd>

          <dt class="col-sm-4"><span>Estado de Conciencia</span></dt>
          <dd class="col-sm-8"><span>{{ signosVitales.estadoConciencia || '-' }}</span></dd>

          <dt class="col-sm-4"><span>Observaciones</span></dt>
          <dd class="col-sm-8"><span>{{ signosVitales.observaciones || '-' }}</span></dd>

          <dt class="col-sm-4"><span>Expediente del Paciente</span></dt>
          <dd class="col-sm-8">
            <div v-if="signosVitales.paciente && signosVitales.paciente.id">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: signosVitales.paciente.id } }">
                Ver Ficha Completa (ID: {{ signosVitales.paciente.id }})
              </router-link>
            </div>
            <div v-else>
              <span class="badge badge-warning">Sin Asignar</span>
            </div>
          </dd>
        </dl>

        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="signosVitales.id"
          :to="{ name: 'SignosVitalesEdit', params: { signosVitalesId: signosVitales.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary ml-2">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./signos-vitales-details.component.ts"></script>