package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.PacienteMedicamentoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.PacienteMedicamento}.
 */
public interface PacienteMedicamentoService {
    /**
     * Save a pacienteMedicamento.
     *
     * @param pacienteMedicamentoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PacienteMedicamentoDTO> save(PacienteMedicamentoDTO pacienteMedicamentoDTO);

    /**
     * Updates a pacienteMedicamento.
     *
     * @param pacienteMedicamentoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PacienteMedicamentoDTO> update(PacienteMedicamentoDTO pacienteMedicamentoDTO);

    /**
     * Partially updates a pacienteMedicamento.
     *
     * @param pacienteMedicamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PacienteMedicamentoDTO> partialUpdate(PacienteMedicamentoDTO pacienteMedicamentoDTO);

    /**
     * Get all the pacienteMedicamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PacienteMedicamentoDTO> findAll(Pageable pageable);

    /**
     * Get all the pacienteMedicamentos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PacienteMedicamentoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of pacienteMedicamentos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" pacienteMedicamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PacienteMedicamentoDTO> findOne(Long id);

    /**
     * Delete the "id" pacienteMedicamento.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
