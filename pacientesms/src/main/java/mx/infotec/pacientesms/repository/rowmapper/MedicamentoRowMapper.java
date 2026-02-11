package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.Medicamento;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Medicamento}, with proper type conversions.
 */
@Service
public class MedicamentoRowMapper implements BiFunction<Row, String, Medicamento> {

    private final ColumnConverter converter;

    public MedicamentoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Medicamento} stored in the database.
     */
    @Override
    public Medicamento apply(Row row, String prefix) {
        Medicamento entity = new Medicamento();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setIngredienteActivo(converter.fromRow(row, prefix + "_ingrediente_activo", String.class));
        entity.setPresentacion(converter.fromRow(row, prefix + "_presentacion", String.class));
        return entity;
    }
}
