package mx.infotec.pacientesms.service.mapper;

import static mx.infotec.pacientesms.domain.SignosVitalesAsserts.*;
import static mx.infotec.pacientesms.domain.SignosVitalesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SignosVitalesMapperTest {

    private SignosVitalesMapper signosVitalesMapper;

    @BeforeEach
    void setUp() {
        signosVitalesMapper = new SignosVitalesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSignosVitalesSample1();
        var actual = signosVitalesMapper.toEntity(signosVitalesMapper.toDto(expected));
        assertSignosVitalesAllPropertiesEquals(expected, actual);
    }
}
