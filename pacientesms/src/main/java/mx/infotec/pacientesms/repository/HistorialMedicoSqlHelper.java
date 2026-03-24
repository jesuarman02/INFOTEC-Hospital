package mx.infotec.pacientesms.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class HistorialMedicoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("datos_biometricos_sanguineos", table, columnPrefix + "_datos_biometricos_sanguineos"));
        columns.add(Column.aliased("alergias", table, columnPrefix + "_alergias"));
        columns.add(Column.aliased("enfermedades_cronicas", table, columnPrefix + "_enfermedades_cronicas"));
        columns.add(Column.aliased("cirugias_previas", table, columnPrefix + "_cirugias_previas"));
        columns.add(Column.aliased("medicamentos_actuales", table, columnPrefix + "_medicamentos_actuales"));
        columns.add(Column.aliased("antecedentes_familiares_hereditarios", table, columnPrefix + "_antecedentes_familiares_hereditarios"));
        columns.add(Column.aliased("antecedentes_personales_patologicos", table, columnPrefix + "_antecedentes_personales_patologicos"));
        columns.add(Column.aliased("habitos_consumo_otros", table, columnPrefix + "_habitos_consumo_otros"));
        columns.add(Column.aliased("observaciones_generales", table, columnPrefix + "_observaciones_generales"));
        columns.add(Column.aliased("estado", table, columnPrefix + "_estado"));
        columns.add(Column.aliased("paciente_id", table, columnPrefix + "_paciente_id"));
        columns.add(Column.aliased("paciente_ecu", table, columnPrefix + "_paciente_ecu"));
        columns.add(Column.aliased("paciente_nombre", table, columnPrefix + "_paciente_nombre"));
        columns.add(Column.aliased("paciente_apellido_paterno", table, columnPrefix + "_paciente_apellido_paterno"));

        return columns;
    }
}