<template>
  <transition name="modal-fade">
    <div v-if="visible" class="custom-modal-overlay">
      <div class="custom-modal-box wizard-box">
        
        <div class="custom-modal-header">
          <h3 class="title-text d-flex align-items-center">
            <font-awesome-icon icon="file-medical" class="mr-2"></font-awesome-icon> 
            Historial Médico
          </h3>
          <button class="close-btn" @click="cerrarModal" type="button">
            <font-awesome-icon icon="times" class="icon-close"></font-awesome-icon>
          </button>
        </div>

        <div class="custom-modal-body">
          <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
            
            <div class="search-section" v-if="!pacientePreCargado && !historialMedico.id">
              <h5 class="section-title">
                <font-awesome-icon icon="search" class="icon-label text-muted"></font-awesome-icon> Identificación del paciente
              </h5>
              <p class="text-muted" style="margin-top: -8px; font-size: 0.85rem;">Por favor ingresa su ECU</p>
              
              <div class="input-group mb-3">
                <input 
                  type="text" 
                  class="custom-input-wizard" 
                  style="border-top-right-radius: 0; border-bottom-right-radius: 0;"
                  v-model="ecuSearchString" 
                  placeholder="Ej: 12345" 
                  @input="ecuSearchString = ecuSearchString.replace(/\D/g, '')"
                  @keyup.enter.prevent="buscarPaciente()" 
                  :readonly="pacienteEncontrado !== null"
                />
                <div class="input-group-append">
                  <button class="btn btn-custom m-0 rounded-right" style="border-top-left-radius: 0; border-bottom-left-radius: 0;" type="button" @click="buscarPaciente()" :disabled="isSearchingEcu || !ecuSearchString || pacienteEncontrado !== null">
                    <font-awesome-icon icon="search" v-if="!isSearchingEcu"></font-awesome-icon>
                    <font-awesome-icon icon="sync" spin v-if="isSearchingEcu"></font-awesome-icon>
                  </button>
                </div>
              </div>
              
              <div v-if="pacienteEncontrado" class="alert alert-success d-flex align-items-center mb-0 shadow-sm">
                <font-awesome-icon icon="check-circle" class="mr-3 text-success" style="font-size: 1.5rem;"></font-awesome-icon>
                <div>
                  <strong>Paciente seleccionado:</strong><br>
                  {{ pacienteEncontrado.nombre }} {{ pacienteEncontrado.apellidoPaterno }} {{ pacienteEncontrado.apellidoMaterno }}
                </div>
              </div>
            </div>

            <div class="search-section" style="background-color: #fdf2f5; border-color: #fbcfe8;" v-if="pacientePreCargado">
               <h5 class="section-title mb-2" style="color: #5c1830;">
                <font-awesome-icon icon="user-injured" class="icon-label mr-2"></font-awesome-icon> Historial de:
              </h5>
              <div class="d-flex align-items-center bg-white p-2 rounded border justify-content-between">
                <div>
                  <span class="badge badge-secondary mr-2" style="font-size: 0.9rem;">ECU: {{ pacientePreCargado.ecu }}</span>
                  <span class="font-weight-bold">{{ pacientePreCargado.nombreCompleto }}</span>
                </div>
                <span class="badge badge-warning text-dark" style="font-size: 0.85rem;" v-if="!historialMedico.id">
                  <font-awesome-icon icon="save"></font-awesome-icon> Autoguardado en Borrador
                </span>
              </div>
            </div>

            <div v-if="pacienteEncontrado || pacientePreCargado || historialMedico.id" class="mt-4">
              
              <div class="accordion-item mb-3 shadow-sm border">
                <div class="accordion-header" @click="toggleSeccion(1)">
                  <h5 class="mb-0 text-dark"><font-awesome-icon icon="weight" class="mr-2"></font-awesome-icon> 1. Datos Biométricos</h5>
                  <font-awesome-icon :icon="seccionAbierta === 1 ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
                </div>
                <transition name="slide-fade">
                  <div class="accordion-body p-4 bg-white" v-show="seccionAbierta === 1">
                    <div class="row">
                      <div class="form-group col-md-4">
                        <label class="label-wizard">Altura (cm)</label>
                        <input type="number" class="custom-input-wizard" v-model="historialMedico.altura" placeholder="Ej: 175" min="20" max="250" @input="calcularIMC" />
                      </div>
                      <div class="form-group col-md-4">
                        <label class="label-wizard">Peso (kg)</label>
                        <input type="number" step="0.1" class="custom-input-wizard" v-model="historialMedico.peso" placeholder="Ej: 70.5" min="0.5" max="350" @input="calcularIMC" />
                      </div>
                      <div class="form-group col-md-4">
                        <label class="label-wizard">IMC</label>
                        <input type="text" class="custom-input-wizard font-weight-bold text-center" v-model="historialMedico.imc" readonly style="color: #611232;" />
                      </div>
                      <div class="form-group col-md-6">
                        <label class="label-wizard">Grupo Sanguíneo</label>
                        <select class="custom-input-wizard" v-model="historialMedico.grupoSanguineo">
                          <option value="null">-- Seleccione --</option>
                          <option value="A">A</option><option value="B">B</option><option value="AB">AB</option><option value="O">O</option>
                        </select>
                      </div>
                      <div class="form-group col-md-6">
                        <label class="label-wizard">Factor RH</label>
                        <select class="custom-input-wizard" v-model="historialMedico.factorRh">
                          <option value="null">-- Seleccione --</option>
                          <option value="+">Positivo (+)</option><option value="-">Negativo (-)</option>
                        </select>
                      </div>
                    </div>
                  </div>
                </transition>
              </div>

              <div class="accordion-item mb-3 shadow-sm border">
                <div class="accordion-header" @click="toggleSeccion(2)">
                  <h5 class="mb-0 text-dark"><font-awesome-icon icon="exclamation-triangle" class="mr-2"></font-awesome-icon> 2. Alergias</h5>
                  <font-awesome-icon :icon="seccionAbierta === 2 ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
                </div>
                <transition name="slide-fade">
                  <div class="accordion-body p-4 bg-white" v-show="seccionAbierta === 2">
                    <p class="font-weight-bold mb-3">¿Tiene alergias conocidas?</p>
                    <div class="row mb-3">
                      <div class="col-md-6">
                        <label class="custom-radio-option">
                          <input type="radio" :value="true" v-model="historialMedico.tieneAlergias" @change="limpiarAlergias">
                          <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                        </label>
                      </div>
                      <div class="col-md-6">
                        <label class="custom-radio-option">
                          <input type="radio" :value="false" v-model="historialMedico.tieneAlergias" @change="limpiarAlergias">
                          <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                        </label>
                      </div>
                    </div>

                    <div v-if="historialMedico.tieneAlergias === true" class="p-3 bg-light border rounded mt-3">
                      <div class="row">
                        <div class="form-group col-md-6">
                          <label class="label-wizard">Tipo de alergia</label>
                          <select class="custom-input-wizard" v-model="historialMedico.tipoAlergia">
                            <option value="null">-- Seleccione el origen --</option>
                            <option value="MEDICAMENTOS">Medicamentos</option>
                            <option value="ALIMENTOS">Alimentos</option>
                            <option value="AMBIENTALES">Ambientales</option>
                            <option value="CONTACTO">Contacto</option>
                            <option value="PICADURAS">Picaduras o Animales</option>
                            <option value="OTROS">Otros</option>
                          </select>
                        </div>
                        <div class="form-group col-md-6">
                          <label class="label-wizard">Sustancia exacta</label>
                          <input type="text" class="custom-input-wizard" v-model="historialMedico.sustanciaAlergia" placeholder="Ej: Penicilina, Mariscos..." />
                        </div>

                        <div class="form-group col-12 mt-3">
                          <label class="label-wizard">Reacción que presenta (Seleccione varias)</label>
                          <div class="row">
                            <div class="col-md-4">
                              <span class="text-success font-weight-bold mb-2 d-block">Leves</span>
                              <label class="custom-checkbox-option"><input type="checkbox" value="Picazón" v-model="historialMedico.reaccionesAlergia"><div class="checkbox-square"></div><span>Picazón</span></label>
                              <label class="custom-checkbox-option"><input type="checkbox" value="Estornudos" v-model="historialMedico.reaccionesAlergia"><div class="checkbox-square"></div><span>Estornudos</span></label>
                              <label class="custom-checkbox-option"><input type="checkbox" value="Congestión" v-model="historialMedico.reaccionesAlergia"><div class="checkbox-square"></div><span>Congestión nasal</span></label>
                            </div>
                            <div class="col-md-4">
                              <span class="text-warning font-weight-bold mb-2 d-block">Moderadas</span>
                              <label class="custom-checkbox-option"><input type="checkbox" value="Ronchas" v-model="historialMedico.reaccionesAlergia"><div class="checkbox-square"></div><span>Ronchas / Urticaria</span></label>
                              <label class="custom-checkbox-option"><input type="checkbox" value="Inflamación" v-model="historialMedico.reaccionesAlergia"><div class="checkbox-square"></div><span>Inflamación leve</span></label>
                              <label class="custom-checkbox-option"><input type="checkbox" value="Náuseas" v-model="historialMedico.reaccionesAlergia"><div class="checkbox-square"></div><span>Náuseas / Vómito</span></label>
                            </div>
                            <div class="col-md-4">
                              <span class="text-danger font-weight-bold mb-2 d-block">Graves</span>
                              <label class="custom-checkbox-option"><input type="checkbox" value="Dificultad respirar" v-model="historialMedico.reaccionesAlergia"><div class="checkbox-square border-danger"></div><span class="text-danger font-weight-bold">Dificultad respiratoria</span></label>
                              <label class="custom-checkbox-option"><input type="checkbox" value="Anafilaxia" v-model="historialMedico.reaccionesAlergia"><div class="checkbox-square border-danger"></div><span class="text-danger font-weight-bold">Anafilaxia</span></label>
                            </div>
                          </div>
                        </div>

                        <div class="form-group col-md-4 mt-3">
                          <label class="label-wizard">Gravedad</label>
                          <select class="custom-input-wizard" v-model="historialMedico.gravedadAlergia">
                            <option value="null">-- Seleccione --</option>
                            <option value="LEVE">Leve</option><option value="MODERADA">Moderada</option><option value="GRAVE">Grave</option><option value="ANAFILACTICA">Anafiláctica</option>
                          </select>
                        </div>
                        <div class="form-group col-md-4 mt-3">
                          <label class="label-wizard">Tratamiento habitual</label>
                          <select class="custom-input-wizard" v-model="historialMedico.tratamientoAlergia">
                            <option value="null">-- Seleccione --</option>
                            <option value="ANTIHISTAMINICOS">Antihistamínicos</option><option value="EPINEFRINA">Epinefrina</option><option value="NINGUNO">Ninguno</option>
                          </select>
                        </div>
                        <div class="form-group col-md-4 mt-3">
                          <label class="label-wizard">Último episodio</label>
                          <input type="date" class="custom-input-wizard" v-model="historialMedico.ultimoEpisodioAlergia" />
                        </div>
                      </div>
                    </div>
                  </div>
                </transition>
              </div>

              <div class="accordion-item mb-3 shadow-sm border">
                <div class="accordion-header" @click="toggleSeccion(3)">
                  <h5 class="mb-0 text-dark"><font-awesome-icon icon="heartbeat" class="mr-2"></font-awesome-icon> 3. Historial Patológico y Quirúrgico</h5>
                  <font-awesome-icon :icon="seccionAbierta === 3 ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
                </div>
                <transition name="slide-fade">
                  <div class="accordion-body p-4 bg-white" v-show="seccionAbierta === 3">
                    
                    <p class="font-weight-bold mb-3">¿Padece alguna enfermedad crónica?</p>
                    <div class="row mb-4">
                      <div class="col-md-4"><label class="custom-radio-option"><input type="radio" value="SI" v-model="historialMedico.padeceCronicas" @change="manejarCambioCronicas"><div class="radio-circle"></div><span class="radio-text">Sí</span></label></div>
                      <div class="col-md-4"><label class="custom-radio-option"><input type="radio" value="NO" v-model="historialMedico.padeceCronicas" @change="manejarCambioCronicas"><div class="radio-circle"></div><span class="radio-text">No</span></label></div>
                      <div class="col-md-4"><label class="custom-radio-option"><input type="radio" value="DESCONOCE" v-model="historialMedico.padeceCronicas" @change="manejarCambioCronicas"><div class="radio-circle"></div><span class="radio-text">Desconoce</span></label></div>
                    </div>
                    <div v-if="historialMedico.padeceCronicas === 'SI'" class="mb-4">
                      <div v-for="(enfermedad, index) in listaEnfermedades" :key="'enf'+index" class="p-3 mb-2 bg-light border rounded position-relative">
                        <button type="button" class="btn btn-sm btn-danger position-absolute" style="top: 10px; right: 10px;" @click="eliminarEnfermedad(index)" v-if="listaEnfermedades.length > 1"><font-awesome-icon icon="times"></font-awesome-icon></button>
                        <div class="row">
                          <div class="form-group col-md-4">
                            <label class="label-wizard">Clasificación</label>
                            <select class="custom-input-wizard" v-model="enfermedad.tipo">
                              <option value="null">-- Seleccione --</option>
                              <option value="Cardiovasculares">Cardiovasculares</option>
                              <option value="Metabólicas">Metabólicas</option>
                              <option value="Respiratorias">Respiratorias</option>
                            </select>
                          </div>
                          <div class="form-group col-md-4"><label class="label-wizard">Enfermedad Específica</label><input type="text" class="custom-input-wizard" v-model="enfermedad.nombre" /></div>
                          <div class="form-group col-md-4"><label class="label-wizard">Tratamiento actual</label><input type="text" class="custom-input-wizard" v-model="enfermedad.tratamiento" /></div>
                        </div>
                      </div>
                      <button type="button" class="btn btn-outline-dark btn-sm" @click="agregarEnfermedad">Añadir otra enfermedad</button>
                    </div>

                    <hr class="my-4">

                    <p class="font-weight-bold mb-3">¿Ha tenido cirugías previas?</p>
                    <div class="row mb-3">
                      <div class="col-md-6"><label class="custom-radio-option"><input type="radio" value="SI" v-model="historialMedico.haTenidoCirugias" @change="manejarCambioCirugias"><div class="radio-circle"></div><span class="radio-text">Sí</span></label></div>
                      <div class="col-md-6"><label class="custom-radio-option"><input type="radio" value="NO" v-model="historialMedico.haTenidoCirugias" @change="manejarCambioCirugias"><div class="radio-circle"></div><span class="radio-text">No</span></label></div>
                    </div>
                    <div v-if="historialMedico.haTenidoCirugias === 'SI'">
                      <div v-for="(cirugia, index) in listaCirugias" :key="'cir'+index" class="p-3 mb-2 bg-light border rounded position-relative">
                        <button type="button" class="btn btn-sm btn-danger position-absolute" style="top: 10px; right: 10px;" @click="eliminarCirugia(index)" v-if="listaCirugias.length > 1"><font-awesome-icon icon="times"></font-awesome-icon></button>
                        <div class="row">
                          <div class="form-group col-md-4">
                            <label class="label-wizard">Tipo</label>
                            <select class="custom-input-wizard" v-model="cirugia.tipo">
                              <option value="null">-- Seleccione --</option>
                              <option value="General">General</option>
                              <option value="Traumatológica">Traumatológica</option>
                            </select>
                          </div>
                          <div class="form-group col-md-5"><label class="label-wizard">Procedimiento</label><input type="text" class="custom-input-wizard" v-model="cirugia.nombre" /></div>
                          <div class="form-group col-md-3"><label class="label-wizard">Año</label><input type="text" class="custom-input-wizard" v-model="cirugia.anio" /></div>
                        </div>
                      </div>
                      <button type="button" class="btn btn-outline-dark btn-sm" @click="agregarCirugia">Añadir otra cirugía</button>
                    </div>

                  </div>
                </transition>
              </div>

              <div class="accordion-item mb-4 shadow-sm border">
                <div class="accordion-header" @click="toggleSeccion(4)">
                  <h5 class="mb-0 text-dark"><font-awesome-icon icon="pills" class="mr-2"></font-awesome-icon> 4. Medicamentos y Hábitos</h5>
                  <font-awesome-icon :icon="seccionAbierta === 4 ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
                </div>
                <transition name="slide-fade">
                  <div class="accordion-body p-4 bg-white" v-show="seccionAbierta === 4">
                    <p class="font-weight-bold mb-3">¿Toma medicamentos de forma regular?</p>
                    <div class="row mb-4">
                      <div class="col-md-6"><label class="custom-radio-option"><input type="radio" value="SI" v-model="historialMedico.tomaMedicamentos" @change="manejarCambioMedicamentos"><div class="radio-circle"></div><span class="radio-text">Sí</span></label></div>
                      <div class="col-md-6"><label class="custom-radio-option"><input type="radio" value="NO" v-model="historialMedico.tomaMedicamentos" @change="manejarCambioMedicamentos"><div class="radio-circle"></div><span class="radio-text">No</span></label></div>
                    </div>
                    
                    <div v-if="historialMedico.tomaMedicamentos === 'SI'">
                      <div v-for="(med, index) in listaMedicamentos" :key="'med'+index" class="p-3 mb-2 bg-light border rounded position-relative">
                        <button type="button" class="btn btn-sm btn-danger position-absolute" style="top: 10px; right: 10px;" @click="eliminarMedicamento(index)" v-if="listaMedicamentos.length > 1"><font-awesome-icon icon="times"></font-awesome-icon></button>
                        <div class="row">
                          <div class="form-group col-md-4"><label class="label-wizard">Medicamento</label><input type="text" class="custom-input-wizard" v-model="med.nombre" /></div>
                          <div class="form-group col-md-5"><label class="label-wizard">Motivo</label><input type="text" class="custom-input-wizard" v-model="med.motivo" /></div>
                          <div class="form-group col-md-3">
                            <label class="label-wizard">Frecuencia</label>
                            <select class="custom-input-wizard" v-model="med.frecuencia">
                              <option value="null">-- Seleccione --</option>
                              <option value="1 vez al día">1 vez al día</option>
                              <option value="Cada 12 horas">Cada 12 horas</option>
                            </select>
                          </div>
                        </div>
                      </div>
                      <button type="button" class="btn btn-outline-dark btn-sm mb-4" @click="agregarMedicamento">Añadir otro medicamento</button>
                    </div>

                    <hr class="my-4">

                    <p class="font-weight-bold mb-3">Hábitos y Consumo</p>
                    <div class="row">
                      <div class="form-group col-md-4">
                        <label class="label-wizard">Tabaco</label>
                        <select class="custom-input-wizard" v-model="formHabitos.tabaco"><option value="No fuma">No fuma</option><option value="Fuma actualmente">Fuma actualmente</option><option value="Exfumador">Exfumador</option></select>
                      </div>
                      <div class="form-group col-md-4">
                        <label class="label-wizard">Alcohol</label>
                        <select class="custom-input-wizard" v-model="formHabitos.alcohol"><option value="No">No</option><option value="Ocasional">Ocasional</option><option value="Regular">Regular</option></select>
                      </div>
                      <div class="form-group col-md-4">
                        <label class="label-wizard">Drogas</label>
                        <select class="custom-input-wizard" v-model="formHabitos.drogas"><option value="No">No</option><option value="Sí">Sí</option></select>
                      </div>
                      <div class="form-group col-12 mt-3 mb-0">
                        <label class="label-wizard">Observaciones Generales</label>
                        <textarea class="custom-input-wizard" v-model="historialMedico.observacionesGenerales" rows="2" placeholder="Notas adicionales del médico..."></textarea>
                      </div>
                    </div>
                  </div>
                </transition>
              </div>

            </div>
          </form>
        </div>

        <div class="custom-modal-footer" v-if="pacienteEncontrado || pacientePreCargado || historialMedico.id">
          <span class="text-muted font-weight-bold" style="font-size: 0.85rem;" v-if="!historialMedico.id">El borrador se guarda automáticamente.</span>
          <span v-else></span> <div>
            <button type="button" class="btn-cancel mr-2" @click="cerrarModal">
              <font-awesome-icon icon="ban" class="mr-2"></font-awesome-icon> Cancelar
            </button>
            <button type="button" class="btn-save" @click="save" :disabled="isSaving">
              <font-awesome-icon icon="save" class="mr-2" v-if="!isSaving"></font-awesome-icon>
              <font-awesome-icon icon="sync" spin class="mr-2" v-if="isSaving"></font-awesome-icon>
              {{ isSaving ? 'Guardando...' : 'Finalizar Historial' }}
            </button>
          </div>
        </div>

      </div>
    </div>
  </transition>
</template>

<script lang="ts" src="./historial-medico-modal.component.ts"></script>

<style scoped>
/* ======================================= */
/* 🔥 ESTILOS DEL MODAL 🔥                 */
/* ======================================= */
.modal-fade-enter-active, .modal-fade-leave-active { transition: opacity 0.3s ease; }
.modal-fade-enter-from, .modal-fade-leave-to { opacity: 0; }

.custom-modal-overlay { 
  position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; 
  background-color: rgba(20, 20, 20, 0.6); backdrop-filter: blur(5px); 
  display: flex; justify-content: center; align-items: center; z-index: 9999; 
}

.custom-modal-box.wizard-box { 
  background: #f4f6f8; width: 95%; max-width: 950px; max-height: 90vh; 
  border-radius: 12px; display: flex; flex-direction: column; overflow: hidden; 
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5); 
}

.custom-modal-header { 
  background-color: #611232; padding: 1.2rem 1.5rem; 
  display: flex; justify-content: space-between; align-items: center; 
}
.title-text { color: #ffffff; margin: 0; font-weight: bold; font-size: 1.2rem; }
.close-btn { 
  background: rgba(255,255,255,0.1); border: none; width: 32px; height: 32px; 
  border-radius: 50%; color: white; cursor: pointer; transition: 0.2s; 
  display: flex; justify-content: center; align-items: center;
}
.close-btn:hover { background: rgba(255,255,255,0.3); transform: rotate(90deg); }

.custom-modal-body { padding: 1.5rem; overflow-y: auto; flex: 1; }
.custom-modal-footer { 
  padding: 1rem 1.5rem; background-color: #ffffff; display: flex; 
  justify-content: space-between; align-items: center; border-top: 1px solid #e2e8f0; 
}

/* BOTONES DEL FOOTER */
.btn-cancel { padding: 0.6rem 1.2rem; border-radius: 8px; cursor: pointer; font-weight: 700; border: none; background: #94a3b8; color: white; transition: 0.2s; }
.btn-cancel:hover { background: #64748b; }
.btn-save { padding: 0.6rem 1.5rem; border-radius: 8px; cursor: pointer; font-weight: 700; border: none; background: #611232; color: white; transition: 0.2s; }
.btn-save:hover:not(:disabled) { background: #4a0d26; transform: translateY(-2px); box-shadow: 0 4px 10px rgba(97,18,50,0.3); }
.btn-save:disabled { background: #c5a4b1; cursor: not-allowed; }

/* ======================================= */
/* 🔥 ESTILOS DEL ACORDEÓN 🔥              */
/* ======================================= */
.accordion-item { border-radius: 8px; overflow: hidden; background-color: #ffffff; }
.accordion-header { 
  background-color: #e2e8f0; padding: 1rem 1.5rem; cursor: pointer; 
  display: flex; justify-content: space-between; align-items: center; 
  transition: background-color 0.2s; user-select: none;
}
.accordion-header:hover { background-color: #cbd5e1; }
.accordion-header h5 { font-size: 1.05rem; font-weight: 700; margin: 0; }
.accordion-body { border-top: 1px solid #e2e8f0; }

/* ANIMACIÓN ACORDEÓN */
.slide-fade-enter-active { transition: all 0.3s ease-out; }
.slide-fade-leave-active { transition: all 0.2s cubic-bezier(1, 0.5, 0.8, 1); }
.slide-fade-enter-from, .slide-fade-leave-to { transform: translateY(-10px); opacity: 0; }

/* ========================================= */
/* 🔥 RADIO BUTTONS Y CHECKBOXES PREMIUM 🔥  */
/* ========================================= */
.custom-radio-option {
  display: flex; align-items: center; padding: 12px 18px; margin-bottom: 10px;
  border: 2px solid #e2e8f0; border-radius: 12px; background-color: #ffffff;
  cursor: pointer; transition: all 0.3s ease; position: relative;
}
.custom-radio-option:hover { background-color: #f8fafc; border-color: #cbd5e1; transform: translateY(-2px); box-shadow: 0 4px 10px rgba(0,0,0,0.03); }
.custom-radio-option input[type="radio"] { position: absolute; opacity: 0; cursor: pointer; }
.radio-circle { width: 22px; height: 22px; border-radius: 50%; border: 2px solid #cbd5e1; margin-right: 15px; display: flex; align-items: center; justify-content: center; transition: all 0.3s ease; flex-shrink: 0; }
.radio-text { font-size: 0.95rem; color: #475569; font-weight: 500; transition: all 0.3s ease; }
.custom-radio-option input[type="radio"]:checked ~ .radio-circle { border-color: #611232; background-color: #611232; }
.custom-radio-option input[type="radio"]:checked ~ .radio-circle::after { content: ''; width: 8px; height: 8px; border-radius: 50%; background-color: white; }
.custom-radio-option input[type="radio"]:checked ~ .radio-text { color: #611232; font-weight: 700; }
.custom-radio-option:has(input[type="radio"]:checked) { border-color: #611232; background-color: #fdf2f5; box-shadow: 0 4px 15px rgba(92, 24, 48, 0.1); }

/* CHECKBOXES (Para alergias) */
.custom-checkbox-option {
  display: flex; align-items: center; padding: 8px 12px; margin-bottom: 8px;
  border: 2px solid #e2e8f0; border-radius: 8px; background: #fff; cursor: pointer; transition: 0.3s;
}
.custom-checkbox-option input[type="checkbox"] { position: absolute; opacity: 0; cursor: pointer; }
.checkbox-square {
  width: 20px; height: 20px; border-radius: 4px; border: 2px solid #cbd5e1; margin-right: 12px;
  display: flex; align-items: center; justify-content: center; transition: 0.3s; flex-shrink: 0;
}
.custom-checkbox-option input[type="checkbox"]:checked ~ .checkbox-square { background: #611232; border-color: #611232; }
.custom-checkbox-option input[type="checkbox"]:checked ~ .checkbox-square::after { content: '✔'; color: white; font-size: 12px; font-weight: bold; }
.custom-checkbox-option:has(input[type="checkbox"]:checked) { border-color: #611232; background-color: #fdf2f5; }

/* ========================================= */
/* 🔥 INPUTS DE TEXTO PREMIUM 🔥             */
/* ========================================= */
.custom-input-wizard {  
  width: 100%; padding: 12px 16px; border: 2px solid #e2e8f0; border-radius: 8px;
  font-size: 0.95rem; color: #1e293b; font-weight: 600; background-color: #f8fafc; transition: all 0.3s ease;
}
.custom-input-wizard:focus { outline: none; border-color: #611232; background-color: #ffffff; box-shadow: 0 0 0 4px rgba(97, 18, 50, 0.15); }
.custom-input-wizard[readonly] { background-color: #e2e8f0; color: #64748b; border-color: #cbd5e1; cursor: not-allowed; }
.label-wizard { font-size: 0.95rem; color: #475569; font-weight: 700; margin-bottom: 6px; display: block; }
.btn-custom { background-color: #611232; border-color: #611232; color: white; transition: all 0.3s ease; }
.btn-custom:hover { background-color: #4a0d27; border-color: #4a0d27; transform: translateY(-2px); box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2); }
.btn-custom:active { transform: scale(0.97); }

/* ========================================= */
/* 📱 RESPONSIVO                             */
/* ========================================= */
@media (max-width: 768px) {
  .custom-modal-box.wizard-box { width: 98%; max-height: 95vh; }
  .custom-modal-header, .custom-modal-body, .custom-modal-footer { padding: 1rem; }
  .custom-radio-option { padding: 14px 18px; }
  .custom-modal-footer { flex-direction: column; }
  .custom-modal-footer > div { width: 100%; display: flex; flex-direction: column; gap: 10px; margin-top: 10px;}
  .btn-save, .btn-cancel { width: 100%; text-align: center; margin: 0 !important;}
}
</style>