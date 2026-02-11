/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PacienteAlergia from './paciente-alergia.vue';
import PacienteAlergiaService from './paciente-alergia.service';
import AlertService from '@/shared/alert/alert.service';

type PacienteAlergiaComponentType = InstanceType<typeof PacienteAlergia>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PacienteAlergia Management Component', () => {
    let pacienteAlergiaServiceStub: SinonStubbedInstance<PacienteAlergiaService>;
    let mountOptions: MountingOptions<PacienteAlergiaComponentType>['global'];

    beforeEach(() => {
      pacienteAlergiaServiceStub = sinon.createStubInstance<PacienteAlergiaService>(PacienteAlergiaService);
      pacienteAlergiaServiceStub.retrieve.resolves({ headers: {} });

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
          pacienteAlergiaService: () => pacienteAlergiaServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        pacienteAlergiaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PacienteAlergia, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(pacienteAlergiaServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.pacienteAlergias[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: PacienteAlergiaComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PacienteAlergia, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        pacienteAlergiaServiceStub.retrieve.reset();
        pacienteAlergiaServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        pacienteAlergiaServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePacienteAlergia();
        await comp.$nextTick(); // clear components

        // THEN
        expect(pacienteAlergiaServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(pacienteAlergiaServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
