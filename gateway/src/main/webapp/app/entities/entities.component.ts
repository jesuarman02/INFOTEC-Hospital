import { defineComponent, provide } from 'vue';

import PacienteService from './pacientems/paciente/paciente.service';
import InfoSocioeconomicaService from './pacientems/info-socioeconomica/info-socioeconomica.service';
import DireccionService from './pacientems/direccion/direccion.service';
import HistorialMedicoService from './pacientems/historial-medico/historial-medico.service';
import SignosVitalesService from './pacientems/signos-vitales/signos-vitales.service';
import TipoVialidadService from './pacientesms/tipo-vialidad/tipo-vialidad.service';
import CodigoPostalService from './pacientesms/codigo-postal/codigo-postal.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('pacienteService', () => new PacienteService());
    provide('infoSocioeconomicaService', () => new InfoSocioeconomicaService());
    provide('direccionService', () => new DireccionService());
    provide('historialMedicoService', () => new HistorialMedicoService());
    provide('signosVitalesService', () => new SignosVitalesService());
    provide('tipoVialidadService', () => new TipoVialidadService());
    provide('codigoPostalService', () => new CodigoPostalService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
