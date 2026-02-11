package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.InfoSocioeconomica;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the InfoSocioeconomica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfoSocioeconomicaRepository
    extends ReactiveCrudRepository<InfoSocioeconomica, Long>, InfoSocioeconomicaRepositoryInternal {
    @Query("SELECT * FROM info_socioeconomica entity WHERE entity.id not in (select paciente_id from paciente)")
    Flux<InfoSocioeconomica> findAllWherePacienteIsNull();

    @Override
    <S extends InfoSocioeconomica> Mono<S> save(S entity);

    @Override
    Flux<InfoSocioeconomica> findAll();

    @Override
    Mono<InfoSocioeconomica> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface InfoSocioeconomicaRepositoryInternal {
    <S extends InfoSocioeconomica> Mono<S> save(S entity);

    Flux<InfoSocioeconomica> findAllBy(Pageable pageable);

    Flux<InfoSocioeconomica> findAll();

    Mono<InfoSocioeconomica> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InfoSocioeconomica> findAllBy(Pageable pageable, Criteria criteria);
}
