package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.Alergia;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Alergia}, with proper type conversions.
 */
@Service
public class AlergiaRowMapper implements BiFunction<Row, String, Alergia> {

    private final ColumnConverter converter;

    public AlergiaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Alergia} stored in the database.
     */
    @Override
    public Alergia apply(Row row, String prefix) {
        Alergia entity = new Alergia();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setDescripcion(converter.fromRow(row, prefix + "_descripcion", String.class));
        return entity;
    }
}
