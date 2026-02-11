package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.EntidadFederativa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntidadFederativaDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 5)
    private String clave;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    private String nombre;

    @Size(max = 10)
    private String abreviatura;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntidadFederativaDTO)) {
            return false;
        }

        EntidadFederativaDTO entidadFederativaDTO = (EntidadFederativaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, entidadFederativaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntidadFederativaDTO{" +
            "id=" + getId() +
            ", clave='" + getClave() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", abreviatura='" + getAbreviatura() + "'" +
            "}";
    }
}
