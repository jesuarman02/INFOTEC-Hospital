package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.EntidadFederativaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntidadFederativaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntidadFederativa.class);
        EntidadFederativa entidadFederativa1 = getEntidadFederativaSample1();
        EntidadFederativa entidadFederativa2 = new EntidadFederativa();
        assertThat(entidadFederativa1).isNotEqualTo(entidadFederativa2);

        entidadFederativa2.setId(entidadFederativa1.getId());
        assertThat(entidadFederativa1).isEqualTo(entidadFederativa2);

        entidadFederativa2 = getEntidadFederativaSample2();
        assertThat(entidadFederativa1).isNotEqualTo(entidadFederativa2);
    }
}
