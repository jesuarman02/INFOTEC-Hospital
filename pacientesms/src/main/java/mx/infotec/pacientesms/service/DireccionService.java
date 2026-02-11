package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.DireccionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.Direccion}.
 */
public interface DireccionService {
    /**
     * Save a direccion.
     *
     * @param direccionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DireccionDTO> save(DireccionDTO direccionDTO);

    /**
     * Updates a direccion.
     *
     * @param direccionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DireccionDTO> update(DireccionDTO direccionDTO);

    /**
     * Partially updates a direccion.
     *
     * @param direccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DireccionDTO> partialUpdate(DireccionDTO direccionDTO);

    /**
     * Get all the direccions.
     *
     * @return the list of entities.
     */
    Flux<DireccionDTO> findAll();

    /**
     * Get all the DireccionDTO where Paciente is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<DireccionDTO> findAllWherePacienteIsNull();

    /**
     * Get all the direccions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DireccionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of direccions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" direccion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DireccionDTO> findOne(Long id);

    /**
     * Delete the "id" direccion.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
