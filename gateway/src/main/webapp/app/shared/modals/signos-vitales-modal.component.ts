import { type Ref, computed, defineComponent, inject, ref, watch } from 'vue';
import type { PropType } from 'vue';
import { useI18n } from 'vue-i18n';
import useVuelidate from '@vuelidate/core';
import { required, minValue, maxValue, maxLength } from '@vuelidate/validators';
import Swal from 'sweetalert2';

import SignosVitalesService from '@/entities/pacientems/signos-vitales/signos-vitales.service';
import PacienteService from '@/entities/pacientems/paciente/paciente.service';
import { type IPaciente } from '@/shared/model/pacientems/paciente.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SignosVitalesModal',
  props: { 
    visible: { type: Boolean, required: true },
    pacientePreCargado: { type: Object as PropType<any>, default: null } 
  },
  emits: ['update:visible', 'saved'],

  setup(props, { emit }) {
    const { t: t$ } = useI18n();

    const signosVitalesService = inject('signosVitalesService', () => new SignosVitalesService());
    const pacienteService = inject('pacienteService', () => new PacienteService());

    const ecuSearchString = ref('');
    const isSearchingEcu = ref(false);
    const pacienteEncontrado: Ref<IPaciente | null> = ref(null);
    const isSaving = ref(false);

    const initSignos = () => ({
      id: null,
      pacienteEcu: null,
      fechaRegistro: new Date(), 
      tipo: 'Ingreso',
      personal: '',
      frecuenciaCardiaca: null,
      tensionArterial: '',
      frecuenciaRespiratoria: null,
      temperatura: null,
      saturacionOxigeno: null,
      glucosa: null,
      dolor: null,
      estadoConciencia: 'Alerta',
      observaciones: ''
    });

    const signosVitales = ref<any>(initSignos());

// 🔥 MANEJO PURO LOCAL (CORREGIDO) 🔥
const fechaRegistroLocal = computed({
  get: () => {
    const d = signosVitales.value.fechaRegistro;
    if (!d) return '';

    const dateObj = new Date(d);
    if (isNaN(dateObj.getTime())) return '';

    const pad = (n: number) => (n < 10 ? '0' + n : n);

    return `${dateObj.getFullYear()}-${pad(dateObj.getMonth() + 1)}-${pad(dateObj.getDate())}T${pad(dateObj.getHours())}:${pad(dateObj.getMinutes())}`;
  },

  // 🔥 CORRECCIÓN IMPORTANTE AQUÍ
  set: (val: string) => {
    if (!val) {
      signosVitales.value.fechaRegistro = undefined;
      return;
    }

    // 🔥 Parseo manual SIN UTC
    const [datePart, timePart] = val.split('T');
    const [year, month, day] = datePart.split('-').map(Number);
    const [hours, minutes] = timePart.split(':').map(Number);

    signosVitales.value.fechaRegistro = new Date(year, month - 1, day, hours, minutes);
  }
});

    const rules = {
      fechaRegistro: { required },
      frecuenciaCardiaca: { min: minValue(0), max: maxValue(300) },
      tensionArterial: { maxLength: maxLength(10) },
      frecuenciaRespiratoria: { min: minValue(0), max: maxValue(100) },
      temperatura: { min: minValue(20), max: maxValue(45) },
      saturacionOxigeno: { min: minValue(0), max: maxValue(100) },
      glucosa: { min: minValue(0), max: maxValue(1000) },
      dolor: { min: minValue(0), max: maxValue(10) },
      tipo: {}, personal: {}, estadoConciencia: {}, observaciones: {}
    };
    const v$ = useVuelidate(rules, signosVitales);

    watch(() => props.visible, async (newVal) => {
      if (newVal) {
        if (props.pacientePreCargado) {
          pacienteEncontrado.value = props.pacientePreCargado;
          isSaving.value = true;
          
          try {
            const res = await signosVitalesService().retrieve();
            const signosExistentes = res.data.find((sv: any) => sv.pacienteEcu === props.pacientePreCargado.ecu || sv.paciente?.id === props.pacientePreCargado.id);
            
            if (signosExistentes) {
              const datosCargados = { ...signosExistentes };
              
              // Nos aseguramos de parsear la fecha de la base de datos a un objeto Date nativo
              if (datosCargados.fechaRegistro) {
                datosCargados.fechaRegistro = new Date(datosCargados.fechaRegistro);
              }
              
              signosVitales.value = datosCargados;
            } else {
              signosVitales.value = initSignos();
            }
          } catch (error) {
            console.error("Error al buscar signos vitales previos", error);
            signosVitales.value = initSignos();
          } finally {
            isSaving.value = false;
          }
        }
      } else {
        resetModal();
      }
    });

    const buscarPaciente = async () => {
      if (!ecuSearchString.value) return;
      isSearchingEcu.value = true;
      pacienteEncontrado.value = null;
      try {
        const res = await pacienteService().retrieve();
        const ecuNumerico = parseInt(ecuSearchString.value, 10);
        const encontrado = res.data.find((p: IPaciente) => p.ecu === ecuNumerico);

        if (encontrado) {
          pacienteEncontrado.value = encontrado;
          Swal.fire({ icon: 'success', title: 'Paciente Encontrado', showConfirmButton: false, timer: 1500 });
        } else {
          Swal.fire({ icon: 'error', title: 'No encontrado', text: 'No existe un paciente con ese ECU.', confirmButtonColor: '#611232' });
        }
      } catch (error: any) {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Ocurrió un error al buscar al paciente.', confirmButtonColor: '#611232' });
      } finally {
        isSearchingEcu.value = false;
      }
    };

    const resetModal = () => {
      pacienteEncontrado.value = null;
      ecuSearchString.value = '';
      signosVitales.value = initSignos();
      v$.value.$reset();
    };

    const cerrarModal = () => {
      Swal.fire({
        title: '¿Cancelar captura?',
        text: "Los datos ingresados se perderán.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#888',
        cancelButtonColor: '#611232',
        confirmButtonText: 'Sí, salir',
        cancelButtonText: 'Continuar editando'
      }).then((result) => {
        if (result.isConfirmed) {
          emit('update:visible', false);
        }
      });
    };

const save = async () => {
  isSaving.value = true;

  try {
    const payload: any = { ...signosVitales.value };

    // 🔥 CONVERTIR FECHA A STRING LOCAL (SIN UTC)
    if (payload.fechaRegistro instanceof Date) {
      const pad = (n: number) => (n < 10 ? '0' + n : n);
      const d = payload.fechaRegistro;

      payload.fechaRegistro = `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:00`;
    }

    if (pacienteEncontrado.value) {
      payload.pacienteNombre = pacienteEncontrado.value.nombre;
      payload.pacienteApellidoPaterno = pacienteEncontrado.value.apellidoPaterno;
      payload.paciente = { id: pacienteEncontrado.value.id };
      payload.pacienteEcu = pacienteEncontrado.value.ecu;
    }

    if (payload.id) {
      await signosVitalesService().update(payload);
    } else {
      await signosVitalesService().create(payload);
    }

    Swal.fire({
      icon: 'success',
      title: '¡Guardado Exitoso!',
      text: 'Signos vitales registrados correctamente.',
      showConfirmButton: false,
      timer: 2000
    });

    emit('saved');
    resetModal();
    emit('update:visible', false);

  } catch (error: any) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'No se pudo guardar el registro.',
      confirmButtonColor: '#611232'
    });
  } finally {
    isSaving.value = false;
  }
};

    const getColorDolor = (valor: number) => {
      if (valor === 0) return 'text-success';
      if (valor >= 1 && valor <= 3) return 'text-info';
      if (valor >= 4 && valor <= 6) return 'text-warning';
      if (valor >= 7 && valor <= 9) return 'text-danger';
      if (valor === 10) return 'text-dark';
      return '';
    };

    const actualizarFechaHora = () => {
      signosVitales.value.fechaRegistro = new Date();
    };

    return {
      t$, v$, signosVitales, pacienteEncontrado, ecuSearchString, isSearchingEcu, isSaving, fechaRegistroLocal,
      buscarPaciente, save, cerrarModal, getColorDolor, actualizarFechaHora
    };
  }
});