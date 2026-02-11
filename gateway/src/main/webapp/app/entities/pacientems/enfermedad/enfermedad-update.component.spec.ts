/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import EnfermedadUpdate from './enfermedad-update.vue';
import EnfermedadService from './enfermedad.service';
import AlertService from '@/shared/alert/alert.service';

type EnfermedadUpdateComponentType = InstanceType<typeof EnfermedadUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const enfermedadSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EnfermedadUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Enfermedad Management Update Component', () => {
    let comp: EnfermedadUpdateComponentType;
    let enfermedadServiceStub: SinonStubbedInstance<EnfermedadService>;

    beforeEach(() => {
      route = {};
      enfermedadServiceStub = sinon.createStubInstance<EnfermedadService>(EnfermedadService);
      enfermedadServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          enfermedadService: () => enfermedadServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EnfermedadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.enfermedad = enfermedadSample;
        enfermedadServiceStub.update.resolves(enfermedadSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(enfermedadServiceStub.update.calledWith(enfermedadSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        enfermedadServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EnfermedadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.enfermedad = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(enfermedadServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        enfermedadServiceStub.find.resolves(enfermedadSample);
        enfermedadServiceStub.retrieve.resolves([enfermedadSample]);

        // WHEN
        route = {
          params: {
            enfermedadId: `${enfermedadSample.id}`,
          },
        };
        const wrapper = shallowMount(EnfermedadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.enfermedad).toMatchObject(enfermedadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        enfermedadServiceStub.find.resolves(enfermedadSample);
        const wrapper = shallowMount(EnfermedadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
