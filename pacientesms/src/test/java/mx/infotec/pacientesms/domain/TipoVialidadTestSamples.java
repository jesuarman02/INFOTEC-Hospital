package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TipoVialidadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TipoVialidad getTipoVialidadSample1() {
        return new TipoVialidad().id(1L).nombre("nombre1");
    }

    public static TipoVialidad getTipoVialidadSample2() {
        return new TipoVialidad().id(2L).nombre("nombre2");
    }

    public static TipoVialidad getTipoVialidadRandomSampleGenerator() {
        return new TipoVialidad().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString());
    }
}
