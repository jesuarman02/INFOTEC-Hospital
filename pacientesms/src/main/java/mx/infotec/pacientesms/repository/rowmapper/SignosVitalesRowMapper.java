package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.SignosVitales;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SignosVitales}, with proper type conversions.
 */
@Service
public class SignosVitalesRowMapper implements BiFunction<Row, String, SignosVitales> {

    private final ColumnConverter converter;

    public SignosVitalesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SignosVitales} stored in the database.
     */
    @Override
    public SignosVitales apply(Row row, String prefix) {
        SignosVitales entity = new SignosVitales();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFechaRegistro(converter.fromRow(row, prefix + "_fecha_registro", Instant.class));
        entity.setFrecuenciaCardiaca(converter.fromRow(row, prefix + "_frecuencia_cardiaca", Integer.class));
        entity.setTensionArterial(converter.fromRow(row, prefix + "_tension_arterial", String.class));
        entity.setFrecuenciaRespiratoria(converter.fromRow(row, prefix + "_frecuencia_respiratoria", Integer.class));
        entity.setTemperatura(converter.fromRow(row, prefix + "_temperatura", BigDecimal.class));
        entity.setSaturacionOxigeno(converter.fromRow(row, prefix + "_saturacion_oxigeno", Integer.class));
        entity.setPeso(converter.fromRow(row, prefix + "_peso", BigDecimal.class));
        entity.setEstatura(converter.fromRow(row, prefix + "_estatura", BigDecimal.class));
        entity.setImc(converter.fromRow(row, prefix + "_imc", BigDecimal.class));
        entity.setPacienteId(converter.fromRow(row, prefix + "_paciente_id", Long.class));
        return entity;
    }
}
