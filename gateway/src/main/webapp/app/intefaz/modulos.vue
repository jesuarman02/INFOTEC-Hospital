<template>
     <div class="main-content flex-grow-1 p-4">

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
        </div>
      </div>
</template>

 <script lang="ts">
import { defineComponent, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'InterfazPacientes',
  setup() {

    const router = useRouter();

    const mostrarHeader = ref(true);
    const mostrarSubirArchivos = ref(false);

    // 🔥 FUNCIÓN PARA IR A calendario.vue
    const irCalendario = () => {
      router.push('/calendario');
    };

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

    return {
      paciente,
      mostrarHeader,
      mostrarSubirArchivos,
      irCalendario
    };
  }
});
</script>
<style scoped src="../../content/css/modulos.css"></style>
