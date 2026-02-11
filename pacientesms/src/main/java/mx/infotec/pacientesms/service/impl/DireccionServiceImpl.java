package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.DireccionRepository;
import mx.infotec.pacientesms.service.DireccionService;
import mx.infotec.pacientesms.service.dto.DireccionDTO;
import mx.infotec.pacientesms.service.mapper.DireccionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.Direccion}.
 */
@Service
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private static final Logger LOG = LoggerFactory.getLogger(DireccionServiceImpl.class);

    private final DireccionRepository direccionRepository;

    private final DireccionMapper direccionMapper;

    public DireccionServiceImpl(DireccionRepository direccionRepository, DireccionMapper direccionMapper) {
        this.direccionRepository = direccionRepository;
        this.direccionMapper = direccionMapper;
    }

    @Override
    public Mono<DireccionDTO> save(DireccionDTO direccionDTO) {
        LOG.debug("Request to save Direccion : {}", direccionDTO);
        return direccionRepository.save(direccionMapper.toEntity(direccionDTO)).map(direccionMapper::toDto);
    }

    @Override
    public Mono<DireccionDTO> update(DireccionDTO direccionDTO) {
        LOG.debug("Request to update Direccion : {}", direccionDTO);
        return direccionRepository.save(direccionMapper.toEntity(direccionDTO)).map(direccionMapper::toDto);
    }

    @Override
    public Mono<DireccionDTO> partialUpdate(DireccionDTO direccionDTO) {
        LOG.debug("Request to partially update Direccion : {}", direccionDTO);

        return direccionRepository
            .findById(direccionDTO.getId())
            .map(existingDireccion -> {
                direccionMapper.partialUpdate(existingDireccion, direccionDTO);

                return existingDireccion;
            })
            .flatMap(direccionRepository::save)
            .map(direccionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DireccionDTO> findAll() {
        LOG.debug("Request to get all Direccions");
        return direccionRepository.findAll().map(direccionMapper::toDto);
    }

    public Flux<DireccionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return direccionRepository.findAllWithEagerRelationships(pageable).map(direccionMapper::toDto);
    }

    /**
     *  Get all the direccions where Paciente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DireccionDTO> findAllWherePacienteIsNull() {
        LOG.debug("Request to get all direccions where Paciente is null");
        return direccionRepository.findAllWherePacienteIsNull().map(direccionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return direccionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<DireccionDTO> findOne(Long id) {
        LOG.debug("Request to get Direccion : {}", id);
        return direccionRepository.findOneWithEagerRelationships(id).map(direccionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Direccion : {}", id);
        return direccionRepository.deleteById(id);
    }
}
