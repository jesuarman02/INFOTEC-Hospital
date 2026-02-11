package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.DireccionAsserts.*;
import static mx.infotec.pacientesms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mx.infotec.pacientesms.IntegrationTest;
import mx.infotec.pacientesms.domain.Direccion;
import mx.infotec.pacientesms.repository.DireccionRepository;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.service.DireccionService;
import mx.infotec.pacientesms.service.dto.DireccionDTO;
import mx.infotec.pacientesms.service.mapper.DireccionMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link DireccionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DireccionResourceIT {

    private static final String DEFAULT_NOMBRE_VIALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_VIALIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_EXTERIOR = "AAAAAAAAAA";
    private static final String UPDATED_NUM_EXTERIOR = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_INTERIOR = "AAAAAAAAAA";
    private static final String UPDATED_NUM_INTERIOR = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/direccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DireccionRepository direccionRepository;

    @Mock
    private DireccionRepository direccionRepositoryMock;

    @Autowired
    private DireccionMapper direccionMapper;

    @Mock
    private DireccionService direccionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Direccion direccion;

    private Direccion insertedDireccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createEntity() {
        return new Direccion()
            .nombreVialidad(DEFAULT_NOMBRE_VIALIDAD)
            .numExterior(DEFAULT_NUM_EXTERIOR)
            .numInterior(DEFAULT_NUM_INTERIOR)
            .telefono(DEFAULT_TELEFONO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createUpdatedEntity() {
        return new Direccion()
            .nombreVialidad(UPDATED_NOMBRE_VIALIDAD)
            .numExterior(UPDATED_NUM_EXTERIOR)
            .numInterior(UPDATED_NUM_INTERIOR)
            .telefono(UPDATED_TELEFONO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Direccion.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        direccion = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDireccion != null) {
            direccionRepository.delete(insertedDireccion).block();
            insertedDireccion = null;
        }
        deleteEntities(em);
    }

    @Test
    void createDireccion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);
        var returnedDireccionDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(direccionDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(DireccionDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Direccion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDireccion = direccionMapper.toEntity(returnedDireccionDTO);
        assertDireccionUpdatableFieldsEquals(returnedDireccion, getPersistedDireccion(returnedDireccion));

        insertedDireccion = returnedDireccion;
    }

    @Test
    void createDireccionWithExistingId() throws Exception {
        // Create the Direccion with an existing ID
        direccion.setId(1L);
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(direccionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDireccionsAsStream() {
        // Initialize the database
        direccionRepository.save(direccion).block();

        List<Direccion> direccionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DireccionDTO.class)
            .getResponseBody()
            .map(direccionMapper::toEntity)
            .filter(direccion::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(direccionList).isNotNull();
        assertThat(direccionList).hasSize(1);
        Direccion testDireccion = direccionList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertDireccionAllPropertiesEquals(direccion, testDireccion);
        assertDireccionUpdatableFieldsEquals(direccion, testDireccion);
    }

    @Test
    void getAllDireccions() {
        // Initialize the database
        insertedDireccion = direccionRepository.save(direccion).block();

        // Get all the direccionList
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
            .value(hasItem(direccion.getId().intValue()))
            .jsonPath("$.[*].nombreVialidad")
            .value(hasItem(DEFAULT_NOMBRE_VIALIDAD))
            .jsonPath("$.[*].numExterior")
            .value(hasItem(DEFAULT_NUM_EXTERIOR))
            .jsonPath("$.[*].numInterior")
            .value(hasItem(DEFAULT_NUM_INTERIOR))
            .jsonPath("$.[*].telefono")
            .value(hasItem(DEFAULT_TELEFONO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDireccionsWithEagerRelationshipsIsEnabled() {
        when(direccionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(direccionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDireccionsWithEagerRelationshipsIsNotEnabled() {
        when(direccionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(direccionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getDireccion() {
        // Initialize the database
        insertedDireccion = direccionRepository.save(direccion).block();

        // Get the direccion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, direccion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(direccion.getId().intValue()))
            .jsonPath("$.nombreVialidad")
            .value(is(DEFAULT_NOMBRE_VIALIDAD))
            .jsonPath("$.numExterior")
            .value(is(DEFAULT_NUM_EXTERIOR))
            .jsonPath("$.numInterior")
            .value(is(DEFAULT_NUM_INTERIOR))
            .jsonPath("$.telefono")
            .value(is(DEFAULT_TELEFONO));
    }

    @Test
    void getNonExistingDireccion() {
        // Get the direccion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.save(direccion).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion
        Direccion updatedDireccion = direccionRepository.findById(direccion.getId()).block();
        updatedDireccion
            .nombreVialidad(UPDATED_NOMBRE_VIALIDAD)
            .numExterior(UPDATED_NUM_EXTERIOR)
            .numInterior(UPDATED_NUM_INTERIOR)
            .telefono(UPDATED_TELEFONO);
        DireccionDTO direccionDTO = direccionMapper.toDto(updatedDireccion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, direccionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(direccionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDireccionToMatchAllProperties(updatedDireccion);
    }

    @Test
    void putNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, direccionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(direccionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(direccionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(direccionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.save(direccion).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion.nombreVialidad(UPDATED_NOMBRE_VIALIDAD).numExterior(UPDATED_NUM_EXTERIOR).numInterior(UPDATED_NUM_INTERIOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDireccion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDireccion, direccion),
            getPersistedDireccion(direccion)
        );
    }

    @Test
    void fullUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.save(direccion).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion
            .nombreVialidad(UPDATED_NOMBRE_VIALIDAD)
            .numExterior(UPDATED_NUM_EXTERIOR)
            .numInterior(UPDATED_NUM_INTERIOR)
            .telefono(UPDATED_TELEFONO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDireccion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(partialUpdatedDireccion, getPersistedDireccion(partialUpdatedDireccion));
    }

    @Test
    void patchNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, direccionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(direccionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(direccionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(direccionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDireccion() {
        // Initialize the database
        insertedDireccion = direccionRepository.save(direccion).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the direccion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, direccion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return direccionRepository.count().block();
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

    protected Direccion getPersistedDireccion(Direccion direccion) {
        return direccionRepository.findById(direccion.getId()).block();
    }

    protected void assertPersistedDireccionToMatchAllProperties(Direccion expectedDireccion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDireccionAllPropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
        assertDireccionUpdatableFieldsEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }

    protected void assertPersistedDireccionToMatchUpdatableProperties(Direccion expectedDireccion) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDireccionAllUpdatablePropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
        assertDireccionUpdatableFieldsEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }
}
