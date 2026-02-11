package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.EnfermedadDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.Enfermedad}.
 */
public interface EnfermedadService {
    /**
     * Save a enfermedad.
     *
     * @param enfermedadDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EnfermedadDTO> save(EnfermedadDTO enfermedadDTO);

    /**
     * Updates a enfermedad.
     *
     * @param enfermedadDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EnfermedadDTO> update(EnfermedadDTO enfermedadDTO);

    /**
     * Partially updates a enfermedad.
     *
     * @param enfermedadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EnfermedadDTO> partialUpdate(EnfermedadDTO enfermedadDTO);

    /**
     * Get all the enfermedads.
     *
     * @return the list of entities.
     */
    Flux<EnfermedadDTO> findAll();

    /**
     * Returns the number of enfermedads available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" enfermedad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EnfermedadDTO> findOne(Long id);

    /**
     * Delete the "id" enfermedad.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
