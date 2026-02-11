package mx.infotec.pacientesms.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SignosVitalesSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("fecha_registro", table, columnPrefix + "_fecha_registro"));
        columns.add(Column.aliased("frecuencia_cardiaca", table, columnPrefix + "_frecuencia_cardiaca"));
        columns.add(Column.aliased("tension_arterial", table, columnPrefix + "_tension_arterial"));
        columns.add(Column.aliased("frecuencia_respiratoria", table, columnPrefix + "_frecuencia_respiratoria"));
        columns.add(Column.aliased("temperatura", table, columnPrefix + "_temperatura"));
        columns.add(Column.aliased("saturacion_oxigeno", table, columnPrefix + "_saturacion_oxigeno"));
        columns.add(Column.aliased("peso", table, columnPrefix + "_peso"));
        columns.add(Column.aliased("estatura", table, columnPrefix + "_estatura"));
        columns.add(Column.aliased("imc", table, columnPrefix + "_imc"));

        columns.add(Column.aliased("paciente_id", table, columnPrefix + "_paciente_id"));
        return columns;
    }
}
