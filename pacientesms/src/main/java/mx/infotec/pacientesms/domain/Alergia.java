package mx.infotec.pacientesms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Alergia.
 */
@Table("alergia")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alergia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("nombre")
    private String nombre;

    @Size(max = 255)
    @Column("descripcion")
    private String descripcion;

    @Transient
    @JsonIgnoreProperties(value = { "paciente", "alergia" }, allowSetters = true)
    private Set<PacienteAlergia> pacientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alergia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Alergia nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Alergia descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<PacienteAlergia> getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(Set<PacienteAlergia> pacienteAlergias) {
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.setAlergia(null));
        }
        if (pacienteAlergias != null) {
            pacienteAlergias.forEach(i -> i.setAlergia(this));
        }
        this.pacientes = pacienteAlergias;
    }

    public Alergia pacientes(Set<PacienteAlergia> pacienteAlergias) {
        this.setPacientes(pacienteAlergias);
        return this;
    }

    public Alergia addPacientes(PacienteAlergia pacienteAlergia) {
        this.pacientes.add(pacienteAlergia);
        pacienteAlergia.setAlergia(this);
        return this;
    }

    public Alergia removePacientes(PacienteAlergia pacienteAlergia) {
        this.pacientes.remove(pacienteAlergia);
        pacienteAlergia.setAlergia(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alergia)) {
            return false;
        }
        return getId() != null && getId().equals(((Alergia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alergia{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
