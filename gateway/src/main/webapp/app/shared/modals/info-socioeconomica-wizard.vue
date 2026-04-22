<template>
  <transition name="modal-fade">
    <div v-if="visible" class="custom-modal-overlay">
      <div class="custom-modal-box wizard-box">
        
        <div class="custom-modal-header">
          <h3 class="title-text d-flex align-items-center">
            <img src="/content/images/clipboard.svg" style="width: 24px; height: 24px; filter: brightness(0) invert(1); margin-right: 10px;" /> 
            Estudio Socioeconómico
          </h3>
          <button class="close-btn" @click="cerrarModal" type="button">
            <font-awesome-icon icon="times" class="icon-close"></font-awesome-icon>
          </button>
        </div>

        <div class="custom-modal-body">
          
          <div v-if="!mostrarCuestionario" class="search-section mt-4 mx-auto" style="max-width: 600px;">
            <h5 class="section-title text-center mb-3">Identificación del paciente</h5>
            <p class="text-muted text-center" style="font-size: 0.9rem;">Por favor ingresa su ECU</p>
            
            <div class="input-group mb-4">
              <input 
                type="text" 
                class="form-control form-control-lg text-center font-weight-bold" 
                v-model="ecuSearchString" 
                placeholder="Ej: 12345" 
                @input="ecuSearchString = ecuSearchString.replace(/\D/g, '')"
                :readonly="pacienteEncontrado !== null"
              />
              <div class="input-group-append" v-if="isSearchingEcu">
                <span class="input-group-text bg-white">
                  <font-awesome-icon icon="sync" spin class="text-primary"></font-awesome-icon>
                </span>
              </div>
            </div>

            <div v-if="pacienteEncontrado" class="alert alert-success d-flex align-items-center mb-4 text-left shadow-sm">
              <font-awesome-icon icon="check-circle" class="mr-3" style="font-size: 1.5rem; color: #198754;"></font-awesome-icon>
              <div>
                <strong>Paciente seleccionado:</strong><br>
                {{ nombreCompleto }} ({{ edadCalculada }} - {{ formatoSexo }})
              </div>
              <button type="button" class="btn btn-sm btn-outline-success ml-auto" @click="limpiarBusqueda()">
                <font-awesome-icon icon="times"></font-awesome-icon>
              </button>
            </div>

            <button v-if="pacienteEncontrado" type="button" @click="iniciarCuestionario" class="btn btn-block btn-lg shadow" style="background-color: #611232; color: white; border-radius: 8px;">
              Iniciar Cuestionario (10 min)
            </button>
          </div>

          <form v-else name="editForm" role="form" novalidate v-on:submit.prevent="save()">
            
            <div class="alert alert-secondary d-flex justify-content-between align-items-center mb-4">
              <span class="font-weight-bold">Paciente: {{ nombreCompleto }}</span>
              <span class="badge badge-warning text-dark" style="font-size: 0.85rem;">
                <font-awesome-icon icon="save"></font-awesome-icon> Autoguardado en Borrador
              </span>
            </div>

            <div class="accordion-item mb-3 shadow-sm border">
              <div class="accordion-header" @click="toggleSeccion(1)">
                <h5 class="mb-0">1. Identidad y Familia</h5>
                <font-awesome-icon :icon="seccionAbierta === 1 ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
              </div>
              <transition name="slide-fade">
                <div class="accordion-body p-3 bg-white" v-show="seccionAbierta === 1">
                  <div class="card mb-4 border-dark">
                    <div class="card-header bg-light border-dark">
                      <h5 class="mb-0 text-center font-weight-bold text-dark">1. Nombre y datos de la persona</h5>
                    </div>
                    <div class="card-body">
                      <div class="row">
                        <div class="col-12 form-group">
                          <label class="label-wizard">Nombre completo:</label>
                          <input type="text" class="custom-input-wizard" :value="nombreCompleto" readonly />
                        </div>
                        <div class="col-12 form-group">
                          <label class="label-wizard">CURP:</label>
                          <input type="text" class="custom-input-wizard" :value="pacienteEncontrado?.curp" readonly />
                        </div>
                        <div class="col-md-4 form-group">
                          <label class="label-wizard">Edad:</label>
                          <input type="text" class="custom-input-wizard" :value="edadCalculada" readonly />
                        </div>
                        <div class="col-md-4 form-group">
                          <label class="label-wizard">Sexo:</label>
                          <input type="text" class="custom-input-wizard" :value="formatoSexo" readonly />
                        </div>
                        <div class="col-md-4 form-group">
                          <label class="label-wizard">Estado Civil:</label>
                          <input type="text" class="custom-input-wizard" :value="pacienteEncontrado?.estadoCivil" readonly />
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-header bg-light border-dark">
                      <h5 class="mb-0 text-center font-weight-bold text-dark">2. Documento oficial que presenta para acreditar su identidad</h5>
                    </div>
                    <div class="card-body">
                      <div class="form-group mb-0">
                        <div :class="{ 'is-invalid border border-danger p-2 rounded': v$.documentoIdentidad.$error }">
                          <label class="custom-radio-option">
                            <input type="radio" value="CREDENCIAL_VOTAR" v-model="v$.documentoIdentidad.$model">
                            <div class="radio-circle"></div><span class="radio-text">Credencial para votar vigente</span>
                          </label>
                          <label class="custom-radio-option">
                            <input type="radio" value="CARTILLA_MILITAR" v-model="v$.documentoIdentidad.$model">
                            <div class="radio-circle"></div><span class="radio-text">Cartilla del Servicio Militar Nacional</span>
                          </label>
                          <label class="custom-radio-option">
                            <input type="radio" value="PASAPORTE" v-model="v$.documentoIdentidad.$model">
                            <div class="radio-circle"></div><span class="radio-text">Pasaporte vigente</span>
                          </label>
                          <label class="custom-radio-option">
                            <input type="radio" value="INAPAM" v-model="v$.documentoIdentidad.$model">
                            <div class="radio-circle"></div><span class="radio-text">Credencial del Instituto Nacional de las Personas Adultas Mayores (INAPAM)</span>
                          </label>
                          <label class="custom-radio-option">
                            <input type="radio" value="CEDULA_PROFESIONAL" v-model="v$.documentoIdentidad.$model">
                            <div class="radio-circle"></div><span class="radio-text">Cédula profesional</span>
                          </label>
                          <label class="custom-radio-option">
                            <input type="radio" value="CONSTANCIA_IDENTIDAD" v-model="v$.documentoIdentidad.$model">
                            <div class="radio-circle"></div><span class="radio-text">Constancia de Identidad</span>
                          </label>
                        </div>
                        <div class="invalid-feedback d-block mt-2" v-if="v$.documentoIdentidad.$error">
                          <font-awesome-icon icon="exclamation-circle"></font-awesome-icon> Por favor, seleccione un documento oficial.
                        </div>
                      </div>
                    </div>
                  </div> 

                  <div class="card mb-4 border-dark">
                    <div class="card-header bg-light border-dark">
                      <h5 class="mb-0 text-center font-weight-bold text-dark">3. Tipo de vivienda</h5>
                    </div>
                    <div class="card-body">
                      <p class="font-weight-bold text-center mb-3">¿Su vivienda es...?</p>
                      <div class="form-group mb-0">
                        <div :class="{ 'is-invalid border border-danger p-2 rounded': v$.tipoVivienda.$error }">
                          <div class="row">
                            <div class="col-md-6">
                              <label class="custom-radio-option">
                                <input type="radio" value="CASA_INDEPENDIENTE" v-model="v$.tipoVivienda.$model">
                                <div class="radio-circle"></div><span class="radio-text">Casa independiente</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="ANEXO_CASA" v-model="v$.tipoVivienda.$model">
                                <div class="radio-circle"></div><span class="radio-text">Anexo a casa</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="VIVIENDA_MOVIL" v-model="v$.tipoVivienda.$model">
                                <div class="radio-circle"></div><span class="radio-text">Vivienda móvil</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="CUARTO_VECINDAD" v-model="v$.tipoVivienda.$model">
                                <div class="radio-circle"></div><span class="radio-text">Vivienda o cuarto en vecindad</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="EN_CONSTRUCCION" v-model="v$.tipoVivienda.$model">
                                <div class="radio-circle"></div><span class="radio-text">Vivienda en construcción no habitada</span>
                              </label>
                            </div>
                            <div class="col-md-6">
                              <label class="custom-radio-option">
                                <input type="radio" value="ASILO_ORFANATO_CONVENTO" v-model="v$.tipoVivienda.$model">
                                <div class="radio-circle"></div><span class="radio-text">Asilo, orfanato o convento</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="REFUGIO" v-model="v$.tipoVivienda.$model">
                                <div class="radio-circle"></div><span class="radio-text">Refugio</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="CUARTO_AZOTEA" v-model="v$.tipoVivienda.$model">
                                <div class="radio-circle"></div><span class="radio-text">Vivienda o cuarto en azotea</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="DEPARTAMENTO_EDIFICIO" v-model="v$.tipoVivienda.$model">
                                <div class="radio-circle"></div><span class="radio-text">Departamento en edificio / Unidad habitacional</span>
                              </label>
                            </div>
                          </div>
                        </div>
                        <div class="invalid-feedback d-block mt-2" v-if="v$.tipoVivienda.$error">
                          <font-awesome-icon icon="exclamation-circle"></font-awesome-icon> Por favor, seleccione el tipo de vivienda.
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-header bg-light border-dark">
                      <h5 class="mb-0 text-center font-weight-bold text-dark">4. ¿Cuántas personas habitan la vivienda?</h5>
                    </div>
                    <div class="card-body">
                      <div class="row text-center">
                        <div class="col-md-4 form-group">
                          <label class="label-wizard">Hombres:</label>
                          <input
                            type="text"
                            class="custom-input-wizard text-center"
                            maxlength="2"
                            :class="{ 'is-invalid border-danger': v$.numHombres.$error }"
                            v-model="v$.numHombres.$model"
                            @input="limpiarInputNumerico($event, 'numHombres')"                  
                          />
                          <div class="invalid-feedback text-danger font-weight-bold mt-1" v-if="v$.numHombres.$error">Debe ser un número entre 0 y 20.</div>
                        </div>
                        <div class="col-md-4 form-group">
                          <label class="label-wizard">Mujeres:</label>
                          <input
                            type="text"
                            class="custom-input-wizard text-center"
                            maxlength="2"
                            :class="{ 'is-invalid border-danger': v$.numMujeres.$error }"
                            v-model="v$.numMujeres.$model"
                            @input="limpiarInputNumerico($event, 'numMujeres')"                  
                          />
                          <div class="invalid-feedback text-danger font-weight-bold mt-1" v-if="v$.numMujeres.$error">Debe ser un número entre 0 y 20.</div>
                        </div>
                        <div class="col-md-4 form-group">
                          <label class="label-wizard text-primary">Total:</label>
                          <input
                            type="text"
                            class="custom-input-wizard text-center text-primary"
                            :value="totalHabitantes"
                            readonly
                          />
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      <div class="row m-0">
                        
                        <div class="col-md-4 p-4 border-right border-dark text-center d-flex flex-column justify-content-center">
                          <p class="font-weight-bold mb-3">5. ¿Se considera la persona jefa del hogar?</p>
                          <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.jefeHogar.$error }">
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="true" v-model="v$.jefeHogar.$model">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                            </label>
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="false" v-model="v$.jefeHogar.$model">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                            </label>
                          </div>
                          <div class="invalid-feedback d-block mt-1" v-if="v$.jefeHogar.$error">Seleccione una opción.</div>
                        </div>

                        <div class="col-md-4 p-4 border-right border-dark">
                          <p class="font-weight-bold mb-3 text-center">6. Teléfono de contacto</p>
                          <p class="text-center mb-3">¿Tiene teléfono?</p>
                          
                          <div class="form-group mb-3 text-center" :class="{ 'is-invalid border border-danger p-2 rounded': v$.tieneTelefono.$error }">
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="true" v-model="v$.tieneTelefono.$model">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                            </label>
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="false" v-model="v$.tieneTelefono.$model" @change="limpiarTelefonos()">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                            </label>
                          </div>
                          <div class="invalid-feedback d-block text-center mb-3" v-if="v$.tieneTelefono.$error">Seleccione una opción.</div>

                          <div v-if="infoSocioeconomica.tieneTelefono === true">
                            <div class="form-group mb-3">
                              <label class="label-wizard">Fijo:</label>
                              <input type="text" class="custom-input-wizard" maxlength="10" :class="{ 'is-invalid border-danger': v$.numeroFijo.$error }" v-model="v$.numeroFijo.$model" @input="limpiarInputNumerico($event, 'numeroFijo')" />
                              <div class="invalid-feedback text-danger font-weight-bold" v-if="v$.numeroFijo.$error">10 dígitos obligatorios.</div>
                            </div>
                            <div class="form-group mb-3">
                              <label class="label-wizard">Recados:</label>
                              <input type="text" class="custom-input-wizard" maxlength="10" :class="{ 'is-invalid border-danger': v$.numeroRecados.$error }" v-model="v$.numeroRecados.$model" @input="limpiarInputNumerico($event, 'numeroRecados')" />
                              <div class="invalid-feedback text-danger font-weight-bold" v-if="v$.numeroRecados.$error">10 dígitos obligatorios.</div>
                            </div>
                            <div class="form-group mb-0">
                              <label class="label-wizard">Celular:</label>
                              <input type="text" class="custom-input-wizard" maxlength="10" :class="{ 'is-invalid border-danger': v$.numeroCelular.$error }" v-model="v$.numeroCelular.$model" @input="limpiarInputNumerico($event, 'numeroCelular')" />
                              <div class="invalid-feedback text-danger font-weight-bold" v-if="v$.numeroCelular.$error">10 dígitos obligatorios.</div>
                            </div>
                          </div>
                        </div>

                        <div class="col-md-4 p-4">
                          <p class="font-weight-bold mb-3 text-center">7. ¿Padece alguna enfermedad o discapacidad?</p>
                          
                          <div class="form-group mb-3 text-center" :class="{ 'is-invalid border border-danger p-2 rounded': v$.padeceEnfermedad.$error }">
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="true" v-model="v$.padeceEnfermedad.$model">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                            </label>
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="false" v-model="v$.padeceEnfermedad.$model" @change="limpiarEnfermedad()">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                            </label>
                          </div>
                          <div class="invalid-feedback d-block text-center mb-3" v-if="v$.padeceEnfermedad.$error">Seleccione una opción.</div>

                          <div v-if="infoSocioeconomica.padeceEnfermedad === true">
                            <label class="label-wizard">¿Cuál?:</label>
                            <input 
                              type="text" 
                              class="custom-input-wizard" 
                              :class="{ 'is-invalid border-danger': v$.enfermedadCual.$error }"
                              v-model="v$.enfermedadCual.$model"
                            />
                            <div class="invalid-feedback text-danger font-weight-bold mt-1" v-if="v$.enfermedadCual.$error">Especifique la enfermedad.</div>
                          </div>
                        </div>

                      </div>
                    </div>
                  </div>

                </div>
              </transition>
            </div>

            <div class="accordion-item mb-3 shadow-sm border">
              <div class="accordion-header" @click="toggleSeccion(2)">
                <h5 class="mb-0">2. Salud, Educación y Cultura</h5>
                <font-awesome-icon :icon="seccionAbierta === 2 ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
              </div>
              <transition name="slide-fade">
                <div class="accordion-body p-3 bg-white" v-show="seccionAbierta === 2">
                  <div class="card mb-4 border-dark">
                    <div class="card-header bg-light border-dark">
                      <h5 class="mb-0 text-center font-weight-bold text-dark">8. Actualmente, ¿a qué Institución está afiliada/o o inscrita/o para recibir atención médica?</h5>
                    </div>
                    <div class="card-body">
                      <div class="form-group mb-0">
                        <div :class="{ 'is-invalid border border-danger p-2 rounded': v$.institucionMedica.$error }">
                          <div class="row">
                            <div class="col-md-4">
                              <label class="custom-radio-option">
                                <input type="radio" value="INSABI" v-model="v$.institucionMedica.$model">
                                <div class="radio-circle"></div><span class="radio-text">Instituto de la Salud para el Bienestar</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="PEMEX_DEFENSA_MARINA" v-model="v$.institucionMedica.$model">
                                <div class="radio-circle"></div><span class="radio-text">PEMEX, Defensa o Marina</span>
                              </label>
                            </div>
                            <div class="col-md-4">
                              <label class="custom-radio-option">
                                <input type="radio" value="IMSS" v-model="v$.institucionMedica.$model">
                                <div class="radio-circle"></div><span class="radio-text">IMSS</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="CLINICA_PRIVADA" v-model="v$.institucionMedica.$model">
                                <div class="radio-circle"></div><span class="radio-text">Clínica u hospital privado</span>
                              </label>
                            </div>
                            <div class="col-md-4">
                              <label class="custom-radio-option">
                                <input type="radio" value="ISSSTE" v-model="v$.institucionMedica.$model">
                                <div class="radio-circle"></div><span class="radio-text">ISSSTE</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="NINGUNA" v-model="v$.institucionMedica.$model">
                                <div class="radio-circle"></div><span class="radio-text">A ninguna</span>
                              </label>
                            </div>
                          </div>
                        </div>
                        <div class="invalid-feedback d-block mt-2" v-if="v$.institucionMedica.$error">
                          <font-awesome-icon icon="exclamation-circle"></font-awesome-icon> Por favor, seleccione una institución.
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      <div class="row m-0">
                        
                        <div class="col-md-4 p-4 border-right border-dark">
                          <p class="font-weight-bold mb-3 text-center">9. ¿Habla alguna lengua indígena?</p>
                          
                          <div class="form-group mb-3 text-center" :class="{ 'is-invalid border border-danger p-2 rounded': v$.hablaLenguaIndigena.$error }">
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="true" v-model="v$.hablaLenguaIndigena.$model">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                            </label>
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="false" v-model="v$.hablaLenguaIndigena.$model" @change="limpiarLengua()">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                            </label>
                          </div>
                          <div class="invalid-feedback d-block text-center mb-3" v-if="v$.hablaLenguaIndigena.$error">Seleccione una opción.</div>

                          <div v-if="infoSocioeconomica.hablaLenguaIndigena === true">
                            <label class="label-wizard">¿Cuál es?</label>
                            <input 
                              type="text" 
                              class="custom-input-wizard" 
                              :class="{ 'is-invalid border-danger': v$.lenguaIndigenaCual.$error }"
                              v-model="v$.lenguaIndigenaCual.$model" 
                            />
                            <div class="invalid-feedback text-danger font-weight-bold mt-1" v-if="v$.lenguaIndigenaCual.$error">Especifique la lengua.</div>
                          </div>
                        </div>

                        <div class="col-md-4 p-4 border-right border-dark text-center d-flex flex-column justify-content-center">
                          <p class="font-weight-bold mb-3">10. ¿También habla español?</p>
                          <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.hablaEspanol.$error }">
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="true" v-model="v$.hablaEspanol.$model">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                            </label>
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="false" v-model="v$.hablaEspanol.$model">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                            </label>
                          </div>
                          <div class="invalid-feedback d-block mt-1" v-if="v$.hablaEspanol.$error">Seleccione una opción.</div>
                        </div>

                        <div class="col-md-4 p-4">
                          <p class="font-weight-bold mb-3 text-center">11. De acuerdo con su cultura, ¿se considera indígena?</p>
                          
                          <div class="form-group mb-3 text-center" :class="{ 'is-invalid border border-danger p-2 rounded': v$.consideraIndigena.$error }">
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="true" v-model="v$.consideraIndigena.$model">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                            </label>
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="false" v-model="v$.consideraIndigena.$model" @change="limpiarGrupoIndigena()">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                            </label>
                          </div>
                          <div class="invalid-feedback d-block text-center mb-3" v-if="v$.consideraIndigena.$error">Seleccione una opción.</div>

                          <div v-if="infoSocioeconomica.consideraIndigena === true">
                            <label class="label-wizard">¿A qué grupo pertenece?</label>
                            <input 
                              type="text" 
                              class="custom-input-wizard" 
                              :class="{ 'is-invalid border-danger': v$.grupoIndigenaCual.$error }"
                              v-model="v$.grupoIndigenaCual.$model" 
                            />
                            <div class="invalid-feedback text-danger font-weight-bold mt-1" v-if="v$.grupoIndigenaCual.$error">Especifique el grupo.</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      <div class="row m-0 text-center">
                        <div class="col-md-6 p-0 border-right border-dark">
                          <div class="bg-light border-bottom border-dark p-2 font-weight-bold">
                            Alfabetismo
                          </div>
                          <div class="p-4 d-flex flex-column justify-content-center">
                            <p class="font-weight-bold mb-3">12. ¿Sabe leer y escribir un recado?</p>
                            <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.sabeLeerEscribir.$error }">
                              <label class="custom-radio-option text-left">
                                <input type="radio" :value="true" v-model="v$.sabeLeerEscribir.$model">
                                <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" :value="false" v-model="v$.sabeLeerEscribir.$model">
                                <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                              </label>
                            </div>
                            <div class="invalid-feedback d-block mt-1" v-if="v$.sabeLeerEscribir.$error">Seleccione una opción.</div>
                          </div>
                        </div>

                        <div class="col-md-6 p-0">
                          <div class="bg-light border-bottom border-dark p-2 font-weight-bold">
                            Asistencia a la escuela
                          </div>
                          <div class="p-4 d-flex flex-column justify-content-center">
                            <p class="font-weight-bold mb-3">13. ¿Actualmente asiste a la escuela?</p>
                            <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.estudia.$error }">
                              <label class="custom-radio-option text-left">
                                <input type="radio" :value="true" v-model="v$.estudia.$model">
                                <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" :value="false" v-model="v$.estudia.$model">
                                <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                              </label>
                            </div>
                            <div class="invalid-feedback d-block mt-1" v-if="v$.estudia.$error">Seleccione una opción.</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </transition>
            </div>

            <div class="accordion-item mb-3 shadow-sm border">
              <div class="accordion-header" @click="toggleSeccion(3)">
                <h5 class="mb-0">3. Situación Laboral y Económica</h5>
                <font-awesome-icon :icon="seccionAbierta === 3 ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
              </div>
              <transition name="slide-fade">
                <div class="accordion-body p-3 bg-white" v-show="seccionAbierta === 3">
                  <div class="card mb-4 border-dark">
                    <div class="card-header bg-light border-dark">
                      <h5 class="mb-0 text-center font-weight-bold text-dark">14. Condición de actividad, ¿actualmente?</h5>
                    </div>
                    <div class="card-body">
                      <div class="form-group mb-0">
                        <div :class="{ 'is-invalid border border-danger p-2 rounded': v$.condicionLaboral.$error }">
                          <div class="row">
                            <div class="col-md-4">
                              <label class="custom-radio-option">
                                <input type="radio" value="TRABAJA" v-model="v$.condicionLaboral.$model">
                                <div class="radio-circle"></div><span class="radio-text">Trabaja</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="ESTUDIA_TRABAJA" v-model="v$.condicionLaboral.$model">
                                <div class="radio-circle"></div><span class="radio-text">Estudia y trabaja</span>
                              </label>
                            </div>
                            <div class="col-md-4">
                              <label class="custom-radio-option">
                                <input type="radio" value="BUSCA_TRABAJO" v-model="v$.condicionLaboral.$model">
                                <div class="radio-circle"></div><span class="radio-text">Busca trabajo</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="QUEHACERES" v-model="v$.condicionLaboral.$model">
                                <div class="radio-circle"></div><span class="radio-text">Realiza quehaceres domésticos</span>
                              </label>
                            </div>
                            <div class="col-md-4">
                              <label class="custom-radio-option">
                                <input type="radio" value="ESTUDIA" v-model="v$.condicionLaboral.$model">
                                <div class="radio-circle"></div><span class="radio-text">Estudia</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="ELABORA_ARTESANIAS" v-model="v$.condicionLaboral.$model">
                                <div class="radio-circle"></div><span class="radio-text">Elabora artesanías</span>
                              </label>
                            </div>
                          </div>
                        </div>
                        <div class="invalid-feedback d-block mt-2" v-if="v$.condicionLaboral.$error">
                          <font-awesome-icon icon="exclamation-circle"></font-awesome-icon> Por favor, seleccione una opción.
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-header bg-light border-dark">
                      <h5 class="mb-0 text-center font-weight-bold text-dark">15. Nivel de escolaridad, ¿cuál fue el último nivel que aprobó en la escuela?</h5>
                    </div>
                    <div class="card-body">
                      <div class="form-group mb-0">
                        <div :class="{ 'is-invalid border border-danger p-2 rounded': v$.gradoMaximoEstudios.$error }">
                          <div class="row">
                            <div class="col-md-4">
                              <label class="custom-radio-option">
                                <input type="radio" value="KINDER" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Kinder o preescolar</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="PREPARATORIA" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Preparatoria o Bachillerato</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="TECNICA_SECUNDARIA" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Carrera técnica o comercial con secundaria completa</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="POSGRADO" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Posgrado (Maestría o Doctorado)</span>
                              </label>
                            </div>
                            <div class="col-md-4">
                              <label class="custom-radio-option">
                                <input type="radio" value="PRIMARIA" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Primaria</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="NORMAL_BASICA" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Normal Básica</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="TECNICA_PREPARATORIA" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Carrera técnica o comercial con preparatoria completa</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="NINGUNO" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Ninguno</span>
                              </label>
                            </div>
                            <div class="col-md-4">
                              <label class="custom-radio-option">
                                <input type="radio" value="SECUNDARIA" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Secundaria</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="CARRERA_TECNICA" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Carrera técnica o comercial</span>
                              </label>
                              <label class="custom-radio-option">
                                <input type="radio" value="LICENCIATURA" v-model="v$.gradoMaximoEstudios.$model">
                                <div class="radio-circle"></div><span class="radio-text">Profesional o Licenciatura</span>
                              </label>
                            </div>
                          </div>
                        </div>
                        <div class="invalid-feedback d-block mt-2" v-if="v$.gradoMaximoEstudios.$error">
                          <font-awesome-icon icon="exclamation-circle"></font-awesome-icon> Por favor, seleccione un nivel de escolaridad.
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      <div class="row m-0">
                        <div class="col-md-6 p-4 border-right border-dark d-flex flex-column justify-content-center">
                          <p class="font-weight-bold mb-3 text-center">
                            16. Verificación de condición de actividad, ¿cuál es su mayor fuente de ingreso?
                          </p>
                          <div class="form-group mb-0">
                            <input 
                              type="text" 
                              class="custom-input-wizard text-center" 
                              placeholder="Ej: Empleo formal, Negocio propio, etc."
                              :class="{ 'is-invalid border-danger': v$.mayorFuenteIngreso.$error }"
                              v-model="v$.mayorFuenteIngreso.$model"
                            />
                            <div class="invalid-feedback text-danger font-weight-bold text-center mt-1" v-if="v$.mayorFuenteIngreso.$error">
                              Especifique la fuente de ingreso.
                            </div>
                          </div>
                        </div>

                        <div class="col-md-6 p-4 d-flex flex-column justify-content-center text-center">
                          <p class="font-weight-bold mb-3">
                            17. Considerando su trabajo principal, ¿cuánto tiempo se dedica a trabajar?
                          </p>
                          <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.tiempoEmpleado.$error }">
                            <label class="custom-radio-option text-left">
                              <input type="radio" value="UNOS_MESES" v-model="v$.tiempoEmpleado.$model">
                              <div class="radio-circle"></div><span class="radio-text">¿Unos meses al año?</span>
                            </label>
                            <label class="custom-radio-option text-left">
                              <input type="radio" value="TODO_EL_ANO" v-model="v$.tiempoEmpleado.$model">
                              <div class="radio-circle"></div><span class="radio-text">¿Todo el año?</span>
                            </label>
                            <label class="custom-radio-option text-left">
                              <input type="radio" value="OTRO" v-model="v$.tiempoEmpleado.$model">
                              <div class="radio-circle"></div><span class="radio-text">Otro</span>
                            </label>
                          </div>
                          <div class="invalid-feedback d-block mt-2" v-if="v$.tiempoEmpleado.$error">
                            <font-awesome-icon icon="exclamation-circle"></font-awesome-icon> Seleccione una opción.
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      <div class="row m-0 text-center">
                        <div class="col-md-6 p-4 border-right border-dark d-flex flex-column justify-content-center">
                          <p class="font-weight-bold mb-3">18. En su trabajo principal, ¿cuánto gana al mes?</p>
                          <div class="input-with-prefix mx-auto" style="max-width: 250px;">
                            <span class="input-prefix">$</span>
                            <input 
                              type="text" 
                              class="custom-input-wizard font-weight-bold text-center" 
                              :class="{ 'is-invalid border-danger': v$.ingresoMensual.$error }"
                              v-model="v$.ingresoMensual.$model"
                              @input="limpiarInputNumerico($event, 'ingresoMensual')"
                            />
                            <div class="invalid-feedback text-danger font-weight-bold mt-1" v-if="v$.ingresoMensual.$error">
                              Monto inválido.
                            </div>
                          </div>
                        </div>

                        <div class="col-md-6 p-4 d-flex flex-column justify-content-center">
                          <p class="font-weight-bold mb-3">19. ¿Es persona jubilada o pensionada?</p>
                          <div class="form-group mb-3" :class="{ 'is-invalid border border-danger p-2 rounded': v$.esJubiladoPensionado.$error }">
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="true" v-model="v$.esJubiladoPensionado.$model">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">Sí</span>
                            </label>
                            <label class="custom-radio-option text-left">
                              <input type="radio" :value="false" v-model="v$.esJubiladoPensionado.$model" @change="limpiarPension()">
                              <div class="radio-circle"></div><span class="radio-text font-weight-bold">No</span>
                            </label>
                          </div>
                          <div class="invalid-feedback d-block mb-3" v-if="v$.esJubiladoPensionado.$error">Seleccione una opción.</div>

                          <div v-if="infoSocioeconomica.esJubiladoPensionado === true">
                            <label class="label-wizard">¿Cuánto dinero recibe al mes?</label>
                            <div class="input-with-prefix mx-auto mt-2" style="max-width: 250px;">
                              <span class="input-prefix">$</span>
                              <input 
                                type="text" 
                                class="custom-input-wizard font-weight-bold text-center" 
                                :class="{ 'is-invalid border-danger': v$.montoPension.$error }"
                                v-model="v$.montoPension.$model"
                                @input="limpiarInputNumerico($event, 'montoPension')"
                              />
                              <div class="invalid-feedback text-danger font-weight-bold mt-1" v-if="v$.montoPension.$error">
                                Monto inválido.
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </transition>
            </div>

            <div class="accordion-item mb-4 shadow-sm border">
              <div class="accordion-header" @click="toggleSeccion(4)">
                <h5 class="mb-0">4. Infraestructura de la Vivienda</h5>
                <font-awesome-icon :icon="seccionAbierta === 4 ? 'chevron-up' : 'chevron-down'"></font-awesome-icon>
              </div>
              <transition name="slide-fade">
                <div class="accordion-body p-3 bg-white" v-show="seccionAbierta === 4">
                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      
                      <div class="row m-0 text-center border-bottom border-dark">
                        <div class="col-md-6 p-4 border-right border-dark d-flex flex-column justify-content-center align-items-center">
                          <p class="font-weight-bold mb-3">
                            20. ¿Cuántos cuartos tiene en total su vivienda, contando la cocina? <br>
                            <span class="font-weight-normal text-muted" style="font-size: 0.9em;">(sin contar pasillos ni baños)</span>
                          </p>
                          <div class="d-flex align-items-center justify-content-center mt-3">
                            <input 
                              type="text" 
                              class="custom-input-wizard text-center mr-2" 
                              style="width: 80px;"
                              maxlength="2"
                              :class="{ 'is-invalid border-danger': v$.numeroHabitaciones.$error }"
                              v-model="v$.numeroHabitaciones.$model"
                              @input="limpiarInputNumerico($event, 'numeroHabitaciones')"
                            />
                            <span class="font-weight-bold text-muted">cuartos</span>
                          </div>
                          <div class="invalid-feedback text-danger font-weight-bold d-block mt-2" v-if="v$.numeroHabitaciones.$error">
                            Debe ser un número entre 0 y 20.
                          </div>
                        </div>

                        <div class="col-md-6 p-4 d-flex flex-column justify-content-center align-items-center">
                          <p class="font-weight-bold mb-3">
                            21. ¿Cuántos cuartos usan para dormir?
                          </p>
                          <div class="d-flex align-items-center justify-content-center mt-3">
                            <input 
                              type="text" 
                              class="custom-input-wizard text-center mr-2" 
                              style="width: 80px;"
                              maxlength="2"
                              :class="{ 'is-invalid border-danger': v$.cuartosDormir.$error }"
                              v-model="v$.cuartosDormir.$model"
                              @input="limpiarInputNumerico($event, 'cuartosDormir')"
                            />
                            <span class="font-weight-bold text-muted">cuartos</span>
                          </div>
                          <div class="invalid-feedback text-danger font-weight-bold d-block mt-2" v-if="v$.cuartosDormir.$error">
                            Debe ser un número entre 0 y 20.
                          </div>
                        </div>
                      </div>

                      <div class="row m-0 p-4 text-center">
                        <div class="col-12">
                          <p class="font-weight-bold mb-4">
                            22. ¿De qué material es la mayor parte del piso de su vivienda? <br>
                          </p>
                          <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-3 rounded': v$.materialVivienda.$error }">
                            <div class="row justify-content-center">
                              <div class="col-md-4">
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="TIERRA" v-model="v$.materialVivienda.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Tierra</span>
                                </label>
                              </div>
                              <div class="col-md-4">
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="CEMENTO" v-model="v$.materialVivienda.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Cemento</span>
                                </label>
                              </div>
                              <div class="col-md-4">
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="MOSAICO_MADERA_OTRO" v-model="v$.materialVivienda.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Mosaico, madera u otro recubrimiento</span>
                                </label>
                              </div>
                            </div>
                          </div>
                          <div class="invalid-feedback d-block mt-2" v-if="v$.materialVivienda.$error">
                            Seleccione un material para el piso.
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      <div class="row m-0">
                        <div class="col-md-6 p-0 border-right border-dark">
                          <div class="bg-light border-bottom border-dark p-3 font-weight-bold text-center">
                            23. ¿De qué material es la mayor parte del techo de su vivienda?
                          </div>
                          <div class="p-4">
                            <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-3 rounded': v$.materialTecho.$error }">
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="DESECHO" v-model="v$.materialTecho.$model">
                                <div class="radio-circle"></div><span class="radio-text">Material de desecho (cartón, hule, tela, llanta)</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="LAMINA_CARTON" v-model="v$.materialTecho.$model">
                                <div class="radio-circle"></div><span class="radio-text">Lámina de cartón</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="LAMINA_METALICA" v-model="v$.materialTecho.$model">
                                <div class="radio-circle"></div><span class="radio-text">Lámina metálica / asbesto</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="PALMA_PAJA" v-model="v$.materialTecho.$model">
                                <div class="radio-circle"></div><span class="radio-text">Palma o paja</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="LOSA_CONCRETO" v-model="v$.materialTecho.$model">
                                <div class="radio-circle"></div><span class="radio-text">Losa de concreto o viguetas con bovedilla</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="MADERA" v-model="v$.materialTecho.$model">
                                <div class="radio-circle"></div><span class="radio-text">Madera o tejamanil</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="TERRADO_VIGUERIA" v-model="v$.materialTecho.$model">
                                <div class="radio-circle"></div><span class="radio-text">Terrado con viguería</span>
                              </label>
                              <label class="custom-radio-option text-left mb-0">
                                <input type="radio" value="TEJA" v-model="v$.materialTecho.$model">
                                <div class="radio-circle"></div><span class="radio-text">Teja</span>
                              </label>
                            </div>
                            <div class="invalid-feedback d-block mt-2" v-if="v$.materialTecho.$error">
                              Seleccione el material del techo.
                            </div>
                          </div>
                        </div>

                        <div class="col-md-6 p-0">
                          <div class="bg-light border-bottom border-dark p-3 font-weight-bold text-center">
                            24. ¿De qué material es la mayor parte de las paredes o muros de su vivienda?
                          </div>
                          <div class="p-4">
                            <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-3 rounded': v$.materialParedes.$error }">
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="DESECHO" v-model="v$.materialParedes.$model">
                                <div class="radio-circle"></div><span class="radio-text">Material de desecho (cartón, hule, tela, llanta)</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="LAMINA_CARTON" v-model="v$.materialParedes.$model">
                                <div class="radio-circle"></div><span class="radio-text">Lámina de cartón</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="LAMINA_METALICA" v-model="v$.materialParedes.$model">
                                <div class="radio-circle"></div><span class="radio-text">Lámina metálica / asbesto</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="CARRIZO_BAMBU_PALMA" v-model="v$.materialParedes.$model">
                                <div class="radio-circle"></div><span class="radio-text">Carrizo, bambú o palma</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="EMBARRO_BAJAREQUE" v-model="v$.materialParedes.$model">
                                <div class="radio-circle"></div><span class="radio-text">Embarro o bajareque</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="ADOBE" v-model="v$.materialParedes.$model">
                                <div class="radio-circle"></div><span class="radio-text">Adobe</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="MADERA" v-model="v$.materialParedes.$model">
                                <div class="radio-circle"></div><span class="radio-text">Madera</span>
                              </label>
                              <label class="custom-radio-option text-left mb-0">
                                <input type="radio" value="TABIQUE_LADRILLO_BLOCK" v-model="v$.materialParedes.$model">
                                <div class="radio-circle"></div><span class="radio-text">Tabique, ladrillo, block o concreto</span>
                              </label>
                            </div>
                            <div class="invalid-feedback d-block mt-2" v-if="v$.materialParedes.$error">
                              Seleccione el material de las paredes.
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      <div class="row m-0">
                        <div class="col-md-6 p-0 border-right border-dark">
                          <div class="bg-light border-bottom border-dark p-3 font-weight-bold text-center">
                            25. ¿Qué tipo de baño o escusado tiene en su vivienda?
                          </div>
                          <div class="p-4">
                            <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-3 rounded': v$.tipoBano.$error }">
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="NO_TIENE" v-model="v$.tipoBano.$model">
                                <div class="radio-circle"></div><span class="radio-text">No tiene</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="POZO_HOYO_NEGRO" v-model="v$.tipoBano.$model">
                                <div class="radio-circle"></div><span class="radio-text">Pozo u hoyo negro</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="AGUA_CON_CUBETA" v-model="v$.tipoBano.$model">
                                <div class="radio-circle"></div><span class="radio-text">Le echan agua con cubeta</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="LETRINA_SECA_HUMEDA" v-model="v$.tipoBano.$model">
                                <div class="radio-circle"></div><span class="radio-text">Sin admisión de agua (letrina seca / húmeda)</span>
                              </label>
                              <label class="custom-radio-option text-left mb-0">
                                <input type="radio" value="CONEXION_DESCARGA_DIRECTA" v-model="v$.tipoBano.$model">
                                <div class="radio-circle"></div><span class="radio-text">Con conexión y con descarga directa de agua</span>
                              </label>
                            </div>
                            <div class="invalid-feedback d-block mt-2" v-if="v$.tipoBano.$error">
                              Seleccione el tipo de baño.
                            </div>
                          </div>
                        </div>

                        <div class="col-md-6 p-0">
                          <div class="bg-light border-bottom border-dark p-3 font-weight-bold text-center">
                            26. ¿Qué tipo de drenaje o desagüe de aguas tiene en su vivienda?
                          </div>
                          <div class="p-4">
                            <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-3 rounded': v$.tipoDrenaje.$error }">
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="RED_PUBLICA" v-model="v$.tipoDrenaje.$model">
                                <div class="radio-circle"></div><span class="radio-text">A la red pública</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="FOSA_SEPTICA" v-model="v$.tipoDrenaje.$model">
                                <div class="radio-circle"></div><span class="radio-text">A una fosa séptica</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="NO_TIENE" v-model="v$.tipoDrenaje.$model">
                                <div class="radio-circle"></div><span class="radio-text">No tiene desagüe</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="TUBERIA_GRIETA_BARRANCA" v-model="v$.tipoDrenaje.$model">
                                <div class="radio-circle"></div><span class="radio-text">A una tubería que da a una grieta o barranca</span>
                              </label>
                              <label class="custom-radio-option text-left mb-0">
                                <input type="radio" value="TUBERIA_RIO_LAGO_MAR" v-model="v$.tipoDrenaje.$model">
                                <div class="radio-circle"></div><span class="radio-text">A una tubería que da a un río, lago o mar</span>
                              </label>
                            </div>
                            <div class="invalid-feedback d-block mt-2" v-if="v$.tipoDrenaje.$error">
                              Seleccione el tipo de drenaje.
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      <div class="row m-0">
                        <div class="col-md-6 p-0 border-right border-dark">
                          <div class="bg-light border-bottom border-dark p-3 font-weight-bold text-center">
                            27. ¿En su vivienda, qué hacen con la basura?
                          </div>
                          <div class="p-4">
                            <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-3 rounded': v$.manejoBasura.$error }">
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="QUEMAN" v-model="v$.manejoBasura.$model">
                                <div class="radio-circle"></div><span class="radio-text">La queman</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="RIO_LAGO_BARRANCA" v-model="v$.manejoBasura.$model">
                                <div class="radio-circle"></div><span class="radio-text">La tiran al río, lago, mar o barranca</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="TERRENO_BALDIO_CALLE" v-model="v$.manejoBasura.$model">
                                <div class="radio-circle"></div><span class="radio-text">La tiran en un terreno baldío o calle</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="BASURERO_PUBLICO" v-model="v$.manejoBasura.$model">
                                <div class="radio-circle"></div><span class="radio-text">La tiran en el basurero público</span>
                              </label>
                              <label class="custom-radio-option text-left mb-0">
                                <input type="radio" value="CONTENEDOR_CAMION" v-model="v$.manejoBasura.$model">
                                <div class="radio-circle"></div><span class="radio-text">La tiran en un contenedor, la recoge un camión / carro de basura</span>
                              </label>
                            </div>
                            <div class="invalid-feedback d-block mt-2" v-if="v$.manejoBasura.$error">
                              Seleccione una opción.
                            </div>
                          </div>
                        </div>

                        <div class="col-md-6 p-0">
                          <div class="bg-light border-bottom border-dark p-3 font-weight-bold text-center">
                            28. En su vivienda, ¿la luz eléctrica la obtienen?
                          </div>
                          <div class="p-4">
                            <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-3 rounded': v$.fuenteLuzElectrica.$error }">
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="SERVICIO_PUBLICO" v-model="v$.fuenteLuzElectrica.$model">
                                <div class="radio-circle"></div><span class="radio-text">Del servicio público</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="PLANTA_PARTICULAR" v-model="v$.fuenteLuzElectrica.$model">
                                <div class="radio-circle"></div><span class="radio-text">De una planta particular</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="PANEL_SOLAR" v-model="v$.fuenteLuzElectrica.$model">
                                <div class="radio-circle"></div><span class="radio-text">De panel solar</span>
                              </label>
                              <label class="custom-radio-option text-left">
                                <input type="radio" value="OTRA_FUENTE" v-model="v$.fuenteLuzElectrica.$model">
                                <div class="radio-circle"></div><span class="radio-text">De otra fuente</span>
                              </label>
                              <label class="custom-radio-option text-left mb-0">
                                <input type="radio" value="NO_TIENE" v-model="v$.fuenteLuzElectrica.$model">
                                <div class="radio-circle"></div><span class="radio-text">No tienen luz eléctrica</span>
                              </label>
                            </div>
                            <div class="invalid-feedback d-block mt-2" v-if="v$.fuenteLuzElectrica.$error">
                              Seleccione una opción.
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="card mb-4 border-dark">
                    <div class="card-body p-0">
                      <div class="row m-0 p-4 border-bottom border-dark text-center">
                        <div class="col-12">
                          <div class="bg-light border border-dark p-3 mb-4 font-weight-bold">
                            29. ¿Cuál es el combustible que más usan para cocinar?
                          </div>
                          <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-3 rounded': v$.combustibleCocina.$error }">
                            <div class="row justify-content-center">
                              <div class="col-md-4">
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="ELECTRICIDAD" v-model="v$.combustibleCocina.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Electricidad</span>
                                </label>
                              </div>
                              <div class="col-md-4">
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="GAS_LP" v-model="v$.combustibleCocina.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Gas L.P.</span>
                                </label>
                              </div>
                              <div class="col-md-4">
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="LENA_CARBON" v-model="v$.combustibleCocina.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Leña o carbón</span>
                                </label>
                              </div>
                            </div>
                          </div>
                          <div class="invalid-feedback d-block mt-2" v-if="v$.combustibleCocina.$error">
                            Seleccione el tipo de combustible.
                          </div>
                        </div>
                      </div>

                      <div class="row m-0 p-4 text-center">
                        <div class="col-12">
                          <div class="bg-light border border-dark p-3 mb-4 font-weight-bold">
                            30. La vivienda que habita, ¿es?
                          </div>
                          <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-3 rounded': v$.tenenciaVivienda.$error }">
                            <div class="row">
                              <div class="col-md-4 px-md-4">
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="PROPIA_PAGADA" v-model="v$.tenenciaVivienda.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Propia y está pagada</span>
                                </label>
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="PRESTADA_CUIDANDO" v-model="v$.tenenciaVivienda.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Prestada o la está cuidando</span>
                                </label>
                              </div>
                              <div class="col-md-4 px-md-4">
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="PROPIA_PAGANDO" v-model="v$.tenenciaVivienda.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Propia y la está pagando</span>
                                </label>
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="RENTADA" v-model="v$.tenenciaVivienda.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Rentada o alquilada</span>
                                </label>
                              </div>
                              <div class="col-md-4 px-md-4">
                                <label class="custom-radio-option text-left">
                                  <input type="radio" value="PROPIA_HIPOTECADA" v-model="v$.tenenciaVivienda.$model">
                                  <div class="radio-circle"></div><span class="radio-text">Propia y está hipotecada</span>
                                </label>
                              </div>
                            </div>
                          </div>
                          <div class="invalid-feedback d-block mt-2" v-if="v$.tenenciaVivienda.$error">
                            Seleccione la situación de la vivienda.
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  
                </div>
              </transition>
            </div>

          </form>
        </div> 

        <div class="custom-modal-footer" v-if="mostrarCuestionario">
          <span class="text-muted font-weight-bold" style="font-size: 0.85rem;">Puedes cerrar esta ventana, el borrador se guardará.</span>
<button 
  type="button" 
  class="btn btn-custom px-5 py-2 font-weight-bold"
  style="border-radius: 8px;"
  @click="save" 
  :disabled="isSaving"
>
  <font-awesome-icon icon="save"></font-awesome-icon> 
  <span v-if="isSaving"> Guardando...</span>
  <span v-else> Finalizar y Guardar</span>
</button>
        </div>

      </div>
    </div>
  </transition>
</template>

<script lang="ts" src="./info-socioeconomica-wizard.component.ts"></script>

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

.custom-modal-box { 
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
.accordion-header h5 { font-size: 1.05rem; font-weight: 700; color: #1e293b; margin: 0; }
.accordion-body { border-top: 1px solid #e2e8f0; }

/* ANIMACIÓN ACORDEÓN */
.slide-fade-enter-active { transition: all 0.3s ease-out; }
.slide-fade-leave-active { transition: all 0.2s cubic-bezier(1, 0.5, 0.8, 1); }
.slide-fade-enter-from, .slide-fade-leave-to { transform: translateY(-10px); opacity: 0; }

/* ========================================= */
/* 🔥 RADIO BUTTONS PREMIUM TIPO TARJETA 🔥  */
/* ========================================= */

.custom-radio-option {
  display: flex;
  align-items: center;
  padding: 12px 18px;
  margin-bottom: 10px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  background-color: #ffffff;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.custom-radio-option:hover {
  background-color: #f8fafc;
  border-color: #cbd5e1;
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0,0,0,0.03);
}

.custom-radio-option input[type="radio"] {
  position: absolute;
  opacity: 0;
  cursor: pointer;
}

.radio-circle {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  border: 2px solid #cbd5e1;
  margin-right: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.radio-text {
  font-size: 0.95rem;
  color: #475569;
  font-weight: 500;
  transition: all 0.3s ease;
}

/* ESTADO SELECCIONADO (CHECKED) */
.custom-radio-option input[type="radio"]:checked ~ .radio-circle {
  border-color: #5c1830;
  background-color: #5c1830;
}

.custom-radio-option input[type="radio"]:checked ~ .radio-circle::after {
  content: '';
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: white;
}

.custom-radio-option input[type="radio"]:checked ~ .radio-text {
  color: #5c1830;
  font-weight: 700;
}

.custom-radio-option:has(input[type="radio"]:checked) {
  border-color: #5c1830;
  background-color: #fdf2f5; 
  box-shadow: 0 4px 15px rgba(92, 24, 48, 0.1);
}

/* ========================================= */
/* 🔥 INPUTS DE TEXTO PREMIUM 🔥             */
/* ========================================= */
.custom-input-wizard {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 0.95rem;
  color: #1e293b;
  font-weight: 600;
  background-color: #f8fafc;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.custom-input-wizard:focus {
  outline: none;
  border-color: #611232;
  background-color: #ffffff;
  box-shadow: 0 0 0 4px rgba(97, 18, 50, 0.15);
}

.custom-input-wizard[readonly] {
  background-color: #e2e8f0;
  color: #64748b;
  border-color: #cbd5e1;
  cursor: not-allowed;
}

.input-with-prefix {
  position: relative;
  display: flex;
  align-items: center;
}

.input-prefix {
  position: absolute;
  left: 16px;
  font-size: 1.2rem;
  font-weight: bold;
  color: #611232;
  pointer-events: none;
}

.input-with-prefix .custom-input-wizard {
  padding-left: 35px;
}

.label-wizard {
  font-size: 0.95rem;
  color: #475569;
  font-weight: 700;
  margin-bottom: 8px;
  display: block;
}

/* BOTÓN GUINDA GENERAL */
.btn-custom {
  background-color: #611232;
  border-color: #611232;
  color: white;
  transition: all 0.3s ease;
}
.btn-custom:hover {
  background-color: #4a0d27;
  border-color: #4a0d27;
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}
.btn-custom:active {
  transform: scale(0.97); 
}

/* ========================================= */
/* 📱 DISEÑO RESPONSIVO (MÓVILES Y TABLETS)  */
/* ========================================= */
@media (max-width: 768px) {
  /* 1. Expandir el modal a casi toda la pantalla */
  .custom-modal-box.wizard-box {
    width: 98%;
    max-height: 95vh;
  }
  
  /* 2. Reducir márgenes internos para ganar espacio */
  .custom-modal-header, .custom-modal-body, .custom-modal-footer {
    padding: 1rem;
  }

  /* 3. Hacer las tarjetas de radio más grandes para el dedo */
  .custom-radio-option {
    padding: 16px 20px; 
    margin-bottom: 12px;
  }
  
  .radio-text {
    font-size: 1rem; 
  }

  /* 4. Arreglar bordes extraños de Bootstrap al apilarse las columnas */
  .border-right {
    border-right: none !important; /* Quita la raya lateral fea */
    border-bottom: 1px solid #343a40 !important; /* Pone una raya divisoria debajo */
  }

  /* 5. Inputs un poco más altos */
  .custom-input-wizard {
    font-size: 1rem;
    padding: 14px 16px;
  }

  /* 6. Apilar el footer (Texto de borrador arriba, botón gigante abajo) */
  .custom-modal-footer {
    flex-direction: column;
    gap: 12px;
    text-align: center;
  }
  
  .custom-modal-footer .btn {
    width: 100%;
    justify-content: center;
  }
}
</style>