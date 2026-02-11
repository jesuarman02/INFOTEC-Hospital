/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TipoVialidadUpdate from './tipo-vialidad-update.vue';
import TipoVialidadService from './tipo-vialidad.service';
import AlertService from '@/shared/alert/alert.service';

type TipoVialidadUpdateComponentType = InstanceType<typeof TipoVialidadUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tipoVialidadSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TipoVialidadUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TipoVialidad Management Update Component', () => {
    let comp: TipoVialidadUpdateComponentType;
    let tipoVialidadServiceStub: SinonStubbedInstance<TipoVialidadService>;

    beforeEach(() => {
      route = {};
      tipoVialidadServiceStub = sinon.createStubInstance<TipoVialidadService>(TipoVialidadService);
      tipoVialidadServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          tipoVialidadService: () => tipoVialidadServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TipoVialidadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tipoVialidad = tipoVialidadSample;
        tipoVialidadServiceStub.update.resolves(tipoVialidadSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tipoVialidadServiceStub.update.calledWith(tipoVialidadSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tipoVialidadServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TipoVialidadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tipoVialidad = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tipoVialidadServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tipoVialidadServiceStub.find.resolves(tipoVialidadSample);
        tipoVialidadServiceStub.retrieve.resolves([tipoVialidadSample]);

        // WHEN
        route = {
          params: {
            tipoVialidadId: `${tipoVialidadSample.id}`,
          },
        };
        const wrapper = shallowMount(TipoVialidadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.tipoVialidad).toMatchObject(tipoVialidadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tipoVialidadServiceStub.find.resolves(tipoVialidadSample);
        const wrapper = shallowMount(TipoVialidadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
