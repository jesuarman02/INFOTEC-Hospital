package mx.infotec.pacientesms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import jakarta.validation.constraints.*;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class DireccionDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String nombreVialidad;

    @Size(max = 20)
    private String numExterior;

    @Size(max = 20)
    private String numInterior;

    @Size(max = 15)
    private String telefono;

    // ==========================================
    // 1. TIPO DE VIALIDAD (Nuevo)
    // ==========================================
    private Long tipoVialidadId;
    private String tipoVialidadNombre;
    private TipoVialidadDTO tipoVialidad;

    // ==========================================
    // 2. CÓDIGO POSTAL (Nuevo - Relación)
    // ==========================================
    // Nota: En el JDL le llamamos 'codigoPostalInfo' a la relación
    private Long codigoPostalInfoId;
    private String codigoPostalInfoCodigo; 
    private CodigoPostalDTO codigoPostalInfo;

    // ==========================================
    // 3. ENTIDAD FEDERATIVA (Restaurado)
    // ==========================================
    private Long entidadFederativaId;
    private String entidadFederativaNombre;
    private EntidadFederativaDTO entidadFederativa;

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreVialidad() { return nombreVialidad; }
    public void setNombreVialidad(String nombreVialidad) { this.nombreVialidad = nombreVialidad; }

    public String getNumExterior() { return numExterior; }
    public void setNumExterior(String numExterior) { this.numExterior = numExterior; }

    public String getNumInterior() { return numInterior; }
    public void setNumInterior(String numInterior) { this.numInterior = numInterior; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // Vialidad
    public Long getTipoVialidadId() { return tipoVialidadId; }
    public void setTipoVialidadId(Long tipoVialidadId) { this.tipoVialidadId = tipoVialidadId; }
    
    public String getTipoVialidadNombre() { return tipoVialidadNombre; }
    public void setTipoVialidadNombre(String tipoVialidadNombre) { this.tipoVialidadNombre = tipoVialidadNombre; }

    public TipoVialidadDTO getTipoVialidad() { return tipoVialidad; }
    public void setTipoVialidad(TipoVialidadDTO tipoVialidad) { this.tipoVialidad = tipoVialidad; }

    // Codigo Postal
    public Long getCodigoPostalInfoId() { return codigoPostalInfoId; }
    public void setCodigoPostalInfoId(Long codigoPostalInfoId) { this.codigoPostalInfoId = codigoPostalInfoId; }

    public String getCodigoPostalInfoCodigo() { return codigoPostalInfoCodigo; }
    public void setCodigoPostalInfoCodigo(String codigoPostalInfoCodigo) { this.codigoPostalInfoCodigo = codigoPostalInfoCodigo; }

    public CodigoPostalDTO getCodigoPostalInfo() { return codigoPostalInfo; }
    public void setCodigoPostalInfo(CodigoPostalDTO codigoPostalInfo) { this.codigoPostalInfo = codigoPostalInfo; }

    // Entidad Federativa
    public Long getEntidadFederativaId() { return entidadFederativaId; }
    public void setEntidadFederativaId(Long entidadFederativaId) { this.entidadFederativaId = entidadFederativaId; }

    public String getEntidadFederativaNombre() { return entidadFederativaNombre; }
    public void setEntidadFederativaNombre(String entidadFederativaNombre) { this.entidadFederativaNombre = entidadFederativaNombre; }

    public EntidadFederativaDTO getEntidadFederativa() { return entidadFederativa; }
    public void setEntidadFederativa(EntidadFederativaDTO entidadFederativa) { this.entidadFederativa = entidadFederativa; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof DireccionDTO)) { return false; }
        DireccionDTO direccionDTO = (DireccionDTO) o;
        if (this.id == null) { return false; }
        return Objects.equals(this.id, direccionDTO.id);
    }

    @Override
    public int hashCode() { return Objects.hash(this.id); }

    @Override
    public String toString() {
        return "DireccionDTO{" +
            "id=" + getId() +
            ", nombreVialidad='" + getNombreVialidad() + "'" +
            ", numExterior='" + getNumExterior() + "'" +
            ", numInterior='" + getNumInterior() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", tipoVialidad=" + getTipoVialidadId() +
            ", codigoPostal=" + getCodigoPostalInfoId() +
            ", entidad=" + getEntidadFederativaId() +
            "}";
    }
}