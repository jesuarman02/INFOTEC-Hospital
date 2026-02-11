/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PacienteEnfermedadDetails from './paciente-enfermedad-details.vue';
import PacienteEnfermedadService from './paciente-enfermedad.service';
import AlertService from '@/shared/alert/alert.service';

type PacienteEnfermedadDetailsComponentType = InstanceType<typeof PacienteEnfermedadDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pacienteEnfermedadSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('PacienteEnfermedad Management Detail Component', () => {
    let pacienteEnfermedadServiceStub: SinonStubbedInstance<PacienteEnfermedadService>;
    let mountOptions: MountingOptions<PacienteEnfermedadDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      pacienteEnfermedadServiceStub = sinon.createStubInstance<PacienteEnfermedadService>(PacienteEnfermedadService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          pacienteEnfermedadService: () => pacienteEnfermedadServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        pacienteEnfermedadServiceStub.find.resolves(pacienteEnfermedadSample);
        route = {
          params: {
            pacienteEnfermedadId: `${123}`,
          },
        };
        const wrapper = shallowMount(PacienteEnfermedadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.pacienteEnfermedad).toMatchObject(pacienteEnfermedadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pacienteEnfermedadServiceStub.find.resolves(pacienteEnfermedadSample);
        const wrapper = shallowMount(PacienteEnfermedadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
