package mx.infotec.pacientesms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import mx.infotec.pacientesms.repository.CodigoPostalRepository;
import mx.infotec.pacientesms.service.CodigoPostalService;
import mx.infotec.pacientesms.service.dto.CodigoPostalDTO;
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
 * REST controller for managing {@link mx.infotec.pacientesms.domain.CodigoPostal}.
 */
@RestController
@RequestMapping("/api/codigo-postals")
public class CodigoPostalResource {

    private static final Logger LOG = LoggerFactory.getLogger(CodigoPostalResource.class);

    private static final String ENTITY_NAME = "pacientesmsCodigoPostal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodigoPostalService codigoPostalService;

    private final CodigoPostalRepository codigoPostalRepository;

    public CodigoPostalResource(CodigoPostalService codigoPostalService, CodigoPostalRepository codigoPostalRepository) {
        this.codigoPostalService = codigoPostalService;
        this.codigoPostalRepository = codigoPostalRepository;
    }

    /**
     * {@code POST  /codigo-postals} : Create a new codigoPostal.
     *
     * @param codigoPostalDTO the codigoPostalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codigoPostalDTO, or with status {@code 400 (Bad Request)} if the codigoPostal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CodigoPostalDTO>> createCodigoPostal(@Valid @RequestBody CodigoPostalDTO codigoPostalDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CodigoPostal : {}", codigoPostalDTO);
        if (codigoPostalDTO.getId() != null) {
            throw new BadRequestAlertException("A new codigoPostal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return codigoPostalService
            .save(codigoPostalDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/codigo-postals/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /codigo-postals/:id} : Updates an existing codigoPostal.
     *
     * @param id the id of the codigoPostalDTO to save.
     * @param codigoPostalDTO the codigoPostalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codigoPostalDTO,
     * or with status {@code 400 (Bad Request)} if the codigoPostalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codigoPostalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CodigoPostalDTO>> updateCodigoPostal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CodigoPostalDTO codigoPostalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CodigoPostal : {}, {}", id, codigoPostalDTO);
        if (codigoPostalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codigoPostalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return codigoPostalRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return codigoPostalService
                    .update(codigoPostalDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /codigo-postals/:id} : Partial updates given fields of an existing codigoPostal, field will ignore if it is null
     *
     * @param id the id of the codigoPostalDTO to save.
     * @param codigoPostalDTO the codigoPostalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codigoPostalDTO,
     * or with status {@code 400 (Bad Request)} if the codigoPostalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the codigoPostalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the codigoPostalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CodigoPostalDTO>> partialUpdateCodigoPostal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CodigoPostalDTO codigoPostalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CodigoPostal partially : {}, {}", id, codigoPostalDTO);
        if (codigoPostalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codigoPostalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return codigoPostalRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CodigoPostalDTO> result = codigoPostalService.partialUpdate(codigoPostalDTO);

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
     * {@code GET  /codigo-postals} : get all the codigoPostals.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codigoPostals in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CodigoPostalDTO>>> getAllCodigoPostals(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of CodigoPostals");
        return codigoPostalService
            .countAll()
            .zipWith(codigoPostalService.findAll(pageable).collectList())
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
     * @code GET  /bycp/{codigo} : get all the codigoPostals by codigo.
     * @param codigo codigo parameter
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codigoPostals in body.
     */
    // 1. Cambiamos {dCode} por {codigo} para que coincida
    @GetMapping(value = "/bycp/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CodigoPostalDTO>>> getByCodigo(
        // 2. Descomentamos y aseguramos que el PathVariable sea "codigo"
        @PathVariable("codigo") String codigo, 
        // 3. Descomentamos el Pageable
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of CodigoPostals by codigo: {}", codigo);
        
        return codigoPostalService
            .countAll()
            // Aquí ya funcionará porque 'codigo' y 'pageable' ya existen como parámetros
            .zipWith(codigoPostalService.findByCodigo(codigo, pageable).collectList())            
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
     * {@code GET  /codigo-postals/:id} : get the "id" codigoPostal.
     *
     * @param id the id of the codigoPostalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codigoPostalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CodigoPostalDTO>> getCodigoPostal(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CodigoPostal : {}", id);
        Mono<CodigoPostalDTO> codigoPostalDTO = codigoPostalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(codigoPostalDTO);
    }

    /**
     * {@code DELETE  /codigo-postals/:id} : delete the "id" codigoPostal.
     *
     * @param id the id of the codigoPostalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCodigoPostal(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CodigoPostal : {}", id);
        return codigoPostalService
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
