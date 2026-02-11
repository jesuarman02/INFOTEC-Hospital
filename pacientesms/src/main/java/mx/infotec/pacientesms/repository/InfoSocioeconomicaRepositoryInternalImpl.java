package mx.infotec.pacientesms.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import mx.infotec.pacientesms.domain.InfoSocioeconomica;
import mx.infotec.pacientesms.repository.rowmapper.InfoSocioeconomicaRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the InfoSocioeconomica entity.
 */
@SuppressWarnings("unused")
class InfoSocioeconomicaRepositoryInternalImpl
    extends SimpleR2dbcRepository<InfoSocioeconomica, Long>
    implements InfoSocioeconomicaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InfoSocioeconomicaRowMapper infosocioeconomicaMapper;

    private static final Table entityTable = Table.aliased("info_socioeconomica", EntityManager.ENTITY_ALIAS);

    public InfoSocioeconomicaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        InfoSocioeconomicaRowMapper infosocioeconomicaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InfoSocioeconomica.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.infosocioeconomicaMapper = infosocioeconomicaMapper;
    }

    @Override
    public Flux<InfoSocioeconomica> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InfoSocioeconomica> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InfoSocioeconomicaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InfoSocioeconomica.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InfoSocioeconomica> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<InfoSocioeconomica> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private InfoSocioeconomica process(Row row, RowMetadata metadata) {
        InfoSocioeconomica entity = infosocioeconomicaMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends InfoSocioeconomica> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
