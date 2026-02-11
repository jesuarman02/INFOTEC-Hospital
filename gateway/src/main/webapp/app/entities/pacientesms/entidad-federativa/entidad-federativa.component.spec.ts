/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import EntidadFederativa from './entidad-federativa.vue';
import EntidadFederativaService from './entidad-federativa.service';
import AlertService from '@/shared/alert/alert.service';

type EntidadFederativaComponentType = InstanceType<typeof EntidadFederativa>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('EntidadFederativa Management Component', () => {
    let entidadFederativaServiceStub: SinonStubbedInstance<EntidadFederativaService>;
    let mountOptions: MountingOptions<EntidadFederativaComponentType>['global'];

    beforeEach(() => {
      entidadFederativaServiceStub = sinon.createStubInstance<EntidadFederativaService>(EntidadFederativaService);
      entidadFederativaServiceStub.retrieve.resolves({ headers: {} });

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
          entidadFederativaService: () => entidadFederativaServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        entidadFederativaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(EntidadFederativa, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(entidadFederativaServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.entidadFederativas[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: EntidadFederativaComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(EntidadFederativa, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        entidadFederativaServiceStub.retrieve.reset();
        entidadFederativaServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        entidadFederativaServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeEntidadFederativa();
        await comp.$nextTick(); // clear components

        // THEN
        expect(entidadFederativaServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(entidadFederativaServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
