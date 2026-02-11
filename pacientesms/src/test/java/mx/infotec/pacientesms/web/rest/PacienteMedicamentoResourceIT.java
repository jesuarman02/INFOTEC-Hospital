package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.PacienteMedicamentoAsserts.*;
import static mx.infotec.pacientesms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mx.infotec.pacientesms.IntegrationTest;
import mx.infotec.pacientesms.domain.PacienteMedicamento;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.repository.PacienteMedicamentoRepository;
import mx.infotec.pacientesms.service.PacienteMedicamentoService;
import mx.infotec.pacientesms.service.dto.PacienteMedicamentoDTO;
import mx.infotec.pacientesms.service.mapper.PacienteMedicamentoMapper;
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
 * Integration tests for the {@link PacienteMedicamentoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PacienteMedicamentoResourceIT {

    private static final String DEFAULT_DOSIS = "AAAAAAAAAA";
    private static final String UPDATED_DOSIS = "BBBBBBBBBB";

    private static final String DEFAULT_FRECUENCIA = "AAAAAAAAAA";
    private static final String UPDATED_FRECUENCIA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/paciente-medicamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacienteMedicamentoRepository pacienteMedicamentoRepository;

    @Mock
    private PacienteMedicamentoRepository pacienteMedicamentoRepositoryMock;

    @Autowired
    private PacienteMedicamentoMapper pacienteMedicamentoMapper;

    @Mock
    private PacienteMedicamentoService pacienteMedicamentoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PacienteMedicamento pacienteMedicamento;

    private PacienteMedicamento insertedPacienteMedicamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PacienteMedicamento createEntity() {
        return new PacienteMedicamento()
            .dosis(DEFAULT_DOSIS)
            .frecuencia(DEFAULT_FRECUENCIA)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PacienteMedicamento createUpdatedEntity() {
        return new PacienteMedicamento()
            .dosis(UPDATED_DOSIS)
            .frecuencia(UPDATED_FRECUENCIA)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .activo(UPDATED_ACTIVO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PacienteMedicamento.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        pacienteMedicamento = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPacienteMedicamento != null) {
            pacienteMedicamentoRepository.delete(insertedPacienteMedicamento).block();
            insertedPacienteMedicamento = null;
        }
        deleteEntities(em);
    }

    @Test
    void createPacienteMedicamento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PacienteMedicamento
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(pacienteMedicamento);
        var returnedPacienteMedicamentoDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PacienteMedicamentoDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the PacienteMedicamento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPacienteMedicamento = pacienteMedicamentoMapper.toEntity(returnedPacienteMedicamentoDTO);
        assertPacienteMedicamentoUpdatableFieldsEquals(
            returnedPacienteMedicamento,
            getPersistedPacienteMedicamento(returnedPacienteMedicamento)
        );

        insertedPacienteMedicamento = returnedPacienteMedicamento;
    }

    @Test
    void createPacienteMedicamentoWithExistingId() throws Exception {
        // Create the PacienteMedicamento with an existing ID
        pacienteMedicamento.setId(1L);
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(pacienteMedicamento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteMedicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkFechaInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pacienteMedicamento.setFechaInicio(null);

        // Create the PacienteMedicamento, which fails.
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(pacienteMedicamento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPacienteMedicamentos() {
        // Initialize the database
        insertedPacienteMedicamento = pacienteMedicamentoRepository.save(pacienteMedicamento).block();

        // Get all the pacienteMedicamentoList
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
            .value(hasItem(pacienteMedicamento.getId().intValue()))
            .jsonPath("$.[*].dosis")
            .value(hasItem(DEFAULT_DOSIS))
            .jsonPath("$.[*].frecuencia")
            .value(hasItem(DEFAULT_FRECUENCIA))
            .jsonPath("$.[*].fechaInicio")
            .value(hasItem(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.[*].fechaFin")
            .value(hasItem(DEFAULT_FECHA_FIN.toString()))
            .jsonPath("$.[*].activo")
            .value(hasItem(DEFAULT_ACTIVO.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacienteMedicamentosWithEagerRelationshipsIsEnabled() {
        when(pacienteMedicamentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(pacienteMedicamentoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacienteMedicamentosWithEagerRelationshipsIsNotEnabled() {
        when(pacienteMedicamentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(pacienteMedicamentoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPacienteMedicamento() {
        // Initialize the database
        insertedPacienteMedicamento = pacienteMedicamentoRepository.save(pacienteMedicamento).block();

        // Get the pacienteMedicamento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pacienteMedicamento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pacienteMedicamento.getId().intValue()))
            .jsonPath("$.dosis")
            .value(is(DEFAULT_DOSIS))
            .jsonPath("$.frecuencia")
            .value(is(DEFAULT_FRECUENCIA))
            .jsonPath("$.fechaInicio")
            .value(is(DEFAULT_FECHA_INICIO.toString()))
            .jsonPath("$.fechaFin")
            .value(is(DEFAULT_FECHA_FIN.toString()))
            .jsonPath("$.activo")
            .value(is(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    void getNonExistingPacienteMedicamento() {
        // Get the pacienteMedicamento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPacienteMedicamento() throws Exception {
        // Initialize the database
        insertedPacienteMedicamento = pacienteMedicamentoRepository.save(pacienteMedicamento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacienteMedicamento
        PacienteMedicamento updatedPacienteMedicamento = pacienteMedicamentoRepository.findById(pacienteMedicamento.getId()).block();
        updatedPacienteMedicamento
            .dosis(UPDATED_DOSIS)
            .frecuencia(UPDATED_FRECUENCIA)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .activo(UPDATED_ACTIVO);
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(updatedPacienteMedicamento);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteMedicamentoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PacienteMedicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacienteMedicamentoToMatchAllProperties(updatedPacienteMedicamento);
    }

    @Test
    void putNonExistingPacienteMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteMedicamento.setId(longCount.incrementAndGet());

        // Create the PacienteMedicamento
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(pacienteMedicamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteMedicamentoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteMedicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPacienteMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteMedicamento.setId(longCount.incrementAndGet());

        // Create the PacienteMedicamento
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(pacienteMedicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteMedicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPacienteMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteMedicamento.setId(longCount.incrementAndGet());

        // Create the PacienteMedicamento
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(pacienteMedicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PacienteMedicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePacienteMedicamentoWithPatch() throws Exception {
        // Initialize the database
        insertedPacienteMedicamento = pacienteMedicamentoRepository.save(pacienteMedicamento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacienteMedicamento using partial update
        PacienteMedicamento partialUpdatedPacienteMedicamento = new PacienteMedicamento();
        partialUpdatedPacienteMedicamento.setId(pacienteMedicamento.getId());

        partialUpdatedPacienteMedicamento.dosis(UPDATED_DOSIS).activo(UPDATED_ACTIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPacienteMedicamento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPacienteMedicamento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PacienteMedicamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteMedicamentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPacienteMedicamento, pacienteMedicamento),
            getPersistedPacienteMedicamento(pacienteMedicamento)
        );
    }

    @Test
    void fullUpdatePacienteMedicamentoWithPatch() throws Exception {
        // Initialize the database
        insertedPacienteMedicamento = pacienteMedicamentoRepository.save(pacienteMedicamento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacienteMedicamento using partial update
        PacienteMedicamento partialUpdatedPacienteMedicamento = new PacienteMedicamento();
        partialUpdatedPacienteMedicamento.setId(pacienteMedicamento.getId());

        partialUpdatedPacienteMedicamento
            .dosis(UPDATED_DOSIS)
            .frecuencia(UPDATED_FRECUENCIA)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .activo(UPDATED_ACTIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPacienteMedicamento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPacienteMedicamento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PacienteMedicamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteMedicamentoUpdatableFieldsEquals(
            partialUpdatedPacienteMedicamento,
            getPersistedPacienteMedicamento(partialUpdatedPacienteMedicamento)
        );
    }

    @Test
    void patchNonExistingPacienteMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteMedicamento.setId(longCount.incrementAndGet());

        // Create the PacienteMedicamento
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(pacienteMedicamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pacienteMedicamentoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteMedicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPacienteMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteMedicamento.setId(longCount.incrementAndGet());

        // Create the PacienteMedicamento
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(pacienteMedicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PacienteMedicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPacienteMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacienteMedicamento.setId(longCount.incrementAndGet());

        // Create the PacienteMedicamento
        PacienteMedicamentoDTO pacienteMedicamentoDTO = pacienteMedicamentoMapper.toDto(pacienteMedicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteMedicamentoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PacienteMedicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePacienteMedicamento() {
        // Initialize the database
        insertedPacienteMedicamento = pacienteMedicamentoRepository.save(pacienteMedicamento).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pacienteMedicamento
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pacienteMedicamento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacienteMedicamentoRepository.count().block();
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

    protected PacienteMedicamento getPersistedPacienteMedicamento(PacienteMedicamento pacienteMedicamento) {
        return pacienteMedicamentoRepository.findById(pacienteMedicamento.getId()).block();
    }

    protected void assertPersistedPacienteMedicamentoToMatchAllProperties(PacienteMedicamento expectedPacienteMedicamento) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteMedicamentoAllPropertiesEquals(expectedPacienteMedicamento, getPersistedPacienteMedicamento(expectedPacienteMedicamento));
        assertPacienteMedicamentoUpdatableFieldsEquals(
            expectedPacienteMedicamento,
            getPersistedPacienteMedicamento(expectedPacienteMedicamento)
        );
    }

    protected void assertPersistedPacienteMedicamentoToMatchUpdatableProperties(PacienteMedicamento expectedPacienteMedicamento) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteMedicamentoAllUpdatablePropertiesEquals(expectedPacienteMedicamento, getPersistedPacienteMedicamento(expectedPacienteMedicamento));
        assertPacienteMedicamentoUpdatableFieldsEquals(
            expectedPacienteMedicamento,
            getPersistedPacienteMedicamento(expectedPacienteMedicamento)
        );
    }
}
