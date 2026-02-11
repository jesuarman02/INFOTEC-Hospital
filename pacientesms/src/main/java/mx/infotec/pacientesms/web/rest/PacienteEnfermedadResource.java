package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.PacienteEnfermedadRepository;
import mx.infotec.pacientesms.service.PacienteEnfermedadService;
import mx.infotec.pacientesms.service.dto.PacienteEnfermedadDTO;
import mx.infotec.pacientesms.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link mx.infotec.pacientesms.domain.PacienteEnfermedad}.
 */
@RestController
@RequestMapping("/api/paciente-enfermedads")
public class PacienteEnfermedadResource {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteEnfermedadResource.class);

    private static final String ENTITY_NAME = "pacientemsPacienteEnfermedad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacienteEnfermedadService pacienteEnfermedadService;

    private final PacienteEnfermedadRepository pacienteEnfermedadRepository;

    public PacienteEnfermedadResource(
        PacienteEnfermedadService pacienteEnfermedadService,
        PacienteEnfermedadRepository pacienteEnfermedadRepository
    ) {
        this.pacienteEnfermedadService = pacienteEnfermedadService;
        this.pacienteEnfermedadRepository = pacienteEnfermedadRepository;
    }

    /**
     * {@code POST  /paciente-enfermedads} : Create a new pacienteEnfermedad.
     *
     * @param pacienteEnfermedadDTO the pacienteEnfermedadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pacienteEnfermedadDTO, or with status {@code 400 (Bad Request)} if the pacienteEnfermedad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PacienteEnfermedadDTO>> createPacienteEnfermedad(
        @Valid @RequestBody PacienteEnfermedadDTO pacienteEnfermedadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PacienteEnfermedad : {}", pacienteEnfermedadDTO);
        if (pacienteEnfermedadDTO.getId() != null) {
            throw new BadRequestAlertException("A new pacienteEnfermedad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return pacienteEnfermedadService
            .save(pacienteEnfermedadDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/paciente-enfermedads/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /paciente-enfermedads/:id} : Updates an existing pacienteEnfermedad.
     *
     * @param id the id of the pacienteEnfermedadDTO to save.
     * @param pacienteEnfermedadDTO the pacienteEnfermedadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacienteEnfermedadDTO,
     * or with status {@code 400 (Bad Request)} if the pacienteEnfermedadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pacienteEnfermedadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PacienteEnfermedadDTO>> updatePacienteEnfermedad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PacienteEnfermedadDTO pacienteEnfermedadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PacienteEnfermedad : {}, {}", id, pacienteEnfermedadDTO);
        if (pacienteEnfermedadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacienteEnfermedadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pacienteEnfermedadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return pacienteEnfermedadService
                    .update(pacienteEnfermedadDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /paciente-enfermedads/:id} : Partial updates given fields of an existing pacienteEnfermedad, field will ignore if it is null
     *
     * @param id the id of the pacienteEnfermedadDTO to save.
     * @param pacienteEnfermedadDTO the pacienteEnfermedadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacienteEnfermedadDTO,
     * or with status {@code 400 (Bad Request)} if the pacienteEnfermedadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pacienteEnfermedadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pacienteEnfermedadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PacienteEnfermedadDTO>> partialUpdatePacienteEnfermedad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PacienteEnfermedadDTO pacienteEnfermedadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PacienteEnfermedad partially : {}, {}", id, pacienteEnfermedadDTO);
        if (pacienteEnfermedadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacienteEnfermedadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pacienteEnfermedadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PacienteEnfermedadDTO> result = pacienteEnfermedadService.partialUpdate(pacienteEnfermedadDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /paciente-enfermedads} : get all the pacienteEnfermedads.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pacienteEnfermedads in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<PacienteEnfermedadDTO>> getAllPacienteEnfermedads(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PacienteEnfermedads");
        return pacienteEnfermedadService.findAll().collectList();
    }

    /**
     * {@code GET  /paciente-enfermedads} : get all the pacienteEnfermedads as a stream.
     * @return the {@link Flux} of pacienteEnfermedads.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PacienteEnfermedadDTO> getAllPacienteEnfermedadsAsStream() {
        LOG.debug("REST request to get all PacienteEnfermedads as a stream");
        return pacienteEnfermedadService.findAll();
    }

    /**
     * {@code GET  /paciente-enfermedads/:id} : get the "id" pacienteEnfermedad.
     *
     * @param id the id of the pacienteEnfermedadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pacienteEnfermedadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PacienteEnfermedadDTO>> getPacienteEnfermedad(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PacienteEnfermedad : {}", id);
        Mono<PacienteEnfermedadDTO> pacienteEnfermedadDTO = pacienteEnfermedadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pacienteEnfermedadDTO);
    }

    /**
     * {@code DELETE  /paciente-enfermedads/:id} : delete the "id" pacienteEnfermedad.
     *
     * @param id the id of the pacienteEnfermedadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePacienteEnfermedad(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PacienteEnfermedad : {}", id);
        return pacienteEnfermedadService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
