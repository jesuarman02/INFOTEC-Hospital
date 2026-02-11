package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.Enfermedad;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Enfermedad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnfermedadRepository extends ReactiveCrudRepository<Enfermedad, Long>, EnfermedadRepositoryInternal {
    @Override
    <S extends Enfermedad> Mono<S> save(S entity);

    @Override
    Flux<Enfermedad> findAll();

    @Override
    Mono<Enfermedad> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EnfermedadRepositoryInternal {
    <S extends Enfermedad> Mono<S> save(S entity);

    Flux<Enfermedad> findAllBy(Pageable pageable);

    Flux<Enfermedad> findAll();

    Mono<Enfermedad> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Enfermedad> findAllBy(Pageable pageable, Criteria criteria);
}
