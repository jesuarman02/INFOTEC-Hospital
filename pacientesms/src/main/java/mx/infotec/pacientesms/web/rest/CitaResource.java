package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.domain.Cita;
import mx.infotec.pacientesms.repository.CitaRepository;
import mx.infotec.pacientesms.service.CitaService;
import mx.infotec.pacientesms.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link mx.infotec.pacientesms.domain.Cita}.
 */
@RestController
@RequestMapping("/api/citas")
public class CitaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CitaResource.class);

    private static final String ENTITY_NAME = "pacientemsCita";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CitaService citaService;

    private final CitaRepository citaRepository;

    public CitaResource(CitaService citaService, CitaRepository citaRepository) {
        this.citaService = citaService;
        this.citaRepository = citaRepository;
    }

    /**
     * {@code POST  /citas} : Create a new cita.
     *
     * @param cita the cita to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cita, or with status {@code 400 (Bad Request)} if the cita has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Cita>> createCita(@Valid @RequestBody Cita cita) throws URISyntaxException {
        LOG.debug("REST request to save Cita : {}", cita);
        if (cita.getId() != null) {
            throw new BadRequestAlertException("A new cita cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return citaService
            .save(cita)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/citas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /citas/:id} : Updates an existing cita.
     *
     * @param id the id of the cita to save.
     * @param cita the cita to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cita,
     * or with status {@code 400 (Bad Request)} if the cita is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cita couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Cita>> updateCita(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cita cita
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cita : {}, {}", id, cita);
        if (cita.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cita.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return citaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return citaService
                    .update(cita)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /citas/:id} : Partial updates given fields of an existing cita, field will ignore if it is null
     *
     * @param id the id of the cita to save.
     * @param cita the cita to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cita,
     * or with status {@code 400 (Bad Request)} if the cita is not valid,
     * or with status {@code 404 (Not Found)} if the cita is not found,
     * or with status {@code 500 (Internal Server Error)} if the cita couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Cita>> partialUpdateCita(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cita cita
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cita partially : {}, {}", id, cita);
        if (cita.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cita.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return citaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Cita> result = citaService.partialUpdate(cita);

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
     * {@code GET  /citas} : get all the citas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of citas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Cita>>> getAllCitas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Citas");
        return citaService
            .countAll()
            .zipWith(citaService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /citas/:id} : get the "id" cita.
     *
     * @param id the id of the cita to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cita, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Cita>> getCita(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cita : {}", id);
        Mono<Cita> cita = citaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cita);
    }

    /**
     * {@code DELETE  /citas/:id} : delete the "id" cita.
     *
     * @param id the id of the cita to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCita(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cita : {}", id);
        return citaService
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
