package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SignosVitalesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SignosVitales getSignosVitalesSample1() {
        return new SignosVitales()
            .id(1L)
            .frecuenciaCardiaca(1)
            .tensionArterial("tensionArterial1")
            .frecuenciaRespiratoria(1)
            .saturacionOxigeno(1);
    }

    public static SignosVitales getSignosVitalesSample2() {
        return new SignosVitales()
            .id(2L)
            .frecuenciaCardiaca(2)
            .tensionArterial("tensionArterial2")
            .frecuenciaRespiratoria(2)
            .saturacionOxigeno(2);
    }

    public static SignosVitales getSignosVitalesRandomSampleGenerator() {
        return new SignosVitales()
            .id(longCount.incrementAndGet())
            .frecuenciaCardiaca(intCount.incrementAndGet())
            .tensionArterial(UUID.randomUUID().toString())
            .frecuenciaRespiratoria(intCount.incrementAndGet())
            .saturacionOxigeno(intCount.incrementAndGet());
    }
}
