package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.EntidadFederativaAsserts.*;
import static mx.infotec.pacientesms.domain.EntidadFederativaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntidadFederativaMapperTest {

    private EntidadFederativaMapper entidadFederativaMapper;

    @BeforeEach
    void setUp() {
        entidadFederativaMapper = new EntidadFederativaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEntidadFederativaSample1();
        var actual = entidadFederativaMapper.toEntity(entidadFederativaMapper.toDto(expected));
        assertEntidadFederativaAllPropertiesEquals(expected, actual);
    }
}
