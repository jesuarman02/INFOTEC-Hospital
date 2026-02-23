package mx.infotec.pacientesms.domain.enumeration;

public enum EstadoCivil {
    NO_ESPECIFICADO("0"),
    NO_APLICA("8"),
    SE_IGNORA("9"),
    CASADO("C"),
    DIVORCIADO("D"),
    SEPARADO("P"),
    SOLTERO("S"),
    UNION_LIBRE("U"),
    VIUDO("V");

    private final String codigo;

    EstadoCivil(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
