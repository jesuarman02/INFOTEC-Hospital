package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.EntidadFederativa;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EntidadFederativa}, with proper type conversions.
 */
@Service
public class EntidadFederativaRowMapper implements BiFunction<Row, String, EntidadFederativa> {

    private final ColumnConverter converter;

    public EntidadFederativaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link EntidadFederativa} stored in the database.
     */
    @Override
    public EntidadFederativa apply(Row row, String prefix) {
        EntidadFederativa entity = new EntidadFederativa();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setClave(converter.fromRow(row, prefix + "_clave", String.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setAbreviatura(converter.fromRow(row, prefix + "_abreviatura", String.class));
        return entity;
    }
}
