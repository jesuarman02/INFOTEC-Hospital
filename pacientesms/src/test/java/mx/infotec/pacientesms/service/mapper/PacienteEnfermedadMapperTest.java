package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.PacienteEnfermedadAsserts.*;
import static mx.infotec.pacientesms.domain.PacienteEnfermedadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PacienteEnfermedadMapperTest {

    private PacienteEnfermedadMapper pacienteEnfermedadMapper;

    @BeforeEach
    void setUp() {
        pacienteEnfermedadMapper = new PacienteEnfermedadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPacienteEnfermedadSample1();
        var actual = pacienteEnfermedadMapper.toEntity(pacienteEnfermedadMapper.toDto(expected));
        assertPacienteEnfermedadAllPropertiesEquals(expected, actual);
    }
}
