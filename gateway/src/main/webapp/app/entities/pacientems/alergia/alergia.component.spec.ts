/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Alergia from './alergia.vue';
import AlergiaService from './alergia.service';
import AlertService from '@/shared/alert/alert.service';

type AlergiaComponentType = InstanceType<typeof Alergia>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Alergia Management Component', () => {
    let alergiaServiceStub: SinonStubbedInstance<AlergiaService>;
    let mountOptions: MountingOptions<AlergiaComponentType>['global'];

    beforeEach(() => {
      alergiaServiceStub = sinon.createStubInstance<AlergiaService>(AlergiaService);
      alergiaServiceStub.retrieve.resolves({ headers: {} });

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
          alergiaService: () => alergiaServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        alergiaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Alergia, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(alergiaServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.alergias[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: AlergiaComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Alergia, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        alergiaServiceStub.retrieve.reset();
        alergiaServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        alergiaServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAlergia();
        await comp.$nextTick(); // clear components

        // THEN
        expect(alergiaServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(alergiaServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
