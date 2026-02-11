package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InfoSocioeconomicaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static InfoSocioeconomica getInfoSocioeconomicaSample1() {
        return new InfoSocioeconomica()
            .id(1L)
            .tipoVivienda("tipoVivienda1")
            .materialVivienda("materialVivienda1")
            .numeroHabitaciones(1)
            .numeroHabitantes(1)
            .serviciosDisponibles("serviciosDisponibles1")
            .personasDependientes(1)
            .apoyosGubernamentales("apoyosGubernamentales1")
            .ocupacionActual("ocupacionActual1")
            .condicionLaboral("condicionLaboral1")
            .tipoEmpleo("tipoEmpleo1")
            .lugarTrabajo("lugarTrabajo1")
            .tiempoEmpleado("tiempoEmpleado1")
            .gradoMaximoEstudios("gradoMaximoEstudios1")
            .aniosEstudio(1)
            .institucionMedica("institucionMedica1")
            .tipoAfiliacion("tipoAfiliacion1")
            .numeroAfiliacion("numeroAfiliacion1")
            .medioTransporte("medioTransporte1")
            .tiempoTraslado(1);
    }

    public static InfoSocioeconomica getInfoSocioeconomicaSample2() {
        return new InfoSocioeconomica()
            .id(2L)
            .tipoVivienda("tipoVivienda2")
            .materialVivienda("materialVivienda2")
            .numeroHabitaciones(2)
            .numeroHabitantes(2)
            .serviciosDisponibles("serviciosDisponibles2")
            .personasDependientes(2)
            .apoyosGubernamentales("apoyosGubernamentales2")
            .ocupacionActual("ocupacionActual2")
            .condicionLaboral("condicionLaboral2")
            .tipoEmpleo("tipoEmpleo2")
            .lugarTrabajo("lugarTrabajo2")
            .tiempoEmpleado("tiempoEmpleado2")
            .gradoMaximoEstudios("gradoMaximoEstudios2")
            .aniosEstudio(2)
            .institucionMedica("institucionMedica2")
            .tipoAfiliacion("tipoAfiliacion2")
            .numeroAfiliacion("numeroAfiliacion2")
            .medioTransporte("medioTransporte2")
            .tiempoTraslado(2);
    }

    public static InfoSocioeconomica getInfoSocioeconomicaRandomSampleGenerator() {
        return new InfoSocioeconomica()
            .id(longCount.incrementAndGet())
            .tipoVivienda(UUID.randomUUID().toString())
            .materialVivienda(UUID.randomUUID().toString())
            .numeroHabitaciones(intCount.incrementAndGet())
            .numeroHabitantes(intCount.incrementAndGet())
            .serviciosDisponibles(UUID.randomUUID().toString())
            .personasDependientes(intCount.incrementAndGet())
            .apoyosGubernamentales(UUID.randomUUID().toString())
            .ocupacionActual(UUID.randomUUID().toString())
            .condicionLaboral(UUID.randomUUID().toString())
            .tipoEmpleo(UUID.randomUUID().toString())
            .lugarTrabajo(UUID.randomUUID().toString())
            .tiempoEmpleado(UUID.randomUUID().toString())
            .gradoMaximoEstudios(UUID.randomUUID().toString())
            .aniosEstudio(intCount.incrementAndGet())
            .institucionMedica(UUID.randomUUID().toString())
            .tipoAfiliacion(UUID.randomUUID().toString())
            .numeroAfiliacion(UUID.randomUUID().toString())
            .medioTransporte(UUID.randomUUID().toString())
            .tiempoTraslado(intCount.incrementAndGet());
    }
}
