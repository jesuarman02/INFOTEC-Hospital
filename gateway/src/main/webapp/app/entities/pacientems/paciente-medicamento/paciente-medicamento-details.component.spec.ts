/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PacienteMedicamentoDetails from './paciente-medicamento-details.vue';
import PacienteMedicamentoService from './paciente-medicamento.service';
import AlertService from '@/shared/alert/alert.service';

type PacienteMedicamentoDetailsComponentType = InstanceType<typeof PacienteMedicamentoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pacienteMedicamentoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('PacienteMedicamento Management Detail Component', () => {
    let pacienteMedicamentoServiceStub: SinonStubbedInstance<PacienteMedicamentoService>;
    let mountOptions: MountingOptions<PacienteMedicamentoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      pacienteMedicamentoServiceStub = sinon.createStubInstance<PacienteMedicamentoService>(PacienteMedicamentoService);

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
          pacienteMedicamentoService: () => pacienteMedicamentoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        pacienteMedicamentoServiceStub.find.resolves(pacienteMedicamentoSample);
        route = {
          params: {
            pacienteMedicamentoId: `${123}`,
          },
        };
        const wrapper = shallowMount(PacienteMedicamentoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.pacienteMedicamento).toMatchObject(pacienteMedicamentoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pacienteMedicamentoServiceStub.find.resolves(pacienteMedicamentoSample);
        const wrapper = shallowMount(PacienteMedicamentoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
