package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.HistorialMedico;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the HistorialMedico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistorialMedicoRepository extends ReactiveCrudRepository<HistorialMedico, Long>, HistorialMedicoRepositoryInternal {
    @Query("SELECT * FROM historial_medico entity WHERE entity.id not in (select paciente_id from paciente)")
    Flux<HistorialMedico> findAllWherePacienteIsNull();

    @Override
    <S extends HistorialMedico> Mono<S> save(S entity);

    @Override
    Flux<HistorialMedico> findAll();

    @Override
    Mono<HistorialMedico> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface HistorialMedicoRepositoryInternal {
    <S extends HistorialMedico> Mono<S> save(S entity);

    Flux<HistorialMedico> findAllBy(Pageable pageable);

    Flux<HistorialMedico> findAll();

    Mono<HistorialMedico> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HistorialMedico> findAllBy(Pageable pageable, Criteria criteria);
}
