package mx.infotec.pacientesms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link mx.infotec.pacientesms.domain.CodigoPostal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CodigoPostalDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 5)
    private String codigo;

    @Size(max = 100)
    private String asentamiento;

    @Size(max = 50)
    private String tipoAsentamiento;

    @Size(max = 100)
    private String municipio;

    @Size(max = 100)
    private String estado;

    @Size(max = 100)
    private String ciudad;

    @Size(max = 5)
    private String codigoPostalAdministracion;

    @Size(max = 5)
    private String claveEstado;

    @Size(max = 5)
    private String claveOficina;

    @Size(max = 5)
    private String claveCP;

    @Size(max = 5)
    private String claveTipoAsentamiento;

    @Size(max = 5)
    private String claveMunicipio;

    @Size(max = 10)
    private String idAsentamientoCons;

    @Size(max = 50)
    private String zona;

    @Size(max = 5)
    private String claveCiudad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(String asentamiento) {
        this.asentamiento = asentamiento;
    }

    public String getTipoAsentamiento() {
        return tipoAsentamiento;
    }

    public void setTipoAsentamiento(String tipoAsentamiento) {
        this.tipoAsentamiento = tipoAsentamiento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostalAdministracion() {
        return codigoPostalAdministracion;
    }

    public void setCodigoPostalAdministracion(String codigoPostalAdministracion) {
        this.codigoPostalAdministracion = codigoPostalAdministracion;
    }

    public String getClaveEstado() {
        return claveEstado;
    }

    public void setClaveEstado(String claveEstado) {
        this.claveEstado = claveEstado;
    }

    public String getClaveOficina() {
        return claveOficina;
    }

    public void setClaveOficina(String claveOficina) {
        this.claveOficina = claveOficina;
    }

    public String getClaveCP() {
        return claveCP;
    }

    public void setClaveCP(String claveCP) {
        this.claveCP = claveCP;
    }

    public String getClaveTipoAsentamiento() {
        return claveTipoAsentamiento;
    }

    public void setClaveTipoAsentamiento(String claveTipoAsentamiento) {
        this.claveTipoAsentamiento = claveTipoAsentamiento;
    }

    public String getClaveMunicipio() {
        return claveMunicipio;
    }

    public void setClaveMunicipio(String claveMunicipio) {
        this.claveMunicipio = claveMunicipio;
    }

    public String getIdAsentamientoCons() {
        return idAsentamientoCons;
    }

    public void setIdAsentamientoCons(String idAsentamientoCons) {
        this.idAsentamientoCons = idAsentamientoCons;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getClaveCiudad() {
        return claveCiudad;
    }

    public void setClaveCiudad(String claveCiudad) {
        this.claveCiudad = claveCiudad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodigoPostalDTO)) {
            return false;
        }

        CodigoPostalDTO codigoPostalDTO = (CodigoPostalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, codigoPostalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodigoPostalDTO{" +
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
