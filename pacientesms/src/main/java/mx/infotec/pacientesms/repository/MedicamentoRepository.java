package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.Medicamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Medicamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicamentoRepository extends ReactiveCrudRepository<Medicamento, Long>, MedicamentoRepositoryInternal {
    @Override
    <S extends Medicamento> Mono<S> save(S entity);

    @Override
    Flux<Medicamento> findAll();

    @Override
    Mono<Medicamento> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MedicamentoRepositoryInternal {
    <S extends Medicamento> Mono<S> save(S entity);

    Flux<Medicamento> findAllBy(Pageable pageable);

    Flux<Medicamento> findAll();

    Mono<Medicamento> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Medicamento> findAllBy(Pageable pageable, Criteria criteria);
}
