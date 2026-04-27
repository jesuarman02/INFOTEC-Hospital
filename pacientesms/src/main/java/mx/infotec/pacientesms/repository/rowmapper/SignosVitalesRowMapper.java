package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        entity.setFechaRegistro(converter.fromRow(row, prefix + "_fecha_registro", LocalDateTime.class));
        entity.setFrecuenciaCardiaca(converter.fromRow(row, prefix + "_frecuencia_cardiaca", Integer.class));
        entity.setTensionArterial(converter.fromRow(row, prefix + "_tension_arterial", String.class));
        entity.setFrecuenciaRespiratoria(converter.fromRow(row, prefix + "_frecuencia_respiratoria", Integer.class));
        entity.setTemperatura(converter.fromRow(row, prefix + "_temperatura", BigDecimal.class));
        entity.setSaturacionOxigeno(converter.fromRow(row, prefix + "_saturacion_oxigeno", Integer.class));
        
        // --- NUEVAS COLUMNAS (IDENTIFICACIÓN DEL PACIENTE) ---
        entity.setPacienteEcu(converter.fromRow(row, prefix + "_paciente_ecu", Integer.class));
        entity.setPacienteNombre(converter.fromRow(row, prefix + "_paciente_nombre", String.class));
        entity.setPacienteApellidoPaterno(converter.fromRow(row, prefix + "_paciente_apellido_paterno", String.class));
        
        // --- NUEVAS COLUMNAS (CONTEXTO Y EVALUACIÓN) ---
        entity.setTipo(converter.fromRow(row, prefix + "_tipo", String.class));
        entity.setPersonal(converter.fromRow(row, prefix + "_personal", String.class));
        entity.setGlucosa(converter.fromRow(row, prefix + "_glucosa", Integer.class));
        entity.setDolor(converter.fromRow(row, prefix + "_dolor", Integer.class));
        entity.setEstadoConciencia(converter.fromRow(row, prefix + "_estado_conciencia", String.class));
        entity.setObservaciones(converter.fromRow(row, prefix + "_observaciones", String.class));

        // --- RELACIÓN ORIGINAL DE JHIPSTER ---
        entity.setPacienteId(converter.fromRow(row, prefix + "_paciente_id", Long.class));
        return entity;
    }
}