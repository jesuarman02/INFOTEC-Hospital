<template>
  <div class="interface-container">
    
    <!-- 1. PANEL IZQUIERDO: Tu Sidebar -->
    <aside class="left-panel-glass">
      <!-- Nota: Si no vas a poner un buscador aquí, el evento toggle-search no hará nada visualmente, pero lo dejamos preparado -->
      <SidebarModule @toggle-search="irPacientes" />
    </aside>

    <!-- 2. PANEL DERECHO: Tu Área de Trabajo (Calendario) -->
    <main class="right-panel-workspace">
      
      <div class="calendar-card">
        <!-- IZQUIERDA INFO DEL DIA -->
        <div class="left-section">
          <h2 class="mes">{{ mesNombre }} {{ anioActual }}</h2>
          <div class="numero-dia">{{ diaSeleccionado }}</div>
          <h3 class="titulo-citas">Citas</h3>

          <div v-if="citasDelDia.length === 0" class="text-muted mt-3">
            No hay citas
          </div>

          <div v-for="(cita, index) in citasDelDia" :key="index" class="cita-card mt-3 p-2 border-left border-danger shadow-sm">
            <strong>{{ cita.hora }}</strong> - {{ cita.paciente }}
          </div>
        </div>

        <!-- DERECHA CALENDARIO -->
        <div class="right-section">
          <div class="month-switch d-flex justify-content-between align-items-center mb-4">
            <button class="btn btn-light btn-sm" @click="cambiarMes(-1)">◀</button>
            <h2 class="titulo-mes m-0">{{ mesNombre }} {{ anioActual }}</h2>
            <button class="btn btn-light btn-sm" @click="cambiarMes(1)">▶</button>
          </div>

          <div class="week-days text-muted font-weight-bold">
            <div v-for="d in diasSemana" :key="d">{{ d }}</div>
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

      <!-- Botón de agendar debajo de la tarjeta del calendario -->
      <div class="d-flex justify-content-end mt-4">
        <button class="btn btn-danger px-4 py-2 shadow-sm" @click="abrirModal">
          Agendar Cita
        </button>
      </div>

    </main>

    <!-- 3. MODAL DE CITA (Fuera del flujo Flexbox para que cubra toda la pantalla) -->
    <div v-if="modalAbierta" class="modal-overlay">
      <div class="modal-content shadow-lg p-4 rounded-lg" style="max-width: 500px; background: white;">
        <h3 class="text-danger border-bottom pb-2 mb-4">Registrar Cita</h3>
        <p class="text-muted mb-4"><strong>Día:</strong> {{ diaSeleccionado }} {{ mesNombre }} {{ anioActual }}</p>

        <div class="row">
          <div class="col-md-6 mb-3"><input class="form-control" v-model="form.ecu" placeholder="ECU"></div>
          <div class="col-md-6 mb-3"><input class="form-control" v-model="form.nombre" placeholder="Nombre"></div>
          <div class="col-md-6 mb-3"><input class="form-control" v-model="form.apellidoPaterno" placeholder="Apellido Paterno"></div>
          <div class="col-md-6 mb-3"><input class="form-control" v-model="form.apellidoMaterno" placeholder="Apellido Materno"></div>
          <div class="col-md-6 mb-3">
            <select class="form-control" v-model="form.sexo">
              <option value="">Sexo</option>
              <option value="H">Hombre</option>
              <option value="M">Mujer</option>
            </select>
          </div>
          <div class="col-md-6 mb-3">
            <select class="form-control" v-model="form.nacionalidad">
              <option value="">Nacionalidad</option>
              <option value="Mexicana">Mexicana</option>
              <option value="Extranjera">Extranjera</option>
            </select>
          </div>
          <div class="col-md-6 mb-3"><input class="form-control" type="date" v-model="form.fechaNacimiento"></div>
          <div class="col-md-6 mb-3"><input class="form-control" v-model="form.estadoCivil" placeholder="Estado Civil"></div>
          <div class="col-md-6 mb-3"><input class="form-control" v-model="form.curp" placeholder="CURP"></div>
          <div class="col-md-6 mb-3"><input class="form-control" type="time" v-model="form.hora"></div>
        </div>

        <div class="d-flex justify-content-end mt-4 border-top pt-3">
          <button class="btn btn-light mr-3" @click="cerrarModal">Cancelar</button>
          <button class="btn btn-danger" @click="guardarCita">Guardar Cita</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
// ¡Mucho más limpio con <script setup>! No necesitamos "defineComponent" ni retornar variables.
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import SidebarModule from '@/intefaz/sidebarModule.vue';
import '../../../content/css/calendario.css'; // Asegúrate de que esta ruta sea correcta

const router = useRouter();

const irPacientes = () => {
  router.push('/interfaz-pacientes'); // o '/pacientes' según tu ruta
};

// Variables de estado
const mostrarBuscador = ref(false); 
const modalAbierta = ref(false);
const fecha = ref(new Date());
const diaSeleccionado = ref(new Date().getDate());
const diasSemana = ['L','M','X','J','V','S','D'];

// Formulario reactivo
const form = ref({
  ecu: '', nombre: '', apellidoPaterno: '', apellidoMaterno: '',
  sexo: '', nacionalidad: '', fechaNacimiento: '', estadoCivil: '', curp: '', hora: ''
});

// Datos simulados (Mock)
const citas = ref([
  { dia: 6, mes: 2, anio: 2026, hora: '09:00', paciente: 'Juan Pérez' },
  { dia: 6, mes: 2, anio: 2026, hora: '12:30', paciente: 'Ana López' },
  { dia: 10, mes: 2, anio: 2026, hora: '15:00', paciente: 'Carlos Ruiz' }
]);

// Propiedades computadas
const anioActual = computed(() => fecha.value.getFullYear());
const mesActualNumero = computed(() => fecha.value.getMonth());
const mesNombre = computed(() => fecha.value.toLocaleString('es-MX', { month: 'short' }).toUpperCase());

const diasDelMes = computed(() => {
  const ultimoDia = new Date(anioActual.value, mesActualNumero.value + 1, 0).getDate();
  return Array.from({ length: ultimoDia }, (_, i) => i + 1);
});

const citasDelDia = computed(() =>
  citas.value.filter(c => c.dia === diaSeleccionado.value && c.mes === mesActualNumero.value && c.anio === anioActual.value)
);

// Métodos
const cambiarMes = (direccion: number) => {
  fecha.value = new Date(anioActual.value, mesActualNumero.value + direccion, 1);
  diaSeleccionado.value = 1;
};

const seleccionarDia = (dia: number) => {
  diaSeleccionado.value = dia;
};

const tieneCita = (dia: number) => {
  return citas.value.some(c => c.dia === dia && c.mes === mesActualNumero.value && c.anio === anioActual.value);
};

const abrirModal = () => { modalAbierta.value = true; };
const cerrarModal = () => { modalAbierta.value = false; };

const guardarCita = () => {
  if(!form.value.hora || !form.value.nombre) return; // Pequeña validación básica
  citas.value.push({
    dia: diaSeleccionado.value,
    mes: mesActualNumero.value,
    anio: anioActual.value,
    hora: form.value.hora,
    paciente: `${form.value.nombre} ${form.value.apellidoPaterno}`.trim()
  });
  cerrarModal();
  // Limpiamos el formulario
  form.value = { ecu: '', nombre: '', apellidoPaterno: '', apellidoMaterno: '', sexo: '', nacionalidad: '', fechaNacimiento: '', estadoCivil: '', curp: '', hora: '' };
};
</script>