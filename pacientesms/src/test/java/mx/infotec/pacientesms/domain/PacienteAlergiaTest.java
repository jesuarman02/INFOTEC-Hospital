package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.AlergiaTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteAlergiaTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteAlergiaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PacienteAlergia.class);
        PacienteAlergia pacienteAlergia1 = getPacienteAlergiaSample1();
        PacienteAlergia pacienteAlergia2 = new PacienteAlergia();
        assertThat(pacienteAlergia1).isNotEqualTo(pacienteAlergia2);

        pacienteAlergia2.setId(pacienteAlergia1.getId());
        assertThat(pacienteAlergia1).isEqualTo(pacienteAlergia2);

        pacienteAlergia2 = getPacienteAlergiaSample2();
        assertThat(pacienteAlergia1).isNotEqualTo(pacienteAlergia2);
    }

    @Test
    void pacienteTest() {
        PacienteAlergia pacienteAlergia = getPacienteAlergiaRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        pacienteAlergia.setPaciente(pacienteBack);
        assertThat(pacienteAlergia.getPaciente()).isEqualTo(pacienteBack);

        pacienteAlergia.paciente(null);
        assertThat(pacienteAlergia.getPaciente()).isNull();
    }

    @Test
    void alergiaTest() {
        PacienteAlergia pacienteAlergia = getPacienteAlergiaRandomSampleGenerator();
        Alergia alergiaBack = getAlergiaRandomSampleGenerator();

        pacienteAlergia.setAlergia(alergiaBack);
        assertThat(pacienteAlergia.getAlergia()).isEqualTo(alergiaBack);

        pacienteAlergia.alergia(null);
        assertThat(pacienteAlergia.getAlergia()).isNull();
    }
}
