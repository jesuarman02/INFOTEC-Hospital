package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.CodigoPostalRepository;
import mx.infotec.pacientesms.service.CodigoPostalService;
import mx.infotec.pacientesms.service.dto.CodigoPostalDTO;
import mx.infotec.pacientesms.service.mapper.CodigoPostalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.CodigoPostal}.
 */
@Service
@Transactional
public class CodigoPostalServiceImpl implements CodigoPostalService {

    private static final Logger LOG = LoggerFactory.getLogger(CodigoPostalServiceImpl.class);

    private final CodigoPostalRepository codigoPostalRepository;

    private final CodigoPostalMapper codigoPostalMapper;

    public CodigoPostalServiceImpl(CodigoPostalRepository codigoPostalRepository, CodigoPostalMapper codigoPostalMapper) {
        this.codigoPostalRepository = codigoPostalRepository;
        this.codigoPostalMapper = codigoPostalMapper;
    }

    @Override
    public Mono<CodigoPostalDTO> save(CodigoPostalDTO codigoPostalDTO) {
        LOG.debug("Request to save CodigoPostal : {}", codigoPostalDTO);
        return codigoPostalRepository.save(codigoPostalMapper.toEntity(codigoPostalDTO)).map(codigoPostalMapper::toDto);
    }

    @Override
    public Mono<CodigoPostalDTO> update(CodigoPostalDTO codigoPostalDTO) {
        LOG.debug("Request to update CodigoPostal : {}", codigoPostalDTO);
        return codigoPostalRepository.save(codigoPostalMapper.toEntity(codigoPostalDTO)).map(codigoPostalMapper::toDto);
    }

    @Override
    public Mono<CodigoPostalDTO> partialUpdate(CodigoPostalDTO codigoPostalDTO) {
        LOG.debug("Request to partially update CodigoPostal : {}", codigoPostalDTO);

        return codigoPostalRepository
            .findById(codigoPostalDTO.getId())
            .map(existingCodigoPostal -> {
                codigoPostalMapper.partialUpdate(existingCodigoPostal, codigoPostalDTO);

                return existingCodigoPostal;
            })
            .flatMap(codigoPostalRepository::save)
            .map(codigoPostalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CodigoPostalDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CodigoPostals");
        return codigoPostalRepository.findAllBy(pageable).map(codigoPostalMapper::toDto);
    }
    //Modificacion
    @Override
    @Transactional(readOnly = true)
    // CAMBIO AQUÍ: de findByDCodigo a findByCodigo
    public Flux<CodigoPostalDTO> findByCodigo(String codigo, Pageable pageable) {
        LOG.debug("Request to get CodigoPostals by codigo");
        // CAMBIO AQUÍ TAMBIÉN:
        return codigoPostalRepository.findByCodigo(codigo, pageable).map(codigoPostalMapper::toDto);
    }


    public Mono<Long> countAll() {
        return codigoPostalRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CodigoPostalDTO> findOne(Long id) {
        LOG.debug("Request to get CodigoPostal : {}", id);
        return codigoPostalRepository.findById(id).map(codigoPostalMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete CodigoPostal : {}", id);
        return codigoPostalRepository.deleteById(id);
    }
}
