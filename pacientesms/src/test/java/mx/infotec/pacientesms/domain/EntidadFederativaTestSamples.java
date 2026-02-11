package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EntidadFederativaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EntidadFederativa getEntidadFederativaSample1() {
        return new EntidadFederativa().id(1L).clave("clave1").nombre("nombre1").abreviatura("abreviatura1");
    }

    public static EntidadFederativa getEntidadFederativaSample2() {
        return new EntidadFederativa().id(2L).clave("clave2").nombre("nombre2").abreviatura("abreviatura2");
    }

    public static EntidadFederativa getEntidadFederativaRandomSampleGenerator() {
        return new EntidadFederativa()
            .id(longCount.incrementAndGet())
            .clave(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .abreviatura(UUID.randomUUID().toString());
    }
}
