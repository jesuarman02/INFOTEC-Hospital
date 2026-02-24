  <template>
    <div class="d-flex app-container">
      <div class="sidebar-icons d-flex flex-column align-items-center py-4">
        <div class="icon-wrapper mb-4 " @click="mostrarHeader = !mostrarHeader">
          <img src="/content/images/md-del-usuario.svg" class="sidebar-svg" alt="Médico" />
        </div>
       <div class="icon-wrapper mb-4" @click="mostrarCalendario = true">
        <img src="/content/images/calendario.svg" class="sidebar-svg" alt="Calendario" />
      </div>
        <div class="icon-wrapper mb-4" @click="mostrarSubirArchivos = true">
          <img src="/content/images/carpeta.svg" class="sidebar-svg" alt="Expedientes" />
        </div>
      </div>
    <div class="sidebar-list p-3 border-right"
    :class="{ 'sidebar-collapsed': !mostrarHeader }"
    >

  <transition name="fade">
    <div v-show="mostrarHeader">

    <div class="sidebar-list p-3 border-right">
      <h5 class="title-pacientes mt-2 mb-3">{{ $t('interfaz-pacientes.patients') }}</h5>

      <div class="search-container mb-4">
        <div class="input-group fancy-search">
          <div class="input-group-prepend">
            <span class="input-group-text bg-transparent border-0 pl-3">
              <font-awesome-icon icon="search" class="text-muted search-icon" />
            </span>
          </div>
          <input 
  type="text" 
  class="form-control border-0 bg-transparent search-input" 
  placeholder="ECU, Nombre del Paciente" 
  @keyup.enter="realizarBusqueda" 
/>
        </div>
      </div>

    </div>
  </transition>

</div>

      <div class="main-content flex-grow-1 p-4">
      <div v-if="busquedaRealizada" class="row h-100">
        <div class="col-md-5 d-flex flex-column">

          <div class="doctor-header d-flex align-items-center justify-content-between mb-2 px-2">
            <div class="doctor-name">{{ $t('interfaz-pacientes.dr') }}</div>
            <div class="specialty-name text-center">Especialidad Médica</div>
          </div>

        <!-- NUEVO ENCABEZADO -->
        <div class="top-info text-center">
          <div class="top-doctor">{{ paciente.medico.nombre }}</div>
          <div class="top-specialty">{{ paciente.medico.especialidad }}</div>

          <div class="top-button-wrapper">
            <button class="btn agregar-paciente-btn">AGREGAR PACIENTE</button>
          </div>
        </div>
        <!-- FIN ENCABEZADO -->

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
    <span class="data-label">ECU -</span>
    <span class="data-value">{{ paciente.ecu }}</span>
  </div>

  <div class="data-row">
    <span class="data-label">Nombre:</span>
    <span class="data-value">{{ paciente.nombre }}</span>
  </div>

  <div class="data-row">
    <span class="data-label">Edad:</span>
    <span class="data-value">{{ paciente.edad }}</span>
  </div>

  <div class="data-row">
    <span class="data-label">Sexo:</span>
    <span class="data-value">{{ paciente.sexo }}</span>
  </div>

  <div class="data-row">
    <span class="data-label">Dirección:</span>
    <span class="data-value">{{ paciente.direccion }}</span>
  </div>

  <div class="data-row">
    <span class="data-label">Teléfono:</span>
    <span class="data-value">{{ paciente.telefono }}</span>
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
            <!--causa ingreso-->
          <div class="col-md-7 d-flex flex-column">
            <div class="card dashboard-card mb-4 shadow-sm custom-grey-card flex-fill">
              <div class="card-title-custom">MOTIVO DE INGRESO</div>
              <div class="card-body d-flex align-items-center justify-content-center">
                <h3 class="font-weight-bold text-uppercase">
                {{ paciente.causaIngreso }}
              </h3>
              </div>
            </div>
            <!--estatus-->
            <div class="card dashboard-card mb-4 shadow-sm custom-grey-card flex-fill">
              <div class="card-title-custom">ESTADO ACTUAL</div>
              <div class="card-body d-flex align-items-center justify-content-center">
                <h3 class="font-weight-bold text-uppercase">
                  {{ paciente.estatus }}
                </h3>
              </div>
            </div>
            <!--trataminetos-->
            <div class="card dashboard-card mb-3 shadow-sm custom-grey-card flex-fill">
              <div class="card-title-custom">TRATAMIENTO</div>
              <div class="card-body d-flex align-items-center justify-content-center">
                <h3 class="font-weight-bold text-uppercase">
                  {{ paciente.tratamientos }}
                </h3>
              </div>
            </div>
          </div>

          <div>
            <b-card no-body>
              <b-tabs card>
                <b-tab title="Datos del Paciente" active>
                  <b-card-text>datos como su curp etc</b-card-text>
                </b-tab>
                <b-tab title="Datos Medicos">
                  <b-card-text>como altura, peso, alergias</b-card-text>
                </b-tab>
                <b-tab title="Diagnostico">
                  <b-card-text>datoos de sus diagnósticos clinicos</b-card-text>
                </b-tab>
                <b-tab title="Datos del medico">
                  <b-card-text>datos del médico que atendió al paciente</b-card-text>
                </b-tab>
              </b-tabs>
            </b-card>
          </div>
        </div>
      </div>  
    </div>
    <!-- CALENDARIO MODAL -->
  <div v-if="mostrarCalendario" class="calendar-overlay">
    <div class="calendar-box">
    <div class="calendar-header">
      <span>Calendario</span>
      <button class="close-btn" @click="mostrarCalendario = false">✕</button>
    </div>
    <input type="date" class="calendar-input" />
  </div>
</div>
<!-- MODAL SUBIR ARCHIVOS -->
<div v-if="mostrarSubirArchivos" class="upload-overlay">
  <div class="upload-box">

    <div class="upload-header">
      <span>Subir Archivos Médicos</span>
      <button class="close-btn" @click="mostrarSubirArchivos = false">✕</button>
    </div>
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
          <span class="h6 text-dark border-left pl-2 border-danger">ABCD123456HDFRRN01</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Nombre(s)</label>
          <span class="h6 text-dark border-left pl-2 border-danger">Juan</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Primer Apellido</label>
          <span class="h6 text-dark border-left pl-2 border-danger">Pérez</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Segundo Apellido</label>
          <span class="h6 text-dark border-left pl-2 border-danger">García</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Fecha de Nacimiento</label>
          <span class="h6 text-dark border-left pl-2 border-danger">15/05/1985</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Edad</label>
          <span class="h6 text-dark border-left pl-2 border-danger">40 años</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Sexo / Género</label>
          <span class="h6 text-dark border-left pl-2 border-danger">Masculino</span>
        </div>
        <div class="data-item mr-5 mb-4">
          <label class="text-muted small d-block">Estado Civil</label>
          <span class="h6 text-dark border-left pl-2 border-danger">Casado</span>
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
      </div>
      <div v-else class="d-flex flex-column align-items-center justify-content-center h-100 text-muted">
    <font-awesome-icon icon="search" size="4x" class="mb-3 opacity-20" />
    p-3000-0-0: Realice una búsqueda para ver los detalles del paciente.
  </div>
    </div> 
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'; // Importamos ref

export default defineComponent({
  name: 'InterfazPacientes',
  setup() {
    // Variable para controlar si se muestra la info
    const busquedaRealizada = ref(false);
    const mostrarCalendario = ref(false);
    const mostrarSubirArchivos = ref(false);

    // Función para activar la vista al presionar Enter o buscar
    const realizarBusqueda = () => {
      busquedaRealizada.value = true;
    };

    return {
      busquedaRealizada,
      realizarBusqueda
      mostrarCalendario,
      mostrarSubirArchivos
    };
  },
});
</script>

<style scoped>
/* =========================================
   ESTILOS GENERALES
   ========================================= */
.app-container {
  height: calc(100vh - 60px);
  overflow: hidden;
  background-color: rgb(255, 255, 255); /* Fondo blanco general */
}

    <div class="upload-body">
      <input 
        type="file" 
        accept=".pdf,.jpg,.jpeg"
        class="upload-input"
      />
      <p class="upload-text">
        Formatos permitidos: PDF, JPG
      </p>
    </div>

  </div>
</div>
  </template>


    <script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { ref } from 'vue';
  export default defineComponent({
    name: 'InterfazPacientes',
    setup() {

  const mostrarCalendario = ref(false);
  const mostrarHeader = ref(true);
  const mostrarSubirArchivos = ref(false);
  const paciente = reactive({
    ecu: "ECU-000245",
    nombre: "Juan Carlos Pérez López",
    edad: 34,
    sexo: "Masculino",
    direccion: "Av. Reforma 123, CDMX",
    telefono: "55 1234 5678",

    causaIngreso: "Dolor abdominal agudo",
    estatus: "En observación",
    tratamientos: "Suero intravenoso y analgésicos",

    datosPaciente: {
      curp: "PELJ900101HDFRRN09",
      fechaNacimiento: "01/01/1990",
      estadoCivil: "Soltero",
      ocupacion: "Ingeniero"
    },

    datosMedicos: {
      altura: "1.75 m",
      peso: "82 kg",
      alergias: "Penicilina",
      tipoSangre: "O+"
    },

    diagnostico: {
      principal: "Gastritis aguda",
      observaciones: "Requiere dieta blanda 7 días"
    },

    medico: {
      nombre: "Dra. María Fernanda Torres",
      cedula: "12345678",
      especialidad: "Otorrinolaringologia"
    }
  });

  return { paciente, mostrarCalendario, mostrarHeader, mostrarSubirArchivos  };
}
  });
  </script>


    <style scoped>

    .app-container {
     height: auto;
     min-height: calc(100vh - 60px);
     background-color: rgb(255, 255, 255);
    }


    .sidebar-icons {
      width: 80px;
      background-color: #611232;
      flex-shrink: 0;
    }

    .icon-wrapper {
      width: 50px;
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.3s ease;
      opacity: 0.7;
    }

    .icon-wrapper:hover {
      background-color: rgba(255, 255, 255, 0.1);
      opacity: 1;
    }

    .sidebar-svg {
      width: 24px;
      height: 24px;
      filter: invert(1);
      transition: transform 0.2s;
    }

    .icon-wrapper:hover .sidebar-svg {
      transform: scale(1.1);
    }

    .icon-wrapper.active {
      background-color: #eecbc4;
      opacity: 1;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    .icon-wrapper.active .sidebar-svg {
      filter: none;
    }

    .sidebar-list {
      width: 300px;
      background-color: rgb(255, 255, 255);
      display: flex;
      flex-direction: column;
      border-right: 1px solid #e0e0e0;
      transition: width 0.3s ease;
    }
    .sidebar-collapsed {
     width: 80px;
    }

    .title-pacientes {
      font-weight: 800;
      letter-spacing: 1px;
      color: #333;
      font-family: 'Arial Black', sans-serif;
    }

    .fancy-search {
      background-color: #f1f3f5;
      border-radius: 25px;
      padding: 5px;
      transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
      border: 1px solid transparent;
    }

    .fancy-search:focus-within {
      background-color: #ffffff;
      border-color: #eecbc4;
      box-shadow: 0 4px 12px rgba(97, 18, 50, 0.15);
      transform: translateY(-2px);
    }

    .search-input {
      box-shadow: none !important;
      font-size: 0.9rem;
      color: #495057;
    }

    .search-icon {
      transition: color 0.3s;
    }

    .fancy-search:focus-within .search-icon {
      color: #611232 !important;
    }


    .main-content {
    flex: 1;
    min-height: 100vh; /* 🔥 pantalla completa */
    position: relative;
    overflow: hidden;  /* evita que el before se salga */
    padding-bottom: 150px;
    }
    /* IMAGEN DE FONDO TIPO MARCA DE AGUA */
  .main-content::before{
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: url('/content/images/fondo.png');
    background-repeat: no-repeat;
    background-size: cover; /* 🔥 cubre toda la pantalla */
    background-position: center;
    opacity: 0.1; /* 🔥 ajusta transparencia aquí */
    z-index: 0;
  }    
   
    .custom-grey-card {
      background-color: rgba(255, 255, 255, 0.75);
      backdrop-filter: blur(8px);
      border-radius: 15px; 
      border: 3px solid rgba(97,18,50,0.25);
      box-shadow:
      0 6px 15px rgba(0,0,0,0.08);
      transition:
        transform 0.2s ease,
        box-shadow 0.2s ease;
    }

    .custom-grey-card:hover {
      transform: translateY(-3px);  
      box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1) !important;
    }


    .doctor-header {
      font-weight: 100;
      color: #333;
    }

    .doctor-name {
      font-size: 32px;
      font-weight: 700;
      margin-left: 45px;
    }

    .specialty-name {
      font-size: 32px;
      font-weight: 1000;
      margin-right: -910px;
    }

    .agregar-paciente-btn {
      background-color: #611232;
      color: white;
      border-radius: 90px;
      padding: 15px 48px;
      font-weight: 5600;
    }

    .agregar-paciente-btn:hover {
      background-color: #7a1a40;
    }


    .vital-icon-box {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .vital-svg {
      width: 40px;
      height: 40px;
    }


    .dashboard-card h2,
    .dashboard-card h3,
    .dashboard-card h4,
    .dashboard-card h5 {
      color: #333;
      font-family: 'Arial', sans-serif;
    }
    /* ===== NUEVO ORDEN SUPERIOR ===== */

  .top-info{
    width:100%;
    margin-bottom:40px;
    padding-left: 650px;
    text-align: left;
  }

  .top-doctor{
    font-size:38px;
    font-weight:800;
    color:#333;
    margin-bottom:45x;
  }

  .top-specialty{
    font-size:26px;
    font-weight:500;
    color:#555;
  }

  .top-button-wrapper{
    margin-top:20px;
  }


  /* ocultar encabezado viejo */
  .doctor-header{
    display:none;
  }
  .col-md-5{
    margin-top:0px;
  }
  .col-md-7 > .d-flex.justify-content-end.mb-2{
    display:none;
  }


  /* bajar las cards */
  .col-md-7 .dashboard-card{
    margin-top:25px;
  }

  .col-md-7{
    padding-top:10px;
  }
  /* ===== TITULOS FLOTANTES ===== */
.dashboard-card{
  position: relative;
  padding-top: 35px;
}

.card-title-custom{
  position: absolute;
  top: -12px;
  left: 50px;
  background: #611232;
  color: white;
  padding: 16px 18px;
  border-radius: 80px;
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 1px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.15);
}

/* ===== ICONO ECU PERSONALIZADO ===== */

.ecu-icon{
  display:flex;
  justify-content:center;
  align-items:center;
  margin-bottom:20px;
}

.ecu-img{
  width:210px;
  height:150px;
  object-fit:contain;
  transition: transform 0.2s ease;
}

.ecu-img:hover{
  transform: scale(1.1);
}


/* ===== NUEVO DISEÑO LIMPIO DATOS PACIENTE ===== */

.patient-data{
  width:100%;
  max-width:450px;
  margin-top:15px;
}

.data-row{
  display:flex;
  align-items:center;
  gap:10px;
  padding:12px 0;
}

.data-label{
  font-size:20px;
  font-weight:700;
  color:#333;
}

.data-value{
  font-size:22px;
  font-weight:500;
  color:#000;
}
/* ===== CALENDARIO OVERLAY ===== */

.calendar-overlay{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.35);
  backdrop-filter: blur(4px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.calendar-box{
  background: white;
  padding: 30px;
  border-radius: 20px;
  width: 320px;
  box-shadow: 0 15px 40px rgba(0,0,0,0.2);
  animation: fadeIn 0.2s ease;
}

.calendar-header{
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 700;
  margin-bottom: 20px;
}

.close-btn{
  border: none;
  background: #611232;
  color: white;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  cursor: pointer;
}

.calendar-input{
  width: 100%;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid #ccc;
}

@keyframes fadeIn{
  from { opacity:0; transform: scale(0.9); }
  to { opacity:1; transform: scale(1); }
}
/* ===== MODAL SUBIR ARCHIVOS ===== */

.upload-overlay{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.35);
  backdrop-filter: blur(4px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.upload-box{
  background: white;
  padding: 30px;
  border-radius: 20px;
  width: 400px;
  box-shadow: 0 15px 40px rgba(0,0,0,0.2);
  animation: fadeIn 0.2s ease;
}

.upload-header{
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 700;
  margin-bottom: 20px;
  font-size: 18px;
}

.upload-body{
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.upload-input{
  border: 2px dashed #611232;
  padding: 20px;
  border-radius: 15px;
  width: 100%;
  text-align: center;
  cursor: pointer;
}

.upload-text{
  font-size: 13px;
  color: #666;
}
.card {
  border-radius: 10px !important;
  border: 1px solid rgba(0,0,0,0.05) !important;
}

.card h5 {
  letter-spacing: 1px;
  text-transform: uppercase;
  font-size: 0.9rem;
}
/* Color de la pestaña seleccionada (Activa) */
::v-deep .nav-tabs .nav-link.active {
  color: #d32f2f !important; /* Un rojo elegante */
  border-bottom: 3px solid #d32f2f !important;
  font-weight: 700;
}

/* Color del texto en pestañas no seleccionadas */
::v-deep .nav-tabs .nav-link {
  color: #555;
  border: none;
}

/* Color al pasar el mouse por encima */
::v-deep .nav-tabs .nav-link:hover {
  color: #b71c1c;
  background-color: rgba(211, 47, 47, 0.05); /* Un fondo rojo muy tenue */
}
</style>
