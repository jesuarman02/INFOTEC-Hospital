package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DireccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Direccion getDireccionSample1() {
        return new Direccion()
            .id(1L)
            .nombreVialidad("nombreVialidad1")
            .numExterior("numExterior1")
            .numInterior("numInterior1")
            .telefono("telefono1");
    }

    public static Direccion getDireccionSample2() {
        return new Direccion()
            .id(2L)
            .nombreVialidad("nombreVialidad2")
            .numExterior("numExterior2")
            .numInterior("numInterior2")
            .telefono("telefono2");
    }

    public static Direccion getDireccionRandomSampleGenerator() {
        return new Direccion()
            .id(longCount.incrementAndGet())
            .nombreVialidad(UUID.randomUUID().toString())
            .numExterior(UUID.randomUUID().toString())
            .numInterior(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
