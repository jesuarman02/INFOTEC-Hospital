package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.EnfermedadRepository;
import mx.infotec.pacientesms.service.EnfermedadService;
import mx.infotec.pacientesms.service.dto.EnfermedadDTO;
import mx.infotec.pacientesms.service.mapper.EnfermedadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.Enfermedad}.
 */
@Service
@Transactional
public class EnfermedadServiceImpl implements EnfermedadService {

    private static final Logger LOG = LoggerFactory.getLogger(EnfermedadServiceImpl.class);

    private final EnfermedadRepository enfermedadRepository;

    private final EnfermedadMapper enfermedadMapper;

    public EnfermedadServiceImpl(EnfermedadRepository enfermedadRepository, EnfermedadMapper enfermedadMapper) {
        this.enfermedadRepository = enfermedadRepository;
        this.enfermedadMapper = enfermedadMapper;
    }

    @Override
    public Mono<EnfermedadDTO> save(EnfermedadDTO enfermedadDTO) {
        LOG.debug("Request to save Enfermedad : {}", enfermedadDTO);
        return enfermedadRepository.save(enfermedadMapper.toEntity(enfermedadDTO)).map(enfermedadMapper::toDto);
    }

    @Override
    public Mono<EnfermedadDTO> update(EnfermedadDTO enfermedadDTO) {
        LOG.debug("Request to update Enfermedad : {}", enfermedadDTO);
        return enfermedadRepository.save(enfermedadMapper.toEntity(enfermedadDTO)).map(enfermedadMapper::toDto);
    }

    @Override
    public Mono<EnfermedadDTO> partialUpdate(EnfermedadDTO enfermedadDTO) {
        LOG.debug("Request to partially update Enfermedad : {}", enfermedadDTO);

        return enfermedadRepository
            .findById(enfermedadDTO.getId())
            .map(existingEnfermedad -> {
                enfermedadMapper.partialUpdate(existingEnfermedad, enfermedadDTO);

                return existingEnfermedad;
            })
            .flatMap(enfermedadRepository::save)
            .map(enfermedadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EnfermedadDTO> findAll() {
        LOG.debug("Request to get all Enfermedads");
        return enfermedadRepository.findAll().map(enfermedadMapper::toDto);
    }

    public Mono<Long> countAll() {
        return enfermedadRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EnfermedadDTO> findOne(Long id) {
        LOG.debug("Request to get Enfermedad : {}", id);
        return enfermedadRepository.findById(id).map(enfermedadMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Enfermedad : {}", id);
        return enfermedadRepository.deleteById(id);
    }
}
