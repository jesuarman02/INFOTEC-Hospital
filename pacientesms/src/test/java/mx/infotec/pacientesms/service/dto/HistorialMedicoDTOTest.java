package mx.infotec.pacientesms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistorialMedicoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistorialMedicoDTO.class);
        HistorialMedicoDTO historialMedicoDTO1 = new HistorialMedicoDTO();
        historialMedicoDTO1.setId(1L);
        HistorialMedicoDTO historialMedicoDTO2 = new HistorialMedicoDTO();
        assertThat(historialMedicoDTO1).isNotEqualTo(historialMedicoDTO2);
        historialMedicoDTO2.setId(historialMedicoDTO1.getId());
        assertThat(historialMedicoDTO1).isEqualTo(historialMedicoDTO2);
        historialMedicoDTO2.setId(2L);
        assertThat(historialMedicoDTO1).isNotEqualTo(historialMedicoDTO2);
        historialMedicoDTO1.setId(null);
        assertThat(historialMedicoDTO1).isNotEqualTo(historialMedicoDTO2);
    }
}
