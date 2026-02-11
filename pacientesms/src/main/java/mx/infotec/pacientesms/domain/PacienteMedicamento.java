package mx.infotec.pacientesms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PacienteMedicamento.
 */
@Table("paciente_medicamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteMedicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Size(max = 50)
    @Column("dosis")
    private String dosis;

    @Size(max = 50)
    @Column("frecuencia")
    private String frecuencia;

    @NotNull(message = "must not be null")
    @Column("fecha_inicio")
    private LocalDate fechaInicio;

    @Column("fecha_fin")
    private LocalDate fechaFin;

    @Column("activo")
    private Boolean activo;

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

    @Transient
    @JsonIgnoreProperties(value = { "pacientes" }, allowSetters = true)
    private Medicamento medicamento;

    @Column("paciente_id")
    private Long pacienteId;

    @Column("medicamento_id")
    private Long medicamentoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PacienteMedicamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDosis() {
        return this.dosis;
    }

    public PacienteMedicamento dosis(String dosis) {
        this.setDosis(dosis);
        return this;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return this.frecuencia;
    }

    public PacienteMedicamento frecuencia(String frecuencia) {
        this.setFrecuencia(frecuencia);
        return this;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public PacienteMedicamento fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public PacienteMedicamento fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public PacienteMedicamento activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        this.pacienteId = paciente != null ? paciente.getId() : null;
    }

    public PacienteMedicamento paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Medicamento getMedicamento() {
        return this.medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
        this.medicamentoId = medicamento != null ? medicamento.getId() : null;
    }

    public PacienteMedicamento medicamento(Medicamento medicamento) {
        this.setMedicamento(medicamento);
        return this;
    }

    public Long getPacienteId() {
        return this.pacienteId;
    }

    public void setPacienteId(Long paciente) {
        this.pacienteId = paciente;
    }

    public Long getMedicamentoId() {
        return this.medicamentoId;
    }

    public void setMedicamentoId(Long medicamento) {
        this.medicamentoId = medicamento;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteMedicamento)) {
            return false;
        }
        return getId() != null && getId().equals(((PacienteMedicamento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteMedicamento{" +
            "id=" + getId() +
            ", dosis='" + getDosis() + "'" +
            ", frecuencia='" + getFrecuencia() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
