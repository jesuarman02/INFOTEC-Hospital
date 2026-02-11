package mx.infotec.pacientesms.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class InfoSocioeconomicaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("tipo_vivienda", table, columnPrefix + "_tipo_vivienda"));
        columns.add(Column.aliased("material_vivienda", table, columnPrefix + "_material_vivienda"));
        columns.add(Column.aliased("numero_habitaciones", table, columnPrefix + "_numero_habitaciones"));
        columns.add(Column.aliased("numero_habitantes", table, columnPrefix + "_numero_habitantes"));
        columns.add(Column.aliased("servicios_disponibles", table, columnPrefix + "_servicios_disponibles"));
        columns.add(Column.aliased("ingreso_mensual", table, columnPrefix + "_ingreso_mensual"));
        columns.add(Column.aliased("ingreso_mensual_hogar", table, columnPrefix + "_ingreso_mensual_hogar"));
        columns.add(Column.aliased("gasto_mensual", table, columnPrefix + "_gasto_mensual"));
        columns.add(Column.aliased("personas_dependientes", table, columnPrefix + "_personas_dependientes"));
        columns.add(Column.aliased("apoyos_gubernamentales", table, columnPrefix + "_apoyos_gubernamentales"));
        columns.add(Column.aliased("ocupacion_actual", table, columnPrefix + "_ocupacion_actual"));
        columns.add(Column.aliased("condicion_laboral", table, columnPrefix + "_condicion_laboral"));
        columns.add(Column.aliased("tipo_empleo", table, columnPrefix + "_tipo_empleo"));
        columns.add(Column.aliased("lugar_trabajo", table, columnPrefix + "_lugar_trabajo"));
        columns.add(Column.aliased("tiempo_empleado", table, columnPrefix + "_tiempo_empleado"));
        columns.add(Column.aliased("grado_maximo_estudios", table, columnPrefix + "_grado_maximo_estudios"));
        columns.add(Column.aliased("anios_estudio", table, columnPrefix + "_anios_estudio"));
        columns.add(Column.aliased("estudia", table, columnPrefix + "_estudia"));
        columns.add(Column.aliased("institucion_medica", table, columnPrefix + "_institucion_medica"));
        columns.add(Column.aliased("tipo_afiliacion", table, columnPrefix + "_tipo_afiliacion"));
        columns.add(Column.aliased("numero_afiliacion", table, columnPrefix + "_numero_afiliacion"));
        columns.add(Column.aliased("medio_transporte", table, columnPrefix + "_medio_transporte"));
        columns.add(Column.aliased("tiempo_traslado", table, columnPrefix + "_tiempo_traslado"));
        columns.add(Column.aliased("fecha_actualizacion", table, columnPrefix + "_fecha_actualizacion"));

        return columns;
    }
}
