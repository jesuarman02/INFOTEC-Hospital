package mx.infotec.pacientesms.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CodigoPostal.
 */
@Table("codigo_postal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CodigoPostal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 5)
    @Column("codigo")
    private String codigo;

    @Size(max = 100)
    @Column("asentamiento")
    private String asentamiento;

    @Size(max = 50)
    @Column("tipo_asentamiento")
    private String tipoAsentamiento;

    @Size(max = 100)
    @Column("municipio")
    private String municipio;

    @Size(max = 100)
    @Column("estado")
    private String estado;

    @Size(max = 100)
    @Column("ciudad")
    private String ciudad;

    @Size(max = 5)
    @Column("codigo_postal_administracion")
    private String codigoPostalAdministracion;

    @Size(max = 5)
    @Column("clave_estado")
    private String claveEstado;

    @Size(max = 5)
    @Column("clave_oficina")
    private String claveOficina;

    @Size(max = 5)
    @Column("clave_cp")
    private String claveCP;

    @Size(max = 5)
    @Column("clave_tipo_asentamiento")
    private String claveTipoAsentamiento;

    @Size(max = 5)
    @Column("clave_municipio")
    private String claveMunicipio;

    @Size(max = 10)
    @Column("id_asentamiento_cons")
    private String idAsentamientoCons;

    @Size(max = 50)
    @Column("zona")
    private String zona;

    @Size(max = 5)
    @Column("clave_ciudad")
    private String claveCiudad;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CodigoPostal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public CodigoPostal codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAsentamiento() {
        return this.asentamiento;
    }

    public CodigoPostal asentamiento(String asentamiento) {
        this.setAsentamiento(asentamiento);
        return this;
    }

    public void setAsentamiento(String asentamiento) {
        this.asentamiento = asentamiento;
    }

    public String getTipoAsentamiento() {
        return this.tipoAsentamiento;
    }

    public CodigoPostal tipoAsentamiento(String tipoAsentamiento) {
        this.setTipoAsentamiento(tipoAsentamiento);
        return this;
    }

    public void setTipoAsentamiento(String tipoAsentamiento) {
        this.tipoAsentamiento = tipoAsentamiento;
    }

    public String getMunicipio() {
        return this.municipio;
    }

    public CodigoPostal municipio(String municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return this.estado;
    }

    public CodigoPostal estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public CodigoPostal ciudad(String ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostalAdministracion() {
        return this.codigoPostalAdministracion;
    }

    public CodigoPostal codigoPostalAdministracion(String codigoPostalAdministracion) {
        this.setCodigoPostalAdministracion(codigoPostalAdministracion);
        return this;
    }

    public void setCodigoPostalAdministracion(String codigoPostalAdministracion) {
        this.codigoPostalAdministracion = codigoPostalAdministracion;
    }

    public String getClaveEstado() {
        return this.claveEstado;
    }

    public CodigoPostal claveEstado(String claveEstado) {
        this.setClaveEstado(claveEstado);
        return this;
    }

    public void setClaveEstado(String claveEstado) {
        this.claveEstado = claveEstado;
    }

    public String getClaveOficina() {
        return this.claveOficina;
    }

    public CodigoPostal claveOficina(String claveOficina) {
        this.setClaveOficina(claveOficina);
        return this;
    }

    public void setClaveOficina(String claveOficina) {
        this.claveOficina = claveOficina;
    }

    public String getClaveCP() {
        return this.claveCP;
    }

    public CodigoPostal claveCP(String claveCP) {
        this.setClaveCP(claveCP);
        return this;
    }

    public void setClaveCP(String claveCP) {
        this.claveCP = claveCP;
    }

    public String getClaveTipoAsentamiento() {
        return this.claveTipoAsentamiento;
    }

    public CodigoPostal claveTipoAsentamiento(String claveTipoAsentamiento) {
        this.setClaveTipoAsentamiento(claveTipoAsentamiento);
        return this;
    }

    public void setClaveTipoAsentamiento(String claveTipoAsentamiento) {
        this.claveTipoAsentamiento = claveTipoAsentamiento;
    }

    public String getClaveMunicipio() {
        return this.claveMunicipio;
    }

    public CodigoPostal claveMunicipio(String claveMunicipio) {
        this.setClaveMunicipio(claveMunicipio);
        return this;
    }

    public void setClaveMunicipio(String claveMunicipio) {
        this.claveMunicipio = claveMunicipio;
    }

    public String getIdAsentamientoCons() {
        return this.idAsentamientoCons;
    }

    public CodigoPostal idAsentamientoCons(String idAsentamientoCons) {
        this.setIdAsentamientoCons(idAsentamientoCons);
        return this;
    }

    public void setIdAsentamientoCons(String idAsentamientoCons) {
        this.idAsentamientoCons = idAsentamientoCons;
    }

    public String getZona() {
        return this.zona;
    }

    public CodigoPostal zona(String zona) {
        this.setZona(zona);
        return this;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getClaveCiudad() {
        return this.claveCiudad;
    }

    public CodigoPostal claveCiudad(String claveCiudad) {
        this.setClaveCiudad(claveCiudad);
        return this;
    }

    public void setClaveCiudad(String claveCiudad) {
        this.claveCiudad = claveCiudad;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodigoPostal)) {
            return false;
        }
        return getId() != null && getId().equals(((CodigoPostal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodigoPostal{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", asentamiento='" + getAsentamiento() + "'" +
            ", tipoAsentamiento='" + getTipoAsentamiento() + "'" +
            ", municipio='" + getMunicipio() + "'" +
            ", estado='" + getEstado() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", codigoPostalAdministracion='" + getCodigoPostalAdministracion() + "'" +
            ", claveEstado='" + getClaveEstado() + "'" +
            ", claveOficina='" + getClaveOficina() + "'" +
            ", claveCP='" + getClaveCP() + "'" +
            ", claveTipoAsentamiento='" + getClaveTipoAsentamiento() + "'" +
            ", claveMunicipio='" + getClaveMunicipio() + "'" +
            ", idAsentamientoCons='" + getIdAsentamientoCons() + "'" +
            ", zona='" + getZona() + "'" +
            ", claveCiudad='" + getClaveCiudad() + "'" +
            "}";
    }
}
