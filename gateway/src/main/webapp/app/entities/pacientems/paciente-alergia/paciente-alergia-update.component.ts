import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PacienteAlergiaService from './paciente-alergia.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import AlergiaService from '@/entities/pacientems/alergia/alergia.service';
import { type IAlergia } from '@/shared/model/pacientems/alergia.model';
import { type IPacienteAlergia, PacienteAlergia } from '@/shared/model/pacientems/paciente-alergia.model';
import { GradoAlergia } from '@/shared/model/enumerations/grado-alergia.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteAlergiaUpdate',
  setup() {
    const pacienteAlergiaService = inject('pacienteAlergiaService', () => new PacienteAlergiaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const pacienteAlergia: Ref<IPacienteAlergia> = ref(new PacienteAlergia());

    const pacienteService = inject('pacienteService', () => new PacienteService());

    const pacientes: Ref<IPaciente[]> = ref([]);

    const alergiaService = inject('alergiaService', () => new AlergiaService());

    const alergias: Ref<IAlergia[]> = ref([]);
    const gradoAlergiaValues: Ref<string[]> = ref(Object.keys(GradoAlergia));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePacienteAlergia = async pacienteAlergiaId => {
      try {
        const res = await pacienteAlergiaService().find(pacienteAlergiaId);
        pacienteAlergia.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteAlergiaId) {
      retrievePacienteAlergia(route.params.pacienteAlergiaId);
    }

    const initRelationships = () => {
      pacienteService()
        .retrieve()
        .then(res => {
          pacientes.value = res.data;
        });
      alergiaService()
        .retrieve()
        .then(res => {
          alergias.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fechaDeteccion: {},
      reaccion: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 150 }).toString(), 150),
      },
      gravedad: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      paciente: {},
      alergia: {},
    };
    const v$ = useVuelidate(validationRules, pacienteAlergia as any);
    v$.value.$validate();

    return {
      pacienteAlergiaService,
      alertService,
      pacienteAlergia,
      previousState,
      gradoAlergiaValues,
      isSaving,
      currentLanguage,
      pacientes,
      alergias,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.pacienteAlergia.id) {
        this.pacienteAlergiaService()
          .update(this.pacienteAlergia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsPacienteAlergia.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.pacienteAlergiaService()
          .create(this.pacienteAlergia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsPacienteAlergia.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
