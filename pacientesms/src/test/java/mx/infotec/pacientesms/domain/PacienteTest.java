package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.DireccionTestSamples.*;
import static mx.infotec.pacientesms.domain.HistorialMedicoTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteTestSamples.*;
import static mx.infotec.pacientesms.domain.SignosVitalesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paciente.class);
        Paciente paciente1 = getPacienteSample1();
        Paciente paciente2 = new Paciente();
        assertThat(paciente1).isNotEqualTo(paciente2);

        paciente2.setId(paciente1.getId());
        assertThat(paciente1).isEqualTo(paciente2);

        paciente2 = getPacienteSample2();
        assertThat(paciente1).isNotEqualTo(paciente2);
    }

    @Test
    void direccionTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Direccion direccionBack = getDireccionRandomSampleGenerator();

        paciente.setDireccion(direccionBack);
        assertThat(paciente.getDireccion()).isEqualTo(direccionBack);

        paciente.direccion(null);
        assertThat(paciente.getDireccion()).isNull();
    }



    @Test
    void historialGeneralTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        HistorialMedico historialMedicoBack = getHistorialMedicoRandomSampleGenerator();

        paciente.setHistorialGeneral(historialMedicoBack);
        assertThat(paciente.getHistorialGeneral()).isEqualTo(historialMedicoBack);

        paciente.historialGeneral(null);
        assertThat(paciente.getHistorialGeneral()).isNull();
    }

    @Test
    void signosVitalesTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        SignosVitales signosVitalesBack = getSignosVitalesRandomSampleGenerator();

        paciente.addSignosVitales(signosVitalesBack);
        assertThat(paciente.getSignosVitales()).containsOnly(signosVitalesBack);
        assertThat(signosVitalesBack.getPaciente()).isEqualTo(paciente);

        paciente.removeSignosVitales(signosVitalesBack);
        assertThat(paciente.getSignosVitales()).doesNotContain(signosVitalesBack);
        assertThat(signosVitalesBack.getPaciente()).isNull();

        paciente.signosVitales(new HashSet<>(Set.of(signosVitalesBack)));
        assertThat(paciente.getSignosVitales()).containsOnly(signosVitalesBack);
        assertThat(signosVitalesBack.getPaciente()).isEqualTo(paciente);

        paciente.setSignosVitales(new HashSet<>());
        assertThat(paciente.getSignosVitales()).doesNotContain(signosVitalesBack);
        assertThat(signosVitalesBack.getPaciente()).isNull();
    }

}
