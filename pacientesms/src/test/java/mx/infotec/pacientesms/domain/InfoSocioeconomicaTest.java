package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.InfoSocioeconomicaTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfoSocioeconomicaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfoSocioeconomica.class);
        InfoSocioeconomica infoSocioeconomica1 = getInfoSocioeconomicaSample1();
        InfoSocioeconomica infoSocioeconomica2 = new InfoSocioeconomica();
        assertThat(infoSocioeconomica1).isNotEqualTo(infoSocioeconomica2);

        infoSocioeconomica2.setId(infoSocioeconomica1.getId());
        assertThat(infoSocioeconomica1).isEqualTo(infoSocioeconomica2);

        infoSocioeconomica2 = getInfoSocioeconomicaSample2();
        assertThat(infoSocioeconomica1).isNotEqualTo(infoSocioeconomica2);
    }

    @Test
    void pacienteTest() {
        InfoSocioeconomica infoSocioeconomica = getInfoSocioeconomicaRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        infoSocioeconomica.setPaciente(pacienteBack);
        assertThat(infoSocioeconomica.getPaciente()).isEqualTo(pacienteBack);
        assertThat(pacienteBack.getInfoSocioeconomica()).isEqualTo(infoSocioeconomica);

        infoSocioeconomica.paciente(null);
        assertThat(infoSocioeconomica.getPaciente()).isNull();
        assertThat(pacienteBack.getInfoSocioeconomica()).isNull();
    }
}
