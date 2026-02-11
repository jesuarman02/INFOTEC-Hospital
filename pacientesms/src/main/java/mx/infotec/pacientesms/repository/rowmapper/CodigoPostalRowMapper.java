package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.CodigoPostal;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CodigoPostal}, with proper type conversions.
 */
@Service
public class CodigoPostalRowMapper implements BiFunction<Row, String, CodigoPostal> {

    private final ColumnConverter converter;

    public CodigoPostalRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CodigoPostal} stored in the database.
     */
    @Override
    public CodigoPostal apply(Row row, String prefix) {
        CodigoPostal entity = new CodigoPostal();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCodigo(converter.fromRow(row, prefix + "_codigo", String.class));
        entity.setAsentamiento(converter.fromRow(row, prefix + "_asentamiento", String.class));
        entity.setTipoAsentamiento(converter.fromRow(row, prefix + "_tipo_asentamiento", String.class));
        entity.setMunicipio(converter.fromRow(row, prefix + "_municipio", String.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", String.class));
        entity.setCiudad(converter.fromRow(row, prefix + "_ciudad", String.class));
        entity.setCodigoPostalAdministracion(converter.fromRow(row, prefix + "_codigo_postal_administracion", String.class));
        entity.setClaveEstado(converter.fromRow(row, prefix + "_clave_estado", String.class));
        entity.setClaveOficina(converter.fromRow(row, prefix + "_clave_oficina", String.class));
        entity.setClaveCP(converter.fromRow(row, prefix + "_clave_cp", String.class));
        entity.setClaveTipoAsentamiento(converter.fromRow(row, prefix + "_clave_tipo_asentamiento", String.class));
        entity.setClaveMunicipio(converter.fromRow(row, prefix + "_clave_municipio", String.class));
        entity.setIdAsentamientoCons(converter.fromRow(row, prefix + "_id_asentamiento_cons", String.class));
        entity.setZona(converter.fromRow(row, prefix + "_zona", String.class));
        entity.setClaveCiudad(converter.fromRow(row, prefix + "_clave_ciudad", String.class));
        return entity;
    }
}
