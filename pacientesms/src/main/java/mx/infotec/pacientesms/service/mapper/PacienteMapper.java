package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Direccion;
import mx.infotec.pacientesms.domain.EntidadFederativa;
import mx.infotec.pacientesms.domain.HistorialMedico;
import mx.infotec.pacientesms.domain.InfoSocioeconomica;
import mx.infotec.pacientesms.domain.Paciente;
import mx.infotec.pacientesms.service.dto.DireccionDTO;
import mx.infotec.pacientesms.service.dto.EntidadFederativaDTO;
import mx.infotec.pacientesms.service.dto.HistorialMedicoDTO;
import mx.infotec.pacientesms.service.dto.InfoSocioeconomicaDTO;
import mx.infotec.pacientesms.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paciente} and its DTO {@link PacienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {
    @Mapping(target = "direccion", source = "direccion", qualifiedByName = "direccionNombreVialidad")
    @Mapping(target = "infoSocioeconomica", source = "infoSocioeconomica", qualifiedByName = "infoSocioeconomicaId")
    @Mapping(target = "historialGeneral", source = "historialGeneral", qualifiedByName = "historialMedicoId")
    @Mapping(target = "entidadNacimiento", source = "entidadNacimiento", qualifiedByName = "entidadFederativaNombre")
    PacienteDTO toDto(Paciente s);

    @Named("direccionNombreVialidad")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombreVialidad", source = "nombreVialidad")
    DireccionDTO toDtoDireccionNombreVialidad(Direccion direccion);

    @Named("infoSocioeconomicaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InfoSocioeconomicaDTO toDtoInfoSocioeconomicaId(InfoSocioeconomica infoSocioeconomica);

    @Named("historialMedicoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HistorialMedicoDTO toDtoHistorialMedicoId(HistorialMedico historialMedico);

    @Named("entidadFederativaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    EntidadFederativaDTO toDtoEntidadFederativaNombre(EntidadFederativa entidadFederativa);
}
