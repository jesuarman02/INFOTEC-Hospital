package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.HistorialMedicoAsserts.*;
import static mx.infotec.pacientesms.domain.HistorialMedicoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistorialMedicoMapperTest {

    private HistorialMedicoMapper historialMedicoMapper;

    @BeforeEach
    void setUp() {
        historialMedicoMapper = new HistorialMedicoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistorialMedicoSample1();
        var actual = historialMedicoMapper.toEntity(historialMedicoMapper.toDto(expected));
        assertHistorialMedicoAllPropertiesEquals(expected, actual);
    }
}
