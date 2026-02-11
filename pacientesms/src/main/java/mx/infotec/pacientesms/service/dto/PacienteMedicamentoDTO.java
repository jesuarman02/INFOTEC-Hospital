package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.PacienteMedicamento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteMedicamentoDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String dosis;

    @Size(max = 50)
    private String frecuencia;

    @NotNull(message = "must not be null")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Boolean activo;

    private PacienteDTO paciente;

    private MedicamentoDTO medicamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    public MedicamentoDTO getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(MedicamentoDTO medicamento) {
        this.medicamento = medicamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteMedicamentoDTO)) {
            return false;
        }

        PacienteMedicamentoDTO pacienteMedicamentoDTO = (PacienteMedicamentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pacienteMedicamentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteMedicamentoDTO{" +
            "id=" + getId() +
            ", dosis='" + getDosis() + "'" +
            ", frecuencia='" + getFrecuencia() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", activo='" + getActivo() + "'" +
            ", paciente=" + getPaciente() +
            ", medicamento=" + getMedicamento() +
            "}";
    }
}
