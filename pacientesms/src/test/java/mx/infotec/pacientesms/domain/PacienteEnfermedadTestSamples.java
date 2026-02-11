package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteEnfermedadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PacienteEnfermedad getPacienteEnfermedadSample1() {
        return new PacienteEnfermedad().id(1L).estado("estado1").notas("notas1");
    }

    public static PacienteEnfermedad getPacienteEnfermedadSample2() {
        return new PacienteEnfermedad().id(2L).estado("estado2").notas("notas2");
    }

    public static PacienteEnfermedad getPacienteEnfermedadRandomSampleGenerator() {
        return new PacienteEnfermedad()
            .id(longCount.incrementAndGet())
            .estado(UUID.randomUUID().toString())
            .notas(UUID.randomUUID().toString());
    }
}
