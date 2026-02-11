package mx.infotec.pacientesms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteMedicamentoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PacienteMedicamentoDTO.class);
        PacienteMedicamentoDTO pacienteMedicamentoDTO1 = new PacienteMedicamentoDTO();
        pacienteMedicamentoDTO1.setId(1L);
        PacienteMedicamentoDTO pacienteMedicamentoDTO2 = new PacienteMedicamentoDTO();
        assertThat(pacienteMedicamentoDTO1).isNotEqualTo(pacienteMedicamentoDTO2);
        pacienteMedicamentoDTO2.setId(pacienteMedicamentoDTO1.getId());
        assertThat(pacienteMedicamentoDTO1).isEqualTo(pacienteMedicamentoDTO2);
        pacienteMedicamentoDTO2.setId(2L);
        assertThat(pacienteMedicamentoDTO1).isNotEqualTo(pacienteMedicamentoDTO2);
        pacienteMedicamentoDTO1.setId(null);
        assertThat(pacienteMedicamentoDTO1).isNotEqualTo(pacienteMedicamentoDTO2);
    }
}
