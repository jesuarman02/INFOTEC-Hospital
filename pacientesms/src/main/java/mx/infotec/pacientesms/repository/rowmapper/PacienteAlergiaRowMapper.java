package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.PacienteAlergia;
import mx.infotec.pacientesms.domain.enumeration.GradoAlergia;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PacienteAlergia}, with proper type conversions.
 */
@Service
public class PacienteAlergiaRowMapper implements BiFunction<Row, String, PacienteAlergia> {

    private final ColumnConverter converter;

    public PacienteAlergiaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PacienteAlergia} stored in the database.
     */
    @Override
    public PacienteAlergia apply(Row row, String prefix) {
        PacienteAlergia entity = new PacienteAlergia();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFechaDeteccion(converter.fromRow(row, prefix + "_fecha_deteccion", LocalDate.class));
        entity.setReaccion(converter.fromRow(row, prefix + "_reaccion", String.class));
        entity.setGravedad(converter.fromRow(row, prefix + "_gravedad", GradoAlergia.class));
        entity.setPacienteId(converter.fromRow(row, prefix + "_paciente_id", Long.class));
        entity.setAlergiaId(converter.fromRow(row, prefix + "_alergia_id", Long.class));
        return entity;
    }
}
