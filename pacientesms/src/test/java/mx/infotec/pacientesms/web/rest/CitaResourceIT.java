package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.CitaAsserts.*;
import static mx.infotec.pacientesms.web.rest.TestUtil.createUpdateProxyForBean;
import static mx.infotec.pacientesms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mx.infotec.pacientesms.IntegrationTest;
import mx.infotec.pacientesms.domain.Cita;
import mx.infotec.pacientesms.repository.CitaRepository;
import mx.infotec.pacientesms.repository.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CitaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CitaResourceIT {

    private static final Integer DEFAULT_ECU = 1;
    private static final Integer UPDATED_ECU = 2;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO_PATERNO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO_PATERNO = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO_MATERNO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO_MATERNO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_HORA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_HORA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/citas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Cita cita;

    private Cita insertedCita;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createEntity() {
        return new Cita()
            .ecu(DEFAULT_ECU)
            .nombre(DEFAULT_NOMBRE)
            .apellidoPaterno(DEFAULT_APELLIDO_PATERNO)
            .apellidoMaterno(DEFAULT_APELLIDO_MATERNO)
            .fechaHora(DEFAULT_FECHA_HORA)
            .motivo(DEFAULT_MOTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cita createUpdatedEntity() {
        return new Cita()
            .ecu(UPDATED_ECU)
            .nombre(UPDATED_NOMBRE)
            .apellidoPaterno(UPDATED_APELLIDO_PATERNO)
            .apellidoMaterno(UPDATED_APELLIDO_MATERNO)
            .fechaHora(UPDATED_FECHA_HORA)
            .motivo(UPDATED_MOTIVO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Cita.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        cita = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCita != null) {
            citaRepository.delete(insertedCita).block();
            insertedCita = null;
        }
        deleteEntities(em);
    }

    @Test
    void createCita() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cita
        var returnedCita = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Cita.class)
            .returnResult()
            .getResponseBody();

        // Validate the Cita in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCitaUpdatableFieldsEquals(returnedCita, getPersistedCita(returnedCita));

        insertedCita = returnedCita;
    }

    @Test
    void createCitaWithExistingId() throws Exception {
        // Create the Cita with an existing ID
        cita.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkEcuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cita.setEcu(null);

        // Create the Cita, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cita.setNombre(null);

        // Create the Cita, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkApellidoPaternoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cita.setApellidoPaterno(null);

        // Create the Cita, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaHoraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cita.setFechaHora(null);

        // Create the Cita, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkMotivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cita.setMotivo(null);

        // Create the Cita, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCitas() {
        // Initialize the database
        insertedCita = citaRepository.save(cita).block();

        // Get all the citaList
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
            .value(hasItem(cita.getId().intValue()))
            .jsonPath("$.[*].ecu")
            .value(hasItem(DEFAULT_ECU))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].apellidoPaterno")
            .value(hasItem(DEFAULT_APELLIDO_PATERNO))
            .jsonPath("$.[*].apellidoMaterno")
            .value(hasItem(DEFAULT_APELLIDO_MATERNO))
            .jsonPath("$.[*].fechaHora")
            .value(hasItem(sameInstant(DEFAULT_FECHA_HORA)))
            .jsonPath("$.[*].motivo")
            .value(hasItem(DEFAULT_MOTIVO));
    }

    @Test
    void getCita() {
        // Initialize the database
        insertedCita = citaRepository.save(cita).block();

        // Get the cita
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cita.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cita.getId().intValue()))
            .jsonPath("$.ecu")
            .value(is(DEFAULT_ECU))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.apellidoPaterno")
            .value(is(DEFAULT_APELLIDO_PATERNO))
            .jsonPath("$.apellidoMaterno")
            .value(is(DEFAULT_APELLIDO_MATERNO))
            .jsonPath("$.fechaHora")
            .value(is(sameInstant(DEFAULT_FECHA_HORA)))
            .jsonPath("$.motivo")
            .value(is(DEFAULT_MOTIVO));
    }

    @Test
    void getNonExistingCita() {
        // Get the cita
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCita() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.save(cita).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cita
        Cita updatedCita = citaRepository.findById(cita.getId()).block();
        updatedCita
            .ecu(UPDATED_ECU)
            .nombre(UPDATED_NOMBRE)
            .apellidoPaterno(UPDATED_APELLIDO_PATERNO)
            .apellidoMaterno(UPDATED_APELLIDO_MATERNO)
            .fechaHora(UPDATED_FECHA_HORA)
            .motivo(UPDATED_MOTIVO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCita.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedCita))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCitaToMatchAllProperties(updatedCita);
    }

    @Test
    void putNonExistingCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cita.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.save(cita).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita.apellidoPaterno(UPDATED_APELLIDO_PATERNO).fechaHora(UPDATED_FECHA_HORA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCita.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCita))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cita in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCita, cita), getPersistedCita(cita));
    }

    @Test
    void fullUpdateCitaWithPatch() throws Exception {
        // Initialize the database
        insertedCita = citaRepository.save(cita).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cita using partial update
        Cita partialUpdatedCita = new Cita();
        partialUpdatedCita.setId(cita.getId());

        partialUpdatedCita
            .ecu(UPDATED_ECU)
            .nombre(UPDATED_NOMBRE)
            .apellidoPaterno(UPDATED_APELLIDO_PATERNO)
            .apellidoMaterno(UPDATED_APELLIDO_MATERNO)
            .fechaHora(UPDATED_FECHA_HORA)
            .motivo(UPDATED_MOTIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCita.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCita))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cita in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitaUpdatableFieldsEquals(partialUpdatedCita, getPersistedCita(partialUpdatedCita));
    }

    @Test
    void patchNonExistingCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cita.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCita() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cita.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(cita))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cita in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCita() {
        // Initialize the database
        insertedCita = citaRepository.save(cita).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cita
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cita.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return citaRepository.count().block();
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

    protected Cita getPersistedCita(Cita cita) {
        return citaRepository.findById(cita.getId()).block();
    }

    protected void assertPersistedCitaToMatchAllProperties(Cita expectedCita) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCitaAllPropertiesEquals(expectedCita, getPersistedCita(expectedCita));
        assertCitaUpdatableFieldsEquals(expectedCita, getPersistedCita(expectedCita));
    }

    protected void assertPersistedCitaToMatchUpdatableProperties(Cita expectedCita) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCitaAllUpdatablePropertiesEquals(expectedCita, getPersistedCita(expectedCita));
        assertCitaUpdatableFieldsEquals(expectedCita, getPersistedCita(expectedCita));
    }
}
