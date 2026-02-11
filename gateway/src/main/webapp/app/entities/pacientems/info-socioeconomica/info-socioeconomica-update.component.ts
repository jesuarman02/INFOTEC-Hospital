import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import InfoSocioeconomicaService from './info-socioeconomica.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IInfoSocioeconomica, InfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InfoSocioeconomicaUpdate',
  setup() {
    const infoSocioeconomicaService = inject('infoSocioeconomicaService', () => new InfoSocioeconomicaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const infoSocioeconomica: Ref<IInfoSocioeconomica> = ref(new InfoSocioeconomica());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveInfoSocioeconomica = async infoSocioeconomicaId => {
      try {
        const res = await infoSocioeconomicaService().find(infoSocioeconomicaId);
        infoSocioeconomica.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.infoSocioeconomicaId) {
      retrieveInfoSocioeconomica(route.params.infoSocioeconomicaId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      tipoVivienda: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      materialVivienda: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      numeroHabitaciones: {},
      numeroHabitantes: {},
      serviciosDisponibles: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      ingresoMensual: {},
      ingresoMensualHogar: {},
      gastoMensual: {},
      personasDependientes: {},
      apoyosGubernamentales: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      ocupacionActual: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      condicionLaboral: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      tipoEmpleo: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      lugarTrabajo: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 150 }).toString(), 150),
      },
      tiempoEmpleado: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      gradoMaximoEstudios: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      aniosEstudio: {},
      estudia: {},
      institucionMedica: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      tipoAfiliacion: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      numeroAfiliacion: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 30 }).toString(), 30),
      },
      medioTransporte: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      tiempoTraslado: {},
      fechaActualizacion: {},
    };
    const v$ = useVuelidate(validationRules, infoSocioeconomica as any);
    v$.value.$validate();

    return {
      infoSocioeconomicaService,
      alertService,
      infoSocioeconomica,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.infoSocioeconomica.id) {
        this.infoSocioeconomicaService()
          .update(this.infoSocioeconomica)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsInfoSocioeconomica.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.infoSocioeconomicaService()
          .create(this.infoSocioeconomica)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsInfoSocioeconomica.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
