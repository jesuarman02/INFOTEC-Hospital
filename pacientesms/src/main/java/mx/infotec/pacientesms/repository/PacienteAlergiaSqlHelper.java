package mx.infotec.pacientesms.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PacienteAlergiaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("fecha_deteccion", table, columnPrefix + "_fecha_deteccion"));
        columns.add(Column.aliased("reaccion", table, columnPrefix + "_reaccion"));
        columns.add(Column.aliased("gravedad", table, columnPrefix + "_gravedad"));

        columns.add(Column.aliased("paciente_id", table, columnPrefix + "_paciente_id"));
        columns.add(Column.aliased("alergia_id", table, columnPrefix + "_alergia_id"));
        return columns;
    }
}
