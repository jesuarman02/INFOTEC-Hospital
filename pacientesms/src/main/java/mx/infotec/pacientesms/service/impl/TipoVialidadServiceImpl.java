package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.TipoVialidadRepository;
import mx.infotec.pacientesms.service.TipoVialidadService;
import mx.infotec.pacientesms.service.dto.TipoVialidadDTO;
import mx.infotec.pacientesms.service.mapper.TipoVialidadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.TipoVialidad}.
 */
@Service
@Transactional
public class TipoVialidadServiceImpl implements TipoVialidadService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoVialidadServiceImpl.class);

    private final TipoVialidadRepository tipoVialidadRepository;

    private final TipoVialidadMapper tipoVialidadMapper;

    public TipoVialidadServiceImpl(TipoVialidadRepository tipoVialidadRepository, TipoVialidadMapper tipoVialidadMapper) {
        this.tipoVialidadRepository = tipoVialidadRepository;
        this.tipoVialidadMapper = tipoVialidadMapper;
    }

    @Override
    public Mono<TipoVialidadDTO> save(TipoVialidadDTO tipoVialidadDTO) {
        LOG.debug("Request to save TipoVialidad : {}", tipoVialidadDTO);
        return tipoVialidadRepository.save(tipoVialidadMapper.toEntity(tipoVialidadDTO)).map(tipoVialidadMapper::toDto);
    }

    @Override
    public Mono<TipoVialidadDTO> update(TipoVialidadDTO tipoVialidadDTO) {
        LOG.debug("Request to update TipoVialidad : {}", tipoVialidadDTO);
        return tipoVialidadRepository.save(tipoVialidadMapper.toEntity(tipoVialidadDTO)).map(tipoVialidadMapper::toDto);
    }

    @Override
    public Mono<TipoVialidadDTO> partialUpdate(TipoVialidadDTO tipoVialidadDTO) {
        LOG.debug("Request to partially update TipoVialidad : {}", tipoVialidadDTO);

        return tipoVialidadRepository
            .findById(tipoVialidadDTO.getId())
            .map(existingTipoVialidad -> {
                tipoVialidadMapper.partialUpdate(existingTipoVialidad, tipoVialidadDTO);

                return existingTipoVialidad;
            })
            .flatMap(tipoVialidadRepository::save)
            .map(tipoVialidadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TipoVialidadDTO> findAll() {
        LOG.debug("Request to get all TipoVialidads");
        return tipoVialidadRepository.findAll().map(tipoVialidadMapper::toDto);
    }

    public Mono<Long> countAll() {
        return tipoVialidadRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TipoVialidadDTO> findOne(Long id) {
        LOG.debug("Request to get TipoVialidad : {}", id);
        return tipoVialidadRepository.findById(id).map(tipoVialidadMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete TipoVialidad : {}", id);
        return tipoVialidadRepository.deleteById(id);
    }
}
