<template>
     <div class="main-content flex-grow-1 p-4">

        <div class="top-info text-center">
          <div class="top-doctor">{{ paciente?.medico?.nombre || 'Médico sin asignar' }}</div>
          <div class="top-specialty">{{ paciente?.medico?.especialidad || 'Especialidad no definida' }}</div>

          <div class="top-button-wrapper">
            <button class="btn agregar-paciente-btn">AGREGAR PACIENTE</button>
          </div>
        </div>
        <div class="row">
          <div class="col-md-5 d-flex flex-column">

            <div class="card dashboard-card flex-grow-1 mb-4 shadow-sm custom-grey-card">
              <div class="card-title-custom">DATOS DEL PACIENTE</div>
              <div class="card-body d-flex flex-column align-items-center justify-content-center text-center">
                
                <div class="patient-icon">
                  <font-awesome-icon icon="user-injured" />
                </div>
                
                <div class="patient-data">
                  <div class="ecu-icon">
                    <img src="/content/images/user.svg" class="ecu-img" alt="ECU" />
                  </div>
                  
                  <div class="data-row">
                    <strong>ECU -</strong> <span>{{ paciente?.ecu }}</span>
                  </div>

                  <div class="data-row">
                    <span class="data-label">Nombre:</span>
                    <span class="data-value">
                      {{ paciente?.nombre }} {{ paciente?.apellidoPaterno }} {{ paciente?.apellidoMaterno || '' }}
                    </span>
                  </div>

                  <div class="data-row">
                    <span class="data-label">Nacimiento:</span>
                    <span class="data-value">{{ paciente?.fechaNacimiento || 'No registrada' }}</span>
                  </div>

                  <div class="data-row">
                    <span class="data-label">Sexo:</span>
                    <span class="data-value">{{ paciente?.sexo }}</span>
                  </div>

                  <div class="data-row">
                    <span class="data-label">Nacionalidad:</span>
                    <span class="data-value">{{ paciente?.nacionalidad || 'No registrada' }}</span>
                  </div>

                  <div class="data-row">
                    <span class="data-label">CURP:</span>
                    <span class="data-value">{{ paciente?.curp || 'N/A' }}</span>
                  </div>

                </div>
              </div>
            </div>

            <div class="card dashboard-card mb-3 shadow-sm custom-grey-card" style="height: 120px">
              <div class="card-body d-flex align-items-center justify-content-around">
                <div class="vital-icon-box">
                  <img src="/content/images/regla-vertical.svg" class="vital-svg" alt="Talla" />
                </div>
                <div class="vital-icon-box">
                  <img src="/content/images/escala.svg" class="vital-svg" alt="Peso" />
                </div>
                <div class="vital-icon-box">
                  <img src="/content/images/corazon.svg" class="vital-svg" alt="Cardio" />
                </div>
              </div>
            </div>
          </div>
            
          <div class="col-md-7 d-flex flex-column">
            <div class="card dashboard-card mb-4 shadow-sm custom-grey-card flex-fill">
              <div class="card-title-custom">MOTIVO DE INGRESO</div>
              <div class="card-body d-flex align-items-center justify-content-center">
                <h3 class="font-weight-bold text-uppercase">
                  {{ paciente?.causaIngreso || 'DATOS CLÍNICOS PENDIENTES' }}
                </h3>
              </div>
            </div>
            
            <div class="card dashboard-card mb-4 shadow-sm custom-grey-card flex-fill">
              <div class="card-title-custom">ESTADO ACTUAL</div>
              <div class="card-body d-flex align-items-center justify-content-center">
                <h3 class="font-weight-bold text-uppercase">
                  {{ paciente?.estatus || 'EN VALORACIÓN' }}
                </h3>
              </div>
            </div>
            
            <div class="card dashboard-card mb-3 shadow-sm custom-grey-card flex-fill">
              <div class="card-title-custom">TRATAMIENTO</div>
              <div class="card-body d-flex align-items-center justify-content-center">
                <h3 class="font-weight-bold text-uppercase">
                  {{ paciente?.tratamientos || 'SIN TRATAMIENTO ASIGNADO' }}
                </h3>
              </div>
            </div>
          </div>
        </div>
      </div>
</template>

 <script setup lang="ts">
// 1. Declaramos que este componente recibe un "paciente"
const props = defineProps({
  paciente: {
    type: Object,
    required: true
  }
});
</script>
<style scoped src="../../content/css/modulos.css"></style>
