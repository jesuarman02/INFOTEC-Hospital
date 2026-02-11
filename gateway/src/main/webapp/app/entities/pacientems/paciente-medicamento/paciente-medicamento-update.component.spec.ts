/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PacienteMedicamentoUpdate from './paciente-medicamento-update.vue';
import PacienteMedicamentoService from './paciente-medicamento.service';
import AlertService from '@/shared/alert/alert.service';

import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import MedicamentoService from '@/entities/pacientems/medicamento/medicamento.service';

type PacienteMedicamentoUpdateComponentType = InstanceType<typeof PacienteMedicamentoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pacienteMedicamentoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PacienteMedicamentoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PacienteMedicamento Management Update Component', () => {
    let comp: PacienteMedicamentoUpdateComponentType;
    let pacienteMedicamentoServiceStub: SinonStubbedInstance<PacienteMedicamentoService>;

    beforeEach(() => {
      route = {};
      pacienteMedicamentoServiceStub = sinon.createStubInstance<PacienteMedicamentoService>(PacienteMedicamentoService);
      pacienteMedicamentoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          pacienteMedicamentoService: () => pacienteMedicamentoServiceStub,
          pacienteService: () =>
            sinon.createStubInstance<PacienteService>(PacienteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          medicamentoService: () =>
            sinon.createStubInstance<MedicamentoService>(MedicamentoService, {
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
        const wrapper = shallowMount(PacienteMedicamentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pacienteMedicamento = pacienteMedicamentoSample;
        pacienteMedicamentoServiceStub.update.resolves(pacienteMedicamentoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteMedicamentoServiceStub.update.calledWith(pacienteMedicamentoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        pacienteMedicamentoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PacienteMedicamentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pacienteMedicamento = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteMedicamentoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        pacienteMedicamentoServiceStub.find.resolves(pacienteMedicamentoSample);
        pacienteMedicamentoServiceStub.retrieve.resolves([pacienteMedicamentoSample]);

        // WHEN
        route = {
          params: {
            pacienteMedicamentoId: `${pacienteMedicamentoSample.id}`,
          },
        };
        const wrapper = shallowMount(PacienteMedicamentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.pacienteMedicamento).toMatchObject(pacienteMedicamentoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pacienteMedicamentoServiceStub.find.resolves(pacienteMedicamentoSample);
        const wrapper = shallowMount(PacienteMedicamentoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
