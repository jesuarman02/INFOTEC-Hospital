package mx.infotec.pacientesms.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import mx.infotec.pacientesms.domain.SignosVitales;
import mx.infotec.pacientesms.repository.rowmapper.PacienteRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.SignosVitalesRowMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the SignosVitales entity.
 */
@SuppressWarnings("unused")
class SignosVitalesRepositoryInternalImpl extends SimpleR2dbcRepository<SignosVitales, Long> implements SignosVitalesRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PacienteRowMapper pacienteMapper;
    private final SignosVitalesRowMapper signosvitalesMapper;

    private static final Table entityTable = Table.aliased("signos_vitales", EntityManager.ENTITY_ALIAS);
    private static final Table pacienteTable = Table.aliased("paciente", "paciente");

    public SignosVitalesRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PacienteRowMapper pacienteMapper,
        SignosVitalesRowMapper signosvitalesMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SignosVitales.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.pacienteMapper = pacienteMapper;
        this.signosvitalesMapper = signosvitalesMapper;
    }

    @Override
    public Flux<SignosVitales> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SignosVitales> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SignosVitalesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PacienteSqlHelper.getColumns(pacienteTable, "paciente"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(pacienteTable)
            .on(Column.create("paciente_id", entityTable))
            .equals(Column.create("id", pacienteTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SignosVitales.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SignosVitales> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SignosVitales> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private SignosVitales process(Row row, RowMetadata metadata) {
        SignosVitales entity = signosvitalesMapper.apply(row, "e");
        entity.setPaciente(pacienteMapper.apply(row, "paciente"));
        return entity;
    }

    @Override
    public <S extends SignosVitales> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
