package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.SignosVitalesAsserts.*;
import static mx.infotec.pacientesms.web.rest.TestUtil.createUpdateProxyForBean;
import static mx.infotec.pacientesms.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mx.infotec.pacientesms.IntegrationTest;
import mx.infotec.pacientesms.domain.SignosVitales;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.repository.SignosVitalesRepository;
import mx.infotec.pacientesms.service.dto.SignosVitalesDTO;
import mx.infotec.pacientesms.service.mapper.SignosVitalesMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link SignosVitalesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SignosVitalesResourceIT {

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_FRECUENCIA_CARDIACA = 1;
    private static final Integer UPDATED_FRECUENCIA_CARDIACA = 2;

    private static final String DEFAULT_TENSION_ARTERIAL = "AAAAAAAAAA";
    private static final String UPDATED_TENSION_ARTERIAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_FRECUENCIA_RESPIRATORIA = 1;
    private static final Integer UPDATED_FRECUENCIA_RESPIRATORIA = 2;

    private static final BigDecimal DEFAULT_TEMPERATURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_TEMPERATURA = new BigDecimal(2);

    private static final Integer DEFAULT_SATURACION_OXIGENO = 1;
    private static final Integer UPDATED_SATURACION_OXIGENO = 2;

    private static final BigDecimal DEFAULT_PESO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PESO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ESTATURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_ESTATURA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_IMC = new BigDecimal(1);
    private static final BigDecimal UPDATED_IMC = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/signos-vitales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SignosVitalesRepository signosVitalesRepository;

    @Autowired
    private SignosVitalesMapper signosVitalesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SignosVitales signosVitales;

    private SignosVitales insertedSignosVitales;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignosVitales createEntity() {
        return new SignosVitales()
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .frecuenciaCardiaca(DEFAULT_FRECUENCIA_CARDIACA)
            .tensionArterial(DEFAULT_TENSION_ARTERIAL)
            .frecuenciaRespiratoria(DEFAULT_FRECUENCIA_RESPIRATORIA)
            .temperatura(DEFAULT_TEMPERATURA)
            .saturacionOxigeno(DEFAULT_SATURACION_OXIGENO)
            .peso(DEFAULT_PESO)
            .estatura(DEFAULT_ESTATURA)
            .imc(DEFAULT_IMC);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignosVitales createUpdatedEntity() {
        return new SignosVitales()
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .tensionArterial(UPDATED_TENSION_ARTERIAL)
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .temperatura(UPDATED_TEMPERATURA)
            .saturacionOxigeno(UPDATED_SATURACION_OXIGENO)
            .peso(UPDATED_PESO)
            .estatura(UPDATED_ESTATURA)
            .imc(UPDATED_IMC);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SignosVitales.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        signosVitales = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSignosVitales != null) {
            signosVitalesRepository.delete(insertedSignosVitales).block();
            insertedSignosVitales = null;
        }
        deleteEntities(em);
    }

    @Test
    void createSignosVitales() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);
        var returnedSignosVitalesDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SignosVitalesDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the SignosVitales in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSignosVitales = signosVitalesMapper.toEntity(returnedSignosVitalesDTO);
        assertSignosVitalesUpdatableFieldsEquals(returnedSignosVitales, getPersistedSignosVitales(returnedSignosVitales));

        insertedSignosVitales = returnedSignosVitales;
    }

    @Test
    void createSignosVitalesWithExistingId() throws Exception {
        // Create the SignosVitales with an existing ID
        signosVitales.setId(1L);
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkFechaRegistroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        signosVitales.setFechaRegistro(null);

        // Create the SignosVitales, which fails.
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSignosVitales() {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.save(signosVitales).block();

        // Get all the signosVitalesList
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
            .value(hasItem(signosVitales.getId().intValue()))
            .jsonPath("$.[*].fechaRegistro")
            .value(hasItem(DEFAULT_FECHA_REGISTRO.toString()))
            .jsonPath("$.[*].frecuenciaCardiaca")
            .value(hasItem(DEFAULT_FRECUENCIA_CARDIACA))
            .jsonPath("$.[*].tensionArterial")
            .value(hasItem(DEFAULT_TENSION_ARTERIAL))
            .jsonPath("$.[*].frecuenciaRespiratoria")
            .value(hasItem(DEFAULT_FRECUENCIA_RESPIRATORIA))
            .jsonPath("$.[*].temperatura")
            .value(hasItem(sameNumber(DEFAULT_TEMPERATURA)))
            .jsonPath("$.[*].saturacionOxigeno")
            .value(hasItem(DEFAULT_SATURACION_OXIGENO))
            .jsonPath("$.[*].peso")
            .value(hasItem(sameNumber(DEFAULT_PESO)))
            .jsonPath("$.[*].estatura")
            .value(hasItem(sameNumber(DEFAULT_ESTATURA)))
            .jsonPath("$.[*].imc")
            .value(hasItem(sameNumber(DEFAULT_IMC)));
    }

    @Test
    void getSignosVitales() {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.save(signosVitales).block();

        // Get the signosVitales
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, signosVitales.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(signosVitales.getId().intValue()))
            .jsonPath("$.fechaRegistro")
            .value(is(DEFAULT_FECHA_REGISTRO.toString()))
            .jsonPath("$.frecuenciaCardiaca")
            .value(is(DEFAULT_FRECUENCIA_CARDIACA))
            .jsonPath("$.tensionArterial")
            .value(is(DEFAULT_TENSION_ARTERIAL))
            .jsonPath("$.frecuenciaRespiratoria")
            .value(is(DEFAULT_FRECUENCIA_RESPIRATORIA))
            .jsonPath("$.temperatura")
            .value(is(sameNumber(DEFAULT_TEMPERATURA)))
            .jsonPath("$.saturacionOxigeno")
            .value(is(DEFAULT_SATURACION_OXIGENO))
            .jsonPath("$.peso")
            .value(is(sameNumber(DEFAULT_PESO)))
            .jsonPath("$.estatura")
            .value(is(sameNumber(DEFAULT_ESTATURA)))
            .jsonPath("$.imc")
            .value(is(sameNumber(DEFAULT_IMC)));
    }

    @Test
    void getNonExistingSignosVitales() {
        // Get the signosVitales
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSignosVitales() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.save(signosVitales).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signosVitales
        SignosVitales updatedSignosVitales = signosVitalesRepository.findById(signosVitales.getId()).block();
        updatedSignosVitales
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .tensionArterial(UPDATED_TENSION_ARTERIAL)
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .temperatura(UPDATED_TEMPERATURA)
            .saturacionOxigeno(UPDATED_SATURACION_OXIGENO)
            .peso(UPDATED_PESO)
            .estatura(UPDATED_ESTATURA)
            .imc(UPDATED_IMC);
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(updatedSignosVitales);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, signosVitalesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSignosVitalesToMatchAllProperties(updatedSignosVitales);
    }

    @Test
    void putNonExistingSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, signosVitalesDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSignosVitalesWithPatch() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.save(signosVitales).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signosVitales using partial update
        SignosVitales partialUpdatedSignosVitales = new SignosVitales();
        partialUpdatedSignosVitales.setId(signosVitales.getId());

        partialUpdatedSignosVitales
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .temperatura(UPDATED_TEMPERATURA)
            .estatura(UPDATED_ESTATURA)
            .imc(UPDATED_IMC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSignosVitales.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSignosVitales))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SignosVitales in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSignosVitalesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSignosVitales, signosVitales),
            getPersistedSignosVitales(signosVitales)
        );
    }

    @Test
    void fullUpdateSignosVitalesWithPatch() throws Exception {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.save(signosVitales).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signosVitales using partial update
        SignosVitales partialUpdatedSignosVitales = new SignosVitales();
        partialUpdatedSignosVitales.setId(signosVitales.getId());

        partialUpdatedSignosVitales
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .tensionArterial(UPDATED_TENSION_ARTERIAL)
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .temperatura(UPDATED_TEMPERATURA)
            .saturacionOxigeno(UPDATED_SATURACION_OXIGENO)
            .peso(UPDATED_PESO)
            .estatura(UPDATED_ESTATURA)
            .imc(UPDATED_IMC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSignosVitales.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSignosVitales))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SignosVitales in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSignosVitalesUpdatableFieldsEquals(partialUpdatedSignosVitales, getPersistedSignosVitales(partialUpdatedSignosVitales));
    }

    @Test
    void patchNonExistingSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, signosVitalesDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSignosVitales() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signosVitales.setId(longCount.incrementAndGet());

        // Create the SignosVitales
        SignosVitalesDTO signosVitalesDTO = signosVitalesMapper.toDto(signosVitales);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(signosVitalesDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SignosVitales in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSignosVitales() {
        // Initialize the database
        insertedSignosVitales = signosVitalesRepository.save(signosVitales).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the signosVitales
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, signosVitales.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return signosVitalesRepository.count().block();
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

    protected SignosVitales getPersistedSignosVitales(SignosVitales signosVitales) {
        return signosVitalesRepository.findById(signosVitales.getId()).block();
    }

    protected void assertPersistedSignosVitalesToMatchAllProperties(SignosVitales expectedSignosVitales) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSignosVitalesAllPropertiesEquals(expectedSignosVitales, getPersistedSignosVitales(expectedSignosVitales));
        assertSignosVitalesUpdatableFieldsEquals(expectedSignosVitales, getPersistedSignosVitales(expectedSignosVitales));
    }

    protected void assertPersistedSignosVitalesToMatchUpdatableProperties(SignosVitales expectedSignosVitales) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSignosVitalesAllUpdatablePropertiesEquals(expectedSignosVitales, getPersistedSignosVitales(expectedSignosVitales));
        assertSignosVitalesUpdatableFieldsEquals(expectedSignosVitales, getPersistedSignosVitales(expectedSignosVitales));
    }
}
