/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AlergiaUpdate from './alergia-update.vue';
import AlergiaService from './alergia.service';
import AlertService from '@/shared/alert/alert.service';

type AlergiaUpdateComponentType = InstanceType<typeof AlergiaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const alergiaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AlergiaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Alergia Management Update Component', () => {
    let comp: AlergiaUpdateComponentType;
    let alergiaServiceStub: SinonStubbedInstance<AlergiaService>;

    beforeEach(() => {
      route = {};
      alergiaServiceStub = sinon.createStubInstance<AlergiaService>(AlergiaService);
      alergiaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          alergiaService: () => alergiaServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(AlergiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.alergia = alergiaSample;
        alergiaServiceStub.update.resolves(alergiaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(alergiaServiceStub.update.calledWith(alergiaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        alergiaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AlergiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.alergia = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(alergiaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        alergiaServiceStub.find.resolves(alergiaSample);
        alergiaServiceStub.retrieve.resolves([alergiaSample]);

        // WHEN
        route = {
          params: {
            alergiaId: `${alergiaSample.id}`,
          },
        };
        const wrapper = shallowMount(AlergiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.alergia).toMatchObject(alergiaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        alergiaServiceStub.find.resolves(alergiaSample);
        const wrapper = shallowMount(AlergiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
