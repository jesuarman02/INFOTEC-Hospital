package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnfermedadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Enfermedad getEnfermedadSample1() {
        return new Enfermedad().id(1L).nombre("nombre1").tipo("tipo1").codigoCIE("codigoCIE1");
    }

    public static Enfermedad getEnfermedadSample2() {
        return new Enfermedad().id(2L).nombre("nombre2").tipo("tipo2").codigoCIE("codigoCIE2");
    }

    public static Enfermedad getEnfermedadRandomSampleGenerator() {
        return new Enfermedad()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .tipo(UUID.randomUUID().toString())
            .codigoCIE(UUID.randomUUID().toString());
    }
}
