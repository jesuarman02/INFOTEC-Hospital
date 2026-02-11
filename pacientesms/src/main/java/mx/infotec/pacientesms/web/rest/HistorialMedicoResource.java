package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.HistorialMedicoRepository;
import mx.infotec.pacientesms.service.HistorialMedicoService;
import mx.infotec.pacientesms.service.dto.HistorialMedicoDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.HistorialMedico}.
 */
@RestController
@RequestMapping("/api/historial-medicos")
public class HistorialMedicoResource {

    private static final Logger LOG = LoggerFactory.getLogger(HistorialMedicoResource.class);

    private static final String ENTITY_NAME = "pacientemsHistorialMedico";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistorialMedicoService historialMedicoService;

    private final HistorialMedicoRepository historialMedicoRepository;

    public HistorialMedicoResource(HistorialMedicoService historialMedicoService, HistorialMedicoRepository historialMedicoRepository) {
        this.historialMedicoService = historialMedicoService;
        this.historialMedicoRepository = historialMedicoRepository;
    }

    /**
     * {@code POST  /historial-medicos} : Create a new historialMedico.
     *
     * @param historialMedicoDTO the historialMedicoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historialMedicoDTO, or with status {@code 400 (Bad Request)} if the historialMedico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<HistorialMedicoDTO>> createHistorialMedico(@Valid @RequestBody HistorialMedicoDTO historialMedicoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HistorialMedico : {}", historialMedicoDTO);
        if (historialMedicoDTO.getId() != null) {
            throw new BadRequestAlertException("A new historialMedico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return historialMedicoService
            .save(historialMedicoDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/historial-medicos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /historial-medicos/:id} : Updates an existing historialMedico.
     *
     * @param id the id of the historialMedicoDTO to save.
     * @param historialMedicoDTO the historialMedicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historialMedicoDTO,
     * or with status {@code 400 (Bad Request)} if the historialMedicoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historialMedicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<HistorialMedicoDTO>> updateHistorialMedico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistorialMedicoDTO historialMedicoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HistorialMedico : {}, {}", id, historialMedicoDTO);
        if (historialMedicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historialMedicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historialMedicoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return historialMedicoService
                    .update(historialMedicoDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /historial-medicos/:id} : Partial updates given fields of an existing historialMedico, field will ignore if it is null
     *
     * @param id the id of the historialMedicoDTO to save.
     * @param historialMedicoDTO the historialMedicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historialMedicoDTO,
     * or with status {@code 400 (Bad Request)} if the historialMedicoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historialMedicoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historialMedicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HistorialMedicoDTO>> partialUpdateHistorialMedico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistorialMedicoDTO historialMedicoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HistorialMedico partially : {}, {}", id, historialMedicoDTO);
        if (historialMedicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historialMedicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historialMedicoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HistorialMedicoDTO> result = historialMedicoService.partialUpdate(historialMedicoDTO);

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
     * {@code GET  /historial-medicos} : get all the historialMedicos.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historialMedicos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<HistorialMedicoDTO>> getAllHistorialMedicos(@RequestParam(name = "filter", required = false) String filter) {
        if ("paciente-is-null".equals(filter)) {
            LOG.debug("REST request to get all HistorialMedicos where paciente is null");
            return historialMedicoService.findAllWherePacienteIsNull().collectList();
        }
        LOG.debug("REST request to get all HistorialMedicos");
        return historialMedicoService.findAll().collectList();
    }

    /**
     * {@code GET  /historial-medicos} : get all the historialMedicos as a stream.
     * @return the {@link Flux} of historialMedicos.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<HistorialMedicoDTO> getAllHistorialMedicosAsStream() {
        LOG.debug("REST request to get all HistorialMedicos as a stream");
        return historialMedicoService.findAll();
    }

    /**
     * {@code GET  /historial-medicos/:id} : get the "id" historialMedico.
     *
     * @param id the id of the historialMedicoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historialMedicoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HistorialMedicoDTO>> getHistorialMedico(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HistorialMedico : {}", id);
        Mono<HistorialMedicoDTO> historialMedicoDTO = historialMedicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historialMedicoDTO);
    }

    /**
     * {@code DELETE  /historial-medicos/:id} : delete the "id" historialMedico.
     *
     * @param id the id of the historialMedicoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteHistorialMedico(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HistorialMedico : {}", id);
        return historialMedicoService
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
