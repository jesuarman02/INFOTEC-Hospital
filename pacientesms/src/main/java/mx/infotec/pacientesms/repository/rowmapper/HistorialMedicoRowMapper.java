package mx.infotec.pacientesms.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import mx.infotec.pacientesms.domain.HistorialMedico;
import org.springframework.stereotype.Service;

@Service
public class HistorialMedicoRowMapper implements BiFunction<Row, String, HistorialMedico> {

    private final ColumnConverter converter;

    public HistorialMedicoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    @Override
    public HistorialMedico apply(Row row, String prefix) {
        HistorialMedico entity = new HistorialMedico();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDatosBiometricosSanguineos(converter.fromRow(row, prefix + "_datos_biometricos_sanguineos", String.class));
        entity.setAlergias(converter.fromRow(row, prefix + "_alergias", String.class));
        entity.setEnfermedadesCronicas(converter.fromRow(row, prefix + "_enfermedades_cronicas", String.class));
        entity.setCirugiasPrevias(converter.fromRow(row, prefix + "_cirugias_previas", String.class));
        entity.setMedicamentosActuales(converter.fromRow(row, prefix + "_medicamentos_actuales", String.class));
        entity.setAntecedentesFamiliaresHereditarios(converter.fromRow(row, prefix + "_antecedentes_familiares_hereditarios", String.class));
        entity.setAntecedentesPersonalesPatologicos(converter.fromRow(row, prefix + "_antecedentes_personales_patologicos", String.class));
        entity.setHabitosConsumoOtros(converter.fromRow(row, prefix + "_habitos_consumo_otros", String.class));
        entity.setObservacionesGenerales(converter.fromRow(row, prefix + "_observaciones_generales", String.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", String.class));
        entity.setPacienteId(converter.fromRow(row, prefix + "_paciente_id", Long.class));
        entity.setPacienteEcu(converter.fromRow(row, prefix + "_paciente_ecu", Integer.class));
        entity.setPacienteNombre(converter.fromRow(row, prefix + "_paciente_nombre", String.class));
        entity.setPacienteApellidoPaterno(converter.fromRow(row, prefix + "_paciente_apellido_paterno", String.class));
        return entity;
    }
}