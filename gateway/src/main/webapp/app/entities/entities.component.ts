import { defineComponent, provide } from 'vue';

import PacienteService from './pacientems/paciente/paciente.service';
import InfoSocioeconomicaService from './pacientems/info-socioeconomica/info-socioeconomica.service';
import DireccionService from './pacientems/direccion/direccion.service';
import HistorialMedicoService from './pacientems/historial-medico/historial-medico.service';
import SignosVitalesService from './pacientems/signos-vitales/signos-vitales.service';
import EnfermedadService from './pacientems/enfermedad/enfermedad.service';
import AlergiaService from './pacientems/alergia/alergia.service';
import MedicamentoService from './pacientems/medicamento/medicamento.service';
import PacienteEnfermedadService from './pacientems/paciente-enfermedad/paciente-enfermedad.service';
import PacienteAlergiaService from './pacientems/paciente-alergia/paciente-alergia.service';
import PacienteMedicamentoService from './pacientems/paciente-medicamento/paciente-medicamento.service';
import EntidadFederativaService from './pacientesms/entidad-federativa/entidad-federativa.service';
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
    provide('enfermedadService', () => new EnfermedadService());
    provide('alergiaService', () => new AlergiaService());
    provide('medicamentoService', () => new MedicamentoService());
    provide('pacienteEnfermedadService', () => new PacienteEnfermedadService());
    provide('pacienteAlergiaService', () => new PacienteAlergiaService());
    provide('pacienteMedicamentoService', () => new PacienteMedicamentoService());
    provide('entidadFederativaService', () => new EntidadFederativaService());
    provide('tipoVialidadService', () => new TipoVialidadService());
    provide('codigoPostalService', () => new CodigoPostalService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
