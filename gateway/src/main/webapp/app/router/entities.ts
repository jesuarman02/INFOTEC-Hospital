import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Paciente = () => import('@/entities/pacientems/paciente/paciente.vue');
const PacienteUpdate = () => import('@/entities/pacientems/paciente/paciente-update.vue');
const PacienteDetails = () => import('@/entities/pacientems/paciente/paciente-details.vue');

const InfoSocioeconomica = () => import('@/entities/pacientems/info-socioeconomica/info-socioeconomica.vue');
const InfoSocioeconomicaUpdate = () => import('@/entities/pacientems/info-socioeconomica/info-socioeconomica-update.vue');
const InfoSocioeconomicaDetails = () => import('@/entities/pacientems/info-socioeconomica/info-socioeconomica-details.vue');

const Direccion = () => import('@/entities/pacientems/direccion/direccion.vue');
const DireccionUpdate = () => import('@/entities/pacientems/direccion/direccion-update.vue');
const DireccionDetails = () => import('@/entities/pacientems/direccion/direccion-details.vue');

const HistorialMedico = () => import('@/entities/pacientems/historial-medico/historial-medico.vue');
const HistorialMedicoUpdate = () => import('@/entities/pacientems/historial-medico/historial-medico-update.vue');
const HistorialMedicoDetails = () => import('@/entities/pacientems/historial-medico/historial-medico-details.vue');

const SignosVitales = () => import('@/entities/pacientems/signos-vitales/signos-vitales.vue');
const SignosVitalesUpdate = () => import('@/entities/pacientems/signos-vitales/signos-vitales-update.vue');
const SignosVitalesDetails = () => import('@/entities/pacientems/signos-vitales/signos-vitales-details.vue');

const Enfermedad = () => import('@/entities/pacientems/enfermedad/enfermedad.vue');
const EnfermedadUpdate = () => import('@/entities/pacientems/enfermedad/enfermedad-update.vue');
const EnfermedadDetails = () => import('@/entities/pacientems/enfermedad/enfermedad-details.vue');

const Alergia = () => import('@/entities/pacientems/alergia/alergia.vue');
const AlergiaUpdate = () => import('@/entities/pacientems/alergia/alergia-update.vue');
const AlergiaDetails = () => import('@/entities/pacientems/alergia/alergia-details.vue');

const Medicamento = () => import('@/entities/pacientems/medicamento/medicamento.vue');
const MedicamentoUpdate = () => import('@/entities/pacientems/medicamento/medicamento-update.vue');
const MedicamentoDetails = () => import('@/entities/pacientems/medicamento/medicamento-details.vue');

const PacienteEnfermedad = () => import('@/entities/pacientems/paciente-enfermedad/paciente-enfermedad.vue');
const PacienteEnfermedadUpdate = () => import('@/entities/pacientems/paciente-enfermedad/paciente-enfermedad-update.vue');
const PacienteEnfermedadDetails = () => import('@/entities/pacientems/paciente-enfermedad/paciente-enfermedad-details.vue');

const PacienteAlergia = () => import('@/entities/pacientems/paciente-alergia/paciente-alergia.vue');
const PacienteAlergiaUpdate = () => import('@/entities/pacientems/paciente-alergia/paciente-alergia-update.vue');
const PacienteAlergiaDetails = () => import('@/entities/pacientems/paciente-alergia/paciente-alergia-details.vue');

const PacienteMedicamento = () => import('@/entities/pacientems/paciente-medicamento/paciente-medicamento.vue');
const PacienteMedicamentoUpdate = () => import('@/entities/pacientems/paciente-medicamento/paciente-medicamento-update.vue');
const PacienteMedicamentoDetails = () => import('@/entities/pacientems/paciente-medicamento/paciente-medicamento-details.vue');

const EntidadFederativa = () => import('@/entities/pacientesms/entidad-federativa/entidad-federativa.vue');
const EntidadFederativaUpdate = () => import('@/entities/pacientesms/entidad-federativa/entidad-federativa-update.vue');
const EntidadFederativaDetails = () => import('@/entities/pacientesms/entidad-federativa/entidad-federativa-details.vue');

const TipoVialidad = () => import('@/entities/pacientesms/tipo-vialidad/tipo-vialidad.vue');
const TipoVialidadUpdate = () => import('@/entities/pacientesms/tipo-vialidad/tipo-vialidad-update.vue');
const TipoVialidadDetails = () => import('@/entities/pacientesms/tipo-vialidad/tipo-vialidad-details.vue');

const CodigoPostal = () => import('@/entities/pacientesms/codigo-postal/codigo-postal.vue');
const CodigoPostalUpdate = () => import('@/entities/pacientesms/codigo-postal/codigo-postal-update.vue');
const CodigoPostalDetails = () => import('@/entities/pacientesms/codigo-postal/codigo-postal-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'paciente',
      name: 'Paciente',
      component: Paciente,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente/new',
      name: 'PacienteCreate',
      component: PacienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente/:pacienteId/edit',
      name: 'PacienteEdit',
      component: PacienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente/:pacienteId/view',
      name: 'PacienteView',
      component: PacienteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'info-socioeconomica',
      name: 'InfoSocioeconomica',
      component: InfoSocioeconomica,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'info-socioeconomica/new',
      name: 'InfoSocioeconomicaCreate',
      component: InfoSocioeconomicaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'info-socioeconomica/:infoSocioeconomicaId/edit',
      name: 'InfoSocioeconomicaEdit',
      component: InfoSocioeconomicaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'info-socioeconomica/:infoSocioeconomicaId/view',
      name: 'InfoSocioeconomicaView',
      component: InfoSocioeconomicaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'direccion',
      name: 'Direccion',
      component: Direccion,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'direccion/new',
      name: 'DireccionCreate',
      component: DireccionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'direccion/:direccionId/edit',
      name: 'DireccionEdit',
      component: DireccionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'direccion/:direccionId/view',
      name: 'DireccionView',
      component: DireccionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historial-medico',
      name: 'HistorialMedico',
      component: HistorialMedico,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historial-medico/new',
      name: 'HistorialMedicoCreate',
      component: HistorialMedicoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historial-medico/:historialMedicoId/edit',
      name: 'HistorialMedicoEdit',
      component: HistorialMedicoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historial-medico/:historialMedicoId/view',
      name: 'HistorialMedicoView',
      component: HistorialMedicoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales',
      name: 'SignosVitales',
      component: SignosVitales,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales/new',
      name: 'SignosVitalesCreate',
      component: SignosVitalesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales/:signosVitalesId/edit',
      name: 'SignosVitalesEdit',
      component: SignosVitalesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales/:signosVitalesId/view',
      name: 'SignosVitalesView',
      component: SignosVitalesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'enfermedad',
      name: 'Enfermedad',
      component: Enfermedad,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'enfermedad/new',
      name: 'EnfermedadCreate',
      component: EnfermedadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'enfermedad/:enfermedadId/edit',
      name: 'EnfermedadEdit',
      component: EnfermedadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'enfermedad/:enfermedadId/view',
      name: 'EnfermedadView',
      component: EnfermedadDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alergia',
      name: 'Alergia',
      component: Alergia,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alergia/new',
      name: 'AlergiaCreate',
      component: AlergiaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alergia/:alergiaId/edit',
      name: 'AlergiaEdit',
      component: AlergiaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'alergia/:alergiaId/view',
      name: 'AlergiaView',
      component: AlergiaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medicamento',
      name: 'Medicamento',
      component: Medicamento,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medicamento/new',
      name: 'MedicamentoCreate',
      component: MedicamentoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medicamento/:medicamentoId/edit',
      name: 'MedicamentoEdit',
      component: MedicamentoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medicamento/:medicamentoId/view',
      name: 'MedicamentoView',
      component: MedicamentoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-enfermedad',
      name: 'PacienteEnfermedad',
      component: PacienteEnfermedad,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-enfermedad/new',
      name: 'PacienteEnfermedadCreate',
      component: PacienteEnfermedadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-enfermedad/:pacienteEnfermedadId/edit',
      name: 'PacienteEnfermedadEdit',
      component: PacienteEnfermedadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-enfermedad/:pacienteEnfermedadId/view',
      name: 'PacienteEnfermedadView',
      component: PacienteEnfermedadDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-alergia',
      name: 'PacienteAlergia',
      component: PacienteAlergia,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-alergia/new',
      name: 'PacienteAlergiaCreate',
      component: PacienteAlergiaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-alergia/:pacienteAlergiaId/edit',
      name: 'PacienteAlergiaEdit',
      component: PacienteAlergiaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-alergia/:pacienteAlergiaId/view',
      name: 'PacienteAlergiaView',
      component: PacienteAlergiaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-medicamento',
      name: 'PacienteMedicamento',
      component: PacienteMedicamento,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-medicamento/new',
      name: 'PacienteMedicamentoCreate',
      component: PacienteMedicamentoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-medicamento/:pacienteMedicamentoId/edit',
      name: 'PacienteMedicamentoEdit',
      component: PacienteMedicamentoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente-medicamento/:pacienteMedicamentoId/view',
      name: 'PacienteMedicamentoView',
      component: PacienteMedicamentoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'entidad-federativa',
      name: 'EntidadFederativa',
      component: EntidadFederativa,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'entidad-federativa/new',
      name: 'EntidadFederativaCreate',
      component: EntidadFederativaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'entidad-federativa/:entidadFederativaId/edit',
      name: 'EntidadFederativaEdit',
      component: EntidadFederativaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'entidad-federativa/:entidadFederativaId/view',
      name: 'EntidadFederativaView',
      component: EntidadFederativaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-vialidad',
      name: 'TipoVialidad',
      component: TipoVialidad,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-vialidad/new',
      name: 'TipoVialidadCreate',
      component: TipoVialidadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-vialidad/:tipoVialidadId/edit',
      name: 'TipoVialidadEdit',
      component: TipoVialidadUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tipo-vialidad/:tipoVialidadId/view',
      name: 'TipoVialidadView',
      component: TipoVialidadDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'codigo-postal',
      name: 'CodigoPostal',
      component: CodigoPostal,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'codigo-postal/new',
      name: 'CodigoPostalCreate',
      component: CodigoPostalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'codigo-postal/:codigoPostalId/edit',
      name: 'CodigoPostalEdit',
      component: CodigoPostalUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'codigo-postal/:codigoPostalId/view',
      name: 'CodigoPostalView',
      component: CodigoPostalDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
