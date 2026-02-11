import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PacienteService from './paciente.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import DireccionService from '@/entities/pacientems/direccion/direccion.service';
import { type IDireccion } from '@/shared/model/pacientems/direccion.model';
import InfoSocioeconomicaService from '@/entities/pacientems/info-socioeconomica/info-socioeconomica.service';
import { type IInfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';
import HistorialMedicoService from '@/entities/pacientems/historial-medico/historial-medico.service';
import { type IHistorialMedico } from '@/shared/model/pacientems/historial-medico.model';
import EntidadFederativaService from '@/entities/pacientesms/entidad-federativa/entidad-federativa.service';
import { type IEntidadFederativa } from '@/shared/model/pacientesms/entidad-federativa.model';
import { type IPaciente, Paciente } from '@/shared/model/pacientems/paciente.model';
import { Sexo } from '@/shared/model/enumerations/sexo.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteUpdate',
  setup() {
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paciente: Ref<IPaciente> = ref(new Paciente());

    const direccionService = inject('direccionService', () => new DireccionService());

    const direccions: Ref<IDireccion[]> = ref([]);

    const infoSocioeconomicaService = inject('infoSocioeconomicaService', () => new InfoSocioeconomicaService());

    const infoSocioeconomicas: Ref<IInfoSocioeconomica[]> = ref([]);

    const historialMedicoService = inject('historialMedicoService', () => new HistorialMedicoService());

    const historialMedicos: Ref<IHistorialMedico[]> = ref([]);

    const entidadFederativaService = inject('entidadFederativaService', () => new EntidadFederativaService());

    const entidadFederativas: Ref<IEntidadFederativa[]> = ref([]);
    const sexoValues: Ref<string[]> = ref(Object.keys(Sexo));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePaciente = async pacienteId => {
      try {
        const res = await pacienteService().find(pacienteId);
        paciente.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteId) {
      retrievePaciente(route.params.pacienteId);
    }

    const initRelationships = () => {
      direccionService()
        .retrieve()
        .then(res => {
          direccions.value = res.data;
        });
      infoSocioeconomicaService()
        .retrieve()
        .then(res => {
          infoSocioeconomicas.value = res.data;
        });
      historialMedicoService()
        .retrieve()
        .then(res => {
          historialMedicos.value = res.data;
        });
      entidadFederativaService()
        .retrieve()
        .then(res => {
          entidadFederativas.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      ecu: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      apellidoPaterno: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      apellidoMaterno: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      sexo: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      nacionalidad: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 40 }).toString(), 40),
      },
      fechaNacimiento: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      estadoCivil: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 20 }).toString(), 20),
      },
      curp: {
        required: validations.required(t$('entity.validation.required').toString()),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 18 }).toString(), 18),
      },
      direccion: {},
      infoSocioeconomica: {},
      historialGeneral: {},
      entidadNacimiento: {},
    };
    const v$ = useVuelidate(validationRules, paciente as any);
    v$.value.$validate();

    return {
      pacienteService,
      alertService,
      paciente,
      previousState,
      sexoValues,
      isSaving,
      currentLanguage,
      direccions,
      infoSocioeconomicas,
      historialMedicos,
      entidadFederativas,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.paciente.id) {
        this.pacienteService()
          .update(this.paciente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsPaciente.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.pacienteService()
          .create(this.paciente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsPaciente.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
