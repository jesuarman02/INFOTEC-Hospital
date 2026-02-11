package mx.infotec.pacientesms.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import mx.infotec.pacientesms.domain.PacienteEnfermedad;
import mx.infotec.pacientesms.repository.rowmapper.EnfermedadRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.PacienteEnfermedadRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PacienteEnfermedad entity.
 */
@SuppressWarnings("unused")
class PacienteEnfermedadRepositoryInternalImpl
    extends SimpleR2dbcRepository<PacienteEnfermedad, Long>
    implements PacienteEnfermedadRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PacienteRowMapper pacienteMapper;
    private final EnfermedadRowMapper enfermedadMapper;
    private final PacienteEnfermedadRowMapper pacienteenfermedadMapper;

    private static final Table entityTable = Table.aliased("paciente_enfermedad", EntityManager.ENTITY_ALIAS);
    private static final Table pacienteTable = Table.aliased("paciente", "paciente");
    private static final Table enfermedadTable = Table.aliased("enfermedad", "enfermedad");

    public PacienteEnfermedadRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PacienteRowMapper pacienteMapper,
        EnfermedadRowMapper enfermedadMapper,
        PacienteEnfermedadRowMapper pacienteenfermedadMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PacienteEnfermedad.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.pacienteMapper = pacienteMapper;
        this.enfermedadMapper = enfermedadMapper;
        this.pacienteenfermedadMapper = pacienteenfermedadMapper;
    }

    @Override
    public Flux<PacienteEnfermedad> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PacienteEnfermedad> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PacienteEnfermedadSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PacienteSqlHelper.getColumns(pacienteTable, "paciente"));
        columns.addAll(EnfermedadSqlHelper.getColumns(enfermedadTable, "enfermedad"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(pacienteTable)
            .on(Column.create("paciente_id", entityTable))
            .equals(Column.create("id", pacienteTable))
            .leftOuterJoin(enfermedadTable)
            .on(Column.create("enfermedad_id", entityTable))
            .equals(Column.create("id", enfermedadTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PacienteEnfermedad.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PacienteEnfermedad> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PacienteEnfermedad> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PacienteEnfermedad> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PacienteEnfermedad> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PacienteEnfermedad> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PacienteEnfermedad process(Row row, RowMetadata metadata) {
        PacienteEnfermedad entity = pacienteenfermedadMapper.apply(row, "e");
        entity.setPaciente(pacienteMapper.apply(row, "paciente"));
        entity.setEnfermedad(enfermedadMapper.apply(row, "enfermedad"));
        return entity;
    }

    @Override
    public <S extends PacienteEnfermedad> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
