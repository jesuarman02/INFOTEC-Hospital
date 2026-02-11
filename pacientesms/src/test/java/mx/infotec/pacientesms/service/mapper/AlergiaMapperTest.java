package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.AlergiaAsserts.*;
import static mx.infotec.pacientesms.domain.AlergiaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlergiaMapperTest {

    private AlergiaMapper alergiaMapper;

    @BeforeEach
    void setUp() {
        alergiaMapper = new AlergiaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlergiaSample1();
        var actual = alergiaMapper.toEntity(alergiaMapper.toDto(expected));
        assertAlergiaAllPropertiesEquals(expected, actual);
    }
}
