package mx.infotec.pacientesms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SignosVitales.
 */
@Table("signos_vitales")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignosVitales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("fecha_registro")
    private Instant fechaRegistro;

    @Column("frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Size(max = 10)
    @Column("tension_arterial")
    private String tensionArterial;

    @Column("frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @Column("temperatura")
    private BigDecimal temperatura;

    @Column("saturacion_oxigeno")
    private Integer saturacionOxigeno;

    @Column("peso")
    private BigDecimal peso;

    @Column("estatura")
    private BigDecimal estatura;

    @Column("imc")
    private BigDecimal imc;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "direccion",
            "infoSocioeconomica",
            "historialGeneral",
            "signosVitales",
            "misEnfermedades",
            "misAlergias",
            "misMedicamentos",
            "entidadNacimiento",
        },
        allowSetters = true
    )
    private Paciente paciente;

    @Column("paciente_id")
    private Long pacienteId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SignosVitales id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public SignosVitales fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getFrecuenciaCardiaca() {
        return this.frecuenciaCardiaca;
    }

    public SignosVitales frecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.setFrecuenciaCardiaca(frecuenciaCardiaca);
        return this;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public String getTensionArterial() {
        return this.tensionArterial;
    }

    public SignosVitales tensionArterial(String tensionArterial) {
        this.setTensionArterial(tensionArterial);
        return this;
    }

    public void setTensionArterial(String tensionArterial) {
        this.tensionArterial = tensionArterial;
    }

    public Integer getFrecuenciaRespiratoria() {
        return this.frecuenciaRespiratoria;
    }

    public SignosVitales frecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.setFrecuenciaRespiratoria(frecuenciaRespiratoria);
        return this;
    }

    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public BigDecimal getTemperatura() {
        return this.temperatura;
    }

    public SignosVitales temperatura(BigDecimal temperatura) {
        this.setTemperatura(temperatura);
        return this;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura != null ? temperatura.stripTrailingZeros() : null;
    }

    public Integer getSaturacionOxigeno() {
        return this.saturacionOxigeno;
    }

    public SignosVitales saturacionOxigeno(Integer saturacionOxigeno) {
        this.setSaturacionOxigeno(saturacionOxigeno);
        return this;
    }

    public void setSaturacionOxigeno(Integer saturacionOxigeno) {
        this.saturacionOxigeno = saturacionOxigeno;
    }

    public BigDecimal getPeso() {
        return this.peso;
    }

    public SignosVitales peso(BigDecimal peso) {
        this.setPeso(peso);
        return this;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso != null ? peso.stripTrailingZeros() : null;
    }

    public BigDecimal getEstatura() {
        return this.estatura;
    }

    public SignosVitales estatura(BigDecimal estatura) {
        this.setEstatura(estatura);
        return this;
    }

    public void setEstatura(BigDecimal estatura) {
        this.estatura = estatura != null ? estatura.stripTrailingZeros() : null;
    }

    public BigDecimal getImc() {
        return this.imc;
    }

    public SignosVitales imc(BigDecimal imc) {
        this.setImc(imc);
        return this;
    }

    public void setImc(BigDecimal imc) {
        this.imc = imc != null ? imc.stripTrailingZeros() : null;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        this.pacienteId = paciente != null ? paciente.getId() : null;
    }

    public SignosVitales paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Long getPacienteId() {
        return this.pacienteId;
    }

    public void setPacienteId(Long paciente) {
        this.pacienteId = paciente;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignosVitales)) {
            return false;
        }
        return getId() != null && getId().equals(((SignosVitales) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignosVitales{" +
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
            "}";
    }
}
