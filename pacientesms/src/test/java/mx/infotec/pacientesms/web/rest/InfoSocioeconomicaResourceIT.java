package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.InfoSocioeconomicaAsserts.*;
import static mx.infotec.pacientesms.web.rest.TestUtil.createUpdateProxyForBean;
import static mx.infotec.pacientesms.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mx.infotec.pacientesms.IntegrationTest;
import mx.infotec.pacientesms.domain.InfoSocioeconomica;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.repository.InfoSocioeconomicaRepository;
import mx.infotec.pacientesms.service.dto.InfoSocioeconomicaDTO;
import mx.infotec.pacientesms.service.mapper.InfoSocioeconomicaMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link InfoSocioeconomicaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InfoSocioeconomicaResourceIT {

    private static final String DEFAULT_TIPO_VIVIENDA = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_VIVIENDA = "BBBBBBBBBB";

    private static final String DEFAULT_MATERIAL_VIVIENDA = "AAAAAAAAAA";
    private static final String UPDATED_MATERIAL_VIVIENDA = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_HABITACIONES = 1;
    private static final Integer UPDATED_NUMERO_HABITACIONES = 2;

    private static final Integer DEFAULT_NUMERO_HABITANTES = 1;
    private static final Integer UPDATED_NUMERO_HABITANTES = 2;

    private static final String DEFAULT_SERVICIOS_DISPONIBLES = "AAAAAAAAAA";
    private static final String UPDATED_SERVICIOS_DISPONIBLES = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_INGRESO_MENSUAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_INGRESO_MENSUAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INGRESO_MENSUAL_HOGAR = new BigDecimal(1);
    private static final BigDecimal UPDATED_INGRESO_MENSUAL_HOGAR = new BigDecimal(2);

    private static final BigDecimal DEFAULT_GASTO_MENSUAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_GASTO_MENSUAL = new BigDecimal(2);

    private static final Integer DEFAULT_PERSONAS_DEPENDIENTES = 1;
    private static final Integer UPDATED_PERSONAS_DEPENDIENTES = 2;

    private static final String DEFAULT_APOYOS_GUBERNAMENTALES = "AAAAAAAAAA";
    private static final String UPDATED_APOYOS_GUBERNAMENTALES = "BBBBBBBBBB";

    private static final String DEFAULT_OCUPACION_ACTUAL = "AAAAAAAAAA";
    private static final String UPDATED_OCUPACION_ACTUAL = "BBBBBBBBBB";

    private static final String DEFAULT_CONDICION_LABORAL = "AAAAAAAAAA";
    private static final String UPDATED_CONDICION_LABORAL = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_EMPLEO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_EMPLEO = "BBBBBBBBBB";

    private static final String DEFAULT_LUGAR_TRABAJO = "AAAAAAAAAA";
    private static final String UPDATED_LUGAR_TRABAJO = "BBBBBBBBBB";

    private static final String DEFAULT_TIEMPO_EMPLEADO = "AAAAAAAAAA";
    private static final String UPDATED_TIEMPO_EMPLEADO = "BBBBBBBBBB";

    private static final String DEFAULT_GRADO_MAXIMO_ESTUDIOS = "AAAAAAAAAA";
    private static final String UPDATED_GRADO_MAXIMO_ESTUDIOS = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANIOS_ESTUDIO = 1;
    private static final Integer UPDATED_ANIOS_ESTUDIO = 2;

    private static final Boolean DEFAULT_ESTUDIA = false;
    private static final Boolean UPDATED_ESTUDIA = true;

    private static final String DEFAULT_INSTITUCION_MEDICA = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUCION_MEDICA = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_AFILIACION = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_AFILIACION = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_AFILIACION = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_AFILIACION = "BBBBBBBBBB";

    private static final String DEFAULT_MEDIO_TRANSPORTE = "AAAAAAAAAA";
    private static final String UPDATED_MEDIO_TRANSPORTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIEMPO_TRASLADO = 1;
    private static final Integer UPDATED_TIEMPO_TRASLADO = 2;

    private static final LocalDate DEFAULT_FECHA_ACTUALIZACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ACTUALIZACION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/info-socioeconomicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InfoSocioeconomicaRepository infoSocioeconomicaRepository;

    @Autowired
    private InfoSocioeconomicaMapper infoSocioeconomicaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private InfoSocioeconomica infoSocioeconomica;

    private InfoSocioeconomica insertedInfoSocioeconomica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoSocioeconomica createEntity() {
        return new InfoSocioeconomica()
            .tipoVivienda(DEFAULT_TIPO_VIVIENDA)
            .materialVivienda(DEFAULT_MATERIAL_VIVIENDA)
            .numeroHabitaciones(DEFAULT_NUMERO_HABITACIONES)
            .numeroHabitantes(DEFAULT_NUMERO_HABITANTES)
            .serviciosDisponibles(DEFAULT_SERVICIOS_DISPONIBLES)
            .ingresoMensual(DEFAULT_INGRESO_MENSUAL)
            .ingresoMensualHogar(DEFAULT_INGRESO_MENSUAL_HOGAR)
            .gastoMensual(DEFAULT_GASTO_MENSUAL)
            .personasDependientes(DEFAULT_PERSONAS_DEPENDIENTES)
            .apoyosGubernamentales(DEFAULT_APOYOS_GUBERNAMENTALES)
            .ocupacionActual(DEFAULT_OCUPACION_ACTUAL)
            .condicionLaboral(DEFAULT_CONDICION_LABORAL)
            .tipoEmpleo(DEFAULT_TIPO_EMPLEO)
            .lugarTrabajo(DEFAULT_LUGAR_TRABAJO)
            .tiempoEmpleado(DEFAULT_TIEMPO_EMPLEADO)
            .gradoMaximoEstudios(DEFAULT_GRADO_MAXIMO_ESTUDIOS)
            .aniosEstudio(DEFAULT_ANIOS_ESTUDIO)
            .estudia(DEFAULT_ESTUDIA)
            .institucionMedica(DEFAULT_INSTITUCION_MEDICA)
            .tipoAfiliacion(DEFAULT_TIPO_AFILIACION)
            .numeroAfiliacion(DEFAULT_NUMERO_AFILIACION)
            .medioTransporte(DEFAULT_MEDIO_TRANSPORTE)
            .tiempoTraslado(DEFAULT_TIEMPO_TRASLADO)
            .fechaActualizacion(DEFAULT_FECHA_ACTUALIZACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoSocioeconomica createUpdatedEntity() {
        return new InfoSocioeconomica()
            .tipoVivienda(UPDATED_TIPO_VIVIENDA)
            .materialVivienda(UPDATED_MATERIAL_VIVIENDA)
            .numeroHabitaciones(UPDATED_NUMERO_HABITACIONES)
            .numeroHabitantes(UPDATED_NUMERO_HABITANTES)
            .serviciosDisponibles(UPDATED_SERVICIOS_DISPONIBLES)
            .ingresoMensual(UPDATED_INGRESO_MENSUAL)
            .ingresoMensualHogar(UPDATED_INGRESO_MENSUAL_HOGAR)
            .gastoMensual(UPDATED_GASTO_MENSUAL)
            .personasDependientes(UPDATED_PERSONAS_DEPENDIENTES)
            .apoyosGubernamentales(UPDATED_APOYOS_GUBERNAMENTALES)
            .ocupacionActual(UPDATED_OCUPACION_ACTUAL)
            .condicionLaboral(UPDATED_CONDICION_LABORAL)
            .tipoEmpleo(UPDATED_TIPO_EMPLEO)
            .lugarTrabajo(UPDATED_LUGAR_TRABAJO)
            .tiempoEmpleado(UPDATED_TIEMPO_EMPLEADO)
            .gradoMaximoEstudios(UPDATED_GRADO_MAXIMO_ESTUDIOS)
            .aniosEstudio(UPDATED_ANIOS_ESTUDIO)
            .estudia(UPDATED_ESTUDIA)
            .institucionMedica(UPDATED_INSTITUCION_MEDICA)
            .tipoAfiliacion(UPDATED_TIPO_AFILIACION)
            .numeroAfiliacion(UPDATED_NUMERO_AFILIACION)
            .medioTransporte(UPDATED_MEDIO_TRANSPORTE)
            .tiempoTraslado(UPDATED_TIEMPO_TRASLADO)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(InfoSocioeconomica.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        infoSocioeconomica = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInfoSocioeconomica != null) {
            infoSocioeconomicaRepository.delete(insertedInfoSocioeconomica).block();
            insertedInfoSocioeconomica = null;
        }
        deleteEntities(em);
    }

    @Test
    void createInfoSocioeconomica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InfoSocioeconomica
        InfoSocioeconomicaDTO infoSocioeconomicaDTO = infoSocioeconomicaMapper.toDto(infoSocioeconomica);
        var returnedInfoSocioeconomicaDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(infoSocioeconomicaDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(InfoSocioeconomicaDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the InfoSocioeconomica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInfoSocioeconomica = infoSocioeconomicaMapper.toEntity(returnedInfoSocioeconomicaDTO);
        assertInfoSocioeconomicaUpdatableFieldsEquals(
            returnedInfoSocioeconomica,
            getPersistedInfoSocioeconomica(returnedInfoSocioeconomica)
        );

        insertedInfoSocioeconomica = returnedInfoSocioeconomica;
    }

    @Test
    void createInfoSocioeconomicaWithExistingId() throws Exception {
        // Create the InfoSocioeconomica with an existing ID
        infoSocioeconomica.setId(1L);
        InfoSocioeconomicaDTO infoSocioeconomicaDTO = infoSocioeconomicaMapper.toDto(infoSocioeconomica);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(infoSocioeconomicaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoSocioeconomica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInfoSocioeconomicasAsStream() {
        // Initialize the database
        infoSocioeconomicaRepository.save(infoSocioeconomica).block();

        List<InfoSocioeconomica> infoSocioeconomicaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(InfoSocioeconomicaDTO.class)
            .getResponseBody()
            .map(infoSocioeconomicaMapper::toEntity)
            .filter(infoSocioeconomica::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(infoSocioeconomicaList).isNotNull();
        assertThat(infoSocioeconomicaList).hasSize(1);
        InfoSocioeconomica testInfoSocioeconomica = infoSocioeconomicaList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertInfoSocioeconomicaAllPropertiesEquals(infoSocioeconomica, testInfoSocioeconomica);
        assertInfoSocioeconomicaUpdatableFieldsEquals(infoSocioeconomica, testInfoSocioeconomica);
    }

    @Test
    void getAllInfoSocioeconomicas() {
        // Initialize the database
        insertedInfoSocioeconomica = infoSocioeconomicaRepository.save(infoSocioeconomica).block();

        // Get all the infoSocioeconomicaList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(infoSocioeconomica.getId().intValue()))
            .jsonPath("$.[*].tipoVivienda")
            .value(hasItem(DEFAULT_TIPO_VIVIENDA))
            .jsonPath("$.[*].materialVivienda")
            .value(hasItem(DEFAULT_MATERIAL_VIVIENDA))
            .jsonPath("$.[*].numeroHabitaciones")
            .value(hasItem(DEFAULT_NUMERO_HABITACIONES))
            .jsonPath("$.[*].numeroHabitantes")
            .value(hasItem(DEFAULT_NUMERO_HABITANTES))
            .jsonPath("$.[*].serviciosDisponibles")
            .value(hasItem(DEFAULT_SERVICIOS_DISPONIBLES))
            .jsonPath("$.[*].ingresoMensual")
            .value(hasItem(sameNumber(DEFAULT_INGRESO_MENSUAL)))
            .jsonPath("$.[*].ingresoMensualHogar")
            .value(hasItem(sameNumber(DEFAULT_INGRESO_MENSUAL_HOGAR)))
            .jsonPath("$.[*].gastoMensual")
            .value(hasItem(sameNumber(DEFAULT_GASTO_MENSUAL)))
            .jsonPath("$.[*].personasDependientes")
            .value(hasItem(DEFAULT_PERSONAS_DEPENDIENTES))
            .jsonPath("$.[*].apoyosGubernamentales")
            .value(hasItem(DEFAULT_APOYOS_GUBERNAMENTALES))
            .jsonPath("$.[*].ocupacionActual")
            .value(hasItem(DEFAULT_OCUPACION_ACTUAL))
            .jsonPath("$.[*].condicionLaboral")
            .value(hasItem(DEFAULT_CONDICION_LABORAL))
            .jsonPath("$.[*].tipoEmpleo")
            .value(hasItem(DEFAULT_TIPO_EMPLEO))
            .jsonPath("$.[*].lugarTrabajo")
            .value(hasItem(DEFAULT_LUGAR_TRABAJO))
            .jsonPath("$.[*].tiempoEmpleado")
            .value(hasItem(DEFAULT_TIEMPO_EMPLEADO))
            .jsonPath("$.[*].gradoMaximoEstudios")
            .value(hasItem(DEFAULT_GRADO_MAXIMO_ESTUDIOS))
            .jsonPath("$.[*].aniosEstudio")
            .value(hasItem(DEFAULT_ANIOS_ESTUDIO))
            .jsonPath("$.[*].estudia")
            .value(hasItem(DEFAULT_ESTUDIA.booleanValue()))
            .jsonPath("$.[*].institucionMedica")
            .value(hasItem(DEFAULT_INSTITUCION_MEDICA))
            .jsonPath("$.[*].tipoAfiliacion")
            .value(hasItem(DEFAULT_TIPO_AFILIACION))
            .jsonPath("$.[*].numeroAfiliacion")
            .value(hasItem(DEFAULT_NUMERO_AFILIACION))
            .jsonPath("$.[*].medioTransporte")
            .value(hasItem(DEFAULT_MEDIO_TRANSPORTE))
            .jsonPath("$.[*].tiempoTraslado")
            .value(hasItem(DEFAULT_TIEMPO_TRASLADO))
            .jsonPath("$.[*].fechaActualizacion")
            .value(hasItem(DEFAULT_FECHA_ACTUALIZACION.toString()));
    }

    @Test
    void getInfoSocioeconomica() {
        // Initialize the database
        insertedInfoSocioeconomica = infoSocioeconomicaRepository.save(infoSocioeconomica).block();

        // Get the infoSocioeconomica
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, infoSocioeconomica.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(infoSocioeconomica.getId().intValue()))
            .jsonPath("$.tipoVivienda")
            .value(is(DEFAULT_TIPO_VIVIENDA))
            .jsonPath("$.materialVivienda")
            .value(is(DEFAULT_MATERIAL_VIVIENDA))
            .jsonPath("$.numeroHabitaciones")
            .value(is(DEFAULT_NUMERO_HABITACIONES))
            .jsonPath("$.numeroHabitantes")
            .value(is(DEFAULT_NUMERO_HABITANTES))
            .jsonPath("$.serviciosDisponibles")
            .value(is(DEFAULT_SERVICIOS_DISPONIBLES))
            .jsonPath("$.ingresoMensual")
            .value(is(sameNumber(DEFAULT_INGRESO_MENSUAL)))
            .jsonPath("$.ingresoMensualHogar")
            .value(is(sameNumber(DEFAULT_INGRESO_MENSUAL_HOGAR)))
            .jsonPath("$.gastoMensual")
            .value(is(sameNumber(DEFAULT_GASTO_MENSUAL)))
            .jsonPath("$.personasDependientes")
            .value(is(DEFAULT_PERSONAS_DEPENDIENTES))
            .jsonPath("$.apoyosGubernamentales")
            .value(is(DEFAULT_APOYOS_GUBERNAMENTALES))
            .jsonPath("$.ocupacionActual")
            .value(is(DEFAULT_OCUPACION_ACTUAL))
            .jsonPath("$.condicionLaboral")
            .value(is(DEFAULT_CONDICION_LABORAL))
            .jsonPath("$.tipoEmpleo")
            .value(is(DEFAULT_TIPO_EMPLEO))
            .jsonPath("$.lugarTrabajo")
            .value(is(DEFAULT_LUGAR_TRABAJO))
            .jsonPath("$.tiempoEmpleado")
            .value(is(DEFAULT_TIEMPO_EMPLEADO))
            .jsonPath("$.gradoMaximoEstudios")
            .value(is(DEFAULT_GRADO_MAXIMO_ESTUDIOS))
            .jsonPath("$.aniosEstudio")
            .value(is(DEFAULT_ANIOS_ESTUDIO))
            .jsonPath("$.estudia")
            .value(is(DEFAULT_ESTUDIA.booleanValue()))
            .jsonPath("$.institucionMedica")
            .value(is(DEFAULT_INSTITUCION_MEDICA))
            .jsonPath("$.tipoAfiliacion")
            .value(is(DEFAULT_TIPO_AFILIACION))
            .jsonPath("$.numeroAfiliacion")
            .value(is(DEFAULT_NUMERO_AFILIACION))
            .jsonPath("$.medioTransporte")
            .value(is(DEFAULT_MEDIO_TRANSPORTE))
            .jsonPath("$.tiempoTraslado")
            .value(is(DEFAULT_TIEMPO_TRASLADO))
            .jsonPath("$.fechaActualizacion")
            .value(is(DEFAULT_FECHA_ACTUALIZACION.toString()));
    }

    @Test
    void getNonExistingInfoSocioeconomica() {
        // Get the infoSocioeconomica
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInfoSocioeconomica() throws Exception {
        // Initialize the database
        insertedInfoSocioeconomica = infoSocioeconomicaRepository.save(infoSocioeconomica).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the infoSocioeconomica
        InfoSocioeconomica updatedInfoSocioeconomica = infoSocioeconomicaRepository.findById(infoSocioeconomica.getId()).block();
        updatedInfoSocioeconomica
            .tipoVivienda(UPDATED_TIPO_VIVIENDA)
            .materialVivienda(UPDATED_MATERIAL_VIVIENDA)
            .numeroHabitaciones(UPDATED_NUMERO_HABITACIONES)
            .numeroHabitantes(UPDATED_NUMERO_HABITANTES)
            .serviciosDisponibles(UPDATED_SERVICIOS_DISPONIBLES)
            .ingresoMensual(UPDATED_INGRESO_MENSUAL)
            .ingresoMensualHogar(UPDATED_INGRESO_MENSUAL_HOGAR)
            .gastoMensual(UPDATED_GASTO_MENSUAL)
            .personasDependientes(UPDATED_PERSONAS_DEPENDIENTES)
            .apoyosGubernamentales(UPDATED_APOYOS_GUBERNAMENTALES)
            .ocupacionActual(UPDATED_OCUPACION_ACTUAL)
            .condicionLaboral(UPDATED_CONDICION_LABORAL)
            .tipoEmpleo(UPDATED_TIPO_EMPLEO)
            .lugarTrabajo(UPDATED_LUGAR_TRABAJO)
            .tiempoEmpleado(UPDATED_TIEMPO_EMPLEADO)
            .gradoMaximoEstudios(UPDATED_GRADO_MAXIMO_ESTUDIOS)
            .aniosEstudio(UPDATED_ANIOS_ESTUDIO)
            .estudia(UPDATED_ESTUDIA)
            .institucionMedica(UPDATED_INSTITUCION_MEDICA)
            .tipoAfiliacion(UPDATED_TIPO_AFILIACION)
            .numeroAfiliacion(UPDATED_NUMERO_AFILIACION)
            .medioTransporte(UPDATED_MEDIO_TRANSPORTE)
            .tiempoTraslado(UPDATED_TIEMPO_TRASLADO)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION);
        InfoSocioeconomicaDTO infoSocioeconomicaDTO = infoSocioeconomicaMapper.toDto(updatedInfoSocioeconomica);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, infoSocioeconomicaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(infoSocioeconomicaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InfoSocioeconomica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInfoSocioeconomicaToMatchAllProperties(updatedInfoSocioeconomica);
    }

    @Test
    void putNonExistingInfoSocioeconomica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        infoSocioeconomica.setId(longCount.incrementAndGet());

        // Create the InfoSocioeconomica
        InfoSocioeconomicaDTO infoSocioeconomicaDTO = infoSocioeconomicaMapper.toDto(infoSocioeconomica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, infoSocioeconomicaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(infoSocioeconomicaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoSocioeconomica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInfoSocioeconomica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        infoSocioeconomica.setId(longCount.incrementAndGet());

        // Create the InfoSocioeconomica
        InfoSocioeconomicaDTO infoSocioeconomicaDTO = infoSocioeconomicaMapper.toDto(infoSocioeconomica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(infoSocioeconomicaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoSocioeconomica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInfoSocioeconomica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        infoSocioeconomica.setId(longCount.incrementAndGet());

        // Create the InfoSocioeconomica
        InfoSocioeconomicaDTO infoSocioeconomicaDTO = infoSocioeconomicaMapper.toDto(infoSocioeconomica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(infoSocioeconomicaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InfoSocioeconomica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInfoSocioeconomicaWithPatch() throws Exception {
        // Initialize the database
        insertedInfoSocioeconomica = infoSocioeconomicaRepository.save(infoSocioeconomica).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the infoSocioeconomica using partial update
        InfoSocioeconomica partialUpdatedInfoSocioeconomica = new InfoSocioeconomica();
        partialUpdatedInfoSocioeconomica.setId(infoSocioeconomica.getId());

        partialUpdatedInfoSocioeconomica
            .tipoVivienda(UPDATED_TIPO_VIVIENDA)
            .numeroHabitaciones(UPDATED_NUMERO_HABITACIONES)
            .serviciosDisponibles(UPDATED_SERVICIOS_DISPONIBLES)
            .ingresoMensualHogar(UPDATED_INGRESO_MENSUAL_HOGAR)
            .apoyosGubernamentales(UPDATED_APOYOS_GUBERNAMENTALES)
            .ocupacionActual(UPDATED_OCUPACION_ACTUAL)
            .condicionLaboral(UPDATED_CONDICION_LABORAL)
            .tipoEmpleo(UPDATED_TIPO_EMPLEO)
            .tiempoEmpleado(UPDATED_TIEMPO_EMPLEADO)
            .gradoMaximoEstudios(UPDATED_GRADO_MAXIMO_ESTUDIOS)
            .aniosEstudio(UPDATED_ANIOS_ESTUDIO)
            .estudia(UPDATED_ESTUDIA)
            .tipoAfiliacion(UPDATED_TIPO_AFILIACION)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInfoSocioeconomica.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedInfoSocioeconomica))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InfoSocioeconomica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInfoSocioeconomicaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInfoSocioeconomica, infoSocioeconomica),
            getPersistedInfoSocioeconomica(infoSocioeconomica)
        );
    }

    @Test
    void fullUpdateInfoSocioeconomicaWithPatch() throws Exception {
        // Initialize the database
        insertedInfoSocioeconomica = infoSocioeconomicaRepository.save(infoSocioeconomica).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the infoSocioeconomica using partial update
        InfoSocioeconomica partialUpdatedInfoSocioeconomica = new InfoSocioeconomica();
        partialUpdatedInfoSocioeconomica.setId(infoSocioeconomica.getId());

        partialUpdatedInfoSocioeconomica
            .tipoVivienda(UPDATED_TIPO_VIVIENDA)
            .materialVivienda(UPDATED_MATERIAL_VIVIENDA)
            .numeroHabitaciones(UPDATED_NUMERO_HABITACIONES)
            .numeroHabitantes(UPDATED_NUMERO_HABITANTES)
            .serviciosDisponibles(UPDATED_SERVICIOS_DISPONIBLES)
            .ingresoMensual(UPDATED_INGRESO_MENSUAL)
            .ingresoMensualHogar(UPDATED_INGRESO_MENSUAL_HOGAR)
            .gastoMensual(UPDATED_GASTO_MENSUAL)
            .personasDependientes(UPDATED_PERSONAS_DEPENDIENTES)
            .apoyosGubernamentales(UPDATED_APOYOS_GUBERNAMENTALES)
            .ocupacionActual(UPDATED_OCUPACION_ACTUAL)
            .condicionLaboral(UPDATED_CONDICION_LABORAL)
            .tipoEmpleo(UPDATED_TIPO_EMPLEO)
            .lugarTrabajo(UPDATED_LUGAR_TRABAJO)
            .tiempoEmpleado(UPDATED_TIEMPO_EMPLEADO)
            .gradoMaximoEstudios(UPDATED_GRADO_MAXIMO_ESTUDIOS)
            .aniosEstudio(UPDATED_ANIOS_ESTUDIO)
            .estudia(UPDATED_ESTUDIA)
            .institucionMedica(UPDATED_INSTITUCION_MEDICA)
            .tipoAfiliacion(UPDATED_TIPO_AFILIACION)
            .numeroAfiliacion(UPDATED_NUMERO_AFILIACION)
            .medioTransporte(UPDATED_MEDIO_TRANSPORTE)
            .tiempoTraslado(UPDATED_TIEMPO_TRASLADO)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInfoSocioeconomica.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedInfoSocioeconomica))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InfoSocioeconomica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInfoSocioeconomicaUpdatableFieldsEquals(
            partialUpdatedInfoSocioeconomica,
            getPersistedInfoSocioeconomica(partialUpdatedInfoSocioeconomica)
        );
    }

    @Test
    void patchNonExistingInfoSocioeconomica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        infoSocioeconomica.setId(longCount.incrementAndGet());

        // Create the InfoSocioeconomica
        InfoSocioeconomicaDTO infoSocioeconomicaDTO = infoSocioeconomicaMapper.toDto(infoSocioeconomica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, infoSocioeconomicaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(infoSocioeconomicaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoSocioeconomica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInfoSocioeconomica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        infoSocioeconomica.setId(longCount.incrementAndGet());

        // Create the InfoSocioeconomica
        InfoSocioeconomicaDTO infoSocioeconomicaDTO = infoSocioeconomicaMapper.toDto(infoSocioeconomica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(infoSocioeconomicaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoSocioeconomica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInfoSocioeconomica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        infoSocioeconomica.setId(longCount.incrementAndGet());

        // Create the InfoSocioeconomica
        InfoSocioeconomicaDTO infoSocioeconomicaDTO = infoSocioeconomicaMapper.toDto(infoSocioeconomica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(infoSocioeconomicaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InfoSocioeconomica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInfoSocioeconomica() {
        // Initialize the database
        insertedInfoSocioeconomica = infoSocioeconomicaRepository.save(infoSocioeconomica).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the infoSocioeconomica
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, infoSocioeconomica.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return infoSocioeconomicaRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected InfoSocioeconomica getPersistedInfoSocioeconomica(InfoSocioeconomica infoSocioeconomica) {
        return infoSocioeconomicaRepository.findById(infoSocioeconomica.getId()).block();
    }

    protected void assertPersistedInfoSocioeconomicaToMatchAllProperties(InfoSocioeconomica expectedInfoSocioeconomica) {
        // Test fails because reactive api returns an empty object instead of null
        // assertInfoSocioeconomicaAllPropertiesEquals(expectedInfoSocioeconomica, getPersistedInfoSocioeconomica(expectedInfoSocioeconomica));
        assertInfoSocioeconomicaUpdatableFieldsEquals(
            expectedInfoSocioeconomica,
            getPersistedInfoSocioeconomica(expectedInfoSocioeconomica)
        );
    }

    protected void assertPersistedInfoSocioeconomicaToMatchUpdatableProperties(InfoSocioeconomica expectedInfoSocioeconomica) {
        // Test fails because reactive api returns an empty object instead of null
        // assertInfoSocioeconomicaAllUpdatablePropertiesEquals(expectedInfoSocioeconomica, getPersistedInfoSocioeconomica(expectedInfoSocioeconomica));
        assertInfoSocioeconomicaUpdatableFieldsEquals(
            expectedInfoSocioeconomica,
            getPersistedInfoSocioeconomica(expectedInfoSocioeconomica)
        );
    }
}
