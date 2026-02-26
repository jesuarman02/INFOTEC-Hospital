package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import mx.infotec.pacientesms.domain.enumeration.Sexo;
import mx.infotec.pacientesms.domain.enumeration.Nacionalidad;
import mx.infotec.pacientesms.domain.enumeration.EstadoCivil;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.Paciente} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer ecu;

    @NotNull(message = "must not be null")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    private String apellidoPaterno;

    @Size(max = 50)
    private String apellidoMaterno;

    @NotNull(message = "must not be null")
    private Sexo sexo;

    @NotNull(message = "must not be null")
    private String embarazo;

    @NotNull(message = "must not be null")
    private Nacionalidad nacionalidad;

    @NotNull(message = "must not be null")
    private LocalDate fechaNacimiento;

    @NotNull(message = "must not be null")
    private EstadoCivil estadoCivil;

    @NotNull(message = "must not be null")
    @Size(max = 18)
    private String curp;

    private DireccionDTO direccion;

    private InfoSocioeconomicaDTO infoSocioeconomica;

    private HistorialMedicoDTO historialGeneral;

    private EntidadFederativaDTO entidadNacimiento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEcu() {
        return ecu;
    }

    public void setEcu(Integer ecu) {
        this.ecu = ecu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getEmbarazo() {
        return embarazo;
    }

    public void setEmbarazo(String embarazo) {
        this.embarazo = embarazo;
    }

    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public DireccionDTO getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionDTO direccion) {
        this.direccion = direccion;
    }

    public InfoSocioeconomicaDTO getInfoSocioeconomica() {
        return infoSocioeconomica;
    }

    public void setInfoSocioeconomica(InfoSocioeconomicaDTO infoSocioeconomica) {
        this.infoSocioeconomica = infoSocioeconomica;
    }

    public HistorialMedicoDTO getHistorialGeneral() {
        return historialGeneral;
    }

    public void setHistorialGeneral(HistorialMedicoDTO historialGeneral) {
        this.historialGeneral = historialGeneral;
    }

    public EntidadFederativaDTO getEntidadNacimiento() {
        return entidadNacimiento;
    }

    public void setEntidadNacimiento(EntidadFederativaDTO entidadNacimiento) {
        this.entidadNacimiento = entidadNacimiento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteDTO)) {
            return false;
        }

        PacienteDTO pacienteDTO = (PacienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pacienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteDTO{" +
            "id=" + getId() +
            ", ecu=" + getEcu() +
            ", nombre='" + getNombre() + "'" +
            ", apellidoPaterno='" + getApellidoPaterno() + "'" +
            ", apellidoMaterno='" + getApellidoMaterno() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", embarazo='" + getEmbarazo() + "'" +
            ", nacionalidad='" + getNacionalidad() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", estadoCivil='" + getEstadoCivil() + "'" +
            ", curp='" + getCurp() + "'" +
            ", direccion=" + getDireccion() +
            ", infoSocioeconomica=" + getInfoSocioeconomica() +
            ", historialGeneral=" + getHistorialGeneral() +
            ", entidadNacimiento=" + getEntidadNacimiento() +
            "}";
    }
}
