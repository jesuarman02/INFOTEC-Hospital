package mx.infotec.pacientesms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteEnfermedadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PacienteEnfermedadDTO.class);
        PacienteEnfermedadDTO pacienteEnfermedadDTO1 = new PacienteEnfermedadDTO();
        pacienteEnfermedadDTO1.setId(1L);
        PacienteEnfermedadDTO pacienteEnfermedadDTO2 = new PacienteEnfermedadDTO();
        assertThat(pacienteEnfermedadDTO1).isNotEqualTo(pacienteEnfermedadDTO2);
        pacienteEnfermedadDTO2.setId(pacienteEnfermedadDTO1.getId());
        assertThat(pacienteEnfermedadDTO1).isEqualTo(pacienteEnfermedadDTO2);
        pacienteEnfermedadDTO2.setId(2L);
        assertThat(pacienteEnfermedadDTO1).isNotEqualTo(pacienteEnfermedadDTO2);
        pacienteEnfermedadDTO1.setId(null);
        assertThat(pacienteEnfermedadDTO1).isNotEqualTo(pacienteEnfermedadDTO2);
    }
}
