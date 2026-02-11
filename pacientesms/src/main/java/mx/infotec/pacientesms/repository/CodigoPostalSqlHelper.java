package mx.infotec.pacientesms.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CodigoPostalSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("codigo", table, columnPrefix + "_codigo"));
        columns.add(Column.aliased("asentamiento", table, columnPrefix + "_asentamiento"));
        columns.add(Column.aliased("tipo_asentamiento", table, columnPrefix + "_tipo_asentamiento"));
        columns.add(Column.aliased("municipio", table, columnPrefix + "_municipio"));
        columns.add(Column.aliased("estado", table, columnPrefix + "_estado"));
        columns.add(Column.aliased("ciudad", table, columnPrefix + "_ciudad"));
        columns.add(Column.aliased("codigo_postal_administracion", table, columnPrefix + "_codigo_postal_administracion"));
        columns.add(Column.aliased("clave_estado", table, columnPrefix + "_clave_estado"));
        columns.add(Column.aliased("clave_oficina", table, columnPrefix + "_clave_oficina"));
        columns.add(Column.aliased("clave_cp", table, columnPrefix + "_clave_cp"));
        columns.add(Column.aliased("clave_tipo_asentamiento", table, columnPrefix + "_clave_tipo_asentamiento"));
        columns.add(Column.aliased("clave_municipio", table, columnPrefix + "_clave_municipio"));
        columns.add(Column.aliased("id_asentamiento_cons", table, columnPrefix + "_id_asentamiento_cons"));
        columns.add(Column.aliased("zona", table, columnPrefix + "_zona"));
        columns.add(Column.aliased("clave_ciudad", table, columnPrefix + "_clave_ciudad"));

        return columns;
    }
}
