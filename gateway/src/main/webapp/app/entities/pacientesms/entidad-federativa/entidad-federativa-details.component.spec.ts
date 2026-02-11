/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import EntidadFederativaDetails from './entidad-federativa-details.vue';
import EntidadFederativaService from './entidad-federativa.service';
import AlertService from '@/shared/alert/alert.service';

type EntidadFederativaDetailsComponentType = InstanceType<typeof EntidadFederativaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const entidadFederativaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('EntidadFederativa Management Detail Component', () => {
    let entidadFederativaServiceStub: SinonStubbedInstance<EntidadFederativaService>;
    let mountOptions: MountingOptions<EntidadFederativaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      entidadFederativaServiceStub = sinon.createStubInstance<EntidadFederativaService>(EntidadFederativaService);

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
          entidadFederativaService: () => entidadFederativaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        entidadFederativaServiceStub.find.resolves(entidadFederativaSample);
        route = {
          params: {
            entidadFederativaId: `${123}`,
          },
        };
        const wrapper = shallowMount(EntidadFederativaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.entidadFederativa).toMatchObject(entidadFederativaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        entidadFederativaServiceStub.find.resolves(entidadFederativaSample);
        const wrapper = shallowMount(EntidadFederativaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
