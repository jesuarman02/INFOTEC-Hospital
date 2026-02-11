package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.PacienteEnfermedad;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PacienteEnfermedad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacienteEnfermedadRepository
    extends ReactiveCrudRepository<PacienteEnfermedad, Long>, PacienteEnfermedadRepositoryInternal {
    @Override
    Mono<PacienteEnfermedad> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PacienteEnfermedad> findAllWithEagerRelationships();

    @Override
    Flux<PacienteEnfermedad> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM paciente_enfermedad entity WHERE entity.paciente_id = :id")
    Flux<PacienteEnfermedad> findByPaciente(Long id);

    @Query("SELECT * FROM paciente_enfermedad entity WHERE entity.paciente_id IS NULL")
    Flux<PacienteEnfermedad> findAllWherePacienteIsNull();

    @Query("SELECT * FROM paciente_enfermedad entity WHERE entity.enfermedad_id = :id")
    Flux<PacienteEnfermedad> findByEnfermedad(Long id);

    @Query("SELECT * FROM paciente_enfermedad entity WHERE entity.enfermedad_id IS NULL")
    Flux<PacienteEnfermedad> findAllWhereEnfermedadIsNull();

    @Override
    <S extends PacienteEnfermedad> Mono<S> save(S entity);

    @Override
    Flux<PacienteEnfermedad> findAll();

    @Override
    Mono<PacienteEnfermedad> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PacienteEnfermedadRepositoryInternal {
    <S extends PacienteEnfermedad> Mono<S> save(S entity);

    Flux<PacienteEnfermedad> findAllBy(Pageable pageable);

    Flux<PacienteEnfermedad> findAll();

    Mono<PacienteEnfermedad> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PacienteEnfermedad> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PacienteEnfermedad> findOneWithEagerRelationships(Long id);

    Flux<PacienteEnfermedad> findAllWithEagerRelationships();

    Flux<PacienteEnfermedad> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
