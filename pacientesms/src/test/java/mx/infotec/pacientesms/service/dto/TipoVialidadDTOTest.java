package mx.infotec.pacientesms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoVialidadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoVialidadDTO.class);
        TipoVialidadDTO tipoVialidadDTO1 = new TipoVialidadDTO();
        tipoVialidadDTO1.setId(1L);
        TipoVialidadDTO tipoVialidadDTO2 = new TipoVialidadDTO();
        assertThat(tipoVialidadDTO1).isNotEqualTo(tipoVialidadDTO2);
        tipoVialidadDTO2.setId(tipoVialidadDTO1.getId());
        assertThat(tipoVialidadDTO1).isEqualTo(tipoVialidadDTO2);
        tipoVialidadDTO2.setId(2L);
        assertThat(tipoVialidadDTO1).isNotEqualTo(tipoVialidadDTO2);
        tipoVialidadDTO1.setId(null);
        assertThat(tipoVialidadDTO1).isNotEqualTo(tipoVialidadDTO2);
    }
}
