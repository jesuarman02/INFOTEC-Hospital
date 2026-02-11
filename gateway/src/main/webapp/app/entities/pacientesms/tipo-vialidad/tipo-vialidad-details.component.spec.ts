/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TipoVialidadDetails from './tipo-vialidad-details.vue';
import TipoVialidadService from './tipo-vialidad.service';
import AlertService from '@/shared/alert/alert.service';

type TipoVialidadDetailsComponentType = InstanceType<typeof TipoVialidadDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tipoVialidadSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('TipoVialidad Management Detail Component', () => {
    let tipoVialidadServiceStub: SinonStubbedInstance<TipoVialidadService>;
    let mountOptions: MountingOptions<TipoVialidadDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tipoVialidadServiceStub = sinon.createStubInstance<TipoVialidadService>(TipoVialidadService);

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
          tipoVialidadService: () => tipoVialidadServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tipoVialidadServiceStub.find.resolves(tipoVialidadSample);
        route = {
          params: {
            tipoVialidadId: `${123}`,
          },
        };
        const wrapper = shallowMount(TipoVialidadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.tipoVialidad).toMatchObject(tipoVialidadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tipoVialidadServiceStub.find.resolves(tipoVialidadSample);
        const wrapper = shallowMount(TipoVialidadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
