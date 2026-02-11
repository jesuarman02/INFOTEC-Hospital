package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.TipoVialidadAsserts.*;
import static mx.infotec.pacientesms.domain.TipoVialidadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoVialidadMapperTest {

    private TipoVialidadMapper tipoVialidadMapper;

    @BeforeEach
    void setUp() {
        tipoVialidadMapper = new TipoVialidadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoVialidadSample1();
        var actual = tipoVialidadMapper.toEntity(tipoVialidadMapper.toDto(expected));
        assertTipoVialidadAllPropertiesEquals(expected, actual);
    }
}
