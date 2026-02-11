package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.EnfermedadTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteEnfermedadTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnfermedadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enfermedad.class);
        Enfermedad enfermedad1 = getEnfermedadSample1();
        Enfermedad enfermedad2 = new Enfermedad();
        assertThat(enfermedad1).isNotEqualTo(enfermedad2);

        enfermedad2.setId(enfermedad1.getId());
        assertThat(enfermedad1).isEqualTo(enfermedad2);

        enfermedad2 = getEnfermedadSample2();
        assertThat(enfermedad1).isNotEqualTo(enfermedad2);
    }

    @Test
    void pacientesTest() {
        Enfermedad enfermedad = getEnfermedadRandomSampleGenerator();
        PacienteEnfermedad pacienteEnfermedadBack = getPacienteEnfermedadRandomSampleGenerator();

        enfermedad.addPacientes(pacienteEnfermedadBack);
        assertThat(enfermedad.getPacientes()).containsOnly(pacienteEnfermedadBack);
        assertThat(pacienteEnfermedadBack.getEnfermedad()).isEqualTo(enfermedad);

        enfermedad.removePacientes(pacienteEnfermedadBack);
        assertThat(enfermedad.getPacientes()).doesNotContain(pacienteEnfermedadBack);
        assertThat(pacienteEnfermedadBack.getEnfermedad()).isNull();

        enfermedad.pacientes(new HashSet<>(Set.of(pacienteEnfermedadBack)));
        assertThat(enfermedad.getPacientes()).containsOnly(pacienteEnfermedadBack);
        assertThat(pacienteEnfermedadBack.getEnfermedad()).isEqualTo(enfermedad);

        enfermedad.setPacientes(new HashSet<>());
        assertThat(enfermedad.getPacientes()).doesNotContain(pacienteEnfermedadBack);
        assertThat(pacienteEnfermedadBack.getEnfermedad()).isNull();
    }
}
