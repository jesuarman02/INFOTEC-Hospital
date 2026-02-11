package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.PacienteAlergiaRepository;
import mx.infotec.pacientesms.service.PacienteAlergiaService;
import mx.infotec.pacientesms.service.dto.PacienteAlergiaDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.PacienteAlergia}.
 */
@RestController
@RequestMapping("/api/paciente-alergias")
public class PacienteAlergiaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteAlergiaResource.class);

    private static final String ENTITY_NAME = "pacientemsPacienteAlergia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacienteAlergiaService pacienteAlergiaService;

    private final PacienteAlergiaRepository pacienteAlergiaRepository;

    public PacienteAlergiaResource(PacienteAlergiaService pacienteAlergiaService, PacienteAlergiaRepository pacienteAlergiaRepository) {
        this.pacienteAlergiaService = pacienteAlergiaService;
        this.pacienteAlergiaRepository = pacienteAlergiaRepository;
    }

    /**
     * {@code POST  /paciente-alergias} : Create a new pacienteAlergia.
     *
     * @param pacienteAlergiaDTO the pacienteAlergiaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pacienteAlergiaDTO, or with status {@code 400 (Bad Request)} if the pacienteAlergia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PacienteAlergiaDTO>> createPacienteAlergia(@Valid @RequestBody PacienteAlergiaDTO pacienteAlergiaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PacienteAlergia : {}", pacienteAlergiaDTO);
        if (pacienteAlergiaDTO.getId() != null) {
            throw new BadRequestAlertException("A new pacienteAlergia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return pacienteAlergiaService
            .save(pacienteAlergiaDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/paciente-alergias/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /paciente-alergias/:id} : Updates an existing pacienteAlergia.
     *
     * @param id the id of the pacienteAlergiaDTO to save.
     * @param pacienteAlergiaDTO the pacienteAlergiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacienteAlergiaDTO,
     * or with status {@code 400 (Bad Request)} if the pacienteAlergiaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pacienteAlergiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PacienteAlergiaDTO>> updatePacienteAlergia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PacienteAlergiaDTO pacienteAlergiaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PacienteAlergia : {}, {}", id, pacienteAlergiaDTO);
        if (pacienteAlergiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacienteAlergiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pacienteAlergiaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return pacienteAlergiaService
                    .update(pacienteAlergiaDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /paciente-alergias/:id} : Partial updates given fields of an existing pacienteAlergia, field will ignore if it is null
     *
     * @param id the id of the pacienteAlergiaDTO to save.
     * @param pacienteAlergiaDTO the pacienteAlergiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacienteAlergiaDTO,
     * or with status {@code 400 (Bad Request)} if the pacienteAlergiaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pacienteAlergiaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pacienteAlergiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PacienteAlergiaDTO>> partialUpdatePacienteAlergia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PacienteAlergiaDTO pacienteAlergiaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PacienteAlergia partially : {}, {}", id, pacienteAlergiaDTO);
        if (pacienteAlergiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacienteAlergiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pacienteAlergiaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PacienteAlergiaDTO> result = pacienteAlergiaService.partialUpdate(pacienteAlergiaDTO);

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
     * {@code GET  /paciente-alergias} : get all the pacienteAlergias.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pacienteAlergias in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<PacienteAlergiaDTO>> getAllPacienteAlergias(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PacienteAlergias");
        return pacienteAlergiaService.findAll().collectList();
    }

    /**
     * {@code GET  /paciente-alergias} : get all the pacienteAlergias as a stream.
     * @return the {@link Flux} of pacienteAlergias.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PacienteAlergiaDTO> getAllPacienteAlergiasAsStream() {
        LOG.debug("REST request to get all PacienteAlergias as a stream");
        return pacienteAlergiaService.findAll();
    }

    /**
     * {@code GET  /paciente-alergias/:id} : get the "id" pacienteAlergia.
     *
     * @param id the id of the pacienteAlergiaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pacienteAlergiaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PacienteAlergiaDTO>> getPacienteAlergia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PacienteAlergia : {}", id);
        Mono<PacienteAlergiaDTO> pacienteAlergiaDTO = pacienteAlergiaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pacienteAlergiaDTO);
    }

    /**
     * {@code DELETE  /paciente-alergias/:id} : delete the "id" pacienteAlergia.
     *
     * @param id the id of the pacienteAlergiaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePacienteAlergia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PacienteAlergia : {}", id);
        return pacienteAlergiaService
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
