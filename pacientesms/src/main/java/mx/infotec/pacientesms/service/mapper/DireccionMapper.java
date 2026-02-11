package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Direccion;
import mx.infotec.pacientesms.domain.EntidadFederativa;
import mx.infotec.pacientesms.domain.TipoVialidad;
import mx.infotec.pacientesms.domain.CodigoPostal;
import mx.infotec.pacientesms.service.dto.DireccionDTO;
import mx.infotec.pacientesms.service.dto.EntidadFederativaDTO;
import mx.infotec.pacientesms.service.dto.TipoVialidadDTO;
import mx.infotec.pacientesms.service.dto.CodigoPostalDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { 
    EntidadFederativaMapper.class, 
    TipoVialidadMapper.class, 
    CodigoPostalMapper.class 
})
public interface DireccionMapper extends EntityMapper<DireccionDTO, Direccion> {

    // LECTURA (Entity -> DTO)
    @Mapping(target = "entidadFederativaId", source = "entidadFederativa.id")
    @Mapping(target = "entidadFederativaNombre", source = "entidadFederativa.nombre")
    @Mapping(target = "entidadFederativa", source = "entidadFederativa")
    
    @Mapping(target = "tipoVialidadId", source = "tipoVialidad.id")
    @Mapping(target = "tipoVialidadNombre", source = "tipoVialidad.nombre")
    @Mapping(target = "tipoVialidad", source = "tipoVialidad")

    @Mapping(target = "codigoPostalInfoId", source = "codigoPostalInfo.id")
    @Mapping(target = "codigoPostalInfoCodigo", source = "codigoPostalInfo.codigo")
    @Mapping(target = "codigoPostalInfo", source = "codigoPostalInfo")
    DireccionDTO toDto(Direccion s);


    // ESCRITURA (DTO -> Entity)
    @Mapping(target = "entidadFederativa", source = "entidadFederativaId")
    @Mapping(target = "tipoVialidad", source = "tipoVialidadId")
    @Mapping(target = "codigoPostalInfo", source = "codigoPostalInfoId")
    Direccion toEntity(DireccionDTO direccionDTO);

    // Métodos para convertir DTOs parciales (Nombre) para mostrar en listas
    @Named("entidadFederativaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    EntidadFederativaDTO toDtoEntidadFederativaNombre(EntidadFederativa entidadFederativa);

    @Named("tipoVialidadNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TipoVialidadDTO toDtoTipoVialidadNombre(TipoVialidad tipoVialidad);

    @Named("codigoPostalCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    CodigoPostalDTO toDtoCodigoPostalCodigo(CodigoPostal codigoPostal);

    // Helpers para crear entidades vacías desde ID
    default EntidadFederativa fromIdEntidad(Long id) {
        if (id == null) return null;
        EntidadFederativa obj = new EntidadFederativa();
        obj.setId(id);
        return obj;
    }

    default TipoVialidad fromIdVialidad(Long id) {
        if (id == null) return null;
        TipoVialidad obj = new TipoVialidad();
        obj.setId(id);
        return obj;
    }

    default CodigoPostal fromIdCP(Long id) {
        if (id == null) return null;
        CodigoPostal obj = new CodigoPostal();
        obj.setId(id);
        return obj;
    }
}