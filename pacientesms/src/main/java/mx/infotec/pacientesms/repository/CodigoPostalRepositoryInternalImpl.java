package mx.infotec.pacientesms.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import mx.infotec.pacientesms.domain.CodigoPostal;
import mx.infotec.pacientesms.repository.rowmapper.CodigoPostalRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the CodigoPostal entity.
 */
@SuppressWarnings("unused")
class CodigoPostalRepositoryInternalImpl extends SimpleR2dbcRepository<CodigoPostal, Long> implements CodigoPostalRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CodigoPostalRowMapper codigopostalMapper;

    private static final Table entityTable = Table.aliased("codigo_postal", EntityManager.ENTITY_ALIAS);

    public CodigoPostalRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CodigoPostalRowMapper codigopostalMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CodigoPostal.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.codigopostalMapper = codigopostalMapper;
    }

    @Override
    public Flux<CodigoPostal> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<CodigoPostal> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CodigoPostalSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CodigoPostal.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CodigoPostal> findAll() {
        return findAllBy(null);
    }

    //Modificacion
    public Flux<CodigoPostal> findByDCodigo(String dCodigo){
        Comparison whereClause = Conditions.isEqual(entityTable.column("dCodigo"), Conditions.just(dCodigo.toString()));
        return createQuery(null, whereClause).all();
    }

    @Override
    public Mono<CodigoPostal> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private CodigoPostal process(Row row, RowMetadata metadata) {
        CodigoPostal entity = codigopostalMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends CodigoPostal> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
