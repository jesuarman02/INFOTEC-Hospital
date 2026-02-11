package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.MedicamentoTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteMedicamentoTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteMedicamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PacienteMedicamento.class);
        PacienteMedicamento pacienteMedicamento1 = getPacienteMedicamentoSample1();
        PacienteMedicamento pacienteMedicamento2 = new PacienteMedicamento();
        assertThat(pacienteMedicamento1).isNotEqualTo(pacienteMedicamento2);

        pacienteMedicamento2.setId(pacienteMedicamento1.getId());
        assertThat(pacienteMedicamento1).isEqualTo(pacienteMedicamento2);

        pacienteMedicamento2 = getPacienteMedicamentoSample2();
        assertThat(pacienteMedicamento1).isNotEqualTo(pacienteMedicamento2);
    }

    @Test
    void pacienteTest() {
        PacienteMedicamento pacienteMedicamento = getPacienteMedicamentoRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        pacienteMedicamento.setPaciente(pacienteBack);
        assertThat(pacienteMedicamento.getPaciente()).isEqualTo(pacienteBack);

        pacienteMedicamento.paciente(null);
        assertThat(pacienteMedicamento.getPaciente()).isNull();
    }

    @Test
    void medicamentoTest() {
        PacienteMedicamento pacienteMedicamento = getPacienteMedicamentoRandomSampleGenerator();
        Medicamento medicamentoBack = getMedicamentoRandomSampleGenerator();

        pacienteMedicamento.setMedicamento(medicamentoBack);
        assertThat(pacienteMedicamento.getMedicamento()).isEqualTo(medicamentoBack);

        pacienteMedicamento.medicamento(null);
        assertThat(pacienteMedicamento.getMedicamento()).isNull();
    }
}
