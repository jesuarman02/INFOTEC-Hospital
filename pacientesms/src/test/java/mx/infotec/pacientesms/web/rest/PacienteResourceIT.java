package mx.infotec.pacientesms.web.rest;

import static mx.infotec.pacientesms.domain.PacienteAsserts.*;
import static mx.infotec.pacientesms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import mx.infotec.pacientesms.IntegrationTest;
import mx.infotec.pacientesms.domain.Paciente;
import mx.infotec.pacientesms.domain.enumeration.EstadoCivil;
import mx.infotec.pacientesms.domain.enumeration.Nacionalidad;
import mx.infotec.pacientesms.domain.enumeration.Sexo;
import mx.infotec.pacientesms.repository.EntityManager;
import mx.infotec.pacientesms.repository.PacienteRepository;
import mx.infotec.pacientesms.service.PacienteService;
import mx.infotec.pacientesms.service.dto.PacienteDTO;
import mx.infotec.pacientesms.service.mapper.PacienteMapper;
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
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PacienteResourceIT {

    private static final Integer DEFAULT_ECU = 1;
    private static final Integer UPDATED_ECU = 2;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO_PATERNO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO_PATERNO = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO_MATERNO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO_MATERNO = "BBBBBBBBBB";

    private static final Sexo DEFAULT_SEXO = Sexo.M;
    private static final Sexo UPDATED_SEXO = Sexo.M;

private static final Nacionalidad DEFAULT_NACIONALIDAD = Nacionalidad.MEXICANA;    
private static final Nacionalidad UPDATED_NACIONALIDAD = Nacionalidad.EXTRANJERA;
    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

private static final EstadoCivil DEFAULT_ESTADO_CIVIL = EstadoCivil.SOLTERO;
private static final EstadoCivil UPDATED_ESTADO_CIVIL = EstadoCivil.CASADO;
    private static final String DEFAULT_CURP = "AAAAAAAAAA";
    private static final String UPDATED_CURP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Mock
    private PacienteRepository pacienteRepositoryMock;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Mock
    private PacienteService pacienteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Paciente paciente;

    private Paciente insertedPaciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity() {
        return new Paciente()
            .ecu(DEFAULT_ECU)
            .nombre(DEFAULT_NOMBRE)
            .apellidoPaterno(DEFAULT_APELLIDO_PATERNO)
            .apellidoMaterno(DEFAULT_APELLIDO_MATERNO)
            .sexo(DEFAULT_SEXO)
            .nacionalidad(DEFAULT_NACIONALIDAD)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .estadoCivil(DEFAULT_ESTADO_CIVIL)
            .curp(DEFAULT_CURP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity() {
        return new Paciente()
            .ecu(UPDATED_ECU)
            .nombre(UPDATED_NOMBRE)
            .apellidoPaterno(UPDATED_APELLIDO_PATERNO)
            .apellidoMaterno(UPDATED_APELLIDO_MATERNO)
            .sexo(UPDATED_SEXO)
            .nacionalidad(UPDATED_NACIONALIDAD)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .curp(UPDATED_CURP);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Paciente.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        paciente = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaciente != null) {
            pacienteRepository.delete(insertedPaciente).block();
            insertedPaciente = null;
        }
        deleteEntities(em);
    }

    @Test
    void createPaciente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);
        var returnedPacienteDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PacienteDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Paciente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaciente = pacienteMapper.toEntity(returnedPacienteDTO);
        assertPacienteUpdatableFieldsEquals(returnedPaciente, getPersistedPaciente(returnedPaciente));

        insertedPaciente = returnedPaciente;
    }

    @Test
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkEcuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setEcu(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setNombre(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkApellidoPaternoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setApellidoPaterno(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSexoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setSexo(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaNacimientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setFechaNacimiento(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCurpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setCurp(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPacientes() {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        // Get all the pacienteList
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
            .value(hasItem(paciente.getId().intValue()))
            .jsonPath("$.[*].ecu")
            .value(hasItem(DEFAULT_ECU))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].apellidoPaterno")
            .value(hasItem(DEFAULT_APELLIDO_PATERNO))
            .jsonPath("$.[*].apellidoMaterno")
            .value(hasItem(DEFAULT_APELLIDO_MATERNO))
            .jsonPath("$.[*].sexo")
            .value(hasItem(DEFAULT_SEXO.toString()))
            .jsonPath("$.[*].nacionalidad")
            .value(hasItem(DEFAULT_NACIONALIDAD))
            .jsonPath("$.[*].fechaNacimiento")
            .value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString()))
            .jsonPath("$.[*].estadoCivil")
            .value(hasItem(DEFAULT_ESTADO_CIVIL))
            .jsonPath("$.[*].curp")
            .value(hasItem(DEFAULT_CURP));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacientesWithEagerRelationshipsIsEnabled() {
        when(pacienteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(pacienteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacientesWithEagerRelationshipsIsNotEnabled() {
        when(pacienteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(pacienteRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPaciente() {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        // Get the paciente
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paciente.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paciente.getId().intValue()))
            .jsonPath("$.ecu")
            .value(is(DEFAULT_ECU))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.apellidoPaterno")
            .value(is(DEFAULT_APELLIDO_PATERNO))
            .jsonPath("$.apellidoMaterno")
            .value(is(DEFAULT_APELLIDO_MATERNO))
            .jsonPath("$.sexo")
            .value(is(DEFAULT_SEXO.toString()))
            .jsonPath("$.nacionalidad")
            .value(is(DEFAULT_NACIONALIDAD))
            .jsonPath("$.fechaNacimiento")
            .value(is(DEFAULT_FECHA_NACIMIENTO.toString()))
            .jsonPath("$.estadoCivil")
            .value(is(DEFAULT_ESTADO_CIVIL))
            .jsonPath("$.curp")
            .value(is(DEFAULT_CURP));
    }

    @Test
    void getNonExistingPaciente() {
        // Get the paciente
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).block();
        updatedPaciente
            .ecu(UPDATED_ECU)
            .nombre(UPDATED_NOMBRE)
            .apellidoPaterno(UPDATED_APELLIDO_PATERNO)
            .apellidoMaterno(UPDATED_APELLIDO_MATERNO)
            .sexo(UPDATED_SEXO)
            .nacionalidad(UPDATED_NACIONALIDAD)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .curp(UPDATED_CURP);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(updatedPaciente);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacienteToMatchAllProperties(updatedPaciente);
    }

    @Test
    void putNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.nombre(UPDATED_NOMBRE).nacionalidad(UPDATED_NACIONALIDAD).fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPaciente))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaciente, paciente), getPersistedPaciente(paciente));
    }

    @Test
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .ecu(UPDATED_ECU)
            .nombre(UPDATED_NOMBRE)
            .apellidoPaterno(UPDATED_APELLIDO_PATERNO)
            .apellidoMaterno(UPDATED_APELLIDO_MATERNO)
            .sexo(UPDATED_SEXO)
            .nacionalidad(UPDATED_NACIONALIDAD)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .estadoCivil(UPDATED_ESTADO_CIVIL)
            .curp(UPDATED_CURP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPaciente))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(partialUpdatedPaciente, getPersistedPaciente(partialUpdatedPaciente));
    }

    @Test
    void patchNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pacienteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePaciente() {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paciente
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paciente.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacienteRepository.count().block();
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

    protected Paciente getPersistedPaciente(Paciente paciente) {
        return pacienteRepository.findById(paciente.getId()).block();
    }

    protected void assertPersistedPacienteToMatchAllProperties(Paciente expectedPaciente) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteAllPropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
        assertPacienteUpdatableFieldsEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }

    protected void assertPersistedPacienteToMatchUpdatableProperties(Paciente expectedPaciente) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteAllUpdatablePropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
        assertPacienteUpdatableFieldsEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }
}
