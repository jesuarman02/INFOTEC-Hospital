package mx.infotec.pacientesms.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DireccionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre_vialidad", table, columnPrefix + "_nombre_vialidad"));
        columns.add(Column.aliased("num_exterior", table, columnPrefix + "_num_exterior"));
        columns.add(Column.aliased("num_interior", table, columnPrefix + "_num_interior"));
        columns.add(Column.aliased("telefono", table, columnPrefix + "_telefono"));

        columns.add(Column.aliased("tipo_vialidad_id", table, columnPrefix + "_tipo_vialidad_id"));
        columns.add(Column.aliased("codigo_postal_info_id", table, columnPrefix + "_codigo_postal_info_id"));
        columns.add(Column.aliased("entidad_federativa_id", table, columnPrefix + "_entidad_federativa_id"));
        return columns;
    }
}
