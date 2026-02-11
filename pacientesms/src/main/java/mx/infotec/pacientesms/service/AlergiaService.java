package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.AlergiaDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.Alergia}.
 */
public interface AlergiaService {
    /**
     * Save a alergia.
     *
     * @param alergiaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AlergiaDTO> save(AlergiaDTO alergiaDTO);

    /**
     * Updates a alergia.
     *
     * @param alergiaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AlergiaDTO> update(AlergiaDTO alergiaDTO);

    /**
     * Partially updates a alergia.
     *
     * @param alergiaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AlergiaDTO> partialUpdate(AlergiaDTO alergiaDTO);

    /**
     * Get all the alergias.
     *
     * @return the list of entities.
     */
    Flux<AlergiaDTO> findAll();

    /**
     * Returns the number of alergias available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" alergia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AlergiaDTO> findOne(Long id);

    /**
     * Delete the "id" alergia.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
