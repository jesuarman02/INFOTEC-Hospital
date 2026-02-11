package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.HistorialMedicoAsserts.*;
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
import mx.infotec.pacientesms.domain.HistorialMedico;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.repository.HistorialMedicoRepository;
import mx.infotec.pacientesms.service.dto.HistorialMedicoDTO;
import mx.infotec.pacientesms.service.mapper.HistorialMedicoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link HistorialMedicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HistorialMedicoResourceIT {

    private static final String DEFAULT_ANTECEDENTES_QUIRURGICOS = "AAAAAAAAAA";
    private static final String UPDATED_ANTECEDENTES_QUIRURGICOS = "BBBBBBBBBB";

    private static final String DEFAULT_ESQUEMA_VACUNACION = "AAAAAAAAAA";
    private static final String UPDATED_ESQUEMA_VACUNACION = "BBBBBBBBBB";

    private static final String DEFAULT_HABITOS = "AAAAAAAAAA";
    private static final String UPDATED_HABITOS = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES_GENERALES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES_GENERALES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historial-medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistorialMedicoRepository historialMedicoRepository;

    @Autowired
    private HistorialMedicoMapper historialMedicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HistorialMedico historialMedico;

    private HistorialMedico insertedHistorialMedico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistorialMedico createEntity() {
        return new HistorialMedico()
            .antecedentesQuirurgicos(DEFAULT_ANTECEDENTES_QUIRURGICOS)
            .esquemaVacunacion(DEFAULT_ESQUEMA_VACUNACION)
            .habitos(DEFAULT_HABITOS)
            .observacionesGenerales(DEFAULT_OBSERVACIONES_GENERALES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistorialMedico createUpdatedEntity() {
        return new HistorialMedico()
            .antecedentesQuirurgicos(UPDATED_ANTECEDENTES_QUIRURGICOS)
            .esquemaVacunacion(UPDATED_ESQUEMA_VACUNACION)
            .habitos(UPDATED_HABITOS)
            .observacionesGenerales(UPDATED_OBSERVACIONES_GENERALES);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HistorialMedico.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        historialMedico = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHistorialMedico != null) {
            historialMedicoRepository.delete(insertedHistorialMedico).block();
            insertedHistorialMedico = null;
        }
        deleteEntities(em);
    }

    @Test
    void createHistorialMedico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistorialMedico
        HistorialMedicoDTO historialMedicoDTO = historialMedicoMapper.toDto(historialMedico);
        var returnedHistorialMedicoDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historialMedicoDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(HistorialMedicoDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the HistorialMedico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistorialMedico = historialMedicoMapper.toEntity(returnedHistorialMedicoDTO);
        assertHistorialMedicoUpdatableFieldsEquals(returnedHistorialMedico, getPersistedHistorialMedico(returnedHistorialMedico));

        insertedHistorialMedico = returnedHistorialMedico;
    }

    @Test
    void createHistorialMedicoWithExistingId() throws Exception {
        // Create the HistorialMedico with an existing ID
        historialMedico.setId(1L);
        HistorialMedicoDTO historialMedicoDTO = historialMedicoMapper.toDto(historialMedico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historialMedicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistorialMedico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllHistorialMedicosAsStream() {
        // Initialize the database
        historialMedicoRepository.save(historialMedico).block();

        List<HistorialMedico> historialMedicoList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(HistorialMedicoDTO.class)
            .getResponseBody()
            .map(historialMedicoMapper::toEntity)
            .filter(historialMedico::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(historialMedicoList).isNotNull();
        assertThat(historialMedicoList).hasSize(1);
        HistorialMedico testHistorialMedico = historialMedicoList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertHistorialMedicoAllPropertiesEquals(historialMedico, testHistorialMedico);
        assertHistorialMedicoUpdatableFieldsEquals(historialMedico, testHistorialMedico);
    }

    @Test
    void getAllHistorialMedicos() {
        // Initialize the database
        insertedHistorialMedico = historialMedicoRepository.save(historialMedico).block();

        // Get all the historialMedicoList
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
            .value(hasItem(historialMedico.getId().intValue()))
            .jsonPath("$.[*].antecedentesQuirurgicos")
            .value(hasItem(DEFAULT_ANTECEDENTES_QUIRURGICOS))
            .jsonPath("$.[*].esquemaVacunacion")
            .value(hasItem(DEFAULT_ESQUEMA_VACUNACION))
            .jsonPath("$.[*].habitos")
            .value(hasItem(DEFAULT_HABITOS))
            .jsonPath("$.[*].observacionesGenerales")
            .value(hasItem(DEFAULT_OBSERVACIONES_GENERALES.toString()));
    }

    @Test
    void getHistorialMedico() {
        // Initialize the database
        insertedHistorialMedico = historialMedicoRepository.save(historialMedico).block();

        // Get the historialMedico
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, historialMedico.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(historialMedico.getId().intValue()))
            .jsonPath("$.antecedentesQuirurgicos")
            .value(is(DEFAULT_ANTECEDENTES_QUIRURGICOS))
            .jsonPath("$.esquemaVacunacion")
            .value(is(DEFAULT_ESQUEMA_VACUNACION))
            .jsonPath("$.habitos")
            .value(is(DEFAULT_HABITOS))
            .jsonPath("$.observacionesGenerales")
            .value(is(DEFAULT_OBSERVACIONES_GENERALES.toString()));
    }

    @Test
    void getNonExistingHistorialMedico() {
        // Get the historialMedico
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHistorialMedico() throws Exception {
        // Initialize the database
        insertedHistorialMedico = historialMedicoRepository.save(historialMedico).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historialMedico
        HistorialMedico updatedHistorialMedico = historialMedicoRepository.findById(historialMedico.getId()).block();
        updatedHistorialMedico
            .antecedentesQuirurgicos(UPDATED_ANTECEDENTES_QUIRURGICOS)
            .esquemaVacunacion(UPDATED_ESQUEMA_VACUNACION)
            .habitos(UPDATED_HABITOS)
            .observacionesGenerales(UPDATED_OBSERVACIONES_GENERALES);
        HistorialMedicoDTO historialMedicoDTO = historialMedicoMapper.toDto(updatedHistorialMedico);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, historialMedicoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historialMedicoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HistorialMedico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistorialMedicoToMatchAllProperties(updatedHistorialMedico);
    }

    @Test
    void putNonExistingHistorialMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historialMedico.setId(longCount.incrementAndGet());

        // Create the HistorialMedico
        HistorialMedicoDTO historialMedicoDTO = historialMedicoMapper.toDto(historialMedico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, historialMedicoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historialMedicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistorialMedico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHistorialMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historialMedico.setId(longCount.incrementAndGet());

        // Create the HistorialMedico
        HistorialMedicoDTO historialMedicoDTO = historialMedicoMapper.toDto(historialMedico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historialMedicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistorialMedico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHistorialMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historialMedico.setId(longCount.incrementAndGet());

        // Create the HistorialMedico
        HistorialMedicoDTO historialMedicoDTO = historialMedicoMapper.toDto(historialMedico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historialMedicoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HistorialMedico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHistorialMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedHistorialMedico = historialMedicoRepository.save(historialMedico).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historialMedico using partial update
        HistorialMedico partialUpdatedHistorialMedico = new HistorialMedico();
        partialUpdatedHistorialMedico.setId(historialMedico.getId());

        partialUpdatedHistorialMedico.esquemaVacunacion(UPDATED_ESQUEMA_VACUNACION).habitos(UPDATED_HABITOS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistorialMedico.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedHistorialMedico))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HistorialMedico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistorialMedicoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistorialMedico, historialMedico),
            getPersistedHistorialMedico(historialMedico)
        );
    }

    @Test
    void fullUpdateHistorialMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedHistorialMedico = historialMedicoRepository.save(historialMedico).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historialMedico using partial update
        HistorialMedico partialUpdatedHistorialMedico = new HistorialMedico();
        partialUpdatedHistorialMedico.setId(historialMedico.getId());

        partialUpdatedHistorialMedico
            .antecedentesQuirurgicos(UPDATED_ANTECEDENTES_QUIRURGICOS)
            .esquemaVacunacion(UPDATED_ESQUEMA_VACUNACION)
            .habitos(UPDATED_HABITOS)
            .observacionesGenerales(UPDATED_OBSERVACIONES_GENERALES);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistorialMedico.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedHistorialMedico))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HistorialMedico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistorialMedicoUpdatableFieldsEquals(
            partialUpdatedHistorialMedico,
            getPersistedHistorialMedico(partialUpdatedHistorialMedico)
        );
    }

    @Test
    void patchNonExistingHistorialMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historialMedico.setId(longCount.incrementAndGet());

        // Create the HistorialMedico
        HistorialMedicoDTO historialMedicoDTO = historialMedicoMapper.toDto(historialMedico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, historialMedicoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(historialMedicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistorialMedico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHistorialMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historialMedico.setId(longCount.incrementAndGet());

        // Create the HistorialMedico
        HistorialMedicoDTO historialMedicoDTO = historialMedicoMapper.toDto(historialMedico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(historialMedicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistorialMedico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHistorialMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historialMedico.setId(longCount.incrementAndGet());

        // Create the HistorialMedico
        HistorialMedicoDTO historialMedicoDTO = historialMedicoMapper.toDto(historialMedico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(historialMedicoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HistorialMedico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHistorialMedico() {
        // Initialize the database
        insertedHistorialMedico = historialMedicoRepository.save(historialMedico).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historialMedico
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, historialMedico.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historialMedicoRepository.count().block();
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

    protected HistorialMedico getPersistedHistorialMedico(HistorialMedico historialMedico) {
        return historialMedicoRepository.findById(historialMedico.getId()).block();
    }

    protected void assertPersistedHistorialMedicoToMatchAllProperties(HistorialMedico expectedHistorialMedico) {
        // Test fails because reactive api returns an empty object instead of null
        // assertHistorialMedicoAllPropertiesEquals(expectedHistorialMedico, getPersistedHistorialMedico(expectedHistorialMedico));
        assertHistorialMedicoUpdatableFieldsEquals(expectedHistorialMedico, getPersistedHistorialMedico(expectedHistorialMedico));
    }

    protected void assertPersistedHistorialMedicoToMatchUpdatableProperties(HistorialMedico expectedHistorialMedico) {
        // Test fails because reactive api returns an empty object instead of null
        // assertHistorialMedicoAllUpdatablePropertiesEquals(expectedHistorialMedico, getPersistedHistorialMedico(expectedHistorialMedico));
        assertHistorialMedicoUpdatableFieldsEquals(expectedHistorialMedico, getPersistedHistorialMedico(expectedHistorialMedico));
    }
}
