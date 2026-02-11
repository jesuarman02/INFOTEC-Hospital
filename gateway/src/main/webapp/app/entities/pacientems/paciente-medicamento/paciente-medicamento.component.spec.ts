/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PacienteMedicamento from './paciente-medicamento.vue';
import PacienteMedicamentoService from './paciente-medicamento.service';
import AlertService from '@/shared/alert/alert.service';

type PacienteMedicamentoComponentType = InstanceType<typeof PacienteMedicamento>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PacienteMedicamento Management Component', () => {
    let pacienteMedicamentoServiceStub: SinonStubbedInstance<PacienteMedicamentoService>;
    let mountOptions: MountingOptions<PacienteMedicamentoComponentType>['global'];

    beforeEach(() => {
      pacienteMedicamentoServiceStub = sinon.createStubInstance<PacienteMedicamentoService>(PacienteMedicamentoService);
      pacienteMedicamentoServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          pacienteMedicamentoService: () => pacienteMedicamentoServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        pacienteMedicamentoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PacienteMedicamento, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(pacienteMedicamentoServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.pacienteMedicamentos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(PacienteMedicamento, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(pacienteMedicamentoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: PacienteMedicamentoComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PacienteMedicamento, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        pacienteMedicamentoServiceStub.retrieve.reset();
        pacienteMedicamentoServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        pacienteMedicamentoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(pacienteMedicamentoServiceStub.retrieve.called).toBeTruthy();
        expect(comp.pacienteMedicamentos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(pacienteMedicamentoServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        pacienteMedicamentoServiceStub.retrieve.reset();
        pacienteMedicamentoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(pacienteMedicamentoServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.pacienteMedicamentos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(pacienteMedicamentoServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        pacienteMedicamentoServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePacienteMedicamento();
        await comp.$nextTick(); // clear components

        // THEN
        expect(pacienteMedicamentoServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(pacienteMedicamentoServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
