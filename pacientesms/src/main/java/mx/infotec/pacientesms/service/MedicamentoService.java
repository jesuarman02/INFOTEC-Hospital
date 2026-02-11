package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.MedicamentoDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.Medicamento}.
 */
public interface MedicamentoService {
    /**
     * Save a medicamento.
     *
     * @param medicamentoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MedicamentoDTO> save(MedicamentoDTO medicamentoDTO);

    /**
     * Updates a medicamento.
     *
     * @param medicamentoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MedicamentoDTO> update(MedicamentoDTO medicamentoDTO);

    /**
     * Partially updates a medicamento.
     *
     * @param medicamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MedicamentoDTO> partialUpdate(MedicamentoDTO medicamentoDTO);

    /**
     * Get all the medicamentos.
     *
     * @return the list of entities.
     */
    Flux<MedicamentoDTO> findAll();

    /**
     * Returns the number of medicamentos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" medicamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MedicamentoDTO> findOne(Long id);

    /**
     * Delete the "id" medicamento.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
