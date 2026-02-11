export interface IHistorialMedico {
  id?: number;
  antecedentesQuirurgicos?: string | null;
  esquemaVacunacion?: string | null;
  habitos?: string | null;
  observacionesGenerales?: string | null;
}

export class HistorialMedico implements IHistorialMedico {
  constructor(
    public id?: number,
    public antecedentesQuirurgicos?: string | null,
    public esquemaVacunacion?: string | null,
    public habitos?: string | null,
    public observacionesGenerales?: string | null,
  ) {}
}
