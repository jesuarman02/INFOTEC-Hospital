package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.PacienteMedicamento;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PacienteMedicamento}, with proper type conversions.
 */
@Service
public class PacienteMedicamentoRowMapper implements BiFunction<Row, String, PacienteMedicamento> {

    private final ColumnConverter converter;

    public PacienteMedicamentoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PacienteMedicamento} stored in the database.
     */
    @Override
    public PacienteMedicamento apply(Row row, String prefix) {
        PacienteMedicamento entity = new PacienteMedicamento();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDosis(converter.fromRow(row, prefix + "_dosis", String.class));
        entity.setFrecuencia(converter.fromRow(row, prefix + "_frecuencia", String.class));
        entity.setFechaInicio(converter.fromRow(row, prefix + "_fecha_inicio", LocalDate.class));
        entity.setFechaFin(converter.fromRow(row, prefix + "_fecha_fin", LocalDate.class));
        entity.setActivo(converter.fromRow(row, prefix + "_activo", Boolean.class));
        entity.setPacienteId(converter.fromRow(row, prefix + "_paciente_id", Long.class));
        entity.setMedicamentoId(converter.fromRow(row, prefix + "_medicamento_id", Long.class));
        return entity;
    }
}
