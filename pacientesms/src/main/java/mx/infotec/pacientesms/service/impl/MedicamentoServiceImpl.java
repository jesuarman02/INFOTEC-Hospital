package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.MedicamentoRepository;
import mx.infotec.pacientesms.service.MedicamentoService;
import mx.infotec.pacientesms.service.dto.MedicamentoDTO;
import mx.infotec.pacientesms.service.mapper.MedicamentoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.Medicamento}.
 */
@Service
@Transactional
public class MedicamentoServiceImpl implements MedicamentoService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicamentoServiceImpl.class);

    private final MedicamentoRepository medicamentoRepository;

    private final MedicamentoMapper medicamentoMapper;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository, MedicamentoMapper medicamentoMapper) {
        this.medicamentoRepository = medicamentoRepository;
        this.medicamentoMapper = medicamentoMapper;
    }

    @Override
    public Mono<MedicamentoDTO> save(MedicamentoDTO medicamentoDTO) {
        LOG.debug("Request to save Medicamento : {}", medicamentoDTO);
        return medicamentoRepository.save(medicamentoMapper.toEntity(medicamentoDTO)).map(medicamentoMapper::toDto);
    }

    @Override
    public Mono<MedicamentoDTO> update(MedicamentoDTO medicamentoDTO) {
        LOG.debug("Request to update Medicamento : {}", medicamentoDTO);
        return medicamentoRepository.save(medicamentoMapper.toEntity(medicamentoDTO)).map(medicamentoMapper::toDto);
    }

    @Override
    public Mono<MedicamentoDTO> partialUpdate(MedicamentoDTO medicamentoDTO) {
        LOG.debug("Request to partially update Medicamento : {}", medicamentoDTO);

        return medicamentoRepository
            .findById(medicamentoDTO.getId())
            .map(existingMedicamento -> {
                medicamentoMapper.partialUpdate(existingMedicamento, medicamentoDTO);

                return existingMedicamento;
            })
            .flatMap(medicamentoRepository::save)
            .map(medicamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MedicamentoDTO> findAll() {
        LOG.debug("Request to get all Medicamentos");
        return medicamentoRepository.findAll().map(medicamentoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return medicamentoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MedicamentoDTO> findOne(Long id) {
        LOG.debug("Request to get Medicamento : {}", id);
        return medicamentoRepository.findById(id).map(medicamentoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Medicamento : {}", id);
        return medicamentoRepository.deleteById(id);
    }
}
