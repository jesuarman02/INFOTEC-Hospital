package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.PacienteAlergia;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PacienteAlergia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacienteAlergiaRepository extends ReactiveCrudRepository<PacienteAlergia, Long>, PacienteAlergiaRepositoryInternal {
    @Override
    Mono<PacienteAlergia> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PacienteAlergia> findAllWithEagerRelationships();

    @Override
    Flux<PacienteAlergia> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM paciente_alergia entity WHERE entity.paciente_id = :id")
    Flux<PacienteAlergia> findByPaciente(Long id);

    @Query("SELECT * FROM paciente_alergia entity WHERE entity.paciente_id IS NULL")
    Flux<PacienteAlergia> findAllWherePacienteIsNull();

    @Query("SELECT * FROM paciente_alergia entity WHERE entity.alergia_id = :id")
    Flux<PacienteAlergia> findByAlergia(Long id);

    @Query("SELECT * FROM paciente_alergia entity WHERE entity.alergia_id IS NULL")
    Flux<PacienteAlergia> findAllWhereAlergiaIsNull();

    @Override
    <S extends PacienteAlergia> Mono<S> save(S entity);

    @Override
    Flux<PacienteAlergia> findAll();

    @Override
    Mono<PacienteAlergia> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PacienteAlergiaRepositoryInternal {
    <S extends PacienteAlergia> Mono<S> save(S entity);

    Flux<PacienteAlergia> findAllBy(Pageable pageable);

    Flux<PacienteAlergia> findAll();

    Mono<PacienteAlergia> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PacienteAlergia> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PacienteAlergia> findOneWithEagerRelationships(Long id);

    Flux<PacienteAlergia> findAllWithEagerRelationships();

    Flux<PacienteAlergia> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
