/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Direccion from './direccion.vue';
import DireccionService from './direccion.service';
import AlertService from '@/shared/alert/alert.service';

type DireccionComponentType = InstanceType<typeof Direccion>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Direccion Management Component', () => {
    let direccionServiceStub: SinonStubbedInstance<DireccionService>;
    let mountOptions: MountingOptions<DireccionComponentType>['global'];

    beforeEach(() => {
      direccionServiceStub = sinon.createStubInstance<DireccionService>(DireccionService);
      direccionServiceStub.retrieve.resolves({ headers: {} });

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
          direccionService: () => direccionServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        direccionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Direccion, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(direccionServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.direccions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: DireccionComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Direccion, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        direccionServiceStub.retrieve.reset();
        direccionServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        direccionServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeDireccion();
        await comp.$nextTick(); // clear components

        // THEN
        expect(direccionServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(direccionServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
