package mx.infotec.pacientesms.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PacienteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("ecu", table, columnPrefix + "_ecu"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("apellido_paterno", table, columnPrefix + "_apellido_paterno"));
        columns.add(Column.aliased("apellido_materno", table, columnPrefix + "_apellido_materno"));
        columns.add(Column.aliased("sexo", table, columnPrefix + "_sexo"));
        columns.add(Column.aliased("embarazo", table, columnPrefix + "_embarazo"));
        columns.add(Column.aliased("nacionalidad", table, columnPrefix + "_nacionalidad"));
        columns.add(Column.aliased("fecha_nacimiento", table, columnPrefix + "_fecha_nacimiento"));
        columns.add(Column.aliased("estado_civil", table, columnPrefix + "_estado_civil"));
        columns.add(Column.aliased("curp", table, columnPrefix + "_curp"));

        columns.add(Column.aliased("direccion_id", table, columnPrefix + "_direccion_id"));
        columns.add(Column.aliased("info_socioeconomica_id", table, columnPrefix + "_info_socioeconomica_id"));
        columns.add(Column.aliased("historial_general_id", table, columnPrefix + "_historial_general_id"));
        columns.add(Column.aliased("entidad_nacimiento_id", table, columnPrefix + "_entidad_nacimiento_id"));
        return columns;
    }
}
