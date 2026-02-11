package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.AlergiaAsserts.*;
import static mx.infotec.pacientesms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mx.infotec.pacientesms.IntegrationTest;
import mx.infotec.pacientesms.domain.Alergia;
import mx.infotec.pacientesms.repository.AlergiaRepository;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.service.dto.AlergiaDTO;
import mx.infotec.pacientesms.service.mapper.AlergiaMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AlergiaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AlergiaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/alergias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlergiaRepository alergiaRepository;

    @Autowired
    private AlergiaMapper alergiaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Alergia alergia;

    private Alergia insertedAlergia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alergia createEntity() {
        return new Alergia().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alergia createUpdatedEntity() {
        return new Alergia().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Alergia.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        alergia = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlergia != null) {
            alergiaRepository.delete(insertedAlergia).block();
            insertedAlergia = null;
        }
        deleteEntities(em);
    }

    @Test
    void createAlergia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Alergia
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(alergia);
        var returnedAlergiaDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(AlergiaDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Alergia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlergia = alergiaMapper.toEntity(returnedAlergiaDTO);
        assertAlergiaUpdatableFieldsEquals(returnedAlergia, getPersistedAlergia(returnedAlergia));

        insertedAlergia = returnedAlergia;
    }

    @Test
    void createAlergiaWithExistingId() throws Exception {
        // Create the Alergia with an existing ID
        alergia.setId(1L);
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(alergia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alergia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alergia.setNombre(null);

        // Create the Alergia, which fails.
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(alergia);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllAlergiasAsStream() {
        // Initialize the database
        alergiaRepository.save(alergia).block();

        List<Alergia> alergiaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(AlergiaDTO.class)
            .getResponseBody()
            .map(alergiaMapper::toEntity)
            .filter(alergia::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(alergiaList).isNotNull();
        assertThat(alergiaList).hasSize(1);
        Alergia testAlergia = alergiaList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertAlergiaAllPropertiesEquals(alergia, testAlergia);
        assertAlergiaUpdatableFieldsEquals(alergia, testAlergia);
    }

    @Test
    void getAllAlergias() {
        // Initialize the database
        insertedAlergia = alergiaRepository.save(alergia).block();

        // Get all the alergiaList
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
            .value(hasItem(alergia.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].descripcion")
            .value(hasItem(DEFAULT_DESCRIPCION));
    }

    @Test
    void getAlergia() {
        // Initialize the database
        insertedAlergia = alergiaRepository.save(alergia).block();

        // Get the alergia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, alergia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(alergia.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.descripcion")
            .value(is(DEFAULT_DESCRIPCION));
    }

    @Test
    void getNonExistingAlergia() {
        // Get the alergia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAlergia() throws Exception {
        // Initialize the database
        insertedAlergia = alergiaRepository.save(alergia).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alergia
        Alergia updatedAlergia = alergiaRepository.findById(alergia.getId()).block();
        updatedAlergia.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(updatedAlergia);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, alergiaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Alergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlergiaToMatchAllProperties(updatedAlergia);
    }

    @Test
    void putNonExistingAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alergia.setId(longCount.incrementAndGet());

        // Create the Alergia
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(alergia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, alergiaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alergia.setId(longCount.incrementAndGet());

        // Create the Alergia
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(alergia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alergia.setId(longCount.incrementAndGet());

        // Create the Alergia
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(alergia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Alergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAlergiaWithPatch() throws Exception {
        // Initialize the database
        insertedAlergia = alergiaRepository.save(alergia).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alergia using partial update
        Alergia partialUpdatedAlergia = new Alergia();
        partialUpdatedAlergia.setId(alergia.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAlergia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAlergia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Alergia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlergiaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlergia, alergia), getPersistedAlergia(alergia));
    }

    @Test
    void fullUpdateAlergiaWithPatch() throws Exception {
        // Initialize the database
        insertedAlergia = alergiaRepository.save(alergia).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alergia using partial update
        Alergia partialUpdatedAlergia = new Alergia();
        partialUpdatedAlergia.setId(alergia.getId());

        partialUpdatedAlergia.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAlergia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedAlergia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Alergia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlergiaUpdatableFieldsEquals(partialUpdatedAlergia, getPersistedAlergia(partialUpdatedAlergia));
    }

    @Test
    void patchNonExistingAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alergia.setId(longCount.incrementAndGet());

        // Create the Alergia
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(alergia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, alergiaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alergia.setId(longCount.incrementAndGet());

        // Create the Alergia
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(alergia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Alergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alergia.setId(longCount.incrementAndGet());

        // Create the Alergia
        AlergiaDTO alergiaDTO = alergiaMapper.toDto(alergia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(alergiaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Alergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAlergia() {
        // Initialize the database
        insertedAlergia = alergiaRepository.save(alergia).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alergia
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, alergia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alergiaRepository.count().block();
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

    protected Alergia getPersistedAlergia(Alergia alergia) {
        return alergiaRepository.findById(alergia.getId()).block();
    }

    protected void assertPersistedAlergiaToMatchAllProperties(Alergia expectedAlergia) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAlergiaAllPropertiesEquals(expectedAlergia, getPersistedAlergia(expectedAlergia));
        assertAlergiaUpdatableFieldsEquals(expectedAlergia, getPersistedAlergia(expectedAlergia));
    }

    protected void assertPersistedAlergiaToMatchUpdatableProperties(Alergia expectedAlergia) {
        // Test fails because reactive api returns an empty object instead of null
        // assertAlergiaAllUpdatablePropertiesEquals(expectedAlergia, getPersistedAlergia(expectedAlergia));
        assertAlergiaUpdatableFieldsEquals(expectedAlergia, getPersistedAlergia(expectedAlergia));
    }
}
