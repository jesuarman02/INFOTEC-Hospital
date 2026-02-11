package mx.infotec.pacientesms.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Size(max = 50)
    @Column("tipo_vivienda")
    private String tipoVivienda;

    @Size(max = 100)
    @Column("material_vivienda")
    private String materialVivienda;

    @Column("numero_habitaciones")
    private Integer numeroHabitaciones;

    @Column("numero_habitantes")
    private Integer numeroHabitantes;

    @Size(max = 255)
    @Column("servicios_disponibles")
    private String serviciosDisponibles;

    @Column("ingreso_mensual")
    private BigDecimal ingresoMensual;

    @Column("ingreso_mensual_hogar")
    private BigDecimal ingresoMensualHogar;

    @Column("gasto_mensual")
    private BigDecimal gastoMensual;

    @Column("personas_dependientes")
    private Integer personasDependientes;

    @Size(max = 255)
    @Column("apoyos_gubernamentales")
    private String apoyosGubernamentales;

    @Size(max = 100)
    @Column("ocupacion_actual")
    private String ocupacionActual;

    @Size(max = 50)
    @Column("condicion_laboral")
    private String condicionLaboral;

    @Size(max = 50)
    @Column("tipo_empleo")
    private String tipoEmpleo;

    @Size(max = 150)
    @Column("lugar_trabajo")
    private String lugarTrabajo;

    @Size(max = 50)
    @Column("tiempo_empleado")
    private String tiempoEmpleado;

    @Size(max = 50)
    @Column("grado_maximo_estudios")
    private String gradoMaximoEstudios;

    @Column("anios_estudio")
    private Integer aniosEstudio;

    @Column("estudia")
    private Boolean estudia;

    @Size(max = 100)
    @Column("institucion_medica")
    private String institucionMedica;

    @Size(max = 50)
    @Column("tipo_afiliacion")
    private String tipoAfiliacion;

    @Size(max = 30)
    @Column("numero_afiliacion")
    private String numeroAfiliacion;

    @Size(max = 50)
    @Column("medio_transporte")
    private String medioTransporte;

    @Column("tiempo_traslado")
    private Integer tiempoTraslado;

    @Column("fecha_actualizacion")
    private LocalDate fechaActualizacion;

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

    public String getTipoVivienda() {
        return this.tipoVivienda;
    }

    public InfoSocioeconomica tipoVivienda(String tipoVivienda) {
        this.setTipoVivienda(tipoVivienda);
        return this;
    }

    public void setTipoVivienda(String tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }

    public String getMaterialVivienda() {
        return this.materialVivienda;
    }

    public InfoSocioeconomica materialVivienda(String materialVivienda) {
        this.setMaterialVivienda(materialVivienda);
        return this;
    }

    public void setMaterialVivienda(String materialVivienda) {
        this.materialVivienda = materialVivienda;
    }

    public Integer getNumeroHabitaciones() {
        return this.numeroHabitaciones;
    }

    public InfoSocioeconomica numeroHabitaciones(Integer numeroHabitaciones) {
        this.setNumeroHabitaciones(numeroHabitaciones);
        return this;
    }

    public void setNumeroHabitaciones(Integer numeroHabitaciones) {
        this.numeroHabitaciones = numeroHabitaciones;
    }

    public Integer getNumeroHabitantes() {
        return this.numeroHabitantes;
    }

    public InfoSocioeconomica numeroHabitantes(Integer numeroHabitantes) {
        this.setNumeroHabitantes(numeroHabitantes);
        return this;
    }

    public void setNumeroHabitantes(Integer numeroHabitantes) {
        this.numeroHabitantes = numeroHabitantes;
    }

    public String getServiciosDisponibles() {
        return this.serviciosDisponibles;
    }

    public InfoSocioeconomica serviciosDisponibles(String serviciosDisponibles) {
        this.setServiciosDisponibles(serviciosDisponibles);
        return this;
    }

    public void setServiciosDisponibles(String serviciosDisponibles) {
        this.serviciosDisponibles = serviciosDisponibles;
    }

    public BigDecimal getIngresoMensual() {
        return this.ingresoMensual;
    }

    public InfoSocioeconomica ingresoMensual(BigDecimal ingresoMensual) {
        this.setIngresoMensual(ingresoMensual);
        return this;
    }

    public void setIngresoMensual(BigDecimal ingresoMensual) {
        this.ingresoMensual = ingresoMensual != null ? ingresoMensual.stripTrailingZeros() : null;
    }

    public BigDecimal getIngresoMensualHogar() {
        return this.ingresoMensualHogar;
    }

    public InfoSocioeconomica ingresoMensualHogar(BigDecimal ingresoMensualHogar) {
        this.setIngresoMensualHogar(ingresoMensualHogar);
        return this;
    }

    public void setIngresoMensualHogar(BigDecimal ingresoMensualHogar) {
        this.ingresoMensualHogar = ingresoMensualHogar != null ? ingresoMensualHogar.stripTrailingZeros() : null;
    }

    public BigDecimal getGastoMensual() {
        return this.gastoMensual;
    }

    public InfoSocioeconomica gastoMensual(BigDecimal gastoMensual) {
        this.setGastoMensual(gastoMensual);
        return this;
    }

    public void setGastoMensual(BigDecimal gastoMensual) {
        this.gastoMensual = gastoMensual != null ? gastoMensual.stripTrailingZeros() : null;
    }

    public Integer getPersonasDependientes() {
        return this.personasDependientes;
    }

    public InfoSocioeconomica personasDependientes(Integer personasDependientes) {
        this.setPersonasDependientes(personasDependientes);
        return this;
    }

    public void setPersonasDependientes(Integer personasDependientes) {
        this.personasDependientes = personasDependientes;
    }

    public String getApoyosGubernamentales() {
        return this.apoyosGubernamentales;
    }

    public InfoSocioeconomica apoyosGubernamentales(String apoyosGubernamentales) {
        this.setApoyosGubernamentales(apoyosGubernamentales);
        return this;
    }

    public void setApoyosGubernamentales(String apoyosGubernamentales) {
        this.apoyosGubernamentales = apoyosGubernamentales;
    }

    public String getOcupacionActual() {
        return this.ocupacionActual;
    }

    public InfoSocioeconomica ocupacionActual(String ocupacionActual) {
        this.setOcupacionActual(ocupacionActual);
        return this;
    }

    public void setOcupacionActual(String ocupacionActual) {
        this.ocupacionActual = ocupacionActual;
    }

    public String getCondicionLaboral() {
        return this.condicionLaboral;
    }

    public InfoSocioeconomica condicionLaboral(String condicionLaboral) {
        this.setCondicionLaboral(condicionLaboral);
        return this;
    }

    public void setCondicionLaboral(String condicionLaboral) {
        this.condicionLaboral = condicionLaboral;
    }

    public String getTipoEmpleo() {
        return this.tipoEmpleo;
    }

    public InfoSocioeconomica tipoEmpleo(String tipoEmpleo) {
        this.setTipoEmpleo(tipoEmpleo);
        return this;
    }

    public void setTipoEmpleo(String tipoEmpleo) {
        this.tipoEmpleo = tipoEmpleo;
    }

    public String getLugarTrabajo() {
        return this.lugarTrabajo;
    }

    public InfoSocioeconomica lugarTrabajo(String lugarTrabajo) {
        this.setLugarTrabajo(lugarTrabajo);
        return this;
    }

    public void setLugarTrabajo(String lugarTrabajo) {
        this.lugarTrabajo = lugarTrabajo;
    }

    public String getTiempoEmpleado() {
        return this.tiempoEmpleado;
    }

    public InfoSocioeconomica tiempoEmpleado(String tiempoEmpleado) {
        this.setTiempoEmpleado(tiempoEmpleado);
        return this;
    }

    public void setTiempoEmpleado(String tiempoEmpleado) {
        this.tiempoEmpleado = tiempoEmpleado;
    }

    public String getGradoMaximoEstudios() {
        return this.gradoMaximoEstudios;
    }

    public InfoSocioeconomica gradoMaximoEstudios(String gradoMaximoEstudios) {
        this.setGradoMaximoEstudios(gradoMaximoEstudios);
        return this;
    }

    public void setGradoMaximoEstudios(String gradoMaximoEstudios) {
        this.gradoMaximoEstudios = gradoMaximoEstudios;
    }

    public Integer getAniosEstudio() {
        return this.aniosEstudio;
    }

    public InfoSocioeconomica aniosEstudio(Integer aniosEstudio) {
        this.setAniosEstudio(aniosEstudio);
        return this;
    }

    public void setAniosEstudio(Integer aniosEstudio) {
        this.aniosEstudio = aniosEstudio;
    }

    public Boolean getEstudia() {
        return this.estudia;
    }

    public InfoSocioeconomica estudia(Boolean estudia) {
        this.setEstudia(estudia);
        return this;
    }

    public void setEstudia(Boolean estudia) {
        this.estudia = estudia;
    }

    public String getInstitucionMedica() {
        return this.institucionMedica;
    }

    public InfoSocioeconomica institucionMedica(String institucionMedica) {
        this.setInstitucionMedica(institucionMedica);
        return this;
    }

    public void setInstitucionMedica(String institucionMedica) {
        this.institucionMedica = institucionMedica;
    }

    public String getTipoAfiliacion() {
        return this.tipoAfiliacion;
    }

    public InfoSocioeconomica tipoAfiliacion(String tipoAfiliacion) {
        this.setTipoAfiliacion(tipoAfiliacion);
        return this;
    }

    public void setTipoAfiliacion(String tipoAfiliacion) {
        this.tipoAfiliacion = tipoAfiliacion;
    }

    public String getNumeroAfiliacion() {
        return this.numeroAfiliacion;
    }

    public InfoSocioeconomica numeroAfiliacion(String numeroAfiliacion) {
        this.setNumeroAfiliacion(numeroAfiliacion);
        return this;
    }

    public void setNumeroAfiliacion(String numeroAfiliacion) {
        this.numeroAfiliacion = numeroAfiliacion;
    }

    public String getMedioTransporte() {
        return this.medioTransporte;
    }

    public InfoSocioeconomica medioTransporte(String medioTransporte) {
        this.setMedioTransporte(medioTransporte);
        return this;
    }

    public void setMedioTransporte(String medioTransporte) {
        this.medioTransporte = medioTransporte;
    }

    public Integer getTiempoTraslado() {
        return this.tiempoTraslado;
    }

    public InfoSocioeconomica tiempoTraslado(Integer tiempoTraslado) {
        this.setTiempoTraslado(tiempoTraslado);
        return this;
    }

    public void setTiempoTraslado(Integer tiempoTraslado) {
        this.tiempoTraslado = tiempoTraslado;
    }

    public LocalDate getFechaActualizacion() {
        return this.fechaActualizacion;
    }

    public InfoSocioeconomica fechaActualizacion(LocalDate fechaActualizacion) {
        this.setFechaActualizacion(fechaActualizacion);
        return this;
    }

    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InfoSocioeconomica{" +
            "id=" + getId() +
            ", tipoVivienda='" + getTipoVivienda() + "'" +
            ", materialVivienda='" + getMaterialVivienda() + "'" +
            ", numeroHabitaciones=" + getNumeroHabitaciones() +
            ", numeroHabitantes=" + getNumeroHabitantes() +
            ", serviciosDisponibles='" + getServiciosDisponibles() + "'" +
            ", ingresoMensual=" + getIngresoMensual() +
            ", ingresoMensualHogar=" + getIngresoMensualHogar() +
            ", gastoMensual=" + getGastoMensual() +
            ", personasDependientes=" + getPersonasDependientes() +
            ", apoyosGubernamentales='" + getApoyosGubernamentales() + "'" +
            ", ocupacionActual='" + getOcupacionActual() + "'" +
            ", condicionLaboral='" + getCondicionLaboral() + "'" +
            ", tipoEmpleo='" + getTipoEmpleo() + "'" +
            ", lugarTrabajo='" + getLugarTrabajo() + "'" +
            ", tiempoEmpleado='" + getTiempoEmpleado() + "'" +
            ", gradoMaximoEstudios='" + getGradoMaximoEstudios() + "'" +
            ", aniosEstudio=" + getAniosEstudio() +
            ", estudia='" + getEstudia() + "'" +
            ", institucionMedica='" + getInstitucionMedica() + "'" +
            ", tipoAfiliacion='" + getTipoAfiliacion() + "'" +
            ", numeroAfiliacion='" + getNumeroAfiliacion() + "'" +
            ", medioTransporte='" + getMedioTransporte() + "'" +
            ", tiempoTraslado=" + getTiempoTraslado() +
            ", fechaActualizacion='" + getFechaActualizacion() + "'" +
            "}";
    }
}
