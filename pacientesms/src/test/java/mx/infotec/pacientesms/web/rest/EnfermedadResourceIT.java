package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.EnfermedadAsserts.*;
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
import mx.infotec.pacientesms.domain.Enfermedad;
import mx.infotec.pacientesms.repository.EnfermedadRepository;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.service.dto.EnfermedadDTO;
import mx.infotec.pacientesms.service.mapper.EnfermedadMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link EnfermedadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EnfermedadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_CIE = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_CIE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enfermedads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnfermedadRepository enfermedadRepository;

    @Autowired
    private EnfermedadMapper enfermedadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Enfermedad enfermedad;

    private Enfermedad insertedEnfermedad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermedad createEntity() {
        return new Enfermedad().nombre(DEFAULT_NOMBRE).tipo(DEFAULT_TIPO).codigoCIE(DEFAULT_CODIGO_CIE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermedad createUpdatedEntity() {
        return new Enfermedad().nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).codigoCIE(UPDATED_CODIGO_CIE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Enfermedad.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        enfermedad = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEnfermedad != null) {
            enfermedadRepository.delete(insertedEnfermedad).block();
            insertedEnfermedad = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEnfermedad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);
        var returnedEnfermedadDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EnfermedadDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Enfermedad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnfermedad = enfermedadMapper.toEntity(returnedEnfermedadDTO);
        assertEnfermedadUpdatableFieldsEquals(returnedEnfermedad, getPersistedEnfermedad(returnedEnfermedad));

        insertedEnfermedad = returnedEnfermedad;
    }

    @Test
    void createEnfermedadWithExistingId() throws Exception {
        // Create the Enfermedad with an existing ID
        enfermedad.setId(1L);
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enfermedad.setNombre(null);

        // Create the Enfermedad, which fails.
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllEnfermedadsAsStream() {
        // Initialize the database
        enfermedadRepository.save(enfermedad).block();

        List<Enfermedad> enfermedadList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(EnfermedadDTO.class)
            .getResponseBody()
            .map(enfermedadMapper::toEntity)
            .filter(enfermedad::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(enfermedadList).isNotNull();
        assertThat(enfermedadList).hasSize(1);
        Enfermedad testEnfermedad = enfermedadList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertEnfermedadAllPropertiesEquals(enfermedad, testEnfermedad);
        assertEnfermedadUpdatableFieldsEquals(enfermedad, testEnfermedad);
    }

    @Test
    void getAllEnfermedads() {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.save(enfermedad).block();

        // Get all the enfermedadList
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
            .value(hasItem(enfermedad.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].tipo")
            .value(hasItem(DEFAULT_TIPO))
            .jsonPath("$.[*].codigoCIE")
            .value(hasItem(DEFAULT_CODIGO_CIE));
    }

    @Test
    void getEnfermedad() {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.save(enfermedad).block();

        // Get the enfermedad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, enfermedad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(enfermedad.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.tipo")
            .value(is(DEFAULT_TIPO))
            .jsonPath("$.codigoCIE")
            .value(is(DEFAULT_CODIGO_CIE));
    }

    @Test
    void getNonExistingEnfermedad() {
        // Get the enfermedad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEnfermedad() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.save(enfermedad).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermedad
        Enfermedad updatedEnfermedad = enfermedadRepository.findById(enfermedad.getId()).block();
        updatedEnfermedad.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).codigoCIE(UPDATED_CODIGO_CIE);
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(updatedEnfermedad);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, enfermedadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnfermedadToMatchAllProperties(updatedEnfermedad);
    }

    @Test
    void putNonExistingEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, enfermedadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEnfermedadWithPatch() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.save(enfermedad).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermedad using partial update
        Enfermedad partialUpdatedEnfermedad = new Enfermedad();
        partialUpdatedEnfermedad.setId(enfermedad.getId());

        partialUpdatedEnfermedad.tipo(UPDATED_TIPO).codigoCIE(UPDATED_CODIGO_CIE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEnfermedad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEnfermedad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Enfermedad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnfermedadUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnfermedad, enfermedad),
            getPersistedEnfermedad(enfermedad)
        );
    }

    @Test
    void fullUpdateEnfermedadWithPatch() throws Exception {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.save(enfermedad).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermedad using partial update
        Enfermedad partialUpdatedEnfermedad = new Enfermedad();
        partialUpdatedEnfermedad.setId(enfermedad.getId());

        partialUpdatedEnfermedad.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).codigoCIE(UPDATED_CODIGO_CIE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEnfermedad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEnfermedad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Enfermedad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnfermedadUpdatableFieldsEquals(partialUpdatedEnfermedad, getPersistedEnfermedad(partialUpdatedEnfermedad));
    }

    @Test
    void patchNonExistingEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, enfermedadDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(enfermedadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEnfermedad() {
        // Initialize the database
        insertedEnfermedad = enfermedadRepository.save(enfermedad).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enfermedad
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, enfermedad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enfermedadRepository.count().block();
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

    protected Enfermedad getPersistedEnfermedad(Enfermedad enfermedad) {
        return enfermedadRepository.findById(enfermedad.getId()).block();
    }

    protected void assertPersistedEnfermedadToMatchAllProperties(Enfermedad expectedEnfermedad) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEnfermedadAllPropertiesEquals(expectedEnfermedad, getPersistedEnfermedad(expectedEnfermedad));
        assertEnfermedadUpdatableFieldsEquals(expectedEnfermedad, getPersistedEnfermedad(expectedEnfermedad));
    }

    protected void assertPersistedEnfermedadToMatchUpdatableProperties(Enfermedad expectedEnfermedad) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEnfermedadAllUpdatablePropertiesEquals(expectedEnfermedad, getPersistedEnfermedad(expectedEnfermedad));
        assertEnfermedadUpdatableFieldsEquals(expectedEnfermedad, getPersistedEnfermedad(expectedEnfermedad));
    }
}
