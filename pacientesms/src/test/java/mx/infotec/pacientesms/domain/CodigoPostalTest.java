package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.CodigoPostalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodigoPostalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodigoPostal.class);
        CodigoPostal codigoPostal1 = getCodigoPostalSample1();
        CodigoPostal codigoPostal2 = new CodigoPostal();
        assertThat(codigoPostal1).isNotEqualTo(codigoPostal2);

        codigoPostal2.setId(codigoPostal1.getId());
        assertThat(codigoPostal1).isEqualTo(codigoPostal2);

        codigoPostal2 = getCodigoPostalSample2();
        assertThat(codigoPostal1).isNotEqualTo(codigoPostal2);
    }
}
