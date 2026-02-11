package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.TipoVialidad;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TipoVialidad}, with proper type conversions.
 */
@Service
public class TipoVialidadRowMapper implements BiFunction<Row, String, TipoVialidad> {

    private final ColumnConverter converter;

    public TipoVialidadRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TipoVialidad} stored in the database.
     */
    @Override
    public TipoVialidad apply(Row row, String prefix) {
        TipoVialidad entity = new TipoVialidad();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        return entity;
    }
}
