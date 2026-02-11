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

    private BigDecimal peso;

    private BigDecimal estatura;

    private BigDecimal imc;

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

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getEstatura() {
        return estatura;
    }

    public void setEstatura(BigDecimal estatura) {
        this.estatura = estatura;
    }

    public BigDecimal getImc() {
        return imc;
    }

    public void setImc(BigDecimal imc) {
        this.imc = imc;
    }

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
            ", peso=" + getPeso() +
            ", estatura=" + getEstatura() +
            ", imc=" + getImc() +
            ", paciente=" + getPaciente() +
            "}";
    }
}
