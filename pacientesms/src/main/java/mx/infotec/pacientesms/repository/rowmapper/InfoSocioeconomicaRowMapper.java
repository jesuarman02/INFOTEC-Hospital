package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
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
        entity.setTipoVivienda(converter.fromRow(row, prefix + "_tipo_vivienda", String.class));
        entity.setMaterialVivienda(converter.fromRow(row, prefix + "_material_vivienda", String.class));
        entity.setNumeroHabitaciones(converter.fromRow(row, prefix + "_numero_habitaciones", Integer.class));
        entity.setNumeroHabitantes(converter.fromRow(row, prefix + "_numero_habitantes", Integer.class));
        entity.setServiciosDisponibles(converter.fromRow(row, prefix + "_servicios_disponibles", String.class));
        entity.setIngresoMensual(converter.fromRow(row, prefix + "_ingreso_mensual", BigDecimal.class));
        entity.setIngresoMensualHogar(converter.fromRow(row, prefix + "_ingreso_mensual_hogar", BigDecimal.class));
        entity.setGastoMensual(converter.fromRow(row, prefix + "_gasto_mensual", BigDecimal.class));
        entity.setPersonasDependientes(converter.fromRow(row, prefix + "_personas_dependientes", Integer.class));
        entity.setApoyosGubernamentales(converter.fromRow(row, prefix + "_apoyos_gubernamentales", String.class));
        entity.setOcupacionActual(converter.fromRow(row, prefix + "_ocupacion_actual", String.class));
        entity.setCondicionLaboral(converter.fromRow(row, prefix + "_condicion_laboral", String.class));
        entity.setTipoEmpleo(converter.fromRow(row, prefix + "_tipo_empleo", String.class));
        entity.setLugarTrabajo(converter.fromRow(row, prefix + "_lugar_trabajo", String.class));
        entity.setTiempoEmpleado(converter.fromRow(row, prefix + "_tiempo_empleado", String.class));
        entity.setGradoMaximoEstudios(converter.fromRow(row, prefix + "_grado_maximo_estudios", String.class));
        entity.setAniosEstudio(converter.fromRow(row, prefix + "_anios_estudio", Integer.class));
        entity.setEstudia(converter.fromRow(row, prefix + "_estudia", Boolean.class));
        entity.setInstitucionMedica(converter.fromRow(row, prefix + "_institucion_medica", String.class));
        entity.setTipoAfiliacion(converter.fromRow(row, prefix + "_tipo_afiliacion", String.class));
        entity.setNumeroAfiliacion(converter.fromRow(row, prefix + "_numero_afiliacion", String.class));
        entity.setMedioTransporte(converter.fromRow(row, prefix + "_medio_transporte", String.class));
        entity.setTiempoTraslado(converter.fromRow(row, prefix + "_tiempo_traslado", Integer.class));
        entity.setFechaActualizacion(converter.fromRow(row, prefix + "_fecha_actualizacion", LocalDate.class));
        return entity;
    }
}
