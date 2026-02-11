package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.MedicamentoRepository;
import mx.infotec.pacientesms.service.MedicamentoService;
import mx.infotec.pacientesms.service.dto.MedicamentoDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.Medicamento}.
 */
@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicamentoResource.class);

    private static final String ENTITY_NAME = "pacientemsMedicamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicamentoService medicamentoService;

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoResource(MedicamentoService medicamentoService, MedicamentoRepository medicamentoRepository) {
        this.medicamentoService = medicamentoService;
        this.medicamentoRepository = medicamentoRepository;
    }

    /**
     * {@code POST  /medicamentos} : Create a new medicamento.
     *
     * @param medicamentoDTO the medicamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicamentoDTO, or with status {@code 400 (Bad Request)} if the medicamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MedicamentoDTO>> createMedicamento(@Valid @RequestBody MedicamentoDTO medicamentoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Medicamento : {}", medicamentoDTO);
        if (medicamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return medicamentoService
            .save(medicamentoDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/medicamentos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /medicamentos/:id} : Updates an existing medicamento.
     *
     * @param id the id of the medicamentoDTO to save.
     * @param medicamentoDTO the medicamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamentoDTO,
     * or with status {@code 400 (Bad Request)} if the medicamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MedicamentoDTO>> updateMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicamentoDTO medicamentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Medicamento : {}, {}", id, medicamentoDTO);
        if (medicamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return medicamentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return medicamentoService
                    .update(medicamentoDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /medicamentos/:id} : Partial updates given fields of an existing medicamento, field will ignore if it is null
     *
     * @param id the id of the medicamentoDTO to save.
     * @param medicamentoDTO the medicamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamentoDTO,
     * or with status {@code 400 (Bad Request)} if the medicamentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicamentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MedicamentoDTO>> partialUpdateMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicamentoDTO medicamentoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Medicamento partially : {}, {}", id, medicamentoDTO);
        if (medicamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return medicamentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MedicamentoDTO> result = medicamentoService.partialUpdate(medicamentoDTO);

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
     * {@code GET  /medicamentos} : get all the medicamentos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicamentos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<MedicamentoDTO>> getAllMedicamentos() {
        LOG.debug("REST request to get all Medicamentos");
        return medicamentoService.findAll().collectList();
    }

    /**
     * {@code GET  /medicamentos} : get all the medicamentos as a stream.
     * @return the {@link Flux} of medicamentos.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MedicamentoDTO> getAllMedicamentosAsStream() {
        LOG.debug("REST request to get all Medicamentos as a stream");
        return medicamentoService.findAll();
    }

    /**
     * {@code GET  /medicamentos/:id} : get the "id" medicamento.
     *
     * @param id the id of the medicamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MedicamentoDTO>> getMedicamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Medicamento : {}", id);
        Mono<MedicamentoDTO> medicamentoDTO = medicamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicamentoDTO);
    }

    /**
     * {@code DELETE  /medicamentos/:id} : delete the "id" medicamento.
     *
     * @param id the id of the medicamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMedicamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Medicamento : {}", id);
        return medicamentoService
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
