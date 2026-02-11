<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="infoSocioeconomica">
        <h2 class="jh-entity-heading" data-cy="infoSocioeconomicaDetailsHeading">
          <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.detail.title')"></span> {{ infoSocioeconomica.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tipoVivienda')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.tipoVivienda }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.materialVivienda')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.materialVivienda }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.numeroHabitaciones')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.numeroHabitaciones }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.numeroHabitantes')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.numeroHabitantes }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.serviciosDisponibles')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.serviciosDisponibles }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.ingresoMensual')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.ingresoMensual }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.ingresoMensualHogar')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.ingresoMensualHogar }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.gastoMensual')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.gastoMensual }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.personasDependientes')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.personasDependientes }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.apoyosGubernamentales')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.apoyosGubernamentales }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.ocupacionActual')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.ocupacionActual }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.condicionLaboral')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.condicionLaboral }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tipoEmpleo')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.tipoEmpleo }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.lugarTrabajo')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.lugarTrabajo }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tiempoEmpleado')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.tiempoEmpleado }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.gradoMaximoEstudios')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.gradoMaximoEstudios }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.aniosEstudio')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.aniosEstudio }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.estudia')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.estudia }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.institucionMedica')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.institucionMedica }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tipoAfiliacion')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.tipoAfiliacion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.numeroAfiliacion')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.numeroAfiliacion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.medioTransporte')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.medioTransporte }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tiempoTraslado')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.tiempoTraslado }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.fechaActualizacion')"></span>
          </dt>
          <dd>
            <span>{{ infoSocioeconomica.fechaActualizacion }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="infoSocioeconomica.id"
          :to="{ name: 'InfoSocioeconomicaEdit', params: { infoSocioeconomicaId: infoSocioeconomica.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./info-socioeconomica-details.component.ts"></script>
