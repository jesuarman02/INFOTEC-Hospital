package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.AlergiaRepository;
import mx.infotec.pacientesms.service.AlergiaService;
import mx.infotec.pacientesms.service.dto.AlergiaDTO;
import mx.infotec.pacientesms.service.mapper.AlergiaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.Alergia}.
 */
@Service
@Transactional
public class AlergiaServiceImpl implements AlergiaService {

    private static final Logger LOG = LoggerFactory.getLogger(AlergiaServiceImpl.class);

    private final AlergiaRepository alergiaRepository;

    private final AlergiaMapper alergiaMapper;

    public AlergiaServiceImpl(AlergiaRepository alergiaRepository, AlergiaMapper alergiaMapper) {
        this.alergiaRepository = alergiaRepository;
        this.alergiaMapper = alergiaMapper;
    }

    @Override
    public Mono<AlergiaDTO> save(AlergiaDTO alergiaDTO) {
        LOG.debug("Request to save Alergia : {}", alergiaDTO);
        return alergiaRepository.save(alergiaMapper.toEntity(alergiaDTO)).map(alergiaMapper::toDto);
    }

    @Override
    public Mono<AlergiaDTO> update(AlergiaDTO alergiaDTO) {
        LOG.debug("Request to update Alergia : {}", alergiaDTO);
        return alergiaRepository.save(alergiaMapper.toEntity(alergiaDTO)).map(alergiaMapper::toDto);
    }

    @Override
    public Mono<AlergiaDTO> partialUpdate(AlergiaDTO alergiaDTO) {
        LOG.debug("Request to partially update Alergia : {}", alergiaDTO);

        return alergiaRepository
            .findById(alergiaDTO.getId())
            .map(existingAlergia -> {
                alergiaMapper.partialUpdate(existingAlergia, alergiaDTO);

                return existingAlergia;
            })
            .flatMap(alergiaRepository::save)
            .map(alergiaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AlergiaDTO> findAll() {
        LOG.debug("Request to get all Alergias");
        return alergiaRepository.findAll().map(alergiaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return alergiaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AlergiaDTO> findOne(Long id) {
        LOG.debug("Request to get Alergia : {}", id);
        return alergiaRepository.findById(id).map(alergiaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Alergia : {}", id);
        return alergiaRepository.deleteById(id);
    }
}
