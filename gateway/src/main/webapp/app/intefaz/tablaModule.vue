<template>
    <div>
          <b-card  no-body>
            <b-tabs card>
              <b-tab title="Datos del Paciente" active>
                <div class="container-fluid p-4">
    
    <b-card border-variant="light" shadow="sm" class="mb-4">
      <h5 class="text-danger mb-4 border-bottom pb-2">
        Datos Generales
      </h5>
      <div class="d-flex flex-wrap">
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">CURP</label>
          <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.curp || 'CURP no disponible' }}</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Nombre(s)</label>
          <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.nombre || 'Nombres no disponibles' }}</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Primer Apellido</label>
          <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.apellidoPaterno || 'Primer apellido no disponible' }}</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Segundo Apellido</label>
          <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.apellidoMaterno || 'Segundo apellido no disponible' }}</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Fecha de Nacimiento</label>
          <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.fechaNacimiento || 'Fecha de nacimiento no disponible' }}</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Edad</label>
          <span class="h6 text-dark border-left pl-2 border-danger">{{ calcularEdad(paciente?.fechaNacimiento) }} años</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Sexo / Género</label>
          <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.sexo || 'Sexo no disponible' }}</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Estado Civil</label>
          <span class="h6 text-dark border-left pl-2 border-danger">{{ paciente?.estadoCivil || 'Estado civil no disponible' }}</span>
        </div>
      </div>
    </b-card>

    <b-card border-variant="light" shadow="sm" class="mb-4">
      <h5 class="text-danger mb-4 border-bottom pb-2">
        Dirección y Contacto
      </h5>
      <div class="d-flex flex-wrap">
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Calle y Número</label>
          <span class="h6 text-dark border-left pl-2 border-danger">Av. Cuauhtémoc 255</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Colonia</label>
          <span class="h6 text-dark border-left pl-2 border-danger">Doctores</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Alcaldía / Municipio</label>
          <span class="h6 text-dark border-left pl-2 border-danger">Cuauhtémoc</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Estado</label>
          <span class="h6 text-dark border-left pl-2 border-danger">CDMX</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Código Postal</label>
          <span class="h6 text-dark border-left pl-2 border-danger">06720</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Teléfono Celular</label>
          <span class="h6 text-dark border-left pl-2 border-danger">55-1234-5678</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Correo Electrónico</label>
          <span class="h6 text-dark border-left pl-2 border-danger">juan.perez@ejemplo.com</span>
        </div>
      </div>
    </b-card>
    <b-card border-variant="light" shadow="sm" class="mb-4">
  <h5 class="text-danger mb-4 border-bottom pb-2">
    Información Socioeconómica
  </h5>

  <div class="d-flex flex-wrap border-bottom mb-3">
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Tipo Vivienda</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Propia</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Material Vivienda</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Concreto</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Número Habitaciones</label>
      <span class="h6 text-dark border-left pl-2 border-danger">3</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Número Habitantes</label>
      <span class="h6 text-dark border-left pl-2 border-danger">4</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Servicios Disponibles</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Agua, Luz, Drenaje</span>
    </div>
  </div>

  <div class="d-flex flex-wrap border-bottom mb-3 pt-2">
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Ingreso Mensual</label>
      <span class="h6 text-dark border-left pl-2 border-danger">$8,500</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Ingreso Mensual Hogar</label>
      <span class="h6 text-dark border-left pl-2 border-danger">$15,000</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Gasto Mensual</label>
      <span class="h6 text-dark border-left pl-2 border-danger">$6,200</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Personas Dependientes</label>
      <span class="h6 text-dark border-left pl-2 border-danger">2</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Apoyos Gubernamentales</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Beca Benito Juárez</span>
    </div>
  </div>

  <div class="d-flex flex-wrap pt-2">
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Ocupación Actual</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Empleado</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Condición Laboral</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Permanente</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Tipo Empleo / Lugar</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Formal / Corporativo</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Tiempo Empleado</label>
      <span class="h6 text-dark border-left pl-2 border-danger">4 Años</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Grado Máximo Estudios</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Licenciatura</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Años Estudio / Estudia</label>
      <span class="h6 text-dark border-left pl-2 border-danger">16 Años / No</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Institución Médica</label>
      <span class="h6 text-dark border-left pl-2 border-danger">IMSS</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Tipo / Número Afiliación</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Directo / 1234567890</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Medio / Tiempo Traslado</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Metro / 45 min</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">Fecha Actualización</label>
      <span class="h6 text-dark border-left pl-2 border-danger">18/02/2026</span>
    </div>
  </div>
</b-card>
    </div>
              </b-tab>
              <b-tab title="Datos Medicos">
                <b-card border-variant="light" shadow="sm" class="mb-4">
  <h5 class="text-danger mb-4 border-bottom pb-2">DATOS MÉDICOS Y ALERTAS</h5>
  <div class="d-flex flex-wrap">
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">ALTURA / PESO / IMC</label>
      <span class="h6 text-dark border-left pl-2 border-danger">1.75 m / 82 kg / 26.8</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">TIPO DE SANGRE</label>
      <span class="h6 text-dark border-left pl-2 border-danger">O Positivo (O+)</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block text-danger font-weight-bold">ALERGIAS</label>
      <span class="h6 text-danger font-weight-bold border-left pl-2 border-danger">PENICILINA</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">PADECIMIENTOS</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Hipertensión Arterial</span>
    </div>
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block">ANTECEDENTES FAMILIARES</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Diabetes (Padre)</span>
    </div>
  </div>
</b-card>
              </b-tab>
              <b-tab title="Diagnostico">
                <div class="accordion" id="accordionDiagnosticos">
  
  <div class="accordion-item">
    <h2 class="accordion-header" id="headingOne">
      <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
        <strong>12/02/2026 - Hipertensión Arterial</strong>
      </button>
    </h2>
    <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne" data-bs-parent="#accordionDiagnosticos">
      <div class="accordion-body">
        <p><strong>Médico Tratante:</strong> Dr. Juan Pérez</p>
        <p><strong>Observaciones:</strong> Paciente presenta presión elevada. Se receta Enalapril 10mg cada 12 horas. Se recomienda dieta baja en sodio.</p>
        <span class="badge bg-warning text-dark">En tratamiento</span>
      </div>
    </div>
  </div>

  <div class="accordion-item">
    <h2 class="accordion-header" id="headingTwo">
      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
        <strong>10/11/2025 - Gastritis Crónica</strong>
      </button>
    </h2>
    <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#accordionDiagnosticos">
      <div class="accordion-body">
        <p><strong>Médico Tratante:</strong> Dra. Ana Gómez</p>
        <p><strong>Observaciones:</strong> Dolor epigástrico recurrente. Se indica omeprazol en ayunas por 14 días.</p>
        <span class="badge bg-success">Controlado</span>
      </div>
    </div>
  </div>

  <div class="accordion-item">
    <h2 class="accordion-header" id="headingThree">
      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
        <strong>05/08/2025 - Migraña con Aura</strong>
      </button>
    </h2>
    <div id="collapseThree" class="accordion-collapse collapse" aria-labelledby="headingThree" data-bs-parent="#accordionDiagnosticos">
      <div class="accordion-body">
        <p><strong>Médico Tratante:</strong> Dr. Raúl S.</p>
        <p><strong>Observaciones:</strong> Episodios desencadenados por estrés. Reposo y analgésicos SOS.</p>
        <span class="badge bg-secondary">Histórico</span>
      </div>
    </div>
  </div>

</div>
              </b-tab>
              <b-tab title="Datos del medico">
                <b-card border-variant="light" shadow="sm" class="mb-4">
  <h5 class="text-danger mb-4 border-bottom pb-2">PERSONAL MÉDICO RESPONSABLE</h5>
  <div class="d-flex flex-wrap">
    
    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block font-weight-bold">MÉDICO ENCARGADO</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Dr. Alejandro Ramírez Montes</span>
    </div>

    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block font-weight-bold">ESPECIALIDAD / SUBESPECIALIDAD</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Medicina Interna / Cardiología</span>
    </div>

    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block font-weight-bold">ÁREA O DEPARTAMENTO</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Piso 3 - Hospitalización / Consulta Externa</span>
    </div>

    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block font-weight-bold">TELÉFONO DE CONTACTO</label>
      <span class="h6 text-dark border-left pl-2 border-danger">55-5272-1000 Ext. 4321</span>
    </div>

    <div class="data-item mr-5 mb-4">
      <label class="text-muted small d-block font-weight-bold">HORARIO DE ATENCIÓN</label>
      <span class="h6 text-dark border-left pl-2 border-danger">Lunes a Viernes (08:00 - 15:00 hrs)</span>
    </div>

  </div>
</b-card>
              </b-tab>
            </b-tabs>
          </b-card>
        </div>
</template>

<script setup lang="ts">

// Recibimos al paciente seleccionado desde el componente padre
const props = defineProps({
  paciente: {
    type: Object,
    default: () => null
  }
});

// Función para calcular edad (si no la tienes aquí, puedes importarla o definirla)
const calcularEdad = (fecha: string | undefined) => {
  if (!fecha) return '0';
  const hoy = new Date();
  const cumple = new Date(fecha);
  let edad = hoy.getFullYear() - cumple.getFullYear();
  if (hoy.getMonth() < cumple.getMonth() || (hoy.getMonth() === cumple.getMonth() && hoy.getDate() < cumple.getDate())) {
    edad--;
  }
  return edad;
};
</script>
<style scoped src="../../content/css/tablas.css"></style>