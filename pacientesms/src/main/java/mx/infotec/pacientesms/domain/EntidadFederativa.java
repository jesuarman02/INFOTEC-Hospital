package mx.infotec.pacientesms.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A EntidadFederativa.
 */
@Table("entidad_federativa")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntidadFederativa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 5)
    @Column("clave")
    private String clave;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    @Column("nombre")
    private String nombre;

    @Size(max = 10)
    @Column("abreviatura")
    private String abreviatura;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EntidadFederativa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return this.clave;
    }

    public EntidadFederativa clave(String clave) {
        this.setClave(clave);
        return this;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return this.nombre;
    }

    public EntidadFederativa nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return this.abreviatura;
    }

    public EntidadFederativa abreviatura(String abreviatura) {
        this.setAbreviatura(abreviatura);
        return this;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntidadFederativa)) {
            return false;
        }
        return getId() != null && getId().equals(((EntidadFederativa) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntidadFederativa{" +
            "id=" + getId() +
            ", clave='" + getClave() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", abreviatura='" + getAbreviatura() + "'" +
            "}";
    }
}
