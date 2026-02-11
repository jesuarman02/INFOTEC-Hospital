package mx.infotec.pacientesms.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Direccion.
 */
@Table("direccion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Direccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Size(max = 100)
    @Column("nombre_vialidad")
    private String nombreVialidad;

    @Size(max = 20)
    @Column("num_exterior")
    private String numExterior;

    @Size(max = 20)
    @Column("num_interior")
    private String numInterior;

    @Size(max = 15)
    @Column("telefono")
    private String telefono;

    @Transient
    private TipoVialidad tipoVialidad;

    @Transient
    private CodigoPostal codigoPostalInfo;

    @Transient
    private EntidadFederativa entidadFederativa;

    @Transient
    private Paciente paciente;

    @Column("tipo_vialidad_id")
    private Long tipoVialidadId;

    @Column("codigo_postal_info_id")
    private Long codigoPostalInfoId;

    @Column("entidad_federativa_id")
    private Long entidadFederativaId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Direccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreVialidad() {
        return this.nombreVialidad;
    }

    public Direccion nombreVialidad(String nombreVialidad) {
        this.setNombreVialidad(nombreVialidad);
        return this;
    }

    public void setNombreVialidad(String nombreVialidad) {
        this.nombreVialidad = nombreVialidad;
    }

    public String getNumExterior() {
        return this.numExterior;
    }

    public Direccion numExterior(String numExterior) {
        this.setNumExterior(numExterior);
        return this;
    }

    public void setNumExterior(String numExterior) {
        this.numExterior = numExterior;
    }

    public String getNumInterior() {
        return this.numInterior;
    }

    public Direccion numInterior(String numInterior) {
        this.setNumInterior(numInterior);
        return this;
    }

    public void setNumInterior(String numInterior) {
        this.numInterior = numInterior;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Direccion telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoVialidad getTipoVialidad() {
        return this.tipoVialidad;
    }

    public void setTipoVialidad(TipoVialidad tipoVialidad) {
        this.tipoVialidad = tipoVialidad;
        this.tipoVialidadId = tipoVialidad != null ? tipoVialidad.getId() : null;
    }

    public Direccion tipoVialidad(TipoVialidad tipoVialidad) {
        this.setTipoVialidad(tipoVialidad);
        return this;
    }

    public CodigoPostal getCodigoPostalInfo() {
        return this.codigoPostalInfo;
    }

    public void setCodigoPostalInfo(CodigoPostal codigoPostal) {
        this.codigoPostalInfo = codigoPostal;
        this.codigoPostalInfoId = codigoPostal != null ? codigoPostal.getId() : null;
    }

    public Direccion codigoPostalInfo(CodigoPostal codigoPostal) {
        this.setCodigoPostalInfo(codigoPostal);
        return this;
    }

    public EntidadFederativa getEntidadFederativa() {
        return this.entidadFederativa;
    }

    public void setEntidadFederativa(EntidadFederativa entidadFederativa) {
        this.entidadFederativa = entidadFederativa;
        this.entidadFederativaId = entidadFederativa != null ? entidadFederativa.getId() : null;
    }

    public Direccion entidadFederativa(EntidadFederativa entidadFederativa) {
        this.setEntidadFederativa(entidadFederativa);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (this.paciente != null) {
            this.paciente.setDireccion(null);
        }
        if (paciente != null) {
            paciente.setDireccion(this);
        }
        this.paciente = paciente;
    }

    public Direccion paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Long getTipoVialidadId() {
        return this.tipoVialidadId;
    }

    public void setTipoVialidadId(Long tipoVialidad) {
        this.tipoVialidadId = tipoVialidad;
    }

    public Long getCodigoPostalInfoId() {
        return this.codigoPostalInfoId;
    }

    public void setCodigoPostalInfoId(Long codigoPostal) {
        this.codigoPostalInfoId = codigoPostal;
    }

    public Long getEntidadFederativaId() {
        return this.entidadFederativaId;
    }

    public void setEntidadFederativaId(Long entidadFederativa) {
        this.entidadFederativaId = entidadFederativa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Direccion)) {
            return false;
        }
        return getId() != null && getId().equals(((Direccion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Direccion{" +
            "id=" + getId() +
            ", nombreVialidad='" + getNombreVialidad() + "'" +
            ", numExterior='" + getNumExterior() + "'" +
            ", numInterior='" + getNumInterior() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
