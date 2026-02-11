package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.HistorialMedicoRepository;
import mx.infotec.pacientesms.service.HistorialMedicoService;
import mx.infotec.pacientesms.service.dto.HistorialMedicoDTO;
import mx.infotec.pacientesms.service.mapper.HistorialMedicoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.HistorialMedico}.
 */
@Service
@Transactional
public class HistorialMedicoServiceImpl implements HistorialMedicoService {

    private static final Logger LOG = LoggerFactory.getLogger(HistorialMedicoServiceImpl.class);

    private final HistorialMedicoRepository historialMedicoRepository;

    private final HistorialMedicoMapper historialMedicoMapper;

    public HistorialMedicoServiceImpl(HistorialMedicoRepository historialMedicoRepository, HistorialMedicoMapper historialMedicoMapper) {
        this.historialMedicoRepository = historialMedicoRepository;
        this.historialMedicoMapper = historialMedicoMapper;
    }

    @Override
    public Mono<HistorialMedicoDTO> save(HistorialMedicoDTO historialMedicoDTO) {
        LOG.debug("Request to save HistorialMedico : {}", historialMedicoDTO);
        return historialMedicoRepository.save(historialMedicoMapper.toEntity(historialMedicoDTO)).map(historialMedicoMapper::toDto);
    }

    @Override
    public Mono<HistorialMedicoDTO> update(HistorialMedicoDTO historialMedicoDTO) {
        LOG.debug("Request to update HistorialMedico : {}", historialMedicoDTO);
        return historialMedicoRepository.save(historialMedicoMapper.toEntity(historialMedicoDTO)).map(historialMedicoMapper::toDto);
    }

    @Override
    public Mono<HistorialMedicoDTO> partialUpdate(HistorialMedicoDTO historialMedicoDTO) {
        LOG.debug("Request to partially update HistorialMedico : {}", historialMedicoDTO);

        return historialMedicoRepository
            .findById(historialMedicoDTO.getId())
            .map(existingHistorialMedico -> {
                historialMedicoMapper.partialUpdate(existingHistorialMedico, historialMedicoDTO);

                return existingHistorialMedico;
            })
            .flatMap(historialMedicoRepository::save)
            .map(historialMedicoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HistorialMedicoDTO> findAll() {
        LOG.debug("Request to get all HistorialMedicos");
        return historialMedicoRepository.findAll().map(historialMedicoMapper::toDto);
    }

    /**
     *  Get all the historialMedicos where Paciente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<HistorialMedicoDTO> findAllWherePacienteIsNull() {
        LOG.debug("Request to get all historialMedicos where Paciente is null");
        return historialMedicoRepository.findAllWherePacienteIsNull().map(historialMedicoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return historialMedicoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HistorialMedicoDTO> findOne(Long id) {
        LOG.debug("Request to get HistorialMedico : {}", id);
        return historialMedicoRepository.findById(id).map(historialMedicoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete HistorialMedico : {}", id);
        return historialMedicoRepository.deleteById(id);
    }
}
