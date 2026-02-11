/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PacienteEnfermedadUpdate from './paciente-enfermedad-update.vue';
import PacienteEnfermedadService from './paciente-enfermedad.service';
import AlertService from '@/shared/alert/alert.service';

import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import EnfermedadService from '@/entities/pacientems/enfermedad/enfermedad.service';

type PacienteEnfermedadUpdateComponentType = InstanceType<typeof PacienteEnfermedadUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pacienteEnfermedadSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PacienteEnfermedadUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PacienteEnfermedad Management Update Component', () => {
    let comp: PacienteEnfermedadUpdateComponentType;
    let pacienteEnfermedadServiceStub: SinonStubbedInstance<PacienteEnfermedadService>;

    beforeEach(() => {
      route = {};
      pacienteEnfermedadServiceStub = sinon.createStubInstance<PacienteEnfermedadService>(PacienteEnfermedadService);
      pacienteEnfermedadServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          pacienteEnfermedadService: () => pacienteEnfermedadServiceStub,
          pacienteService: () =>
            sinon.createStubInstance<PacienteService>(PacienteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          enfermedadService: () =>
            sinon.createStubInstance<EnfermedadService>(EnfermedadService, {
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
        const wrapper = shallowMount(PacienteEnfermedadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pacienteEnfermedad = pacienteEnfermedadSample;
        pacienteEnfermedadServiceStub.update.resolves(pacienteEnfermedadSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteEnfermedadServiceStub.update.calledWith(pacienteEnfermedadSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        pacienteEnfermedadServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PacienteEnfermedadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pacienteEnfermedad = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteEnfermedadServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        pacienteEnfermedadServiceStub.find.resolves(pacienteEnfermedadSample);
        pacienteEnfermedadServiceStub.retrieve.resolves([pacienteEnfermedadSample]);

        // WHEN
        route = {
          params: {
            pacienteEnfermedadId: `${pacienteEnfermedadSample.id}`,
          },
        };
        const wrapper = shallowMount(PacienteEnfermedadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.pacienteEnfermedad).toMatchObject(pacienteEnfermedadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pacienteEnfermedadServiceStub.find.resolves(pacienteEnfermedadSample);
        const wrapper = shallowMount(PacienteEnfermedadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
