package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.MedicamentoTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteMedicamentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicamento.class);
        Medicamento medicamento1 = getMedicamentoSample1();
        Medicamento medicamento2 = new Medicamento();
        assertThat(medicamento1).isNotEqualTo(medicamento2);

        medicamento2.setId(medicamento1.getId());
        assertThat(medicamento1).isEqualTo(medicamento2);

        medicamento2 = getMedicamentoSample2();
        assertThat(medicamento1).isNotEqualTo(medicamento2);
    }

    @Test
    void pacientesTest() {
        Medicamento medicamento = getMedicamentoRandomSampleGenerator();
        PacienteMedicamento pacienteMedicamentoBack = getPacienteMedicamentoRandomSampleGenerator();

        medicamento.addPacientes(pacienteMedicamentoBack);
        assertThat(medicamento.getPacientes()).containsOnly(pacienteMedicamentoBack);
        assertThat(pacienteMedicamentoBack.getMedicamento()).isEqualTo(medicamento);

        medicamento.removePacientes(pacienteMedicamentoBack);
        assertThat(medicamento.getPacientes()).doesNotContain(pacienteMedicamentoBack);
        assertThat(pacienteMedicamentoBack.getMedicamento()).isNull();

        medicamento.pacientes(new HashSet<>(Set.of(pacienteMedicamentoBack)));
        assertThat(medicamento.getPacientes()).containsOnly(pacienteMedicamentoBack);
        assertThat(pacienteMedicamentoBack.getMedicamento()).isEqualTo(medicamento);

        medicamento.setPacientes(new HashSet<>());
        assertThat(medicamento.getPacientes()).doesNotContain(pacienteMedicamentoBack);
        assertThat(pacienteMedicamentoBack.getMedicamento()).isNull();
    }
}
