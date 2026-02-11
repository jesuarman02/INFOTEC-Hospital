package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.PacienteEnfermedadAsserts.*;
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
import mx.infotec.pacientesms.domain.PacienteEnfermedad;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.repository.PacienteEnfermedadRepository;
import mx.infotec.pacientesms.service.PacienteEnfermedadService;
import mx.infotec.pacientesms.service.dto.PacienteEnfermedadDTO;
import mx.infotec.pacientesms.service.mapper.PacienteEnfermedadMapper;
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
 * Integration tests for the {@link PacienteEnfermedadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PacienteEnfermedadResourceIT {

    private static final LocalDate DEFAULT_FECHA_DIAGNOSTICO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_DIAGNOSTICO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paciente-enfermedads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacienteEnfermedadRepository pacienteEnfermedadRepository;

    @Mock
    private PacienteEnfermedadRepository pacienteEnfermedadRepositoryMock;

    @Autowired
    private PacienteEnfermedadMapper pacienteEnfermedadMapper;

    @Mock
    private PacienteEnfermedadService pacienteEnfermedadServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PacienteEnfermedad pacienteEnfermedad;

    private PacienteEnfermedad insertedPacienteEnfermedad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PacienteEnfermedad createEntity() {
        return new PacienteEnfermedad().fechaDiagnostico(DEFAULT_FECHA_DIAGNOSTICO).estado(DEFAULT_ESTADO).notas(DEFAULT_NOTAS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PacienteEnfermedad createUpdatedEntity() {
        return new PacienteEnfermedad().fechaDiagnostico(UPDATED_FECHA_DIAGNOSTICO).estado(UPDATED_ESTADO).notas(UPDATED_NOTAS);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PacienteEnfermedad.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        pacienteEnfermedad = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPacienteEnfermedad != null) {
            pacienteEnfermedadRepository.delete(insertedPacienteEnfermedad).block();
            insertedPacienteEnfermedad = null;
        }
        deleteEntities(em);
    }

    @Test
    void createPacienteEnfermedad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PacienteEnfermedad
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(pacienteEnfermedad);
        var returnedPacienteEnfermedadDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PacienteEnfermedadDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the PacienteEnfermedad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPacienteEnfermedad = pacienteEnfermedadMapper.toEntity(returnedPacienteEnfermedadDTO);
        assertPacienteEnfermedadUpdatableFieldsEquals(
            returnedPacienteEnfermedad,
            getPersistedPacienteEnfermedad(returnedPacienteEnfermedad)
        );

        insertedPacienteEnfermedad = returnedPacienteEnfermedad;
    }

    @Test
    void createPacienteEnfermedadWithExistingId() throws Exception {
        // Create the PacienteEnfermedad with an existing ID
        pacienteEnfermedad.setId(1L);
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(pacienteEnfermedad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteEnfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkFechaDiagnosticoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pacienteEnfermedad.setFechaDiagnostico(null);

        // Create the PacienteEnfermedad, which fails.
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(pacienteEnfermedad);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPacienteEnfermedadsAsStream() {
        // Initialize the database
        pacienteEnfermedadRepository.save(pacienteEnfermedad).block();

        List<PacienteEnfermedad> pacienteEnfermedadList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PacienteEnfermedadDTO.class)
            .getResponseBody()
            .map(pacienteEnfermedadMapper::toEntity)
            .filter(pacienteEnfermedad::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(pacienteEnfermedadList).isNotNull();
        assertThat(pacienteEnfermedadList).hasSize(1);
        PacienteEnfermedad testPacienteEnfermedad = pacienteEnfermedadList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteEnfermedadAllPropertiesEquals(pacienteEnfermedad, testPacienteEnfermedad);
        assertPacienteEnfermedadUpdatableFieldsEquals(pacienteEnfermedad, testPacienteEnfermedad);
    }

    @Test
    void getAllPacienteEnfermedads() {
        // Initialize the database
        insertedPacienteEnfermedad = pacienteEnfermedadRepository.save(pacienteEnfermedad).block();

        // Get all the pacienteEnfermedadList
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
            .value(hasItem(pacienteEnfermedad.getId().intValue()))
            .jsonPath("$.[*].fechaDiagnostico")
            .value(hasItem(DEFAULT_FECHA_DIAGNOSTICO.toString()))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO))
            .jsonPath("$.[*].notas")
            .value(hasItem(DEFAULT_NOTAS));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacienteEnfermedadsWithEagerRelationshipsIsEnabled() {
        when(pacienteEnfermedadServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(pacienteEnfermedadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacienteEnfermedadsWithEagerRelationshipsIsNotEnabled() {
        when(pacienteEnfermedadServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(pacienteEnfermedadRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPacienteEnfermedad() {
        // Initialize the database
        insertedPacienteEnfermedad = pacienteEnfermedadRepository.save(pacienteEnfermedad).block();

        // Get the pacienteEnfermedad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pacienteEnfermedad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pacienteEnfermedad.getId().intValue()))
            .jsonPath("$.fechaDiagnostico")
            .value(is(DEFAULT_FECHA_DIAGNOSTICO.toString()))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO))
            .jsonPath("$.notas")
            .value(is(DEFAULT_NOTAS));
    }

    @Test
    void getNonExistingPacienteEnfermedad() {
        // Get the pacienteEnfermedad
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPacienteEnfermedad() throws Exception {
        // Initialize the database
        insertedPacienteEnfermedad = pacienteEnfermedadRepository.save(pacienteEnfermedad).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacienteEnfermedad
        PacienteEnfermedad updatedPacienteEnfermedad = pacienteEnfermedadRepository.findById(pacienteEnfermedad.getId()).block();
        updatedPacienteEnfermedad.fechaDiagnostico(UPDATED_FECHA_DIAGNOSTICO).estado(UPDATED_ESTADO).notas(UPDATED_NOTAS);
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(updatedPacienteEnfermedad);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteEnfermedadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PacienteEnfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacienteEnfermedadToMatchAllProperties(updatedPacienteEnfermedad);
    }

    @Test
    void putNonExistingPacienteEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteEnfermedad.setId(longCount.incrementAndGet());

        // Create the PacienteEnfermedad
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(pacienteEnfermedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteEnfermedadDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteEnfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPacienteEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteEnfermedad.setId(longCount.incrementAndGet());

        // Create the PacienteEnfermedad
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(pacienteEnfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteEnfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPacienteEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteEnfermedad.setId(longCount.incrementAndGet());

        // Create the PacienteEnfermedad
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(pacienteEnfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PacienteEnfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePacienteEnfermedadWithPatch() throws Exception {
        // Initialize the database
        insertedPacienteEnfermedad = pacienteEnfermedadRepository.save(pacienteEnfermedad).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacienteEnfermedad using partial update
        PacienteEnfermedad partialUpdatedPacienteEnfermedad = new PacienteEnfermedad();
        partialUpdatedPacienteEnfermedad.setId(pacienteEnfermedad.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPacienteEnfermedad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPacienteEnfermedad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PacienteEnfermedad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteEnfermedadUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPacienteEnfermedad, pacienteEnfermedad),
            getPersistedPacienteEnfermedad(pacienteEnfermedad)
        );
    }

    @Test
    void fullUpdatePacienteEnfermedadWithPatch() throws Exception {
        // Initialize the database
        insertedPacienteEnfermedad = pacienteEnfermedadRepository.save(pacienteEnfermedad).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacienteEnfermedad using partial update
        PacienteEnfermedad partialUpdatedPacienteEnfermedad = new PacienteEnfermedad();
        partialUpdatedPacienteEnfermedad.setId(pacienteEnfermedad.getId());

        partialUpdatedPacienteEnfermedad.fechaDiagnostico(UPDATED_FECHA_DIAGNOSTICO).estado(UPDATED_ESTADO).notas(UPDATED_NOTAS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPacienteEnfermedad.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPacienteEnfermedad))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PacienteEnfermedad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteEnfermedadUpdatableFieldsEquals(
            partialUpdatedPacienteEnfermedad,
            getPersistedPacienteEnfermedad(partialUpdatedPacienteEnfermedad)
        );
    }

    @Test
    void patchNonExistingPacienteEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteEnfermedad.setId(longCount.incrementAndGet());

        // Create the PacienteEnfermedad
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(pacienteEnfermedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pacienteEnfermedadDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteEnfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPacienteEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteEnfermedad.setId(longCount.incrementAndGet());

        // Create the PacienteEnfermedad
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(pacienteEnfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteEnfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPacienteEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteEnfermedad.setId(longCount.incrementAndGet());

        // Create the PacienteEnfermedad
        PacienteEnfermedadDTO pacienteEnfermedadDTO = pacienteEnfermedadMapper.toDto(pacienteEnfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteEnfermedadDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PacienteEnfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePacienteEnfermedad() {
        // Initialize the database
        insertedPacienteEnfermedad = pacienteEnfermedadRepository.save(pacienteEnfermedad).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pacienteEnfermedad
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pacienteEnfermedad.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacienteEnfermedadRepository.count().block();
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

    protected PacienteEnfermedad getPersistedPacienteEnfermedad(PacienteEnfermedad pacienteEnfermedad) {
        return pacienteEnfermedadRepository.findById(pacienteEnfermedad.getId()).block();
    }

    protected void assertPersistedPacienteEnfermedadToMatchAllProperties(PacienteEnfermedad expectedPacienteEnfermedad) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteEnfermedadAllPropertiesEquals(expectedPacienteEnfermedad, getPersistedPacienteEnfermedad(expectedPacienteEnfermedad));
        assertPacienteEnfermedadUpdatableFieldsEquals(
            expectedPacienteEnfermedad,
            getPersistedPacienteEnfermedad(expectedPacienteEnfermedad)
        );
    }

    protected void assertPersistedPacienteEnfermedadToMatchUpdatableProperties(PacienteEnfermedad expectedPacienteEnfermedad) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteEnfermedadAllUpdatablePropertiesEquals(expectedPacienteEnfermedad, getPersistedPacienteEnfermedad(expectedPacienteEnfermedad));
        assertPacienteEnfermedadUpdatableFieldsEquals(
            expectedPacienteEnfermedad,
            getPersistedPacienteEnfermedad(expectedPacienteEnfermedad)
        );
    }
}
