package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.PacienteAlergiaAsserts.*;
import static mx.infotec.pacientesms.domain.PacienteAlergiaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PacienteAlergiaMapperTest {

    private PacienteAlergiaMapper pacienteAlergiaMapper;

    @BeforeEach
    void setUp() {
        pacienteAlergiaMapper = new PacienteAlergiaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPacienteAlergiaSample1();
        var actual = pacienteAlergiaMapper.toEntity(pacienteAlergiaMapper.toDto(expected));
        assertPacienteAlergiaAllPropertiesEquals(expected, actual);
    }
}
