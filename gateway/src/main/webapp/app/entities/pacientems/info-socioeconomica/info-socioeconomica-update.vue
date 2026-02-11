<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientemsInfoSocioeconomica.home.createOrEditLabel"
          data-cy="InfoSocioeconomicaCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="infoSocioeconomica.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="infoSocioeconomica.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tipoVivienda')"
              for="info-socioeconomica-tipoVivienda"
            ></label>
            <input
              type="text"
              class="form-control"
              name="tipoVivienda"
              id="info-socioeconomica-tipoVivienda"
              data-cy="tipoVivienda"
              :class="{ valid: !v$.tipoVivienda.$invalid, invalid: v$.tipoVivienda.$invalid }"
              v-model="v$.tipoVivienda.$model"
            />
            <div v-if="v$.tipoVivienda.$anyDirty && v$.tipoVivienda.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipoVivienda.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.materialVivienda')"
              for="info-socioeconomica-materialVivienda"
            ></label>
            <input
              type="text"
              class="form-control"
              name="materialVivienda"
              id="info-socioeconomica-materialVivienda"
              data-cy="materialVivienda"
              :class="{ valid: !v$.materialVivienda.$invalid, invalid: v$.materialVivienda.$invalid }"
              v-model="v$.materialVivienda.$model"
            />
            <div v-if="v$.materialVivienda.$anyDirty && v$.materialVivienda.$invalid">
              <small class="form-text text-danger" v-for="error of v$.materialVivienda.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.numeroHabitaciones')"
              for="info-socioeconomica-numeroHabitaciones"
            ></label>
            <input
              type="number"
              class="form-control"
              name="numeroHabitaciones"
              id="info-socioeconomica-numeroHabitaciones"
              data-cy="numeroHabitaciones"
              :class="{ valid: !v$.numeroHabitaciones.$invalid, invalid: v$.numeroHabitaciones.$invalid }"
              v-model.number="v$.numeroHabitaciones.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.numeroHabitantes')"
              for="info-socioeconomica-numeroHabitantes"
            ></label>
            <input
              type="number"
              class="form-control"
              name="numeroHabitantes"
              id="info-socioeconomica-numeroHabitantes"
              data-cy="numeroHabitantes"
              :class="{ valid: !v$.numeroHabitantes.$invalid, invalid: v$.numeroHabitantes.$invalid }"
              v-model.number="v$.numeroHabitantes.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.serviciosDisponibles')"
              for="info-socioeconomica-serviciosDisponibles"
            ></label>
            <input
              type="text"
              class="form-control"
              name="serviciosDisponibles"
              id="info-socioeconomica-serviciosDisponibles"
              data-cy="serviciosDisponibles"
              :class="{ valid: !v$.serviciosDisponibles.$invalid, invalid: v$.serviciosDisponibles.$invalid }"
              v-model="v$.serviciosDisponibles.$model"
            />
            <div v-if="v$.serviciosDisponibles.$anyDirty && v$.serviciosDisponibles.$invalid">
              <small class="form-text text-danger" v-for="error of v$.serviciosDisponibles.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.ingresoMensual')"
              for="info-socioeconomica-ingresoMensual"
            ></label>
            <input
              type="number"
              class="form-control"
              name="ingresoMensual"
              id="info-socioeconomica-ingresoMensual"
              data-cy="ingresoMensual"
              :class="{ valid: !v$.ingresoMensual.$invalid, invalid: v$.ingresoMensual.$invalid }"
              v-model.number="v$.ingresoMensual.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.ingresoMensualHogar')"
              for="info-socioeconomica-ingresoMensualHogar"
            ></label>
            <input
              type="number"
              class="form-control"
              name="ingresoMensualHogar"
              id="info-socioeconomica-ingresoMensualHogar"
              data-cy="ingresoMensualHogar"
              :class="{ valid: !v$.ingresoMensualHogar.$invalid, invalid: v$.ingresoMensualHogar.$invalid }"
              v-model.number="v$.ingresoMensualHogar.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.gastoMensual')"
              for="info-socioeconomica-gastoMensual"
            ></label>
            <input
              type="number"
              class="form-control"
              name="gastoMensual"
              id="info-socioeconomica-gastoMensual"
              data-cy="gastoMensual"
              :class="{ valid: !v$.gastoMensual.$invalid, invalid: v$.gastoMensual.$invalid }"
              v-model.number="v$.gastoMensual.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.personasDependientes')"
              for="info-socioeconomica-personasDependientes"
            ></label>
            <input
              type="number"
              class="form-control"
              name="personasDependientes"
              id="info-socioeconomica-personasDependientes"
              data-cy="personasDependientes"
              :class="{ valid: !v$.personasDependientes.$invalid, invalid: v$.personasDependientes.$invalid }"
              v-model.number="v$.personasDependientes.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.apoyosGubernamentales')"
              for="info-socioeconomica-apoyosGubernamentales"
            ></label>
            <input
              type="text"
              class="form-control"
              name="apoyosGubernamentales"
              id="info-socioeconomica-apoyosGubernamentales"
              data-cy="apoyosGubernamentales"
              :class="{ valid: !v$.apoyosGubernamentales.$invalid, invalid: v$.apoyosGubernamentales.$invalid }"
              v-model="v$.apoyosGubernamentales.$model"
            />
            <div v-if="v$.apoyosGubernamentales.$anyDirty && v$.apoyosGubernamentales.$invalid">
              <small class="form-text text-danger" v-for="error of v$.apoyosGubernamentales.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.ocupacionActual')"
              for="info-socioeconomica-ocupacionActual"
            ></label>
            <input
              type="text"
              class="form-control"
              name="ocupacionActual"
              id="info-socioeconomica-ocupacionActual"
              data-cy="ocupacionActual"
              :class="{ valid: !v$.ocupacionActual.$invalid, invalid: v$.ocupacionActual.$invalid }"
              v-model="v$.ocupacionActual.$model"
            />
            <div v-if="v$.ocupacionActual.$anyDirty && v$.ocupacionActual.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ocupacionActual.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.condicionLaboral')"
              for="info-socioeconomica-condicionLaboral"
            ></label>
            <input
              type="text"
              class="form-control"
              name="condicionLaboral"
              id="info-socioeconomica-condicionLaboral"
              data-cy="condicionLaboral"
              :class="{ valid: !v$.condicionLaboral.$invalid, invalid: v$.condicionLaboral.$invalid }"
              v-model="v$.condicionLaboral.$model"
            />
            <div v-if="v$.condicionLaboral.$anyDirty && v$.condicionLaboral.$invalid">
              <small class="form-text text-danger" v-for="error of v$.condicionLaboral.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tipoEmpleo')"
              for="info-socioeconomica-tipoEmpleo"
            ></label>
            <input
              type="text"
              class="form-control"
              name="tipoEmpleo"
              id="info-socioeconomica-tipoEmpleo"
              data-cy="tipoEmpleo"
              :class="{ valid: !v$.tipoEmpleo.$invalid, invalid: v$.tipoEmpleo.$invalid }"
              v-model="v$.tipoEmpleo.$model"
            />
            <div v-if="v$.tipoEmpleo.$anyDirty && v$.tipoEmpleo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipoEmpleo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.lugarTrabajo')"
              for="info-socioeconomica-lugarTrabajo"
            ></label>
            <input
              type="text"
              class="form-control"
              name="lugarTrabajo"
              id="info-socioeconomica-lugarTrabajo"
              data-cy="lugarTrabajo"
              :class="{ valid: !v$.lugarTrabajo.$invalid, invalid: v$.lugarTrabajo.$invalid }"
              v-model="v$.lugarTrabajo.$model"
            />
            <div v-if="v$.lugarTrabajo.$anyDirty && v$.lugarTrabajo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.lugarTrabajo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tiempoEmpleado')"
              for="info-socioeconomica-tiempoEmpleado"
            ></label>
            <input
              type="text"
              class="form-control"
              name="tiempoEmpleado"
              id="info-socioeconomica-tiempoEmpleado"
              data-cy="tiempoEmpleado"
              :class="{ valid: !v$.tiempoEmpleado.$invalid, invalid: v$.tiempoEmpleado.$invalid }"
              v-model="v$.tiempoEmpleado.$model"
            />
            <div v-if="v$.tiempoEmpleado.$anyDirty && v$.tiempoEmpleado.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tiempoEmpleado.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.gradoMaximoEstudios')"
              for="info-socioeconomica-gradoMaximoEstudios"
            ></label>
            <input
              type="text"
              class="form-control"
              name="gradoMaximoEstudios"
              id="info-socioeconomica-gradoMaximoEstudios"
              data-cy="gradoMaximoEstudios"
              :class="{ valid: !v$.gradoMaximoEstudios.$invalid, invalid: v$.gradoMaximoEstudios.$invalid }"
              v-model="v$.gradoMaximoEstudios.$model"
            />
            <div v-if="v$.gradoMaximoEstudios.$anyDirty && v$.gradoMaximoEstudios.$invalid">
              <small class="form-text text-danger" v-for="error of v$.gradoMaximoEstudios.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.aniosEstudio')"
              for="info-socioeconomica-aniosEstudio"
            ></label>
            <input
              type="number"
              class="form-control"
              name="aniosEstudio"
              id="info-socioeconomica-aniosEstudio"
              data-cy="aniosEstudio"
              :class="{ valid: !v$.aniosEstudio.$invalid, invalid: v$.aniosEstudio.$invalid }"
              v-model.number="v$.aniosEstudio.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.estudia')"
              for="info-socioeconomica-estudia"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="estudia"
              id="info-socioeconomica-estudia"
              data-cy="estudia"
              :class="{ valid: !v$.estudia.$invalid, invalid: v$.estudia.$invalid }"
              v-model="v$.estudia.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.institucionMedica')"
              for="info-socioeconomica-institucionMedica"
            ></label>
            <input
              type="text"
              class="form-control"
              name="institucionMedica"
              id="info-socioeconomica-institucionMedica"
              data-cy="institucionMedica"
              :class="{ valid: !v$.institucionMedica.$invalid, invalid: v$.institucionMedica.$invalid }"
              v-model="v$.institucionMedica.$model"
            />
            <div v-if="v$.institucionMedica.$anyDirty && v$.institucionMedica.$invalid">
              <small class="form-text text-danger" v-for="error of v$.institucionMedica.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tipoAfiliacion')"
              for="info-socioeconomica-tipoAfiliacion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="tipoAfiliacion"
              id="info-socioeconomica-tipoAfiliacion"
              data-cy="tipoAfiliacion"
              :class="{ valid: !v$.tipoAfiliacion.$invalid, invalid: v$.tipoAfiliacion.$invalid }"
              v-model="v$.tipoAfiliacion.$model"
            />
            <div v-if="v$.tipoAfiliacion.$anyDirty && v$.tipoAfiliacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipoAfiliacion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.numeroAfiliacion')"
              for="info-socioeconomica-numeroAfiliacion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="numeroAfiliacion"
              id="info-socioeconomica-numeroAfiliacion"
              data-cy="numeroAfiliacion"
              :class="{ valid: !v$.numeroAfiliacion.$invalid, invalid: v$.numeroAfiliacion.$invalid }"
              v-model="v$.numeroAfiliacion.$model"
            />
            <div v-if="v$.numeroAfiliacion.$anyDirty && v$.numeroAfiliacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.numeroAfiliacion.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.medioTransporte')"
              for="info-socioeconomica-medioTransporte"
            ></label>
            <input
              type="text"
              class="form-control"
              name="medioTransporte"
              id="info-socioeconomica-medioTransporte"
              data-cy="medioTransporte"
              :class="{ valid: !v$.medioTransporte.$invalid, invalid: v$.medioTransporte.$invalid }"
              v-model="v$.medioTransporte.$model"
            />
            <div v-if="v$.medioTransporte.$anyDirty && v$.medioTransporte.$invalid">
              <small class="form-text text-danger" v-for="error of v$.medioTransporte.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.tiempoTraslado')"
              for="info-socioeconomica-tiempoTraslado"
            ></label>
            <input
              type="number"
              class="form-control"
              name="tiempoTraslado"
              id="info-socioeconomica-tiempoTraslado"
              data-cy="tiempoTraslado"
              :class="{ valid: !v$.tiempoTraslado.$invalid, invalid: v$.tiempoTraslado.$invalid }"
              v-model.number="v$.tiempoTraslado.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientemsInfoSocioeconomica.fechaActualizacion')"
              for="info-socioeconomica-fechaActualizacion"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="info-socioeconomica-fechaActualizacion"
                  v-model="v$.fechaActualizacion.$model"
                  name="fechaActualizacion"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="info-socioeconomica-fechaActualizacion"
                data-cy="fechaActualizacion"
                type="text"
                class="form-control"
                name="fechaActualizacion"
                :class="{ valid: !v$.fechaActualizacion.$invalid, invalid: v$.fechaActualizacion.$invalid }"
                v-model="v$.fechaActualizacion.$model"
              />
            </b-input-group>
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
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./info-socioeconomica-update.component.ts"></script>
