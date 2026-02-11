package mx.infotec.pacientesms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import mx.infotec.pacientesms.domain.enumeration.GradoAlergia;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PacienteAlergia.
 */
@Table("paciente_alergia")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteAlergia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("fecha_deteccion")
    private LocalDate fechaDeteccion;

    @Size(max = 150)
    @Column("reaccion")
    private String reaccion;

    @NotNull(message = "must not be null")
    @Column("gravedad")
    private GradoAlergia gravedad;

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
    private Alergia alergia;

    @Column("paciente_id")
    private Long pacienteId;

    @Column("alergia_id")
    private Long alergiaId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PacienteAlergia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaDeteccion() {
        return this.fechaDeteccion;
    }

    public PacienteAlergia fechaDeteccion(LocalDate fechaDeteccion) {
        this.setFechaDeteccion(fechaDeteccion);
        return this;
    }

    public void setFechaDeteccion(LocalDate fechaDeteccion) {
        this.fechaDeteccion = fechaDeteccion;
    }

    public String getReaccion() {
        return this.reaccion;
    }

    public PacienteAlergia reaccion(String reaccion) {
        this.setReaccion(reaccion);
        return this;
    }

    public void setReaccion(String reaccion) {
        this.reaccion = reaccion;
    }

    public GradoAlergia getGravedad() {
        return this.gravedad;
    }

    public PacienteAlergia gravedad(GradoAlergia gravedad) {
        this.setGravedad(gravedad);
        return this;
    }

    public void setGravedad(GradoAlergia gravedad) {
        this.gravedad = gravedad;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        this.pacienteId = paciente != null ? paciente.getId() : null;
    }

    public PacienteAlergia paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Alergia getAlergia() {
        return this.alergia;
    }

    public void setAlergia(Alergia alergia) {
        this.alergia = alergia;
        this.alergiaId = alergia != null ? alergia.getId() : null;
    }

    public PacienteAlergia alergia(Alergia alergia) {
        this.setAlergia(alergia);
        return this;
    }

    public Long getPacienteId() {
        return this.pacienteId;
    }

    public void setPacienteId(Long paciente) {
        this.pacienteId = paciente;
    }

    public Long getAlergiaId() {
        return this.alergiaId;
    }

    public void setAlergiaId(Long alergia) {
        this.alergiaId = alergia;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteAlergia)) {
            return false;
        }
        return getId() != null && getId().equals(((PacienteAlergia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteAlergia{" +
            "id=" + getId() +
            ", fechaDeteccion='" + getFechaDeteccion() + "'" +
            ", reaccion='" + getReaccion() + "'" +
            ", gravedad='" + getGravedad() + "'" +
            "}";
    }
}
