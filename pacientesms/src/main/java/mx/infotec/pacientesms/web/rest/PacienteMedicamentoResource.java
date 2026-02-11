package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.PacienteMedicamentoRepository;
import mx.infotec.pacientesms.service.PacienteMedicamentoService;
import mx.infotec.pacientesms.service.dto.PacienteMedicamentoDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.PacienteMedicamento}.
 */
@RestController
@RequestMapping("/api/paciente-medicamentos")
public class PacienteMedicamentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteMedicamentoResource.class);

    private static final String ENTITY_NAME = "pacientemsPacienteMedicamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacienteMedicamentoService pacienteMedicamentoService;

    private final PacienteMedicamentoRepository pacienteMedicamentoRepository;

    public PacienteMedicamentoResource(
        PacienteMedicamentoService pacienteMedicamentoService,
        PacienteMedicamentoRepository pacienteMedicamentoRepository
    ) {
        this.pacienteMedicamentoService = pacienteMedicamentoService;
        this.pacienteMedicamentoRepository = pacienteMedicamentoRepository;
    }

    /**
     * {@code POST  /paciente-medicamentos} : Create a new pacienteMedicamento.
     *
     * @param pacienteMedicamentoDTO the pacienteMedicamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pacienteMedicamentoDTO, or with status {@code 400 (Bad Request)} if the pacienteMedicamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PacienteMedicamentoDTO>> createPacienteMedicamento(
        @Valid @RequestBody PacienteMedicamentoDTO pacienteMedicamentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PacienteMedicamento : {}", pacienteMedicamentoDTO);
        if (pacienteMedicamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new pacienteMedicamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return pacienteMedicamentoService
            .save(pacienteMedicamentoDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/paciente-medicamentos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /paciente-medicamentos/:id} : Updates an existing pacienteMedicamento.
     *
     * @param id the id of the pacienteMedicamentoDTO to save.
     * @param pacienteMedicamentoDTO the pacienteMedicamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacienteMedicamentoDTO,
     * or with status {@code 400 (Bad Request)} if the pacienteMedicamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pacienteMedicamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PacienteMedicamentoDTO>> updatePacienteMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PacienteMedicamentoDTO pacienteMedicamentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PacienteMedicamento : {}, {}", id, pacienteMedicamentoDTO);
        if (pacienteMedicamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacienteMedicamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pacienteMedicamentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return pacienteMedicamentoService
                    .update(pacienteMedicamentoDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /paciente-medicamentos/:id} : Partial updates given fields of an existing pacienteMedicamento, field will ignore if it is null
     *
     * @param id the id of the pacienteMedicamentoDTO to save.
     * @param pacienteMedicamentoDTO the pacienteMedicamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacienteMedicamentoDTO,
     * or with status {@code 400 (Bad Request)} if the pacienteMedicamentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pacienteMedicamentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pacienteMedicamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PacienteMedicamentoDTO>> partialUpdatePacienteMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PacienteMedicamentoDTO pacienteMedicamentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PacienteMedicamento partially : {}, {}", id, pacienteMedicamentoDTO);
        if (pacienteMedicamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacienteMedicamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pacienteMedicamentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PacienteMedicamentoDTO> result = pacienteMedicamentoService.partialUpdate(pacienteMedicamentoDTO);

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
     * {@code GET  /paciente-medicamentos} : get all the pacienteMedicamentos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pacienteMedicamentos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PacienteMedicamentoDTO>>> getAllPacienteMedicamentos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of PacienteMedicamentos");
        return pacienteMedicamentoService
            .countAll()
            .zipWith(pacienteMedicamentoService.findAll(pageable).collectList())
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
     * {@code GET  /paciente-medicamentos/:id} : get the "id" pacienteMedicamento.
     *
     * @param id the id of the pacienteMedicamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pacienteMedicamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PacienteMedicamentoDTO>> getPacienteMedicamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PacienteMedicamento : {}", id);
        Mono<PacienteMedicamentoDTO> pacienteMedicamentoDTO = pacienteMedicamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pacienteMedicamentoDTO);
    }

    /**
     * {@code DELETE  /paciente-medicamentos/:id} : delete the "id" pacienteMedicamento.
     *
     * @param id the id of the pacienteMedicamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePacienteMedicamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PacienteMedicamento : {}", id);
        return pacienteMedicamentoService
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
