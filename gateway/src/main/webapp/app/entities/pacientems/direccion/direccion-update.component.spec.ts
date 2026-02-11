/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DireccionUpdate from './direccion-update.vue';
import DireccionService from './direccion.service';
import AlertService from '@/shared/alert/alert.service';

import TipoVialidadService from '@/entities/pacientesms/tipo-vialidad/tipo-vialidad.service';
import CodigoPostalService from '@/entities/pacientesms/codigo-postal/codigo-postal.service';

type DireccionUpdateComponentType = InstanceType<typeof DireccionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const direccionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DireccionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Direccion Management Update Component', () => {
    let comp: DireccionUpdateComponentType;
    let direccionServiceStub: SinonStubbedInstance<DireccionService>;

    beforeEach(() => {
      route = {};
      direccionServiceStub = sinon.createStubInstance<DireccionService>(DireccionService);
      direccionServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          direccionService: () => direccionServiceStub,
          tipoVialidadService: () =>
            sinon.createStubInstance<TipoVialidadService>(TipoVialidadService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          codigoPostalService: () =>
            sinon.createStubInstance<CodigoPostalService>(CodigoPostalService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(DireccionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.direccion = direccionSample;
        direccionServiceStub.update.resolves(direccionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(direccionServiceStub.update.calledWith(direccionSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        direccionServiceStub.create.resolves(entity);
        const wrapper = shallowMount(DireccionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.direccion = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(direccionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        direccionServiceStub.find.resolves(direccionSample);
        direccionServiceStub.retrieve.resolves([direccionSample]);

        // WHEN
        route = {
          params: {
            direccionId: `${direccionSample.id}`,
          },
        };
        const wrapper = shallowMount(DireccionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.direccion).toMatchObject(direccionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        direccionServiceStub.find.resolves(direccionSample);
        const wrapper = shallowMount(DireccionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
