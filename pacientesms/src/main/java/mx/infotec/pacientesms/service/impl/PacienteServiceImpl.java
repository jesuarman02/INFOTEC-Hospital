package mx.infotec.pacientesms.service.impl;
import mx.infotec.pacientesms.service.CurpValidator;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import mx.infotec.pacientesms.repository.PacienteRepository;
import mx.infotec.pacientesms.service.PacienteService;
import mx.infotec.pacientesms.service.dto.PacienteDTO;
import mx.infotec.pacientesms.service.mapper.PacienteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link mx.infotec.pacientesms.domain.Paciente}.
 */
@Service
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteServiceImpl.class);

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    public PacienteServiceImpl(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

@Override
    public Mono<PacienteDTO> save(PacienteDTO pacienteDTO) {
        LOG.debug("Request to save Paciente : {}", pacienteDTO);
        
        // --- INICIO VALIDACIÓN CURP ---
        // Si el CURP es nulo, o si NO empieza con 'EXTXX' y TAMPOCO es un CURP mexicano válido, lanzamos error.
        if (pacienteDTO.getCurp() == null || (!pacienteDTO.getCurp().startsWith("EXTXX") && !CurpValidator.esValido(pacienteDTO.getCurp()))) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "La CURP ingresada no es válida o tiene un formato incorrecto."));
        }
        // --- FIN VALIDACIÓN CURP ---

        return pacienteRepository.save(pacienteMapper.toEntity(pacienteDTO))
            .map(pacienteMapper::toDto);
    }

    @Override
    public Mono<PacienteDTO> update(PacienteDTO pacienteDTO) {
        LOG.debug("Request to update Paciente : {}", pacienteDTO);
        
        // --- INICIO VALIDACIÓN CURP ---
        // Si el CURP es nulo, o si NO empieza con 'EXTXX' y TAMPOCO es un CURP mexicano válido, lanzamos error.
        if (pacienteDTO.getCurp() == null || (!pacienteDTO.getCurp().startsWith("EXTXX") && !CurpValidator.esValido(pacienteDTO.getCurp()))) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "La CURP ingresada no es válida o tiene un formato incorrecto."));
        }
        // --- FIN VALIDACIÓN CURP ---

        return pacienteRepository.save(pacienteMapper.toEntity(pacienteDTO))
            .map(pacienteMapper::toDto);
    }

    @Override
    public Mono<PacienteDTO> partialUpdate(PacienteDTO pacienteDTO) {
        LOG.debug("Request to partially update Paciente : {}", pacienteDTO);

        return pacienteRepository
            .findById(pacienteDTO.getId())
            .map(existingPaciente -> {
                pacienteMapper.partialUpdate(existingPaciente, pacienteDTO);

                return existingPaciente;
            })
            .flatMap(pacienteRepository::save)
            .map(pacienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PacienteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Pacientes");
        return pacienteRepository.findAllBy(pageable).map(pacienteMapper::toDto);
    }

    public Flux<PacienteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pacienteRepository.findAllWithEagerRelationships(pageable).map(pacienteMapper::toDto);
    }

    public Mono<Long> countAll() {
        return pacienteRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PacienteDTO> findOne(Long id) {
        LOG.debug("Request to get Paciente : {}", id);
        return pacienteRepository.findOneWithEagerRelationships(id).map(pacienteMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Paciente : {}", id);
        return pacienteRepository.deleteById(id);
    }
    
}