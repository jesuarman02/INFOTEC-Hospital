<template>
  <div class="perfil-paciente-wrapper">
    
    <div class="card custom-grey-card shadow-sm border-0 mb-3">
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

    <div class="card custom-grey-card shadow-sm border-0">
      <div class="card-body d-flex align-items-center justify-content-between p-3">
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
</template>

 <script setup lang="ts">
// 1. Declaramos que este componente recibe un "paciente"
const props = defineProps({
  paciente: {
    type: Object,
    required: true
  }
});
const calcularEdad = (fechaNacimiento: string | undefined) => {
  if (!fechaNacimiento) return 'N/A'; 
  
  const hoy = new Date();
  const cumpleanos = new Date(fechaNacimiento);
  let edad = hoy.getFullYear() - cumpleanos.getFullYear();
  const m = hoy.getMonth() - cumpleanos.getMonth();
  
  // Si el mes de hoy es menor al mes de nacimiento, le restamos 1 año
  if (m < 0 || (m === 0 && hoy.getDate() < cumpleanos.getDate())) {
    edad--;
  }
  
  return isNaN(edad) ? 'N/A' : `${edad} años`;
};
</script>
<style scoped src="../../content/css/modulos.css"></style>
