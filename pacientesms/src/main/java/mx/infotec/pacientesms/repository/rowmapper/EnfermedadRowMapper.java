package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.Enfermedad;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Enfermedad}, with proper type conversions.
 */
@Service
public class EnfermedadRowMapper implements BiFunction<Row, String, Enfermedad> {

    private final ColumnConverter converter;

    public EnfermedadRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Enfermedad} stored in the database.
     */
    @Override
    public Enfermedad apply(Row row, String prefix) {
        Enfermedad entity = new Enfermedad();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setTipo(converter.fromRow(row, prefix + "_tipo", String.class));
        entity.setCodigoCIE(converter.fromRow(row, prefix + "_codigo_cie", String.class));
        return entity;
    }
}
