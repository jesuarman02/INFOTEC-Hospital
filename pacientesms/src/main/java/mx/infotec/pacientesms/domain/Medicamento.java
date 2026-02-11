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
 * A Medicamento.
 */
@Table("medicamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("nombre")
    private String nombre;

    @Size(max = 100)
    @Column("ingrediente_activo")
    private String ingredienteActivo;

    @Size(max = 50)
    @Column("presentacion")
    private String presentacion;

    @Transient
    @JsonIgnoreProperties(value = { "paciente", "medicamento" }, allowSetters = true)
    private Set<PacienteMedicamento> pacientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medicamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Medicamento nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIngredienteActivo() {
        return this.ingredienteActivo;
    }

    public Medicamento ingredienteActivo(String ingredienteActivo) {
        this.setIngredienteActivo(ingredienteActivo);
        return this;
    }

    public void setIngredienteActivo(String ingredienteActivo) {
        this.ingredienteActivo = ingredienteActivo;
    }

    public String getPresentacion() {
        return this.presentacion;
    }

    public Medicamento presentacion(String presentacion) {
        this.setPresentacion(presentacion);
        return this;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public Set<PacienteMedicamento> getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(Set<PacienteMedicamento> pacienteMedicamentos) {
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.setMedicamento(null));
        }
        if (pacienteMedicamentos != null) {
            pacienteMedicamentos.forEach(i -> i.setMedicamento(this));
        }
        this.pacientes = pacienteMedicamentos;
    }

    public Medicamento pacientes(Set<PacienteMedicamento> pacienteMedicamentos) {
        this.setPacientes(pacienteMedicamentos);
        return this;
    }

    public Medicamento addPacientes(PacienteMedicamento pacienteMedicamento) {
        this.pacientes.add(pacienteMedicamento);
        pacienteMedicamento.setMedicamento(this);
        return this;
    }

    public Medicamento removePacientes(PacienteMedicamento pacienteMedicamento) {
        this.pacientes.remove(pacienteMedicamento);
        pacienteMedicamento.setMedicamento(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicamento)) {
            return false;
        }
        return getId() != null && getId().equals(((Medicamento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicamento{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ingredienteActivo='" + getIngredienteActivo() + "'" +
            ", presentacion='" + getPresentacion() + "'" +
            "}";
    }
}
