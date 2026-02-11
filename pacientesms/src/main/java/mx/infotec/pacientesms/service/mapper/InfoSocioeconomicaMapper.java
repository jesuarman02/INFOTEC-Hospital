package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.InfoSocioeconomica;
import mx.infotec.pacientesms.service.dto.InfoSocioeconomicaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InfoSocioeconomica} and its DTO {@link InfoSocioeconomicaDTO}.
 */
@Mapper(componentModel = "spring")
public interface InfoSocioeconomicaMapper extends EntityMapper<InfoSocioeconomicaDTO, InfoSocioeconomica> {}
