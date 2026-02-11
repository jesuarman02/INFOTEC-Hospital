package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.repository.InfoSocioeconomicaRepository;
import mx.infotec.pacientesms.service.InfoSocioeconomicaService;
import mx.infotec.pacientesms.service.dto.InfoSocioeconomicaDTO;
import mx.infotec.pacientesms.service.mapper.InfoSocioeconomicaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.InfoSocioeconomica}.
 */
@Service
@Transactional
public class InfoSocioeconomicaServiceImpl implements InfoSocioeconomicaService {

    private static final Logger LOG = LoggerFactory.getLogger(InfoSocioeconomicaServiceImpl.class);

    private final InfoSocioeconomicaRepository infoSocioeconomicaRepository;

    private final InfoSocioeconomicaMapper infoSocioeconomicaMapper;

    public InfoSocioeconomicaServiceImpl(
        InfoSocioeconomicaRepository infoSocioeconomicaRepository,
        InfoSocioeconomicaMapper infoSocioeconomicaMapper
    ) {
        this.infoSocioeconomicaRepository = infoSocioeconomicaRepository;
        this.infoSocioeconomicaMapper = infoSocioeconomicaMapper;
    }

    @Override
    public Mono<InfoSocioeconomicaDTO> save(InfoSocioeconomicaDTO infoSocioeconomicaDTO) {
        LOG.debug("Request to save InfoSocioeconomica : {}", infoSocioeconomicaDTO);
        return infoSocioeconomicaRepository
            .save(infoSocioeconomicaMapper.toEntity(infoSocioeconomicaDTO))
            .map(infoSocioeconomicaMapper::toDto);
    }

    @Override
    public Mono<InfoSocioeconomicaDTO> update(InfoSocioeconomicaDTO infoSocioeconomicaDTO) {
        LOG.debug("Request to update InfoSocioeconomica : {}", infoSocioeconomicaDTO);
        return infoSocioeconomicaRepository
            .save(infoSocioeconomicaMapper.toEntity(infoSocioeconomicaDTO))
            .map(infoSocioeconomicaMapper::toDto);
    }

    @Override
    public Mono<InfoSocioeconomicaDTO> partialUpdate(InfoSocioeconomicaDTO infoSocioeconomicaDTO) {
        LOG.debug("Request to partially update InfoSocioeconomica : {}", infoSocioeconomicaDTO);

        return infoSocioeconomicaRepository
            .findById(infoSocioeconomicaDTO.getId())
            .map(existingInfoSocioeconomica -> {
                infoSocioeconomicaMapper.partialUpdate(existingInfoSocioeconomica, infoSocioeconomicaDTO);

                return existingInfoSocioeconomica;
            })
            .flatMap(infoSocioeconomicaRepository::save)
            .map(infoSocioeconomicaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<InfoSocioeconomicaDTO> findAll() {
        LOG.debug("Request to get all InfoSocioeconomicas");
        return infoSocioeconomicaRepository.findAll().map(infoSocioeconomicaMapper::toDto);
    }

    /**
     *  Get all the infoSocioeconomicas where Paciente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<InfoSocioeconomicaDTO> findAllWherePacienteIsNull() {
        LOG.debug("Request to get all infoSocioeconomicas where Paciente is null");
        return infoSocioeconomicaRepository.findAllWherePacienteIsNull().map(infoSocioeconomicaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return infoSocioeconomicaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<InfoSocioeconomicaDTO> findOne(Long id) {
        LOG.debug("Request to get InfoSocioeconomica : {}", id);
        return infoSocioeconomicaRepository.findById(id).map(infoSocioeconomicaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete InfoSocioeconomica : {}", id);
        return infoSocioeconomicaRepository.deleteById(id);
    }
}
