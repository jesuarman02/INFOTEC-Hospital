/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MedicamentoDetails from './medicamento-details.vue';
import MedicamentoService from './medicamento.service';
import AlertService from '@/shared/alert/alert.service';

type MedicamentoDetailsComponentType = InstanceType<typeof MedicamentoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const medicamentoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Medicamento Management Detail Component', () => {
    let medicamentoServiceStub: SinonStubbedInstance<MedicamentoService>;
    let mountOptions: MountingOptions<MedicamentoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      medicamentoServiceStub = sinon.createStubInstance<MedicamentoService>(MedicamentoService);

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
          medicamentoService: () => medicamentoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        medicamentoServiceStub.find.resolves(medicamentoSample);
        route = {
          params: {
            medicamentoId: `${123}`,
          },
        };
        const wrapper = shallowMount(MedicamentoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.medicamento).toMatchObject(medicamentoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        medicamentoServiceStub.find.resolves(medicamentoSample);
        const wrapper = shallowMount(MedicamentoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
