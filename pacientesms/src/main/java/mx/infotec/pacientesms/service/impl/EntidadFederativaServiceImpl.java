package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.EntidadFederativaRepository;
import mx.infotec.pacientesms.service.EntidadFederativaService;
import mx.infotec.pacientesms.service.dto.EntidadFederativaDTO;
import mx.infotec.pacientesms.service.mapper.EntidadFederativaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.EntidadFederativa}.
 */
@Service
@Transactional
public class EntidadFederativaServiceImpl implements EntidadFederativaService {

    private static final Logger LOG = LoggerFactory.getLogger(EntidadFederativaServiceImpl.class);

    private final EntidadFederativaRepository entidadFederativaRepository;

    private final EntidadFederativaMapper entidadFederativaMapper;

    public EntidadFederativaServiceImpl(
        EntidadFederativaRepository entidadFederativaRepository,
        EntidadFederativaMapper entidadFederativaMapper
    ) {
        this.entidadFederativaRepository = entidadFederativaRepository;
        this.entidadFederativaMapper = entidadFederativaMapper;
    }

    @Override
    public Mono<EntidadFederativaDTO> save(EntidadFederativaDTO entidadFederativaDTO) {
        LOG.debug("Request to save EntidadFederativa : {}", entidadFederativaDTO);
        return entidadFederativaRepository.save(entidadFederativaMapper.toEntity(entidadFederativaDTO)).map(entidadFederativaMapper::toDto);
    }

    @Override
    public Mono<EntidadFederativaDTO> update(EntidadFederativaDTO entidadFederativaDTO) {
        LOG.debug("Request to update EntidadFederativa : {}", entidadFederativaDTO);
        return entidadFederativaRepository.save(entidadFederativaMapper.toEntity(entidadFederativaDTO)).map(entidadFederativaMapper::toDto);
    }

    @Override
    public Mono<EntidadFederativaDTO> partialUpdate(EntidadFederativaDTO entidadFederativaDTO) {
        LOG.debug("Request to partially update EntidadFederativa : {}", entidadFederativaDTO);

        return entidadFederativaRepository
            .findById(entidadFederativaDTO.getId())
            .map(existingEntidadFederativa -> {
                entidadFederativaMapper.partialUpdate(existingEntidadFederativa, entidadFederativaDTO);

                return existingEntidadFederativa;
            })
            .flatMap(entidadFederativaRepository::save)
            .map(entidadFederativaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EntidadFederativaDTO> findAll() {
        LOG.debug("Request to get all EntidadFederativas");
        return entidadFederativaRepository.findAll().map(entidadFederativaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return entidadFederativaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EntidadFederativaDTO> findOne(Long id) {
        LOG.debug("Request to get EntidadFederativa : {}", id);
        return entidadFederativaRepository.findById(id).map(entidadFederativaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete EntidadFederativa : {}", id);
        return entidadFederativaRepository.deleteById(id);
    }
}
