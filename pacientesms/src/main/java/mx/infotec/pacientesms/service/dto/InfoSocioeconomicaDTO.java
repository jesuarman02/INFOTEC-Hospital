package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.InfoSocioeconomica} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfoSocioeconomicaDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String tipoVivienda;

    @Size(max = 100)
    private String materialVivienda;

    private Integer numeroHabitaciones;

    private Integer numeroHabitantes;

    @Size(max = 255)
    private String serviciosDisponibles;

    private BigDecimal ingresoMensual;

    private BigDecimal ingresoMensualHogar;

    private BigDecimal gastoMensual;

    private Integer personasDependientes;

    @Size(max = 255)
    private String apoyosGubernamentales;

    @Size(max = 100)
    private String ocupacionActual;

    @Size(max = 50)
    private String condicionLaboral;

    @Size(max = 50)
    private String tipoEmpleo;

    @Size(max = 150)
    private String lugarTrabajo;

    @Size(max = 50)
    private String tiempoEmpleado;

    @Size(max = 50)
    private String gradoMaximoEstudios;

    private Integer aniosEstudio;

    private Boolean estudia;

    @Size(max = 100)
    private String institucionMedica;

    @Size(max = 50)
    private String tipoAfiliacion;

    @Size(max = 30)
    private String numeroAfiliacion;

    @Size(max = 50)
    private String medioTransporte;

    private Integer tiempoTraslado;

    private LocalDate fechaActualizacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoVivienda() {
        return tipoVivienda;
    }

    public void setTipoVivienda(String tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }

    public String getMaterialVivienda() {
        return materialVivienda;
    }

    public void setMaterialVivienda(String materialVivienda) {
        this.materialVivienda = materialVivienda;
    }

    public Integer getNumeroHabitaciones() {
        return numeroHabitaciones;
    }

    public void setNumeroHabitaciones(Integer numeroHabitaciones) {
        this.numeroHabitaciones = numeroHabitaciones;
    }

    public Integer getNumeroHabitantes() {
        return numeroHabitantes;
    }

    public void setNumeroHabitantes(Integer numeroHabitantes) {
        this.numeroHabitantes = numeroHabitantes;
    }

    public String getServiciosDisponibles() {
        return serviciosDisponibles;
    }

    public void setServiciosDisponibles(String serviciosDisponibles) {
        this.serviciosDisponibles = serviciosDisponibles;
    }

    public BigDecimal getIngresoMensual() {
        return ingresoMensual;
    }

    public void setIngresoMensual(BigDecimal ingresoMensual) {
        this.ingresoMensual = ingresoMensual;
    }

    public BigDecimal getIngresoMensualHogar() {
        return ingresoMensualHogar;
    }

    public void setIngresoMensualHogar(BigDecimal ingresoMensualHogar) {
        this.ingresoMensualHogar = ingresoMensualHogar;
    }

    public BigDecimal getGastoMensual() {
        return gastoMensual;
    }

    public void setGastoMensual(BigDecimal gastoMensual) {
        this.gastoMensual = gastoMensual;
    }

    public Integer getPersonasDependientes() {
        return personasDependientes;
    }

    public void setPersonasDependientes(Integer personasDependientes) {
        this.personasDependientes = personasDependientes;
    }

    public String getApoyosGubernamentales() {
        return apoyosGubernamentales;
    }

    public void setApoyosGubernamentales(String apoyosGubernamentales) {
        this.apoyosGubernamentales = apoyosGubernamentales;
    }

    public String getOcupacionActual() {
        return ocupacionActual;
    }

    public void setOcupacionActual(String ocupacionActual) {
        this.ocupacionActual = ocupacionActual;
    }

    public String getCondicionLaboral() {
        return condicionLaboral;
    }

    public void setCondicionLaboral(String condicionLaboral) {
        this.condicionLaboral = condicionLaboral;
    }

    public String getTipoEmpleo() {
        return tipoEmpleo;
    }

    public void setTipoEmpleo(String tipoEmpleo) {
        this.tipoEmpleo = tipoEmpleo;
    }

    public String getLugarTrabajo() {
        return lugarTrabajo;
    }

    public void setLugarTrabajo(String lugarTrabajo) {
        this.lugarTrabajo = lugarTrabajo;
    }

    public String getTiempoEmpleado() {
        return tiempoEmpleado;
    }

    public void setTiempoEmpleado(String tiempoEmpleado) {
        this.tiempoEmpleado = tiempoEmpleado;
    }

    public String getGradoMaximoEstudios() {
        return gradoMaximoEstudios;
    }

    public void setGradoMaximoEstudios(String gradoMaximoEstudios) {
        this.gradoMaximoEstudios = gradoMaximoEstudios;
    }

    public Integer getAniosEstudio() {
        return aniosEstudio;
    }

    public void setAniosEstudio(Integer aniosEstudio) {
        this.aniosEstudio = aniosEstudio;
    }

    public Boolean getEstudia() {
        return estudia;
    }

    public void setEstudia(Boolean estudia) {
        this.estudia = estudia;
    }

    public String getInstitucionMedica() {
        return institucionMedica;
    }

    public void setInstitucionMedica(String institucionMedica) {
        this.institucionMedica = institucionMedica;
    }

    public String getTipoAfiliacion() {
        return tipoAfiliacion;
    }

    public void setTipoAfiliacion(String tipoAfiliacion) {
        this.tipoAfiliacion = tipoAfiliacion;
    }

    public String getNumeroAfiliacion() {
        return numeroAfiliacion;
    }

    public void setNumeroAfiliacion(String numeroAfiliacion) {
        this.numeroAfiliacion = numeroAfiliacion;
    }

    public String getMedioTransporte() {
        return medioTransporte;
    }

    public void setMedioTransporte(String medioTransporte) {
        this.medioTransporte = medioTransporte;
    }

    public Integer getTiempoTraslado() {
        return tiempoTraslado;
    }

    public void setTiempoTraslado(Integer tiempoTraslado) {
        this.tiempoTraslado = tiempoTraslado;
    }

    public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoSocioeconomicaDTO)) {
            return false;
        }

        InfoSocioeconomicaDTO infoSocioeconomicaDTO = (InfoSocioeconomicaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, infoSocioeconomicaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoSocioeconomicaDTO{" +
            "id=" + getId() +
            ", tipoVivienda='" + getTipoVivienda() + "'" +
            ", materialVivienda='" + getMaterialVivienda() + "'" +
            ", numeroHabitaciones=" + getNumeroHabitaciones() +
            ", numeroHabitantes=" + getNumeroHabitantes() +
            ", serviciosDisponibles='" + getServiciosDisponibles() + "'" +
            ", ingresoMensual=" + getIngresoMensual() +
            ", ingresoMensualHogar=" + getIngresoMensualHogar() +
            ", gastoMensual=" + getGastoMensual() +
            ", personasDependientes=" + getPersonasDependientes() +
            ", apoyosGubernamentales='" + getApoyosGubernamentales() + "'" +
            ", ocupacionActual='" + getOcupacionActual() + "'" +
            ", condicionLaboral='" + getCondicionLaboral() + "'" +
            ", tipoEmpleo='" + getTipoEmpleo() + "'" +
            ", lugarTrabajo='" + getLugarTrabajo() + "'" +
            ", tiempoEmpleado='" + getTiempoEmpleado() + "'" +
            ", gradoMaximoEstudios='" + getGradoMaximoEstudios() + "'" +
            ", aniosEstudio=" + getAniosEstudio() +
            ", estudia='" + getEstudia() + "'" +
            ", institucionMedica='" + getInstitucionMedica() + "'" +
            ", tipoAfiliacion='" + getTipoAfiliacion() + "'" +
            ", numeroAfiliacion='" + getNumeroAfiliacion() + "'" +
            ", medioTransporte='" + getMedioTransporte() + "'" +
            ", tiempoTraslado=" + getTiempoTraslado() +
            ", fechaActualizacion='" + getFechaActualizacion() + "'" +
            "}";
    }
}
