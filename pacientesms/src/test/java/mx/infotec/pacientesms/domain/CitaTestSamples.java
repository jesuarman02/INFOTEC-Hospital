package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CitaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cita getCitaSample1() {
        return new Cita()
            .id(1L)
            .ecu(1)
            .nombre("nombre1")
            .apellidoPaterno("apellidoPaterno1")
            .apellidoMaterno("apellidoMaterno1")
            .motivo("motivo1");
    }

    public static Cita getCitaSample2() {
        return new Cita()
            .id(2L)
            .ecu(2)
            .nombre("nombre2")
            .apellidoPaterno("apellidoPaterno2")
            .apellidoMaterno("apellidoMaterno2")
            .motivo("motivo2");
    }

    public static Cita getCitaRandomSampleGenerator() {
        return new Cita()
            .id(longCount.incrementAndGet())
            .ecu(intCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellidoPaterno(UUID.randomUUID().toString())
            .apellidoMaterno(UUID.randomUUID().toString())
            .motivo(UUID.randomUUID().toString());
    }
}
