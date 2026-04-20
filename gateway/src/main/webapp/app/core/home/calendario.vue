<template>
  <div class="interface-container">
    
    <SidebarModule 
      @toggle-search="irPacientes" 
      @toggle-clipboard="abrirClipboard" 
    />

    <main class="right-panel-workspace">
      
      <div v-show="mostrarClipboard" class="search-section-centered">
        <SearchModule2 
          :key="'clipboard-' + mostrarClipboard"
          v-model="searchQuery"
          :mostrarHeader="mostrarClipboard"
          @ir-buscar="cerrarClipboard"
        />
      </div>

      <section class="work-area" v-show="!mostrarClipboard">
        <div class="calendar-card">
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
                v-for="(dia, index) in diasDelMes"
                :key="index"
                class="day"
                :class="{ selected: dia === diaSeleccionado, cita: tieneCita(dia) }"
                :style="{ visibility: dia === null ? 'hidden' : 'visible' }"
                @click="dia !== null && seleccionarDia(dia)"
              >
                {{ dia }}
              </div>
            </div>
          </div>
        </div>

        <div class="d-flex justify-content-end mt-4">
          <button class="btn btn-danger px-4 py-2 shadow-sm" @click="abrirModal">
            Agendar Cita
          </button>
        </div>
      </section>

    </main>

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
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import SidebarModule from '@/intefaz/sidebarModule.vue';
import SearchModule2 from '@/intefaz/searchModule2.vue'; // Importamos tu componente
import '../../../content/css/calendario.css';

const router = useRouter();

// Variables de estado nuevas para el Portapapeles
const mostrarClipboard = ref(false);
const searchQuery = ref('');

// Control del Toggle
const irPacientes = () => {
  router.push('/interfaz-pacientes'); 
};

const abrirClipboard = () => {
  mostrarClipboard.value = true;
};

// Al dar click en "ir-buscar" dentro del clipboard, lo ocultamos para volver a mostrar el calendario
const cerrarClipboard = () => {
  mostrarClipboard.value = false;
};

// Variables de estado del Calendario
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

// Propiedades computadas del Calendario
const anioActual = computed(() => fecha.value.getFullYear());
const mesActualNumero = computed(() => fecha.value.getMonth());
const mesNombre = computed(() => fecha.value.toLocaleString('es-MX', { month: 'short' }).toUpperCase());

const diasDelMes = computed(() => {
  const ultimoDia = new Date(anioActual.value, mesActualNumero.value + 1, 0).getDate();
  const diaDeSemanaInicio = new Date(anioActual.value, mesActualNumero.value, 1).getDay();
  const offset = diaDeSemanaInicio === 0 ? 6 : diaDeSemanaInicio - 1;
  const diasEnBlanco = Array(offset).fill(null);
  const diasReales = Array.from({ length: ultimoDia }, (_, i) => i + 1);
  return [...diasEnBlanco, ...diasReales];
});

const citasDelDia = computed(() =>
  citas.value.filter(c => c.dia === diaSeleccionado.value && c.mes === mesActualNumero.value && c.anio === anioActual.value)
);

// Métodos del Calendario
const cambiarMes = (direccion: number) => {
  fecha.value = new Date(anioActual.value, mesActualNumero.value + direccion, 1);
  diaSeleccionado.value = 1;
};

const seleccionarDia = (dia: number) => {
  diaSeleccionado.value = dia;
};

const tieneCita = (dia: number | null) => {
  if (dia === null) return false; 
  return citas.value.some(c => c.dia === dia && c.mes === mesActualNumero.value && c.anio === anioActual.value);
};

const abrirModal = () => { modalAbierta.value = true; };
const cerrarModal = () => { modalAbierta.value = false; };

const guardarCita = () => {
  if(!form.value.hora || !form.value.nombre) return; 
  citas.value.push({
    dia: diaSeleccionado.value,
    mes: mesActualNumero.value,
    anio: anioActual.value,
    hora: form.value.hora,
    paciente: `${form.value.nombre} ${form.value.apellidoPaterno}`.trim()
  });
  cerrarModal();
  form.value = { ecu: '', nombre: '', apellidoPaterno: '', apellidoMaterno: '', sexo: '', nacionalidad: '', fechaNacimiento: '', estadoCivil: '', curp: '', hora: '' };
};
</script>

<style scoped>
/* ==========================================================================
   ESTRUCTURA BASE
   ========================================================================== */
.interface-container {
  width: 100vw;
  position: relative;
  left: 50%;
  right: 50%;
  margin-left: -50vw;
  margin-right: -50vw;
  display: flex;
  min-height: calc(100vh - 70px);
  background-color: #f4f6f9; 
}

.right-panel-workspace {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start; 
  padding-left: 100px; 
  min-height: 100%;
}

/* 🔥 CLASE NECESARIA PARA CENTRAR EL SEARCHMODULE2 🔥 */
.search-section-centered {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  flex-grow: 1; 
  min-height: calc(100vh - 150px); 
  padding: 2rem;
}

.work-area {
  width: 100%;
  height: auto; 
  flex-grow: 1;
  padding: 2rem; 
  display: flex;
  flex-direction: column;
  align-items: center; 
}

@media (max-width: 1024px) {
  .right-panel-workspace {
    padding-left: 0; 
    padding-top: 90px; 
    justify-content: flex-start; 
  }
  .search-section-centered {
    min-height: auto; 
    margin-top: 2rem;
  }
}
</style>