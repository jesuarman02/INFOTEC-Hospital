package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlergiaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Alergia getAlergiaSample1() {
        return new Alergia().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static Alergia getAlergiaSample2() {
        return new Alergia().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static Alergia getAlergiaRandomSampleGenerator() {
        return new Alergia().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString()).descripcion(UUID.randomUUID().toString());
    }
}
