package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.Cita;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Cita}, with proper type conversions.
 */
@Service
public class CitaRowMapper implements BiFunction<Row, String, Cita> {

    private final ColumnConverter converter;

    public CitaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Cita} stored in the database.
     */
    @Override
    public Cita apply(Row row, String prefix) {
        Cita entity = new Cita();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEcu(converter.fromRow(row, prefix + "_ecu", Integer.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setApellidoPaterno(converter.fromRow(row, prefix + "_apellido_paterno", String.class));
        entity.setApellidoMaterno(converter.fromRow(row, prefix + "_apellido_materno", String.class));
        entity.setFechaHora(converter.fromRow(row, prefix + "_fecha_hora", ZonedDateTime.class));
        entity.setMotivo(converter.fromRow(row, prefix + "_motivo", String.class));
        return entity;
    }
}
