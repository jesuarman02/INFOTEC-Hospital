package mx.infotec.pacientesms.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HistorialMedico.
 */
@Table("historial_medico")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistorialMedico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Size(max = 500)
    @Column("antecedentes_quirurgicos")
    private String antecedentesQuirurgicos;

    @Size(max = 255)
    @Column("esquema_vacunacion")
    private String esquemaVacunacion;

    @Size(max = 255)
    @Column("habitos")
    private String habitos;

    @Column("observaciones_generales")
    private String observacionesGenerales;

    @Transient
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistorialMedico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAntecedentesQuirurgicos() {
        return this.antecedentesQuirurgicos;
    }

    public HistorialMedico antecedentesQuirurgicos(String antecedentesQuirurgicos) {
        this.setAntecedentesQuirurgicos(antecedentesQuirurgicos);
        return this;
    }

    public void setAntecedentesQuirurgicos(String antecedentesQuirurgicos) {
        this.antecedentesQuirurgicos = antecedentesQuirurgicos;
    }

    public String getEsquemaVacunacion() {
        return this.esquemaVacunacion;
    }

    public HistorialMedico esquemaVacunacion(String esquemaVacunacion) {
        this.setEsquemaVacunacion(esquemaVacunacion);
        return this;
    }

    public void setEsquemaVacunacion(String esquemaVacunacion) {
        this.esquemaVacunacion = esquemaVacunacion;
    }

    public String getHabitos() {
        return this.habitos;
    }

    public HistorialMedico habitos(String habitos) {
        this.setHabitos(habitos);
        return this;
    }

    public void setHabitos(String habitos) {
        this.habitos = habitos;
    }

    public String getObservacionesGenerales() {
        return this.observacionesGenerales;
    }

    public HistorialMedico observacionesGenerales(String observacionesGenerales) {
        this.setObservacionesGenerales(observacionesGenerales);
        return this;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (this.paciente != null) {
            this.paciente.setHistorialGeneral(null);
        }
        if (paciente != null) {
            paciente.setHistorialGeneral(this);
        }
        this.paciente = paciente;
    }

    public HistorialMedico paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistorialMedico)) {
            return false;
        }
        return getId() != null && getId().equals(((HistorialMedico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistorialMedico{" +
            "id=" + getId() +
            ", antecedentesQuirurgicos='" + getAntecedentesQuirurgicos() + "'" +
            ", esquemaVacunacion='" + getEsquemaVacunacion() + "'" +
            ", habitos='" + getHabitos() + "'" +
            ", observacionesGenerales='" + getObservacionesGenerales() + "'" +
            "}";
    }
}
