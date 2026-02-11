/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CodigoPostalDetails from './codigo-postal-details.vue';
import CodigoPostalService from './codigo-postal.service';
import AlertService from '@/shared/alert/alert.service';

type CodigoPostalDetailsComponentType = InstanceType<typeof CodigoPostalDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const codigoPostalSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CodigoPostal Management Detail Component', () => {
    let codigoPostalServiceStub: SinonStubbedInstance<CodigoPostalService>;
    let mountOptions: MountingOptions<CodigoPostalDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      codigoPostalServiceStub = sinon.createStubInstance<CodigoPostalService>(CodigoPostalService);

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
          codigoPostalService: () => codigoPostalServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        codigoPostalServiceStub.find.resolves(codigoPostalSample);
        route = {
          params: {
            codigoPostalId: `${123}`,
          },
        };
        const wrapper = shallowMount(CodigoPostalDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.codigoPostal).toMatchObject(codigoPostalSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        codigoPostalServiceStub.find.resolves(codigoPostalSample);
        const wrapper = shallowMount(CodigoPostalDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
