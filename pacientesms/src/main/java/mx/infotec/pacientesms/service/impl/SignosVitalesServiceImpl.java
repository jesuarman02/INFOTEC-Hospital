package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.SignosVitalesRepository;
import mx.infotec.pacientesms.service.SignosVitalesService;
import mx.infotec.pacientesms.service.dto.SignosVitalesDTO;
import mx.infotec.pacientesms.service.mapper.SignosVitalesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.SignosVitales}.
 */
@Service
@Transactional
public class SignosVitalesServiceImpl implements SignosVitalesService {

    private static final Logger LOG = LoggerFactory.getLogger(SignosVitalesServiceImpl.class);

    private final SignosVitalesRepository signosVitalesRepository;

    private final SignosVitalesMapper signosVitalesMapper;

    public SignosVitalesServiceImpl(SignosVitalesRepository signosVitalesRepository, SignosVitalesMapper signosVitalesMapper) {
        this.signosVitalesRepository = signosVitalesRepository;
        this.signosVitalesMapper = signosVitalesMapper;
    }

    @Override
    public Mono<SignosVitalesDTO> save(SignosVitalesDTO signosVitalesDTO) {
        LOG.debug("Request to save SignosVitales : {}", signosVitalesDTO);
        return signosVitalesRepository.save(signosVitalesMapper.toEntity(signosVitalesDTO)).map(signosVitalesMapper::toDto);
    }

    @Override
    public Mono<SignosVitalesDTO> update(SignosVitalesDTO signosVitalesDTO) {
        LOG.debug("Request to update SignosVitales : {}", signosVitalesDTO);
        return signosVitalesRepository.save(signosVitalesMapper.toEntity(signosVitalesDTO)).map(signosVitalesMapper::toDto);
    }

    @Override
    public Mono<SignosVitalesDTO> partialUpdate(SignosVitalesDTO signosVitalesDTO) {
        LOG.debug("Request to partially update SignosVitales : {}", signosVitalesDTO);

        return signosVitalesRepository
            .findById(signosVitalesDTO.getId())
            .map(existingSignosVitales -> {
                signosVitalesMapper.partialUpdate(existingSignosVitales, signosVitalesDTO);

                return existingSignosVitales;
            })
            .flatMap(signosVitalesRepository::save)
            .map(signosVitalesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SignosVitalesDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SignosVitales");
        return signosVitalesRepository.findAllBy(pageable).map(signosVitalesMapper::toDto);
    }

    public Mono<Long> countAll() {
        return signosVitalesRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SignosVitalesDTO> findOne(Long id) {
        LOG.debug("Request to get SignosVitales : {}", id);
        return signosVitalesRepository.findById(id).map(signosVitalesMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete SignosVitales : {}", id);
        return signosVitalesRepository.deleteById(id);
    }
}
