package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.EntidadFederativaAsserts.*;
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
import mx.infotec.pacientesms.domain.EntidadFederativa;
import mx.infotec.pacientesms.repository.EntidadFederativaRepository;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.service.dto.EntidadFederativaDTO;
import mx.infotec.pacientesms.service.mapper.EntidadFederativaMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link EntidadFederativaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EntidadFederativaResourceIT {

    private static final String DEFAULT_CLAVE = "AAAAA";
    private static final String UPDATED_CLAVE = "BBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_ABREVIATURA = "AAAAAAAAAA";
    private static final String UPDATED_ABREVIATURA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entidad-federativas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntidadFederativaRepository entidadFederativaRepository;

    @Autowired
    private EntidadFederativaMapper entidadFederativaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EntidadFederativa entidadFederativa;

    private EntidadFederativa insertedEntidadFederativa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntidadFederativa createEntity() {
        return new EntidadFederativa().clave(DEFAULT_CLAVE).nombre(DEFAULT_NOMBRE).abreviatura(DEFAULT_ABREVIATURA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntidadFederativa createUpdatedEntity() {
        return new EntidadFederativa().clave(UPDATED_CLAVE).nombre(UPDATED_NOMBRE).abreviatura(UPDATED_ABREVIATURA);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EntidadFederativa.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        entidadFederativa = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEntidadFederativa != null) {
            entidadFederativaRepository.delete(insertedEntidadFederativa).block();
            insertedEntidadFederativa = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEntidadFederativa() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntidadFederativa
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);
        var returnedEntidadFederativaDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EntidadFederativaDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the EntidadFederativa in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntidadFederativa = entidadFederativaMapper.toEntity(returnedEntidadFederativaDTO);
        assertEntidadFederativaUpdatableFieldsEquals(returnedEntidadFederativa, getPersistedEntidadFederativa(returnedEntidadFederativa));

        insertedEntidadFederativa = returnedEntidadFederativa;
    }

    @Test
    void createEntidadFederativaWithExistingId() throws Exception {
        // Create the EntidadFederativa with an existing ID
        entidadFederativa.setId(1L);
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntidadFederativa in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkClaveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        entidadFederativa.setClave(null);

        // Create the EntidadFederativa, which fails.
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        entidadFederativa.setNombre(null);

        // Create the EntidadFederativa, which fails.
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllEntidadFederativasAsStream() {
        // Initialize the database
        entidadFederativaRepository.save(entidadFederativa).block();

        List<EntidadFederativa> entidadFederativaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(EntidadFederativaDTO.class)
            .getResponseBody()
            .map(entidadFederativaMapper::toEntity)
            .filter(entidadFederativa::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(entidadFederativaList).isNotNull();
        assertThat(entidadFederativaList).hasSize(1);
        EntidadFederativa testEntidadFederativa = entidadFederativaList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertEntidadFederativaAllPropertiesEquals(entidadFederativa, testEntidadFederativa);
        assertEntidadFederativaUpdatableFieldsEquals(entidadFederativa, testEntidadFederativa);
    }

    @Test
    void getAllEntidadFederativas() {
        // Initialize the database
        insertedEntidadFederativa = entidadFederativaRepository.save(entidadFederativa).block();

        // Get all the entidadFederativaList
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
            .value(hasItem(entidadFederativa.getId().intValue()))
            .jsonPath("$.[*].clave")
            .value(hasItem(DEFAULT_CLAVE))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].abreviatura")
            .value(hasItem(DEFAULT_ABREVIATURA));
    }

    @Test
    void getEntidadFederativa() {
        // Initialize the database
        insertedEntidadFederativa = entidadFederativaRepository.save(entidadFederativa).block();

        // Get the entidadFederativa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, entidadFederativa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(entidadFederativa.getId().intValue()))
            .jsonPath("$.clave")
            .value(is(DEFAULT_CLAVE))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.abreviatura")
            .value(is(DEFAULT_ABREVIATURA));
    }

    @Test
    void getNonExistingEntidadFederativa() {
        // Get the entidadFederativa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEntidadFederativa() throws Exception {
        // Initialize the database
        insertedEntidadFederativa = entidadFederativaRepository.save(entidadFederativa).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entidadFederativa
        EntidadFederativa updatedEntidadFederativa = entidadFederativaRepository.findById(entidadFederativa.getId()).block();
        updatedEntidadFederativa.clave(UPDATED_CLAVE).nombre(UPDATED_NOMBRE).abreviatura(UPDATED_ABREVIATURA);
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(updatedEntidadFederativa);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entidadFederativaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntidadFederativa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntidadFederativaToMatchAllProperties(updatedEntidadFederativa);
    }

    @Test
    void putNonExistingEntidadFederativa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadFederativa.setId(longCount.incrementAndGet());

        // Create the EntidadFederativa
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, entidadFederativaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntidadFederativa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEntidadFederativa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadFederativa.setId(longCount.incrementAndGet());

        // Create the EntidadFederativa
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntidadFederativa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEntidadFederativa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadFederativa.setId(longCount.incrementAndGet());

        // Create the EntidadFederativa
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntidadFederativa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEntidadFederativaWithPatch() throws Exception {
        // Initialize the database
        insertedEntidadFederativa = entidadFederativaRepository.save(entidadFederativa).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entidadFederativa using partial update
        EntidadFederativa partialUpdatedEntidadFederativa = new EntidadFederativa();
        partialUpdatedEntidadFederativa.setId(entidadFederativa.getId());

        partialUpdatedEntidadFederativa.clave(UPDATED_CLAVE).nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntidadFederativa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntidadFederativa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntidadFederativa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntidadFederativaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntidadFederativa, entidadFederativa),
            getPersistedEntidadFederativa(entidadFederativa)
        );
    }

    @Test
    void fullUpdateEntidadFederativaWithPatch() throws Exception {
        // Initialize the database
        insertedEntidadFederativa = entidadFederativaRepository.save(entidadFederativa).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entidadFederativa using partial update
        EntidadFederativa partialUpdatedEntidadFederativa = new EntidadFederativa();
        partialUpdatedEntidadFederativa.setId(entidadFederativa.getId());

        partialUpdatedEntidadFederativa.clave(UPDATED_CLAVE).nombre(UPDATED_NOMBRE).abreviatura(UPDATED_ABREVIATURA);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEntidadFederativa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEntidadFederativa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EntidadFederativa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntidadFederativaUpdatableFieldsEquals(
            partialUpdatedEntidadFederativa,
            getPersistedEntidadFederativa(partialUpdatedEntidadFederativa)
        );
    }

    @Test
    void patchNonExistingEntidadFederativa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadFederativa.setId(longCount.incrementAndGet());

        // Create the EntidadFederativa
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, entidadFederativaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntidadFederativa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEntidadFederativa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadFederativa.setId(longCount.incrementAndGet());

        // Create the EntidadFederativa
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EntidadFederativa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEntidadFederativa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadFederativa.setId(longCount.incrementAndGet());

        // Create the EntidadFederativa
        EntidadFederativaDTO entidadFederativaDTO = entidadFederativaMapper.toDto(entidadFederativa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(entidadFederativaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EntidadFederativa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEntidadFederativa() {
        // Initialize the database
        insertedEntidadFederativa = entidadFederativaRepository.save(entidadFederativa).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entidadFederativa
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, entidadFederativa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entidadFederativaRepository.count().block();
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

    protected EntidadFederativa getPersistedEntidadFederativa(EntidadFederativa entidadFederativa) {
        return entidadFederativaRepository.findById(entidadFederativa.getId()).block();
    }

    protected void assertPersistedEntidadFederativaToMatchAllProperties(EntidadFederativa expectedEntidadFederativa) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntidadFederativaAllPropertiesEquals(expectedEntidadFederativa, getPersistedEntidadFederativa(expectedEntidadFederativa));
        assertEntidadFederativaUpdatableFieldsEquals(expectedEntidadFederativa, getPersistedEntidadFederativa(expectedEntidadFederativa));
    }

    protected void assertPersistedEntidadFederativaToMatchUpdatableProperties(EntidadFederativa expectedEntidadFederativa) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEntidadFederativaAllUpdatablePropertiesEquals(expectedEntidadFederativa, getPersistedEntidadFederativa(expectedEntidadFederativa));
        assertEntidadFederativaUpdatableFieldsEquals(expectedEntidadFederativa, getPersistedEntidadFederativa(expectedEntidadFederativa));
    }
}
