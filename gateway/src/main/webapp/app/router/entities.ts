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
