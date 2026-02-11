package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.TipoVialidad;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TipoVialidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoVialidadRepository extends ReactiveCrudRepository<TipoVialidad, Long>, TipoVialidadRepositoryInternal {
    @Override
    <S extends TipoVialidad> Mono<S> save(S entity);

    @Override
    Flux<TipoVialidad> findAll();

    @Override
    Mono<TipoVialidad> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TipoVialidadRepositoryInternal {
    <S extends TipoVialidad> Mono<S> save(S entity);

    Flux<TipoVialidad> findAllBy(Pageable pageable);

    Flux<TipoVialidad> findAll();

    Mono<TipoVialidad> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TipoVialidad> findAllBy(Pageable pageable, Criteria criteria);
}
