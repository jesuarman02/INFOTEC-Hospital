package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.Paciente;
import mx.infotec.pacientesms.domain.enumeration.Sexo;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Paciente}, with proper type conversions.
 */
@Service
public class PacienteRowMapper implements BiFunction<Row, String, Paciente> {

    private final ColumnConverter converter;

    public PacienteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Paciente} stored in the database.
     */
    @Override
    public Paciente apply(Row row, String prefix) {
        Paciente entity = new Paciente();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEcu(converter.fromRow(row, prefix + "_ecu", Integer.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setApellidoPaterno(converter.fromRow(row, prefix + "_apellido_paterno", String.class));
        entity.setApellidoMaterno(converter.fromRow(row, prefix + "_apellido_materno", String.class));
        entity.setSexo(converter.fromRow(row, prefix + "_sexo", Sexo.class));
        entity.setNacionalidad(converter.fromRow(row, prefix + "_nacionalidad", String.class));
        entity.setFechaNacimiento(converter.fromRow(row, prefix + "_fecha_nacimiento", LocalDate.class));
        entity.setEstadoCivil(converter.fromRow(row, prefix + "_estado_civil", String.class));
        entity.setCurp(converter.fromRow(row, prefix + "_curp", String.class));
        entity.setDireccionId(converter.fromRow(row, prefix + "_direccion_id", Long.class));
        entity.setInfoSocioeconomicaId(converter.fromRow(row, prefix + "_info_socioeconomica_id", Long.class));
        entity.setHistorialGeneralId(converter.fromRow(row, prefix + "_historial_general_id", Long.class));
        entity.setEntidadNacimientoId(converter.fromRow(row, prefix + "_entidad_nacimiento_id", Long.class));
        return entity;
    }
}
