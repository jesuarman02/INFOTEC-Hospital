/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import TipoVialidad from './tipo-vialidad.vue';
import TipoVialidadService from './tipo-vialidad.service';
import AlertService from '@/shared/alert/alert.service';

type TipoVialidadComponentType = InstanceType<typeof TipoVialidad>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('TipoVialidad Management Component', () => {
    let tipoVialidadServiceStub: SinonStubbedInstance<TipoVialidadService>;
    let mountOptions: MountingOptions<TipoVialidadComponentType>['global'];

    beforeEach(() => {
      tipoVialidadServiceStub = sinon.createStubInstance<TipoVialidadService>(TipoVialidadService);
      tipoVialidadServiceStub.retrieve.resolves({ headers: {} });

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
          tipoVialidadService: () => tipoVialidadServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tipoVialidadServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(TipoVialidad, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(tipoVialidadServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.tipoVialidads[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: TipoVialidadComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(TipoVialidad, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        tipoVialidadServiceStub.retrieve.reset();
        tipoVialidadServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        tipoVialidadServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeTipoVialidad();
        await comp.$nextTick(); // clear components

        // THEN
        expect(tipoVialidadServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(tipoVialidadServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
