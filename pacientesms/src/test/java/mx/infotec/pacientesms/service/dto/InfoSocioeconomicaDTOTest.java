package mx.infotec.pacientesms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfoSocioeconomicaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfoSocioeconomicaDTO.class);
        InfoSocioeconomicaDTO infoSocioeconomicaDTO1 = new InfoSocioeconomicaDTO();
        infoSocioeconomicaDTO1.setId(1L);
        InfoSocioeconomicaDTO infoSocioeconomicaDTO2 = new InfoSocioeconomicaDTO();
        assertThat(infoSocioeconomicaDTO1).isNotEqualTo(infoSocioeconomicaDTO2);
        infoSocioeconomicaDTO2.setId(infoSocioeconomicaDTO1.getId());
        assertThat(infoSocioeconomicaDTO1).isEqualTo(infoSocioeconomicaDTO2);
        infoSocioeconomicaDTO2.setId(2L);
        assertThat(infoSocioeconomicaDTO1).isNotEqualTo(infoSocioeconomicaDTO2);
        infoSocioeconomicaDTO1.setId(null);
        assertThat(infoSocioeconomicaDTO1).isNotEqualTo(infoSocioeconomicaDTO2);
    }
}
