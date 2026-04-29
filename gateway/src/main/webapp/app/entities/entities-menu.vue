<template>
  <div>
    <b-dropdown-item to="/paciente">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.pacientemsPaciente')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/info-socioeconomica">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.pacientemsInfoSocioeconomica')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/direccion">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.pacientemsDireccion')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/historial-medico">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.pacientemsHistorialMedico')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/signos-vitales">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.pacientemsSignosVitales')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/cita">
      <font-awesome-icon icon="asterisk" />
      <span>Cita</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tipo-vialidad">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.pacientesmsTipoVialidad')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/codigo-postal">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.pacientesmsCodigoPostal')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
