/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DireccionDetails from './direccion-details.vue';
import DireccionService from './direccion.service';
import AlertService from '@/shared/alert/alert.service';

type DireccionDetailsComponentType = InstanceType<typeof DireccionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const direccionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Direccion Management Detail Component', () => {
    let direccionServiceStub: SinonStubbedInstance<DireccionService>;
    let mountOptions: MountingOptions<DireccionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      direccionServiceStub = sinon.createStubInstance<DireccionService>(DireccionService);

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
          direccionService: () => direccionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        direccionServiceStub.find.resolves(direccionSample);
        route = {
          params: {
            direccionId: `${123}`,
          },
        };
        const wrapper = shallowMount(DireccionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.direccion).toMatchObject(direccionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        direccionServiceStub.find.resolves(direccionSample);
        const wrapper = shallowMount(DireccionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
