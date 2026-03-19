package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.InfoSocioeconomica} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InfoSocioeconomicaDTO implements Serializable {

    private Long id;

    @NotNull
    private Long pacienteId;

    @NotNull
    @Size(max = 255)
    private String pregunta;

    @Size(max = 255)
    private String respuesta;

    @Size(max = 255)
    private String respuestaAbierta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuestaAbierta() {
        return respuestaAbierta;
    }

    public void setRespuestaAbierta(String respuestaAbierta) {
        this.respuestaAbierta = respuestaAbierta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InfoSocioeconomicaDTO)) {
            return false;
        }

        InfoSocioeconomicaDTO infoSocioeconomicaDTO = (InfoSocioeconomicaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, infoSocioeconomicaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoSocioeconomicaDTO{" +
            "id=" + getId() +
            ", pacienteId=" + getPacienteId() +
            ", pregunta='" + getPregunta() + "'" +
            ", respuesta='" + getRespuesta() + "'" +
            ", respuestaAbierta='" + getRespuestaAbierta() + "'" +
            "}";
    }
}