package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.SignosVitales;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SignosVitales entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignosVitalesRepository extends ReactiveCrudRepository<SignosVitales, Long>, SignosVitalesRepositoryInternal {
    Flux<SignosVitales> findAllBy(Pageable pageable);

    @Query("SELECT * FROM signos_vitales entity WHERE entity.paciente_id = :id")
    Flux<SignosVitales> findByPaciente(Long id);

    @Query("SELECT * FROM signos_vitales entity WHERE entity.paciente_id IS NULL")
    Flux<SignosVitales> findAllWherePacienteIsNull();

    @Override
    <S extends SignosVitales> Mono<S> save(S entity);

    @Override
    Flux<SignosVitales> findAll();

    @Override
    Mono<SignosVitales> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SignosVitalesRepositoryInternal {
    <S extends SignosVitales> Mono<S> save(S entity);

    Flux<SignosVitales> findAllBy(Pageable pageable);

    Flux<SignosVitales> findAll();

    Mono<SignosVitales> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SignosVitales> findAllBy(Pageable pageable, Criteria criteria);
}
