package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.EntidadFederativaDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.EntidadFederativa}.
 */
public interface EntidadFederativaService {
    /**
     * Save a entidadFederativa.
     *
     * @param entidadFederativaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EntidadFederativaDTO> save(EntidadFederativaDTO entidadFederativaDTO);

    /**
     * Updates a entidadFederativa.
     *
     * @param entidadFederativaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EntidadFederativaDTO> update(EntidadFederativaDTO entidadFederativaDTO);

    /**
     * Partially updates a entidadFederativa.
     *
     * @param entidadFederativaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EntidadFederativaDTO> partialUpdate(EntidadFederativaDTO entidadFederativaDTO);

    /**
     * Get all the entidadFederativas.
     *
     * @return the list of entities.
     */
    Flux<EntidadFederativaDTO> findAll();

    /**
     * Returns the number of entidadFederativas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" entidadFederativa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EntidadFederativaDTO> findOne(Long id);

    /**
     * Delete the "id" entidadFederativa.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
