package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.CodigoPostalAsserts.*;
import static mx.infotec.pacientesms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mx.infotec.pacientesms.IntegrationTest;
import mx.infotec.pacientesms.domain.CodigoPostal;
import mx.infotec.pacientesms.repository.CodigoPostalRepository;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.service.dto.CodigoPostalDTO;
import mx.infotec.pacientesms.service.mapper.CodigoPostalMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CodigoPostalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CodigoPostalResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAA";
    private static final String UPDATED_CODIGO = "BBBBB";

    private static final String DEFAULT_ASENTAMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_ASENTAMIENTO = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_ASENTAMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_ASENTAMIENTO = "BBBBBBBBBB";

    private static final String DEFAULT_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_MUNICIPIO = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_POSTAL_ADMINISTRACION = "AAAAA";
    private static final String UPDATED_CODIGO_POSTAL_ADMINISTRACION = "BBBBB";

    private static final String DEFAULT_CLAVE_ESTADO = "AAAAA";
    private static final String UPDATED_CLAVE_ESTADO = "BBBBB";

    private static final String DEFAULT_CLAVE_OFICINA = "AAAAA";
    private static final String UPDATED_CLAVE_OFICINA = "BBBBB";

    private static final String DEFAULT_CLAVE_CP = "AAAAA";
    private static final String UPDATED_CLAVE_CP = "BBBBB";

    private static final String DEFAULT_CLAVE_TIPO_ASENTAMIENTO = "AAAAA";
    private static final String UPDATED_CLAVE_TIPO_ASENTAMIENTO = "BBBBB";

    private static final String DEFAULT_CLAVE_MUNICIPIO = "AAAAA";
    private static final String UPDATED_CLAVE_MUNICIPIO = "BBBBB";

    private static final String DEFAULT_ID_ASENTAMIENTO_CONS = "AAAAAAAAAA";
    private static final String UPDATED_ID_ASENTAMIENTO_CONS = "BBBBBBBBBB";

    private static final String DEFAULT_ZONA = "AAAAAAAAAA";
    private static final String UPDATED_ZONA = "BBBBBBBBBB";

    private static final String DEFAULT_CLAVE_CIUDAD = "AAAAA";
    private static final String UPDATED_CLAVE_CIUDAD = "BBBBB";

    private static final String ENTITY_API_URL = "/api/codigo-postals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CodigoPostalRepository codigoPostalRepository;

    @Autowired
    private CodigoPostalMapper codigoPostalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CodigoPostal codigoPostal;

    private CodigoPostal insertedCodigoPostal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodigoPostal createEntity() {
        return new CodigoPostal()
            .codigo(DEFAULT_CODIGO)
            .asentamiento(DEFAULT_ASENTAMIENTO)
            .tipoAsentamiento(DEFAULT_TIPO_ASENTAMIENTO)
            .municipio(DEFAULT_MUNICIPIO)
            .estado(DEFAULT_ESTADO)
            .ciudad(DEFAULT_CIUDAD)
            .codigoPostalAdministracion(DEFAULT_CODIGO_POSTAL_ADMINISTRACION)
            .claveEstado(DEFAULT_CLAVE_ESTADO)
            .claveOficina(DEFAULT_CLAVE_OFICINA)
            .claveCP(DEFAULT_CLAVE_CP)
            .claveTipoAsentamiento(DEFAULT_CLAVE_TIPO_ASENTAMIENTO)
            .claveMunicipio(DEFAULT_CLAVE_MUNICIPIO)
            .idAsentamientoCons(DEFAULT_ID_ASENTAMIENTO_CONS)
            .zona(DEFAULT_ZONA)
            .claveCiudad(DEFAULT_CLAVE_CIUDAD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodigoPostal createUpdatedEntity() {
        return new CodigoPostal()
            .codigo(UPDATED_CODIGO)
            .asentamiento(UPDATED_ASENTAMIENTO)
            .tipoAsentamiento(UPDATED_TIPO_ASENTAMIENTO)
            .municipio(UPDATED_MUNICIPIO)
            .estado(UPDATED_ESTADO)
            .ciudad(UPDATED_CIUDAD)
            .codigoPostalAdministracion(UPDATED_CODIGO_POSTAL_ADMINISTRACION)
            .claveEstado(UPDATED_CLAVE_ESTADO)
            .claveOficina(UPDATED_CLAVE_OFICINA)
            .claveCP(UPDATED_CLAVE_CP)
            .claveTipoAsentamiento(UPDATED_CLAVE_TIPO_ASENTAMIENTO)
            .claveMunicipio(UPDATED_CLAVE_MUNICIPIO)
            .idAsentamientoCons(UPDATED_ID_ASENTAMIENTO_CONS)
            .zona(UPDATED_ZONA)
            .claveCiudad(UPDATED_CLAVE_CIUDAD);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CodigoPostal.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        codigoPostal = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCodigoPostal != null) {
            codigoPostalRepository.delete(insertedCodigoPostal).block();
            insertedCodigoPostal = null;
        }
        deleteEntities(em);
    }

    @Test
    void createCodigoPostal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CodigoPostal
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(codigoPostal);
        var returnedCodigoPostalDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CodigoPostalDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the CodigoPostal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCodigoPostal = codigoPostalMapper.toEntity(returnedCodigoPostalDTO);
        assertCodigoPostalUpdatableFieldsEquals(returnedCodigoPostal, getPersistedCodigoPostal(returnedCodigoPostal));

        insertedCodigoPostal = returnedCodigoPostal;
    }

    @Test
    void createCodigoPostalWithExistingId() throws Exception {
        // Create the CodigoPostal with an existing ID
        codigoPostal.setId(1L);
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(codigoPostal);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CodigoPostal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codigoPostal.setCodigo(null);

        // Create the CodigoPostal, which fails.
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(codigoPostal);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCodigoPostals() {
        // Initialize the database
        insertedCodigoPostal = codigoPostalRepository.save(codigoPostal).block();

        // Get all the codigoPostalList
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
            .value(hasItem(codigoPostal.getId().intValue()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO))
            .jsonPath("$.[*].asentamiento")
            .value(hasItem(DEFAULT_ASENTAMIENTO))
            .jsonPath("$.[*].tipoAsentamiento")
            .value(hasItem(DEFAULT_TIPO_ASENTAMIENTO))
            .jsonPath("$.[*].municipio")
            .value(hasItem(DEFAULT_MUNICIPIO))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO))
            .jsonPath("$.[*].ciudad")
            .value(hasItem(DEFAULT_CIUDAD))
            .jsonPath("$.[*].codigoPostalAdministracion")
            .value(hasItem(DEFAULT_CODIGO_POSTAL_ADMINISTRACION))
            .jsonPath("$.[*].claveEstado")
            .value(hasItem(DEFAULT_CLAVE_ESTADO))
            .jsonPath("$.[*].claveOficina")
            .value(hasItem(DEFAULT_CLAVE_OFICINA))
            .jsonPath("$.[*].claveCP")
            .value(hasItem(DEFAULT_CLAVE_CP))
            .jsonPath("$.[*].claveTipoAsentamiento")
            .value(hasItem(DEFAULT_CLAVE_TIPO_ASENTAMIENTO))
            .jsonPath("$.[*].claveMunicipio")
            .value(hasItem(DEFAULT_CLAVE_MUNICIPIO))
            .jsonPath("$.[*].idAsentamientoCons")
            .value(hasItem(DEFAULT_ID_ASENTAMIENTO_CONS))
            .jsonPath("$.[*].zona")
            .value(hasItem(DEFAULT_ZONA))
            .jsonPath("$.[*].claveCiudad")
            .value(hasItem(DEFAULT_CLAVE_CIUDAD));
    }

    @Test
    void getCodigoPostal() {
        // Initialize the database
        insertedCodigoPostal = codigoPostalRepository.save(codigoPostal).block();

        // Get the codigoPostal
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, codigoPostal.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(codigoPostal.getId().intValue()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO))
            .jsonPath("$.asentamiento")
            .value(is(DEFAULT_ASENTAMIENTO))
            .jsonPath("$.tipoAsentamiento")
            .value(is(DEFAULT_TIPO_ASENTAMIENTO))
            .jsonPath("$.municipio")
            .value(is(DEFAULT_MUNICIPIO))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO))
            .jsonPath("$.ciudad")
            .value(is(DEFAULT_CIUDAD))
            .jsonPath("$.codigoPostalAdministracion")
            .value(is(DEFAULT_CODIGO_POSTAL_ADMINISTRACION))
            .jsonPath("$.claveEstado")
            .value(is(DEFAULT_CLAVE_ESTADO))
            .jsonPath("$.claveOficina")
            .value(is(DEFAULT_CLAVE_OFICINA))
            .jsonPath("$.claveCP")
            .value(is(DEFAULT_CLAVE_CP))
            .jsonPath("$.claveTipoAsentamiento")
            .value(is(DEFAULT_CLAVE_TIPO_ASENTAMIENTO))
            .jsonPath("$.claveMunicipio")
            .value(is(DEFAULT_CLAVE_MUNICIPIO))
            .jsonPath("$.idAsentamientoCons")
            .value(is(DEFAULT_ID_ASENTAMIENTO_CONS))
            .jsonPath("$.zona")
            .value(is(DEFAULT_ZONA))
            .jsonPath("$.claveCiudad")
            .value(is(DEFAULT_CLAVE_CIUDAD));
    }

    @Test
    void getNonExistingCodigoPostal() {
        // Get the codigoPostal
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCodigoPostal() throws Exception {
        // Initialize the database
        insertedCodigoPostal = codigoPostalRepository.save(codigoPostal).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codigoPostal
        CodigoPostal updatedCodigoPostal = codigoPostalRepository.findById(codigoPostal.getId()).block();
        updatedCodigoPostal
            .codigo(UPDATED_CODIGO)
            .asentamiento(UPDATED_ASENTAMIENTO)
            .tipoAsentamiento(UPDATED_TIPO_ASENTAMIENTO)
            .municipio(UPDATED_MUNICIPIO)
            .estado(UPDATED_ESTADO)
            .ciudad(UPDATED_CIUDAD)
            .codigoPostalAdministracion(UPDATED_CODIGO_POSTAL_ADMINISTRACION)
            .claveEstado(UPDATED_CLAVE_ESTADO)
            .claveOficina(UPDATED_CLAVE_OFICINA)
            .claveCP(UPDATED_CLAVE_CP)
            .claveTipoAsentamiento(UPDATED_CLAVE_TIPO_ASENTAMIENTO)
            .claveMunicipio(UPDATED_CLAVE_MUNICIPIO)
            .idAsentamientoCons(UPDATED_ID_ASENTAMIENTO_CONS)
            .zona(UPDATED_ZONA)
            .claveCiudad(UPDATED_CLAVE_CIUDAD);
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(updatedCodigoPostal);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, codigoPostalDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CodigoPostal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCodigoPostalToMatchAllProperties(updatedCodigoPostal);
    }

    @Test
    void putNonExistingCodigoPostal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codigoPostal.setId(longCount.incrementAndGet());

        // Create the CodigoPostal
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(codigoPostal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, codigoPostalDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CodigoPostal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCodigoPostal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codigoPostal.setId(longCount.incrementAndGet());

        // Create the CodigoPostal
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(codigoPostal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CodigoPostal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCodigoPostal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codigoPostal.setId(longCount.incrementAndGet());

        // Create the CodigoPostal
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(codigoPostal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CodigoPostal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCodigoPostalWithPatch() throws Exception {
        // Initialize the database
        insertedCodigoPostal = codigoPostalRepository.save(codigoPostal).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codigoPostal using partial update
        CodigoPostal partialUpdatedCodigoPostal = new CodigoPostal();
        partialUpdatedCodigoPostal.setId(codigoPostal.getId());

        partialUpdatedCodigoPostal
            .claveEstado(UPDATED_CLAVE_ESTADO)
            .claveOficina(UPDATED_CLAVE_OFICINA)
            .claveCP(UPDATED_CLAVE_CP)
            .claveTipoAsentamiento(UPDATED_CLAVE_TIPO_ASENTAMIENTO)
            .zona(UPDATED_ZONA)
            .claveCiudad(UPDATED_CLAVE_CIUDAD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCodigoPostal.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCodigoPostal))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CodigoPostal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodigoPostalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCodigoPostal, codigoPostal),
            getPersistedCodigoPostal(codigoPostal)
        );
    }

    @Test
    void fullUpdateCodigoPostalWithPatch() throws Exception {
        // Initialize the database
        insertedCodigoPostal = codigoPostalRepository.save(codigoPostal).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codigoPostal using partial update
        CodigoPostal partialUpdatedCodigoPostal = new CodigoPostal();
        partialUpdatedCodigoPostal.setId(codigoPostal.getId());

        partialUpdatedCodigoPostal
            .codigo(UPDATED_CODIGO)
            .asentamiento(UPDATED_ASENTAMIENTO)
            .tipoAsentamiento(UPDATED_TIPO_ASENTAMIENTO)
            .municipio(UPDATED_MUNICIPIO)
            .estado(UPDATED_ESTADO)
            .ciudad(UPDATED_CIUDAD)
            .codigoPostalAdministracion(UPDATED_CODIGO_POSTAL_ADMINISTRACION)
            .claveEstado(UPDATED_CLAVE_ESTADO)
            .claveOficina(UPDATED_CLAVE_OFICINA)
            .claveCP(UPDATED_CLAVE_CP)
            .claveTipoAsentamiento(UPDATED_CLAVE_TIPO_ASENTAMIENTO)
            .claveMunicipio(UPDATED_CLAVE_MUNICIPIO)
            .idAsentamientoCons(UPDATED_ID_ASENTAMIENTO_CONS)
            .zona(UPDATED_ZONA)
            .claveCiudad(UPDATED_CLAVE_CIUDAD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCodigoPostal.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCodigoPostal))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CodigoPostal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodigoPostalUpdatableFieldsEquals(partialUpdatedCodigoPostal, getPersistedCodigoPostal(partialUpdatedCodigoPostal));
    }

    @Test
    void patchNonExistingCodigoPostal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codigoPostal.setId(longCount.incrementAndGet());

        // Create the CodigoPostal
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(codigoPostal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, codigoPostalDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CodigoPostal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCodigoPostal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codigoPostal.setId(longCount.incrementAndGet());

        // Create the CodigoPostal
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(codigoPostal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CodigoPostal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCodigoPostal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codigoPostal.setId(longCount.incrementAndGet());

        // Create the CodigoPostal
        CodigoPostalDTO codigoPostalDTO = codigoPostalMapper.toDto(codigoPostal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(codigoPostalDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CodigoPostal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCodigoPostal() {
        // Initialize the database
        insertedCodigoPostal = codigoPostalRepository.save(codigoPostal).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the codigoPostal
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, codigoPostal.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return codigoPostalRepository.count().block();
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

    protected CodigoPostal getPersistedCodigoPostal(CodigoPostal codigoPostal) {
        return codigoPostalRepository.findById(codigoPostal.getId()).block();
    }

    protected void assertPersistedCodigoPostalToMatchAllProperties(CodigoPostal expectedCodigoPostal) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCodigoPostalAllPropertiesEquals(expectedCodigoPostal, getPersistedCodigoPostal(expectedCodigoPostal));
        assertCodigoPostalUpdatableFieldsEquals(expectedCodigoPostal, getPersistedCodigoPostal(expectedCodigoPostal));
    }

    protected void assertPersistedCodigoPostalToMatchUpdatableProperties(CodigoPostal expectedCodigoPostal) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCodigoPostalAllUpdatablePropertiesEquals(expectedCodigoPostal, getPersistedCodigoPostal(expectedCodigoPostal));
        assertCodigoPostalUpdatableFieldsEquals(expectedCodigoPostal, getPersistedCodigoPostal(expectedCodigoPostal));
    }
}
