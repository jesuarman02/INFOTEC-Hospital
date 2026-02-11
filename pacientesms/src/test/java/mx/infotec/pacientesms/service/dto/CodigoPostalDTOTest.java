package mx.infotec.pacientesms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodigoPostalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodigoPostalDTO.class);
        CodigoPostalDTO codigoPostalDTO1 = new CodigoPostalDTO();
        codigoPostalDTO1.setId(1L);
        CodigoPostalDTO codigoPostalDTO2 = new CodigoPostalDTO();
        assertThat(codigoPostalDTO1).isNotEqualTo(codigoPostalDTO2);
        codigoPostalDTO2.setId(codigoPostalDTO1.getId());
        assertThat(codigoPostalDTO1).isEqualTo(codigoPostalDTO2);
        codigoPostalDTO2.setId(2L);
        assertThat(codigoPostalDTO1).isNotEqualTo(codigoPostalDTO2);
        codigoPostalDTO1.setId(null);
        assertThat(codigoPostalDTO1).isNotEqualTo(codigoPostalDTO2);
    }
}
