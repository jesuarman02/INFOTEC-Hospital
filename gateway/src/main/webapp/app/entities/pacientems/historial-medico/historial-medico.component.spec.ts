/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import HistorialMedico from './historial-medico.vue';
import HistorialMedicoService from './historial-medico.service';
import AlertService from '@/shared/alert/alert.service';

type HistorialMedicoComponentType = InstanceType<typeof HistorialMedico>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('HistorialMedico Management Component', () => {
    let historialMedicoServiceStub: SinonStubbedInstance<HistorialMedicoService>;
    let mountOptions: MountingOptions<HistorialMedicoComponentType>['global'];

    beforeEach(() => {
      historialMedicoServiceStub = sinon.createStubInstance<HistorialMedicoService>(HistorialMedicoService);
      historialMedicoServiceStub.retrieve.resolves({ headers: {} });

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
          historialMedicoService: () => historialMedicoServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        historialMedicoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(HistorialMedico, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(historialMedicoServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.historialMedicos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: HistorialMedicoComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(HistorialMedico, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        historialMedicoServiceStub.retrieve.reset();
        historialMedicoServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        historialMedicoServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeHistorialMedico();
        await comp.$nextTick(); // clear components

        // THEN
        expect(historialMedicoServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(historialMedicoServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
