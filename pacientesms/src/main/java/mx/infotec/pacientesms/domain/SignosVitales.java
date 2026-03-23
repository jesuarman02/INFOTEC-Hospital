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

    // --- NUEVAS COLUMNAS (IDENTIFICACIÓN DEL PACIENTE) ---
    @Column("paciente_ecu")
    private Integer pacienteEcu;

    @Column("paciente_nombre")
    private String pacienteNombre;

    @Column("paciente_apellido_paterno")
    private String pacienteApellidoPaterno;

    // --- NUEVAS COLUMNAS (CONTEXTO Y EVALUACIÓN) ---
    @Column("tipo")
    private String tipo;

    @Column("personal")
    private String personal;

    @Column("glucosa")
    private Integer glucosa;

    @Column("dolor")
    private Integer dolor;

    @Column("estado_conciencia")
    private String estadoConciencia;

    @Column("observaciones")
    private String observaciones;

    // --- RELACIÓN ORIGINAL DE JHIPSTER ---
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

    // --- GETTERS Y SETTERS NUEVOS ---

    public Integer getPacienteEcu() { return pacienteEcu; }
    public SignosVitales pacienteEcu(Integer pacienteEcu) { this.setPacienteEcu(pacienteEcu); return this; }
    public void setPacienteEcu(Integer pacienteEcu) { this.pacienteEcu = pacienteEcu; }

    public String getPacienteNombre() { return pacienteNombre; }
    public SignosVitales pacienteNombre(String pacienteNombre) { this.setPacienteNombre(pacienteNombre); return this; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }

    public String getPacienteApellidoPaterno() { return pacienteApellidoPaterno; }
    public SignosVitales pacienteApellidoPaterno(String pacienteApellidoPaterno) { this.setPacienteApellidoPaterno(pacienteApellidoPaterno); return this; }
    public void setPacienteApellidoPaterno(String pacienteApellidoPaterno) { this.pacienteApellidoPaterno = pacienteApellidoPaterno; }

    public String getTipo() { return tipo; }
    public SignosVitales tipo(String tipo) { this.setTipo(tipo); return this; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getPersonal() { return personal; }
    public SignosVitales personal(String personal) { this.setPersonal(personal); return this; }
    public void setPersonal(String personal) { this.personal = personal; }

    public Integer getGlucosa() { return glucosa; }
    public SignosVitales glucosa(Integer glucosa) { this.setGlucosa(glucosa); return this; }
    public void setGlucosa(Integer glucosa) { this.glucosa = glucosa; }

    public Integer getDolor() { return dolor; }
    public SignosVitales dolor(Integer dolor) { this.setDolor(dolor); return this; }
    public void setDolor(Integer dolor) { this.dolor = dolor; }

    public String getEstadoConciencia() { return estadoConciencia; }
    public SignosVitales estadoConciencia(String estadoConciencia) { this.setEstadoConciencia(estadoConciencia); return this; }
    public void setEstadoConciencia(String estadoConciencia) { this.estadoConciencia = estadoConciencia; }

    public String getObservaciones() { return observaciones; }
    public SignosVitales observaciones(String observaciones) { this.setObservaciones(observaciones); return this; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    // --- GETTERS Y SETTERS RELACIÓN PACIENTE ---

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
            ", pacienteEcu=" + getPacienteEcu() +
            ", pacienteNombre='" + getPacienteNombre() + "'" +
            ", pacienteApellidoPaterno='" + getPacienteApellidoPaterno() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", personal='" + getPersonal() + "'" +
            ", glucosa=" + getGlucosa() +
            ", dolor=" + getDolor() +
            ", estadoConciencia='" + getEstadoConciencia() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}