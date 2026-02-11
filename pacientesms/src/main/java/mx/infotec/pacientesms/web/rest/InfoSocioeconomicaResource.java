package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.InfoSocioeconomicaRepository;
import mx.infotec.pacientesms.service.InfoSocioeconomicaService;
import mx.infotec.pacientesms.service.dto.InfoSocioeconomicaDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.InfoSocioeconomica}.
 */
@RestController
@RequestMapping("/api/info-socioeconomicas")
public class InfoSocioeconomicaResource {

    private static final Logger LOG = LoggerFactory.getLogger(InfoSocioeconomicaResource.class);

    private static final String ENTITY_NAME = "pacientemsInfoSocioeconomica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfoSocioeconomicaService infoSocioeconomicaService;

    private final InfoSocioeconomicaRepository infoSocioeconomicaRepository;

    public InfoSocioeconomicaResource(
        InfoSocioeconomicaService infoSocioeconomicaService,
        InfoSocioeconomicaRepository infoSocioeconomicaRepository
    ) {
        this.infoSocioeconomicaService = infoSocioeconomicaService;
        this.infoSocioeconomicaRepository = infoSocioeconomicaRepository;
    }

    /**
     * {@code POST  /info-socioeconomicas} : Create a new infoSocioeconomica.
     *
     * @param infoSocioeconomicaDTO the infoSocioeconomicaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infoSocioeconomicaDTO, or with status {@code 400 (Bad Request)} if the infoSocioeconomica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InfoSocioeconomicaDTO>> createInfoSocioeconomica(
        @Valid @RequestBody InfoSocioeconomicaDTO infoSocioeconomicaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save InfoSocioeconomica : {}", infoSocioeconomicaDTO);
        if (infoSocioeconomicaDTO.getId() != null) {
            throw new BadRequestAlertException("A new infoSocioeconomica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return infoSocioeconomicaService
            .save(infoSocioeconomicaDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/info-socioeconomicas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /info-socioeconomicas/:id} : Updates an existing infoSocioeconomica.
     *
     * @param id the id of the infoSocioeconomicaDTO to save.
     * @param infoSocioeconomicaDTO the infoSocioeconomicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infoSocioeconomicaDTO,
     * or with status {@code 400 (Bad Request)} if the infoSocioeconomicaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infoSocioeconomicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<InfoSocioeconomicaDTO>> updateInfoSocioeconomica(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InfoSocioeconomicaDTO infoSocioeconomicaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InfoSocioeconomica : {}, {}", id, infoSocioeconomicaDTO);
        if (infoSocioeconomicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infoSocioeconomicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return infoSocioeconomicaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return infoSocioeconomicaService
                    .update(infoSocioeconomicaDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /info-socioeconomicas/:id} : Partial updates given fields of an existing infoSocioeconomica, field will ignore if it is null
     *
     * @param id the id of the infoSocioeconomicaDTO to save.
     * @param infoSocioeconomicaDTO the infoSocioeconomicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infoSocioeconomicaDTO,
     * or with status {@code 400 (Bad Request)} if the infoSocioeconomicaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the infoSocioeconomicaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the infoSocioeconomicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InfoSocioeconomicaDTO>> partialUpdateInfoSocioeconomica(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InfoSocioeconomicaDTO infoSocioeconomicaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InfoSocioeconomica partially : {}, {}", id, infoSocioeconomicaDTO);
        if (infoSocioeconomicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infoSocioeconomicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return infoSocioeconomicaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InfoSocioeconomicaDTO> result = infoSocioeconomicaService.partialUpdate(infoSocioeconomicaDTO);

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
     * {@code GET  /info-socioeconomicas} : get all the infoSocioeconomicas.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infoSocioeconomicas in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<InfoSocioeconomicaDTO>> getAllInfoSocioeconomicas(@RequestParam(name = "filter", required = false) String filter) {
        if ("paciente-is-null".equals(filter)) {
            LOG.debug("REST request to get all InfoSocioeconomicas where paciente is null");
            return infoSocioeconomicaService.findAllWherePacienteIsNull().collectList();
        }
        LOG.debug("REST request to get all InfoSocioeconomicas");
        return infoSocioeconomicaService.findAll().collectList();
    }

    /**
     * {@code GET  /info-socioeconomicas} : get all the infoSocioeconomicas as a stream.
     * @return the {@link Flux} of infoSocioeconomicas.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<InfoSocioeconomicaDTO> getAllInfoSocioeconomicasAsStream() {
        LOG.debug("REST request to get all InfoSocioeconomicas as a stream");
        return infoSocioeconomicaService.findAll();
    }

    /**
     * {@code GET  /info-socioeconomicas/:id} : get the "id" infoSocioeconomica.
     *
     * @param id the id of the infoSocioeconomicaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infoSocioeconomicaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InfoSocioeconomicaDTO>> getInfoSocioeconomica(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InfoSocioeconomica : {}", id);
        Mono<InfoSocioeconomicaDTO> infoSocioeconomicaDTO = infoSocioeconomicaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infoSocioeconomicaDTO);
    }

    /**
     * {@code DELETE  /info-socioeconomicas/:id} : delete the "id" infoSocioeconomica.
     *
     * @param id the id of the infoSocioeconomicaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInfoSocioeconomica(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InfoSocioeconomica : {}", id);
        return infoSocioeconomicaService
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
