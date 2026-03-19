<template>
  <div>
    <b-card no-body>
      <b-tabs card>
        
        <b-tab title="Datos del Paciente" active>
          <div class="container-fluid p-4">
            
            <b-card border-variant="light" shadow="sm" class="mb-4">
              <h5 class="text-danger mb-4 border-bottom pb-2">Datos Generales</h5>
              <div class="d-flex flex-wrap">
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">CURP</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.curp || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Nombre(s)</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.nombre || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Primer Apellido</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.apellidoPaterno || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Segundo Apellido</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.apellidoMaterno || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Fecha de Nacimiento</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.fechaNacimiento || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Edad</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ calcularEdad(paciente?.fechaNacimiento) }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Sexo / Género</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.sexo || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Nacionalidad</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.nacionalidad || 'N/A' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Estado Civil</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.estadoCivil || 'N/A' }}</span>
                </div>
              </div>
            </b-card>

            <b-card border-variant="light" shadow="sm" class="mb-4">
              <h5 class="text-danger mb-4 border-bottom pb-2">Dirección y Contacto</h5>
              <div class="d-flex flex-wrap">
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Dirección</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.direccion || 'Dato no capturado' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Teléfono Celular</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.telefono || 'Dato no capturado' }}</span>
                </div>
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block">Correo Electrónico</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.email || 'Dato no capturado' }}</span>
                </div>
              </div>
            </b-card>

            <b-card border-variant="light" shadow="sm" class="mb-4">
              <h5 class="text-danger mb-4 border-bottom pb-2">Información Socioeconómica</h5>
              <p class="text-muted"><em>Sección pendiente de integrar con el microservicio correspondiente.</em></p>
            </b-card>

          </div>
        </b-tab>

        <b-tab title="Datos Medicos">
          <b-card border-variant="light" shadow="sm" class="mb-4">
            <h5 class="text-danger mb-4 border-bottom pb-2">DATOS MÉDICOS Y ALERTAS</h5>
            <p class="text-muted"><em>Datos clínicos pendientes de integrar.</em></p>
          </b-card>
        </b-tab>

        <b-tab title="Diagnostico">
           <div class="p-4">
             <p class="text-muted"><em>Historial de diagnósticos en desarrollo.</em></p>
           </div>
        </b-tab>

        <b-tab title="Datos del medico">
          <b-card border-variant="light" shadow="sm" class="mb-4">
            <h5 class="text-danger mb-4 border-bottom pb-2">PERSONAL MÉDICO RESPONSABLE</h5>
             <div class="d-flex flex-wrap">
                <div class="data-item mr-5 mb-4">
                  <label class="text-muted small d-block font-weight-bold">MÉDICO ENCARGADO</label>
                  <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.medicoEncargado || 'Sin asignar' }}</span>
                </div>
             </div>
          </b-card>
        </b-tab>

      </b-tabs>
    </b-card>
  </div>
</template>

<script setup lang="ts">
// 1. Ahora recibimos UN paciente (igual que en modulos.vue)
const props = defineProps({
  paciente: {
    type: Object,
    required: false, // Lo ponemos false por si aún no buscamos a nadie
    default: () => ({})
  }
});

// 2. Función para calcular la edad automáticamente a partir de la fecha
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

<style scoped src="../../content/css/tablas.css"></style>