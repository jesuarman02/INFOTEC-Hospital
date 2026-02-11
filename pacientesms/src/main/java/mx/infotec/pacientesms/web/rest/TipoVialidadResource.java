package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.TipoVialidadRepository;
import mx.infotec.pacientesms.service.TipoVialidadService;
import mx.infotec.pacientesms.service.dto.TipoVialidadDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.TipoVialidad}.
 */
@RestController
@RequestMapping("/api/tipo-vialidads")
public class TipoVialidadResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoVialidadResource.class);

    private static final String ENTITY_NAME = "pacientesmsTipoVialidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoVialidadService tipoVialidadService;

    private final TipoVialidadRepository tipoVialidadRepository;

    public TipoVialidadResource(TipoVialidadService tipoVialidadService, TipoVialidadRepository tipoVialidadRepository) {
        this.tipoVialidadService = tipoVialidadService;
        this.tipoVialidadRepository = tipoVialidadRepository;
    }

    /**
     * {@code POST  /tipo-vialidads} : Create a new tipoVialidad.
     *
     * @param tipoVialidadDTO the tipoVialidadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoVialidadDTO, or with status {@code 400 (Bad Request)} if the tipoVialidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<TipoVialidadDTO>> createTipoVialidad(@Valid @RequestBody TipoVialidadDTO tipoVialidadDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TipoVialidad : {}", tipoVialidadDTO);
        if (tipoVialidadDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoVialidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return tipoVialidadService
            .save(tipoVialidadDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/tipo-vialidads/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tipo-vialidads/:id} : Updates an existing tipoVialidad.
     *
     * @param id the id of the tipoVialidadDTO to save.
     * @param tipoVialidadDTO the tipoVialidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoVialidadDTO,
     * or with status {@code 400 (Bad Request)} if the tipoVialidadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoVialidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TipoVialidadDTO>> updateTipoVialidad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoVialidadDTO tipoVialidadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoVialidad : {}, {}", id, tipoVialidadDTO);
        if (tipoVialidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoVialidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tipoVialidadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return tipoVialidadService
                    .update(tipoVialidadDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /tipo-vialidads/:id} : Partial updates given fields of an existing tipoVialidad, field will ignore if it is null
     *
     * @param id the id of the tipoVialidadDTO to save.
     * @param tipoVialidadDTO the tipoVialidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoVialidadDTO,
     * or with status {@code 400 (Bad Request)} if the tipoVialidadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoVialidadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoVialidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TipoVialidadDTO>> partialUpdateTipoVialidad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoVialidadDTO tipoVialidadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TipoVialidad partially : {}, {}", id, tipoVialidadDTO);
        if (tipoVialidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoVialidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return tipoVialidadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TipoVialidadDTO> result = tipoVialidadService.partialUpdate(tipoVialidadDTO);

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
     * {@code GET  /tipo-vialidads} : get all the tipoVialidads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoVialidads in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<TipoVialidadDTO>> getAllTipoVialidads() {
        LOG.debug("REST request to get all TipoVialidads");
        return tipoVialidadService.findAll().collectList();
    }

    /**
     * {@code GET  /tipo-vialidads} : get all the tipoVialidads as a stream.
     * @return the {@link Flux} of tipoVialidads.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<TipoVialidadDTO> getAllTipoVialidadsAsStream() {
        LOG.debug("REST request to get all TipoVialidads as a stream");
        return tipoVialidadService.findAll();
    }

    /**
     * {@code GET  /tipo-vialidads/:id} : get the "id" tipoVialidad.
     *
     * @param id the id of the tipoVialidadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoVialidadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TipoVialidadDTO>> getTipoVialidad(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoVialidad : {}", id);
        Mono<TipoVialidadDTO> tipoVialidadDTO = tipoVialidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoVialidadDTO);
    }

    /**
     * {@code DELETE  /tipo-vialidads/:id} : delete the "id" tipoVialidad.
     *
     * @param id the id of the tipoVialidadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTipoVialidad(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoVialidad : {}", id);
        return tipoVialidadService
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
