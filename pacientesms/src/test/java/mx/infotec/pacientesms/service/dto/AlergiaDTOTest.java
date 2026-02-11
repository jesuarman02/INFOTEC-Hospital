package mx.infotec.pacientesms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlergiaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlergiaDTO.class);
        AlergiaDTO alergiaDTO1 = new AlergiaDTO();
        alergiaDTO1.setId(1L);
        AlergiaDTO alergiaDTO2 = new AlergiaDTO();
        assertThat(alergiaDTO1).isNotEqualTo(alergiaDTO2);
        alergiaDTO2.setId(alergiaDTO1.getId());
        assertThat(alergiaDTO1).isEqualTo(alergiaDTO2);
        alergiaDTO2.setId(2L);
        assertThat(alergiaDTO1).isNotEqualTo(alergiaDTO2);
        alergiaDTO1.setId(null);
        assertThat(alergiaDTO1).isNotEqualTo(alergiaDTO2);
    }
}
