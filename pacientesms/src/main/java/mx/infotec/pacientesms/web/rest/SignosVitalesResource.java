package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.SignosVitalesRepository;
import mx.infotec.pacientesms.service.SignosVitalesService;
import mx.infotec.pacientesms.service.dto.SignosVitalesDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.SignosVitales}.
 */
@RestController
@RequestMapping("/api/signos-vitales")
public class SignosVitalesResource {

    private static final Logger LOG = LoggerFactory.getLogger(SignosVitalesResource.class);

    private static final String ENTITY_NAME = "pacientemsSignosVitales";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignosVitalesService signosVitalesService;

    private final SignosVitalesRepository signosVitalesRepository;

    public SignosVitalesResource(SignosVitalesService signosVitalesService, SignosVitalesRepository signosVitalesRepository) {
        this.signosVitalesService = signosVitalesService;
        this.signosVitalesRepository = signosVitalesRepository;
    }

    /**
     * {@code POST  /signos-vitales} : Create a new signosVitales.
     *
     * @param signosVitalesDTO the signosVitalesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signosVitalesDTO, or with status {@code 400 (Bad Request)} if the signosVitales has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SignosVitalesDTO>> createSignosVitales(@Valid @RequestBody SignosVitalesDTO signosVitalesDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SignosVitales : {}", signosVitalesDTO);
        if (signosVitalesDTO.getId() != null) {
            throw new BadRequestAlertException("A new signosVitales cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return signosVitalesService
            .save(signosVitalesDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/signos-vitales/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /signos-vitales/:id} : Updates an existing signosVitales.
     *
     * @param id the id of the signosVitalesDTO to save.
     * @param signosVitalesDTO the signosVitalesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signosVitalesDTO,
     * or with status {@code 400 (Bad Request)} if the signosVitalesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signosVitalesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SignosVitalesDTO>> updateSignosVitales(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SignosVitalesDTO signosVitalesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SignosVitales : {}, {}", id, signosVitalesDTO);
        if (signosVitalesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signosVitalesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return signosVitalesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return signosVitalesService
                    .update(signosVitalesDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /signos-vitales/:id} : Partial updates given fields of an existing signosVitales, field will ignore if it is null
     *
     * @param id the id of the signosVitalesDTO to save.
     * @param signosVitalesDTO the signosVitalesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signosVitalesDTO,
     * or with status {@code 400 (Bad Request)} if the signosVitalesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the signosVitalesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the signosVitalesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SignosVitalesDTO>> partialUpdateSignosVitales(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SignosVitalesDTO signosVitalesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SignosVitales partially : {}, {}", id, signosVitalesDTO);
        if (signosVitalesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signosVitalesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return signosVitalesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SignosVitalesDTO> result = signosVitalesService.partialUpdate(signosVitalesDTO);

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
     * {@code GET  /signos-vitales} : get all the signosVitales.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signosVitales in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SignosVitalesDTO>>> getAllSignosVitales(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of SignosVitales");
        return signosVitalesService
            .countAll()
            .zipWith(signosVitalesService.findAll(pageable).collectList())
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
     * {@code GET  /signos-vitales/:id} : get the "id" signosVitales.
     *
     * @param id the id of the signosVitalesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signosVitalesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SignosVitalesDTO>> getSignosVitales(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SignosVitales : {}", id);
        Mono<SignosVitalesDTO> signosVitalesDTO = signosVitalesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signosVitalesDTO);
    }

    /**
     * {@code DELETE  /signos-vitales/:id} : delete the "id" signosVitales.
     *
     * @param id the id of the signosVitalesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSignosVitales(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SignosVitales : {}", id);
        return signosVitalesService
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
