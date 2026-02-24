package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Paciente getPacienteSample1() {
        return new Paciente().id(1L).ecu(5).nombre("nombre1").apellidoPaterno("apellidoPaterno1").apellidoMaterno("apellidoMaterno1").curp("curp1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente().id(2L).ecu(2).nombre("nombre2").apellidoPaterno("apellidoPaterno2").apellidoMaterno("apellidoMaterno2").curp("curp2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente()
            .id(longCount.incrementAndGet())
            .ecu(intCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellidoPaterno(UUID.randomUUID().toString())
            .apellidoMaterno(UUID.randomUUID().toString())
            .curp(UUID.randomUUID().toString());
    }
}