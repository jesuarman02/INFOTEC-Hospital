/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import InfoSocioeconomica from './info-socioeconomica.vue';
import InfoSocioeconomicaService from './info-socioeconomica.service';
import AlertService from '@/shared/alert/alert.service';

type InfoSocioeconomicaComponentType = InstanceType<typeof InfoSocioeconomica>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('InfoSocioeconomica Management Component', () => {
    let infoSocioeconomicaServiceStub: SinonStubbedInstance<InfoSocioeconomicaService>;
    let mountOptions: MountingOptions<InfoSocioeconomicaComponentType>['global'];

    beforeEach(() => {
      infoSocioeconomicaServiceStub = sinon.createStubInstance<InfoSocioeconomicaService>(InfoSocioeconomicaService);
      infoSocioeconomicaServiceStub.retrieve.resolves({ headers: {} });

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
          infoSocioeconomicaService: () => infoSocioeconomicaServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        infoSocioeconomicaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(InfoSocioeconomica, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(infoSocioeconomicaServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.infoSocioeconomicas[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: InfoSocioeconomicaComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(InfoSocioeconomica, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        infoSocioeconomicaServiceStub.retrieve.reset();
        infoSocioeconomicaServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        infoSocioeconomicaServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeInfoSocioeconomica();
        await comp.$nextTick(); // clear components

        // THEN
        expect(infoSocioeconomicaServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(infoSocioeconomicaServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
