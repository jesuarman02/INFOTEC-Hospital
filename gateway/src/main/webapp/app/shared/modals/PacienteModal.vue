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
            
            <div class="form-group" v-if="paciente.id">
              <label><img src="/content/images/fingerprint.svg" class="icon-label" /> ID</label>
              <input type="text" class="custom-input bg-light" v-model="paciente.id" readonly />
            </div>
            
            <div class="form-group">
              <label><img src="/content/images/hash.svg" class="icon-label" /> ECU <span class="text-danger">*</span></label>
              <!-- 🚀 CAMBIO 1: type="number", min="0" y el @keydown para bloquear signos y letras -->
              <input 
                type="number" 
                v-model="v$.ecu.$model" 
                @keydown="bloquearCaracteresEcu"
                min="0"
                required 
                class="custom-input" 
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
                <img src="/content/images/arrow-down-square.svg" class="select-icon" />
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
import { defineComponent, ref, watch, inject } from 'vue'; 
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
  props: { visible: { type: Boolean, required: true } },
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
          title: '¿Cerrar sin guardar?',
          text: "Tienes datos escritos que se perderán.",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#611232',
          cancelButtonColor: '#94a3b8',
          confirmButtonText: 'Sí, salir',
          cancelButtonText: 'Cancelar'
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
        if (paciente.value.id) {
          await pacienteService().update(paciente.value);
        } else {
          await pacienteService().create(paciente.value);
        }

        Swal.fire({
          icon: 'success',
          title: '¡Paciente Guardado!',
          text: 'La base de datos ha registrado la información.',
          showConfirmButton: false,
          timer: 1500
        });

        emit('saved'); // Le avisamos al componente padre que ya terminamos
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
/* ANIMACIONES */
.modal-fade-enter-active, .modal-fade-leave-active { transition: opacity 0.3s ease; }
.modal-fade-enter-from, .modal-fade-leave-to { opacity: 0; }

@keyframes elasticPop {
  0% { transform: scale(0.9); opacity: 0; }
  70% { transform: scale(1.02); opacity: 1; }
  100% { transform: scale(1); opacity: 1; }
}

@keyframes spin {
  100% { transform: rotate(360deg); }
}

/* MODAL ESTRUCTURA */
.custom-modal-overlay {
  position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
  background-color: rgba(20, 20, 20, 0.5);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  display: flex; justify-content: center; align-items: center;
  z-index: 9999;
}

.custom-modal-box {
  background: #ffffff; 
  width: 90%; max-width: 700px;
  max-height: 85vh; 
  border-radius: 12px; 
  box-shadow: 0 20px 40px -10px rgba(0, 0, 0, 0.3);
  display: flex; flex-direction: column; overflow: hidden;
  animation: elasticPop 0.3s ease-out;
}

.custom-modal-header {
  background-color: #611232; 
  padding: 1rem 1.5rem; 
  display: flex; justify-content: space-between; align-items: center;
  border-bottom: 3px solid #4a0d26; 
}

.title-text { 
  color: #ffffff; margin: 0; font-weight: bold; font-size: 1.15rem;
  display: flex; align-items: center;
}

.close-btn { 
  background: rgba(255,255,255,0.1); border: none; 
  width: 30px; height: 30px; border-radius: 50%;
  cursor: pointer; display: flex; justify-content: center; align-items: center;
  transition: all 0.2s ease; 
}
.close-btn:hover { background: rgba(255,255,255,0.25); transform: rotate(90deg); }

/* CLASES DE ICONOS SVG/PNG */
.icon-title { 
  width: 22px; height: 22px; margin-right: 8px; 
  /* Lo convierte a blanco brillante */
  filter: brightness(0) invert(1);
}
.icon-close {
  width: 16px; height: 16px; 
  filter: brightness(0) invert(1);
}
.icon-label {
  width: 15px; height: 15px; margin-right: 6px;
  vertical-align: text-bottom;
  opacity: 0.7; /* Suaviza el negro para que no sea tan agresivo */
}
.icon-error {
  width: 14px; height: 14px; margin-right: 4px;
  /* Lo convierte al color rojo de error #e63946 usando filtros CSS */
  filter: invert(27%) sepia(91%) saturate(5436%) hue-rotate(345deg) brightness(93%) contrast(97%);
}
.btn-icon {
  width: 16px; height: 16px; margin-right: 6px;
}
.select-icon { 
  position: absolute; right: 12px; top: 50%; transform: translateY(-50%); 
  width: 16px; height: 16px; opacity: 0.6; pointer-events: none; 
}
.spinner { animation: spin 1s linear infinite; }

.d-flex { display: flex; }
.align-center { align-items: center; }

/* CUERPO Y GRID */
.custom-modal-body { 
  padding: 1.2rem 1.5rem; overflow-y: auto; flex: 1; text-align: left;
}

.formulario-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 1rem; }
.col-span-2 { grid-column: span 2; }

.form-group { display: flex; flex-direction: column; }
.form-group label { margin-bottom: 0.3rem; font-weight: 700; color: #333; font-size: 0.85rem;}
.text-danger { color: #e63946; }

.custom-input {
  padding: 0.55rem 0.8rem; 
  border: 1px solid #cbd5e1;
  border-radius: 6px; font-size: 0.95rem; color: #1e293b;
  font-weight: 500; background-color: #f8fafc;
  transition: all 0.2s ease;
  width: 100%; box-sizing: border-box;
}
.custom-input:hover { border-color: #94a3b8; }
.custom-input:focus { 
  outline: none; border-color: #611232; 
  background-color: #ffffff;
  box-shadow: 0 0 0 3px rgba(97, 18, 50, 0.1); 
}
.curp-input { font-family: monospace; font-size: 1rem; letter-spacing: 1px; text-transform: uppercase; }

.custom-radio-box { display: flex; gap: 0.8rem; }
.radio-label {
  display: flex; align-items: center; gap: 0.5rem;
  padding: 0.5rem 0.8rem; border: 1px solid #cbd5e1; border-radius: 6px;
  cursor: pointer; transition: all 0.2s; background: #f8fafc; flex: 1; justify-content: center;
  margin: 0; font-size: 0.9rem;
}
.radio-label:hover { border-color: #611232; background: #fff5f8; }
.radio-label input[type="radio"] { accent-color: #611232; }

.select-wrapper { position: relative; }
select.custom-input { appearance: none; padding-right: 2rem; cursor: pointer; }

.error-text { color: #e63946; font-size: 0.75rem; margin-top: 0.2rem; font-weight: 600; display: flex; align-items: center; }
.text-highlight { color: #611232 !important; }
.box-highlight { border-left: 3px solid #611232; padding-left: 8px; }

/* FOOTER */
.custom-modal-footer {
  padding: 1rem 1.5rem; background-color: #f8fafc;
  display: flex; justify-content: flex-end; gap: 1rem;
  border-top: 1px solid #e2e8f0;
}

.custom-btn { 
  padding: 0.6rem 1.2rem; border-radius: 6px; cursor: pointer; 
  font-size: 0.95rem; font-weight: 700; border: none; 
  display: flex; align-items: center; transition: all 0.2s; 
}
.btn-cancel { background: #94a3b8; color: white; }
.btn-cancel:hover { background: #64748b; }
.btn-save { background: #611232; color: white; }
.btn-save:hover { background: #4a0d26; }
.btn-save:disabled { background: #a87b8d; cursor: not-allowed; }

/* Invertir colores de íconos en botones primarios y secundarios */
.btn-cancel .btn-icon, .btn-save .btn-icon {
  filter: brightness(0) invert(1);
}
</style>