package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistorialMedicoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));




    public static HistorialMedico getHistorialMedicoRandomSampleGenerator() {
        return new HistorialMedico()
            .id(longCount.incrementAndGet());
    }
}
