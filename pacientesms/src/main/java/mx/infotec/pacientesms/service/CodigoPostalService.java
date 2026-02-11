package mx.infotec.pacientesms.service;

import mx.infotec.pacientesms.service.dto.CodigoPostalDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link mx.infotec.pacientesms.domain.CodigoPostal}.
 */
public interface CodigoPostalService {
    /**
     * Save a codigoPostal.
     *
     * @param codigoPostalDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CodigoPostalDTO> save(CodigoPostalDTO codigoPostalDTO);

    /**
     * Updates a codigoPostal.
     *
     * @param codigoPostalDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CodigoPostalDTO> update(CodigoPostalDTO codigoPostalDTO);

    /**
     * Partially updates a codigoPostal.
     *
     * @param codigoPostalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CodigoPostalDTO> partialUpdate(CodigoPostalDTO codigoPostalDTO);

    /**
     * Get all the codigoPostals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CodigoPostalDTO> findAll(Pageable pageable);

    /**
     * Get the postal code 
     * @param dCodigo  dCodigo
     * @param pageable the pagination information.
     * @return
     */
    Flux<CodigoPostalDTO> findByCodigo(String codigo, Pageable pageable);

    /**
     * Returns the number of codigoPostals available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" codigoPostal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CodigoPostalDTO> findOne(Long id);

    /**
     * Delete the "id" codigoPostal.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
