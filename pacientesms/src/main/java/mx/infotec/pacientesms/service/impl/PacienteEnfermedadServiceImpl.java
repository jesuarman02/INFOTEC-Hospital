package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.PacienteEnfermedadRepository;
import mx.infotec.pacientesms.service.PacienteEnfermedadService;
import mx.infotec.pacientesms.service.dto.PacienteEnfermedadDTO;
import mx.infotec.pacientesms.service.mapper.PacienteEnfermedadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.PacienteEnfermedad}.
 */
@Service
@Transactional
public class PacienteEnfermedadServiceImpl implements PacienteEnfermedadService {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteEnfermedadServiceImpl.class);

    private final PacienteEnfermedadRepository pacienteEnfermedadRepository;

    private final PacienteEnfermedadMapper pacienteEnfermedadMapper;

    public PacienteEnfermedadServiceImpl(
        PacienteEnfermedadRepository pacienteEnfermedadRepository,
        PacienteEnfermedadMapper pacienteEnfermedadMapper
    ) {
        this.pacienteEnfermedadRepository = pacienteEnfermedadRepository;
        this.pacienteEnfermedadMapper = pacienteEnfermedadMapper;
    }

    @Override
    public Mono<PacienteEnfermedadDTO> save(PacienteEnfermedadDTO pacienteEnfermedadDTO) {
        LOG.debug("Request to save PacienteEnfermedad : {}", pacienteEnfermedadDTO);
        return pacienteEnfermedadRepository
            .save(pacienteEnfermedadMapper.toEntity(pacienteEnfermedadDTO))
            .map(pacienteEnfermedadMapper::toDto);
    }

    @Override
    public Mono<PacienteEnfermedadDTO> update(PacienteEnfermedadDTO pacienteEnfermedadDTO) {
        LOG.debug("Request to update PacienteEnfermedad : {}", pacienteEnfermedadDTO);
        return pacienteEnfermedadRepository
            .save(pacienteEnfermedadMapper.toEntity(pacienteEnfermedadDTO))
            .map(pacienteEnfermedadMapper::toDto);
    }

    @Override
    public Mono<PacienteEnfermedadDTO> partialUpdate(PacienteEnfermedadDTO pacienteEnfermedadDTO) {
        LOG.debug("Request to partially update PacienteEnfermedad : {}", pacienteEnfermedadDTO);

        return pacienteEnfermedadRepository
            .findById(pacienteEnfermedadDTO.getId())
            .map(existingPacienteEnfermedad -> {
                pacienteEnfermedadMapper.partialUpdate(existingPacienteEnfermedad, pacienteEnfermedadDTO);

                return existingPacienteEnfermedad;
            })
            .flatMap(pacienteEnfermedadRepository::save)
            .map(pacienteEnfermedadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PacienteEnfermedadDTO> findAll() {
        LOG.debug("Request to get all PacienteEnfermedads");
        return pacienteEnfermedadRepository.findAll().map(pacienteEnfermedadMapper::toDto);
    }

    public Flux<PacienteEnfermedadDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pacienteEnfermedadRepository.findAllWithEagerRelationships(pageable).map(pacienteEnfermedadMapper::toDto);
    }

    public Mono<Long> countAll() {
        return pacienteEnfermedadRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PacienteEnfermedadDTO> findOne(Long id) {
        LOG.debug("Request to get PacienteEnfermedad : {}", id);
        return pacienteEnfermedadRepository.findOneWithEagerRelationships(id).map(pacienteEnfermedadMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete PacienteEnfermedad : {}", id);
        return pacienteEnfermedadRepository.deleteById(id);
    }
}
