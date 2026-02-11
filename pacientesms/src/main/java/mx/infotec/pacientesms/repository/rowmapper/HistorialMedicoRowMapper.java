package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.HistorialMedico;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HistorialMedico}, with proper type conversions.
 */
@Service
public class HistorialMedicoRowMapper implements BiFunction<Row, String, HistorialMedico> {

    private final ColumnConverter converter;

    public HistorialMedicoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HistorialMedico} stored in the database.
     */
    @Override
    public HistorialMedico apply(Row row, String prefix) {
        HistorialMedico entity = new HistorialMedico();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAntecedentesQuirurgicos(converter.fromRow(row, prefix + "_antecedentes_quirurgicos", String.class));
        entity.setEsquemaVacunacion(converter.fromRow(row, prefix + "_esquema_vacunacion", String.class));
        entity.setHabitos(converter.fromRow(row, prefix + "_habitos", String.class));
        entity.setObservacionesGenerales(converter.fromRow(row, prefix + "_observaciones_generales", String.class));
        return entity;
    }
}
