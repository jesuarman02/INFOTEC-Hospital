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
                <label class="form-control-label">Altura (m)</label>
                <input type="number" step="0.01" class="form-control" v-model="historialMedico.altura" @input="calcularIMC" />
              </div>
              <div class="form-group col-md-4">
                <label class="form-control-label">Peso (kg)</label>
                <input type="number" step="0.1" class="form-control" v-model="historialMedico.peso" @input="calcularIMC" />
              </div>
              <div class="form-group col-md-4">
                <label class="form-control-label">IMC</label>
                <input type="text" class="form-control" v-model="historialMedico.imc" readonly style="background-color: #e9ecef;" />
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

          <div class="card mb-3">
            <div class="card-header bg-secondary text-white">Antecedentes Clínicos</div>
            <div class="card-body row">
              <div class="form-group col-md-6">
                <label class="form-control-label">Alergias</label>
                <textarea class="form-control" v-model="historialMedico.alergias" rows="2"></textarea>
              </div>
              <div class="form-group col-md-6">
                <label class="form-control-label">Enfermedades Crónicas</label>
                <textarea class="form-control" v-model="historialMedico.enfermedadesCronicas" rows="2"></textarea>
              </div>
              <div class="form-group col-md-6">
                <label class="form-control-label">Cirugías Previas</label>
                <textarea class="form-control" v-model="historialMedico.cirugiasPrevias" rows="2"></textarea>
              </div>
              <div class="form-group col-md-6">
                <label class="form-control-label">Medicamentos Actuales</label>
                <textarea class="form-control" v-model="historialMedico.medicamentosActuales" rows="2"></textarea>
              </div>
            </div>
          </div>

          <div class="card mb-3">
            <div class="card-header bg-secondary text-white">Antecedentes Personales y Familiares</div>
            <div class="card-body">
              <div class="form-group">
                <label class="form-control-label">Antecedentes Familiares</label>
                <textarea class="form-control" v-model="historialMedico.antecedentesFamiliares" rows="2"></textarea>
              </div>
              <div class="form-group">
                <label class="form-control-label">Antecedentes Personales Patológicos</label>
                <textarea class="form-control" v-model="historialMedico.antecedentesPatologicos" rows="2"></textarea>
              </div>
              <div class="form-group">
                <label class="form-control-label">Antecedentes Personales No Patológicos</label>
                <textarea class="form-control" v-model="historialMedico.antecedentesNoPatologicos" rows="2"></textarea>
              </div>
              <div class="form-group">
                <label class="form-control-label">Antecedentes Gineco-Obstétricos</label>
                <textarea class="form-control" v-model="historialMedico.antecedentesGinecoObstetricos" rows="2" placeholder="Llenar solo si aplica"></textarea>
              </div>
            </div>
          </div>

          <div class="card mb-3">
            <div class="card-header bg-warning text-dark">Hábitos, Consumo y Otros</div>
            <div class="card-body row">
              <div class="form-group col-md-4">
                <label class="form-control-label">Consumo de Tabaco</label>
                <select class="form-control" v-model="historialMedico.consumoTabaco">
                  <option :value="true">Sí</option>
                  <option :value="false">No</option>
                </select>
              </div>
              <div class="form-group col-md-4">
                <label class="form-control-label">Consumo de Alcohol</label>
                <select class="form-control" v-model="historialMedico.consumoAlcohol">
                  <option :value="true">Sí</option>
                  <option :value="false">No</option>
                </select>
              </div>
              <div class="form-group col-md-4">
                <label class="form-control-label">Consumo de Drogas</label>
                <select class="form-control" v-model="historialMedico.consumoDrogas">
                  <option :value="true">Sí</option>
                  <option :value="false">No</option>
                </select>
              </div>
              <div class="form-group col-md-6">
                <label class="form-control-label">Esquema de Vacunación / Vacunas</label>
                <textarea class="form-control" v-model="historialMedico.vacunas" rows="2"></textarea>
              </div>
              <div class="form-group col-md-6">
                <label class="form-control-label">Discapacidad</label>
                <textarea class="form-control" v-model="historialMedico.discapacidad" rows="2" placeholder="Especifique si aplica"></textarea>
              </div>
              <div class="form-group col-12">
                <label class="form-control-label">Observaciones Generales</label>
                <textarea class="form-control" v-model="historialMedico.observacionesGenerales" rows="3"></textarea>
              </div>
            </div>
          </div>

        </div>
        
        <div class="mt-4" v-if="pacienteEncontrado || historialMedico.id">
          <button type="button" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancelar</span>
          </button>
          <button type="submit" :disabled="isSaving || !pacienteEncontrado" class="btn btn-primary">
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Guardar Historial</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts" src="./historial-medico-update.component.ts"></script>