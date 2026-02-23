<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsPaciente.home.createOrEditLabel"
          data-cy="PacienteCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsPaciente.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="paciente.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="paciente.id" readonly />
          </div>
          
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.ecu')" for="paciente-ecu"></label>
            <input
              type="number"
              class="form-control"
              name="ecu"
              id="paciente-ecu"
              data-cy="ecu"
              :class="{ valid: !v$.ecu.$invalid, invalid: v$.ecu.$invalid }"
              v-model.number="v$.ecu.$model"
              required
            />
            <div v-if="v$.ecu.$anyDirty && v$.ecu.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ecu.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.nombre')" for="paciente-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="paciente-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              @input="onInputUpper($event, v$.nombre)"              
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">
                {{ error.$validator === 'soloLetras' ? 'Solo se permiten letras y espacios' : error.$message }}
              </small>
            </div>
          </div>
          
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPaciente.apellidoPaterno')"
              for="paciente-apellidoPaterno"
            ></label>
            <input
              type="text"
              class="form-control"
              name="apellidoPaterno"
              id="paciente-apellidoPaterno"
              data-cy="apellidoPaterno"
              :class="{ valid: !v$.apellidoPaterno.$invalid, invalid: v$.apellidoPaterno.$invalid }"
              v-model="v$.apellidoPaterno.$model"
              @input="onInputUpper($event, v$.apellidoPaterno)"
              required
            />
            <div v-if="v$.apellidoPaterno.$anyDirty && v$.apellidoPaterno.$invalid">
              <small class="form-text text-danger" v-for="error of v$.apellidoPaterno.$errors" :key="error.$uid">
                {{ error.$validator === 'soloLetras' ? 'Solo se permiten letras y espacios' : error.$message }}
              </small>
            </div>
          </div>
          
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPaciente.apellidoMaterno')"
              for="paciente-apellidoMaterno"
            ></label>
            <input
              type="text"
              class="form-control"
              name="apellidoMaterno"
              id="paciente-apellidoMaterno"
              data-cy="apellidoMaterno"
              :class="{ valid: !v$.apellidoMaterno.$invalid, invalid: v$.apellidoMaterno.$invalid }"
              v-model="v$.apellidoMaterno.$model"
              @input="onInputUpper($event, v$.apellidoMaterno)"
            />
            <div v-if="v$.apellidoMaterno.$anyDirty && v$.apellidoMaterno.$invalid">
              <small class="form-text text-danger" v-for="error of v$.apellidoMaterno.$errors" :key="error.$uid">
                {{ error.$validator === 'soloLetras' ? 'Solo se permiten letras y espacios' : error.$message }}
              </small>
            </div>
          </div>
          
          <div class="form-group">
            <label class="form-control-label d-block" v-text="t$('gatewayApp.pacientemsPaciente.sexo')"></label>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="sexo" id="sexoH" value="H" v-model="v$.sexo.$model" required>
              <label class="form-check-label" for="sexoH">Hombre</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="sexo" id="sexoM" value="M" v-model="v$.sexo.$model" required>
              <label class="form-check-label" for="sexoM">Mujer</label>
            </div>
            <div v-if="v$.sexo.$anyDirty && v$.sexo.$invalid" class="d-block mt-1">
              <small class="form-text text-danger" v-for="error of v$.sexo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          
          <div class="form-group mt-3">
            <label class="form-control-label d-block" v-text="t$('gatewayApp.pacientemsPaciente.nacionalidad')"></label>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="nacionalidad" id="nacMexicana" value="MEXICANA" v-model="v$.nacionalidad.$model">
              <label class="form-check-label" for="nacMexicana">Mexicana</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="nacionalidad" id="nacExtranjera" value="EXTRANJERA" v-model="v$.nacionalidad.$model">
              <label class="form-check-label" for="nacExtranjera">Extranjera</label>
            </div>
            <div v-if="v$.nacionalidad.$anyDirty && v$.nacionalidad.$invalid" class="d-block mt-1">
              <small class="form-text text-danger" v-for="error of v$.nacionalidad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          
          <div class="form-group mt-3">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsPaciente.fechaNacimiento')"
              for="paciente-fechaNacimiento"
            ></label>
            <input
              type="date"
              class="form-control"
              name="fechaNacimiento"
              id="paciente-fechaNacimiento"
              data-cy="fechaNacimiento"
              :class="{ valid: !v$.fechaNacimiento.$invalid, invalid: v$.fechaNacimiento.$invalid }"
              v-model="v$.fechaNacimiento.$model"
              required
            />
            <div v-if="v$.fechaNacimiento.$anyDirty && v$.fechaNacimiento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaNacimiento.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.estadoCivil')" for="paciente-estadoCivil"></label>
            <select
              class="form-control"
              name="estadoCivil"
              :class="{ valid: !v$.estadoCivil.$invalid, invalid: v$.estadoCivil.$invalid }"
              v-model="v$.estadoCivil.$model"
              id="paciente-estadoCivil"
              data-cy="estadoCivil"
            >
              <option value="" disabled selected>-- Estado civil --</option>
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
            <div v-if="v$.estadoCivil.$anyDirty && v$.estadoCivil.$invalid">
              <small class="form-text text-danger" v-for="error of v$.estadoCivil.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientemsPaciente.curp')" for="paciente-curp"></label>
            <input
              type="text"
              class="form-control"
              name="curp"
              id="paciente-curp"
              data-cy="curp"
              style="text-transform: uppercase;"
              :class="{ valid: !v$.curp.$invalid, invalid: v$.curp.$invalid }"
              v-model="v$.curp.$model"
              @input="onInputUpper($event, v$.curp)"
              required
            />
            <div v-if="v$.curp.$anyDirty && v$.curp.$invalid">
              <small class="form-text text-danger" v-for="error of v$.curp.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>

        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./paciente-update.component.ts"></script>