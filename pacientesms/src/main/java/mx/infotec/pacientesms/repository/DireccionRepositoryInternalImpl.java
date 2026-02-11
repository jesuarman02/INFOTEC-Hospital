package mx.infotec.pacientesms.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import mx.infotec.pacientesms.domain.Direccion;
import mx.infotec.pacientesms.repository.rowmapper.CodigoPostalRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.DireccionRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.EntidadFederativaRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.TipoVialidadRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Direccion entity.
 */
@SuppressWarnings("unused")
class DireccionRepositoryInternalImpl extends SimpleR2dbcRepository<Direccion, Long> implements DireccionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TipoVialidadRowMapper tipovialidadMapper;
    private final CodigoPostalRowMapper codigopostalMapper;
    private final EntidadFederativaRowMapper entidadfederativaMapper;
    private final DireccionRowMapper direccionMapper;

    private static final Table entityTable = Table.aliased("direccion", EntityManager.ENTITY_ALIAS);
    private static final Table tipoVialidadTable = Table.aliased("tipo_vialidad", "tipoVialidad");
    private static final Table codigoPostalInfoTable = Table.aliased("codigo_postal", "codigoPostalInfo");
    private static final Table entidadFederativaTable = Table.aliased("entidad_federativa", "entidadFederativa");

    public DireccionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TipoVialidadRowMapper tipovialidadMapper,
        CodigoPostalRowMapper codigopostalMapper,
        EntidadFederativaRowMapper entidadfederativaMapper,
        DireccionRowMapper direccionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Direccion.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.tipovialidadMapper = tipovialidadMapper;
        this.codigopostalMapper = codigopostalMapper;
        this.entidadfederativaMapper = entidadfederativaMapper;
        this.direccionMapper = direccionMapper;
    }

    @Override
    public Flux<Direccion> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Direccion> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DireccionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TipoVialidadSqlHelper.getColumns(tipoVialidadTable, "tipoVialidad"));
        columns.addAll(CodigoPostalSqlHelper.getColumns(codigoPostalInfoTable, "codigoPostalInfo"));
        columns.addAll(EntidadFederativaSqlHelper.getColumns(entidadFederativaTable, "entidadFederativa"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(tipoVialidadTable)
            .on(Column.create("tipo_vialidad_id", entityTable))
            .equals(Column.create("id", tipoVialidadTable))
            .leftOuterJoin(codigoPostalInfoTable)
            .on(Column.create("codigo_postal_info_id", entityTable))
            .equals(Column.create("id", codigoPostalInfoTable))
            .leftOuterJoin(entidadFederativaTable)
            .on(Column.create("entidad_federativa_id", entityTable))
            .equals(Column.create("id", entidadFederativaTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Direccion.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Direccion> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Direccion> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Direccion> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Direccion> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Direccion> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Direccion process(Row row, RowMetadata metadata) {
        Direccion entity = direccionMapper.apply(row, "e");
        entity.setTipoVialidad(tipovialidadMapper.apply(row, "tipoVialidad"));
        entity.setCodigoPostalInfo(codigopostalMapper.apply(row, "codigoPostalInfo"));
        entity.setEntidadFederativa(entidadfederativaMapper.apply(row, "entidadFederativa"));
        return entity;
    }

    @Override
    public <S extends Direccion> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
