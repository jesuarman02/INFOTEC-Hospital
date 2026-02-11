package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicamentoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Medicamento getMedicamentoSample1() {
        return new Medicamento().id(1L).nombre("nombre1").ingredienteActivo("ingredienteActivo1").presentacion("presentacion1");
    }

    public static Medicamento getMedicamentoSample2() {
        return new Medicamento().id(2L).nombre("nombre2").ingredienteActivo("ingredienteActivo2").presentacion("presentacion2");
    }

    public static Medicamento getMedicamentoRandomSampleGenerator() {
        return new Medicamento()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .ingredienteActivo(UUID.randomUUID().toString())
            .presentacion(UUID.randomUUID().toString());
    }
}
