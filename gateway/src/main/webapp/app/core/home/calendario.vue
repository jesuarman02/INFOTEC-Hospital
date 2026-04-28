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
        <p class="text-muted mb-4"><strong>Día Seleccionado:</strong> {{ diaSeleccionado }} {{ mesNombre }} {{ anioActual }}</p>

        <div class="row">
          
          <div class="col-md-12 mb-4">
            <label class="font-weight-bold text-dark mb-1">ECU del Paciente <span class="text-danger">*</span></label>
            <div class="input-group">
              <input 
                type="number" 
                class="form-control" 
                v-model="form.ecu" 
                @blur="buscarPacienteParaCita"
                @keyup.enter="buscarPacienteParaCita"
                placeholder="Ingrese el ECU..."
              >
              <div class="input-group-append">
                <button class="btn btn-danger" type="button" @click="buscarPacienteParaCita" :disabled="buscandoPaciente">
                  Buscar
                </button>
              </div>
            </div>
            <small :class="mensajeClase" class="mt-1 d-block font-weight-bold">{{ mensajeBusqueda }}</small>
          </div>

          <div class="col-md-12 mb-3">
            <label class="text-muted small mb-1">Nombre</label>
            <input class="form-control bg-light" v-model="form.nombre" placeholder="-" disabled>
          </div>
          <div class="col-md-6 mb-3">
            <label class="text-muted small mb-1">Apellido Paterno</label>
            <input class="form-control bg-light" v-model="form.apellidoPaterno" placeholder="-" disabled>
          </div>
          <div class="col-md-6 mb-3">
            <label class="text-muted small mb-1">Apellido Materno</label>
            <input class="form-control bg-light" v-model="form.apellidoMaterno" placeholder="-" disabled>
          </div>

          <div class="col-12 border-bottom my-2"></div>

          <div class="col-md-12 mt-3">
             <label class="font-weight-bold text-dark mb-1">Hora de la Cita <span class="text-danger">*</span></label>
            <input class="form-control" type="time" v-model="form.hora">
          </div>
        </div>

        <div class="d-flex justify-content-end mt-4 border-top pt-3">
          <button class="btn btn-light mr-3" @click="cerrarModal">Cancelar</button>
          <button class="btn btn-danger" @click="guardarCita" :disabled="!form.pacienteId || !form.hora">Guardar Cita</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'; 
import { useRouter } from 'vue-router';
import axios from 'axios';
import SidebarModule from '@/intefaz/sidebarModule.vue';
import SearchModule2 from '@/intefaz/searchModule2.vue'; 
import '../../../content/css/calendario.css';

const router = useRouter();

// --- ESTADO DE NAVEGACIÓN ---
const mostrarClipboard = ref(false);
const searchQuery = ref('');

const irPacientes = () => { router.push('/interfaz-pacientes'); };
const abrirClipboard = () => { mostrarClipboard.value = true; };
const cerrarClipboard = () => { mostrarClipboard.value = false; };

// --- ESTADO DEL CALENDARIO ---
const modalAbierta = ref(false);
const fecha = ref(new Date());
const diaSeleccionado = ref(new Date().getDate());
const diasSemana = ['L','M','X','J','V','S','D'];

// --- FORMULARIO ---
const form = ref({
  ecu: '', 
  pacienteId: null as number | null,
  nombre: '', 
  apellidoPaterno: '', 
  apellidoMaterno: '', 
  hora: ''
});

// --- LÓGICA DE BÚSQUEDA DE PACIENTE ---
const buscandoPaciente = ref(false);
const mensajeBusqueda = ref('');
const mensajeClase = ref('text-muted');

const buscarPacienteParaCita = async () => {
  const ecuNum = Number(form.value.ecu);
  if (!ecuNum || isNaN(ecuNum)) return;

  buscandoPaciente.value = true;
  mensajeBusqueda.value = 'Buscando paciente...';
  mensajeClase.value = 'text-info';

  try {
    const res = await axios.get(`services/pacientesms/api/pacientes/ecu/${ecuNum}`);
    if (res.data && res.data.id) {
      form.value.pacienteId = res.data.id;
      form.value.nombre = res.data.nombre;
      form.value.apellidoPaterno = res.data.apellidoPaterno;
      form.value.apellidoMaterno = res.data.apellidoMaterno;
      mensajeBusqueda.value = '✓ Paciente verificado';
      mensajeClase.value = 'text-success';
    }
  } catch (error) {
    form.value.pacienteId = null;
    form.value.nombre = '';
    form.value.apellidoPaterno = '';
    form.value.apellidoMaterno = '';
    mensajeBusqueda.value = '❌ No se encontró el paciente';
    mensajeClase.value = 'text-danger';
  } finally {
    buscandoPaciente.value = false;
  }
};

// --- GESTIÓN DE CITAS (CRUD) ---
interface Cita {
  id?: number;
  dia: number;
  mes: number;
  anio: number;
  hora: string;
  paciente: string;
  pacienteId: number | null;
}

const citas = ref<Cita[]>([]); 

// 🔥 Cargar citas desde PostgreSQL
const cargarCitas = async () => {
  try {
    const res = await axios.get(`services/citasms/api/citas`);
    
    citas.value = res.data.map((c: any) => {
      // Parsear la fecha asumiendo formato "YYYY-MM-DD"
      // Se usa un workaround de JS para evitar problemas de Timezone al crear el Date
      const [year, month, day] = c.fecha.split('-');
      const f = new Date(year, month - 1, day); 

      return {
        id: c.id,
        dia: f.getDate(),
        mes: f.getMonth(),
        anio: f.getFullYear(),
        hora: c.hora,
        pacienteId: c.paciente?.id,
        // Si el paciente no trae nombre (por ejemplo si solo trae el ID en el JSON), poner "Paciente ID: X"
        paciente: c.paciente?.nombre ? `${c.paciente.nombre} ${c.paciente.apellidoPaterno}` : `Paciente ECU/ID: ${c.paciente?.id}`
      };
    });
  } catch (error) {
    console.error("Error al cargar citas de la BD:", error);
  }
};

// Ejecutar carga inicial
onMounted(() => {
  cargarCitas();
});

// Refrescar al cambiar de mes
watch(fecha, () => {
  cargarCitas();
});

// --- LÓGICA VISUAL DEL CALENDARIO ---
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

const cambiarMes = (direccion: number) => {
  fecha.value = new Date(anioActual.value, mesActualNumero.value + direccion, 1);
  diaSeleccionado.value = 1;
};

const seleccionarDia = (dia: number) => { diaSeleccionado.value = dia; };

const tieneCita = (dia: number | null) => {
  if (dia === null) return false; 
  return citas.value.some(c => c.dia === dia && c.mes === mesActualNumero.value && c.anio === anioActual.value);
};

// --- CONTROL DEL MODAL ---
const abrirModal = () => { modalAbierta.value = true; };
const cerrarModal = () => { 
  modalAbierta.value = false; 
  form.value = { ecu: '', pacienteId: null, nombre: '', apellidoPaterno: '', apellidoMaterno: '', hora: '' };
  mensajeBusqueda.value = '';
};

// --- GUARDAR CITA (Conexión Real a Postgres) ---
const guardandoCita = ref(false); 

const guardarCita = async () => {
  // 1. Validaciones
  if(!form.value.hora || !form.value.pacienteId) {
    alert("Datos incompletos. Busque un paciente y asigne una hora.");
    return;
  }

  // 2. Comprobar si ya existe una cita ese día para ese paciente localmente
  const yaTieneCita = citas.value.some(c => 
    c.dia === diaSeleccionado.value && 
    c.mes === mesActualNumero.value && 
    c.anio === anioActual.value && 
    c.pacienteId === form.value.pacienteId
  );

  if (yaTieneCita) {
    alert("Este paciente ya tiene una cita agendada para este día.");
    return;
  }

  guardandoCita.value = true;

  try {
    // 3. Formatear la fecha para la BD (Ej: 2026-04-27)
    const mesF = (mesActualNumero.value + 1).toString().padStart(2, '0');
    const diaF = diaSeleccionado.value.toString().padStart(2, '0');
    const fechaISO = `${anioActual.value}-${mesF}-${diaF}`;

    // 4. El Payload (Mandamos el ID del paciente para forjar la relación en Postgres)
    const citaParaEnviar = {
      fecha: fechaISO,
      hora: form.value.hora,
      paciente: {
        id: form.value.pacienteId 
      }
    };

    // 5. POST al backend
    const res = await axios.post(`services/citasms/api/citas`, citaParaEnviar);

    if (res.status === 201 || res.status === 200) {
      // 6. Si se guardó en BD, refrescamos la vista y cerramos
      await cargarCitas(); 
      cerrarModal();
      alert("Cita registrada exitosamente en el sistema.");
    }

  } catch (error: any) {
    console.error("Error en la persistencia:", error);
    
    if (error.response?.status === 400) {
      alert("Error (400): Datos inválidos enviados al servidor.");
    } else if (error.response?.status === 404) {
      alert("Error (404): El servicio de citas no fue encontrado. (Revisa si el microservicio está arriba y la tabla existe)");
    } else {
      alert("Error de conexión. No se pudo guardar la cita en la base de datos.");
    }
  } finally {
    guardandoCita.value = false;
  }
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