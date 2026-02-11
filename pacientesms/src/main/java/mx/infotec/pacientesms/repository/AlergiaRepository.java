package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.Alergia;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Alergia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlergiaRepository extends ReactiveCrudRepository<Alergia, Long>, AlergiaRepositoryInternal {
    @Override
    <S extends Alergia> Mono<S> save(S entity);

    @Override
    Flux<Alergia> findAll();

    @Override
    Mono<Alergia> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AlergiaRepositoryInternal {
    <S extends Alergia> Mono<S> save(S entity);

    Flux<Alergia> findAllBy(Pageable pageable);

    Flux<Alergia> findAll();

    Mono<Alergia> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Alergia> findAllBy(Pageable pageable, Criteria criteria);
}
