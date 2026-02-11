package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.TipoVialidadDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.TipoVialidad}.
 */
public interface TipoVialidadService {
    /**
     * Save a tipoVialidad.
     *
     * @param tipoVialidadDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TipoVialidadDTO> save(TipoVialidadDTO tipoVialidadDTO);

    /**
     * Updates a tipoVialidad.
     *
     * @param tipoVialidadDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TipoVialidadDTO> update(TipoVialidadDTO tipoVialidadDTO);

    /**
     * Partially updates a tipoVialidad.
     *
     * @param tipoVialidadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TipoVialidadDTO> partialUpdate(TipoVialidadDTO tipoVialidadDTO);

    /**
     * Get all the tipoVialidads.
     *
     * @return the list of entities.
     */
    Flux<TipoVialidadDTO> findAll();

    /**
     * Returns the number of tipoVialidads available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" tipoVialidad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TipoVialidadDTO> findOne(Long id);

    /**
     * Delete the "id" tipoVialidad.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
