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
 * A Enfermedad.
 */
@Table("enfermedad")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enfermedad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("nombre")
    private String nombre;

    @Size(max = 20)
    @Column("tipo")
    private String tipo;

    @Size(max = 10)
    @Column("codigo_cie")
    private String codigoCIE;

    @Transient
    @JsonIgnoreProperties(value = { "paciente", "enfermedad" }, allowSetters = true)
    private Set<PacienteEnfermedad> pacientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enfermedad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Enfermedad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Enfermedad tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigoCIE() {
        return this.codigoCIE;
    }

    public Enfermedad codigoCIE(String codigoCIE) {
        this.setCodigoCIE(codigoCIE);
        return this;
    }

    public void setCodigoCIE(String codigoCIE) {
        this.codigoCIE = codigoCIE;
    }

    public Set<PacienteEnfermedad> getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(Set<PacienteEnfermedad> pacienteEnfermedads) {
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.setEnfermedad(null));
        }
        if (pacienteEnfermedads != null) {
            pacienteEnfermedads.forEach(i -> i.setEnfermedad(this));
        }
        this.pacientes = pacienteEnfermedads;
    }

    public Enfermedad pacientes(Set<PacienteEnfermedad> pacienteEnfermedads) {
        this.setPacientes(pacienteEnfermedads);
        return this;
    }

    public Enfermedad addPacientes(PacienteEnfermedad pacienteEnfermedad) {
        this.pacientes.add(pacienteEnfermedad);
        pacienteEnfermedad.setEnfermedad(this);
        return this;
    }

    public Enfermedad removePacientes(PacienteEnfermedad pacienteEnfermedad) {
        this.pacientes.remove(pacienteEnfermedad);
        pacienteEnfermedad.setEnfermedad(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enfermedad)) {
            return false;
        }
        return getId() != null && getId().equals(((Enfermedad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enfermedad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", codigoCIE='" + getCodigoCIE() + "'" +
            "}";
    }
}
