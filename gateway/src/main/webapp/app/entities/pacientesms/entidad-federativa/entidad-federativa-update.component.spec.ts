/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import EntidadFederativaUpdate from './entidad-federativa-update.vue';
import EntidadFederativaService from './entidad-federativa.service';
import AlertService from '@/shared/alert/alert.service';

type EntidadFederativaUpdateComponentType = InstanceType<typeof EntidadFederativaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const entidadFederativaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EntidadFederativaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('EntidadFederativa Management Update Component', () => {
    let comp: EntidadFederativaUpdateComponentType;
    let entidadFederativaServiceStub: SinonStubbedInstance<EntidadFederativaService>;

    beforeEach(() => {
      route = {};
      entidadFederativaServiceStub = sinon.createStubInstance<EntidadFederativaService>(EntidadFederativaService);
      entidadFederativaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          entidadFederativaService: () => entidadFederativaServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EntidadFederativaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.entidadFederativa = entidadFederativaSample;
        entidadFederativaServiceStub.update.resolves(entidadFederativaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(entidadFederativaServiceStub.update.calledWith(entidadFederativaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        entidadFederativaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EntidadFederativaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.entidadFederativa = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(entidadFederativaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        entidadFederativaServiceStub.find.resolves(entidadFederativaSample);
        entidadFederativaServiceStub.retrieve.resolves([entidadFederativaSample]);

        // WHEN
        route = {
          params: {
            entidadFederativaId: `${entidadFederativaSample.id}`,
          },
        };
        const wrapper = shallowMount(EntidadFederativaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.entidadFederativa).toMatchObject(entidadFederativaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        entidadFederativaServiceStub.find.resolves(entidadFederativaSample);
        const wrapper = shallowMount(EntidadFederativaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
