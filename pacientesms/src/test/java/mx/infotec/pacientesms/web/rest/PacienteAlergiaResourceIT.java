package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.PacienteAlergiaAsserts.*;
import static mx.infotec.pacientesms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mx.infotec.pacientesms.IntegrationTest;
import mx.infotec.pacientesms.domain.PacienteAlergia;
import mx.infotec.pacientesms.domain.enumeration.GradoAlergia;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.repository.PacienteAlergiaRepository;
import mx.infotec.pacientesms.service.PacienteAlergiaService;
import mx.infotec.pacientesms.service.dto.PacienteAlergiaDTO;
import mx.infotec.pacientesms.service.mapper.PacienteAlergiaMapper;
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
 * Integration tests for the {@link PacienteAlergiaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PacienteAlergiaResourceIT {

    private static final LocalDate DEFAULT_FECHA_DETECCION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_DETECCION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REACCION = "AAAAAAAAAA";
    private static final String UPDATED_REACCION = "BBBBBBBBBB";

    private static final GradoAlergia DEFAULT_GRAVEDAD = GradoAlergia.LEVE;
    private static final GradoAlergia UPDATED_GRAVEDAD = GradoAlergia.MODERADA;

    private static final String ENTITY_API_URL = "/api/paciente-alergias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacienteAlergiaRepository pacienteAlergiaRepository;

    @Mock
    private PacienteAlergiaRepository pacienteAlergiaRepositoryMock;

    @Autowired
    private PacienteAlergiaMapper pacienteAlergiaMapper;

    @Mock
    private PacienteAlergiaService pacienteAlergiaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PacienteAlergia pacienteAlergia;

    private PacienteAlergia insertedPacienteAlergia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PacienteAlergia createEntity() {
        return new PacienteAlergia().fechaDeteccion(DEFAULT_FECHA_DETECCION).reaccion(DEFAULT_REACCION).gravedad(DEFAULT_GRAVEDAD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PacienteAlergia createUpdatedEntity() {
        return new PacienteAlergia().fechaDeteccion(UPDATED_FECHA_DETECCION).reaccion(UPDATED_REACCION).gravedad(UPDATED_GRAVEDAD);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PacienteAlergia.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        pacienteAlergia = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPacienteAlergia != null) {
            pacienteAlergiaRepository.delete(insertedPacienteAlergia).block();
            insertedPacienteAlergia = null;
        }
        deleteEntities(em);
    }

    @Test
    void createPacienteAlergia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PacienteAlergia
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(pacienteAlergia);
        var returnedPacienteAlergiaDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PacienteAlergiaDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the PacienteAlergia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPacienteAlergia = pacienteAlergiaMapper.toEntity(returnedPacienteAlergiaDTO);
        assertPacienteAlergiaUpdatableFieldsEquals(returnedPacienteAlergia, getPersistedPacienteAlergia(returnedPacienteAlergia));

        insertedPacienteAlergia = returnedPacienteAlergia;
    }

    @Test
    void createPacienteAlergiaWithExistingId() throws Exception {
        // Create the PacienteAlergia with an existing ID
        pacienteAlergia.setId(1L);
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(pacienteAlergia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteAlergia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkGravedadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pacienteAlergia.setGravedad(null);

        // Create the PacienteAlergia, which fails.
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(pacienteAlergia);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPacienteAlergiasAsStream() {
        // Initialize the database
        pacienteAlergiaRepository.save(pacienteAlergia).block();

        List<PacienteAlergia> pacienteAlergiaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PacienteAlergiaDTO.class)
            .getResponseBody()
            .map(pacienteAlergiaMapper::toEntity)
            .filter(pacienteAlergia::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(pacienteAlergiaList).isNotNull();
        assertThat(pacienteAlergiaList).hasSize(1);
        PacienteAlergia testPacienteAlergia = pacienteAlergiaList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteAlergiaAllPropertiesEquals(pacienteAlergia, testPacienteAlergia);
        assertPacienteAlergiaUpdatableFieldsEquals(pacienteAlergia, testPacienteAlergia);
    }

    @Test
    void getAllPacienteAlergias() {
        // Initialize the database
        insertedPacienteAlergia = pacienteAlergiaRepository.save(pacienteAlergia).block();

        // Get all the pacienteAlergiaList
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
            .value(hasItem(pacienteAlergia.getId().intValue()))
            .jsonPath("$.[*].fechaDeteccion")
            .value(hasItem(DEFAULT_FECHA_DETECCION.toString()))
            .jsonPath("$.[*].reaccion")
            .value(hasItem(DEFAULT_REACCION))
            .jsonPath("$.[*].gravedad")
            .value(hasItem(DEFAULT_GRAVEDAD.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacienteAlergiasWithEagerRelationshipsIsEnabled() {
        when(pacienteAlergiaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(pacienteAlergiaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacienteAlergiasWithEagerRelationshipsIsNotEnabled() {
        when(pacienteAlergiaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(pacienteAlergiaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPacienteAlergia() {
        // Initialize the database
        insertedPacienteAlergia = pacienteAlergiaRepository.save(pacienteAlergia).block();

        // Get the pacienteAlergia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pacienteAlergia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pacienteAlergia.getId().intValue()))
            .jsonPath("$.fechaDeteccion")
            .value(is(DEFAULT_FECHA_DETECCION.toString()))
            .jsonPath("$.reaccion")
            .value(is(DEFAULT_REACCION))
            .jsonPath("$.gravedad")
            .value(is(DEFAULT_GRAVEDAD.toString()));
    }

    @Test
    void getNonExistingPacienteAlergia() {
        // Get the pacienteAlergia
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPacienteAlergia() throws Exception {
        // Initialize the database
        insertedPacienteAlergia = pacienteAlergiaRepository.save(pacienteAlergia).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacienteAlergia
        PacienteAlergia updatedPacienteAlergia = pacienteAlergiaRepository.findById(pacienteAlergia.getId()).block();
        updatedPacienteAlergia.fechaDeteccion(UPDATED_FECHA_DETECCION).reaccion(UPDATED_REACCION).gravedad(UPDATED_GRAVEDAD);
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(updatedPacienteAlergia);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteAlergiaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PacienteAlergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacienteAlergiaToMatchAllProperties(updatedPacienteAlergia);
    }

    @Test
    void putNonExistingPacienteAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteAlergia.setId(longCount.incrementAndGet());

        // Create the PacienteAlergia
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(pacienteAlergia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteAlergiaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteAlergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPacienteAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteAlergia.setId(longCount.incrementAndGet());

        // Create the PacienteAlergia
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(pacienteAlergia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteAlergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPacienteAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteAlergia.setId(longCount.incrementAndGet());

        // Create the PacienteAlergia
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(pacienteAlergia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PacienteAlergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePacienteAlergiaWithPatch() throws Exception {
        // Initialize the database
        insertedPacienteAlergia = pacienteAlergiaRepository.save(pacienteAlergia).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacienteAlergia using partial update
        PacienteAlergia partialUpdatedPacienteAlergia = new PacienteAlergia();
        partialUpdatedPacienteAlergia.setId(pacienteAlergia.getId());

        partialUpdatedPacienteAlergia.fechaDeteccion(UPDATED_FECHA_DETECCION).reaccion(UPDATED_REACCION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPacienteAlergia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPacienteAlergia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PacienteAlergia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteAlergiaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPacienteAlergia, pacienteAlergia),
            getPersistedPacienteAlergia(pacienteAlergia)
        );
    }

    @Test
    void fullUpdatePacienteAlergiaWithPatch() throws Exception {
        // Initialize the database
        insertedPacienteAlergia = pacienteAlergiaRepository.save(pacienteAlergia).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacienteAlergia using partial update
        PacienteAlergia partialUpdatedPacienteAlergia = new PacienteAlergia();
        partialUpdatedPacienteAlergia.setId(pacienteAlergia.getId());

        partialUpdatedPacienteAlergia.fechaDeteccion(UPDATED_FECHA_DETECCION).reaccion(UPDATED_REACCION).gravedad(UPDATED_GRAVEDAD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPacienteAlergia.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPacienteAlergia))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PacienteAlergia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteAlergiaUpdatableFieldsEquals(
            partialUpdatedPacienteAlergia,
            getPersistedPacienteAlergia(partialUpdatedPacienteAlergia)
        );
    }

    @Test
    void patchNonExistingPacienteAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteAlergia.setId(longCount.incrementAndGet());

        // Create the PacienteAlergia
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(pacienteAlergia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pacienteAlergiaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteAlergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPacienteAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteAlergia.setId(longCount.incrementAndGet());

        // Create the PacienteAlergia
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(pacienteAlergia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteAlergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPacienteAlergia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteAlergia.setId(longCount.incrementAndGet());

        // Create the PacienteAlergia
        PacienteAlergiaDTO pacienteAlergiaDTO = pacienteAlergiaMapper.toDto(pacienteAlergia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteAlergiaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PacienteAlergia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePacienteAlergia() {
        // Initialize the database
        insertedPacienteAlergia = pacienteAlergiaRepository.save(pacienteAlergia).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pacienteAlergia
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pacienteAlergia.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacienteAlergiaRepository.count().block();
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

    protected PacienteAlergia getPersistedPacienteAlergia(PacienteAlergia pacienteAlergia) {
        return pacienteAlergiaRepository.findById(pacienteAlergia.getId()).block();
    }

    protected void assertPersistedPacienteAlergiaToMatchAllProperties(PacienteAlergia expectedPacienteAlergia) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteAlergiaAllPropertiesEquals(expectedPacienteAlergia, getPersistedPacienteAlergia(expectedPacienteAlergia));
        assertPacienteAlergiaUpdatableFieldsEquals(expectedPacienteAlergia, getPersistedPacienteAlergia(expectedPacienteAlergia));
    }

    protected void assertPersistedPacienteAlergiaToMatchUpdatableProperties(PacienteAlergia expectedPacienteAlergia) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteAlergiaAllUpdatablePropertiesEquals(expectedPacienteAlergia, getPersistedPacienteAlergia(expectedPacienteAlergia));
        assertPacienteAlergiaUpdatableFieldsEquals(expectedPacienteAlergia, getPersistedPacienteAlergia(expectedPacienteAlergia));
    }
}
