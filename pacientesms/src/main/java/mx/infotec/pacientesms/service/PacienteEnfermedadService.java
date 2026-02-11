package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.PacienteEnfermedadDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.PacienteEnfermedad}.
 */
public interface PacienteEnfermedadService {
    /**
     * Save a pacienteEnfermedad.
     *
     * @param pacienteEnfermedadDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PacienteEnfermedadDTO> save(PacienteEnfermedadDTO pacienteEnfermedadDTO);

    /**
     * Updates a pacienteEnfermedad.
     *
     * @param pacienteEnfermedadDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PacienteEnfermedadDTO> update(PacienteEnfermedadDTO pacienteEnfermedadDTO);

    /**
     * Partially updates a pacienteEnfermedad.
     *
     * @param pacienteEnfermedadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PacienteEnfermedadDTO> partialUpdate(PacienteEnfermedadDTO pacienteEnfermedadDTO);

    /**
     * Get all the pacienteEnfermedads.
     *
     * @return the list of entities.
     */
    Flux<PacienteEnfermedadDTO> findAll();

    /**
     * Get all the pacienteEnfermedads with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PacienteEnfermedadDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of pacienteEnfermedads available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" pacienteEnfermedad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PacienteEnfermedadDTO> findOne(Long id);

    /**
     * Delete the "id" pacienteEnfermedad.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
