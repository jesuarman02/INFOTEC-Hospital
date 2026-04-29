package mx.infotec.pacientesms.service.impl;

import mx.infotec.pacientesms.domain.Cita;
import mx.infotec.pacientesms.repository.CitaRepository;
import mx.infotec.pacientesms.service.CitaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.Cita}.
 */
@Service
@Transactional
public class CitaServiceImpl implements CitaService {

    private static final Logger LOG = LoggerFactory.getLogger(CitaServiceImpl.class);

    private final CitaRepository citaRepository;

    public CitaServiceImpl(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @Override
    public Mono<Cita> save(Cita cita) {
        LOG.debug("Request to save Cita : {}", cita);
        return citaRepository.save(cita);
    }

    @Override
    public Mono<Cita> update(Cita cita) {
        LOG.debug("Request to update Cita : {}", cita);
        return citaRepository.save(cita);
    }

    @Override
    public Mono<Cita> partialUpdate(Cita cita) {
        LOG.debug("Request to partially update Cita : {}", cita);

        return citaRepository
            .findById(cita.getId())
            .map(existingCita -> {
                if (cita.getEcu() != null) {
                    existingCita.setEcu(cita.getEcu());
                }
                if (cita.getNombre() != null) {
                    existingCita.setNombre(cita.getNombre());
                }
                if (cita.getApellidoPaterno() != null) {
                    existingCita.setApellidoPaterno(cita.getApellidoPaterno());
                }
                if (cita.getApellidoMaterno() != null) {
                    existingCita.setApellidoMaterno(cita.getApellidoMaterno());
                }
                if (cita.getFechaHora() != null) {
                    existingCita.setFechaHora(cita.getFechaHora());
                }
                if (cita.getMotivo() != null) {
                    existingCita.setMotivo(cita.getMotivo());
                }

                return existingCita;
            })
            .flatMap(citaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Cita> findAll(Pageable pageable) {
        LOG.debug("Request to get all Citas");
        return citaRepository.findAllBy(pageable);
    }

    public Mono<Long> countAll() {
        return citaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Cita> findOne(Long id) {
        LOG.debug("Request to get Cita : {}", id);
        return citaRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Cita : {}", id);
        return citaRepository.deleteById(id);
    }
}
