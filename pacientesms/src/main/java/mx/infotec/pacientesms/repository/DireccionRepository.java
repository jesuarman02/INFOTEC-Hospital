package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.Direccion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Direccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DireccionRepository extends ReactiveCrudRepository<Direccion, Long>, DireccionRepositoryInternal {
    @Override
    Mono<Direccion> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Direccion> findAllWithEagerRelationships();

    @Override
    Flux<Direccion> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM direccion entity WHERE entity.tipo_vialidad_id = :id")
    Flux<Direccion> findByTipoVialidad(Long id);

    @Query("SELECT * FROM direccion entity WHERE entity.tipo_vialidad_id IS NULL")
    Flux<Direccion> findAllWhereTipoVialidadIsNull();

    @Query("SELECT * FROM direccion entity WHERE entity.codigo_postal_info_id = :id")
    Flux<Direccion> findByCodigoPostalInfo(Long id);

    @Query("SELECT * FROM direccion entity WHERE entity.codigo_postal_info_id IS NULL")
    Flux<Direccion> findAllWhereCodigoPostalInfoIsNull();

    @Query("SELECT * FROM direccion entity WHERE entity.entidad_federativa_id = :id")
    Flux<Direccion> findByEntidadFederativa(Long id);

    @Query("SELECT * FROM direccion entity WHERE entity.entidad_federativa_id IS NULL")
    Flux<Direccion> findAllWhereEntidadFederativaIsNull();

    @Query("SELECT * FROM direccion entity WHERE entity.id not in (select paciente_id from paciente)")
    Flux<Direccion> findAllWherePacienteIsNull();

    @Override
    <S extends Direccion> Mono<S> save(S entity);

    @Override
    Flux<Direccion> findAll();

    @Override
    Mono<Direccion> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DireccionRepositoryInternal {
    <S extends Direccion> Mono<S> save(S entity);

    Flux<Direccion> findAllBy(Pageable pageable);

    Flux<Direccion> findAll();

    Mono<Direccion> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Direccion> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Direccion> findOneWithEagerRelationships(Long id);

    Flux<Direccion> findAllWithEagerRelationships();

    Flux<Direccion> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
