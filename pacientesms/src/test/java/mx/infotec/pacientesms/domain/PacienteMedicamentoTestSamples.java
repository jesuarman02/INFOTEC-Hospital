package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteMedicamentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PacienteMedicamento getPacienteMedicamentoSample1() {
        return new PacienteMedicamento().id(1L).dosis("dosis1").frecuencia("frecuencia1");
    }

    public static PacienteMedicamento getPacienteMedicamentoSample2() {
        return new PacienteMedicamento().id(2L).dosis("dosis2").frecuencia("frecuencia2");
    }

    public static PacienteMedicamento getPacienteMedicamentoRandomSampleGenerator() {
        return new PacienteMedicamento()
            .id(longCount.incrementAndGet())
            .dosis(UUID.randomUUID().toString())
            .frecuencia(UUID.randomUUID().toString());
    }
}
