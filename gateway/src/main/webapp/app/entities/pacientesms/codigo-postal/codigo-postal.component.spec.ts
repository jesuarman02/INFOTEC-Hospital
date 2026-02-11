/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CodigoPostal from './codigo-postal.vue';
import CodigoPostalService from './codigo-postal.service';
import AlertService from '@/shared/alert/alert.service';

type CodigoPostalComponentType = InstanceType<typeof CodigoPostal>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CodigoPostal Management Component', () => {
    let codigoPostalServiceStub: SinonStubbedInstance<CodigoPostalService>;
    let mountOptions: MountingOptions<CodigoPostalComponentType>['global'];

    beforeEach(() => {
      codigoPostalServiceStub = sinon.createStubInstance<CodigoPostalService>(CodigoPostalService);
      codigoPostalServiceStub.retrieve.resolves({ headers: {} });

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
          codigoPostalService: () => codigoPostalServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        codigoPostalServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CodigoPostal, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(codigoPostalServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.codigoPostals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(CodigoPostal, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(codigoPostalServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: CodigoPostalComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CodigoPostal, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        codigoPostalServiceStub.retrieve.reset();
        codigoPostalServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        codigoPostalServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(codigoPostalServiceStub.retrieve.called).toBeTruthy();
        expect(comp.codigoPostals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(codigoPostalServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        codigoPostalServiceStub.retrieve.reset();
        codigoPostalServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(codigoPostalServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.codigoPostals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(codigoPostalServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        codigoPostalServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCodigoPostal();
        await comp.$nextTick(); // clear components

        // THEN
        expect(codigoPostalServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(codigoPostalServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
