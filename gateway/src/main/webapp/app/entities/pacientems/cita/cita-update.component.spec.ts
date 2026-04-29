/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import CitaUpdate from './cita-update.vue';
import CitaService from './cita.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

type CitaUpdateComponentType = InstanceType<typeof CitaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const citaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CitaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Cita Management Update Component', () => {
    let comp: CitaUpdateComponentType;
    let citaServiceStub: SinonStubbedInstance<CitaService>;

    beforeEach(() => {
      route = {};
      citaServiceStub = sinon.createStubInstance<CitaService>(CitaService);
      citaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          citaService: () => citaServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(CitaUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CitaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cita = citaSample;
        citaServiceStub.update.resolves(citaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(citaServiceStub.update.calledWith(citaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        citaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CitaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cita = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(citaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        citaServiceStub.find.resolves(citaSample);
        citaServiceStub.retrieve.resolves([citaSample]);

        // WHEN
        route = {
          params: {
            citaId: `${citaSample.id}`,
          },
        };
        const wrapper = shallowMount(CitaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cita).toMatchObject(citaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        citaServiceStub.find.resolves(citaSample);
        const wrapper = shallowMount(CitaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
