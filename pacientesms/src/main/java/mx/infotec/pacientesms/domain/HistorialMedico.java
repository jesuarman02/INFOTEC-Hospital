package mx.infotec.pacientesms.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HistorialMedico.
 */
@Table("historial_medico")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistorialMedico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("datos_biometricos_sanguineos")
    private String datosBiometricosSanguineos;

    @Column("alergias")
    private String alergias;

    @Column("enfermedades_cronicas")
    private String enfermedadesCronicas;

    @Column("cirugias_previas")
    private String cirugiasPrevias;

    @Column("medicamentos_actuales")
    private String medicamentosActuales;

    @Column("antecedentes_familiares_hereditarios")
    private String antecedentesFamiliaresHereditarios;

    @Column("antecedentes_personales_patologicos")
    private String antecedentesPersonalesPatologicos;

    @Column("habitos_consumo_otros")
    private String habitosConsumoOtros;

    @Column("observaciones_generales")
    private String observacionesGenerales;

    @Column("estado")
    private String estado;

    @Column("paciente_id")
    private Long pacienteId;

    @Column("paciente_ecu")
    private Integer pacienteEcu;

    @Column("paciente_nombre")
    private String pacienteNombre;

    @Column("paciente_apellido_paterno")
    private String pacienteApellidoPaterno;
    
    // Agrega los public String getPacienteNombre()... y los setters correspondientes.

    @Transient
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistorialMedico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatosBiometricosSanguineos() {
        return this.datosBiometricosSanguineos;
    }

    public HistorialMedico datosBiometricosSanguineos(String datosBiometricosSanguineos) {
        this.setDatosBiometricosSanguineos(datosBiometricosSanguineos);
        return this;
    }

    public void setDatosBiometricosSanguineos(String datosBiometricosSanguineos) {
        this.datosBiometricosSanguineos = datosBiometricosSanguineos;
    }

    public String getAlergias() {
        return this.alergias;
    }

    public HistorialMedico alergias(String alergias) {
        this.setAlergias(alergias);
        return this;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getEnfermedadesCronicas() {
        return this.enfermedadesCronicas;
    }

    public HistorialMedico enfermedadesCronicas(String enfermedadesCronicas) {
        this.setEnfermedadesCronicas(enfermedadesCronicas);
        return this;
    }

    public void setEnfermedadesCronicas(String enfermedadesCronicas) {
        this.enfermedadesCronicas = enfermedadesCronicas;
    }

    public String getCirugiasPrevias() {
        return this.cirugiasPrevias;
    }

    public HistorialMedico cirugiasPrevias(String cirugiasPrevias) {
        this.setCirugiasPrevias(cirugiasPrevias);
        return this;
    }

    public void setCirugiasPrevias(String cirugiasPrevias) {
        this.cirugiasPrevias = cirugiasPrevias;
    }

    public String getMedicamentosActuales() {
        return this.medicamentosActuales;
    }

    public HistorialMedico medicamentosActuales(String medicamentosActuales) {
        this.setMedicamentosActuales(medicamentosActuales);
        return this;
    }

    public void setMedicamentosActuales(String medicamentosActuales) {
        this.medicamentosActuales = medicamentosActuales;
    }

    public String getAntecedentesFamiliaresHereditarios() {
        return this.antecedentesFamiliaresHereditarios;
    }

    public HistorialMedico antecedentesFamiliaresHereditarios(String antecedentesFamiliaresHereditarios) {
        this.setAntecedentesFamiliaresHereditarios(antecedentesFamiliaresHereditarios);
        return this;
    }

    public void setAntecedentesFamiliaresHereditarios(String antecedentesFamiliaresHereditarios) {
        this.antecedentesFamiliaresHereditarios = antecedentesFamiliaresHereditarios;
    }

    public String getAntecedentesPersonalesPatologicos() {
        return this.antecedentesPersonalesPatologicos;
    }

    public HistorialMedico antecedentesPersonalesPatologicos(String antecedentesPersonalesPatologicos) {
        this.setAntecedentesPersonalesPatologicos(antecedentesPersonalesPatologicos);
        return this;
    }

    public void setAntecedentesPersonalesPatologicos(String antecedentesPersonalesPatologicos) {
        this.antecedentesPersonalesPatologicos = antecedentesPersonalesPatologicos;
    }

    public String getHabitosConsumoOtros() {
        return this.habitosConsumoOtros;
    }

    public HistorialMedico habitosConsumoOtros(String habitosConsumoOtros) {
        this.setHabitosConsumoOtros(habitosConsumoOtros);
        return this;
    }

    public void setHabitosConsumoOtros(String habitosConsumoOtros) {
        this.habitosConsumoOtros = habitosConsumoOtros;
    }

    public String getObservacionesGenerales() {
        return this.observacionesGenerales;
    }

    public HistorialMedico observacionesGenerales(String observacionesGenerales) {
        this.setObservacionesGenerales(observacionesGenerales);
        return this;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }

    // Y hasta abajo del archivo, agrega sus Getters y Setters:
    public String getEstado() {
        return this.estado;
    }

    public HistorialMedico estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

        // Getter y Setter de pacienteNombre
    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    // Getter y Setter de pacienteApellidoPaterno
    public String getPacienteApellidoPaterno() {
        return pacienteApellidoPaterno;
    }

    public void setPacienteApellidoPaterno(String pacienteApellidoPaterno) {
        this.pacienteApellidoPaterno = pacienteApellidoPaterno;
    }

    // Y al final del archivo, agrega sus Getters y Setters:
    public Long getPacienteId() { return this.pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Integer getPacienteEcu() { return this.pacienteEcu; }
    public void setPacienteEcu(Integer pacienteEcu) { this.pacienteEcu = pacienteEcu; }
    

    public void setPaciente(Paciente paciente) {
        if (this.paciente != null) {
            this.paciente.setHistorialGeneral(null);
        }
        if (paciente != null) {
            paciente.setHistorialGeneral(this);
        }
        this.paciente = paciente;
    }

    public HistorialMedico paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistorialMedico)) {
            return false;
        }
        return getId() != null && getId().equals(((HistorialMedico) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistorialMedico{" +
            "id=" + getId() +
            "}";
    }
}