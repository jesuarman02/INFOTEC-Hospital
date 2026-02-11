package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteAlergiaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PacienteAlergia getPacienteAlergiaSample1() {
        return new PacienteAlergia().id(1L).reaccion("reaccion1");
    }

    public static PacienteAlergia getPacienteAlergiaSample2() {
        return new PacienteAlergia().id(2L).reaccion("reaccion2");
    }

    public static PacienteAlergia getPacienteAlergiaRandomSampleGenerator() {
        return new PacienteAlergia().id(longCount.incrementAndGet()).reaccion(UUID.randomUUID().toString());
    }
}
