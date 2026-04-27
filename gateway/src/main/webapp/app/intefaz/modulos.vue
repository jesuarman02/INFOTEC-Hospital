<template>
  <div class="perfil-paciente-wrapper">
    
    <div class="card custom-grey-card shadow-sm border-0">
      <div class="card-title-custom">DATOS DEL PACIENTE</div>
      
      <div class="card-body p-0">
        <div class="patient-data">
          
          <div class="ecu-icon">
            <img src="/content/images/user.svg" class="ecu-img" alt="Avatar" />
          </div>
          
          <div class="data-row">
            <span class="data-label">ECU:</span>
            <span class="data-value">{{ paciente?.ecu || 'N/A' }}</span>
          </div>

          <div class="data-row">
            <span class="data-label">Nombre:</span>
            <span class="data-value">
              {{ paciente?.nombre }} {{ paciente?.apellidoPaterno }} {{ paciente?.apellidoMaterno || '' }}
            </span>
          </div>

          <div class="data-row">
            <span class="data-label">Edad:</span>
            <span class="data-value">{{ calcularEdad(paciente?.fechaNacimiento) || 'No registrada' }}</span>
          </div>

          <div class="data-row">
            <span class="data-label">Sexo:</span>
            <span class="data-value">{{ paciente?.sexo }}</span>
          </div>

          <div class="data-row">
            <span class="data-label">Dirección:</span>
            <span class="data-value">
              {{ paciente?.direccion?.vialidad || '' }} 
              {{ paciente?.direccion?.nombreVialidad || 'No registrada' }} 
              {{ paciente?.direccion?.numExterior || '' }} 
              {{ paciente?.direccion?.numInterior ? 'Int. ' + paciente?.direccion?.numInterior : '' }}
            </span>
          </div>

          <div class="data-row">
            <span class="data-label">Teléfono:</span>
            <span class="data-value">{{ paciente?.direccion?.telefono || 'N/A' }}</span>
          </div>

        </div>
      </div>
    </div>

    <br><br>

<div class="card custom-grey-card shadow-sm border-0">
      
      <div class="card-title-custom text-center mb-3" style="font-size: 0.75rem; padding: 12px 15px; letter-spacing: 0.5px;">
        ÚLTIMOS SIGNOS VITALES
      </div>

      <div class="card-body p-3 pt-4">
        <div class="row text-center">
          <div class="col-6 mb-3 px-2">
            <div class="vital-badge shadow-sm">
              <div class="vital-icon text-danger bg-soft-danger"><font-awesome-icon icon="heartbeat" /></div>
              <div class="vital-value">{{ ultimosSignos.frecuenciaCardiaca || '--' }} <span class="vital-unit">lpm</span></div>
              <div class="vital-label">Frec. Cardíaca</div>
            </div>
          </div>
          
          <div class="col-6 mb-3 px-2">
            <div class="vital-badge shadow-sm">
              <div class="vital-icon text-primary bg-soft-primary"><font-awesome-icon icon="tint" /></div>
              <div class="vital-value">{{ ultimosSignos.tensionArterial || '--/--' }}</div>
              <div class="vital-label">Tensión Art.</div>
            </div>
          </div>

          <div class="col-6 px-2">
            <div class="vital-badge shadow-sm">
              <div class="vital-icon text-info bg-soft-info"><font-awesome-icon icon="lungs" /></div>
              <div class="vital-value">{{ ultimosSignos.frecuenciaRespiratoria || '--' }} <span class="vital-unit">rpm</span></div>
              <div class="vital-label">Frec. Resp.</div>
            </div>
          </div>

          <div class="col-6 px-2">
            <div class="vital-badge shadow-sm">
              <div class="vital-icon text-success bg-soft-success"><font-awesome-icon icon="wind" /></div>
              <div class="vital-value">{{ ultimosSignos.saturacionOxigeno || '--' }} <span class="vital-unit">%</span></div>
              <div class="vital-label">SpO2</div>
            </div>
          </div>
        </div>

        <div v-if="!ultimosSignos.id" class="text-center mt-3 text-muted font-weight-bold" style="font-size: 0.85rem;">
          No hay registros recientes
        </div>
        <div v-else class="text-center mt-4 update-date-text">
          <font-awesome-icon icon="clock" class="mr-1 text-primary" />
          Actualizado: {{ formatearFecha(ultimosSignos.fechaRegistro) }}
        </div>

        <div class="text-center mt-3">
          <button class="btn-update-vitals shadow-sm px-4 py-2" style="font-size: 0.8rem;" @click="solicitarActualizacion" title="Modificar signos vitales">
            <font-awesome-icon icon="pencil-alt" class="mr-2" /> Editar
          </button>
        </div>

      </div>
    </div>  

    <SignosVitalesModal 
      v-model:visible="mostrarModalSignos" 
      :pacientePreCargado="paciente" 
      @saved="cargarUltimosSignos" 
    />

  </div>
</template>

<script setup lang="ts">
import { ref, inject, watch, onMounted } from 'vue';
import SignosVitalesService from '@/entities/pacientems/signos-vitales/signos-vitales.service';
import SignosVitalesModal from '@/shared/modals/signos-vitales-modal.vue'; // <-- Importamos el modal

const props = defineProps({
  paciente: {
    type: Object,
    required: true
  }
});

// Variable para controlar la apertura del modal
const mostrarModalSignos = ref(false);

const solicitarActualizacion = () => {
  mostrarModalSignos.value = true; // Abre el modal
};

const calcularEdad = (fechaNacimiento: string | undefined) => {
  if (!fechaNacimiento) return 'N/A'; 
  const hoy = new Date();
  const cumpleanos = new Date(fechaNacimiento);
  let edad = hoy.getFullYear() - cumpleanos.getFullYear();
  const m = hoy.getMonth() - cumpleanos.getMonth();
  if (m < 0 || (m === 0 && hoy.getDate() < cumpleanos.getDate())) { edad--; }
  return isNaN(edad) ? 'N/A' : `${edad} años`;
};

const ultimosSignos = ref<any>({});
const signosVitalesService = inject('signosVitalesService', () => new SignosVitalesService());

const cargarUltimosSignos = async () => {
  if (!props.paciente || !props.paciente.ecu) {
    ultimosSignos.value = {};
    return;
  }
  
  try {
    const res = await signosVitalesService().retrieve();
    const signosDelPaciente = res.data.filter((s: any) => s.pacienteEcu === props.paciente.ecu || s.paciente?.id === props.paciente.id);

    if (signosDelPaciente.length > 0) {
      signosDelPaciente.sort((a: any, b: any) => new Date(b.fechaRegistro).getTime() - new Date(a.fechaRegistro).getTime());
      ultimosSignos.value = signosDelPaciente[0];
    } else {
      ultimosSignos.value = {};
    }
  } catch (error) {
    console.error("Error al cargar los signos vitales:", error);
    ultimosSignos.value = {};
  }
};

const formatearFecha = (fecha: any) => {
  if (!fecha) return '';
  const d = new Date(fecha);
  const pad = (n: number) => (n < 10 ? '0' + n : n);
  
  const horas = d.getHours();
  const ampm = horas >= 12 ? 'PM' : 'AM';
  const horas12 = horas % 12 || 12;

  return `${pad(d.getDate())}/${pad(d.getMonth() + 1)}/${d.getFullYear()} a las ${pad(horas12)}:${pad(d.getMinutes())} ${ampm}`;
};

onMounted(() => {
  cargarUltimosSignos();
});

watch(() => props.paciente, () => {
  cargarUltimosSignos();
}, { deep: true });

</script>

<style scoped src="../../content/css/modulos.css"></style>

<style scoped>
.btn-update-vitals {
  width: 100%;
  background-color: #ffffff;
  color: #611232;
  border: 1px solid #e2e8f0;
  border-radius: 20px;
  padding: 4px 12px;
  font-size: 0.75rem;
  font-weight: 800;
  text-transform: uppercase;
  cursor: pointer;
  transition: all 0.2s ease;
}
.btn-update-vitals:hover {
  background-color: #611232;
  color: #ffffff;
  border-color: #611232;
  transform: translateY(-1px);
}

.update-date-text {
  font-size: 0.85rem;
  font-weight: 800;
  color: #1e293b; 
  background-color: #f8fafc;
  padding: 8px;
  border-radius: 8px;
  border: 1px dashed #cbd5e1;
}

.vital-badge {
  background: #ffffff;
  border-radius: 12px;
  padding: 12px 5px;
  border: 1px solid #f1f5f9;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  transition: transform 0.2s ease;
}
.vital-badge:hover { transform: translateY(-2px); border-color: #e2e8f0; }

.vital-icon {
  width: 36px; height: 36px;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 1.1rem; margin-bottom: 8px;
}

.bg-soft-danger { background-color: #ffe3e3; }
.bg-soft-primary { background-color: #e0e7ff; }
.bg-soft-info { background-color: #e0f2fe; }
.bg-soft-success { background-color: #dcfce7; }

.vital-value {
  font-size: 1.15rem; font-weight: 800; color: #1e293b;
  line-height: 1; margin-bottom: 3px;
}
.vital-unit {
  font-size: 0.6em; color: #94a3b8; font-weight: 700; margin-left: -2px;
}
.vital-label {
  font-size: 0.65rem; font-weight: 800; color: #64748b; text-transform: uppercase;
}

@media (max-width: 576px) {
  .vital-value { font-size: 1rem; }
  .vital-label { font-size: 0.6rem; }
  .btn-update-vitals { padding: 3px 8px; font-size: 0.7rem; }
}
</style>