import { type Ref, computed, defineComponent, inject, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CitaService from './cita.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

// 🔥 1. IMPORTAMOS EL SERVICIO DE PACIENTES 🔥
import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import { type ICita, Cita } from '@/shared/model/pacientems/cita.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CitaUpdate',
  setup() {
    const citaService = inject('citaService', () => new CitaService());
    const alertService = inject('alertService', () => useAlertService(), true);
    
    // 🔥 2. INYECTAMOS EL SERVICIO DE PACIENTES 🔥
    const pacienteService = inject('pacienteService', () => new PacienteService());

    const cita: Ref<ICita> = ref(new Cita());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();
    const previousState = () => router.go(-1);

    const retrieveCita = async (citaId: number) => {
      try {
        const res = await citaService().find(citaId);
        // Formatear la fecha si viene de la base de datos
        if (res.fechaHora) {
          res.fechaHora = new Date(res.fechaHora);
        }
        cita.value = res;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.citaId) {
      retrieveCita(Number(route.params.citaId as string));
    }

    // 🔥 3. EL VIGILANTE DEL ECU (Buscador automático) 🔥
    const pacienteEncontrado = ref(false); // Bandera para saber si bloqueamos los inputs

    watch(() => cita.value.ecu, async (newEcu) => {
      if (!newEcu) {
        // Si borran el ECU, limpiamos los campos y los desbloqueamos
        cita.value.nombre = '';
        cita.value.apellidoPaterno = '';
        cita.value.apellidoMaterno = '';
        pacienteEncontrado.value = false;
        return;
      }

      try {
        // Buscamos a todos los pacientes y filtramos por ECU
        const res = await pacienteService().retrieve();
        const paciente = res.data.find((p: any) => p.ecu === newEcu);

        if (paciente) {
          // Si existe, autocompletamos y bloqueamos
          cita.value.nombre = paciente.nombre;
          cita.value.apellidoPaterno = paciente.apellidoPaterno;
          cita.value.apellidoMaterno = paciente.apellidoMaterno;
          pacienteEncontrado.value = true;
          alertService.showSuccess(`Paciente encontrado: ${paciente.nombre}`);
        } else {
          // Si no existe, dejamos que el usuario lo llene manualmente
          pacienteEncontrado.value = false;
        }
      } catch (error) {
        console.error("Error al buscar paciente", error);
      }
    });
    // 🔥 EL CANDADO DE ZONA HORARIA LOCAL (Mismo de ayer) 🔥
    const fechaHoraLocal = computed({
      get: () => {
        if (!cita.value.fechaHora) return '';
        const date = new Date(cita.value.fechaHora);
        if (isNaN(date.getTime())) return '';
        const offset = date.getTimezoneOffset() * 60000;
        const localDate = new Date(date.getTime() - offset);
        return localDate.toISOString().slice(0, 16);
      },
      set: (val: string) => {
        cita.value.fechaHora = val ? new Date(val) : undefined;
      }
    });

    // 🔥 4. VALIDACIONES DE VUELIDATE 🔥
    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      ecu: { required: validations.required(t$('entity.validation.required').toString()) },
      nombre: { required: validations.required(t$('entity.validation.required').toString()) },
      apellidoPaterno: { required: validations.required(t$('entity.validation.required').toString()) },
      apellidoMaterno: {}, // Opcional
      fechaHora: { required: validations.required(t$('entity.validation.required').toString()) },
      motivo: { required: validations.required(t$('entity.validation.required').toString()) },
    };
    const v$ = useVuelidate(validationRules, cita as any);
    v$.value.$validate();

    return {
      citaService, alertService, cita, previousState, isSaving, currentLanguage, v$, t$,
      pacienteEncontrado,fechaHoraLocal // Exportamos la variable para usarla en el HTML
    };
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.cita.id) {
        this.citaService().update(this.cita).then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacientesmsApp.pacientemsCita.updated', { param: param.id }));
          }).catch(error => { this.isSaving = false; this.alertService.showHttpError(error.response); });
      } else {
        this.citaService().create(this.cita).then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacientesmsApp.pacientemsCita.created', { param: param.id }).toString());
          }).catch(error => { this.isSaving = false; this.alertService.showHttpError(error.response); });
      }
    },
  },
});