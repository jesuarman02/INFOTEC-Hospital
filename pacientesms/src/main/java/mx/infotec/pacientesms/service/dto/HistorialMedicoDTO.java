package mx.infotec.pacientesms.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.HistorialMedico} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistorialMedicoDTO implements Serializable {

    private Long id;

    @Size(max = 500)
    private String antecedentesQuirurgicos;

    @Size(max = 255)
    private String esquemaVacunacion;

    @Size(max = 255)
    private String habitos;

    @Lob
    private String observacionesGenerales;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAntecedentesQuirurgicos() {
        return antecedentesQuirurgicos;
    }

    public void setAntecedentesQuirurgicos(String antecedentesQuirurgicos) {
        this.antecedentesQuirurgicos = antecedentesQuirurgicos;
    }

    public String getEsquemaVacunacion() {
        return esquemaVacunacion;
    }

    public void setEsquemaVacunacion(String esquemaVacunacion) {
        this.esquemaVacunacion = esquemaVacunacion;
    }

    public String getHabitos() {
        return habitos;
    }

    public void setHabitos(String habitos) {
        this.habitos = habitos;
    }

    public String getObservacionesGenerales() {
        return observacionesGenerales;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistorialMedicoDTO)) {
            return false;
        }

        HistorialMedicoDTO historialMedicoDTO = (HistorialMedicoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historialMedicoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistorialMedicoDTO{" +
            "id=" + getId() +
            ", antecedentesQuirurgicos='" + getAntecedentesQuirurgicos() + "'" +
            ", esquemaVacunacion='" + getEsquemaVacunacion() + "'" +
            ", habitos='" + getHabitos() + "'" +
            ", observacionesGenerales='" + getObservacionesGenerales() + "'" +
            "}";
    }
}
