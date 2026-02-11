import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PacienteEnfermedadService from './paciente-enfermedad.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import EnfermedadService from '@/entities/pacientems/enfermedad/enfermedad.service';
import { type IEnfermedad } from '@/shared/model/pacientems/enfermedad.model';
import { type IPacienteEnfermedad, PacienteEnfermedad } from '@/shared/model/pacientems/paciente-enfermedad.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteEnfermedadUpdate',
  setup() {
    const pacienteEnfermedadService = inject('pacienteEnfermedadService', () => new PacienteEnfermedadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const pacienteEnfermedad: Ref<IPacienteEnfermedad> = ref(new PacienteEnfermedad());

    const pacienteService = inject('pacienteService', () => new PacienteService());

    const pacientes: Ref<IPaciente[]> = ref([]);

    const enfermedadService = inject('enfermedadService', () => new EnfermedadService());

    const enfermedads: Ref<IEnfermedad[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePacienteEnfermedad = async pacienteEnfermedadId => {
      try {
        const res = await pacienteEnfermedadService().find(pacienteEnfermedadId);
        pacienteEnfermedad.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteEnfermedadId) {
      retrievePacienteEnfermedad(route.params.pacienteEnfermedadId);
    }

    const initRelationships = () => {
      pacienteService()
        .retrieve()
        .then(res => {
          pacientes.value = res.data;
        });
      enfermedadService()
        .retrieve()
        .then(res => {
          enfermedads.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fechaDiagnostico: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      estado: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 20 }).toString(), 20),
      },
      notas: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      paciente: {},
      enfermedad: {},
    };
    const v$ = useVuelidate(validationRules, pacienteEnfermedad as any);
    v$.value.$validate();

    return {
      pacienteEnfermedadService,
      alertService,
      pacienteEnfermedad,
      previousState,
      isSaving,
      currentLanguage,
      pacientes,
      enfermedads,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.pacienteEnfermedad.id) {
        this.pacienteEnfermedadService()
          .update(this.pacienteEnfermedad)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsPacienteEnfermedad.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.pacienteEnfermedadService()
          .create(this.pacienteEnfermedad)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsPacienteEnfermedad.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
