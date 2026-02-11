package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.EnfermedadTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteEnfermedadTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteEnfermedadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PacienteEnfermedad.class);
        PacienteEnfermedad pacienteEnfermedad1 = getPacienteEnfermedadSample1();
        PacienteEnfermedad pacienteEnfermedad2 = new PacienteEnfermedad();
        assertThat(pacienteEnfermedad1).isNotEqualTo(pacienteEnfermedad2);

        pacienteEnfermedad2.setId(pacienteEnfermedad1.getId());
        assertThat(pacienteEnfermedad1).isEqualTo(pacienteEnfermedad2);

        pacienteEnfermedad2 = getPacienteEnfermedadSample2();
        assertThat(pacienteEnfermedad1).isNotEqualTo(pacienteEnfermedad2);
    }

    @Test
    void pacienteTest() {
        PacienteEnfermedad pacienteEnfermedad = getPacienteEnfermedadRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        pacienteEnfermedad.setPaciente(pacienteBack);
        assertThat(pacienteEnfermedad.getPaciente()).isEqualTo(pacienteBack);

        pacienteEnfermedad.paciente(null);
        assertThat(pacienteEnfermedad.getPaciente()).isNull();
    }

    @Test
    void enfermedadTest() {
        PacienteEnfermedad pacienteEnfermedad = getPacienteEnfermedadRandomSampleGenerator();
        Enfermedad enfermedadBack = getEnfermedadRandomSampleGenerator();

        pacienteEnfermedad.setEnfermedad(enfermedadBack);
        assertThat(pacienteEnfermedad.getEnfermedad()).isEqualTo(enfermedadBack);

        pacienteEnfermedad.enfermedad(null);
        assertThat(pacienteEnfermedad.getEnfermedad()).isNull();
    }
}
