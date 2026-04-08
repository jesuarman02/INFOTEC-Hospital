package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.HistorialMedicoTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistorialMedicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistorialMedico.class);
        HistorialMedico historialMedico2 = new HistorialMedico();
    }

    @Test
    void pacienteTest() {
        HistorialMedico historialMedico = getHistorialMedicoRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        historialMedico.setPaciente(pacienteBack);
        assertThat(historialMedico.getPaciente()).isEqualTo(pacienteBack);
        assertThat(pacienteBack.getHistorialGeneral()).isEqualTo(historialMedico);

        historialMedico.paciente(null);
        assertThat(historialMedico.getPaciente()).isNull();
        assertThat(pacienteBack.getHistorialGeneral()).isNull();
    }
}
