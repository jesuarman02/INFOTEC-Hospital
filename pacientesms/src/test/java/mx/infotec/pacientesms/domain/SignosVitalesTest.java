package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.PacienteTestSamples.*;
import static mx.infotec.pacientesms.domain.SignosVitalesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SignosVitalesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignosVitales.class);
        SignosVitales signosVitales1 = getSignosVitalesSample1();
        SignosVitales signosVitales2 = new SignosVitales();
        assertThat(signosVitales1).isNotEqualTo(signosVitales2);

        signosVitales2.setId(signosVitales1.getId());
        assertThat(signosVitales1).isEqualTo(signosVitales2);

        signosVitales2 = getSignosVitalesSample2();
        assertThat(signosVitales1).isNotEqualTo(signosVitales2);
    }

    @Test
    void pacienteTest() {
        SignosVitales signosVitales = getSignosVitalesRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        signosVitales.setPaciente(pacienteBack);
        assertThat(signosVitales.getPaciente()).isEqualTo(pacienteBack);

        signosVitales.paciente(null);
        assertThat(signosVitales.getPaciente()).isNull();
    }
}
