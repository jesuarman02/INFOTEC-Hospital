<template>
  <transition name="fade">
    <div v-show="mostrarHeader" class="module-wrapper w-100">

      <div v-if="estadoVista === 'inicio'" class="view-container">
        <h3 class="title-main mb-5 text-uppercase">¿QUÉ DESEAS HACER?</h3>
        
        <div class="inicio-grid">
          <div class="big-action-card outline-card" @click="iniciarNuevoPaciente">
            <div class="icon-wrapper">
              <svg viewBox="0 0 24 24" class="svg-icon"><path d="M15 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm-9-2V7H4v3H1v2h3v3h2v-3h3v-2H6zm9 4c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>
            </div>
            <h4>Nuevo Paciente</h4>
            <p>Iniciar el registro de un nuevo paciente paso a paso</p>
          </div>

          <div class="big-action-card outline-card" @click="estadoVista = 'buscar'">
            <div class="icon-wrapper">
              <svg viewBox="0 0 24 24" class="svg-icon"><path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/></svg>
            </div>
            <h4>Buscar Paciente</h4>
            <p>Editar o agregar información de un paciente ya existente</p>
          </div>
        </div>
      </div>

      <div v-else-if="estadoVista === 'buscar'" class="view-container">
        <div class="header-nav w-100">
          <button @click="estadoVista = 'inicio'" class="btn-back">← Volver al inicio</button>
        </div>
        
        <div class="search-section-centered mt-4 w-100">
          <h2 class="title-main mb-4 text-uppercase" style="letter-spacing: 3px;">PACIENTES</h2>
          
          <div class="search-container mb-2">
            <div class="fancy-search-pill">
              <div class="search-icon-wrapper">
                <svg viewBox="0 0 24 24" style="width:24px; fill:#5c1830; opacity: 0.7;"><path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/></svg>
              </div>
              <input 
                type="text" 
                v-model="ecuBuscado" 
                @keyup.enter="buscarYMostrarDashboard" 
                placeholder="Ingresa un ECU" 
                class="minimal-input-pill" 
              />
            </div>
          </div>
          
          <div class="text-center mt-2 mb-5">
            <a href="#" @click.prevent="mostrarModalCurp = true" class="link-no-ecu">
              No conozco el ECU
            </a>
          </div>

          <div class="text-center">
            <button class="btn-guinda px-5 py-2 rounded-pill shadow-sm" @click="buscarYMostrarDashboard" :disabled="!ecuBuscado">
              Consultar Expediente
            </button>
          </div>
        </div>
      </div>

      <div v-else-if="estadoVista === 'nuevo'" class="view-container wizard-view">
        <div class="header-nav w-100">
          <button @click="estadoVista = 'inicio'" class="btn-back">← Cancelar Registro</button>
        </div>

        <transition name="slide-down">
          <div v-if="pacienteActual" class="current-patient-banner mb-4 w-100">
            <div class="d-flex align-items-center">
              <div class="avatar-circle mr-3">
                <svg viewBox="0 0 24 24"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>
              </div>
              <div class="text-left">
                <span class="d-block text-muted" style="font-size: 0.75rem; font-weight: 700; text-transform: uppercase;">Registrando a:</span>
                <h6 class="m-0 font-weight-bold" style="color: #5c1830;">{{ pacienteActual.nombreCompleto }} <span class="badge badge-light ml-2">ECU: {{ pacienteActual.ecu }}</span></h6>
              </div>
            </div>
          </div>
        </transition>

        <div class="step-progress">
          <template v-for="(paso, index) in pasos" :key="paso.id">
            <div class="step" :class="{ active: pasoWizard >= paso.id, completed: progreso[paso.key] }">
              <div class="circle">{{ paso.id }}</div>
              <span>{{ paso.short }}</span>
            </div>
            <div v-if="index < pasos.length - 1" class="line" :class="{ active: pasoWizard > paso.id }"></div>
          </template>
        </div>

        <div class="wizard-workarea mt-3">
          <div class="wizard-card compact-card">
            <div class="wizard-icon" v-html="pasoActualObj.svg"></div>
            <h5 class="mb-2" style="color: #5c1830; font-weight: 700;">Paso {{ pasoWizard }}: {{ pasoActualObj.titulo }}</h5>
            <p class="text-muted mb-4" style="font-size: 0.9rem;">{{ pasoActualObj.desc }}</p>
            
            <button class="btn-guinda px-4 py-2 rounded-pill mb-3" @click="pasoActualObj.action">
              {{ progreso[pasoActualObj.key] ? 'Editar Información' : 'Completar Formulario' }}
            </button>

            <div class="wizard-controls">
              <button class="btn-outline btn-sm" @click="pasoWizard--" :disabled="pasoWizard === 1">Anterior</button>
              <button class="btn-guinda btn-sm" @click="pasoWizard++" :disabled="pasoWizard === 5 || !progreso[pasoActualObj.key]">Siguiente →</button>
            </div>
          </div>
        </div>
      </div>

      <div v-else-if="estadoVista === 'paciente'" class="view-container dashboard-view">
        <div class="header-nav w-100">
          <button @click="estadoVista = 'inicio'" class="btn-back">← Cerrar Expediente</button>
          <h4 class="title-main m-0 ml-3">Gestión del Paciente</h4>
        </div>

        <div v-if="pacienteActual" class="current-patient-banner mb-4 w-100 shadow-sm">
          <div class="d-flex align-items-center">
            <div class="avatar-circle mr-3">
              <svg viewBox="0 0 24 24"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>
            </div>
            <div class="text-left flex-grow-1">
              <span class="d-block text-muted" style="font-size: 0.75rem; font-weight: 700; text-transform: uppercase;">Expediente de:</span>
              <h5 class="m-0 font-weight-bold" style="color: #5c1830;">{{ pacienteActual.nombreCompleto }}</h5>
            </div>
            <div class="text-right">
               <span class="badge badge-light shadow-sm" style="font-size: 1rem; padding: 10px 15px; color: #5c1830; border: 1px solid #e2e8f0;">ECU: {{ pacienteActual.ecu }}</span>
            </div>
          </div>
        </div>

        <div class="completion-banner shadow-sm mb-4">
          <div class="d-flex justify-content-between align-items-center mb-2">
            <h6 class="m-0 font-weight-bold" style="color: #5c1830;">Progreso del Expediente</h6>
            <span class="font-weight-bold">{{ porcentajeCompletado }}% Completado</span>
          </div>
          <div class="progress" style="height: 12px; border-radius: 10px; background-color: #e2e8f0;">
            <div class="progress-bar" role="progressbar" :style="{ width: porcentajeCompletado + '%', backgroundColor: '#5c1830' }"></div>
          </div>
        </div>

        <div class="dashboard-grid-horizontal mt-3">
          <div v-for="paso in pasos.slice(0, 4)" :key="'dash-'+paso.id" class="dash-card" @click="paso.action">
            <div class="dash-icon" v-html="paso.svg"></div>
            <h6 class="dash-title">{{ paso.titulo }}</h6>
            <span class="badge-status" :class="progreso[paso.key] ? 'badge-complete' : 'badge-pending'">
              {{ progreso[paso.key] ? '✔ Completo' : '❌ Incompleto' }}
            </span>
          </div>
        </div>
        
        <div class="dashboard-grid-horizontal-center mt-4">
          <div class="dash-card" style="width: 280px;" @click="pasos[4].action">
            <div class="dash-icon" v-html="pasos[4].svg"></div>
            <h6 class="dash-title">{{ pasos[4].titulo }}</h6>
            <span class="badge-status" :class="progreso[pasos[4].key] ? 'badge-complete' : 'badge-pending'">
              {{ progreso[pasos[4].key] ? '✔ Completo' : '❌ Incompleto' }}
            </span>
          </div>
        </div>
      </div>

      <transition name="fade-scale">
        <div v-if="mostrarModalCurp" class="modal-overlay" @click.self="cerrarModalCurp">
          <div class="modal-content shadow-lg border-0 rounded-lg p-4">

            <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
              <h5 class="modal-title m-0 font-weight-bold" style="color: #5c1830;">
                <img src="/content/images/search.svg" style="width: 20px; filter: brightness(0) saturate(100%) invert(13%) sepia(50%) saturate(4422%) hue-rotate(320deg) brightness(88%) contrast(98%); margin-right: 8px;"/> 
                Buscar ECU
              </h5>
              <button class="btn btn-sm btn-light rounded-circle font-weight-bold text-muted" @click="cerrarModalCurp">✕</button>
            </div>

            <div v-if="!esExtranjero">
              <div class="input-group mb-1 shadow-sm rounded-lg overflow-hidden border">
                <div class="input-group-prepend">
                  <span class="input-group-text bg-light border-0">
                    <img src="/content/images/person-vcard.svg" style="width: 22px; opacity: 0.6;" />
                  </span>
                </div>
                <input class="form-control border-0 bg-light text-uppercase font-weight-bold" v-model="curp" @input="formatearCurp" placeholder="INGRESA EL CURP DEL PACIENTE" maxlength="18" style="font-size: 0.95rem; padding-left: 5px;"/>
              </div>
              
              <div class="d-flex justify-content-between mb-4 mt-2">
                <small class="text-danger font-weight-bold" v-if="curp.length > 0 && !isCurpValid">Formato de CURP inválido</small>
                <small v-else></small>
                <a href="https://www.gob.mx/curp/" target="_blank" class="small font-weight-bold link-gob" style="text-decoration: underline;">¿No conoces el CURP?</a>
              </div>
            </div>

            <div v-else>
              <div class="input-group mb-3 shadow-sm rounded-lg overflow-hidden border">
                 <div class="input-group-prepend"><span class="input-group-text bg-light border-0"><img src="/content/images/user.svg" style="width: 20px; opacity: 0.6;" /></span></div>
                <input class="form-control border-0 bg-light text-uppercase font-weight-bold" v-model="nombre" @input="formatearNombre" placeholder="NOMBRE(S)" maxlength="50"/>
              </div>
              
              <div class="input-group mb-4 shadow-sm rounded-lg overflow-hidden border">
                <div class="input-group-prepend"><span class="input-group-text bg-light border-0"><img src="/content/images/user.svg" style="width: 20px; opacity: 0.6;" /></span></div>
                <input class="form-control border-0 bg-light text-uppercase font-weight-bold" v-model="apellidoPaterno" @input="formatearApellido" placeholder="APELLIDO PATERNO" maxlength="50"/>
              </div>
            </div>

            <div class="form-check mb-4 border-top pt-3 text-center">
              <input class="form-check-input" type="checkbox" id="checkExtranjero" v-model="esExtranjero">
              <label class="form-check-label font-weight-bold text-secondary" for="checkExtranjero" style="cursor:pointer;">Paciente extranjero (Sin CURP)</label>
            </div>

            <div v-if="errorBusqueda" class="alert alert-danger text-center p-2 mb-3 font-weight-bold" style="font-size: 0.85rem;">{{ errorBusqueda }}</div>

            <button class="btn btn-guinda btn-block rounded-pill shadow-sm mb-2 anim-btn font-weight-bold d-flex justify-content-center align-items-center" @click="buscarEcuPorCurp" :disabled="!isFormValid || cargando">
              <span v-if="cargando" class="spinner-border spinner-border-sm mr-2" role="status" aria-hidden="true"></span>
              <img v-else src="/content/images/check-circle.svg" style="width: 18px; filter: brightness(0) invert(1); margin-right: 8px;"/> 
              {{ cargando ? 'Buscando...' : 'Encontrar' }}
            </button>

          </div>
        </div>
      </transition>

      <PacienteModal 
        v-model:visible="mostrarModalPaciente" 
        :pacienteToEdit="pacienteActual"  @save="simularGuardadoPaciente" 
        @saved="simularGuardadoPaciente" 
      />
      <DireccionModal 
        v-model:visible="mostrarModalDireccion" 
        :pacientePreCargado="pacienteActual" 
        @save="simularGuardadoDireccion" 
        @saved="simularGuardadoDireccion" 
      />
      <InfoSocioeconomicaWizardModal 
        v-model:visible="mostrarModalSocio" 
        :pacientePreCargado="pacienteActual" 
        @save="simularGuardadoSocio" 
        @saved="simularGuardadoSocio" 
      />
      <HistorialMedicoModal 
        v-model:visible="mostrarModalHistorial" 
        :pacientePreCargado="pacienteActual" 
        @save="simularGuardadoHistorial" 
        @saved="simularGuardadoHistorial" 
      />
      <SignosVitalesModal 
        v-model:visible="mostrarModalSignos" 
        :pacientePreCargado="pacienteActual" 
        @save="simularGuardadoSignos" 
        @saved="simularGuardadoSignos" 
      />

    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import Swal from 'sweetalert2'
import axios from 'axios'

import PacienteModal from '@/shared/modals/PacienteModal.vue'
import DireccionModal from '@/shared/modals/DireccionModal.vue'
import InfoSocioeconomicaWizardModal from '@/shared/modals/info-socioeconomica-wizard.vue'
import HistorialMedicoModal from '@/shared/modals/historial-medico-modal.vue'
// 🔥 IMPORTAMOS EL MODAL DE SIGNOS VITALES 🔥
import SignosVitalesModal from '@/shared/modals/signos-vitales-modal.vue'
import PacienteService from '@/entities/pacientems/paciente/paciente.service';

const props = defineProps({ mostrarHeader: { type: Boolean, default: true } })

const pacienteService = new PacienteService();

const estadoVista = ref('inicio') 
const pasoWizard = ref(1)
const ecuBuscado = ref('')

const pacienteActual = ref<any>(null)
const mostrarModalCurp = ref(false)

const progreso = ref<Record<string, boolean>>({
  paciente: false, direccion: false, socio: false, historial: false, signos: false
})

const porcentajeCompletado = computed(() => {
  const total = Object.keys(progreso.value).length
  const completados = Object.values(progreso.value).filter(v => v).length
  return Math.round((completados / total) * 100)
})

const mostrarModalPaciente = ref(false)
const mostrarModalDireccion = ref(false)
const mostrarModalSocio = ref(false)
const mostrarModalHistorial = ref(false)
// 🔥 VARIABLE REACTIVA PARA EL MODAL DE SIGNOS VITALES 🔥
const mostrarModalSignos = ref(false)

const pasos = [
  { id: 1, key: 'paciente', short: 'Paciente', titulo: 'Datos del Paciente', desc: 'Registra la información básica: Nombre, apellidos, sexo y CURP.', action: () => mostrarModalPaciente.value = true, svg: '<svg viewBox="0 0 24 24"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>' },
  { id: 2, key: 'direccion', short: 'Dirección', titulo: 'Dirección de Residencia', desc: 'Ingresa el domicilio actual, código postal, estado, municipio.', action: () => mostrarModalDireccion.value = true, svg: '<svg viewBox="0 0 24 24"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg>' },
  { id: 3, key: 'socio', short: 'Socioec.', titulo: 'Info. Socioeconómica', desc: 'Captura el nivel socioeconómico, ingresos mensuales, y número de dependientes.', action: () => mostrarModalSocio.value = true, svg: '<svg viewBox="0 0 24 24"><path d="M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z"/></svg>' },
  { id: 4, key: 'historial', short: 'Historial', titulo: 'Historial Médico', desc: 'Registra antecedentes familiares, alergias conocidas, y padecimientos crónicos.', action: () => mostrarModalHistorial.value = true, svg: '<svg viewBox="0 0 24 24"><path d="M19 3H5c-1.1 0-1.99.9-1.99 2L3 19c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-1 11h-4v4h-4v-4H6v-4h4V6h4v4h4v4z"/></svg>' },
  // 🔥 ACCIÓN ACTUALIZADA PARA ABRIR EL MODAL DE SIGNOS VITALES 🔥
  { id: 5, key: 'signos', short: 'Signos', titulo: 'Signos Vitales', desc: 'Ingresa la toma actual de presión arterial, temperatura, peso, y talla.', action: () => mostrarModalSignos.value = true, svg: '<svg viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg>' }
]

const pasoActualObj = computed(() => pasos.find(p => p.id === pasoWizard.value) || pasos[0])

const iniciarNuevoPaciente = () => {
  Object.keys(progreso.value).forEach(k => progreso.value[k] = false)
  pacienteActual.value = null 
  pasoWizard.value = 1
  estadoVista.value = 'nuevo'
}

const buscarYMostrarDashboard = async () => {
  if (!ecuBuscado.value.trim()) return
  Swal.fire({ title: 'Buscando Expediente...', text: 'Consultando base de datos', allowOutsideClick: false, didOpen: () => { Swal.showLoading() } })
  try {
    const res = await pacienteService.retrieve();
    const ecuNumerico = parseInt(ecuBuscado.value, 10);
    const pacienteEncontrado = res.data.find((p: any) => p.ecu === ecuNumerico);

    if (pacienteEncontrado) {
      pacienteActual.value = pacienteEncontrado;
      pacienteActual.value.nombreCompleto = `${pacienteEncontrado.nombre} ${pacienteEncontrado.apellidoPaterno} ${pacienteEncontrado.apellidoMaterno || ''}`.trim();
      
      progreso.value.paciente = true; 
      progreso.value.direccion = !!pacienteEncontrado.direccion; 
      progreso.value.socio = !!pacienteEncontrado.infoSocioeconomica;

      // 🔥 BÚSQUEDA MANUAL DE HISTORIAL MÉDICO
      try {
        const HistorialService = (await import('@/entities/pacientems/historial-medico/historial-medico.service')).default;
        const hs = new HistorialService();
        const resHistorial = await hs.retrieve();
        progreso.value.historial = resHistorial.data.some((hm: any) => hm.pacienteId === pacienteEncontrado.id);
      } catch (e) {
        progreso.value.historial = !!pacienteEncontrado.historialMedico;
      }

      // 🔥 BÚSQUEDA MANUAL DE SIGNOS VITALES (NUEVO) 🔥
      try {
        const SignosService = (await import('@/entities/pacientems/signos-vitales/signos-vitales.service')).default;
        const ss = new SignosService();
        const resSignos = await ss.retrieve();
        progreso.value.signos = resSignos.data.some((sv: any) => sv.pacienteEcu === pacienteEncontrado.ecu || sv.paciente?.id === pacienteEncontrado.id);
      } catch (e) {
        progreso.value.signos = !!pacienteEncontrado.signosVitales;
      }

      Swal.close();
      estadoVista.value = 'paciente';
      ecuBuscado.value = '';
    } else {
      Swal.fire({ icon: 'error', title: 'No encontrado', text: 'No existe un paciente con ese ECU en el sistema.', confirmButtonColor: '#611232' });
    }
  } catch (error) {
    console.error(error);
    Swal.fire({ icon: 'error', title: 'Error', text: 'Fallo al conectar con el servidor.', confirmButtonColor: '#611232' });
  }
}
const avanzarSiWizard = () => { if (estadoVista.value === 'nuevo' && pasoWizard.value < 5) pasoWizard.value++ }

const simularGuardadoPaciente = (pacienteRegistrado: any) => {
  progreso.value.paciente = true
  if (pacienteRegistrado && pacienteRegistrado.ecu) {
     pacienteActual.value = pacienteRegistrado; 
     pacienteActual.value.nombreCompleto = `${pacienteRegistrado.nombre} ${pacienteRegistrado.apellidoPaterno}`;
  }
  avanzarSiWizard()
}

const simularGuardadoDireccion = () => { progreso.value.direccion = true; avanzarSiWizard(); }
const simularGuardadoSocio = () => { progreso.value.socio = true; avanzarSiWizard(); }
const simularGuardadoHistorial = () => { progreso.value.historial = true; avanzarSiWizard(); }
// 🔥 FUNCIÓN PARA MARCAR SIGNOS VITALES COMO COMPLETADOS 🔥
const simularGuardadoSignos = () => { progreso.value.signos = true; avanzarSiWizard(); }

const esExtranjero = ref(false);
const curp = ref('');
const nombre = ref('');
const apellidoPaterno = ref('');
const errorBusqueda = ref('');
const cargando = ref(false);

const regexCurp = /^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$/;

const formatearCurp = () => curp.value = curp.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
const formatearNombre = () => nombre.value = nombre.value.toUpperCase().replace(/[^A-ZÀ-Ÿ\u00f1\u00d1 ]/g, '').substring(0, 50);
const formatearApellido = () => apellidoPaterno.value = apellidoPaterno.value.toUpperCase().replace(/[^A-ZÀ-Ÿ\u00f1\u00d1 ]/g, '').substring(0, 50);

const isCurpValid = computed(() => curp.value.length === 0 ? true : regexCurp.test(curp.value));
const isFormValid = computed(() => esExtranjero.value ? (nombre.value.trim().length > 0 && apellidoPaterno.value.trim().length > 0) : (curp.value.length === 18 && isCurpValid.value));

const cerrarModalCurp = () => {
  mostrarModalCurp.value = false;
  curp.value = ''; nombre.value = ''; apellidoPaterno.value = '';
  esExtranjero.value = false; errorBusqueda.value = '';
};

const buscarEcuPorCurp = async () => {
  try {
    cargando.value = true;
    errorBusqueda.value = '';
    const res = await axios.get('services/pacientesms/api/pacientes', { params: { page: 0, size: 500, sort: 'id,asc' } });
    
    let listaPacientes = [];
    if (Array.isArray(res.data)) listaPacientes = res.data;
    else if (res.data && Array.isArray(res.data.content)) listaPacientes = res.data.content;
    
    let pEncontrado = esExtranjero.value 
      ? listaPacientes.find((p: any) => p.nombre?.trim().toUpperCase() === nombre.value.trim() && p.apellidoPaterno?.trim().toUpperCase() === apellidoPaterno.value.trim())
      : listaPacientes.find((p: any) => p.curp === curp.value);

    if (pEncontrado && pEncontrado.ecu) {
      ecuBuscado.value = pEncontrado.ecu.toString();
      cerrarModalCurp();
      buscarYMostrarDashboard(); 
    } else {
      errorBusqueda.value = 'No se encontró ningún paciente con estos datos.';
    }
  } catch (error) {
    errorBusqueda.value = 'Error al conectar con la base de datos.';
  } finally {
    cargando.value = false;
  }
};
</script>

<style scoped src="../../content/css/searchbar2.css"></style>