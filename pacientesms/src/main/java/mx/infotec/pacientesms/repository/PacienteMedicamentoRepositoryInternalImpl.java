package mx.infotec.pacientesms.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import mx.infotec.pacientesms.domain.PacienteMedicamento;
import mx.infotec.pacientesms.repository.rowmapper.MedicamentoRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.PacienteMedicamentoRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PacienteMedicamento entity.
 */
@SuppressWarnings("unused")
class PacienteMedicamentoRepositoryInternalImpl
    extends SimpleR2dbcRepository<PacienteMedicamento, Long>
    implements PacienteMedicamentoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PacienteRowMapper pacienteMapper;
    private final MedicamentoRowMapper medicamentoMapper;
    private final PacienteMedicamentoRowMapper pacientemedicamentoMapper;

    private static final Table entityTable = Table.aliased("paciente_medicamento", EntityManager.ENTITY_ALIAS);
    private static final Table pacienteTable = Table.aliased("paciente", "paciente");
    private static final Table medicamentoTable = Table.aliased("medicamento", "medicamento");

    public PacienteMedicamentoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PacienteRowMapper pacienteMapper,
        MedicamentoRowMapper medicamentoMapper,
        PacienteMedicamentoRowMapper pacientemedicamentoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PacienteMedicamento.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.pacienteMapper = pacienteMapper;
        this.medicamentoMapper = medicamentoMapper;
        this.pacientemedicamentoMapper = pacientemedicamentoMapper;
    }

    @Override
    public Flux<PacienteMedicamento> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PacienteMedicamento> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PacienteMedicamentoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PacienteSqlHelper.getColumns(pacienteTable, "paciente"));
        columns.addAll(MedicamentoSqlHelper.getColumns(medicamentoTable, "medicamento"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(pacienteTable)
            .on(Column.create("paciente_id", entityTable))
            .equals(Column.create("id", pacienteTable))
            .leftOuterJoin(medicamentoTable)
            .on(Column.create("medicamento_id", entityTable))
            .equals(Column.create("id", medicamentoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PacienteMedicamento.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PacienteMedicamento> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PacienteMedicamento> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<PacienteMedicamento> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<PacienteMedicamento> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<PacienteMedicamento> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private PacienteMedicamento process(Row row, RowMetadata metadata) {
        PacienteMedicamento entity = pacientemedicamentoMapper.apply(row, "e");
        entity.setPaciente(pacienteMapper.apply(row, "paciente"));
        entity.setMedicamento(medicamentoMapper.apply(row, "medicamento"));
        return entity;
    }

    @Override
    public <S extends PacienteMedicamento> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
