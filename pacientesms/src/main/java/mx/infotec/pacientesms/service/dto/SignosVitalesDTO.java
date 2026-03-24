package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.SignosVitales} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignosVitalesDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant fechaRegistro;

    private Integer frecuenciaCardiaca;

    @Size(max = 10)
    private String tensionArterial;

    private Integer frecuenciaRespiratoria;

    private BigDecimal temperatura;

    private Integer saturacionOxigeno;

    // --- NUEVAS COLUMNAS (IDENTIFICACIÓN DEL PACIENTE) ---
    private Integer pacienteEcu;
    private String pacienteNombre;
    private String pacienteApellidoPaterno;

    // --- NUEVAS COLUMNAS (CONTEXTO Y EVALUACIÓN) ---
    private String tipo;
    private String personal;
    private Integer glucosa;
    private Integer dolor;
    private String estadoConciencia;
    private String observaciones;

    // --- RELACIÓN ORIGINAL JHIPSTER ---
    private PacienteDTO paciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public String getTensionArterial() {
        return tensionArterial;
    }

    public void setTensionArterial(String tensionArterial) {
        this.tensionArterial = tensionArterial;
    }

    public Integer getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria;
    }

    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getSaturacionOxigeno() {
        return saturacionOxigeno;
    }

    public void setSaturacionOxigeno(Integer saturacionOxigeno) {
        this.saturacionOxigeno = saturacionOxigeno;
    }

    // --- GETTERS Y SETTERS NUEVOS ---

    public Integer getPacienteEcu() { return pacienteEcu; }
    public void setPacienteEcu(Integer pacienteEcu) { this.pacienteEcu = pacienteEcu; }

    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }

    public String getPacienteApellidoPaterno() { return pacienteApellidoPaterno; }
    public void setPacienteApellidoPaterno(String pacienteApellidoPaterno) { this.pacienteApellidoPaterno = pacienteApellidoPaterno; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getPersonal() { return personal; }
    public void setPersonal(String personal) { this.personal = personal; }

    public Integer getGlucosa() { return glucosa; }
    public void setGlucosa(Integer glucosa) { this.glucosa = glucosa; }

    public Integer getDolor() { return dolor; }
    public void setDolor(Integer dolor) { this.dolor = dolor; }

    public String getEstadoConciencia() { return estadoConciencia; }
    public void setEstadoConciencia(String estadoConciencia) { this.estadoConciencia = estadoConciencia; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignosVitalesDTO)) {
            return false;
        }

        SignosVitalesDTO signosVitalesDTO = (SignosVitalesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, signosVitalesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignosVitalesDTO{" +
            "id=" + getId() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", frecuenciaCardiaca=" + getFrecuenciaCardiaca() +
            ", tensionArterial='" + getTensionArterial() + "'" +
            ", frecuenciaRespiratoria=" + getFrecuenciaRespiratoria() +
            ", temperatura=" + getTemperatura() +
            ", saturacionOxigeno=" + getSaturacionOxigeno() +
            ", pacienteEcu=" + getPacienteEcu() +
            ", pacienteNombre='" + getPacienteNombre() + "'" +
            ", pacienteApellidoPaterno='" + getPacienteApellidoPaterno() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", personal='" + getPersonal() + "'" +
            ", glucosa=" + getGlucosa() +
            ", dolor=" + getDolor() +
            ", estadoConciencia='" + getEstadoConciencia() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", paciente=" + getPaciente() +
            "}";
    }
}