<template>
  <div class="calendar-wrapper">

    <div class="calendar-card">

      <!-- IZQUIERDA INFO DEL DIA -->
      <div class="left-section">

        <h2 class="mes">{{ mesNombre }} {{ anioActual }}</h2>

        <div class="numero-dia">
          {{ diaSeleccionado }}
        </div>

        <h3 class="titulo-citas">Citas</h3>

        <div v-if="citasDelDia.length === 0">
          No hay citas
        </div>

        <div
          v-for="(cita, index) in citasDelDia"
          :key="index"
          class="cita-card"
        >
          <strong>{{ cita.hora }}</strong> - {{ cita.paciente }}
        </div>

      </div>

      <!-- DERECHA CALENDARIO -->
      <div class="right-section">

        <div class="month-switch">
          <button @click="cambiarMes(-1)">◀</button>
          <h2 class="titulo-mes">
            {{ mesNombre }} {{ anioActual }}
          </h2>
          <button @click="cambiarMes(1)">▶</button>
        </div>

        <div class="week-days">
          <div v-for="d in diasSemana" :key="d">
            {{ d }}
          </div>
        </div>

        <div class="days-grid">
          <div
            v-for="dia in diasDelMes"
            :key="dia"
            class="day"
            :class="{ selected: dia === diaSeleccionado, cita: tieneCita(dia) }"
            @click="seleccionarDia(dia)"
          >
            {{ dia }}
          </div>
        </div>

      </div>

    </div>
    <button class="btn-agendar" @click="abrirModal">
      Agendar Cita
    </button>
    <div v-if="modalAbierta" class="modal-overlay">

  <div class="modal">

    <h2>Registrar Cita</h2>

    <p><strong>Día:</strong> {{ diaSeleccionado }} {{ mesNombre }} {{ anioActual }}</p>

    <input v-model="form.ecu" placeholder="ECU">

    <input v-model="form.nombre" placeholder="Nombre">

    <input v-model="form.apellidoPaterno" placeholder="Apellido Paterno">

    <input v-model="form.apellidoMaterno" placeholder="Apellido Materno">

    <select v-model="form.sexo">
      <option value="">Sexo</option>
      <option value="H">Hombre</option>
      <option value="M">Mujer</option>
    </select>

    <select v-model="form.nacionalidad">
      <option value="">Nacionalidad</option>
      <option value="Mexicana">Mexicana</option>
      <option value="Extranjera">Extranjera</option>
    </select>

    <input type="date" v-model="form.fechaNacimiento">

    <input v-model="form.estadoCivil" placeholder="Estado Civil">

    <input v-model="form.curp" placeholder="CURP">

    <input type="time" v-model="form.hora">

    <div class="modal-buttons">

      <button @click="guardarCita">Guardar</button>

      <button @click="cerrarModal">Cancelar</button>

    </div>

  </div>

  </div>

  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import '../../../content/css/calendario.css';

export default defineComponent({
  name: 'CalendarioPro',
  setup() {

    const router = useRouter();

    const mostrarHeader = ref(true);

    const fecha = ref(new Date());
    const diaSeleccionado = ref(new Date().getDate());
    const diasSemana = ['L','M','X','J','V','S','D'];
    const modalAbierta = ref(false);
    const form = ref({
      ecu: '',
      nombre: '',
      apellidoPaterno: '',
      apellidoMaterno: '',
      sexo: '',
      nacionalidad: '',
      fechaNacimiento: '',
      estadoCivil: '',
      curp: '',
      hora: ''
    });
    const citas = ref([
      { dia: 6, mes: 2, anio: 2026, hora: '09:00', paciente: 'Juan Pérez' },
      { dia: 6, mes: 2, anio: 2026, hora: '12:30', paciente: 'Ana López' },
      { dia: 10, mes: 2, anio: 2026, hora: '15:00', paciente: 'Carlos Ruiz' }
    ]);

    const anioActual = computed(() => fecha.value.getFullYear());
    const mesActualNumero = computed(() => fecha.value.getMonth());

    const mesNombre = computed(() =>
      fecha.value.toLocaleString('es-MX', { month: 'short' })
    );

    const diasDelMes = computed(() => {
      const ultimoDia = new Date(
        anioActual.value,
        mesActualNumero.value + 1,
        0
      ).getDate();

      return Array.from({ length: ultimoDia }, (_, i) => i + 1);
    });

    const cambiarMes = (direccion: number) => {
      fecha.value = new Date(
        anioActual.value,
        mesActualNumero.value + direccion,
        1
      );
      diaSeleccionado.value = 1;
    };

    const seleccionarDia = (dia: number) => {
      diaSeleccionado.value = dia;
    };
      const abrirModal = () => {
        modalAbierta.value = true;
      };

      const cerrarModal = () => {
        modalAbierta.value = false;
      };

      const guardarCita = () => {

        citas.value.push({
          dia: diaSeleccionado.value,
          mes: mesActualNumero.value,
          anio: anioActual.value,
          hora: form.value.hora,
          paciente: form.value.nombre + ' ' + form.value.apellidoPaterno
        });

        cerrarModal();
      };

    const citasDelDia = computed(() =>
      citas.value.filter(c =>
        c.dia === diaSeleccionado.value &&
        c.mes === mesActualNumero.value &&
        c.anio === anioActual.value
      )
    );

    const tieneCita = (dia: number) =>
      citas.value.some(c =>
        c.dia === dia &&
        c.mes === mesActualNumero.value &&
        c.anio === anioActual.value
      );

    const irAPacientes = () => {
      router.replace('/interfaz-pacientes');
    };

    const irCalendario = () => {
      router.push('/calendario');
    };

    return {
      mostrarHeader,
      diasSemana,
      diasDelMes,
      mesNombre,
      anioActual,
      diaSeleccionado,
      cambiarMes,
      seleccionarDia,
      citasDelDia,
      tieneCita,
      irAPacientes,
      irCalendario,
      modalAbierta,
      form,
      abrirModal,
      cerrarModal,
      guardarCita
    };
  }
});
</script>