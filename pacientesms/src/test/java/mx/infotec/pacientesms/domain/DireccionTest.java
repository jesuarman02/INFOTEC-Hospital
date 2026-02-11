package mx.infotec.pacientesms.domain;

import static mx.infotec.pacientesms.domain.CodigoPostalTestSamples.*;
import static mx.infotec.pacientesms.domain.DireccionTestSamples.*;
import static mx.infotec.pacientesms.domain.EntidadFederativaTestSamples.*;
import static mx.infotec.pacientesms.domain.PacienteTestSamples.*;
import static mx.infotec.pacientesms.domain.TipoVialidadTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import mx.infotec.pacientesms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DireccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Direccion.class);
        Direccion direccion1 = getDireccionSample1();
        Direccion direccion2 = new Direccion();
        assertThat(direccion1).isNotEqualTo(direccion2);

        direccion2.setId(direccion1.getId());
        assertThat(direccion1).isEqualTo(direccion2);

        direccion2 = getDireccionSample2();
        assertThat(direccion1).isNotEqualTo(direccion2);
    }

    @Test
    void tipoVialidadTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        TipoVialidad tipoVialidadBack = getTipoVialidadRandomSampleGenerator();

        direccion.setTipoVialidad(tipoVialidadBack);
        assertThat(direccion.getTipoVialidad()).isEqualTo(tipoVialidadBack);

        direccion.tipoVialidad(null);
        assertThat(direccion.getTipoVialidad()).isNull();
    }

    @Test
    void codigoPostalInfoTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        CodigoPostal codigoPostalBack = getCodigoPostalRandomSampleGenerator();

        direccion.setCodigoPostalInfo(codigoPostalBack);
        assertThat(direccion.getCodigoPostalInfo()).isEqualTo(codigoPostalBack);

        direccion.codigoPostalInfo(null);
        assertThat(direccion.getCodigoPostalInfo()).isNull();
    }

    @Test
    void entidadFederativaTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        EntidadFederativa entidadFederativaBack = getEntidadFederativaRandomSampleGenerator();

        direccion.setEntidadFederativa(entidadFederativaBack);
        assertThat(direccion.getEntidadFederativa()).isEqualTo(entidadFederativaBack);

        direccion.entidadFederativa(null);
        assertThat(direccion.getEntidadFederativa()).isNull();
    }

    @Test
    void pacienteTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        direccion.setPaciente(pacienteBack);
        assertThat(direccion.getPaciente()).isEqualTo(pacienteBack);
        assertThat(pacienteBack.getDireccion()).isEqualTo(direccion);

        direccion.paciente(null);
        assertThat(direccion.getPaciente()).isNull();
        assertThat(pacienteBack.getDireccion()).isNull();
    }
}
