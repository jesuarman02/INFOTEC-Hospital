package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.PacienteAsserts.*;
import static mx.infotec.pacientesms.domain.PacienteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PacienteMapperTest {

    private PacienteMapper pacienteMapper;

    @BeforeEach
    void setUp() {
        pacienteMapper = new PacienteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPacienteSample1();
        var actual = pacienteMapper.toEntity(pacienteMapper.toDto(expected));
        assertPacienteAllPropertiesEquals(expected, actual);
    }
}
