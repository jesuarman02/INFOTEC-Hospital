package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistorialMedicoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistorialMedico getHistorialMedicoSample1() {
        return new HistorialMedico()
            .id(1L)
            .antecedentesQuirurgicos("antecedentesQuirurgicos1")
            .esquemaVacunacion("esquemaVacunacion1")
            .habitos("habitos1");
    }

    public static HistorialMedico getHistorialMedicoSample2() {
        return new HistorialMedico()
            .id(2L)
            .antecedentesQuirurgicos("antecedentesQuirurgicos2")
            .esquemaVacunacion("esquemaVacunacion2")
            .habitos("habitos2");
    }

    public static HistorialMedico getHistorialMedicoRandomSampleGenerator() {
        return new HistorialMedico()
            .id(longCount.incrementAndGet())
            .antecedentesQuirurgicos(UUID.randomUUID().toString())
            .esquemaVacunacion(UUID.randomUUID().toString())
            .habitos(UUID.randomUUID().toString());
    }
}
