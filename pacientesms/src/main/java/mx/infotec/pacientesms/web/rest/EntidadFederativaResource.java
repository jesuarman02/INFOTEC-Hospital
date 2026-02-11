package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.EntidadFederativaRepository;
import mx.infotec.pacientesms.service.EntidadFederativaService;
import mx.infotec.pacientesms.service.dto.EntidadFederativaDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.EntidadFederativa}.
 */
@RestController
@RequestMapping("/api/entidad-federativas")
public class EntidadFederativaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntidadFederativaResource.class);

    private static final String ENTITY_NAME = "pacientesmsEntidadFederativa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntidadFederativaService entidadFederativaService;

    private final EntidadFederativaRepository entidadFederativaRepository;

    public EntidadFederativaResource(
        EntidadFederativaService entidadFederativaService,
        EntidadFederativaRepository entidadFederativaRepository
    ) {
        this.entidadFederativaService = entidadFederativaService;
        this.entidadFederativaRepository = entidadFederativaRepository;
    }

    /**
     * {@code POST  /entidad-federativas} : Create a new entidadFederativa.
     *
     * @param entidadFederativaDTO the entidadFederativaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entidadFederativaDTO, or with status {@code 400 (Bad Request)} if the entidadFederativa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EntidadFederativaDTO>> createEntidadFederativa(
        @Valid @RequestBody EntidadFederativaDTO entidadFederativaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EntidadFederativa : {}", entidadFederativaDTO);
        if (entidadFederativaDTO.getId() != null) {
            throw new BadRequestAlertException("A new entidadFederativa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return entidadFederativaService
            .save(entidadFederativaDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/entidad-federativas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /entidad-federativas/:id} : Updates an existing entidadFederativa.
     *
     * @param id the id of the entidadFederativaDTO to save.
     * @param entidadFederativaDTO the entidadFederativaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entidadFederativaDTO,
     * or with status {@code 400 (Bad Request)} if the entidadFederativaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entidadFederativaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EntidadFederativaDTO>> updateEntidadFederativa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EntidadFederativaDTO entidadFederativaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EntidadFederativa : {}, {}", id, entidadFederativaDTO);
        if (entidadFederativaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entidadFederativaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return entidadFederativaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return entidadFederativaService
                    .update(entidadFederativaDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /entidad-federativas/:id} : Partial updates given fields of an existing entidadFederativa, field will ignore if it is null
     *
     * @param id the id of the entidadFederativaDTO to save.
     * @param entidadFederativaDTO the entidadFederativaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entidadFederativaDTO,
     * or with status {@code 400 (Bad Request)} if the entidadFederativaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the entidadFederativaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the entidadFederativaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EntidadFederativaDTO>> partialUpdateEntidadFederativa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EntidadFederativaDTO entidadFederativaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EntidadFederativa partially : {}, {}", id, entidadFederativaDTO);
        if (entidadFederativaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entidadFederativaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return entidadFederativaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EntidadFederativaDTO> result = entidadFederativaService.partialUpdate(entidadFederativaDTO);

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
     * {@code GET  /entidad-federativas} : get all the entidadFederativas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entidadFederativas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<EntidadFederativaDTO>> getAllEntidadFederativas() {
        LOG.debug("REST request to get all EntidadFederativas");
        return entidadFederativaService.findAll().collectList();
    }

    /**
     * {@code GET  /entidad-federativas} : get all the entidadFederativas as a stream.
     * @return the {@link Flux} of entidadFederativas.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<EntidadFederativaDTO> getAllEntidadFederativasAsStream() {
        LOG.debug("REST request to get all EntidadFederativas as a stream");
        return entidadFederativaService.findAll();
    }

    /**
     * {@code GET  /entidad-federativas/:id} : get the "id" entidadFederativa.
     *
     * @param id the id of the entidadFederativaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entidadFederativaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EntidadFederativaDTO>> getEntidadFederativa(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EntidadFederativa : {}", id);
        Mono<EntidadFederativaDTO> entidadFederativaDTO = entidadFederativaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entidadFederativaDTO);
    }

    /**
     * {@code DELETE  /entidad-federativas/:id} : delete the "id" entidadFederativa.
     *
     * @param id the id of the entidadFederativaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEntidadFederativa(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EntidadFederativa : {}", id);
        return entidadFederativaService
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
