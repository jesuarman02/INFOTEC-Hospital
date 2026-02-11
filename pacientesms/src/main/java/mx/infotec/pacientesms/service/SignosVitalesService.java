package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.SignosVitalesDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.SignosVitales}.
 */
public interface SignosVitalesService {
    /**
     * Save a signosVitales.
     *
     * @param signosVitalesDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SignosVitalesDTO> save(SignosVitalesDTO signosVitalesDTO);

    /**
     * Updates a signosVitales.
     *
     * @param signosVitalesDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SignosVitalesDTO> update(SignosVitalesDTO signosVitalesDTO);

    /**
     * Partially updates a signosVitales.
     *
     * @param signosVitalesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SignosVitalesDTO> partialUpdate(SignosVitalesDTO signosVitalesDTO);

    /**
     * Get all the signosVitales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SignosVitalesDTO> findAll(Pageable pageable);

    /**
     * Returns the number of signosVitales available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" signosVitales.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SignosVitalesDTO> findOne(Long id);

    /**
     * Delete the "id" signosVitales.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
