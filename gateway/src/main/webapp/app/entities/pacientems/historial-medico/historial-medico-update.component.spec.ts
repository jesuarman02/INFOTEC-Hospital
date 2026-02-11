/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import HistorialMedicoUpdate from './historial-medico-update.vue';
import HistorialMedicoService from './historial-medico.service';
import AlertService from '@/shared/alert/alert.service';

type HistorialMedicoUpdateComponentType = InstanceType<typeof HistorialMedicoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const historialMedicoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<HistorialMedicoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('HistorialMedico Management Update Component', () => {
    let comp: HistorialMedicoUpdateComponentType;
    let historialMedicoServiceStub: SinonStubbedInstance<HistorialMedicoService>;

    beforeEach(() => {
      route = {};
      historialMedicoServiceStub = sinon.createStubInstance<HistorialMedicoService>(HistorialMedicoService);
      historialMedicoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          historialMedicoService: () => historialMedicoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(HistorialMedicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.historialMedico = historialMedicoSample;
        historialMedicoServiceStub.update.resolves(historialMedicoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(historialMedicoServiceStub.update.calledWith(historialMedicoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        historialMedicoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(HistorialMedicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.historialMedico = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(historialMedicoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        historialMedicoServiceStub.find.resolves(historialMedicoSample);
        historialMedicoServiceStub.retrieve.resolves([historialMedicoSample]);

        // WHEN
        route = {
          params: {
            historialMedicoId: `${historialMedicoSample.id}`,
          },
        };
        const wrapper = shallowMount(HistorialMedicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.historialMedico).toMatchObject(historialMedicoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        historialMedicoServiceStub.find.resolves(historialMedicoSample);
        const wrapper = shallowMount(HistorialMedicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
