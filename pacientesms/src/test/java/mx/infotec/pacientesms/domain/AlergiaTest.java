package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.AlergiaTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteAlergiaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlergiaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alergia.class);
        Alergia alergia1 = getAlergiaSample1();
        Alergia alergia2 = new Alergia();
        assertThat(alergia1).isNotEqualTo(alergia2);

        alergia2.setId(alergia1.getId());
        assertThat(alergia1).isEqualTo(alergia2);

        alergia2 = getAlergiaSample2();
        assertThat(alergia1).isNotEqualTo(alergia2);
    }

    @Test
    void pacientesTest() {
        Alergia alergia = getAlergiaRandomSampleGenerator();
        PacienteAlergia pacienteAlergiaBack = getPacienteAlergiaRandomSampleGenerator();

        alergia.addPacientes(pacienteAlergiaBack);
        assertThat(alergia.getPacientes()).containsOnly(pacienteAlergiaBack);
        assertThat(pacienteAlergiaBack.getAlergia()).isEqualTo(alergia);

        alergia.removePacientes(pacienteAlergiaBack);
        assertThat(alergia.getPacientes()).doesNotContain(pacienteAlergiaBack);
        assertThat(pacienteAlergiaBack.getAlergia()).isNull();

        alergia.pacientes(new HashSet<>(Set.of(pacienteAlergiaBack)));
        assertThat(alergia.getPacientes()).containsOnly(pacienteAlergiaBack);
        assertThat(pacienteAlergiaBack.getAlergia()).isEqualTo(alergia);

        alergia.setPacientes(new HashSet<>());
        assertThat(alergia.getPacientes()).doesNotContain(pacienteAlergiaBack);
        assertThat(pacienteAlergiaBack.getAlergia()).isNull();
    }
}
