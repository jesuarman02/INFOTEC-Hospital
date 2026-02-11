/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AlergiaDetails from './alergia-details.vue';
import AlergiaService from './alergia.service';
import AlertService from '@/shared/alert/alert.service';

type AlergiaDetailsComponentType = InstanceType<typeof AlergiaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const alergiaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Alergia Management Detail Component', () => {
    let alergiaServiceStub: SinonStubbedInstance<AlergiaService>;
    let mountOptions: MountingOptions<AlergiaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      alergiaServiceStub = sinon.createStubInstance<AlergiaService>(AlergiaService);

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
          alergiaService: () => alergiaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        alergiaServiceStub.find.resolves(alergiaSample);
        route = {
          params: {
            alergiaId: `${123}`,
          },
        };
        const wrapper = shallowMount(AlergiaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.alergia).toMatchObject(alergiaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        alergiaServiceStub.find.resolves(alergiaSample);
        const wrapper = shallowMount(AlergiaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
