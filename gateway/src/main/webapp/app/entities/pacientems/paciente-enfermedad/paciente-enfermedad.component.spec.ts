/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PacienteEnfermedad from './paciente-enfermedad.vue';
import PacienteEnfermedadService from './paciente-enfermedad.service';
import AlertService from '@/shared/alert/alert.service';

type PacienteEnfermedadComponentType = InstanceType<typeof PacienteEnfermedad>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PacienteEnfermedad Management Component', () => {
    let pacienteEnfermedadServiceStub: SinonStubbedInstance<PacienteEnfermedadService>;
    let mountOptions: MountingOptions<PacienteEnfermedadComponentType>['global'];

    beforeEach(() => {
      pacienteEnfermedadServiceStub = sinon.createStubInstance<PacienteEnfermedadService>(PacienteEnfermedadService);
      pacienteEnfermedadServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          pacienteEnfermedadService: () => pacienteEnfermedadServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        pacienteEnfermedadServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PacienteEnfermedad, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(pacienteEnfermedadServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.pacienteEnfermedads[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: PacienteEnfermedadComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PacienteEnfermedad, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        pacienteEnfermedadServiceStub.retrieve.reset();
        pacienteEnfermedadServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        pacienteEnfermedadServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePacienteEnfermedad();
        await comp.$nextTick(); // clear components

        // THEN
        expect(pacienteEnfermedadServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(pacienteEnfermedadServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
