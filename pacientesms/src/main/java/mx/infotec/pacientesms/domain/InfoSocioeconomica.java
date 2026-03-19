package mx.infotec.pacientesms.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A InfoSocioeconomica.
 */
@Table("info_socioeconomica")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfoSocioeconomica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Column("paciente_id")
    private Long pacienteId;

    @NotNull
    @Size(max = 255)
    @Column("pregunta")
    private String pregunta;

    @Size(max = 255)
    @Column("respuesta")
    private String respuesta;

    @Size(max = 255)
    @Column("respuesta_abierta")
    private String respuestaAbierta;

    @Transient
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InfoSocioeconomica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPacienteId() {
        return this.pacienteId;
    }

    public InfoSocioeconomica pacienteId(Long pacienteId) {
        this.setPacienteId(pacienteId);
        return this;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPregunta() {
        return this.pregunta;
    }

    public InfoSocioeconomica pregunta(String pregunta) {
        this.setPregunta(pregunta);
        return this;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return this.respuesta;
    }

    public InfoSocioeconomica respuesta(String respuesta) {
        this.setRespuesta(respuesta);
        return this;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuestaAbierta() {
        return this.respuestaAbierta;
    }

    public InfoSocioeconomica respuestaAbierta(String respuestaAbierta) {
        this.setRespuestaAbierta(respuestaAbierta);
        return this;
    }

    public void setRespuestaAbierta(String respuestaAbierta) {
        this.respuestaAbierta = respuestaAbierta;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (this.paciente != null) {
            this.paciente.setInfoSocioeconomica(null);
        }
        if (paciente != null) {
            paciente.setInfoSocioeconomica(this);
        }
        this.paciente = paciente;
    }

    public InfoSocioeconomica paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoSocioeconomica)) {
            return false;
        }
        return getId() != null && getId().equals(((InfoSocioeconomica) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoSocioeconomica{" +
            "id=" + getId() +
            ", pacienteId=" + getPacienteId() +
            ", pregunta='" + getPregunta() + "'" +
            ", respuesta='" + getRespuesta() + "'" +
            ", respuestaAbierta='" + getRespuestaAbierta() + "'" +
            "}";
    }
}