/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import InfoSocioeconomicaDetails from './info-socioeconomica-details.vue';
import InfoSocioeconomicaService from './info-socioeconomica.service';
import AlertService from '@/shared/alert/alert.service';

type InfoSocioeconomicaDetailsComponentType = InstanceType<typeof InfoSocioeconomicaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const infoSocioeconomicaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('InfoSocioeconomica Management Detail Component', () => {
    let infoSocioeconomicaServiceStub: SinonStubbedInstance<InfoSocioeconomicaService>;
    let mountOptions: MountingOptions<InfoSocioeconomicaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      infoSocioeconomicaServiceStub = sinon.createStubInstance<InfoSocioeconomicaService>(InfoSocioeconomicaService);

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
          infoSocioeconomicaService: () => infoSocioeconomicaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        infoSocioeconomicaServiceStub.find.resolves(infoSocioeconomicaSample);
        route = {
          params: {
            infoSocioeconomicaId: `${123}`,
          },
        };
        const wrapper = shallowMount(InfoSocioeconomicaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.infoSocioeconomica).toMatchObject(infoSocioeconomicaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        infoSocioeconomicaServiceStub.find.resolves(infoSocioeconomicaSample);
        const wrapper = shallowMount(InfoSocioeconomicaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
