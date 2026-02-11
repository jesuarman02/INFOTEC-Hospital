package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.PacienteAlergiaRepository;
import mx.infotec.pacientesms.service.PacienteAlergiaService;
import mx.infotec.pacientesms.service.dto.PacienteAlergiaDTO;
import mx.infotec.pacientesms.service.mapper.PacienteAlergiaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.PacienteAlergia}.
 */
@Service
@Transactional
public class PacienteAlergiaServiceImpl implements PacienteAlergiaService {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteAlergiaServiceImpl.class);

    private final PacienteAlergiaRepository pacienteAlergiaRepository;

    private final PacienteAlergiaMapper pacienteAlergiaMapper;

    public PacienteAlergiaServiceImpl(PacienteAlergiaRepository pacienteAlergiaRepository, PacienteAlergiaMapper pacienteAlergiaMapper) {
        this.pacienteAlergiaRepository = pacienteAlergiaRepository;
        this.pacienteAlergiaMapper = pacienteAlergiaMapper;
    }

    @Override
    public Mono<PacienteAlergiaDTO> save(PacienteAlergiaDTO pacienteAlergiaDTO) {
        LOG.debug("Request to save PacienteAlergia : {}", pacienteAlergiaDTO);
        return pacienteAlergiaRepository.save(pacienteAlergiaMapper.toEntity(pacienteAlergiaDTO)).map(pacienteAlergiaMapper::toDto);
    }

    @Override
    public Mono<PacienteAlergiaDTO> update(PacienteAlergiaDTO pacienteAlergiaDTO) {
        LOG.debug("Request to update PacienteAlergia : {}", pacienteAlergiaDTO);
        return pacienteAlergiaRepository.save(pacienteAlergiaMapper.toEntity(pacienteAlergiaDTO)).map(pacienteAlergiaMapper::toDto);
    }

    @Override
    public Mono<PacienteAlergiaDTO> partialUpdate(PacienteAlergiaDTO pacienteAlergiaDTO) {
        LOG.debug("Request to partially update PacienteAlergia : {}", pacienteAlergiaDTO);

        return pacienteAlergiaRepository
            .findById(pacienteAlergiaDTO.getId())
            .map(existingPacienteAlergia -> {
                pacienteAlergiaMapper.partialUpdate(existingPacienteAlergia, pacienteAlergiaDTO);

                return existingPacienteAlergia;
            })
            .flatMap(pacienteAlergiaRepository::save)
            .map(pacienteAlergiaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PacienteAlergiaDTO> findAll() {
        LOG.debug("Request to get all PacienteAlergias");
        return pacienteAlergiaRepository.findAll().map(pacienteAlergiaMapper::toDto);
    }

    public Flux<PacienteAlergiaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pacienteAlergiaRepository.findAllWithEagerRelationships(pageable).map(pacienteAlergiaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return pacienteAlergiaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PacienteAlergiaDTO> findOne(Long id) {
        LOG.debug("Request to get PacienteAlergia : {}", id);
        return pacienteAlergiaRepository.findOneWithEagerRelationships(id).map(pacienteAlergiaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete PacienteAlergia : {}", id);
        return pacienteAlergiaRepository.deleteById(id);
    }
}
