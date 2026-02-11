package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.TipoVialidadAsserts.*;
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
import mx.infotec.pacientesms.domain.TipoVialidad;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.repository.TipoVialidadRepository;
import mx.infotec.pacientesms.service.dto.TipoVialidadDTO;
import mx.infotec.pacientesms.service.mapper.TipoVialidadMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TipoVialidadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TipoVialidadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-vialidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoVialidadRepository tipoVialidadRepository;

    @Autowired
    private TipoVialidadMapper tipoVialidadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TipoVialidad tipoVialidad;

    private TipoVialidad insertedTipoVialidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoVialidad createEntity() {
        return new TipoVialidad().nombre(DEFAULT_NOMBRE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoVialidad createUpdatedEntity() {
        return new TipoVialidad().nombre(UPDATED_NOMBRE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TipoVialidad.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        tipoVialidad = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTipoVialidad != null) {
            tipoVialidadRepository.delete(insertedTipoVialidad).block();
            insertedTipoVialidad = null;
        }
        deleteEntities(em);
    }

    @Test
    void createTipoVialidad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoVialidad
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(tipoVialidad);
        var returnedTipoVialidadDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(TipoVialidadDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the TipoVialidad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoVialidad = tipoVialidadMapper.toEntity(returnedTipoVialidadDTO);
        assertTipoVialidadUpdatableFieldsEquals(returnedTipoVialidad, getPersistedTipoVialidad(returnedTipoVialidad));

        insertedTipoVialidad = returnedTipoVialidad;
    }

    @Test
    void createTipoVialidadWithExistingId() throws Exception {
        // Create the TipoVialidad with an existing ID
        tipoVialidad.setId(1L);
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(tipoVialidad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoVialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoVialidad.setNombre(null);

        // Create the TipoVialidad, which fails.
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(tipoVialidad);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllTipoVialidadsAsStream() {
        // Initialize the database
        tipoVialidadRepository.save(tipoVialidad).block();

        List<TipoVialidad> tipoVialidadList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(TipoVialidadDTO.class)
            .getResponseBody()
            .map(tipoVialidadMapper::toEntity)
            .filter(tipoVialidad::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(tipoVialidadList).isNotNull();
        assertThat(tipoVialidadList).hasSize(1);
        TipoVialidad testTipoVialidad = tipoVialidadList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertTipoVialidadAllPropertiesEquals(tipoVialidad, testTipoVialidad);
        assertTipoVialidadUpdatableFieldsEquals(tipoVialidad, testTipoVialidad);
    }

    @Test
    void getAllTipoVialidads() {
        // Initialize the database
        insertedTipoVialidad = tipoVialidadRepository.save(tipoVialidad).block();

        // Get all the tipoVialidadList
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
            .value(hasItem(tipoVialidad.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE));
    }

    @Test
    void getTipoVialidad() {
        // Initialize the database
        insertedTipoVialidad = tipoVialidadRepository.save(tipoVialidad).block();

        // Get the tipoVialidad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tipoVialidad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tipoVialidad.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE));
    }

    @Test
    void getNonExistingTipoVialidad() {
        // Get the tipoVialidad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTipoVialidad() throws Exception {
        // Initialize the database
        insertedTipoVialidad = tipoVialidadRepository.save(tipoVialidad).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoVialidad
        TipoVialidad updatedTipoVialidad = tipoVialidadRepository.findById(tipoVialidad.getId()).block();
        updatedTipoVialidad.nombre(UPDATED_NOMBRE);
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(updatedTipoVialidad);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tipoVialidadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TipoVialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoVialidadToMatchAllProperties(updatedTipoVialidad);
    }

    @Test
    void putNonExistingTipoVialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoVialidad.setId(longCount.incrementAndGet());

        // Create the TipoVialidad
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(tipoVialidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tipoVialidadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoVialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTipoVialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoVialidad.setId(longCount.incrementAndGet());

        // Create the TipoVialidad
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(tipoVialidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoVialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTipoVialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoVialidad.setId(longCount.incrementAndGet());

        // Create the TipoVialidad
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(tipoVialidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TipoVialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTipoVialidadWithPatch() throws Exception {
        // Initialize the database
        insertedTipoVialidad = tipoVialidadRepository.save(tipoVialidad).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoVialidad using partial update
        TipoVialidad partialUpdatedTipoVialidad = new TipoVialidad();
        partialUpdatedTipoVialidad.setId(tipoVialidad.getId());

        partialUpdatedTipoVialidad.nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTipoVialidad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedTipoVialidad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TipoVialidad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoVialidadUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoVialidad, tipoVialidad),
            getPersistedTipoVialidad(tipoVialidad)
        );
    }

    @Test
    void fullUpdateTipoVialidadWithPatch() throws Exception {
        // Initialize the database
        insertedTipoVialidad = tipoVialidadRepository.save(tipoVialidad).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoVialidad using partial update
        TipoVialidad partialUpdatedTipoVialidad = new TipoVialidad();
        partialUpdatedTipoVialidad.setId(tipoVialidad.getId());

        partialUpdatedTipoVialidad.nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTipoVialidad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedTipoVialidad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TipoVialidad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoVialidadUpdatableFieldsEquals(partialUpdatedTipoVialidad, getPersistedTipoVialidad(partialUpdatedTipoVialidad));
    }

    @Test
    void patchNonExistingTipoVialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoVialidad.setId(longCount.incrementAndGet());

        // Create the TipoVialidad
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(tipoVialidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tipoVialidadDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoVialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTipoVialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoVialidad.setId(longCount.incrementAndGet());

        // Create the TipoVialidad
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(tipoVialidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TipoVialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTipoVialidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoVialidad.setId(longCount.incrementAndGet());

        // Create the TipoVialidad
        TipoVialidadDTO tipoVialidadDTO = tipoVialidadMapper.toDto(tipoVialidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(tipoVialidadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TipoVialidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTipoVialidad() {
        // Initialize the database
        insertedTipoVialidad = tipoVialidadRepository.save(tipoVialidad).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoVialidad
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tipoVialidad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoVialidadRepository.count().block();
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

    protected TipoVialidad getPersistedTipoVialidad(TipoVialidad tipoVialidad) {
        return tipoVialidadRepository.findById(tipoVialidad.getId()).block();
    }

    protected void assertPersistedTipoVialidadToMatchAllProperties(TipoVialidad expectedTipoVialidad) {
        // Test fails because reactive api returns an empty object instead of null
        // assertTipoVialidadAllPropertiesEquals(expectedTipoVialidad, getPersistedTipoVialidad(expectedTipoVialidad));
        assertTipoVialidadUpdatableFieldsEquals(expectedTipoVialidad, getPersistedTipoVialidad(expectedTipoVialidad));
    }

    protected void assertPersistedTipoVialidadToMatchUpdatableProperties(TipoVialidad expectedTipoVialidad) {
        // Test fails because reactive api returns an empty object instead of null
        // assertTipoVialidadAllUpdatablePropertiesEquals(expectedTipoVialidad, getPersistedTipoVialidad(expectedTipoVialidad));
        assertTipoVialidadUpdatableFieldsEquals(expectedTipoVialidad, getPersistedTipoVialidad(expectedTipoVialidad));
    }
}
