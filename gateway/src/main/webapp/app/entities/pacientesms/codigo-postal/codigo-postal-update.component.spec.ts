/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CodigoPostalUpdate from './codigo-postal-update.vue';
import CodigoPostalService from './codigo-postal.service';
import AlertService from '@/shared/alert/alert.service';

type CodigoPostalUpdateComponentType = InstanceType<typeof CodigoPostalUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const codigoPostalSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CodigoPostalUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CodigoPostal Management Update Component', () => {
    let comp: CodigoPostalUpdateComponentType;
    let codigoPostalServiceStub: SinonStubbedInstance<CodigoPostalService>;

    beforeEach(() => {
      route = {};
      codigoPostalServiceStub = sinon.createStubInstance<CodigoPostalService>(CodigoPostalService);
      codigoPostalServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          codigoPostalService: () => codigoPostalServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CodigoPostalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.codigoPostal = codigoPostalSample;
        codigoPostalServiceStub.update.resolves(codigoPostalSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(codigoPostalServiceStub.update.calledWith(codigoPostalSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        codigoPostalServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CodigoPostalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.codigoPostal = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(codigoPostalServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        codigoPostalServiceStub.find.resolves(codigoPostalSample);
        codigoPostalServiceStub.retrieve.resolves([codigoPostalSample]);

        // WHEN
        route = {
          params: {
            codigoPostalId: `${codigoPostalSample.id}`,
          },
        };
        const wrapper = shallowMount(CodigoPostalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.codigoPostal).toMatchObject(codigoPostalSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        codigoPostalServiceStub.find.resolves(codigoPostalSample);
        const wrapper = shallowMount(CodigoPostalUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
