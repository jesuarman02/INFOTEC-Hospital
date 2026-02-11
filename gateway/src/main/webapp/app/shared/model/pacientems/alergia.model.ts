export interface IAlergia {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
}

export class Alergia implements IAlergia {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string | null,
  ) {}
}
