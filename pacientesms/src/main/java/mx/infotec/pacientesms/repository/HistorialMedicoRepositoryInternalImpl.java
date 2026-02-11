package mx.infotec.pacientesms.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import mx.infotec.pacientesms.domain.HistorialMedico;
import mx.infotec.pacientesms.repository.rowmapper.HistorialMedicoRowMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the HistorialMedico entity.
 */
@SuppressWarnings("unused")
class HistorialMedicoRepositoryInternalImpl
    extends SimpleR2dbcRepository<HistorialMedico, Long>
    implements HistorialMedicoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final HistorialMedicoRowMapper historialmedicoMapper;

    private static final Table entityTable = Table.aliased("historial_medico", EntityManager.ENTITY_ALIAS);

    public HistorialMedicoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        HistorialMedicoRowMapper historialmedicoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(HistorialMedico.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.historialmedicoMapper = historialmedicoMapper;
    }

    @Override
    public Flux<HistorialMedico> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<HistorialMedico> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = HistorialMedicoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, HistorialMedico.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<HistorialMedico> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<HistorialMedico> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private HistorialMedico process(Row row, RowMetadata metadata) {
        HistorialMedico entity = historialmedicoMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends HistorialMedico> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
