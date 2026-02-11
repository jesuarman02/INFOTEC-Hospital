/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SignosVitalesDetails from './signos-vitales-details.vue';
import SignosVitalesService from './signos-vitales.service';
import AlertService from '@/shared/alert/alert.service';

type SignosVitalesDetailsComponentType = InstanceType<typeof SignosVitalesDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const signosVitalesSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SignosVitales Management Detail Component', () => {
    let signosVitalesServiceStub: SinonStubbedInstance<SignosVitalesService>;
    let mountOptions: MountingOptions<SignosVitalesDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      signosVitalesServiceStub = sinon.createStubInstance<SignosVitalesService>(SignosVitalesService);

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
          signosVitalesService: () => signosVitalesServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        signosVitalesServiceStub.find.resolves(signosVitalesSample);
        route = {
          params: {
            signosVitalesId: `${123}`,
          },
        };
        const wrapper = shallowMount(SignosVitalesDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.signosVitales).toMatchObject(signosVitalesSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        signosVitalesServiceStub.find.resolves(signosVitalesSample);
        const wrapper = shallowMount(SignosVitalesDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
