package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.EntidadFederativa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the EntidadFederativa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntidadFederativaRepository extends ReactiveCrudRepository<EntidadFederativa, Long>, EntidadFederativaRepositoryInternal {
    @Override
    <S extends EntidadFederativa> Mono<S> save(S entity);

    @Override
    Flux<EntidadFederativa> findAll();

    @Override
    Mono<EntidadFederativa> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EntidadFederativaRepositoryInternal {
    <S extends EntidadFederativa> Mono<S> save(S entity);

    Flux<EntidadFederativa> findAllBy(Pageable pageable);

    Flux<EntidadFederativa> findAll();

    Mono<EntidadFederativa> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EntidadFederativa> findAllBy(Pageable pageable, Criteria criteria);
}
