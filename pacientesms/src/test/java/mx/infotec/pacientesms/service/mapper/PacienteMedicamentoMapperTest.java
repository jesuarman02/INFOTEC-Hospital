package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.PacienteMedicamentoAsserts.*;
import static mx.infotec.pacientesms.domain.PacienteMedicamentoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PacienteMedicamentoMapperTest {

    private PacienteMedicamentoMapper pacienteMedicamentoMapper;

    @BeforeEach
    void setUp() {
        pacienteMedicamentoMapper = new PacienteMedicamentoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPacienteMedicamentoSample1();
        var actual = pacienteMedicamentoMapper.toEntity(pacienteMedicamentoMapper.toDto(expected));
        assertPacienteMedicamentoAllPropertiesEquals(expected, actual);
    }
}
