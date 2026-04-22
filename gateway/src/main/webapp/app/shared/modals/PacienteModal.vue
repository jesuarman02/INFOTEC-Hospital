<template>
  <transition name="modal-fade">
    <div v-if="visible" class="custom-modal-overlay">
      <div class="custom-modal-box">
        
        <div class="custom-modal-header">
          <h3 class="title-text">
            <img src="/content/images/person-plus.svg" class="icon-title" /> Crear o editar Paciente
          </h3>
          <button class="close-btn" @click="cerrarModal">
            <img src="/content/images/x-circle.svg" class="icon-close" />
          </button>
        </div>

        <div class="custom-modal-body">
          <form @submit.prevent="guardar" class="formulario-grid">
            
            <div class="form-group" v-show="false">
              <label><img src="/content/images/fingerprint.svg" class="icon-label" /> ID</label>
              <input type="text" class="custom-input bg-light" v-model="paciente.id" readonly />
            </div>
            
            <div class="form-group">
              <label><img src="/content/images/hash.svg" class="icon-label" /> ECU <span class="text-danger">*</span></label>
              <input 
                type="number" 
                v-model="v$.ecu.$model" 
                @keydown="bloquearCaracteresEcu"
                min="0"
                required 
                class="custom-input"
                :class="{ 'bg-light': !!paciente.id }" 
                :readonly="!!paciente.id" 
                placeholder="Ej. 123456" 
              />
              <span v-if="v$.ecu.$error" class="error-text">
                <img src="/content/images/exclamation-lg.svg" class="icon-error" /> Obligatorio y numérico.
              </span>
            </div>

            <div class="form-group">
              <label><img src="/content/images/person-circle.svg" class="icon-label" /> Nombre <span class="text-danger">*</span></label>
                <input type="text" v-model="v$.nombre.$model" @input="onInputUpper($event, v$.nombre)" required class="custom-input" placeholder="Nombre(s)" />              <span v-if="v$.nombre.$error" class="error-text">
                <img src="/content/images/exclamation-lg.svg" class="icon-error" /> Solo letras, mín. 2.
              </span>
            </div>

            <div class="form-group">
              <label><img src="/content/images/person-circle.svg" class="icon-label" /> Apellido Paterno <span class="text-danger">*</span></label>
                <input type="text" v-model="v$.apellidoPaterno.$model" @input="onInputUpper($event, v$.apellidoPaterno)" required class="custom-input" />              <span v-if="v$.apellidoPaterno.$error" class="error-text">
                <img src="/content/images/exclamation-lg.svg" class="icon-error" /> Solo letras, mín. 2.
              </span>
            </div>

            <div class="form-group">
              <label><img src="/content/images/person-circle.svg" class="icon-label" /> Apellido Materno</label>
                <input type="text" v-model="v$.apellidoMaterno.$model" @input="onInputUpper($event, v$.apellidoMaterno)" class="custom-input" />              <span v-if="v$.apellidoMaterno.$error" class="error-text">
                <img src="/content/images/exclamation-lg.svg" class="icon-error" /> Solo letras.
              </span>
            </div>

            <div class="form-group">
              <label><img src="/content/images/calendar2-date.svg" class="icon-label" /> Fecha Nacimiento <span class="text-danger">*</span></label>
              <input type="date" v-model="v$.fechaNacimiento.$model" required class="custom-input" />
              <span v-if="v$.fechaNacimiento.$error" class="error-text">
                <img src="/content/images/exclamation-lg.svg" class="icon-error" /> Fecha obligatoria.
              </span>
            </div>

            <div class="form-group">
              <label><img src="/content/images/gender-ambiguous.svg" class="icon-label" /> Sexo <span class="text-danger">*</span></label>
              <div class="radio-group custom-radio-box">
                <label class="radio-label"><input type="radio" value="M" v-model="v$.sexo.$model" required> Hombre</label>
                <label class="radio-label"><input type="radio" value="F" v-model="v$.sexo.$model" required> Mujer</label>
              </div>
            </div>

            <div class="form-group col-span-2 slide-down" v-if="paciente.sexo === 'F'">
              <label class="text-highlight"><img src="/content/images/bottle_16554860.png" class="icon-label" /> ¿Paciente está embarazada? <span class="text-danger">*</span></label>
              <div class="radio-group custom-radio-box box-highlight">
                <label class="radio-label"><input type="radio" value="SI" v-model="paciente.embarazo"> Sí</label>
                <label class="radio-label"><input type="radio" value="NO" v-model="paciente.embarazo"> No</label>
              </div>
            </div>

            <div class="form-group">
              <label><img src="/content/images/globe-americas.svg" class="icon-label" /> Nacionalidad</label>
              <div class="radio-group custom-radio-box">
                <label class="radio-label"><input type="radio" value="MEXICANA" v-model="paciente.nacionalidad"> Mexicana</label>
                <label class="radio-label"><input type="radio" value="EXTRANJERA" v-model="paciente.nacionalidad"> Extranjera</label>
              </div>
            </div>

            <div class="form-group">
              <label><img src="/content/images/love_547350.png" class="icon-label" /> Estado Civil</label>
              <div class="select-wrapper">
                <select v-model="paciente.estadoCivil" class="custom-input">
                  <option value="NO_ESPECIFICADO">NO ESPECIFICADO</option>
                  <option value="NO_APLICA">NO APLICA</option>
                  <option value="SE_IGNORA">SE IGNORA</option>
                  <option value="CASADO">CASADO(A)</option>
                  <option value="DIVORCIADO">DIVORCIADO(A)</option>
                  <option value="SEPARADO">SEPARADO(A)</option>
                  <option value="SOLTERO">SOLTERO(A)</option>
                  <option value="UNION_LIBRE">UNION LIBRE</option>
                  <option value="VIUDO">VIUDO(A)</option>
                </select>
              </div>
            </div>

            <div class="form-group col-span-2">
              <label><img src="/content/images/person-vcard.svg" class="icon-label" /> CURP <span class="text-danger">*</span></label>
                <input type="text" v-model="v$.curp.$model" @input="onInputUpper($event, v$.curp)" :readonly="paciente.nacionalidad === 'EXTRANJERA'" required class="custom-input curp-input" placeholder="18 caracteres" maxlength="18" />              <span v-if="v$.curp.$error" class="error-text">
                <img src="/content/images/exclamation-lg.svg" class="icon-error" /> El CURP debe tener 18 caracteres.
              </span>
            </div>

          </form>
        </div>

        <div class="custom-modal-footer">
          <button class="custom-btn btn-cancel" @click="cerrarModal">
            <img src="/content/images/ban.svg" class="btn-icon" /> Cancelar
          </button>
          <button class="custom-btn btn-save" @click="guardar" :disabled="isSaving">
            <span v-if="isSaving" class="d-flex align-center">
              <img src="/content/images/tropical-storm.svg" class="btn-icon spinner" /> Guardando...
            </span>
            <span v-else class="d-flex align-center">
              <img src="/content/images/save.svg" class="btn-icon" /> Guardar Paciente
            </span>
          </button>
        </div>

      </div>
    </div>
  </transition>
</template>

<script lang="ts">
import { defineComponent, ref, watch, inject, type PropType } from 'vue'; 
import useVuelidate from '@vuelidate/core';
import { required, minLength, maxLength, integer } from '@vuelidate/validators';
import Swal from 'sweetalert2';

import PacienteService from '@/entities/pacientems/paciente/paciente.service';

const soloLetrasRegex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
const validadorSoloLetras = (value: string | null | undefined) => {
  if (!value) return true; 
  return soloLetrasRegex.test(value);
};

export default defineComponent({
  name: 'PacienteModal',
props: { 
    visible: { type: Boolean, required: true },
    pacienteToEdit: { type: Object as PropType<any>, default: null } // 🔥 NUEVO PROP 🔥
  },
    emits: ['update:visible', 'saved'],
  
  setup(props, { emit }) {
    const pacienteService = inject('pacienteService', () => new PacienteService());
    
    const isSaving = ref(false);

    const paciente = ref({
        id: null,
        ecu: null,
        nombre: '',
        apellidoPaterno: '',
        apellidoMaterno: '',
        sexo: '',
        embarazo: '',
        nacionalidad: 'MEXICANA',
        fechaNacimiento: '',
        estadoCivil: 'NO_ESPECIFICADO',
        curp: ''
    } as any);

    const rules = {
      ecu: { required, integer },
      nombre: { required, minLength: minLength(2), maxLength: maxLength(60), soloLetras: validadorSoloLetras },
      apellidoPaterno: { required, minLength: minLength(2), maxLength: maxLength(60), soloLetras: validadorSoloLetras },
      apellidoMaterno: { maxLength: maxLength(60), soloLetras: validadorSoloLetras },
      sexo: { required },
      fechaNacimiento: { required },
      curp: { required, minLength: minLength(18), maxLength: maxLength(18) }
    };

    const v$ = useVuelidate(rules, paciente);

    const limpiarFormulario = () => {
      paciente.value = {
        id: null,
        ecu: null,
        nombre: '',
        apellidoPaterno: '',
        apellidoMaterno: '',
        sexo: '',
        embarazo: '',
        nacionalidad: 'MEXICANA',
        fechaNacimiento: '',
        estadoCivil: 'NO_ESPECIFICADO',
        curp: ''
      };
      v$.value.$reset();
    };

    const bloquearCaracteresEcu = (e: KeyboardEvent) => {
      if (['e', 'E', '+', '-', '.'].includes(e.key)) {
        e.preventDefault();
      }
    };

    const onInputUpper = (event: Event, field: any) => {
      const target = event.target as HTMLInputElement;
      if (target) {
        field.$model = target.value.toUpperCase();
      }
    };

    watch(() => paciente.value.sexo, (nuevoSexo) => {
      if (nuevoSexo === 'M') {
        paciente.value.embarazo = 'N/A';
      } else if (nuevoSexo === 'F') {
        if (paciente.value.embarazo === 'N/A' || !paciente.value.embarazo) {
          paciente.value.embarazo = ''; 
        }
      }
    });

    watch(() => paciente.value.nacionalidad, (nuevaNacionalidad) => {
      if (nuevaNacionalidad === 'EXTRANJERA') {
        paciente.value.curp = 'EXTXX' + Date.now().toString().slice(0, 13);
      } else if (nuevaNacionalidad === 'MEXICANA') {
        if (paciente.value.curp && paciente.value.curp.startsWith('EXTXX')) {
          paciente.value.curp = '';
        }
      }
    });
    // 🔥 EL VIGILANTE: Se activa cada que el modal se abre o cierra 🔥
    watch(() => props.visible, (newVal) => {
      if (newVal) { // Si el modal se está abriendo...
        if (props.pacienteToEdit && props.pacienteToEdit.id) {
          // Si le pasamos un paciente que ya existe, llenamos el formulario (Clonando los datos)
          paciente.value = JSON.parse(JSON.stringify(props.pacienteToEdit));
        } else {
          // Si no le pasamos nada (o es modo Nuevo Paciente), limpiamos el formulario
          limpiarFormulario();
        }
      }
    });

    const hayCambiosSinGuardar = () => {
      return (
        paciente.value.ecu !== null ||
        paciente.value.nombre.trim() !== '' ||
        paciente.value.apellidoPaterno.trim() !== '' ||
        paciente.value.apellidoMaterno.trim() !== '' ||
        paciente.value.fechaNacimiento !== '' ||
        (paciente.value.curp !== '' && !paciente.value.curp.startsWith('EXTXX'))
      );
    };

    const cerrarModal = () => {
      if (hayCambiosSinGuardar()) {
      Swal.fire({
          title: '¿Cancelar registro?',
          text: "Los datos ingresados se perderán.",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#888',      // Color gris para "Sí, salir"
          cancelButtonColor: '#611232',    // Color guinda para "Continuar editando"
          confirmButtonText: 'Sí, salir',
          cancelButtonText: 'Continuar editando'
        }).then((result) => {
          if (result.isConfirmed) {
            limpiarFormulario();
            emit('update:visible', false);
          }
        });
      } else {
        emit('update:visible', false);
      }
    };

    const guardar = async () => {
      v$.value.$touch();
      
      if (paciente.value.sexo === 'F' && (!paciente.value.embarazo || paciente.value.embarazo === 'N/A')) {
        Swal.fire({ icon: 'warning', title: 'Falta información', text: 'Por favor, indique si la paciente está embarazada.', confirmButtonColor: '#611232' });
        return;
      }

      if (v$.value.$invalid) {
        Swal.fire({ icon: 'error', title: 'Campos incompletos', text: 'Por favor, revisa los campos marcados en rojo antes de guardar.', confirmButtonColor: '#611232' });
        return;
      }

      isSaving.value = true;

try {
        let respuestaBackend;
        
        if (paciente.value.id) {
          respuestaBackend = await pacienteService().update(paciente.value);
        } else {
          respuestaBackend = await pacienteService().create(paciente.value);
        }

        // 🔥 EXTRAEMOS EL PACIENTE CON SU NUEVO ID GENERADO POR LA BD 🔥

        // Le decimos a TypeScript que esto puede ser cualquier cosa para que no llore por el .data
          const res: any = respuestaBackend;
          const pacienteGuardado = res.data ? res.data : res;

        Swal.fire({
          icon: 'success',
          title: '¡Paciente Guardado!',
          text: 'La base de datos ha registrado la información.',
          showConfirmButton: false,
          timer: 1500
        });

        // 🔥 ENVIAMOS EL PACIENTE REAL (CON ID) AL WIZARD 🔥
        emit('saved', pacienteGuardado); 
        limpiarFormulario();
        emit('update:visible', false);

      } catch (error: any) {
        // 🚀 6. Manejo de errores (Ej. Si el ECU o CURP ya existen en la BD)
        const errorData = JSON.stringify(error.response?.data || {}).toLowerCase();
        
        if (errorData.includes('curp') || errorData.includes('constraint')) {
            Swal.fire({ icon: 'error', title: 'Dato duplicado', text: 'El ECU o CURP ya se encuentra registrado en el sistema.', confirmButtonColor: '#611232' });
        } else {
            Swal.fire({ icon: 'error', title: 'Error del Servidor', text: 'Ocurrió un problema al guardar. Revisa la consola.', confirmButtonColor: '#611232' });
        }
      } finally {
        // 🚀 Apagamos el botón de "Guardando..." pase lo que pase
        isSaving.value = false;
      }
    };

    return { 
      paciente, v$, isSaving, cerrarModal, guardar, onInputUpper, bloquearCaracteresEcu 
    };
  }
});
</script>

<style scoped>
/* ============================= */
/* ANIMACIONES */
/* ============================= */
.modal-fade-enter-active, .modal-fade-leave-active { transition: opacity 0.3s ease; }
.modal-fade-enter-from, .modal-fade-leave-to { opacity: 0; }

@keyframes elasticPop {
  0% { transform: scale(0.95); opacity: 0; }
  100% { transform: scale(1); opacity: 1; }
}

@keyframes spin {
  100% { transform: rotate(360deg); }
}

/* ============================= */
/* OVERLAY */
/* ============================= */
.custom-modal-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(20, 20, 20, 0.5);
  backdrop-filter: blur(4px);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem;
  z-index: 9999;
}

/* ============================= */
/* MODAL BOX */
/* ============================= */
.custom-modal-box {
  background: #ffffff;
  width: 100%;
  max-width: 900px;

  height: 100%;
  max-height: 95vh;

  border-radius: 12px;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  animation: elasticPop 0.25s ease-out;
}

/* 📱 MODO MOBILE FULLSCREEN */
@media (max-width: 640px) {
  .custom-modal-box {
    border-radius: 0;
    max-height: 100vh;
  }
}

/* ============================= */
/* HEADER */
/* ============================= */
.custom-modal-header {
  background-color: #611232;
  padding: 0.9rem 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-text {
  color: #fff;
  font-size: 1rem;
  display: flex;
  align-items: center;
}

.close-btn {
  background: rgba(255,255,255,0.1);
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 50%;
}

/* ============================= */
/* BODY */
/* ============================= */
.custom-modal-body {
  padding: 1rem;
  overflow-y: auto;
  flex: 1;
}

/* ============================= */
/* GRID RESPONSIVO */
/* ============================= */
.formulario-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 0.9rem;
}

/* Tablet */
@media (min-width: 640px) {
  .formulario-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

/* Desktop grande */
@media (min-width: 1024px) {
  .formulario-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1.2rem;
  }
}

.col-span-2 {
  grid-column: span 1;
}

/* En pantallas grandes sí ocupa 2 columnas */
@media (min-width: 640px) {
  .col-span-2 {
    grid-column: span 2;
  }
}

/* ============================= */
/* FORM */
/* ============================= */
.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-size: 0.8rem;
  font-weight: 600;
  margin-bottom: 4px;
}

.custom-input {
  padding: 0.55rem 0.7rem;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 0.9rem;
  background: #f8fafc;
}

.custom-input:focus {
  border-color: #611232;
  background: #fff;
  box-shadow: 0 0 0 2px rgba(97, 18, 50, 0.1);
}

.curp-input {
  font-family: monospace;
  letter-spacing: 1px;
}

/* ============================= */
/* RADIO */
/* ============================= */
.custom-radio-box {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.radio-label {
  flex: 1;
  text-align: center;
  padding: 0.45rem;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 0.85rem;
}

/* ============================= */
/* FOOTER */
/* ============================= */
.custom-modal-footer {
  padding: 0.8rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  background: #f8fafc;
  border-top: 1px solid #e2e8f0;
}

/* Botones en fila en pantallas grandes */
@media (min-width: 640px) {
  .custom-modal-footer {
    flex-direction: row;
    justify-content: flex-end;
  }
}

.custom-btn {
  padding: 0.6rem;
  font-size: 0.9rem;
  border-radius: 6px;
  font-weight: 600;
  display: flex;
  justify-content: center;
  align-items: center;
}

.btn-cancel {
  background: #94a3b8;
  color: white;
}

.btn-save {
  background: #611232;
  color: white;
}

/* ============================= */
/* ICONOS */
/* ============================= */
.icon-title {
  width: 20px;
  margin-right: 6px;
  filter: brightness(0) invert(1);
}

.icon-close {
  width: 16px;
  filter: brightness(0) invert(1);
}

.icon-label {
  width: 14px;
  margin-right: 4px;
  opacity: 0.7;
}

.btn-icon {
  width: 15px;
  margin-right: 5px;
  filter: brightness(0) invert(1);
}

.spinner {
  animation: spin 1s linear infinite;
}

/* ============================= */
/* ERRORES */
/* ============================= */
.error-text {
  font-size: 0.7rem;
  color: #e63946;
  margin-top: 2px;
}
</style>