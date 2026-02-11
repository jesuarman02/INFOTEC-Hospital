package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.CodigoPostalAsserts.*;
import static mx.infotec.pacientesms.domain.CodigoPostalTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodigoPostalMapperTest {

    private CodigoPostalMapper codigoPostalMapper;

    @BeforeEach
    void setUp() {
        codigoPostalMapper = new CodigoPostalMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCodigoPostalSample1();
        var actual = codigoPostalMapper.toEntity(codigoPostalMapper.toDto(expected));
        assertCodigoPostalAllPropertiesEquals(expected, actual);
    }
}
