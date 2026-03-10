export interface IHistorialMedico {
  id?: number;
  pacienteId?: number | null; // Referencia para saber a quién pertenece
  altura?: number | null;
  peso?: number | null;
  imc?: string | null;
  grupoSanguineo?: string | null;
  factorRh?: string | null;
  alergias?: string | null;
  padecimientos?: string | null;
  enfermedadesCronicas?: string | null;
  cirugiasPrevias?: string | null; // Sustituye a antecedentesQuirurgicos
  medicamentosActuales?: string | null;
  antecedentesFamiliares?: string | null;
  antecedentesPatologicos?: string | null;
  antecedentesNoPatologicos?: string | null;
  antecedentesGinecoObstetricos?: string | null;
  consumoTabaco?: boolean | null; // Hábitos desglosados en booleanos
  consumoAlcohol?: boolean | null;
  consumoDrogas?: boolean | null;
  vacunas?: string | null; // Sustituye a esquemaVacunacion
  discapacidad?: string | null;
  observacionesGenerales?: string | null;
  fechaRegistroHistorial?: Date | null;
}

export class HistorialMedico implements IHistorialMedico {
  constructor(
    public id?: number,
    public pacienteId?: number | null,
    public altura?: number | null,
    public peso?: number | null,
    public imc?: string | null,
    public grupoSanguineo?: string | null,
    public factorRh?: string | null,
    public alergias?: string | null,
    public padecimientos?: string | null,
    public enfermedadesCronicas?: string | null,
    public cirugiasPrevias?: string | null,
    public medicamentosActuales?: string | null,
    public antecedentesFamiliares?: string | null,
    public antecedentesPatologicos?: string | null,
    public antecedentesNoPatologicos?: string | null,
    public antecedentesGinecoObstetricos?: string | null,
    public consumoTabaco?: boolean | null,
    public consumoAlcohol?: boolean | null,
    public consumoDrogas?: boolean | null,
    public vacunas?: string | null,
    public discapacidad?: string | null,
    public observacionesGenerales?: string | null,
    public fechaRegistroHistorial?: Date | null,
  ) {}
}