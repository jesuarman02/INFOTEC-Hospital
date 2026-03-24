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
        
        // --- NUEVAS COLUMNAS (IDENTIFICACIÓN DEL PACIENTE) ---
        columns.add(Column.aliased("paciente_ecu", table, columnPrefix + "_paciente_ecu"));
        columns.add(Column.aliased("paciente_nombre", table, columnPrefix + "_paciente_nombre"));
        columns.add(Column.aliased("paciente_apellido_paterno", table, columnPrefix + "_paciente_apellido_paterno"));
        
        // --- NUEVAS COLUMNAS (CONTEXTO Y EVALUACIÓN) ---
        columns.add(Column.aliased("tipo", table, columnPrefix + "_tipo"));
        columns.add(Column.aliased("personal", table, columnPrefix + "_personal"));
        columns.add(Column.aliased("glucosa", table, columnPrefix + "_glucosa"));
        columns.add(Column.aliased("dolor", table, columnPrefix + "_dolor"));
        columns.add(Column.aliased("estado_conciencia", table, columnPrefix + "_estado_conciencia"));
        columns.add(Column.aliased("observaciones", table, columnPrefix + "_observaciones"));

        // --- RELACIÓN ORIGINAL DE JHIPSTER ---
        columns.add(Column.aliased("paciente_id", table, columnPrefix + "_paciente_id"));
        
        return columns;
    }
}