package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.Enfermedad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnfermedadDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    private String nombre;

    @Size(max = 20)
    private String tipo;

    @Size(max = 10)
    private String codigoCIE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigoCIE() {
        return codigoCIE;
    }

    public void setCodigoCIE(String codigoCIE) {
        this.codigoCIE = codigoCIE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnfermedadDTO)) {
            return false;
        }

        EnfermedadDTO enfermedadDTO = (EnfermedadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enfermedadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnfermedadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", codigoCIE='" + getCodigoCIE() + "'" +
            "}";
    }
}
