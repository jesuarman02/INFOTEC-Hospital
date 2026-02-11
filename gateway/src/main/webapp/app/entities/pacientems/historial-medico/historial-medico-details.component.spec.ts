/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import HistorialMedicoDetails from './historial-medico-details.vue';
import HistorialMedicoService from './historial-medico.service';
import AlertService from '@/shared/alert/alert.service';

type HistorialMedicoDetailsComponentType = InstanceType<typeof HistorialMedicoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const historialMedicoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('HistorialMedico Management Detail Component', () => {
    let historialMedicoServiceStub: SinonStubbedInstance<HistorialMedicoService>;
    let mountOptions: MountingOptions<HistorialMedicoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      historialMedicoServiceStub = sinon.createStubInstance<HistorialMedicoService>(HistorialMedicoService);

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
          historialMedicoService: () => historialMedicoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        historialMedicoServiceStub.find.resolves(historialMedicoSample);
        route = {
          params: {
            historialMedicoId: `${123}`,
          },
        };
        const wrapper = shallowMount(HistorialMedicoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.historialMedico).toMatchObject(historialMedicoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        historialMedicoServiceStub.find.resolves(historialMedicoSample);
        const wrapper = shallowMount(HistorialMedicoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
