package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.DireccionTestSamples.*;
import static mx.infotec.pacientesms.domain.EntidadFederativaTestSamples.*;
import static mx.infotec.pacientesms.domain.HistorialMedicoTestSamples.*;
import static mx.infotec.pacientesms.domain.InfoSocioeconomicaTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteAlergiaTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteEnfermedadTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteMedicamentoTestSamples.*;
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
    void infoSocioeconomicaTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        InfoSocioeconomica infoSocioeconomicaBack = getInfoSocioeconomicaRandomSampleGenerator();

        paciente.setInfoSocioeconomica(infoSocioeconomicaBack);
        assertThat(paciente.getInfoSocioeconomica()).isEqualTo(infoSocioeconomicaBack);

        paciente.infoSocioeconomica(null);
        assertThat(paciente.getInfoSocioeconomica()).isNull();
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

    @Test
    void misEnfermedadesTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        PacienteEnfermedad pacienteEnfermedadBack = getPacienteEnfermedadRandomSampleGenerator();

        paciente.addMisEnfermedades(pacienteEnfermedadBack);
        assertThat(paciente.getMisEnfermedades()).containsOnly(pacienteEnfermedadBack);
        assertThat(pacienteEnfermedadBack.getPaciente()).isEqualTo(paciente);

        paciente.removeMisEnfermedades(pacienteEnfermedadBack);
        assertThat(paciente.getMisEnfermedades()).doesNotContain(pacienteEnfermedadBack);
        assertThat(pacienteEnfermedadBack.getPaciente()).isNull();

        paciente.misEnfermedades(new HashSet<>(Set.of(pacienteEnfermedadBack)));
        assertThat(paciente.getMisEnfermedades()).containsOnly(pacienteEnfermedadBack);
        assertThat(pacienteEnfermedadBack.getPaciente()).isEqualTo(paciente);

        paciente.setMisEnfermedades(new HashSet<>());
        assertThat(paciente.getMisEnfermedades()).doesNotContain(pacienteEnfermedadBack);
        assertThat(pacienteEnfermedadBack.getPaciente()).isNull();
    }

    @Test
    void misAlergiasTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        PacienteAlergia pacienteAlergiaBack = getPacienteAlergiaRandomSampleGenerator();

        paciente.addMisAlergias(pacienteAlergiaBack);
        assertThat(paciente.getMisAlergias()).containsOnly(pacienteAlergiaBack);
        assertThat(pacienteAlergiaBack.getPaciente()).isEqualTo(paciente);

        paciente.removeMisAlergias(pacienteAlergiaBack);
        assertThat(paciente.getMisAlergias()).doesNotContain(pacienteAlergiaBack);
        assertThat(pacienteAlergiaBack.getPaciente()).isNull();

        paciente.misAlergias(new HashSet<>(Set.of(pacienteAlergiaBack)));
        assertThat(paciente.getMisAlergias()).containsOnly(pacienteAlergiaBack);
        assertThat(pacienteAlergiaBack.getPaciente()).isEqualTo(paciente);

        paciente.setMisAlergias(new HashSet<>());
        assertThat(paciente.getMisAlergias()).doesNotContain(pacienteAlergiaBack);
        assertThat(pacienteAlergiaBack.getPaciente()).isNull();
    }

    @Test
    void misMedicamentosTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        PacienteMedicamento pacienteMedicamentoBack = getPacienteMedicamentoRandomSampleGenerator();

        paciente.addMisMedicamentos(pacienteMedicamentoBack);
        assertThat(paciente.getMisMedicamentos()).containsOnly(pacienteMedicamentoBack);
        assertThat(pacienteMedicamentoBack.getPaciente()).isEqualTo(paciente);

        paciente.removeMisMedicamentos(pacienteMedicamentoBack);
        assertThat(paciente.getMisMedicamentos()).doesNotContain(pacienteMedicamentoBack);
        assertThat(pacienteMedicamentoBack.getPaciente()).isNull();

        paciente.misMedicamentos(new HashSet<>(Set.of(pacienteMedicamentoBack)));
        assertThat(paciente.getMisMedicamentos()).containsOnly(pacienteMedicamentoBack);
        assertThat(pacienteMedicamentoBack.getPaciente()).isEqualTo(paciente);

        paciente.setMisMedicamentos(new HashSet<>());
        assertThat(paciente.getMisMedicamentos()).doesNotContain(pacienteMedicamentoBack);
        assertThat(pacienteMedicamentoBack.getPaciente()).isNull();
    }

    @Test
    void entidadNacimientoTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        EntidadFederativa entidadFederativaBack = getEntidadFederativaRandomSampleGenerator();

        paciente.setEntidadNacimiento(entidadFederativaBack);
        assertThat(paciente.getEntidadNacimiento()).isEqualTo(entidadFederativaBack);

        paciente.entidadNacimiento(null);
        assertThat(paciente.getEntidadNacimiento()).isNull();
    }
}
