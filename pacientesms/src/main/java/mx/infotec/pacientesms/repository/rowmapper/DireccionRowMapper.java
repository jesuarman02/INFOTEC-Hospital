package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.Direccion;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Direccion}, with proper type conversions.
 */
@Service
public class DireccionRowMapper implements BiFunction<Row, String, Direccion> {

    private final ColumnConverter converter;

    public DireccionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Direccion} stored in the database.
     */
    @Override
    public Direccion apply(Row row, String prefix) {
        Direccion entity = new Direccion();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombreVialidad(converter.fromRow(row, prefix + "_nombre_vialidad", String.class));
        entity.setNumExterior(converter.fromRow(row, prefix + "_num_exterior", String.class));
        entity.setNumInterior(converter.fromRow(row, prefix + "_num_interior", String.class));
        entity.setTelefono(converter.fromRow(row, prefix + "_telefono", String.class));
        entity.setTipoVialidadId(converter.fromRow(row, prefix + "_tipo_vialidad_id", Long.class));
        entity.setCodigoPostalInfoId(converter.fromRow(row, prefix + "_codigo_postal_info_id", Long.class));
        entity.setEntidadFederativaId(converter.fromRow(row, prefix + "_entidad_federativa_id", Long.class));
        return entity;
    }
}
