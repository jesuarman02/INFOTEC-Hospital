package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.EnfermedadRepository;
import mx.infotec.pacientesms.service.EnfermedadService;
import mx.infotec.pacientesms.service.dto.EnfermedadDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.Enfermedad}.
 */
@RestController
@RequestMapping("/api/enfermedads")
public class EnfermedadResource {

    private static final Logger LOG = LoggerFactory.getLogger(EnfermedadResource.class);

    private static final String ENTITY_NAME = "pacientemsEnfermedad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnfermedadService enfermedadService;

    private final EnfermedadRepository enfermedadRepository;

    public EnfermedadResource(EnfermedadService enfermedadService, EnfermedadRepository enfermedadRepository) {
        this.enfermedadService = enfermedadService;
        this.enfermedadRepository = enfermedadRepository;
    }

    /**
     * {@code POST  /enfermedads} : Create a new enfermedad.
     *
     * @param enfermedadDTO the enfermedadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enfermedadDTO, or with status {@code 400 (Bad Request)} if the enfermedad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EnfermedadDTO>> createEnfermedad(@Valid @RequestBody EnfermedadDTO enfermedadDTO) throws URISyntaxException {
        LOG.debug("REST request to save Enfermedad : {}", enfermedadDTO);
        if (enfermedadDTO.getId() != null) {
            throw new BadRequestAlertException("A new enfermedad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return enfermedadService
            .save(enfermedadDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/enfermedads/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /enfermedads/:id} : Updates an existing enfermedad.
     *
     * @param id the id of the enfermedadDTO to save.
     * @param enfermedadDTO the enfermedadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfermedadDTO,
     * or with status {@code 400 (Bad Request)} if the enfermedadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enfermedadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EnfermedadDTO>> updateEnfermedad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EnfermedadDTO enfermedadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Enfermedad : {}, {}", id, enfermedadDTO);
        if (enfermedadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfermedadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return enfermedadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return enfermedadService
                    .update(enfermedadDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /enfermedads/:id} : Partial updates given fields of an existing enfermedad, field will ignore if it is null
     *
     * @param id the id of the enfermedadDTO to save.
     * @param enfermedadDTO the enfermedadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfermedadDTO,
     * or with status {@code 400 (Bad Request)} if the enfermedadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enfermedadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enfermedadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EnfermedadDTO>> partialUpdateEnfermedad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EnfermedadDTO enfermedadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Enfermedad partially : {}, {}", id, enfermedadDTO);
        if (enfermedadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfermedadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return enfermedadRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EnfermedadDTO> result = enfermedadService.partialUpdate(enfermedadDTO);

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
     * {@code GET  /enfermedads} : get all the enfermedads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enfermedads in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<EnfermedadDTO>> getAllEnfermedads() {
        LOG.debug("REST request to get all Enfermedads");
        return enfermedadService.findAll().collectList();
    }

    /**
     * {@code GET  /enfermedads} : get all the enfermedads as a stream.
     * @return the {@link Flux} of enfermedads.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<EnfermedadDTO> getAllEnfermedadsAsStream() {
        LOG.debug("REST request to get all Enfermedads as a stream");
        return enfermedadService.findAll();
    }

    /**
     * {@code GET  /enfermedads/:id} : get the "id" enfermedad.
     *
     * @param id the id of the enfermedadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enfermedadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EnfermedadDTO>> getEnfermedad(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Enfermedad : {}", id);
        Mono<EnfermedadDTO> enfermedadDTO = enfermedadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enfermedadDTO);
    }

    /**
     * {@code DELETE  /enfermedads/:id} : delete the "id" enfermedad.
     *
     * @param id the id of the enfermedadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEnfermedad(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Enfermedad : {}", id);
        return enfermedadService
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
