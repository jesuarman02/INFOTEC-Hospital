package mx.infotec.pacientesms.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CitaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("ecu", table, columnPrefix + "_ecu"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("apellido_paterno", table, columnPrefix + "_apellido_paterno"));
        columns.add(Column.aliased("apellido_materno", table, columnPrefix + "_apellido_materno"));
        columns.add(Column.aliased("fecha_hora", table, columnPrefix + "_fecha_hora"));
        columns.add(Column.aliased("motivo", table, columnPrefix + "_motivo"));

        return columns;
    }
}
