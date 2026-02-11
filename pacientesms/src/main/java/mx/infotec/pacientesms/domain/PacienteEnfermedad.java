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
 * A PacienteEnfermedad.
 */
@Table("paciente_enfermedad")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteEnfermedad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("fecha_diagnostico")
    private LocalDate fechaDiagnostico;

    @Size(max = 20)
    @Column("estado")
    private String estado;

    @Size(max = 255)
    @Column("notas")
    private String notas;

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
    private Enfermedad enfermedad;

    @Column("paciente_id")
    private Long pacienteId;

    @Column("enfermedad_id")
    private Long enfermedadId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PacienteEnfermedad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaDiagnostico() {
        return this.fechaDiagnostico;
    }

    public PacienteEnfermedad fechaDiagnostico(LocalDate fechaDiagnostico) {
        this.setFechaDiagnostico(fechaDiagnostico);
        return this;
    }

    public void setFechaDiagnostico(LocalDate fechaDiagnostico) {
        this.fechaDiagnostico = fechaDiagnostico;
    }

    public String getEstado() {
        return this.estado;
    }

    public PacienteEnfermedad estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return this.notas;
    }

    public PacienteEnfermedad notas(String notas) {
        this.setNotas(notas);
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        this.pacienteId = paciente != null ? paciente.getId() : null;
    }

    public PacienteEnfermedad paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Enfermedad getEnfermedad() {
        return this.enfermedad;
    }

    public void setEnfermedad(Enfermedad enfermedad) {
        this.enfermedad = enfermedad;
        this.enfermedadId = enfermedad != null ? enfermedad.getId() : null;
    }

    public PacienteEnfermedad enfermedad(Enfermedad enfermedad) {
        this.setEnfermedad(enfermedad);
        return this;
    }

    public Long getPacienteId() {
        return this.pacienteId;
    }

    public void setPacienteId(Long paciente) {
        this.pacienteId = paciente;
    }

    public Long getEnfermedadId() {
        return this.enfermedadId;
    }

    public void setEnfermedadId(Long enfermedad) {
        this.enfermedadId = enfermedad;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteEnfermedad)) {
            return false;
        }
        return getId() != null && getId().equals(((PacienteEnfermedad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteEnfermedad{" +
            "id=" + getId() +
            ", fechaDiagnostico='" + getFechaDiagnostico() + "'" +
            ", estado='" + getEstado() + "'" +
            ", notas='" + getNotas() + "'" +
            "}";
    }
}
