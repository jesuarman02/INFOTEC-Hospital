package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.MedicamentoAsserts.*;
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
import mx.infotec.pacientesms.domain.Medicamento;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.repository.MedicamentoRepository;
import mx.infotec.pacientesms.service.dto.MedicamentoDTO;
import mx.infotec.pacientesms.service.mapper.MedicamentoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link MedicamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MedicamentoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENTE_ACTIVO = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENTE_ACTIVO = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENTACION = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private MedicamentoMapper medicamentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Medicamento medicamento;

    private Medicamento insertedMedicamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicamento createEntity() {
        return new Medicamento().nombre(DEFAULT_NOMBRE).ingredienteActivo(DEFAULT_INGREDIENTE_ACTIVO).presentacion(DEFAULT_PRESENTACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicamento createUpdatedEntity() {
        return new Medicamento().nombre(UPDATED_NOMBRE).ingredienteActivo(UPDATED_INGREDIENTE_ACTIVO).presentacion(UPDATED_PRESENTACION);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Medicamento.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        medicamento = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicamento != null) {
            medicamentoRepository.delete(insertedMedicamento).block();
            insertedMedicamento = null;
        }
        deleteEntities(em);
    }

    @Test
    void createMedicamento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);
        var returnedMedicamentoDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MedicamentoDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Medicamento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicamento = medicamentoMapper.toEntity(returnedMedicamentoDTO);
        assertMedicamentoUpdatableFieldsEquals(returnedMedicamento, getPersistedMedicamento(returnedMedicamento));

        insertedMedicamento = returnedMedicamento;
    }

    @Test
    void createMedicamentoWithExistingId() throws Exception {
        // Create the Medicamento with an existing ID
        medicamento.setId(1L);
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicamento.setNombre(null);

        // Create the Medicamento, which fails.
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllMedicamentosAsStream() {
        // Initialize the database
        medicamentoRepository.save(medicamento).block();

        List<Medicamento> medicamentoList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(MedicamentoDTO.class)
            .getResponseBody()
            .map(medicamentoMapper::toEntity)
            .filter(medicamento::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(medicamentoList).isNotNull();
        assertThat(medicamentoList).hasSize(1);
        Medicamento testMedicamento = medicamentoList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertMedicamentoAllPropertiesEquals(medicamento, testMedicamento);
        assertMedicamentoUpdatableFieldsEquals(medicamento, testMedicamento);
    }

    @Test
    void getAllMedicamentos() {
        // Initialize the database
        insertedMedicamento = medicamentoRepository.save(medicamento).block();

        // Get all the medicamentoList
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
            .value(hasItem(medicamento.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].ingredienteActivo")
            .value(hasItem(DEFAULT_INGREDIENTE_ACTIVO))
            .jsonPath("$.[*].presentacion")
            .value(hasItem(DEFAULT_PRESENTACION));
    }

    @Test
    void getMedicamento() {
        // Initialize the database
        insertedMedicamento = medicamentoRepository.save(medicamento).block();

        // Get the medicamento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, medicamento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(medicamento.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.ingredienteActivo")
            .value(is(DEFAULT_INGREDIENTE_ACTIVO))
            .jsonPath("$.presentacion")
            .value(is(DEFAULT_PRESENTACION));
    }

    @Test
    void getNonExistingMedicamento() {
        // Get the medicamento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMedicamento() throws Exception {
        // Initialize the database
        insertedMedicamento = medicamentoRepository.save(medicamento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicamento
        Medicamento updatedMedicamento = medicamentoRepository.findById(medicamento.getId()).block();
        updatedMedicamento.nombre(UPDATED_NOMBRE).ingredienteActivo(UPDATED_INGREDIENTE_ACTIVO).presentacion(UPDATED_PRESENTACION);
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(updatedMedicamento);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicamentoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicamentoToMatchAllProperties(updatedMedicamento);
    }

    @Test
    void putNonExistingMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicamentoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMedicamentoWithPatch() throws Exception {
        // Initialize the database
        insertedMedicamento = medicamentoRepository.save(medicamento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicamento using partial update
        Medicamento partialUpdatedMedicamento = new Medicamento();
        partialUpdatedMedicamento.setId(medicamento.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedicamento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMedicamento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Medicamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicamentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicamento, medicamento),
            getPersistedMedicamento(medicamento)
        );
    }

    @Test
    void fullUpdateMedicamentoWithPatch() throws Exception {
        // Initialize the database
        insertedMedicamento = medicamentoRepository.save(medicamento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicamento using partial update
        Medicamento partialUpdatedMedicamento = new Medicamento();
        partialUpdatedMedicamento.setId(medicamento.getId());

        partialUpdatedMedicamento.nombre(UPDATED_NOMBRE).ingredienteActivo(UPDATED_INGREDIENTE_ACTIVO).presentacion(UPDATED_PRESENTACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedicamento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMedicamento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Medicamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicamentoUpdatableFieldsEquals(partialUpdatedMedicamento, getPersistedMedicamento(partialUpdatedMedicamento));
    }

    @Test
    void patchNonExistingMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, medicamentoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicamentoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMedicamento() {
        // Initialize the database
        insertedMedicamento = medicamentoRepository.save(medicamento).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicamento
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, medicamento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicamentoRepository.count().block();
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

    protected Medicamento getPersistedMedicamento(Medicamento medicamento) {
        return medicamentoRepository.findById(medicamento.getId()).block();
    }

    protected void assertPersistedMedicamentoToMatchAllProperties(Medicamento expectedMedicamento) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMedicamentoAllPropertiesEquals(expectedMedicamento, getPersistedMedicamento(expectedMedicamento));
        assertMedicamentoUpdatableFieldsEquals(expectedMedicamento, getPersistedMedicamento(expectedMedicamento));
    }

    protected void assertPersistedMedicamentoToMatchUpdatableProperties(Medicamento expectedMedicamento) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMedicamentoAllUpdatablePropertiesEquals(expectedMedicamento, getPersistedMedicamento(expectedMedicamento));
        assertMedicamentoUpdatableFieldsEquals(expectedMedicamento, getPersistedMedicamento(expectedMedicamento));
    }
}
