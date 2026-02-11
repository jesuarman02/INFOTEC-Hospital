/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MedicamentoUpdate from './medicamento-update.vue';
import MedicamentoService from './medicamento.service';
import AlertService from '@/shared/alert/alert.service';

type MedicamentoUpdateComponentType = InstanceType<typeof MedicamentoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const medicamentoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MedicamentoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Medicamento Management Update Component', () => {
    let comp: MedicamentoUpdateComponentType;
    let medicamentoServiceStub: SinonStubbedInstance<MedicamentoService>;

    beforeEach(() => {
      route = {};
      medicamentoServiceStub = sinon.createStubInstance<MedicamentoService>(MedicamentoService);
      medicamentoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          medicamentoService: () => medicamentoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(MedicamentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.medicamento = medicamentoSample;
        medicamentoServiceStub.update.resolves(medicamentoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(medicamentoServiceStub.update.calledWith(medicamentoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        medicamentoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MedicamentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.medicamento = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(medicamentoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        medicamentoServiceStub.find.resolves(medicamentoSample);
        medicamentoServiceStub.retrieve.resolves([medicamentoSample]);

        // WHEN
        route = {
          params: {
            medicamentoId: `${medicamentoSample.id}`,
          },
        };
        const wrapper = shallowMount(MedicamentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.medicamento).toMatchObject(medicamentoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        medicamentoServiceStub.find.resolves(medicamentoSample);
        const wrapper = shallowMount(MedicamentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
