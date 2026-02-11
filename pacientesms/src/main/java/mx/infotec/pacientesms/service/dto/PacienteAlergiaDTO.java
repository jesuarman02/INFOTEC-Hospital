package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import mx.infotec.pacientesms.domain.enumeration.GradoAlergia;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.PacienteAlergia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteAlergiaDTO implements Serializable {

    private Long id;

    private LocalDate fechaDeteccion;

    @Size(max = 150)
    private String reaccion;

    @NotNull(message = "must not be null")
    private GradoAlergia gravedad;

    private PacienteDTO paciente;

    private AlergiaDTO alergia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaDeteccion() {
        return fechaDeteccion;
    }

    public void setFechaDeteccion(LocalDate fechaDeteccion) {
        this.fechaDeteccion = fechaDeteccion;
    }

    public String getReaccion() {
        return reaccion;
    }

    public void setReaccion(String reaccion) {
        this.reaccion = reaccion;
    }

    public GradoAlergia getGravedad() {
        return gravedad;
    }

    public void setGravedad(GradoAlergia gravedad) {
        this.gravedad = gravedad;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    public AlergiaDTO getAlergia() {
        return alergia;
    }

    public void setAlergia(AlergiaDTO alergia) {
        this.alergia = alergia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteAlergiaDTO)) {
            return false;
        }

        PacienteAlergiaDTO pacienteAlergiaDTO = (PacienteAlergiaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pacienteAlergiaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteAlergiaDTO{" +
            "id=" + getId() +
            ", fechaDeteccion='" + getFechaDeteccion() + "'" +
            ", reaccion='" + getReaccion() + "'" +
            ", gravedad='" + getGravedad() + "'" +
            ", paciente=" + getPaciente() +
            ", alergia=" + getAlergia() +
            "}";
    }
}
