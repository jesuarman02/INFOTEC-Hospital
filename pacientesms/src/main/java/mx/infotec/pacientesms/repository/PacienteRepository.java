package mx.infotec.pacientesms.repository;

import mx.infotec.pacientesms.domain.Paciente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Paciente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacienteRepository extends ReactiveCrudRepository<Paciente, Long>, PacienteRepositoryInternal {
    Flux<Paciente> findAllBy(Pageable pageable);

    @Override
    Mono<Paciente> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Paciente> findAllWithEagerRelationships();

    @Override
    Flux<Paciente> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM paciente entity WHERE entity.direccion_id = :id")
    Flux<Paciente> findByDireccion(Long id);

    @Query("SELECT * FROM paciente entity WHERE entity.direccion_id IS NULL")
    Flux<Paciente> findAllWhereDireccionIsNull();

    @Query("SELECT * FROM paciente entity WHERE entity.info_socioeconomica_id = :id")
    Flux<Paciente> findByInfoSocioeconomica(Long id);

    @Query("SELECT * FROM paciente entity WHERE entity.info_socioeconomica_id IS NULL")
    Flux<Paciente> findAllWhereInfoSocioeconomicaIsNull();

    @Query("SELECT * FROM paciente entity WHERE entity.historial_general_id = :id")
    Flux<Paciente> findByHistorialGeneral(Long id);

    @Query("SELECT * FROM paciente entity WHERE entity.historial_general_id IS NULL")
    Flux<Paciente> findAllWhereHistorialGeneralIsNull();

    @Query("SELECT * FROM paciente entity WHERE entity.entidad_nacimiento_id = :id")
    Flux<Paciente> findByEntidadNacimiento(Long id);

    @Query("SELECT * FROM paciente entity WHERE entity.entidad_nacimiento_id IS NULL")
    Flux<Paciente> findAllWhereEntidadNacimientoIsNull();

    @Override
    <S extends Paciente> Mono<S> save(S entity);

    @Override
    Flux<Paciente> findAll();

    @Override
    Mono<Paciente> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PacienteRepositoryInternal {
    <S extends Paciente> Mono<S> save(S entity);

    Flux<Paciente> findAllBy(Pageable pageable);

    Flux<Paciente> findAll();

    Mono<Paciente> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Paciente> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Paciente> findOneWithEagerRelationships(Long id);

    Flux<Paciente> findAllWithEagerRelationships();

    Flux<Paciente> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
