/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import EnfermedadDetails from './enfermedad-details.vue';
import EnfermedadService from './enfermedad.service';
import AlertService from '@/shared/alert/alert.service';

type EnfermedadDetailsComponentType = InstanceType<typeof EnfermedadDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const enfermedadSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Enfermedad Management Detail Component', () => {
    let enfermedadServiceStub: SinonStubbedInstance<EnfermedadService>;
    let mountOptions: MountingOptions<EnfermedadDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      enfermedadServiceStub = sinon.createStubInstance<EnfermedadService>(EnfermedadService);

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
          enfermedadService: () => enfermedadServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        enfermedadServiceStub.find.resolves(enfermedadSample);
        route = {
          params: {
            enfermedadId: `${123}`,
          },
        };
        const wrapper = shallowMount(EnfermedadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.enfermedad).toMatchObject(enfermedadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        enfermedadServiceStub.find.resolves(enfermedadSample);
        const wrapper = shallowMount(EnfermedadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
