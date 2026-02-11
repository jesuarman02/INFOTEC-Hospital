package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.PacienteAlergiaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.PacienteAlergia}.
 */
public interface PacienteAlergiaService {
    /**
     * Save a pacienteAlergia.
     *
     * @param pacienteAlergiaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PacienteAlergiaDTO> save(PacienteAlergiaDTO pacienteAlergiaDTO);

    /**
     * Updates a pacienteAlergia.
     *
     * @param pacienteAlergiaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PacienteAlergiaDTO> update(PacienteAlergiaDTO pacienteAlergiaDTO);

    /**
     * Partially updates a pacienteAlergia.
     *
     * @param pacienteAlergiaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PacienteAlergiaDTO> partialUpdate(PacienteAlergiaDTO pacienteAlergiaDTO);

    /**
     * Get all the pacienteAlergias.
     *
     * @return the list of entities.
     */
    Flux<PacienteAlergiaDTO> findAll();

    /**
     * Get all the pacienteAlergias with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PacienteAlergiaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of pacienteAlergias available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" pacienteAlergia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PacienteAlergiaDTO> findOne(Long id);

    /**
     * Delete the "id" pacienteAlergia.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
