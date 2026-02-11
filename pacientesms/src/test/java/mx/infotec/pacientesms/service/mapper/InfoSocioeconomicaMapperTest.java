package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.InfoSocioeconomicaAsserts.*;
import static mx.infotec.pacientesms.domain.InfoSocioeconomicaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InfoSocioeconomicaMapperTest {

    private InfoSocioeconomicaMapper infoSocioeconomicaMapper;

    @BeforeEach
    void setUp() {
        infoSocioeconomicaMapper = new InfoSocioeconomicaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInfoSocioeconomicaSample1();
        var actual = infoSocioeconomicaMapper.toEntity(infoSocioeconomicaMapper.toDto(expected));
        assertInfoSocioeconomicaAllPropertiesEquals(expected, actual);
    }
}
