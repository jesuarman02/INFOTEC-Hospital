package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.InfoSocioeconomicaDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.InfoSocioeconomica}.
 */
public interface InfoSocioeconomicaService {
    /**
     * Save a infoSocioeconomica.
     *
     * @param infoSocioeconomicaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InfoSocioeconomicaDTO> save(InfoSocioeconomicaDTO infoSocioeconomicaDTO);

    /**
     * Updates a infoSocioeconomica.
     *
     * @param infoSocioeconomicaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InfoSocioeconomicaDTO> update(InfoSocioeconomicaDTO infoSocioeconomicaDTO);

    /**
     * Partially updates a infoSocioeconomica.
     *
     * @param infoSocioeconomicaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InfoSocioeconomicaDTO> partialUpdate(InfoSocioeconomicaDTO infoSocioeconomicaDTO);

    /**
     * Get all the infoSocioeconomicas.
     *
     * @return the list of entities.
     */
    Flux<InfoSocioeconomicaDTO> findAll();

    /**
     * Get all the InfoSocioeconomicaDTO where Paciente is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<InfoSocioeconomicaDTO> findAllWherePacienteIsNull();

    /**
     * Returns the number of infoSocioeconomicas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" infoSocioeconomica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InfoSocioeconomicaDTO> findOne(Long id);

    /**
     * Delete the "id" infoSocioeconomica.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
