package mx.infotec.pacientesms.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistorialMedicoDTO implements Serializable {

    private Long id;

    @Lob
    private String datosBiometricosSanguineos;

    @Lob
    private String alergias;

    @Lob
    private String enfermedadesCronicas;

    @Lob
    private String cirugiasPrevias;

    @Lob
    private String medicamentosActuales;

    @Lob
    private String antecedentesFamiliaresHereditarios;

    @Lob
    private String antecedentesPersonalesPatologicos;

    @Lob
    private String habitosConsumoOtros;

    @Lob
    private String observacionesGenerales;

    private String estado;

    // ==========================================
    // PACIENTE (Para mostrar en Front como en Direccion)
    // ==========================================
    private Long pacienteId;
    private Integer pacienteEcu;
    private String pacienteNombre;
    private String pacienteApellidoPaterno;

    // --- GETTERS Y SETTERS ORIGINALES ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDatosBiometricosSanguineos() { return datosBiometricosSanguineos; }
    public void setDatosBiometricosSanguineos(String datosBiometricosSanguineos) { this.datosBiometricosSanguineos = datosBiometricosSanguineos; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getEnfermedadesCronicas() { return enfermedadesCronicas; }
    public void setEnfermedadesCronicas(String enfermedadesCronicas) { this.enfermedadesCronicas = enfermedadesCronicas; }

    public String getCirugiasPrevias() { return cirugiasPrevias; }
    public void setCirugiasPrevias(String cirugiasPrevias) { this.cirugiasPrevias = cirugiasPrevias; }

    public String getMedicamentosActuales() { return medicamentosActuales; }
    public void setMedicamentosActuales(String medicamentosActuales) { this.medicamentosActuales = medicamentosActuales; }

    public String getAntecedentesFamiliaresHereditarios() { return antecedentesFamiliaresHereditarios; }
    public void setAntecedentesFamiliaresHereditarios(String antecedentesFamiliaresHereditarios) { this.antecedentesFamiliaresHereditarios = antecedentesFamiliaresHereditarios; }

    public String getAntecedentesPersonalesPatologicos() { return antecedentesPersonalesPatologicos; }
    public void setAntecedentesPersonalesPatologicos(String antecedentesPersonalesPatologicos) { this.antecedentesPersonalesPatologicos = antecedentesPersonalesPatologicos; }

    public String getHabitosConsumoOtros() { return habitosConsumoOtros; }
    public void setHabitosConsumoOtros(String habitosConsumoOtros) { this.habitosConsumoOtros = habitosConsumoOtros; }

    public String getObservacionesGenerales() { return observacionesGenerales; }
    public void setObservacionesGenerales(String observacionesGenerales) { this.observacionesGenerales = observacionesGenerales; }

    // Con sus respectivos getter y setter
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // --- GETTERS Y SETTERS DEL PACIENTE ---
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Integer getPacienteEcu() { return pacienteEcu; }
    public void setPacienteEcu(Integer pacienteEcu) { this.pacienteEcu = pacienteEcu; }

    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }

    public String getPacienteApellidoPaterno() { return pacienteApellidoPaterno; }
    public void setPacienteApellidoPaterno(String pacienteApellidoPaterno) { this.pacienteApellidoPaterno = pacienteApellidoPaterno; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof HistorialMedicoDTO)) { return false; }
        HistorialMedicoDTO historialMedicoDTO = (HistorialMedicoDTO) o;
        if (this.id == null) { return false; }
        return Objects.equals(this.id, historialMedicoDTO.id);
    }

    @Override
    public int hashCode() { return Objects.hash(this.id); }

    @Override
    public String toString() { return "HistorialMedicoDTO{" + "id=" + getId() + "}"; }
}