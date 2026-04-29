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

      <section class="work-area w-100 d-flex flex-column align-items-center" v-show="!mostrarClipboard">
        
        <div class="calendar-card">
          <div class="left-section">
            <h2 class="mes">{{ mesNombre }} {{ anioActual }}</h2>
            <div class="numero-dia">{{ diaSeleccionado }}</div>
            <h3 class="titulo-citas">Citas del Día</h3>

            <div class="citas-list">
              <div v-if="citasDelDia.length === 0" class="text-muted mt-3 font-weight-bold">
                No hay citas programadas
              </div>

              <div v-for="(cita, index) in citasDelDia" :key="index" class="cita-card mt-3 p-3 shadow-sm position-relative">
                <strong class="text-theme" style="font-size: 1.1rem;">
                  <font-awesome-icon icon="clock" class="mr-1"/> {{ cita.horaDisplay }}
                </strong> - <span class="font-weight-bold text-dark">{{ cita.paciente }}</span>
                <div class="small text-muted mt-1">Motivo: {{ cita.motivo }}</div>

                <div class="d-flex justify-content-end mt-2 pt-2 border-top">
                  <button class="btn btn-sm btn-light text-primary mr-2 shadow-sm" @click="prepararEdicion(cita)" title="Editar">
                    <font-awesome-icon icon="pencil-alt" />
                  </button>
                  <button class="btn btn-sm btn-light text-danger shadow-sm" @click="eliminarCita(cita.id)" title="Eliminar">
                    <font-awesome-icon icon="trash" />
                  </button>
                </div>
              </div>
            </div>

            <div class="mt-4 pt-3 border-top">
              <button class="btn btn-theme w-100 py-2 shadow-sm font-weight-bold text-uppercase" @click="abrirModal" style="letter-spacing: 1px;">
                <font-awesome-icon icon="calendar-plus" class="mr-2" /> Agendar Cita
              </button>
            </div>
          </div>

          <div class="right-section">
            <div class="month-switch d-flex justify-content-between align-items-center mb-4">
              <button class="btn btn-theme-light btn-sm shadow-sm" @click="cambiarMes(-1)">◀</button>
              <h2 class="titulo-mes m-0">{{ mesNombre }} {{ anioActual }}</h2>
              <button class="btn btn-theme-light btn-sm shadow-sm" @click="cambiarMes(1)">▶</button>
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
import { ref, computed, onMounted, watch } from 'vue'; // 🔥 Añadimos onMounted y watch
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

const citas = ref<Cita[]>([]); // Empezamos con un array vacío

// 🔥 NUEVA FUNCIÓN: Carga las citas desde la Base de Datos
const cargarCitas = async () => {
  try {
    const res = await axios.get(`services/citasms/api/citas`);
    
    // Mapeamos los datos del backend a nuestro formato local
    citas.value = res.data.map((c: any) => {
      const f = new Date(c.fecha);
      return {
        id: c.id,
        // Usamos UTC para evitar que la zona horaria nos cambie el día
        dia: f.getUTCDate(),
        mes: f.getUTCMonth(),
        anio: f.getUTCFullYear(),
        hora: c.hora,
        pacienteId: c.paciente?.id,
        paciente: `${c.paciente?.nombre} ${c.paciente?.apellidoPaterno}`
      };
    });
  } catch (error) {
    console.error("Error al cargar citas:", error);
  }
};

// 🔥 EJECUTAR AL CARGAR LA PÁGINA
onMounted(() => {
  cargarCitas();
});

// 🔥 OBSERVAR CAMBIOS DE MES PARA REFRESCAR (Opcional si filtras por mes)
watch(fecha, () => {
  cargarCitas();
});

// --- LÓGICA VISUAL DEL CALENDARIO ---
const anioActual = computed(() => fecha.value.getFullYear());
const mesActualNumero = computed(() => fecha.value.getMonth());
const mesNombre = computed(() => fecha.value.toLocaleString('es-MX', { month: 'long' }).toUpperCase());

const diasDelMes = computed(() => {
  const ultimoDia = new Date(anioActual.value, mesActualNumero.value + 1, 0).getDate();
  const diaDeSemanaInicio = new Date(anioActual.value, mesActualNumero.value, 1).getDay();
  const offset = diaDeSemanaInicio === 0 ? 6 : diaDeSemanaInicio - 1;
  return [...Array(offset).fill(null), ...Array.from({ length: ultimoDia }, (_, i) => i + 1)];
});

const citasDelDia = computed(() =>
  citas.value.filter(c => c.dia === diaSeleccionado.value && c.mes === mesActualNumero.value && c.anio === anioActual.value)
  // Ordenar por hora
  .sort((a, b) => a.horaRaw.localeCompare(b.horaRaw))
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

const abrirModal = () => { modalAbierta.value = true; };
const cerrarModal = () => { 
  modalAbierta.value = false; 
  form.value = { ecu: '', pacienteId: null, nombre: '', apellidoPaterno: '', apellidoMaterno: '', hora: '' };
  mensajeBusqueda.value = '';
};

// --- GUARDAR CITA ---
const guardandoCita = ref(false); // Estado para deshabilitar el botón mientras se guarda

const guardarCita = async () => {
  // 1. Validaciones de Frontend (Primera capa de robustez)
  if(!form.value.hora || !form.value.pacienteId) {
    alert("Datos incompletos. Asegúrese de haber seleccionado un paciente y una hora.");
    return;
  }

  guardandoCita.value = true;

  try {
    // 2. Preparar fecha para PostgreSQL (YYYY-MM-DD)
    const mesF = (mesActualNumero.value + 1).toString().padStart(2, '0');
    const diaF = diaSeleccionado.value.toString().padStart(2, '0');
    const fechaISO = `${anioActual.value}-${mesF}-${diaF}`;

    // 3. Construir el Payload Robusto
    // Enviamos el objeto 'paciente' solo con su ID para crear la relación en Postgres
    const citaParaEnviar = {
      fecha: fechaISO,
      hora: form.value.hora,
      paciente: {
        id: form.value.pacienteId 
      }
    };

    // 4. Petición HTTP POST al Microservicio
    const res = await axios.post(`services/citasms/api/citas`, citaParaEnviar);

    // 5. Verificación de Respuesta
    if (res.status === 201 || res.status === 200) {
      // Si el servidor guardó con éxito, refrescamos la lista local
      await cargarCitas(); // Función que trae las citas actualizadas de Postgres
      cerrarModal();
      alert("Cita registrada exitosamente en el sistema.");
    }

  } catch (error: any) {
    // Manejo de errores robusto
    console.error("Error en la persistencia:", error);
    
    if (error.response?.status === 400) {
      alert("Error: Datos inválidos. Revise que el horario sea correcto.");
    } else if (error.response?.status === 404) {
      alert("Error: El servidor de citas no responde (404).");
    } else {
      alert("No se pudo conectar con la base de datos de PostgreSQL.");
    }
  } finally {
    guardandoCita.value = false;
  }

};
</script>

<style scoped>
/* ==========================================================================
   COLORES INSTITUCIONALES (El Guinda #5c1830)
   ========================================================================== */
.text-theme { color: #5c1830 !important; }
.bg-theme { background-color: #5c1830 !important; }

.btn-theme {
  background-color: #5c1830;
  color: white;
  border: none;
  transition: all 0.2s ease;
}
.btn-theme:hover, .btn-theme:focus {
  background-color: #431122;
  color: white;
  transform: translateY(-2px);
}
.btn-theme:disabled { background-color: #8a5768; }

.btn-theme-light {
  background-color: #5c1830;
  color: white;
  border: none;
  transition: 0.2s;
}
.btn-theme-light:hover { background-color: #431122; color: white; }

/* ==========================================================================
   ESTRUCTURA BASE Y LAYOUT GENERAL
   ========================================================================== */
.interface-container {
  display: flex;
  height: calc(100vh - 65px);
  width: 100vw;
  position: relative;
  left: 50%; right: 50%;
  margin-left: -50vw; margin-right: -50vw;
  background-color: #f4f6f9;
  overflow: hidden;
}

.right-panel-workspace {
  flex-grow: 1;
  display: flex; flex-direction: column;
  justify-content: flex-start; align-items: center;
  padding: 2.5rem;
  background-color: #f8f9fa;
  overflow-y: auto;
}

/* ==========================================================================
   CALENDARIO CARD
   ========================================================================== */
.calendar-card {
  display: flex;
  width: 100%; max-width: 1100px;
  background: white;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  margin-top: 1rem; 
  height: calc(100vh - 150px); /* Fija el alto para permitir scroll interno en la lista */
  min-height: 600px;
}

/* IZQUIERDA */
.left-section {
  width: 38%;
  padding: 30px;
  background: #f5f5f5;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #e2e8f0;
}

.mes { font-size: 32px; font-weight: 800; color: #1e293b; margin-bottom: 5px; }
.numero-dia { font-size: 130px; font-weight: 800; color: #334155; line-height: 1; margin-bottom: 10px; }
.titulo-citas { font-size: 28px; font-weight: 700; color: #1e293b; margin-bottom: 15px; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px;}

.citas-list {
  flex-grow: 1;
  overflow-y: auto;
  padding-right: 5px;
}
/* Scrollbar sutil para las citas */
.citas-list::-webkit-scrollbar { width: 6px; }
.citas-list::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 4px; }

.cita-card {
  background: white;
  border-radius: 8px;
  border-left: 4px solid #5c1830 !important; 
  transition: transform 0.2s;
}
.cita-card:hover { transform: translateX(4px); }

/* DERECHA CALENDARIO */
.right-section {
  width: 62%;
  padding: 30px 40px;
  display: flex;
  flex-direction: column;
}

.month-switch { margin-bottom: 30px; display: flex; justify-content: space-between; align-items: center; }
.titulo-mes { font-size: 26px; font-weight: 800; color: #1e293b; text-transform: uppercase; letter-spacing: 1px;}

.month-switch button {
  padding: 8px 16px;
  border-radius: 6px;
  font-weight: bold;
}

.week-days {
  display: grid; grid-template-columns: repeat(7, 1fr);
  text-align: center; font-weight: 800; font-size: 15px; color: #64748b; margin-bottom: 15px;
}

.days-grid {
  display: grid; grid-template-columns: repeat(7, 1fr); gap: 10px; flex-grow: 1;
}

.day {
  display: flex; align-items: center; justify-content: center;
  border-radius: 12px; cursor: pointer; font-size: 18px; font-weight: 600; color: #334155;
  transition: all 0.2s ease; border: 2px solid transparent; aspect-ratio: 1;
}
.day:hover { background: #f1f5f9; border-color: #e2e8f0; }

.selected {
  background: #5c1830 !important; color: white !important;
  box-shadow: 0 4px 12px rgba(92, 24, 48, 0.35); transform: scale(1.05);
}

.cita {
  background: #f5eef1; color: #5c1830; font-weight: bold; position: relative;
}
/* Puntito indicador de cita */
.cita::after {
  content: ''; position: absolute; bottom: 8px; width: 6px; height: 6px;
  background-color: #5c1830; border-radius: 50%;
}
.cita.selected::after { background-color: white; }

/* ==========================================================================
   MODAL
   ========================================================================== */
.modal-overlay {
  position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
  background: rgba(15, 23, 42, 0.65); backdrop-filter: blur(5px);
  display: flex; justify-content: center; align-items: center; z-index: 9999;
}
.modal-content {
  animation: aparecerModal 0.3s cubic-bezier(0.16, 1, 0.3, 1);
  max-height: 90vh; overflow-y: auto; border: none;
}
.modal-content input:focus, .modal-content textarea:focus {
  outline: none; box-shadow: 0 0 0 2px rgba(92, 24, 48, 0.2);
}
@keyframes aparecerModal {
  from { transform: scale(0.95); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

/* ==========================================================================
   RESPONSIVO
   ========================================================================== */
@media (max-width: 992px) {
  .calendar-card { flex-direction: column; height: auto; min-height: auto; }
  .left-section { width: 100%; border-right: none; border-bottom: 1px solid #e2e8f0; }
  .right-section { width: 100%; }
  .numero-dia { font-size: 100px; }
  .citas-list { max-height: 300px; }
}
@media (max-width: 768px) {
  .right-panel-workspace { padding: 1rem; }
  .numero-dia { font-size: 80px; }
  .day { font-size: 16px; border-radius: 8px; }
  .modal-content { width: 95% !important; padding: 1.5rem !important; max-height: 85vh; }
}
</style>