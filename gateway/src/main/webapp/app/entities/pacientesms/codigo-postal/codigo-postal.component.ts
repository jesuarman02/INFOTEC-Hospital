import { type Ref, defineComponent, inject, onMounted, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';

import CodigoPostalService from './codigo-postal.service';
import { type ICodigoPostal } from '@/shared/model/pacientesms/codigo-postal.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CodigoPostal',
  setup() {
    const { t: t$ } = useI18n();
    const codigoPostalService = inject('codigoPostalService', () => new CodigoPostalService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);
    
    const cpSelected = ref('');

    const codigoPostals: Ref<ICodigoPostal[]> = ref([]);
    const isFetching = ref(false);

    const clear = () => {
      page.value = 1;
    };

    const sort = (): Array<any> => {
      const result = [`${propOrder.value},${reverse.value ? 'desc' : 'asc'}`];
      if (propOrder.value !== 'id') {
        result.push('id');
      }
      return result;
    };

    const retrieveCodigoPostals = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };
        const res = await codigoPostalService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        codigoPostals.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const retrieveByCP = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };

        const res = await codigoPostalService().retrieveByCP(cpSelected.value, paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        codigoPostals.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const findByPostalCode = async () => {

      if (cpSelected.value) {
         await retrieveByCP();
      } else {
         await retrieveCodigoPostals();
      }
    };

    const handleSyncList = () => {
      retrieveCodigoPostals();
    };

    onMounted(async () => {
      await retrieveCodigoPostals();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICodigoPostal) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCodigoPostal = async () => {
      try {
        await codigoPostalService().delete(removeId.value);
        const message = t$('pacientesmsApp.pacientesmsCodigoPostal.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCodigoPostals();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const changeOrder = (newOrder: string) => {
      if (propOrder.value === newOrder) {
        reverse.value = !reverse.value;
      } else {
        reverse.value = false;
      }
      propOrder.value = newOrder;
    };

    // Whenever order changes, reset the pagination
    watch([propOrder, reverse], async () => {
      if (page.value === 1) {
        // Si hay un CP seleccionado, usamos la búsqueda filtrada, si no, la general
        if (cpSelected.value) {
            await retrieveByCP();
        } else {
            await retrieveCodigoPostals();
        }
      } else {
        clear();
      }
    });

    // Whenever page changes, switch to the new page.
    watch(page, async () => {
      if (cpSelected.value) {
          await retrieveByCP();
      } else {
          await retrieveCodigoPostals();
      }
    });

    return {
      codigoPostals,
      handleSyncList,
      isFetching,
      retrieveCodigoPostals,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCodigoPostal,
      itemsPerPage,
      queryCount,
      page,
      propOrder,
      reverse,
      totalItems,
      changeOrder,
      t$,
      cpSelected,
      retrieveByCP,
      findByPostalCode,
    };
  },
});