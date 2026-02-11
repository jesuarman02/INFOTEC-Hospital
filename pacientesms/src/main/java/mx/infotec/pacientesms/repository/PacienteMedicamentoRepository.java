package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.PacienteMedicamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PacienteMedicamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacienteMedicamentoRepository
    extends ReactiveCrudRepository<PacienteMedicamento, Long>, PacienteMedicamentoRepositoryInternal {
    Flux<PacienteMedicamento> findAllBy(Pageable pageable);

    @Override
    Mono<PacienteMedicamento> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PacienteMedicamento> findAllWithEagerRelationships();

    @Override
    Flux<PacienteMedicamento> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM paciente_medicamento entity WHERE entity.paciente_id = :id")
    Flux<PacienteMedicamento> findByPaciente(Long id);

    @Query("SELECT * FROM paciente_medicamento entity WHERE entity.paciente_id IS NULL")
    Flux<PacienteMedicamento> findAllWherePacienteIsNull();

    @Query("SELECT * FROM paciente_medicamento entity WHERE entity.medicamento_id = :id")
    Flux<PacienteMedicamento> findByMedicamento(Long id);

    @Query("SELECT * FROM paciente_medicamento entity WHERE entity.medicamento_id IS NULL")
    Flux<PacienteMedicamento> findAllWhereMedicamentoIsNull();

    @Override
    <S extends PacienteMedicamento> Mono<S> save(S entity);

    @Override
    Flux<PacienteMedicamento> findAll();

    @Override
    Mono<PacienteMedicamento> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PacienteMedicamentoRepositoryInternal {
    <S extends PacienteMedicamento> Mono<S> save(S entity);

    Flux<PacienteMedicamento> findAllBy(Pageable pageable);

    Flux<PacienteMedicamento> findAll();

    Mono<PacienteMedicamento> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PacienteMedicamento> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PacienteMedicamento> findOneWithEagerRelationships(Long id);

    Flux<PacienteMedicamento> findAllWithEagerRelationships();

    Flux<PacienteMedicamento> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
