/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import InfoSocioeconomicaUpdate from './info-socioeconomica-update.vue';
import InfoSocioeconomicaService from './info-socioeconomica.service';
import AlertService from '@/shared/alert/alert.service';

type InfoSocioeconomicaUpdateComponentType = InstanceType<typeof InfoSocioeconomicaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const infoSocioeconomicaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<InfoSocioeconomicaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('InfoSocioeconomica Management Update Component', () => {
    let comp: InfoSocioeconomicaUpdateComponentType;
    let infoSocioeconomicaServiceStub: SinonStubbedInstance<InfoSocioeconomicaService>;

    beforeEach(() => {
      route = {};
      infoSocioeconomicaServiceStub = sinon.createStubInstance<InfoSocioeconomicaService>(InfoSocioeconomicaService);
      infoSocioeconomicaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          infoSocioeconomicaService: () => infoSocioeconomicaServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(InfoSocioeconomicaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.infoSocioeconomica = infoSocioeconomicaSample;
        infoSocioeconomicaServiceStub.update.resolves(infoSocioeconomicaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(infoSocioeconomicaServiceStub.update.calledWith(infoSocioeconomicaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        infoSocioeconomicaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(InfoSocioeconomicaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.infoSocioeconomica = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(infoSocioeconomicaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        infoSocioeconomicaServiceStub.find.resolves(infoSocioeconomicaSample);
        infoSocioeconomicaServiceStub.retrieve.resolves([infoSocioeconomicaSample]);

        // WHEN
        route = {
          params: {
            infoSocioeconomicaId: `${infoSocioeconomicaSample.id}`,
          },
        };
        const wrapper = shallowMount(InfoSocioeconomicaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.infoSocioeconomica).toMatchObject(infoSocioeconomicaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        infoSocioeconomicaServiceStub.find.resolves(infoSocioeconomicaSample);
        const wrapper = shallowMount(InfoSocioeconomicaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
