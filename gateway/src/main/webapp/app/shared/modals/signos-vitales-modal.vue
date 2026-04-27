<template>
  <transition name="modal-fade">
    <div v-if="visible" class="custom-modal-overlay">
      <div class="custom-modal-box wizard-box">
        
        <div class="custom-modal-header">
          <h3 class="title-text d-flex align-items-center">
            <font-awesome-icon icon="heartbeat" class="mr-2"></font-awesome-icon> 
            Captura de Signos Vitales
          </h3>
          <button class="close-btn" @click="cerrarModal" type="button">
            <font-awesome-icon icon="times" class="icon-close"></font-awesome-icon>
          </button>
        </div>

        <div class="custom-modal-body">
          <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
            
            <div class="search-section" v-if="!pacientePreCargado && !signosVitales.id">
              <h5 class="section-title">
                <font-awesome-icon icon="search" class="icon-label text-muted"></font-awesome-icon> Identificación del paciente
              </h5>
              <p class="text-muted" style="margin-top: -8px; font-size: 0.85rem;">Por favor ingresa su ECU</p>
              
              <div class="input-group mb-3">
                <input type="text" class="custom-input-wizard" style="border-top-right-radius: 0; border-bottom-right-radius: 0;" v-model="ecuSearchString" placeholder="Ej: 12345" @input="ecuSearchString = ecuSearchString.replace(/\D/g, '')" @keyup.enter.prevent="buscarPaciente()" :readonly="pacienteEncontrado !== null" />
                <div class="input-group-append">
                  <button class="btn btn-custom m-0 rounded-right" style="border-top-left-radius: 0; border-bottom-left-radius: 0;" type="button" @click="buscarPaciente()" :disabled="isSearchingEcu || !ecuSearchString || pacienteEncontrado !== null">
                    <font-awesome-icon icon="search" v-if="!isSearchingEcu"></font-awesome-icon>
                    <font-awesome-icon icon="sync" spin v-if="isSearchingEcu"></font-awesome-icon>
                  </button>
                </div>
              </div>
              
              <div v-if="pacienteEncontrado" class="alert alert-success d-flex align-items-center mb-0 shadow-sm">
                <font-awesome-icon icon="check-circle" class="mr-3 text-success" style="font-size: 1.5rem;"></font-awesome-icon>
                <div><strong>Paciente seleccionado:</strong><br>{{ pacienteEncontrado.nombre }} {{ pacienteEncontrado.apellidoPaterno }}</div>
              </div>
            </div>

            <div class="search-section" style="background-color: #fdf2f5; border-color: #fbcfe8;" v-if="pacientePreCargado">
               <h5 class="section-title mb-2" style="color: #5c1830;">
                <font-awesome-icon icon="user-injured" class="icon-label mr-2"></font-awesome-icon> Toma de signos para:
              </h5>
              <div class="d-flex align-items-center bg-white p-2 rounded border justify-content-between">
                <div>
                  <span class="badge badge-secondary mr-2" style="font-size: 0.9rem;">ECU: {{ pacientePreCargado.ecu }}</span>
                  <span class="font-weight-bold">{{ pacientePreCargado.nombreCompleto }}</span>
                </div>
              </div>
            </div>

            <div v-if="pacienteEncontrado || pacientePreCargado" class="mt-4">
              
<div class="card mb-4 border shadow-sm" style="border-color: #e2e8f0 !important;">
                <div class="card-header bg-light text-dark font-weight-bold" style="border-bottom: 2px solid #611232;">
                  <font-awesome-icon icon="clock" class="mr-2" style="color: #611232;"></font-awesome-icon> Datos de la Toma
                </div>
                <div class="card-body row pb-2">
                  <div class="form-group col-md-5">
                    <label class="label-wizard">Fecha y Hora</label>
                    <div class="d-flex w-100">
                      <input type="datetime-local" class="custom-input-wizard" style="flex: 1; min-width: 0; border-top-right-radius: 0; border-bottom-right-radius: 0;" v-model="fechaRegistroLocal" required :class="{ 'is-invalid border-danger': v$.fechaRegistro.$invalid }" />
                      <button class="btn btn-light border m-0 d-flex align-items-center justify-content-center" style="flex-shrink: 0; border-left: none; border-top-left-radius: 0; border-bottom-left-radius: 0; border-color: #e2e8f0; color: #611232; padding: 0 15px;" type="button" @click="actualizarFechaHora" title="Sincronizar a hora actual">
                        <font-awesome-icon icon="sync"></font-awesome-icon>
                      </button>
                    </div>
                    <small class="form-text text-muted mt-1">Hora actual al abrir el registro</small>
                  </div>
                  <div class="form-group col-md-3">
                    <label class="label-wizard">Tipo de Toma</label>
                    <select class="custom-input-wizard" v-model="v$.tipo.$model">
                      <option value="Ingreso">Ingreso (Triage)</option>
                      <option value="Rutina">Rutina (Piso/Cama)</option>
                      <option value="Urgencia">Urgencia (Choque)</option>
                      <option value="Alta">Alta Médica</option>
                    </select>
                  </div>
                  <div class="form-group col-md-4">
                    <label class="label-wizard">Personal Responsable</label>
                    <input type="text" class="custom-input-wizard" v-model="v$.personal.$model" placeholder="Ej. Dr. Pérez / Enf. Rodríguez" />
                  </div>
                </div>
              </div>
              <div class="card mb-4 border shadow-sm" style="border-color: #e2e8f0 !important;">
                <div class="card-header bg-light text-dark font-weight-bold" style="border-bottom: 2px solid #611232;">
                  <font-awesome-icon icon="wave-square" class="mr-2" style="color: #611232;"></font-awesome-icon> Mediciones Vitales
                </div>
                <div class="card-body row pb-2">
                  <div class="form-group col-md-4">
                    <label class="label-wizard">Frec. Cardíaca</label>
                    <div class="input-with-suffix">
                      <input type="number" min="0" max="300" class="custom-input-wizard" v-model.number="v$.frecuenciaCardiaca.$model" placeholder="Ej: 75" :class="{ 'border-danger': v$.frecuenciaCardiaca.$invalid }" />
                      <span class="input-suffix">lpm</span>
                    </div>
                  </div>
                  <div class="form-group col-md-4">
                    <label class="label-wizard">Tensión Arterial</label>
                    <input type="text" class="custom-input-wizard text-center font-weight-bold" v-model="v$.tensionArterial.$model" placeholder="Ej: 120/80" :class="{ 'border-danger': v$.tensionArterial.$invalid }" />
                  </div>
                  <div class="form-group col-md-4">
                    <label class="label-wizard">Frec. Respiratoria</label>
                    <div class="input-with-suffix">
                      <input type="number" min="0" max="100" class="custom-input-wizard" v-model.number="v$.frecuenciaRespiratoria.$model" placeholder="Ej: 16" :class="{ 'border-danger': v$.frecuenciaRespiratoria.$invalid }" />
                      <span class="input-suffix">rpm</span>
                    </div>
                  </div>
                  
                  <div class="form-group col-md-4">
                    <label class="label-wizard">Temperatura</label>
                    <div class="input-with-suffix">
                      <input type="number" min="20" max="45" step="0.1" class="custom-input-wizard" v-model.number="v$.temperatura.$model" placeholder="Ej: 36.5" :class="{ 'border-danger': v$.temperatura.$invalid }" />
                      <span class="input-suffix">°C</span>
                    </div>
                  </div>
                  <div class="form-group col-md-4">
                    <label class="label-wizard text-primary">Sat. Oxígeno (SpO2)</label>
                    <div class="input-with-suffix">
                      <input type="number" min="0" max="100" class="custom-input-wizard border-primary text-primary" v-model.number="v$.saturacionOxigeno.$model" placeholder="Ej: 98" :class="{ 'border-danger': v$.saturacionOxigeno.$invalid }" />
                      <span class="input-suffix text-primary">%</span>
                    </div>
                  </div>
                  <div class="form-group col-md-4">
                    <label class="label-wizard text-warning">Glucosa Capilar</label>
                    <div class="input-with-suffix">
                      <input type="number" min="0" max="1000" class="custom-input-wizard border-warning" style="color: #d97706;" v-model.number="v$.glucosa.$model" placeholder="Ej: 90" :class="{ 'border-danger': v$.glucosa.$invalid }" />
                      <span class="input-suffix" style="color: #d97706;">mg/dL</span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="card mb-2 border shadow-sm" style="border-color: #e2e8f0 !important;">
                <div class="card-header bg-light text-dark font-weight-bold" style="border-bottom: 2px solid #611232;">
                  <font-awesome-icon icon="clipboard-check" class="mr-2" style="color: #611232;"></font-awesome-icon> Evaluación Extra
                </div>
                <div class="card-body">
                  <div class="form-group mb-4">
                    <label class="label-wizard text-center d-block mb-3">Nivel de Dolor (0 = Sin dolor, 10 = Máximo dolor)</label>
                    <div class="d-flex justify-content-between px-3 scale-container">
                      <div class="custom-control custom-radio custom-control-inline mx-0" v-for="n in 11" :key="n-1">
                        <input type="radio" :id="'dolor'+(n-1)" name="escalaDolor" :value="n-1" class="custom-control-input" v-model.number="v$.dolor.$model">
                        <label class="custom-control-label font-weight-bold" :for="'dolor'+(n-1)" :class="getColorDolor(n-1)">{{ n-1 }}</label>
                      </div>
                    </div>
                    <div class="d-flex justify-content-between px-3 mt-2 text-muted" style="font-size: 0.85em;">
                      <span>Sin dolor</span>
                      <span>Moderado</span>
                      <span>Intenso</span>
                      <span>Insuperable</span>
                    </div>
                  </div>
                  <hr>
                  <div class="row mt-4">
                    <div class="form-group col-md-4">
                      <label class="label-wizard">Estado de Conciencia</label>
                      <select class="custom-input-wizard" v-model="v$.estadoConciencia.$model">
                        <option value="Alerta">Alerta</option>
                        <option value="Responde a Voz">A Estímulo Verbal</option>
                        <option value="Responde a Dolor">A Estímulo Doloroso</option>
                        <option value="Inconsciente">Inconsciente</option>
                      </select>
                    </div>
                    <div class="form-group col-md-8 mb-0">
                      <label class="label-wizard">Observaciones Adicionales</label>
                      <textarea class="custom-input-wizard" rows="2" v-model="v$.observaciones.$model" placeholder="Ej: Paciente refiere mareo al levantarse..."></textarea>
                    </div>
                  </div>
                </div>
              </div>

            </div>
          </form>
        </div>

        <div class="custom-modal-footer" v-if="pacienteEncontrado || pacientePreCargado">
          <button type="button" class="btn-cancel" @click="cerrarModal">
            <font-awesome-icon icon="ban" class="mr-2"></font-awesome-icon> Cancelar
          </button>
          <button type="button" class="btn-save" @click="save" :disabled="isSaving || v$.$invalid">
            <font-awesome-icon icon="save" class="mr-2" v-if="!isSaving"></font-awesome-icon>
            <font-awesome-icon icon="sync" spin class="mr-2" v-if="isSaving"></font-awesome-icon>
            {{ isSaving ? 'Guardando...' : 'Registrar Signos' }}
          </button>
        </div>

      </div>
    </div>
  </transition>
</template>

<script lang="ts" src="./signos-vitales-modal.component.ts"></script>

<style scoped>
/* ======================================= */
/* 🔥 ESTILOS DEL MODAL 🔥                 */
/* ======================================= */
.modal-fade-enter-active, .modal-fade-leave-active { transition: opacity 0.3s ease; }
.modal-fade-enter-from, .modal-fade-leave-to { opacity: 0; }

.custom-modal-overlay { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: rgba(20, 20, 20, 0.6); backdrop-filter: blur(5px); display: flex; justify-content: center; align-items: center; z-index: 9999; }
.custom-modal-box.wizard-box { background: #f4f6f8; width: 95%; max-width: 850px; max-height: 90vh; border-radius: 12px; display: flex; flex-direction: column; overflow: hidden; box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5); }

.custom-modal-header { background-color: #611232; padding: 1.2rem 1.5rem; display: flex; justify-content: space-between; align-items: center; }
.title-text { color: #ffffff; margin: 0; font-weight: bold; font-size: 1.2rem; }
.close-btn { background: rgba(255,255,255,0.1); border: none; width: 32px; height: 32px; border-radius: 50%; color: white; cursor: pointer; transition: 0.2s; display: flex; justify-content: center; align-items: center; }
.close-btn:hover { background: rgba(255,255,255,0.3); transform: rotate(90deg); }

.custom-modal-body { padding: 1.5rem; overflow-y: auto; flex: 1; }
.custom-modal-footer { padding: 1rem 1.5rem; background-color: #ffffff; display: flex; justify-content: space-between; align-items: center; border-top: 1px solid #e2e8f0; }

/* INPUTS PREMIUM */
.custom-input-wizard { width: 100%; padding: 10px 14px; border: 2px solid #e2e8f0; border-radius: 8px; font-size: 0.95rem; color: #1e293b; font-weight: 600; background-color: #f8fafc; transition: all 0.3s ease; }
.custom-input-wizard:focus { outline: none; border-color: #611232; background-color: #ffffff; box-shadow: 0 0 0 3px rgba(97, 18, 50, 0.15); }
.label-wizard { font-size: 0.9rem; color: #475569; font-weight: 700; margin-bottom: 6px; display: block; }
.btn-custom { background-color: #611232; border-color: #611232; color: white; transition: all 0.3s ease; }

/* SUFIJOS (lpm, °C, %) */
.input-with-suffix { position: relative; display: flex; align-items: center; }
.input-suffix { position: absolute; right: 14px; font-size: 0.9rem; font-weight: bold; color: #64748b; pointer-events: none; }
.input-with-suffix .custom-input-wizard { padding-right: 45px; } 

/* ESCALA DE DOLOR */
.scale-container { flex-wrap: wrap; gap: 10px; }
.custom-control-label::before, .custom-control-label::after { width: 1.5rem; height: 1.5rem; top: -0.2rem; left: -2rem; }
.custom-control-label { padding-left: 0.5rem; font-size: 1.1rem; cursor: pointer; }
.custom-control-inline { margin-right: 0.5rem; }

/* BOTONES */
.btn-cancel { padding: 0.6rem 1.2rem; border-radius: 8px; cursor: pointer; font-weight: 700; border: none; background: #94a3b8; color: white; transition: 0.2s; }
.btn-cancel:hover { background: #64748b; }
.btn-save { padding: 0.6rem 1.5rem; border-radius: 8px; cursor: pointer; font-weight: 700; border: none; background: #611232; color: white; transition: 0.2s; }
.btn-save:hover:not(:disabled) { background: #4a0d26; transform: translateY(-2px); box-shadow: 0 4px 10px rgba(97,18,50,0.3); }
.btn-save:disabled { opacity: 0.6; cursor: not-allowed; }

/* RESPONSIVO */
@media (max-width: 768px) {
  .custom-modal-box.wizard-box { width: 98%; max-height: 95vh; }
  .custom-modal-header, .custom-modal-body, .custom-modal-footer { padding: 1rem; }
  .custom-modal-footer { flex-direction: column; gap: 10px; }
  .btn-save, .btn-cancel { width: 100%; text-align: center; }
}
</style>