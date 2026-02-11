package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.PacienteMedicamentoRepository;
import mx.infotec.pacientesms.service.PacienteMedicamentoService;
import mx.infotec.pacientesms.service.dto.PacienteMedicamentoDTO;
import mx.infotec.pacientesms.service.mapper.PacienteMedicamentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.PacienteMedicamento}.
 */
@Service
@Transactional
public class PacienteMedicamentoServiceImpl implements PacienteMedicamentoService {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteMedicamentoServiceImpl.class);

    private final PacienteMedicamentoRepository pacienteMedicamentoRepository;

    private final PacienteMedicamentoMapper pacienteMedicamentoMapper;

    public PacienteMedicamentoServiceImpl(
        PacienteMedicamentoRepository pacienteMedicamentoRepository,
        PacienteMedicamentoMapper pacienteMedicamentoMapper
    ) {
        this.pacienteMedicamentoRepository = pacienteMedicamentoRepository;
        this.pacienteMedicamentoMapper = pacienteMedicamentoMapper;
    }

    @Override
    public Mono<PacienteMedicamentoDTO> save(PacienteMedicamentoDTO pacienteMedicamentoDTO) {
        LOG.debug("Request to save PacienteMedicamento : {}", pacienteMedicamentoDTO);
        return pacienteMedicamentoRepository
            .save(pacienteMedicamentoMapper.toEntity(pacienteMedicamentoDTO))
            .map(pacienteMedicamentoMapper::toDto);
    }

    @Override
    public Mono<PacienteMedicamentoDTO> update(PacienteMedicamentoDTO pacienteMedicamentoDTO) {
        LOG.debug("Request to update PacienteMedicamento : {}", pacienteMedicamentoDTO);
        return pacienteMedicamentoRepository
            .save(pacienteMedicamentoMapper.toEntity(pacienteMedicamentoDTO))
            .map(pacienteMedicamentoMapper::toDto);
    }

    @Override
    public Mono<PacienteMedicamentoDTO> partialUpdate(PacienteMedicamentoDTO pacienteMedicamentoDTO) {
        LOG.debug("Request to partially update PacienteMedicamento : {}", pacienteMedicamentoDTO);

        return pacienteMedicamentoRepository
            .findById(pacienteMedicamentoDTO.getId())
            .map(existingPacienteMedicamento -> {
                pacienteMedicamentoMapper.partialUpdate(existingPacienteMedicamento, pacienteMedicamentoDTO);

                return existingPacienteMedicamento;
            })
            .flatMap(pacienteMedicamentoRepository::save)
            .map(pacienteMedicamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PacienteMedicamentoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PacienteMedicamentos");
        return pacienteMedicamentoRepository.findAllBy(pageable).map(pacienteMedicamentoMapper::toDto);
    }

    public Flux<PacienteMedicamentoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pacienteMedicamentoRepository.findAllWithEagerRelationships(pageable).map(pacienteMedicamentoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return pacienteMedicamentoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PacienteMedicamentoDTO> findOne(Long id) {
        LOG.debug("Request to get PacienteMedicamento : {}", id);
        return pacienteMedicamentoRepository.findOneWithEagerRelationships(id).map(pacienteMedicamentoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete PacienteMedicamento : {}", id);
        return pacienteMedicamentoRepository.deleteById(id);
    }
}
