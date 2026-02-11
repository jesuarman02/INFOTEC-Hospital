package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.EnfermedadAsserts.*;
import static mx.infotec.pacientesms.domain.EnfermedadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnfermedadMapperTest {

    private EnfermedadMapper enfermedadMapper;

    @BeforeEach
    void setUp() {
        enfermedadMapper = new EnfermedadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnfermedadSample1();
        var actual = enfermedadMapper.toEntity(enfermedadMapper.toDto(expected));
        assertEnfermedadAllPropertiesEquals(expected, actual);
    }
}
