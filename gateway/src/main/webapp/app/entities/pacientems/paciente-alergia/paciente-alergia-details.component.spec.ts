/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PacienteAlergiaDetails from './paciente-alergia-details.vue';
import PacienteAlergiaService from './paciente-alergia.service';
import AlertService from '@/shared/alert/alert.service';

type PacienteAlergiaDetailsComponentType = InstanceType<typeof PacienteAlergiaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pacienteAlergiaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('PacienteAlergia Management Detail Component', () => {
    let pacienteAlergiaServiceStub: SinonStubbedInstance<PacienteAlergiaService>;
    let mountOptions: MountingOptions<PacienteAlergiaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      pacienteAlergiaServiceStub = sinon.createStubInstance<PacienteAlergiaService>(PacienteAlergiaService);

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
          pacienteAlergiaService: () => pacienteAlergiaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        pacienteAlergiaServiceStub.find.resolves(pacienteAlergiaSample);
        route = {
          params: {
            pacienteAlergiaId: `${123}`,
          },
        };
        const wrapper = shallowMount(PacienteAlergiaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.pacienteAlergia).toMatchObject(pacienteAlergiaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pacienteAlergiaServiceStub.find.resolves(pacienteAlergiaSample);
        const wrapper = shallowMount(PacienteAlergiaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
