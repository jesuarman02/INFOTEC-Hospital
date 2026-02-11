package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.AlergiaRepository;
import mx.infotec.pacientesms.service.AlergiaService;
import mx.infotec.pacientesms.service.dto.AlergiaDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.Alergia}.
 */
@RestController
@RequestMapping("/api/alergias")
public class AlergiaResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlergiaResource.class);

    private static final String ENTITY_NAME = "pacientemsAlergia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlergiaService alergiaService;

    private final AlergiaRepository alergiaRepository;

    public AlergiaResource(AlergiaService alergiaService, AlergiaRepository alergiaRepository) {
        this.alergiaService = alergiaService;
        this.alergiaRepository = alergiaRepository;
    }

    /**
     * {@code POST  /alergias} : Create a new alergia.
     *
     * @param alergiaDTO the alergiaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alergiaDTO, or with status {@code 400 (Bad Request)} if the alergia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AlergiaDTO>> createAlergia(@Valid @RequestBody AlergiaDTO alergiaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Alergia : {}", alergiaDTO);
        if (alergiaDTO.getId() != null) {
            throw new BadRequestAlertException("A new alergia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return alergiaService
            .save(alergiaDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/alergias/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /alergias/:id} : Updates an existing alergia.
     *
     * @param id the id of the alergiaDTO to save.
     * @param alergiaDTO the alergiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alergiaDTO,
     * or with status {@code 400 (Bad Request)} if the alergiaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alergiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AlergiaDTO>> updateAlergia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlergiaDTO alergiaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Alergia : {}, {}", id, alergiaDTO);
        if (alergiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alergiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return alergiaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return alergiaService
                    .update(alergiaDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /alergias/:id} : Partial updates given fields of an existing alergia, field will ignore if it is null
     *
     * @param id the id of the alergiaDTO to save.
     * @param alergiaDTO the alergiaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alergiaDTO,
     * or with status {@code 400 (Bad Request)} if the alergiaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alergiaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alergiaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AlergiaDTO>> partialUpdateAlergia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlergiaDTO alergiaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Alergia partially : {}, {}", id, alergiaDTO);
        if (alergiaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alergiaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return alergiaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AlergiaDTO> result = alergiaService.partialUpdate(alergiaDTO);

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
     * {@code GET  /alergias} : get all the alergias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alergias in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<AlergiaDTO>> getAllAlergias() {
        LOG.debug("REST request to get all Alergias");
        return alergiaService.findAll().collectList();
    }

    /**
     * {@code GET  /alergias} : get all the alergias as a stream.
     * @return the {@link Flux} of alergias.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AlergiaDTO> getAllAlergiasAsStream() {
        LOG.debug("REST request to get all Alergias as a stream");
        return alergiaService.findAll();
    }

    /**
     * {@code GET  /alergias/:id} : get the "id" alergia.
     *
     * @param id the id of the alergiaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alergiaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AlergiaDTO>> getAlergia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Alergia : {}", id);
        Mono<AlergiaDTO> alergiaDTO = alergiaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alergiaDTO);
    }

    /**
     * {@code DELETE  /alergias/:id} : delete the "id" alergia.
     *
     * @param id the id of the alergiaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAlergia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Alergia : {}", id);
        return alergiaService
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
