export interface IEnfermedad {
  id?: number;
  nombre?: string;
  tipo?: string | null;
  codigoCIE?: string | null;
}

export class Enfermedad implements IEnfermedad {
  constructor(
    public id?: number,
    public nombre?: string,
    public tipo?: string | null,
    public codigoCIE?: string | null,
  ) {}
}
