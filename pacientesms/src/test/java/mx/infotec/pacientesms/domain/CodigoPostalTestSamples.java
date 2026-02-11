package mx.infotec.pacientesms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CodigoPostalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CodigoPostal getCodigoPostalSample1() {
        return new CodigoPostal()
            .id(1L)
            .codigo("codigo1")
            .asentamiento("asentamiento1")
            .tipoAsentamiento("tipoAsentamiento1")
            .municipio("municipio1")
            .estado("estado1")
            .ciudad("ciudad1")
            .codigoPostalAdministracion("codigoPostalAdministracion1")
            .claveEstado("claveEstado1")
            .claveOficina("claveOficina1")
            .claveCP("claveCP1")
            .claveTipoAsentamiento("claveTipoAsentamiento1")
            .claveMunicipio("claveMunicipio1")
            .idAsentamientoCons("idAsentamientoCons1")
            .zona("zona1")
            .claveCiudad("claveCiudad1");
    }

    public static CodigoPostal getCodigoPostalSample2() {
        return new CodigoPostal()
            .id(2L)
            .codigo("codigo2")
            .asentamiento("asentamiento2")
            .tipoAsentamiento("tipoAsentamiento2")
            .municipio("municipio2")
            .estado("estado2")
            .ciudad("ciudad2")
            .codigoPostalAdministracion("codigoPostalAdministracion2")
            .claveEstado("claveEstado2")
            .claveOficina("claveOficina2")
            .claveCP("claveCP2")
            .claveTipoAsentamiento("claveTipoAsentamiento2")
            .claveMunicipio("claveMunicipio2")
            .idAsentamientoCons("idAsentamientoCons2")
            .zona("zona2")
            .claveCiudad("claveCiudad2");
    }

    public static CodigoPostal getCodigoPostalRandomSampleGenerator() {
        return new CodigoPostal()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .asentamiento(UUID.randomUUID().toString())
            .tipoAsentamiento(UUID.randomUUID().toString())
            .municipio(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .ciudad(UUID.randomUUID().toString())
            .codigoPostalAdministracion(UUID.randomUUID().toString())
            .claveEstado(UUID.randomUUID().toString())
            .claveOficina(UUID.randomUUID().toString())
            .claveCP(UUID.randomUUID().toString())
            .claveTipoAsentamiento(UUID.randomUUID().toString())
            .claveMunicipio(UUID.randomUUID().toString())
            .idAsentamientoCons(UUID.randomUUID().toString())
            .zona(UUID.randomUUID().toString())
            .claveCiudad(UUID.randomUUID().toString());
    }
}
