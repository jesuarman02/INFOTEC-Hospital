/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PacienteAlergiaUpdate from './paciente-alergia-update.vue';
import PacienteAlergiaService from './paciente-alergia.service';
import AlertService from '@/shared/alert/alert.service';

import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import AlergiaService from '@/entities/pacientems/alergia/alergia.service';

type PacienteAlergiaUpdateComponentType = InstanceType<typeof PacienteAlergiaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pacienteAlergiaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PacienteAlergiaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PacienteAlergia Management Update Component', () => {
    let comp: PacienteAlergiaUpdateComponentType;
    let pacienteAlergiaServiceStub: SinonStubbedInstance<PacienteAlergiaService>;

    beforeEach(() => {
      route = {};
      pacienteAlergiaServiceStub = sinon.createStubInstance<PacienteAlergiaService>(PacienteAlergiaService);
      pacienteAlergiaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          pacienteAlergiaService: () => pacienteAlergiaServiceStub,
          pacienteService: () =>
            sinon.createStubInstance<PacienteService>(PacienteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          alergiaService: () =>
            sinon.createStubInstance<AlergiaService>(AlergiaService, {
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
        const wrapper = shallowMount(PacienteAlergiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pacienteAlergia = pacienteAlergiaSample;
        pacienteAlergiaServiceStub.update.resolves(pacienteAlergiaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteAlergiaServiceStub.update.calledWith(pacienteAlergiaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        pacienteAlergiaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PacienteAlergiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pacienteAlergia = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteAlergiaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        pacienteAlergiaServiceStub.find.resolves(pacienteAlergiaSample);
        pacienteAlergiaServiceStub.retrieve.resolves([pacienteAlergiaSample]);

        // WHEN
        route = {
          params: {
            pacienteAlergiaId: `${pacienteAlergiaSample.id}`,
          },
        };
        const wrapper = shallowMount(PacienteAlergiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.pacienteAlergia).toMatchObject(pacienteAlergiaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pacienteAlergiaServiceStub.find.resolves(pacienteAlergiaSample);
        const wrapper = shallowMount(PacienteAlergiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
