package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.PacienteEnfermedad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteEnfermedadDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private LocalDate fechaDiagnostico;

    @Size(max = 20)
    private String estado;

    @Size(max = 255)
    private String notas;

    private PacienteDTO paciente;

    private EnfermedadDTO enfermedad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaDiagnostico() {
        return fechaDiagnostico;
    }

    public void setFechaDiagnostico(LocalDate fechaDiagnostico) {
        this.fechaDiagnostico = fechaDiagnostico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    public EnfermedadDTO getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(EnfermedadDTO enfermedad) {
        this.enfermedad = enfermedad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteEnfermedadDTO)) {
            return false;
        }

        PacienteEnfermedadDTO pacienteEnfermedadDTO = (PacienteEnfermedadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pacienteEnfermedadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteEnfermedadDTO{" +
            "id=" + getId() +
            ", fechaDiagnostico='" + getFechaDiagnostico() + "'" +
            ", estado='" + getEstado() + "'" +
            ", notas='" + getNotas() + "'" +
            ", paciente=" + getPaciente() +
            ", enfermedad=" + getEnfermedad() +
            "}";
    }
}
