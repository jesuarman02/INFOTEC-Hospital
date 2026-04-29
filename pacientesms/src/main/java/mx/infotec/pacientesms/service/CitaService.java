package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.domain.Cita;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.Cita}.
 */
public interface CitaService {
    /**
     * Save a cita.
     *
     * @param cita the entity to save.
     * @return the persisted entity.
     */
    Mono<Cita> save(Cita cita);

    /**
     * Updates a cita.
     *
     * @param cita the entity to update.
     * @return the persisted entity.
     */
    Mono<Cita> update(Cita cita);

    /**
     * Partially updates a cita.
     *
     * @param cita the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Cita> partialUpdate(Cita cita);

    /**
     * Get all the citas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<Cita> findAll(Pageable pageable);

    /**
     * Returns the number of citas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" cita.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Cita> findOne(Long id);

    /**
     * Delete the "id" cita.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
