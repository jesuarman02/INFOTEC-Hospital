package mx.infotec.pacientesms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import mx.infotec.pacientesms.domain.enumeration.Sexo;
import mx.infotec.pacientesms.domain.enumeration.Nacionalidad;
import mx.infotec.pacientesms.domain.enumeration.EstadoCivil;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Paciente.
 */
@Table("paciente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    // ECU obligatorio y único (unicidad se define en la BD)
    @NotNull
    @Column("ecu")
    private Integer ecu;

    // SOLO LETRAS, ESPACIOS Y ACENTOS. MIN 2, MAX 60
    @NotNull
    @Size(min = 2, max = 60)
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1 ]+$", message = "Solo se permiten letras")
    @Column("nombre")
    private String nombre;

    @NotNull
    @Size(min = 2, max = 60)
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1 ]+$", message = "Solo se permiten letras")
    @Column("apellido_paterno")
    private String apellidoPaterno;

    @Size(max = 60)
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1 ]+$", message = "Solo se permiten letras")
    @Column("apellido_materno")
    private String apellidoMaterno;

    // CAMBIO A ENUM
    @NotNull
    @Column("sexo")
    private Sexo sexo;

    @Column("embarazo")
    private String embarazo;

    // CAMBIO A ENUM
    @Column("nacionalidad")
    private Nacionalidad nacionalidad;

    // DEBE SER FECHA PASADA
    @NotNull
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    // ENUM EstadoCivil
    @Column("estado_civil")
    private EstadoCivil estadoCivil;

    // CURP con validación completa
    @NotNull
    @Size(min = 18, max = 18)
    @Pattern(
        regexp = "^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])" +
                 "(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}" +
                 "(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" +
                 "[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$",
        message = "Formato de CURP inválido"
    )
    @Column("curp")
    private String curp;

    @Transient
    private Direccion direccion;

    @Transient
    private InfoSocioeconomica infoSocioeconomica;

    @Transient
    private HistorialMedico historialGeneral;

    @Transient
    @JsonIgnoreProperties(value = { "paciente" }, allowSetters = true)
    private Set<SignosVitales> signosVitales = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "paciente", "enfermedad" }, allowSetters = true)
    private Set<PacienteEnfermedad> misEnfermedades = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "paciente", "alergia" }, allowSetters = true)
    private Set<PacienteAlergia> misAlergias = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "paciente", "medicamento" }, allowSetters = true)
    private Set<PacienteMedicamento> misMedicamentos = new HashSet<>();

    @Transient
    private EntidadFederativa entidadNacimiento;

    @Column("direccion_id")
    private Long direccionId;

    @Column("info_socioeconomica_id")
    private Long infoSocioeconomicaId;

    @Column("historial_general_id")
    private Long historialGeneralId;

    @Column("entidad_nacimiento_id")
    private Long entidadNacimientoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paciente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEcu() {
        return this.ecu;
    }

    public Paciente ecu(Integer ecu) {
        this.setEcu(ecu);
        return this;
    }

    public void setEcu(Integer ecu) {
        this.ecu = ecu;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Paciente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return this.apellidoPaterno;
    }

    public Paciente apellidoPaterno(String apellidoPaterno) {
        this.setApellidoPaterno(apellidoPaterno);
        return this;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return this.apellidoMaterno;
    }

    public Paciente apellidoMaterno(String apellidoMaterno) {
        this.setApellidoMaterno(apellidoMaterno);
        return this;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public Sexo getSexo() {
        return this.sexo;
    }

    public Paciente sexo(Sexo sexo) {
        this.setSexo(sexo);
        return this;
    }
    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getEmbarazo() {
        return this.embarazo;
    }

    public Paciente embarazo(String embarazo) {
        this.setEmbarazo(embarazo);
        return this;
    }

    public void setEmbarazo(String embarazo) {
        this.embarazo = embarazo;
    }

    public Nacionalidad getNacionalidad() {
        return this.nacionalidad;
    }
    public Paciente nacionalidad(Nacionalidad nacionalidad) {
        this.setNacionalidad(nacionalidad);
        return this;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }


    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Paciente fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public EstadoCivil getEstadoCivil() {
        return this.estadoCivil;
    }

    public Paciente estadoCivil(EstadoCivil estadoCivil) {
        this.setEstadoCivil(estadoCivil);
        return this;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }


    public String getCurp() {
        return this.curp;
    }

    public Paciente curp(String curp) {
        this.setCurp(curp);
        return this;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public Direccion getDireccion() {
        return this.direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
        this.direccionId = direccion != null ? direccion.getId() : null;
    }

    public Paciente direccion(Direccion direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public InfoSocioeconomica getInfoSocioeconomica() {
        return this.infoSocioeconomica;
    }

    public void setInfoSocioeconomica(InfoSocioeconomica infoSocioeconomica) {
        this.infoSocioeconomica = infoSocioeconomica;
        this.infoSocioeconomicaId = infoSocioeconomica != null ? infoSocioeconomica.getId() : null;
    }

    public Paciente infoSocioeconomica(InfoSocioeconomica infoSocioeconomica) {
        this.setInfoSocioeconomica(infoSocioeconomica);
        return this;
    }

    public HistorialMedico getHistorialGeneral() {
        return this.historialGeneral;
    }

    public void setHistorialGeneral(HistorialMedico historialMedico) {
        this.historialGeneral = historialMedico;
        this.historialGeneralId = historialMedico != null ? historialMedico.getId() : null;
    }

    public Paciente historialGeneral(HistorialMedico historialMedico) {
        this.setHistorialGeneral(historialMedico);
        return this;
    }

    public Set<SignosVitales> getSignosVitales() {
        return this.signosVitales;
    }

    public void setSignosVitales(Set<SignosVitales> signosVitales) {
        if (this.signosVitales != null) {
            this.signosVitales.forEach(i -> i.setPaciente(null));
        }
        if (signosVitales != null) {
            signosVitales.forEach(i -> i.setPaciente(this));
        }
        this.signosVitales = signosVitales;
    }

    public Paciente signosVitales(Set<SignosVitales> signosVitales) {
        this.setSignosVitales(signosVitales);
        return this;
    }

    public Paciente addSignosVitales(SignosVitales signosVitales) {
        this.signosVitales.add(signosVitales);
        signosVitales.setPaciente(this);
        return this;
    }

    public Paciente removeSignosVitales(SignosVitales signosVitales) {
        this.signosVitales.remove(signosVitales);
        signosVitales.setPaciente(null);
        return this;
    }

    public Set<PacienteEnfermedad> getMisEnfermedades() {
        return this.misEnfermedades;
    }

    public void setMisEnfermedades(Set<PacienteEnfermedad> pacienteEnfermedads) {
        if (this.misEnfermedades != null) {
            this.misEnfermedades.forEach(i -> i.setPaciente(null));
        }
        if (pacienteEnfermedads != null) {
            pacienteEnfermedads.forEach(i -> i.setPaciente(this));
        }
        this.misEnfermedades = pacienteEnfermedads;
    }

    public Paciente misEnfermedades(Set<PacienteEnfermedad> pacienteEnfermedads) {
        this.setMisEnfermedades(pacienteEnfermedads);
        return this;
    }

    public Paciente addMisEnfermedades(PacienteEnfermedad pacienteEnfermedad) {
        this.misEnfermedades.add(pacienteEnfermedad);
        pacienteEnfermedad.setPaciente(this);
        return this;
    }

    public Paciente removeMisEnfermedades(PacienteEnfermedad pacienteEnfermedad) {
        this.misEnfermedades.remove(pacienteEnfermedad);
        pacienteEnfermedad.setPaciente(null);
        return this;
    }

    public Set<PacienteAlergia> getMisAlergias() {
        return this.misAlergias;
    }

    public void setMisAlergias(Set<PacienteAlergia> pacienteAlergias) {
        if (this.misAlergias != null) {
            this.misAlergias.forEach(i -> i.setPaciente(null));
        }
        if (pacienteAlergias != null) {
            pacienteAlergias.forEach(i -> i.setPaciente(this));
        }
        this.misAlergias = pacienteAlergias;
    }

    public Paciente misAlergias(Set<PacienteAlergia> pacienteAlergias) {
        this.setMisAlergias(pacienteAlergias);
        return this;
    }

    public Paciente addMisAlergias(PacienteAlergia pacienteAlergia) {
        this.misAlergias.add(pacienteAlergia);
        pacienteAlergia.setPaciente(this);
        return this;
    }

    public Paciente removeMisAlergias(PacienteAlergia pacienteAlergia) {
        this.misAlergias.remove(pacienteAlergia);
        pacienteAlergia.setPaciente(null);
        return this;
    }

    public Set<PacienteMedicamento> getMisMedicamentos() {
        return this.misMedicamentos;
    }

    public void setMisMedicamentos(Set<PacienteMedicamento> pacienteMedicamentos) {
        if (this.misMedicamentos != null) {
            this.misMedicamentos.forEach(i -> i.setPaciente(null));
        }
        if (pacienteMedicamentos != null) {
            pacienteMedicamentos.forEach(i -> i.setPaciente(this));
        }
        this.misMedicamentos = pacienteMedicamentos;
    }

    public Paciente misMedicamentos(Set<PacienteMedicamento> pacienteMedicamentos) {
        this.setMisMedicamentos(pacienteMedicamentos);
        return this;
    }

    public Paciente addMisMedicamentos(PacienteMedicamento pacienteMedicamento) {
        this.misMedicamentos.add(pacienteMedicamento);
        pacienteMedicamento.setPaciente(this);
        return this;
    }

    public Paciente removeMisMedicamentos(PacienteMedicamento pacienteMedicamento) {
        this.misMedicamentos.remove(pacienteMedicamento);
        pacienteMedicamento.setPaciente(null);
        return this;
    }

    public EntidadFederativa getEntidadNacimiento() {
        return this.entidadNacimiento;
    }

    public void setEntidadNacimiento(EntidadFederativa entidadFederativa) {
        this.entidadNacimiento = entidadFederativa;
        this.entidadNacimientoId = entidadFederativa != null ? entidadFederativa.getId() : null;
    }

    public Paciente entidadNacimiento(EntidadFederativa entidadFederativa) {
        this.setEntidadNacimiento(entidadFederativa);
        return this;
    }

    public Long getDireccionId() {
        return this.direccionId;
    }

    public void setDireccionId(Long direccion) {
        this.direccionId = direccion;
    }

    public Long getInfoSocioeconomicaId() {
        return this.infoSocioeconomicaId;
    }

    public void setInfoSocioeconomicaId(Long infoSocioeconomica) {
        this.infoSocioeconomicaId = infoSocioeconomica;
    }

    public Long getHistorialGeneralId() {
        return this.historialGeneralId;
    }

    public void setHistorialGeneralId(Long historialMedico) {
        this.historialGeneralId = historialMedico;
    }

    public Long getEntidadNacimientoId() {
        return this.entidadNacimientoId;
    }

    public void setEntidadNacimientoId(Long entidadFederativa) {
        this.entidadNacimientoId = entidadFederativa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return getId() != null && getId().equals(((Paciente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
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
            "}";
    }
}
