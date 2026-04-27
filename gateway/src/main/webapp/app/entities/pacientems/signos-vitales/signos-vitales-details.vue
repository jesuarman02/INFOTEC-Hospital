<template>
  <div class="row justify-content-center">
    <div class="col-md-10 col-lg-8">
      <div v-if="signosVitales" class="details-container">
        
        <div class="ficha-header mb-4">
          <div class="d-flex justify-content-between align-items-start">
            <div>
              <h2 class="font-weight-bold text-dark mb-1">
                <font-awesome-icon icon="clipboard-list" class="text-primary mr-2"></font-awesome-icon>
                Reporte de Signos Vitales
              </h2>
              <p class="text-muted mb-0">Registro #{{ signosVitales.id }}</p>
            </div>
            <div class="text-right">
              <span class="badge badge-info p-2" style="font-size: 1rem;">
                {{ signosVitales.tipo || 'N/A' }}
              </span>
            </div>
          </div>
        </div>

        <div class="card shadow-sm border-0 mb-4" style="background-color: #f8fafc;">
          <div class="card-body d-flex align-items-center justify-content-between p-3">
            <div class="d-flex align-items-center">
              <div class="icon-circle bg-primary text-white mr-3">
                <font-awesome-icon icon="user-injured"></font-awesome-icon>
              </div>
              <div>
                <h5 class="mb-0 font-weight-bold text-dark">
                  {{ signosVitales.pacienteNombre }} {{ signosVitales.pacienteApellidoPaterno }}
                </h5>
                <span class="text-muted" style="font-size: 0.9rem;">
                  ECU: <strong>{{ signosVitales.pacienteEcu || 'No registrado' }}</strong>
                </span>
              </div>
            </div>
            <div v-if="signosVitales.paciente && signosVitales.paciente.id">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: signosVitales.paciente.id } }" class="btn btn-outline-primary btn-sm rounded-pill font-weight-bold px-3">
                Ver Expediente
              </router-link>
            </div>
          </div>
        </div>

        <div class="row mb-4">
          <div class="col-md-6">
            <div class="info-box shadow-sm h-100">
              <p class="info-label"><font-awesome-icon icon="calendar-alt" class="mr-2"></font-awesome-icon> Fecha y Hora de Toma</p>
              <p class="info-value mb-0">{{ signosVitales.fechaRegistro ? formatDateLong(signosVitales.fechaRegistro) : 'Sin fecha' }}</p>
            </div>
          </div>
          <div class="col-md-6">
            <div class="info-box shadow-sm h-100">
              <p class="info-label"><font-awesome-icon icon="user-nurse" class="mr-2"></font-awesome-icon> Personal Responsable</p>
              <p class="info-value mb-0">{{ signosVitales.personal || 'No especificado' }}</p>
            </div>
          </div>
        </div>

        <h4 class="section-title text-danger mb-3">
          <font-awesome-icon icon="wave-square" class="mr-2"></font-awesome-icon> Mediciones Vitales
        </h4>
        <div class="row mb-4">
          
          <div class="col-6 col-md-4 mb-3">
            <div class="metric-box shadow-sm">
              <div class="metric-icon text-danger"><font-awesome-icon icon="heartbeat"></font-awesome-icon></div>
              <p class="metric-name">Frec. Cardíaca</p>
              <p class="metric-number">{{ signosVitales.frecuenciaCardiaca || '--' }} <span class="metric-unit">lpm</span></p>
            </div>
          </div>

          <div class="col-6 col-md-4 mb-3">
            <div class="metric-box shadow-sm">
              <div class="metric-icon text-primary"><font-awesome-icon icon="tint"></font-awesome-icon></div>
              <p class="metric-name">Tensión Arterial</p>
              <p class="metric-number" style="font-size: 1.4rem;">{{ signosVitales.tensionArterial || '--/--' }}</p>
            </div>
          </div>

          <div class="col-6 col-md-4 mb-3">
            <div class="metric-box shadow-sm">
              <div class="metric-icon text-info"><font-awesome-icon icon="lungs"></font-awesome-icon></div>
              <p class="metric-name">Frec. Respiratoria</p>
              <p class="metric-number">{{ signosVitales.frecuenciaRespiratoria || '--' }} <span class="metric-unit">rpm</span></p>
            </div>
          </div>

          <div class="col-6 col-md-4 mb-3">
            <div class="metric-box shadow-sm">
              <div class="metric-icon text-warning"><font-awesome-icon icon="thermometer-half"></font-awesome-icon></div>
              <p class="metric-name">Temperatura</p>
              <p class="metric-number">{{ signosVitales.temperatura || '--' }} <span class="metric-unit">°C</span></p>
            </div>
          </div>

          <div class="col-6 col-md-4 mb-3">
            <div class="metric-box shadow-sm">
              <div class="metric-icon text-success"><font-awesome-icon icon="wind"></font-awesome-icon></div>
              <p class="metric-name">Sat. Oxígeno (SpO2)</p>
              <p class="metric-number" :class="getColorSaturacion(signosVitales.saturacionOxigeno)">
                {{ signosVitales.saturacionOxigeno || '--' }} <span class="metric-unit">%</span>
              </p>
            </div>
          </div>

          <div class="col-6 col-md-4 mb-3">
            <div class="metric-box shadow-sm">
              <div class="metric-icon" style="color: #d97706;"><font-awesome-icon icon="cubes"></font-awesome-icon></div>
              <p class="metric-name">Glucosa</p>
              <p class="metric-number">{{ signosVitales.glucosa || '--' }} <span class="metric-unit">mg/dL</span></p>
            </div>
          </div>

        </div>

        <h4 class="section-title text-info mb-3">
          <font-awesome-icon icon="clipboard-check" class="mr-2"></font-awesome-icon> Evaluación Neurológica y Dolor
        </h4>
        
        <div class="row mb-5">
          <div class="col-md-5 mb-3">
            <div class="info-box shadow-sm h-100 text-center p-4">
              <p class="info-label mb-2">Escala de Dolor</p>
              <div class="pain-circle mx-auto" :class="getBgColorDolor(signosVitales.dolor)">
                {{ signosVitales.dolor !== null && signosVitales.dolor !== undefined ? signosVitales.dolor : '-' }}
              </div>
              <p class="mt-2 mb-0 font-weight-bold" :class="getTextColorDolor(signosVitales.dolor)">
                {{ getTextoDolor(signosVitales.dolor) }}
              </p>
            </div>
          </div>

          <div class="col-md-7 mb-3">
            <div class="info-box shadow-sm h-100">
              <p class="info-label mb-1">Estado de Conciencia</p>
              <h5 class="font-weight-bold text-dark mb-3">{{ signosVitales.estadoConciencia || 'No evaluado' }}</h5>
              
              <p class="info-label mb-1">Observaciones</p>
              <div class="p-3 bg-light rounded border">
                <p class="mb-0" style="font-size: 0.95rem; color: #475569;">
                  {{ signosVitales.observaciones || 'Ninguna observación registrada para esta toma.' }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <div class="d-flex justify-content-between border-top pt-4 mb-5">
          <button type="button" @click.prevent="previousState()" class="btn btn-outline-secondary font-weight-bold px-4 rounded-pill">
            <font-awesome-icon icon="arrow-left" class="mr-2"></font-awesome-icon> Volver
          </button>
          
          <router-link v-if="signosVitales.id" :to="{ name: 'SignosVitalesEdit', params: { signosVitalesId: signosVitales.id } }" custom v-slot="{ navigate }">
            <button @click="navigate" class="btn btn-primary font-weight-bold px-5 rounded-pill shadow-sm">
              <font-awesome-icon icon="pencil-alt" class="mr-2"></font-awesome-icon> Editar Registro
            </button>
          </router-link>
        </div>

      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./signos-vitales-details.component.ts"></script>

<style scoped>
/* ========================================= */
/* 🔥 ESTILOS DE LA FICHA CLÍNICA 🔥         */
/* ========================================= */
.details-container {
  background-color: #ffffff;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  margin-top: 20px;
}

.section-title {
  font-size: 1.15rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 1px;
  border-bottom: 2px solid #f1f5f9;
  padding-bottom: 10px;
}

/* Cajas de Información General */
.info-box {
  background-color: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px 20px;
}
.info-label {
  font-size: 0.85rem;
  color: #64748b;
  text-transform: uppercase;
  font-weight: 700;
  letter-spacing: 0.5px;
}
.info-value {
  font-size: 1.1rem;
  color: #1e293b;
  font-weight: 600;
}

/* Cajas de Cuadrícula de Signos Vitales */
.metric-box {
  background-color: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 20px 15px;
  text-align: center;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  height: 100%;
}
.metric-box:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.08) !important;
  border-color: #cbd5e1;
}
.metric-icon {
  font-size: 1.8rem;
  margin-bottom: 10px;
}
.metric-name {
  font-size: 0.85rem;
  color: #64748b;
  font-weight: 700;
  margin-bottom: 5px;
}
.metric-number {
  font-size: 1.8rem;
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 0;
  line-height: 1;
}
.metric-unit {
  font-size: 0.9rem;
  color: #94a3b8;
  font-weight: 600;
}

/* Ícono Circular para el Paciente */
.icon-circle {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
}

/* Escala de Dolor Visual */
.pain-circle {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: 900;
  box-shadow: inset 0 -3px 0 rgba(0,0,0,0.2);
}

/* Responsivo */
@media (max-width: 768px) {
  .details-container { padding: 20px 15px; }
  .metric-number { font-size: 1.5rem; }
  .metric-icon { font-size: 1.5rem; }
}
</style>