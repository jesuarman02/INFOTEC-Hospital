/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Medicamento from './medicamento.vue';
import MedicamentoService from './medicamento.service';
import AlertService from '@/shared/alert/alert.service';

type MedicamentoComponentType = InstanceType<typeof Medicamento>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Medicamento Management Component', () => {
    let medicamentoServiceStub: SinonStubbedInstance<MedicamentoService>;
    let mountOptions: MountingOptions<MedicamentoComponentType>['global'];

    beforeEach(() => {
      medicamentoServiceStub = sinon.createStubInstance<MedicamentoService>(MedicamentoService);
      medicamentoServiceStub.retrieve.resolves({ headers: {} });

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
          medicamentoService: () => medicamentoServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        medicamentoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Medicamento, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(medicamentoServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.medicamentos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: MedicamentoComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Medicamento, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        medicamentoServiceStub.retrieve.reset();
        medicamentoServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        medicamentoServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMedicamento();
        await comp.$nextTick(); // clear components

        // THEN
        expect(medicamentoServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(medicamentoServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
