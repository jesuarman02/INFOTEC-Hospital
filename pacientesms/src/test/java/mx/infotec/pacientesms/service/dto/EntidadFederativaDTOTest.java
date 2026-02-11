package mx.infotec.pacientesms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntidadFederativaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntidadFederativaDTO.class);
        EntidadFederativaDTO entidadFederativaDTO1 = new EntidadFederativaDTO();
        entidadFederativaDTO1.setId(1L);
        EntidadFederativaDTO entidadFederativaDTO2 = new EntidadFederativaDTO();
        assertThat(entidadFederativaDTO1).isNotEqualTo(entidadFederativaDTO2);
        entidadFederativaDTO2.setId(entidadFederativaDTO1.getId());
        assertThat(entidadFederativaDTO1).isEqualTo(entidadFederativaDTO2);
        entidadFederativaDTO2.setId(2L);
        assertThat(entidadFederativaDTO1).isNotEqualTo(entidadFederativaDTO2);
        entidadFederativaDTO1.setId(null);
        assertThat(entidadFederativaDTO1).isNotEqualTo(entidadFederativaDTO2);
    }
}
