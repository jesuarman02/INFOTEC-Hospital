package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.InfoSocioeconomica;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link InfoSocioeconomica}, with proper type conversions.
 */
@Service
public class InfoSocioeconomicaRowMapper implements BiFunction<Row, String, InfoSocioeconomica> {

    private final ColumnConverter converter;

    public InfoSocioeconomicaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InfoSocioeconomica} stored in the database.
     */
    @Override
    public InfoSocioeconomica apply(Row row, String prefix) {
        InfoSocioeconomica entity = new InfoSocioeconomica();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        
        // 👇 Nuestro mapeo limpio 👇
        entity.setPacienteId(converter.fromRow(row, prefix + "_paciente_id", Long.class));
        entity.setPregunta(converter.fromRow(row, prefix + "_pregunta", String.class));
        entity.setRespuesta(converter.fromRow(row, prefix + "_respuesta", String.class));
        entity.setRespuestaAbierta(converter.fromRow(row, prefix + "_respuesta_abierta", String.class));
        
        return entity;
    }
}