package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.TipoVialidadTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoVialidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoVialidad.class);
        TipoVialidad tipoVialidad1 = getTipoVialidadSample1();
        TipoVialidad tipoVialidad2 = new TipoVialidad();
        assertThat(tipoVialidad1).isNotEqualTo(tipoVialidad2);

        tipoVialidad2.setId(tipoVialidad1.getId());
        assertThat(tipoVialidad1).isEqualTo(tipoVialidad2);

        tipoVialidad2 = getTipoVialidadSample2();
        assertThat(tipoVialidad1).isNotEqualTo(tipoVialidad2);
    }
}
