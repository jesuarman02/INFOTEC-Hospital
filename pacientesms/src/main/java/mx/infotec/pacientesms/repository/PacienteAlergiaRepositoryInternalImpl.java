package mx.infotec.pacientesms.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import mx.infotec.pacientesms.domain.PacienteAlergia;
import mx.infotec.pacientesms.repository.rowmapper.AlergiaRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.PacienteAlergiaRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.PacienteRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PacienteAlergia entity.
 */
@SuppressWarnings("unused")
class PacienteAlergiaRepositoryInternalImpl
    extends SimpleR2dbcRepository<PacienteAlergia, Long>
    implements PacienteAlergiaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PacienteRowMapper pacienteMapper;
    private final AlergiaRowMapper alergiaMapper;
    private final PacienteAlergiaRowMapper pacientealergiaMapper;

    private static final Table entityTable = Table.aliased("paciente_alergia", EntityManager.ENTITY_ALIAS);
    private static final Table pacienteTable = Table.aliased("paciente", "paciente");
    private static final Table alergiaTable = Table.aliased("alergia", "alergia");

    public PacienteAlergiaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PacienteRowMapper pacienteMapper,
        AlergiaRowMapper alergiaMapper,
        PacienteAlergiaRowMapper pacientealergiaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PacienteAlergia.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.pacienteMapper = pacienteMapper;
        this.alergiaMapper = alergiaMapper;
        this.pacientealergiaMapper = pacientealergiaMapper;
    }

    @Override
    public Flux<PacienteAlergia> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PacienteAlergia> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PacienteAlergiaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PacienteSqlHelper.getColumns(pacienteTable, "paciente"));
        columns.addAll(AlergiaSqlHelper.getColumns(alergiaTable, "alergia"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(pacienteTable)
            .on(Column.create("paciente_id", entityTable))
            .equals(Column.create("id", pacienteTable))
            .leftOuterJoin(alergiaTable)
            .on(Column.create("alergia_id", entityTable))
            .equals(Column.create("id", alergiaTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PacienteAlergia.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PacienteAlergia> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PacienteAlergia> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PacienteAlergia> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PacienteAlergia> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PacienteAlergia> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PacienteAlergia process(Row row, RowMetadata metadata) {
        PacienteAlergia entity = pacientealergiaMapper.apply(row, "e");
        entity.setPaciente(pacienteMapper.apply(row, "paciente"));
        entity.setAlergia(alergiaMapper.apply(row, "alergia"));
        return entity;
    }

    @Override
    public <S extends PacienteAlergia> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
