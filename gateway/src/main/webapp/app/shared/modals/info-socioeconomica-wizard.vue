<template>
  <transition name="modal-fade">
    <div v-if="visible" class="custom-modal-overlay">
      <div class="custom-modal-box wizard-box">
        
        <div class="custom-modal-header">
          <h3 class="title-text">
            <font-awesome-icon icon="clipboard-list" class="mr-2"></font-awesome-icon> Estudio Socioeconómico
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
                  <label class="font-weight-bold">Nombre completo:</label>
                  <input type="text" class="form-control-plaintext border-bottom border-dark px-2" :value="nombreCompleto" readonly />
                </div>
                <div class="col-12 form-group">
                  <label class="font-weight-bold">CURP:</label>
                  <input type="text" class="form-control-plaintext border-bottom border-dark px-2" :value="pacienteEncontrado?.curp" readonly />
                </div>
                <div class="col-md-4 form-group">
                  <label class="font-weight-bold">Edad:</label>
                  <input type="text" class="form-control-plaintext border-bottom border-dark px-2" :value="edadCalculada" readonly />
                </div>
                <div class="col-md-4 form-group">
                  <label class="font-weight-bold">Sexo:</label>
                  <input type="text" class="form-control-plaintext border-bottom border-dark px-2" :value="formatoSexo" readonly />
                </div>
                <div class="col-md-4 form-group">
                  <label class="font-weight-bold">Estado Civil:</label>
                  <input type="text" class="form-control-plaintext border-bottom border-dark px-2" :value="pacienteEncontrado?.estadoCivil" readonly />
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
                  <div class="form-check mb-2">
                    <input class="form-check-input" type="radio" id="doc_credencial" value="CREDENCIAL_VOTAR" v-model="v$.documentoIdentidad.$model">
                    <label class="form-check-label" for="doc_credencial">Credencial para votar vigente</label>
                  </div>
                  <div class="form-check mb-2">
                    <input class="form-check-input" type="radio" id="doc_cartilla" value="CARTILLA_MILITAR" v-model="v$.documentoIdentidad.$model">
                    <label class="form-check-label" for="doc_cartilla">Cartilla del Servicio Militar Nacional</label>
                  </div>
                  <div class="form-check mb-2">
                    <input class="form-check-input" type="radio" id="doc_pasaporte" value="PASAPORTE" v-model="v$.documentoIdentidad.$model">
                    <label class="form-check-label" for="doc_pasaporte">Pasaporte vigente</label>
                  </div>
                  <div class="form-check mb-2">
                    <input class="form-check-input" type="radio" id="doc_inapam" value="INAPAM" v-model="v$.documentoIdentidad.$model">
                    <label class="form-check-label" for="doc_inapam">Credencial del Instituto Nacional de las Personas Adultas Mayores (INAPAM)</label>
                  </div>
                  <div class="form-check mb-2">
                    <input class="form-check-input" type="radio" id="doc_cedula" value="CEDULA_PROFESIONAL" v-model="v$.documentoIdentidad.$model">
                    <label class="form-check-label" for="doc_cedula">Cédula profesional</label>
                  </div>
                  <div class="form-check mb-2">
                    <input class="form-check-input" type="radio" id="doc_constancia" value="CONSTANCIA_IDENTIDAD" v-model="v$.documentoIdentidad.$model">
                    <label class="form-check-label" for="doc_constancia">Constancia de Identidad</label>
                  </div>
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
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="viv_casa" value="CASA_INDEPENDIENTE" v-model="v$.tipoVivienda.$model">
                        <label class="form-check-label" for="viv_casa">Casa independiente</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="viv_anexo" value="ANEXO_CASA" v-model="v$.tipoVivienda.$model">
                        <label class="form-check-label" for="viv_anexo">Anexo a casa</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="viv_movil" value="VIVIENDA_MOVIL" v-model="v$.tipoVivienda.$model">
                        <label class="form-check-label" for="viv_movil">Vivienda móvil</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="viv_vecindad" value="CUARTO_VECINDAD" v-model="v$.tipoVivienda.$model">
                        <label class="form-check-label" for="viv_vecindad">Vivienda o cuarto en vecindad</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="viv_construccion" value="EN_CONSTRUCCION" v-model="v$.tipoVivienda.$model">
                        <label class="form-check-label" for="viv_construccion">Vivienda en construcción no habitada</label>
                      </div>
                    </div>
                    <div class="col-md-6">
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="viv_asilo" value="ASILO_ORFANATO_CONVENTO" v-model="v$.tipoVivienda.$model">
                        <label class="form-check-label" for="viv_asilo">Asilo, orfanato o convento</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="viv_refugio" value="REFUGIO" v-model="v$.tipoVivienda.$model">
                        <label class="form-check-label" for="viv_refugio">Refugio</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="viv_azotea" value="CUARTO_AZOTEA" v-model="v$.tipoVivienda.$model">
                        <label class="form-check-label" for="viv_azotea">Vivienda o cuarto en azotea</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="viv_departamento" value="DEPARTAMENTO_EDIFICIO" v-model="v$.tipoVivienda.$model">
                        <label class="form-check-label" for="viv_departamento">Departamento en edificio / Unidad habitacional</label>
                      </div>
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
              <h5 class="mb-0 text-center font-weight-bold text-dark">4. ¿Cuántas personas forman parte de su familia, habitan en la misma
              vivienda contando a los niños pequeños, y adultos mayores?</h5>
            </div>
            <div class="card-body">
              <div class="row text-center">
                <div class="col-md-4 form-group">
                  <label class="font-weight-bold">Hombres:</label>
                  <input
                    type="text"
                    class="form-control text-center"
                    maxlength="2"
                    :class="{ 'is-invalid': v$.numHombres.$error }"
                    v-model="v$.numHombres.$model"
                    @input="limpiarInputNumerico($event, 'numHombres')"                  />
                  <div class="invalid-feedback">Debe ser un número entre 0 y 20.</div>
                </div>
                <div class="col-md-4 form-group">
                  <label class="font-weight-bold">Mujeres:</label>
                  <input
                    type="text"
                    class="form-control text-center"
                    maxlength="2"
                    :class="{ 'is-invalid': v$.numMujeres.$error }"
                    v-model="v$.numMujeres.$model"
                    @input="limpiarInputNumerico($event, 'numMujeres')"                  />
                  <div class="invalid-feedback">Debe ser un número entre 0 y 20.</div>
                </div>
                <div class="col-md-4 form-group">
                  <label class="font-weight-bold text-primary">Total:</label>
                  <input
                    type="text"
                    class="form-control-plaintext border-bottom border-primary text-center font-weight-bold"
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
                
                <div class="col-md-4 p-3 border-right border-dark text-center d-flex flex-column justify-content-center">
                  <p class="font-weight-bold mb-3">5. ¿Se considera la persona jefa del hogar?</p>
                  
                  <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-1 rounded': v$.jefeHogar.$error }">
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="jefe_si" :value="true" v-model="v$.jefeHogar.$model">
                      <label class="form-check-label font-weight-bold" for="jefe_si">Sí</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="jefe_no" :value="false" v-model="v$.jefeHogar.$model">
                      <label class="form-check-label font-weight-bold" for="jefe_no">No</label>
                    </div>
                  </div>
                  <div class="invalid-feedback d-block mt-1" v-if="v$.jefeHogar.$error">Seleccione una opción.</div>
                </div>

                <div class="col-md-4 p-3 border-right border-dark">
                  <p class="font-weight-bold mb-2 text-center">6. Teléfono de contacto</p>
                  <p class="text-center mb-2">¿Tiene teléfono?</p>
                  
                  <div class="form-group mb-3 text-center" :class="{ 'is-invalid border border-danger p-1 rounded': v$.tieneTelefono.$error }">
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="tel_si" :value="true" v-model="v$.tieneTelefono.$model">
                      <label class="form-check-label font-weight-bold">Sí</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="tel_no" :value="false" v-model="v$.tieneTelefono.$model" @change="limpiarTelefonos()">
                      <label class="form-check-label font-weight-bold">No</label>
                    </div>
                  </div>
                  <div class="invalid-feedback d-block text-center mb-2" v-if="v$.tieneTelefono.$error">Seleccione una opción.</div>

                  <div v-if="infoSocioeconomica.tieneTelefono === true">
                    <div class="form-group mb-2">
                      <label class="font-weight-bold mb-0" style="font-size: 0.9em;">Fijo:</label>
                      <input type="text" class="form-control form-control-sm border-bottom border-dark rounded-0 px-2" maxlength="10" :class="{ 'is-invalid': v$.numeroFijo.$error }" v-model="v$.numeroFijo.$model" @input="limpiarInputNumerico($event, 'numeroFijo')" />
                      <div class="invalid-feedback" v-if="v$.numeroFijo.$error">10 dígitos.</div>
                    </div>
                    <div class="form-group mb-2">
                      <label class="font-weight-bold mb-0" style="font-size: 0.9em;">Recados:</label>
                      <input type="text" class="form-control form-control-sm border-bottom border-dark rounded-0 px-2" maxlength="10" :class="{ 'is-invalid': v$.numeroRecados.$error }" v-model="v$.numeroRecados.$model" @input="limpiarInputNumerico($event, 'numeroRecados')" />
                      <div class="invalid-feedback" v-if="v$.numeroRecados.$error">10 dígitos.</div>
                    </div>
                    <div class="form-group mb-0">
                      <label class="font-weight-bold mb-0" style="font-size: 0.9em;">Celular:</label>
                      <input type="text" class="form-control form-control-sm border-bottom border-dark rounded-0 px-2" maxlength="10" :class="{ 'is-invalid': v$.numeroCelular.$error }" v-model="v$.numeroCelular.$model" @input="limpiarInputNumerico($event, 'numeroCelular')" />
                      <div class="invalid-feedback" v-if="v$.numeroCelular.$error">10 dígitos.</div>
                    </div>
                  </div>
                </div>

                <div class="col-md-4 p-3">
                  <p class="font-weight-bold mb-2 text-center">7. ¿Actualmente padece alguna enfermedad o discapacidad?</p>
                  
                  <div class="form-group mb-3 text-center" :class="{ 'is-invalid border border-danger p-1 rounded': v$.padeceEnfermedad.$error }">
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="enf_si" :value="true" v-model="v$.padeceEnfermedad.$model">
                      <label class="form-check-label font-weight-bold" for="enf_si">Sí</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="enf_no" :value="false" v-model="v$.padeceEnfermedad.$model" @change="limpiarEnfermedad()">
                      <label class="form-check-label font-weight-bold" for="enf_no">No</label>
                    </div>
                  </div>
                  <div class="invalid-feedback d-block text-center mb-2" v-if="v$.padeceEnfermedad.$error">Seleccione una opción.</div>

                  <div v-if="infoSocioeconomica.padeceEnfermedad === true">
                    <label class="font-weight-bold mb-0">¿Cuál?:</label>
                    <input 
                      type="text" 
                      class="form-control border-bottom border-dark rounded-0 px-2" 
                      :class="{ 'is-invalid': v$.enfermedadCual.$error }"
                      v-model="v$.enfermedadCual.$model"
                    />
                    <div class="invalid-feedback" v-if="v$.enfermedadCual.$error">Especifique la enfermedad.</div>
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
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="inst_insabi" value="INSABI" v-model="v$.institucionMedica.$model">
                        <label class="form-check-label" for="inst_insabi">Instituto de la Salud para el Bienestar</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="inst_pemex" value="PEMEX_DEFENSA_MARINA" v-model="v$.institucionMedica.$model">
                        <label class="form-check-label" for="inst_pemex">PEMEX, Defensa o Marina</label>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="inst_imss" value="IMSS" v-model="v$.institucionMedica.$model">
                        <label class="form-check-label" for="inst_imss">IMSS</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="inst_privado" value="CLINICA_PRIVADA" v-model="v$.institucionMedica.$model">
                        <label class="form-check-label" for="inst_privado">Clínica u hospital privado</label>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="inst_issste" value="ISSSTE" v-model="v$.institucionMedica.$model">
                        <label class="form-check-label" for="inst_issste">ISSSTE</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="inst_ninguna" value="NINGUNA" v-model="v$.institucionMedica.$model">
                        <label class="form-check-label" for="inst_ninguna">A ninguna</label>
                      </div>
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
                
                <div class="col-md-4 p-3 border-right border-dark">
                  <p class="font-weight-bold mb-2">9. ¿Habla alguna lengua indígena?</p>
                  
                  <div class="form-group mb-3 text-center" :class="{ 'is-invalid border border-danger p-1 rounded': v$.hablaLenguaIndigena.$error }">
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="lengua_si" :value="true" v-model="v$.hablaLenguaIndigena.$model">
                      <label class="form-check-label font-weight-bold" for="lengua_si">Sí</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="lengua_no" :value="false" v-model="v$.hablaLenguaIndigena.$model" @change="limpiarLengua()">
                      <label class="form-check-label font-weight-bold" for="lengua_no">No</label>
                    </div>
                  </div>
                  <div class="invalid-feedback d-block text-center mb-2" v-if="v$.hablaLenguaIndigena.$error">Seleccione una opción.</div>

                  <div v-if="infoSocioeconomica.hablaLenguaIndigena === true">
                    <label class="font-weight-bold mb-0">¿Cuál es?</label>
                    <input 
                      type="text" 
                      class="form-control border-bottom border-dark rounded-0 px-2" 
                      :class="{ 'is-invalid': v$.lenguaIndigenaCual.$error }"
                      v-model="v$.lenguaIndigenaCual.$model" 
                    />
                    <div class="invalid-feedback" v-if="v$.lenguaIndigenaCual.$error">Especifique la lengua.</div>
                  </div>
                </div>

                <div class="col-md-4 p-3 border-right border-dark text-center d-flex flex-column justify-content-center">
                  <p class="font-weight-bold mb-3">10. ¿También habla español?</p>
                  
                  <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-1 rounded': v$.hablaEspanol.$error }">
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="espanol_si" :value="true" v-model="v$.hablaEspanol.$model">
                      <label class="form-check-label font-weight-bold" for="espanol_si">Sí</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="espanol_no" :value="false" v-model="v$.hablaEspanol.$model">
                      <label class="form-check-label font-weight-bold" for="espanol_no">No</label>
                    </div>
                  </div>
                  <div class="invalid-feedback d-block mt-1" v-if="v$.hablaEspanol.$error">Seleccione una opción.</div>
                </div>

                <div class="col-md-4 p-3">
                  <p class="font-weight-bold mb-2">11. De acuerdo con su cultura, ¿se considera indígena?</p>
                  
                  <div class="form-group mb-3 text-center" :class="{ 'is-invalid border border-danger p-1 rounded': v$.consideraIndigena.$error }">
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="indigena_si" :value="true" v-model="v$.consideraIndigena.$model">
                      <label class="form-check-label font-weight-bold" for="indigena_si">Sí</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="indigena_no" :value="false" v-model="v$.consideraIndigena.$model" @change="limpiarGrupoIndigena()">
                      <label class="form-check-label font-weight-bold" for="indigena_no">No</label>
                    </div>
                  </div>
                  <div class="invalid-feedback d-block text-center mb-2" v-if="v$.consideraIndigena.$error">Seleccione una opción.</div>

                  <div v-if="infoSocioeconomica.consideraIndigena === true">
                    <label class="font-weight-bold mb-0">¿A qué grupo pertenece?</label>
                    <input 
                      type="text" 
                      class="form-control border-bottom border-dark rounded-0 px-2" 
                      :class="{ 'is-invalid': v$.grupoIndigenaCual.$error }"
                      v-model="v$.grupoIndigenaCual.$model" 
                    />
                    <div class="invalid-feedback" v-if="v$.grupoIndigenaCual.$error">Especifique el grupo.</div>
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
                  <div class="p-3 d-flex flex-column justify-content-center">
                    <p class="font-weight-bold mb-3">12. ¿Sabe leer y escribir un recado?</p>
                    
                    <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-1 rounded': v$.sabeLeerEscribir.$error }">
                      <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="leer_si" :value="true" v-model="v$.sabeLeerEscribir.$model">
                        <label class="form-check-label font-weight-bold" for="leer_si">Sí</label>
                      </div>
                      <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="leer_no" :value="false" v-model="v$.sabeLeerEscribir.$model">
                        <label class="form-check-label font-weight-bold" for="leer_no">No</label>
                      </div>
                    </div>
                    <div class="invalid-feedback d-block mt-1" v-if="v$.sabeLeerEscribir.$error">Seleccione una opción.</div>
                  </div>
                </div>

                <div class="col-md-6 p-0">
                  <div class="bg-light border-bottom border-dark p-2 font-weight-bold">
                    Asistencia a la escuela
                  </div>
                  <div class="p-3 d-flex flex-column justify-content-center">
                    <p class="font-weight-bold mb-3">13. ¿Actualmente asiste a la escuela?</p>
                    
                    <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-1 rounded': v$.estudia.$error }">
                      <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="estudia_si" :value="true" v-model="v$.estudia.$model">
                        <label class="form-check-label font-weight-bold" for="estudia_si">Sí</label>
                      </div>
                      <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" id="estudia_no" :value="false" v-model="v$.estudia.$model">
                        <label class="form-check-label font-weight-bold" for="estudia_no">No</label>
                      </div>
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
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="act_trabaja" value="TRABAJA" v-model="v$.condicionLaboral.$model">
                        <label class="form-check-label" for="act_trabaja">Trabaja</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="act_estudia_trabaja" value="ESTUDIA_TRABAJA" v-model="v$.condicionLaboral.$model">
                        <label class="form-check-label" for="act_estudia_trabaja">Estudia y trabaja</label>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="act_busca" value="BUSCA_TRABAJO" v-model="v$.condicionLaboral.$model">
                        <label class="form-check-label" for="act_busca">Busca trabajo</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="act_quehaceres" value="QUEHACERES" v-model="v$.condicionLaboral.$model">
                        <label class="form-check-label" for="act_quehaceres">Realiza quehaceres domésticos</label>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="act_estudia" value="ESTUDIA" v-model="v$.condicionLaboral.$model">
                        <label class="form-check-label" for="act_estudia">Estudia</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="act_artesanias" value="ELABORA_ARTESANIAS" v-model="v$.condicionLaboral.$model">
                        <label class="form-check-label" for="act_artesanias">Elabora artesanías</label>
                      </div>
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
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_kinder" value="KINDER" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_kinder">Kinder o preescolar</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_prepa" value="PREPARATORIA" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_prepa">Preparatoria o Bachillerato</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_tec_sec" value="TECNICA_SECUNDARIA" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_tec_sec">Carrera técnica o comercial con secundaria completa</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_posgrado" value="POSGRADO" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_posgrado">Posgrado (Maestría o Doctorado)</label>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_primaria" value="PRIMARIA" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_primaria">Primaria</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_normal" value="NORMAL_BASICA" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_normal">Normal Básica</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_tec_prepa" value="TECNICA_PREPARATORIA" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_tec_prepa">Carrera técnica o comercial con preparatoria completa</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_ninguno" value="NINGUNO" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_ninguno">Ninguno</label>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_secundaria" value="SECUNDARIA" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_secundaria">Secundaria</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_tec_com" value="CARRERA_TECNICA" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_tec_com">Carrera técnica o comercial</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="esc_licenciatura" value="LICENCIATURA" v-model="v$.gradoMaximoEstudios.$model">
                        <label class="form-check-label" for="esc_licenciatura">Profesional o Licenciatura</label>
                      </div>
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
                      class="form-control border-bottom border-dark rounded-0 px-2 text-center" 
                      placeholder="ANOTAR LO QUE CORRESPONDA"
                      :class="{ 'is-invalid': v$.mayorFuenteIngreso.$error }"
                      v-model="v$.mayorFuenteIngreso.$model"
                    />
                    <div class="invalid-feedback text-center" v-if="v$.mayorFuenteIngreso.$error">
                      Especifique la fuente de ingreso.
                    </div>
                  </div>
                </div>

                <div class="col-md-6 p-4 d-flex flex-column justify-content-center text-center">
                  <p class="font-weight-bold mb-3">
                    17. Considerando su trabajo principal, ¿cuánto tiempo se dedica a trabajar?
                  </p>
                  
                  <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.tiempoEmpleado.$error }">
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="tiempo_meses" value="UNOS_MESES" v-model="v$.tiempoEmpleado.$model">
                      <label class="form-check-label" for="tiempo_meses">¿Unos meses al año?</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="tiempo_todo" value="TODO_EL_ANO" v-model="v$.tiempoEmpleado.$model">
                      <label class="form-check-label" for="tiempo_todo">¿Todo el año?</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="tiempo_otro" value="OTRO" v-model="v$.tiempoEmpleado.$model">
                      <label class="form-check-label" for="tiempo_otro">Otro</label>
                    </div>
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
                  
                  <div class="d-flex justify-content-center align-items-center">
                    <span class="font-weight-bold mr-2" style="font-size: 1.2em;">$</span>
                    <div style="width: 200px;">
                      <input 
                        type="text" 
                        class="form-control border-bottom border-dark rounded-0 text-center" 
                        :class="{ 'is-invalid': v$.ingresoMensual.$error }"
                        v-model="v$.ingresoMensual.$model"
                        @input="limpiarInputNumerico($event, 'ingresoMensual')"
                      />
                      <div class="invalid-feedback" v-if="v$.ingresoMensual.$error">
                        Ingrese un monto válido (Máx. 100,000).
                      </div>
                    </div>
                  </div>
                </div>

                <div class="col-md-6 p-4 d-flex flex-column justify-content-center">
                  <p class="font-weight-bold mb-3">19. ¿Es persona jubilada o pensionada?</p>
                  
                  <div class="form-group mb-3" :class="{ 'is-invalid border border-danger p-1 rounded': v$.esJubiladoPensionado.$error }">
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="jubilado_si" :value="true" v-model="v$.esJubiladoPensionado.$model">
                      <label class="form-check-label font-weight-bold" for="jubilado_si">Sí</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" id="jubilado_no" :value="false" v-model="v$.esJubiladoPensionado.$model" @change="limpiarPension()">
                      <label class="form-check-label font-weight-bold" for="jubilado_no">No</label>
                    </div>
                  </div>
                  <div class="invalid-feedback d-block mb-2" v-if="v$.esJubiladoPensionado.$error">Seleccione una opción.</div>

                  <div v-if="infoSocioeconomica.esJubiladoPensionado === true">
                    <p class="mb-2">¿Cuánto dinero recibe al mes?</p>
                    <div class="d-flex justify-content-center align-items-center">
                      <span class="font-weight-bold mr-2" style="font-size: 1.2em;">$</span>
                      <div style="width: 200px;">
                        <input 
                          type="text" 
                          class="form-control border-bottom border-dark rounded-0 text-center" 
                          :class="{ 'is-invalid': v$.montoPension.$error }"
                          v-model="v$.montoPension.$model"
                          @input="limpiarInputNumerico($event, 'montoPension')"
                        />
                        <div class="invalid-feedback" v-if="v$.montoPension.$error">
                          Ingrese un monto válido (Máx. 100,000).
                        </div>
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
                    <span class="font-weight-normal" style="font-size: 0.9em;">(sin contar pasillos ni baños)</span>
                  </p>
                  <div class="d-flex align-items-end justify-content-center mt-auto">
                    <input 
                      type="text" 
                      class="form-control border-bottom border-dark rounded-0 text-center mx-2" 
                      style="width: 80px;"
                      maxlength="2"
                      :class="{ 'is-invalid': v$.numeroHabitaciones.$error }"
                      v-model="v$.numeroHabitaciones.$model"
                      @input="limpiarInputNumerico($event, 'numeroHabitaciones')"
                    />
                    <span class="font-weight-bold mb-1">cuartos</span>
                  </div>
                  <div class="invalid-feedback d-block mt-2" v-if="v$.numeroHabitaciones.$error">
                    Debe ser un número entre 0 y 20.
                  </div>
                </div>

                <div class="col-md-6 p-4 d-flex flex-column justify-content-center align-items-center">
                  <p class="font-weight-bold mb-3">
                    21. ¿Cuántos cuartos usan para dormir?
                  </p>
                  <div class="d-flex align-items-end justify-content-center mt-auto">
                    <input 
                      type="text" 
                      class="form-control border-bottom border-dark rounded-0 text-center mx-2" 
                      style="width: 80px;"
                      maxlength="2"
                      :class="{ 'is-invalid': v$.cuartosDormir.$error }"
                      v-model="v$.cuartosDormir.$model"
                      @input="limpiarInputNumerico($event, 'cuartosDormir')"
                    />
                    <span class="font-weight-bold mb-1">cuartos</span>
                  </div>
                  <div class="invalid-feedback d-block mt-2" v-if="v$.cuartosDormir.$error">
                    Debe ser un número entre 0 y 20.
                  </div>
                </div>

              </div>

              <div class="row m-0 p-4 text-center">
                <div class="col-12">
                  <p class="font-weight-bold mb-3">
                    22. ¿De qué material es la mayor parte del piso de su vivienda? <br>
                  </p>
                  
                  <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.materialVivienda.$error }">
                    <div class="row justify-content-center">
                      <div class="col-md-4">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="piso_tierra" value="TIERRA" v-model="v$.materialVivienda.$model">
                          <label class="form-check-label font-weight-bold" for="piso_tierra">Tierra</label>
                        </div>
                      </div>
                      <div class="col-md-4">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="piso_cemento" value="CEMENTO" v-model="v$.materialVivienda.$model">
                          <label class="form-check-label font-weight-bold" for="piso_cemento">Cemento</label>
                        </div>
                      </div>
                      <div class="col-md-4">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="piso_mosaico" value="MOSAICO_MADERA_OTRO" v-model="v$.materialVivienda.$model">
                          <label class="form-check-label font-weight-bold" for="piso_mosaico">Mosaico, madera u otro recubrimiento</label>
                        </div>
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
                  <div class="bg-light border-bottom border-dark p-2 font-weight-bold text-center">
                    23. ¿De qué material es la mayor parte del techo de su vivienda?
                  </div>
                  <div class="p-3">
                    <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.materialTecho.$error }">
                      
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="techo_desecho" value="DESECHO" v-model="v$.materialTecho.$model">
                        <label class="form-check-label" for="techo_desecho">Material de desecho (cartón, hule, tela, llanta)</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="techo_carton" value="LAMINA_CARTON" v-model="v$.materialTecho.$model">
                        <label class="form-check-label" for="techo_carton">Lámina de cartón</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="techo_metalica" value="LAMINA_METALICA" v-model="v$.materialTecho.$model">
                        <label class="form-check-label" for="techo_metalica">Lámina metálica / asbesto</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="techo_palma" value="PALMA_PAJA" v-model="v$.materialTecho.$model">
                        <label class="form-check-label" for="techo_palma">Palma o paja</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="techo_losa" value="LOSA_CONCRETO" v-model="v$.materialTecho.$model">
                        <label class="form-check-label" for="techo_losa">Losa de concreto o viguetas con bovedilla</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="techo_madera" value="MADERA" v-model="v$.materialTecho.$model">
                        <label class="form-check-label" for="techo_madera">Madera o tejamanil</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="techo_terrado" value="TERRADO_VIGUERIA" v-model="v$.materialTecho.$model">
                        <label class="form-check-label" for="techo_terrado">Terrado con viguería</label>
                      </div>
                      <div class="form-check mb-0">
                        <input class="form-check-input" type="radio" id="techo_teja" value="TEJA" v-model="v$.materialTecho.$model">
                        <label class="form-check-label" for="techo_teja">Teja</label>
                      </div>

                    </div>
                    <div class="invalid-feedback d-block mt-2" v-if="v$.materialTecho.$error">
                      Seleccione el material del techo.
                    </div>
                  </div>
                </div>

                <div class="col-md-6 p-0">
                  <div class="bg-light border-bottom border-dark p-2 font-weight-bold text-center">
                    24. ¿De qué material es la mayor parte de las paredes o muros de su vivienda?
                  </div>
                  <div class="p-3">
                    <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.materialParedes.$error }">
                      
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="pared_desecho" value="DESECHO" v-model="v$.materialParedes.$model">
                        <label class="form-check-label" for="pared_desecho">Material de desecho (cartón, hule, tela, llanta)</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="pared_carton" value="LAMINA_CARTON" v-model="v$.materialParedes.$model">
                        <label class="form-check-label" for="pared_carton">Lámina de cartón</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="pared_metalica" value="LAMINA_METALICA" v-model="v$.materialParedes.$model">
                        <label class="form-check-label" for="pared_metalica">Lámina metálica / asbesto</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="pared_carrizo" value="CARRIZO_BAMBU_PALMA" v-model="v$.materialParedes.$model">
                        <label class="form-check-label" for="pared_carrizo">Carrizo, bambú o palma</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="pared_embarro" value="EMBARRO_BAJAREQUE" v-model="v$.materialParedes.$model">
                        <label class="form-check-label" for="pared_embarro">Embarro o bajareque</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="pared_adobe" value="ADOBE" v-model="v$.materialParedes.$model">
                        <label class="form-check-label" for="pared_adobe">Adobe</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="pared_madera" value="MADERA" v-model="v$.materialParedes.$model">
                        <label class="form-check-label" for="pared_madera">Madera</label>
                      </div>
                      <div class="form-check mb-0">
                        <input class="form-check-input" type="radio" id="pared_tabique" value="TABIQUE_LADRILLO_BLOCK" v-model="v$.materialParedes.$model">
                        <label class="form-check-label" for="pared_tabique">Tabique, ladrillo, block o concreto</label>
                      </div>

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
                  <div class="bg-light border-bottom border-dark p-2 font-weight-bold text-center">
                    25. ¿Qué tipo de baño o escusado tiene en su vivienda?
                  </div>
                  <div class="p-3">
                    <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.tipoBano.$error }">
                      
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="bano_no_tiene" value="NO_TIENE" v-model="v$.tipoBano.$model">
                        <label class="form-check-label" for="bano_no_tiene">No tiene</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="bano_pozo" value="POZO_HOYO_NEGRO" v-model="v$.tipoBano.$model">
                        <label class="form-check-label" for="bano_pozo">Pozo u hoyo negro</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="bano_cubeta" value="AGUA_CON_CUBETA" v-model="v$.tipoBano.$model">
                        <label class="form-check-label" for="bano_cubeta">Le echan agua con cubeta</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="bano_letrina" value="LETRINA_SECA_HUMEDA" v-model="v$.tipoBano.$model">
                        <label class="form-check-label" for="bano_letrina">Sin admisión de agua (letrina seca / húmeda)</label>
                      </div>
                      <div class="form-check mb-0">
                        <input class="form-check-input" type="radio" id="bano_conexion" value="CONEXION_DESCARGA_DIRECTA" v-model="v$.tipoBano.$model">
                        <label class="form-check-label" for="bano_conexion">Con conexión y con descarga directa de agua</label>
                      </div>

                    </div>
                    <div class="invalid-feedback d-block mt-2" v-if="v$.tipoBano.$error">
                      Seleccione el tipo de baño.
                    </div>
                  </div>
                </div>

                <div class="col-md-6 p-0">
                  <div class="bg-light border-bottom border-dark p-2 font-weight-bold text-center">
                    26. ¿Qué tipo de drenaje o desagüe de aguas tiene en su vivienda?
                  </div>
                  <div class="p-3">
                    <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.tipoDrenaje.$error }">
                      
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="drenaje_publica" value="RED_PUBLICA" v-model="v$.tipoDrenaje.$model">
                        <label class="form-check-label" for="drenaje_publica">A la red pública</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="drenaje_septica" value="FOSA_SEPTICA" v-model="v$.tipoDrenaje.$model">
                        <label class="form-check-label" for="drenaje_septica">A una fosa séptica</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="drenaje_no_tiene" value="NO_TIENE" v-model="v$.tipoDrenaje.$model">
                        <label class="form-check-label" for="drenaje_no_tiene">No tiene desagüe</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="drenaje_grieta" value="TUBERIA_GRIETA_BARRANCA" v-model="v$.tipoDrenaje.$model">
                        <label class="form-check-label" for="drenaje_grieta">A una tubería que da a una grieta o barranca</label>
                      </div>
                      <div class="form-check mb-0">
                        <input class="form-check-input" type="radio" id="drenaje_rio" value="TUBERIA_RIO_LAGO_MAR" v-model="v$.tipoDrenaje.$model">
                        <label class="form-check-label" for="drenaje_rio">A una tubería que da a un río, lago o mar</label>
                      </div>

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
                  <div class="bg-light border-bottom border-dark p-2 font-weight-bold text-center">
                    27. ¿En su vivienda, qué hacen con la basura?
                  </div>
                  <div class="p-3">
                    <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.manejoBasura.$error }">
                      
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="basura_queman" value="QUEMAN" v-model="v$.manejoBasura.$model">
                        <label class="form-check-label" for="basura_queman">La queman</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="basura_rio" value="RIO_LAGO_BARRANCA" v-model="v$.manejoBasura.$model">
                        <label class="form-check-label" for="basura_rio">La tiran al río, lago, mar o barranca</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="basura_baldio" value="TERRENO_BALDIO_CALLE" v-model="v$.manejoBasura.$model">
                        <label class="form-check-label" for="basura_baldio">La tiran en un terreno baldío o calle</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="basura_basurero" value="BASURERO_PUBLICO" v-model="v$.manejoBasura.$model">
                        <label class="form-check-label" for="basura_basurero">La tiran en el basurero público</label>
                      </div>
                      <div class="form-check mb-0">
                        <input class="form-check-input" type="radio" id="basura_camion" value="CONTENEDOR_CAMION" v-model="v$.manejoBasura.$model">
                        <label class="form-check-label" for="basura_camion">La tiran en un contenedor, la recoge un camión / carro de basura</label>
                      </div>

                    </div>
                    <div class="invalid-feedback d-block mt-2" v-if="v$.manejoBasura.$error">
                      Seleccione una opción.
                    </div>
                  </div>
                </div>

                <div class="col-md-6 p-0">
                  <div class="bg-light border-bottom border-dark p-2 font-weight-bold text-center">
                    28. En su vivienda, ¿la luz eléctrica la obtienen?
                  </div>
                  <div class="p-3">
                    <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.fuenteLuzElectrica.$error }">
                      
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="luz_publico" value="SERVICIO_PUBLICO" v-model="v$.fuenteLuzElectrica.$model">
                        <label class="form-check-label" for="luz_publico">Del servicio público</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="luz_planta" value="PLANTA_PARTICULAR" v-model="v$.fuenteLuzElectrica.$model">
                        <label class="form-check-label" for="luz_planta">De una planta particular</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="luz_panel" value="PANEL_SOLAR" v-model="v$.fuenteLuzElectrica.$model">
                        <label class="form-check-label" for="luz_panel">De panel solar</label>
                      </div>
                      <div class="form-check mb-2">
                        <input class="form-check-input" type="radio" id="luz_otra" value="OTRA_FUENTE" v-model="v$.fuenteLuzElectrica.$model">
                        <label class="form-check-label" for="luz_otra">De otra fuente</label>
                      </div>
                      <div class="form-check mb-0">
                        <input class="form-check-input" type="radio" id="luz_no_tiene" value="NO_TIENE" v-model="v$.fuenteLuzElectrica.$model">
                        <label class="form-check-label" for="luz_no_tiene">No tienen luz eléctrica</label>
                      </div>

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
                  <div class="bg-light border border-dark p-2 mb-3 font-weight-bold">
                    29. ¿Cuál es el combustible que más usan para cocinar?
                  </div>
                  
                  <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.combustibleCocina.$error }">
                    <div class="row justify-content-center">
                      <div class="col-md-4">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="comb_electricidad" value="ELECTRICIDAD" v-model="v$.combustibleCocina.$model">
                          <label class="form-check-label" for="comb_electricidad">Electricidad</label>
                        </div>
                      </div>
                      <div class="col-md-4">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="comb_gas" value="GAS_LP" v-model="v$.combustibleCocina.$model">
                          <label class="form-check-label" for="comb_gas">Gas L.P.</label>
                        </div>
                      </div>
                      <div class="col-md-4">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="comb_lena" value="LENA_CARBON" v-model="v$.combustibleCocina.$model">
                          <label class="form-check-label" for="comb_lena">Leña o carbón</label>
                        </div>
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
                  <div class="bg-light border border-dark p-2 mb-3 font-weight-bold">
                    30. La vivienda que habita, ¿es?
                  </div>
                  
                  <div class="form-group mb-0" :class="{ 'is-invalid border border-danger p-2 rounded': v$.tenenciaVivienda.$error }">
                    <div class="row">
                      <div class="col-md-4 text-left pl-md-5">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="viv_propia_pagada" value="PROPIA_PAGADA" v-model="v$.tenenciaVivienda.$model">
                          <label class="form-check-label" for="viv_propia_pagada">Propia y está pagada</label>
                        </div>
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="viv_prestada" value="PRESTADA_CUIDANDO" v-model="v$.tenenciaVivienda.$model">
                          <label class="form-check-label" for="viv_prestada">Prestada o al está cuidando</label>
                        </div>
                      </div>
                      <div class="col-md-4 text-left pl-md-5">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="viv_propia_pagando" value="PROPIA_PAGANDO" v-model="v$.tenenciaVivienda.$model">
                          <label class="form-check-label" for="viv_propia_pagando">Propia y la está pagando</label>
                        </div>
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="viv_rentada" value="RENTADA" v-model="v$.tenenciaVivienda.$model">
                          <label class="form-check-label" for="viv_rentada">Rentada o alquilada</label>
                        </div>
                      </div>
                      <div class="col-md-4 text-left pl-md-5">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" id="viv_hipotecada" value="PROPIA_HIPOTECADA" v-model="v$.tenenciaVivienda.$model">
                          <label class="form-check-label" for="viv_hipotecada">Propia y está hipotecada</label>
                        </div>
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
        </div> <div class="custom-modal-footer" v-if="mostrarCuestionario">
          <span class="text-muted" style="font-size: 0.85rem;">Puedes cerrar esta ventana, el borrador se guardará.</span>
          <button type="button" class="btn btn-success px-5 font-weight-bold" @click="save" :disabled="isSaving">
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
/* ESTILOS DEL MODAL */
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

/* ESTILOS DEL ACORDEÓN */
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
</style>