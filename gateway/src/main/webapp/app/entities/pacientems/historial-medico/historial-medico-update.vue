<template>
  <div class="row justify-content-center">
    <div class="col-10">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="gatewayApp.historialMedico.home.createOrEditLabel" data-cy="HistorialMedicoCreateUpdateHeading">
          Crear o Editar Historial Médico
        </h2>

        <div class="card mb-4 mt-3" v-if="!historialMedico.id">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Paso 1: Identificar al Paciente</h5>
          </div>
          <div class="card-body">
            <div class="form-group mb-0">
              <label class="form-control-label">Ingrese el ECU del paciente</label>
              <div class="input-group">
                <input 
                  type="text" 
                  class="form-control" 
                  v-model="ecuSearchString" 
                  placeholder="Ej: 12345" 
                  @input="ecuSearchString = ecuSearchString.replace(/\D/g, '')"
                  @keyup.enter.prevent="buscarPaciente()" 
                  :readonly="pacienteEncontrado !== null"
                />
                <div class="input-group-append">
                  <button class="btn btn-primary" type="button" @click="buscarPaciente()" :disabled="isSearchingEcu || !ecuSearchString || pacienteEncontrado !== null">
                    <font-awesome-icon icon="search" v-if="!isSearchingEcu"></font-awesome-icon>
                    <font-awesome-icon icon="sync" spin v-if="isSearchingEcu"></font-awesome-icon>
                    Buscar
                  </button>
                </div>
              </div>
            </div>
            
            <div v-if="pacienteEncontrado" class="alert alert-success mt-3 mb-0">
              <font-awesome-icon icon="check-circle"></font-awesome-icon>
              <strong> Paciente seleccionado:</strong> 
              {{ pacienteEncontrado.nombre }} {{ pacienteEncontrado.apellidoPaterno }} {{ pacienteEncontrado.apellidoMaterno }}
            </div>
          </div>
        </div>

        <div v-if="pacienteEncontrado || historialMedico.id">
          
        <div class="card mb-3">
            <div class="card-header bg-info text-white">Datos Biométricos y Sanguíneos</div>
            <div class="card-body row">
              <div class="form-group col-md-4">
                <label class="form-control-label">Altura (cm)</label>
                <input 
                  type="number" 
                  class="form-control" 
                  v-model="historialMedico.altura" 
                  placeholder="Ej: 175"
                  min="20"
                  max="250"
@input="calcularIMC"                />
                <small class="form-text text-muted">Mín: 20cm, Máx: 250cm</small>
              </div>
              <div class="form-group col-md-4">
                <label class="form-control-label">Peso (kg)</label>
                <input 
                  type="number" 
                  step="0.1" 
                  class="form-control" 
                  v-model="historialMedico.peso" 
                  placeholder="Ej: 70.5"
                  min="0.5"
                  max="350"
@input="calcularIMC"                />
                <small class="form-text text-muted">Mín: 0.5kg, Máx: 350kg</small>
              </div>
              <div class="form-group col-md-4">
                <label class="form-control-label">IMC</label>
                <input 
                  type="text" 
                  class="form-control font-weight-bold" 
                  v-model="historialMedico.imc" 
                  readonly 
                  style="background-color: #e9ecef;" 
                />
              </div>
              <div class="form-group col-md-6">
                <label class="form-control-label">Grupo Sanguíneo</label>
                <select class="form-control" v-model="historialMedico.grupoSanguineo">
                  <option value="null">-- Seleccione --</option>
                  <option value="A">A</option>
                  <option value="B">B</option>
                  <option value="AB">AB</option>
                  <option value="O">O</option>
                </select>
              </div>
              <div class="form-group col-md-6">
                <label class="form-control-label">Factor RH</label>
                <select class="form-control" v-model="historialMedico.factorRh">
                  <option value="null">-- Seleccione --</option>
                  <option value="+">Positivo (+)</option>
                  <option value="-">Negativo (-)</option>
                </select>
              </div>
            </div>
          </div>

          <div class="card mb-3 border-danger">
            <div class="card-header bg-danger text-white font-weight-bold">
              <font-awesome-icon icon="exclamation-triangle"></font-awesome-icon> Alergias
            </div>
            <div class="card-body">
              
              <div class="form-group row">
                <label class="col-md-3 col-form-label font-weight-bold">¿Tiene alergias conocidas?</label>
                <div class="col-md-9 d-flex align-items-center">
                  <div class="custom-control custom-radio custom-control-inline mr-4">
                    <input type="radio" id="alergiaSi" name="tieneAlergias" :value="true" class="custom-control-input" v-model="historialMedico.tieneAlergias" @change="limpiarAlergias">
                    <label class="custom-control-label" for="alergiaSi">Sí</label>
                  </div>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="alergiaNo" name="tieneAlergias" :value="false" class="custom-control-input" v-model="historialMedico.tieneAlergias" @change="limpiarAlergias">
                    <label class="custom-control-label" for="alergiaNo">No</label>
                  </div>
                </div>
              </div>

              <div v-if="historialMedico.tieneAlergias === true" class="p-3 bg-light border rounded mt-3">
                <div class="row">
                  
                  <div class="form-group col-md-6">
                    <label class="form-control-label font-weight-bold">Tipo de alergia</label>
                    <select class="form-control" v-model="historialMedico.tipoAlergia">
                      <option value="null">-- Seleccione el origen --</option>
                      <option value="MEDICAMENTOS">Medicamentos (Antibióticos, Analgésicos...)</option>
                      <option value="ALIMENTOS">Alimentos (Mariscos, Leche, Nueces...)</option>
                      <option value="AMBIENTALES">Ambientales (Polvo, Polen, Ácaros...)</option>
                      <option value="CONTACTO">Contacto (Látex, Metales, Jabones...)</option>
                      <option value="PICADURAS">Picaduras o Animales (Abejas, Gatos...)</option>
                      <option value="OTROS">Otros (Frío, Calor, Ejercicio...)</option>
                    </select>
                  </div>

                  <div class="form-group col-md-6">
                    <label class="form-control-label font-weight-bold">Sustancia o Medicamento exacto</label>
                    <input type="text" class="form-control" v-model="historialMedico.sustanciaAlergia" placeholder="Ej: Penicilina, Mariscos, Polen..." />
                  </div>

                  <div class="form-group col-12 mt-2">
                    <label class="form-control-label font-weight-bold">Reacción que presenta (Puede seleccionar varias)</label>
                    <div class="row px-3">
                      <div class="col-md-4 border-right">
                        <span class="text-success font-weight-bold text-uppercase" style="font-size: 0.85em;">Leves</span>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r1" value="Picazón" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label" for="r1">Picazón</label></div>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r2" value="Estornudos" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label" for="r2">Estornudos</label></div>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r3" value="Congestión nasal" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label" for="r3">Congestión nasal</label></div>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r4" value="Ojos llorosos" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label" for="r4">Ojos llorosos</label></div>
                      </div>
                      <div class="col-md-4 border-right">
                        <span class="text-warning font-weight-bold text-uppercase" style="font-size: 0.85em;">Moderadas</span>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r5" value="Ronchas" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label" for="r5">Ronchas / Urticaria</label></div>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r6" value="Inflamación" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label" for="r6">Inflamación leve</label></div>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r7" value="Náuseas/Vómito" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label" for="r7">Náuseas / Vómito</label></div>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r8" value="Diarrea/Dolor abd." v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label" for="r8">Dolor abdominal</label></div>
                      </div>
                      <div class="col-md-4">
                        <span class="text-danger font-weight-bold text-uppercase" style="font-size: 0.85em;">Graves (Urgencia)</span>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r9" value="Dificultad respirar" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label text-danger" for="r9">Dificultad respiratoria</label></div>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r10" value="Inflamación garganta" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label text-danger" for="r10">Inflamación garganta/labios</label></div>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r11" value="Mareo/Desmayo" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label text-danger" for="r11">Mareo / Desmayo</label></div>
                        <div class="custom-control custom-checkbox"><input type="checkbox" id="r12" value="Anafilaxia" v-model="historialMedico.reaccionesAlergia" class="custom-control-input"><label class="custom-control-label text-danger font-weight-bold" for="r12">Anafilaxia</label></div>
                      </div>
                    </div>
                  </div>

                  <div class="form-group col-md-4 mt-2">
                    <label class="form-control-label font-weight-bold">Gravedad</label>
                    <select class="form-control" v-model="historialMedico.gravedadAlergia">
                      <option value="null">-- Seleccione --</option>
                      <option value="LEVE">Leve (Solo síntomas menores)</option>
                      <option value="MODERADA">Moderada (Inflamación/Digestivos)</option>
                      <option value="GRAVE">Grave (Problemas respiratorios)</option>
                      <option value="ANAFILACTICA">Anafiláctica (Riesgo vital)</option>
                    </select>
                  </div>

                  <div class="form-group col-md-4 mt-2">
                    <label class="form-control-label font-weight-bold">Tratamiento habitual</label>
                    <select class="form-control" v-model="historialMedico.tratamientoAlergia">
                      <option value="null">-- Seleccione --</option>
                      <option value="ANTIHISTAMINICOS">Antihistamínicos (Loratadina...)</option>
                      <option value="CORTICOSTEROIDES">Corticosteroides</option>
                      <option value="EPINEFRINA">Epinefrina / Adrenalina</option>
                      <option value="EVITAR_EXPOSICION">Solo evitar exposición</option>
                      <option value="NINGUNO">Ninguno</option>
                      <option value="OTRO">Otro</option>
                    </select>
                  </div>

                  <div class="form-group col-md-4 mt-2">
                    <label class="form-control-label font-weight-bold">Último episodio (Aprox.)</label>
                    <input type="date" class="form-control" v-model="historialMedico.ultimoEpisodioAlergia" />
                  </div>

                </div>
              </div>

            </div>
          </div>

          <div class="card mb-3 border-warning">
            <div class="card-header bg-warning text-dark font-weight-bold">
              <font-awesome-icon icon="heartbeat"></font-awesome-icon> Enfermedades Crónicas
            </div>
            <div class="card-body">
              
              <div class="form-group row">
                <label class="col-md-4 col-form-label font-weight-bold">¿El paciente padece alguna enfermedad crónica?</label>
                <div class="col-md-8 d-flex align-items-center">
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="cronicaSi" name="padeceCronicas" value="SI" class="custom-control-input" v-model="historialMedico.padeceCronicas" @change="manejarCambioCronicas">
                    <label class="custom-control-label" for="cronicaSi">Sí</label>
                  </div>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="cronicaNo" name="padeceCronicas" value="NO" class="custom-control-input" v-model="historialMedico.padeceCronicas" @change="manejarCambioCronicas">
                    <label class="custom-control-label" for="cronicaNo">No</label>
                  </div>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="cronicaDesconoce" name="padeceCronicas" value="DESCONOCE" class="custom-control-input" v-model="historialMedico.padeceCronicas" @change="manejarCambioCronicas">
                    <label class="custom-control-label" for="cronicaDesconoce">Desconoce</label>
                  </div>
                </div>
              </div>

              <div v-if="historialMedico.padeceCronicas === 'SI'">
                
                <div v-for="(enfermedad, index) in listaEnfermedades" :key="index" class="p-3 mb-3 bg-light border rounded position-relative">
                  
                  <button type="button" class="btn btn-sm btn-danger position-absolute" style="top: 10px; right: 10px;" @click="eliminarEnfermedad(index)" v-if="listaEnfermedades.length > 1" title="Eliminar enfermedad">
                    <font-awesome-icon icon="times"></font-awesome-icon>
                  </button>

                  <h6 class="font-weight-bold text-primary border-bottom pb-2 mb-3">Enfermedad #{{ index + 1 }}</h6>
                  
                  <div class="row">
                    <div class="form-group col-md-4">
                      <label class="font-weight-bold">Clasificación</label>
                      <select class="form-control" v-model="enfermedad.tipo">
                        <option value="null">-- Seleccione --</option>
                        <option value="Cardiovasculares">Cardiovasculares (Hipertensión...)</option>
                        <option value="Metabólicas">Metabólicas (Diabetes, Obesidad...)</option>
                        <option value="Respiratorias">Respiratorias (Asma, EPOC...)</option>
                        <option value="Neurológicas">Neurológicas (Epilepsia, Parkinson...)</option>
                        <option value="Autoinmunes">Autoinmunes (Lupus, Artritis...)</option>
                        <option value="Renales">Renales (Insuficiencia...)</option>
                        <option value="Endocrinas">Endocrinas (Tiroides...)</option>
                        <option value="Hepáticas">Hepáticas (Hepatitis, Cirrosis...)</option>
                        <option value="Infecciosas">Infecciosas crónicas (VIH...)</option>
                        <option value="Cáncer">Cáncer (Mama, Próstata...)</option>
                        <option value="Otra">Otra / Enfermedad Rara</option>
                      </select>
                    </div>

                    <div class="form-group col-md-4">
                      <label class="font-weight-bold">Enfermedad Específica</label>
                      <input type="text" class="form-control" v-model="enfermedad.nombre" placeholder="Ej: Diabetes Tipo 2" />
                    </div>

                    <div class="form-group col-md-4">
                      <label class="font-weight-bold">Año/Tiempo de Diagnóstico</label>
                      <input type="text" class="form-control" v-model="enfermedad.diagnostico" placeholder="Ej: 2018 o 'Hace 10 años'" />
                    </div>

                    <div class="form-group col-md-4">
                      <label class="font-weight-bold">Estado Actual</label>
                      <select class="form-control" v-model="enfermedad.estado">
                        <option value="null">-- Seleccione --</option>
                        <option value="Controlada">Controlada</option>
                        <option value="Parcialmente controlada">Parcialmente controlada</option>
                        <option value="No controlada">No controlada</option>
                        <option value="En tratamiento">En tratamiento</option>
                        <option value="En remisión">En remisión</option>
                      </select>
                    </div>

                    <div class="form-group col-md-4">
                      <label class="font-weight-bold">Tratamiento Actual</label>
                      <input type="text" class="form-control" v-model="enfermedad.tratamiento" placeholder="Ej: Dieta y medicamentos" />
                    </div>
                    <div class="form-group col-md-4">
                      <label class="font-weight-bold">Medicamentos</label>
                      <input type="text" class="form-control" v-model="enfermedad.medicamentos" placeholder="Ej: Metformina 500mg" />
                    </div>

                    <div class="form-group col-md-6">
                      <label class="font-weight-bold">Médico Especialista Tratante</label>
                      <input type="text" class="form-control" v-model="enfermedad.medico" placeholder="Ej: Endocrinólogo Dr. Pérez" />
                    </div>
                    <div class="form-group col-md-6">
                      <label class="font-weight-bold">Hospital o Clínica</label>
                      <input type="text" class="form-control" v-model="enfermedad.hospital" placeholder="Ej: IMSS Clinica 32" />
                    </div>

                    <div class="form-group col-12 mb-0">
                      <label class="font-weight-bold">Observaciones Médicas</label>
                      <textarea class="form-control" v-model="enfermedad.observaciones" rows="2" placeholder="Notas sobre evolución o complicaciones..."></textarea>
                    </div>
                  </div>
                </div>

                <button type="button" class="btn btn-outline-primary btn-sm mt-2" @click="agregarEnfermedad">
                  <font-awesome-icon icon="plus"></font-awesome-icon> Añadir otra enfermedad crónica
                </button>
              </div>

            </div>
          </div>

          <div class="card mb-3 border-info">
            <div class="card-header bg-info text-white font-weight-bold">
              <font-awesome-icon icon="procedures"></font-awesome-icon> Cirugías Previas
            </div>
            <div class="card-body">
              
              <div class="form-group row">
                <label class="col-md-4 col-form-label font-weight-bold">¿El paciente ha tenido cirugías previas?</label>
                <div class="col-md-8 d-flex align-items-center">
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="cirugiaSi" name="haTenidoCirugias" value="SI" class="custom-control-input" v-model="historialMedico.haTenidoCirugias" @change="manejarCambioCirugias">
                    <label class="custom-control-label" for="cirugiaSi">Sí</label>
                  </div>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="cirugiaNo" name="haTenidoCirugias" value="NO" class="custom-control-input" v-model="historialMedico.haTenidoCirugias" @change="manejarCambioCirugias">
                    <label class="custom-control-label" for="cirugiaNo">No</label>
                  </div>
                </div>
              </div>

              <div v-if="historialMedico.haTenidoCirugias === 'SI'">
                
                <div v-for="(cirugia, index) in listaCirugias" :key="index" class="p-3 mb-3 bg-light border rounded position-relative">
                  
                  <button type="button" class="btn btn-sm btn-danger position-absolute" style="top: 10px; right: 10px;" @click="eliminarCirugia(index)" v-if="listaCirugias.length > 1" title="Eliminar cirugía">
                    <font-awesome-icon icon="times"></font-awesome-icon>
                  </button>

                  <h6 class="font-weight-bold text-info border-bottom pb-2 mb-3">Cirugía #{{ index + 1 }}</h6>
                  
                  <div class="row">
                    <div class="form-group col-md-4">
                      <label class="font-weight-bold">Tipo de Cirugía</label>
                      <select class="form-control" v-model="cirugia.tipo">
                        <option value="null">-- Seleccione --</option>
                        <option value="General">General</option>
                        <option value="Traumatológica">Traumatológica</option>
                        <option value="Cardiovascular">Cardiovascular</option>
                        <option value="Neurológica">Neurológica</option>
                        <option value="Ginecológica">Ginecológica</option>
                        <option value="Urológica">Urológica</option>
                        <option value="Estética">Estética</option>
                        <option value="Otra">Otra</option>
                      </select>
                    </div>

                    <div class="form-group col-md-5">
                      <label class="font-weight-bold">Nombre del procedimiento</label>
                      <input type="text" class="form-control" v-model="cirugia.nombre" placeholder="Ej: Apendicectomía, Cesárea..." />
                    </div>

                    <div class="form-group col-md-3">
                      <label class="font-weight-bold">Año</label>
                      <input type="text" class="form-control" v-model="cirugia.anio" placeholder="Ej: 2015" />
                    </div>

                    <div class="form-group col-md-6">
                      <label class="font-weight-bold">Motivo de la cirugía</label>
                      <input type="text" class="form-control" v-model="cirugia.motivo" placeholder="Ej: Apendicitis aguda" />
                    </div>

                    <div class="form-group col-md-6">
                      <label class="font-weight-bold">¿Hubo complicaciones?</label>
                      <select class="form-control" v-model="cirugia.huboComplicaciones">
                        <option value="NO">No</option>
                        <option value="SI">Sí</option>
                      </select>
                    </div>

                    <div class="form-group col-12 mb-0" v-if="cirugia.huboComplicaciones === 'SI'">
                      <label class="font-weight-bold text-danger">Describa la complicación</label>
                      <textarea class="form-control border-danger" v-model="cirugia.descripcionComplicaciones" rows="2" placeholder="Ej: Infección en la herida, reacción a la anestesia..."></textarea>
                    </div>
                  </div>
                </div>

                <button type="button" class="btn btn-outline-info btn-sm mt-2" @click="agregarCirugia">
                  <font-awesome-icon icon="plus"></font-awesome-icon> Añadir otra cirugía
                </button>
              </div>

            </div>
          </div>

          <div class="card mb-3 border-success">
            <div class="card-header bg-success text-white font-weight-bold">
              <font-awesome-icon icon="pills"></font-awesome-icon> Medicamentos Actuales
            </div>
            <div class="card-body">
              <div class="form-group row">
                <label class="col-md-5 col-form-label font-weight-bold">¿El paciente toma medicamentos actualmente?</label>
                <div class="col-md-7 d-flex align-items-center">
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="medSi" value="SI" class="custom-control-input" v-model="historialMedico.tomaMedicamentos" @change="manejarCambioMedicamentos">
                    <label class="custom-control-label" for="medSi">Sí</label>
                  </div>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="medNo" value="NO" class="custom-control-input" v-model="historialMedico.tomaMedicamentos" @change="manejarCambioMedicamentos">
                    <label class="custom-control-label" for="medNo">No</label>
                  </div>
                </div>
              </div>

              <div v-if="historialMedico.tomaMedicamentos === 'SI'">
                <div v-for="(med, index) in listaMedicamentos" :key="'med-'+index" class="p-3 mb-3 bg-light border rounded position-relative">
                  <button type="button" class="btn btn-sm btn-danger position-absolute" style="top: 10px; right: 10px;" @click="eliminarMedicamento(index)" v-if="listaMedicamentos.length > 1">
                    <font-awesome-icon icon="times"></font-awesome-icon>
                  </button>
                  <h6 class="font-weight-bold text-success border-bottom pb-2 mb-3">Medicamento #{{ index + 1 }}</h6>
                  <div class="row">
                    <div class="form-group col-md-4">
                      <label class="font-weight-bold">Nombre del medicamento</label>
                      <input type="text" class="form-control" v-model="med.nombre" placeholder="Ej: Losartán 50mg" />
                    </div>
                    <div class="form-group col-md-5">
                      <label class="font-weight-bold">Motivo</label>
                      <input type="text" class="form-control" v-model="med.motivo" placeholder="Ej: Presión alta" />
                    </div>
                    <div class="form-group col-md-3">
                      <label class="font-weight-bold">Frecuencia</label>
                      <select class="form-control" v-model="med.frecuencia">
                        <option value="null">-- Seleccione --</option>
                        <option value="1 vez al día">1 vez al día</option>
                        <option value="Cada 12 horas">Cada 12 horas</option>
                        <option value="Cada 8 horas">Cada 8 horas</option>
                        <option value="Cada 6 horas">Cada 6 horas</option>
                        <option value="Ocasional">Ocasional</option>
                        <option value="Otro">Otro</option>
                      </select>
                    </div>
                  </div>
                </div>
                <button type="button" class="btn btn-outline-success btn-sm mt-2" @click="agregarMedicamento">
                  <font-awesome-icon icon="plus"></font-awesome-icon> Añadir otro medicamento
                </button>
              </div>
            </div>
          </div>

          <div class="card mb-3 border-primary">
            <div class="card-header bg-primary text-white font-weight-bold">
              <font-awesome-icon icon="users"></font-awesome-icon> Antecedentes Familiares Hereditarios
            </div>
            <div class="card-body">
              <div class="form-group row">
                <label class="col-md-5 col-form-label font-weight-bold">¿Existen enfermedades importantes en la familia?</label>
                <div class="col-md-7 d-flex align-items-center">
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="famSi" value="SI" class="custom-control-input" v-model="historialMedico.tieneFamiliares" @change="manejarCambioFamiliares">
                    <label class="custom-control-label" for="famSi">Sí</label>
                  </div>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="famNo" value="NO" class="custom-control-input" v-model="historialMedico.tieneFamiliares" @change="manejarCambioFamiliares">
                    <label class="custom-control-label" for="famNo">No</label>
                  </div>
                </div>
              </div>

              <div v-if="historialMedico.tieneFamiliares === 'SI'">
                <div v-for="(fam, index) in listaFamiliares" :key="'fam-'+index" class="p-3 mb-3 bg-light border rounded position-relative">
                  <button type="button" class="btn btn-sm btn-danger position-absolute" style="top: 10px; right: 10px;" @click="eliminarFamiliar(index)" v-if="listaFamiliares.length > 1">
                    <font-awesome-icon icon="times"></font-awesome-icon>
                  </button>
                  <div class="row mt-3">
                    <div class="form-group col-md-6">
                      <label class="font-weight-bold">Enfermedad</label>
                      <select class="form-control" v-model="fam.enfermedad">
                        <option value="null">-- Seleccione --</option>
                        <option value="Diabetes">Diabetes</option>
                        <option value="Hipertensión">Hipertensión</option>
                        <option value="Cáncer">Cáncer</option>
                        <option value="Enfermedades cardíacas">Enfermedades cardíacas</option>
                        <option value="Enfermedades renales">Enfermedades renales</option>
                        <option value="Enfermedades mentales">Enfermedades mentales</option>
                        <option value="Obesidad">Obesidad</option>
                        <option value="Asma">Asma</option>
                        <option value="Otra">Otra</option>
                      </select>
                    </div>
                    <div class="form-group col-md-6">
                      <label class="font-weight-bold">Familiar Afectado</label>
                      <select class="form-control" v-model="fam.parentezco">
                        <option value="null">-- Seleccione --</option>
                        <option value="Padre">Padre</option>
                        <option value="Madre">Madre</option>
                        <option value="Hermano/Hermana">Hermano / Hermana</option>
                        <option value="Abuelo/Abuela">Abuelo / Abuela</option>
                        <option value="Tío/Tía">Tío / Tía</option>
                        <option value="Otro">Otro</option>
                      </select>
                    </div>
                  </div>
                </div>
                <button type="button" class="btn btn-outline-primary btn-sm mt-2" @click="agregarFamiliar">
                  <font-awesome-icon icon="plus"></font-awesome-icon> Añadir otro antecedente familiar
                </button>
              </div>
            </div>
          </div>

          <div class="card mb-3 border-dark">
            <div class="card-header bg-dark text-white font-weight-bold">
              <font-awesome-icon icon="file-medical"></font-awesome-icon> Antecedentes Personales Patológicos (Pasados)
            </div>
            <div class="card-body">
              <div class="form-group row">
                <label class="col-md-6 col-form-label font-weight-bold">¿El paciente ha padecido enfermedades importantes anteriormente?</label>
                <div class="col-md-6 d-flex align-items-center">
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="patSi" value="SI" class="custom-control-input" v-model="historialMedico.tienePatologicos" @change="manejarCambioPatologicos">
                    <label class="custom-control-label" for="patSi">Sí</label>
                  </div>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="patNo" value="NO" class="custom-control-input" v-model="historialMedico.tienePatologicos" @change="manejarCambioPatologicos">
                    <label class="custom-control-label" for="patNo">No</label>
                  </div>
                </div>
              </div>

              <div v-if="historialMedico.tienePatologicos === 'SI'">
                <div v-for="(pat, index) in listaPatologicos" :key="'pat-'+index" class="p-3 mb-3 bg-light border rounded position-relative">
                  <button type="button" class="btn btn-sm btn-danger position-absolute" style="top: 10px; right: 10px;" @click="eliminarPatologico(index)" v-if="listaPatologicos.length > 1">
                    <font-awesome-icon icon="times"></font-awesome-icon>
                  </button>
                  <div class="row mt-3">
                    <div class="form-group col-md-8">
                      <label class="font-weight-bold">Enfermedad Padecida</label>
                      <input type="text" class="form-control" v-model="pat.enfermedad" placeholder="Ej: Hepatitis, Tuberculosis, COVID grave..." list="enfermedadesComunes" />
                      <datalist id="enfermedadesComunes">
                        <option value="Hepatitis"></option>
                        <option value="Tuberculosis"></option>
                        <option value="Neumonía"></option>
                        <option value="COVID grave"></option>
                        <option value="Infecciones graves"></option>
                        <option value="Enfermedades cardíacas previas"></option>
                      </datalist>
                    </div>
                    <div class="form-group col-md-4">
                      <label class="font-weight-bold">Año aproximado</label>
                      <input type="number" class="form-control" v-model="pat.anio" placeholder="Ej: 2019" />
                    </div>
                  </div>
                </div>
                <button type="button" class="btn btn-outline-dark btn-sm mt-2" @click="agregarPatologico">
                  <font-awesome-icon icon="plus"></font-awesome-icon> Añadir otro antecedente
                </button>
              </div>
            </div>
          </div>

          <div class="card mb-3 border-secondary">
            <div class="card-header bg-secondary text-white font-weight-bold">
              <font-awesome-icon icon="smoking"></font-awesome-icon> Hábitos, Consumo y Otros
            </div>
            <div class="card-body">
              <div class="row">
                
                <div class="form-group col-md-6 border-bottom pb-3">
                  <label class="font-weight-bold">Consumo de Tabaco</label>
                  <select class="form-control" v-model="formHabitos.tabaco">
                    <option value="No fuma">No fuma</option>
                    <option value="Fuma actualmente">Fuma actualmente</option>
                    <option value="Exfumador">Exfumador</option>
                  </select>
                  
                  <div class="mt-2 p-2 bg-light rounded border border-danger" v-if="formHabitos.tabaco === 'Fuma actualmente'">
                    <label class="font-weight-bold text-danger" style="font-size: 0.9em;">Frecuencia de consumo</label>
                    <select class="form-control form-control-sm" v-model="formHabitos.tabacoFrecuencia">
                      <option value="null">-- Seleccione --</option>
                      <option value="Ocasional">Ocasional</option>
                      <option value="1-5 cigarrillos al día">1-5 cigarrillos al día</option>
                      <option value="6-10 cigarrillos al día">6-10 cigarrillos al día</option>
                      <option value="Más de 10 al día">Más de 10 al día</option>
                    </select>
                  </div>
                </div>

                <div class="form-group col-md-6 border-bottom pb-3">
                  <label class="font-weight-bold">Consumo de Alcohol</label>
                  <select class="form-control" v-model="formHabitos.alcohol">
                    <option value="No">No</option>
                    <option value="Ocasional">Ocasional</option>
                    <option value="Regular">Regular</option>
                  </select>

                  <div class="mt-2 p-2 bg-light rounded border border-warning" v-if="formHabitos.alcohol === 'Ocasional' || formHabitos.alcohol === 'Regular'">
                    <label class="font-weight-bold text-warning" style="font-size: 0.9em; color: #d39e00 !important;">Frecuencia</label>
                    <select class="form-control form-control-sm" v-model="formHabitos.alcoholFrecuencia">
                      <option value="null">-- Seleccione --</option>
                      <option value="Solo en eventos sociales">Solo en eventos sociales</option>
                      <option value="1-2 veces por semana">1-2 veces por semana</option>
                      <option value="3-5 veces por semana">3-5 veces por semana</option>
                      <option value="Diario">Diario</option>
                    </select>
                  </div>
                </div>

                <div class="form-group col-md-12 border-bottom pb-3">
                  <label class="font-weight-bold">Consumo de Drogas</label>
                  <select class="form-control" v-model="formHabitos.drogas">
                    <option value="No">No</option>
                    <option value="Sí">Sí</option>
                    <option value="Prefiere no responder">Prefiere no responder</option>
                  </select>

                  <div class="row mt-2 p-2 mx-0 bg-light rounded border border-dark" v-if="formHabitos.drogas === 'Sí'">
                    <div class="col-md-6">
                      <label class="font-weight-bold text-dark" style="font-size: 0.9em;">Tipo de droga</label>
                      <input type="text" class="form-control form-control-sm" v-model="formHabitos.drogasTipo" placeholder="Ej: Marihuana, Cocaína..." />
                    </div>
                    <div class="col-md-6">
                      <label class="font-weight-bold text-dark" style="font-size: 0.9em;">Frecuencia</label>
                      <select class="form-control form-control-sm" v-model="formHabitos.drogasFrecuencia">
                        <option value="null">-- Seleccione --</option>
                        <option value="Ocasional">Ocasional</option>
                        <option value="Semanal">Semanal</option>
                        <option value="Frecuente">Frecuente</option>
                      </select>
                    </div>
                  </div>
                </div>

                <div class="form-group col-md-6 border-bottom pb-3">
                  <label class="font-weight-bold">Esquema de Vacunación</label>
                  <select class="form-control" v-model="formHabitos.esquemaVacunacion">
                    <option value="Completo">Completo</option>
                    <option value="Incompleto">Incompleto</option>
                    <option value="Desconoce">Desconoce</option>
                  </select>
                  
                  <div class="mt-2">
                    <label class="font-weight-bold text-muted" style="font-size: 0.9em;">Vacunas recientes (Opcional)</label>
                    <input type="text" class="form-control form-control-sm" v-model="formHabitos.vacunasRecientes" placeholder="Ej: COVID-19, Influenza, Tétanos..." />
                  </div>
                </div>

                <div class="form-group col-md-6 border-bottom pb-3">
                  <label class="font-weight-bold">¿Presenta alguna discapacidad?</label>
                  <select class="form-control" v-model="formHabitos.tieneDiscapacidad">
                    <option value="No">No</option>
                    <option value="Sí">Sí</option>
                  </select>

                  <div class="mt-2 p-2 bg-light rounded border border-info" v-if="formHabitos.tieneDiscapacidad === 'Sí'">
                    <label class="font-weight-bold text-info" style="font-size: 0.9em;">Tipo de discapacidad</label>
                    <select class="form-control form-control-sm" v-model="formHabitos.tipoDiscapacidad">
                      <option value="null">-- Seleccione --</option>
                      <option value="Motriz">Motriz</option>
                      <option value="Visual">Visual</option>
                      <option value="Auditiva">Auditiva</option>
                      <option value="Intelectual">Intelectual</option>
                      <option value="Psicosocial">Psicosocial</option>
                      <option value="Otra">Otra</option>
                    </select>
                  </div>
                </div>

                <div class="form-group col-12 mb-0">
                  <label class="font-weight-bold">Observaciones Médicas Generales</label>
                  <textarea class="form-control" v-model="historialMedico.observacionesGenerales" rows="3" placeholder="Ej: Paciente con buena condición general de salud..."></textarea>
                </div>

              </div>
            </div>
          </div>

        </div>
        
        <div class="mt-4" v-if="pacienteEncontrado || historialMedico.id">
          <button type="button" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancelar</span>
          </button>
          <button type="submit" :disabled="isSaving || (!pacienteEncontrado && !historialMedico.id)" class="btn btn-primary">
              <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Guardar Historial</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts" src="./historial-medico-update.component.ts"></script>