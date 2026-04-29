package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.Cita;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Cita entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaRepository extends ReactiveCrudRepository<Cita, Long>, CitaRepositoryInternal {
    Flux<Cita> findAllBy(Pageable pageable);

    @Override
    <S extends Cita> Mono<S> save(S entity);

    @Override
    Flux<Cita> findAll();

    @Override
    Mono<Cita> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CitaRepositoryInternal {
    <S extends Cita> Mono<S> save(S entity);

    Flux<Cita> findAllBy(Pageable pageable);

    Flux<Cita> findAll();

    Mono<Cita> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Cita> findAllBy(Pageable pageable, Criteria criteria);
}
