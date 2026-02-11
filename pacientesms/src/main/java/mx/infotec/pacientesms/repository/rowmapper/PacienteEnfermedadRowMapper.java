package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.PacienteEnfermedad;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PacienteEnfermedad}, with proper type conversions.
 */
@Service
public class PacienteEnfermedadRowMapper implements BiFunction<Row, String, PacienteEnfermedad> {

    private final ColumnConverter converter;

    public PacienteEnfermedadRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PacienteEnfermedad} stored in the database.
     */
    @Override
    public PacienteEnfermedad apply(Row row, String prefix) {
        PacienteEnfermedad entity = new PacienteEnfermedad();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFechaDiagnostico(converter.fromRow(row, prefix + "_fecha_diagnostico", LocalDate.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", String.class));
        entity.setNotas(converter.fromRow(row, prefix + "_notas", String.class));
        entity.setPacienteId(converter.fromRow(row, prefix + "_paciente_id", Long.class));
        entity.setEnfermedadId(converter.fromRow(row, prefix + "_enfermedad_id", Long.class));
        return entity;
    }
}
