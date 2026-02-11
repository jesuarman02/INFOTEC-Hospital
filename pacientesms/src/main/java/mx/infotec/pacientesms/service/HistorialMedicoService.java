package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.HistorialMedicoDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.HistorialMedico}.
 */
public interface HistorialMedicoService {
    /**
     * Save a historialMedico.
     *
     * @param historialMedicoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HistorialMedicoDTO> save(HistorialMedicoDTO historialMedicoDTO);

    /**
     * Updates a historialMedico.
     *
     * @param historialMedicoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<HistorialMedicoDTO> update(HistorialMedicoDTO historialMedicoDTO);

    /**
     * Partially updates a historialMedico.
     *
     * @param historialMedicoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HistorialMedicoDTO> partialUpdate(HistorialMedicoDTO historialMedicoDTO);

    /**
     * Get all the historialMedicos.
     *
     * @return the list of entities.
     */
    Flux<HistorialMedicoDTO> findAll();

    /**
     * Get all the HistorialMedicoDTO where Paciente is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<HistorialMedicoDTO> findAllWherePacienteIsNull();

    /**
     * Returns the number of historialMedicos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" historialMedico.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HistorialMedicoDTO> findOne(Long id);

    /**
     * Delete the "id" historialMedico.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
