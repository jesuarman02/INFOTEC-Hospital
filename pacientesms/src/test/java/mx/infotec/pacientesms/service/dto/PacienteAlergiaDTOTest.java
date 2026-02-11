package mx.infotec.pacientesms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteAlergiaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PacienteAlergiaDTO.class);
        PacienteAlergiaDTO pacienteAlergiaDTO1 = new PacienteAlergiaDTO();
        pacienteAlergiaDTO1.setId(1L);
        PacienteAlergiaDTO pacienteAlergiaDTO2 = new PacienteAlergiaDTO();
        assertThat(pacienteAlergiaDTO1).isNotEqualTo(pacienteAlergiaDTO2);
        pacienteAlergiaDTO2.setId(pacienteAlergiaDTO1.getId());
        assertThat(pacienteAlergiaDTO1).isEqualTo(pacienteAlergiaDTO2);
        pacienteAlergiaDTO2.setId(2L);
        assertThat(pacienteAlergiaDTO1).isNotEqualTo(pacienteAlergiaDTO2);
        pacienteAlergiaDTO1.setId(null);
        assertThat(pacienteAlergiaDTO1).isNotEqualTo(pacienteAlergiaDTO2);
    }
}
