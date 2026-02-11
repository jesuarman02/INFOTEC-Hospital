package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.DireccionAsserts.*;
import static mx.infotec.pacientesms.domain.DireccionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DireccionMapperTest {

    private DireccionMapper direccionMapper;

    @BeforeEach
    void setUp() {
        direccionMapper = new DireccionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDireccionSample1();
        var actual = direccionMapper.toEntity(direccionMapper.toDto(expected));
        assertDireccionAllPropertiesEquals(expected, actual);
    }
}
