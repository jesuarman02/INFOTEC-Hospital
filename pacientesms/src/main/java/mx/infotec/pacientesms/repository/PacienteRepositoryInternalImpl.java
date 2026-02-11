package mx.infotec.pacientesms.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import mx.infotec.pacientesms.domain.Paciente;
import mx.infotec.pacientesms.repository.rowmapper.DireccionRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.EntidadFederativaRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.HistorialMedicoRowMapper;
import mx.infotec.pacientesms.repository.rowmapper.InfoSocioeconomicaRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Paciente entity.
 */
@SuppressWarnings("unused")
class PacienteRepositoryInternalImpl extends SimpleR2dbcRepository<Paciente, Long> implements PacienteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DireccionRowMapper direccionMapper;
    private final InfoSocioeconomicaRowMapper infosocioeconomicaMapper;
    private final HistorialMedicoRowMapper historialmedicoMapper;
    private final EntidadFederativaRowMapper entidadfederativaMapper;
    private final PacienteRowMapper pacienteMapper;

    private static final Table entityTable = Table.aliased("paciente", EntityManager.ENTITY_ALIAS);
    private static final Table direccionTable = Table.aliased("direccion", "direccion");
    private static final Table infoSocioeconomicaTable = Table.aliased("info_socioeconomica", "infoSocioeconomica");
    private static final Table historialGeneralTable = Table.aliased("historial_medico", "historialGeneral");
    private static final Table entidadNacimientoTable = Table.aliased("entidad_federativa", "entidadNacimiento");

    public PacienteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DireccionRowMapper direccionMapper,
        InfoSocioeconomicaRowMapper infosocioeconomicaMapper,
        HistorialMedicoRowMapper historialmedicoMapper,
        EntidadFederativaRowMapper entidadfederativaMapper,
        PacienteRowMapper pacienteMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Paciente.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.direccionMapper = direccionMapper;
        this.infosocioeconomicaMapper = infosocioeconomicaMapper;
        this.historialmedicoMapper = historialmedicoMapper;
        this.entidadfederativaMapper = entidadfederativaMapper;
        this.pacienteMapper = pacienteMapper;
    }

    @Override
    public Flux<Paciente> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Paciente> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PacienteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(DireccionSqlHelper.getColumns(direccionTable, "direccion"));
        columns.addAll(InfoSocioeconomicaSqlHelper.getColumns(infoSocioeconomicaTable, "infoSocioeconomica"));
        columns.addAll(HistorialMedicoSqlHelper.getColumns(historialGeneralTable, "historialGeneral"));
        columns.addAll(EntidadFederativaSqlHelper.getColumns(entidadNacimientoTable, "entidadNacimiento"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(direccionTable)
            .on(Column.create("direccion_id", entityTable))
            .equals(Column.create("id", direccionTable))
            .leftOuterJoin(infoSocioeconomicaTable)
            .on(Column.create("info_socioeconomica_id", entityTable))
            .equals(Column.create("id", infoSocioeconomicaTable))
            .leftOuterJoin(historialGeneralTable)
            .on(Column.create("historial_general_id", entityTable))
            .equals(Column.create("id", historialGeneralTable))
            .leftOuterJoin(entidadNacimientoTable)
            .on(Column.create("entidad_nacimiento_id", entityTable))
            .equals(Column.create("id", entidadNacimientoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Paciente.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Paciente> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Paciente> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Paciente> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Paciente> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Paciente> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Paciente process(Row row, RowMetadata metadata) {
        Paciente entity = pacienteMapper.apply(row, "e");
        entity.setDireccion(direccionMapper.apply(row, "direccion"));
        entity.setInfoSocioeconomica(infosocioeconomicaMapper.apply(row, "infoSocioeconomica"));
        entity.setHistorialGeneral(historialmedicoMapper.apply(row, "historialGeneral"));
        entity.setEntidadNacimiento(entidadfederativaMapper.apply(row, "entidadNacimiento"));
        return entity;
    }

    @Override
    public <S extends Paciente> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
