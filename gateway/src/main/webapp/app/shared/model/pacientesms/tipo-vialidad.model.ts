export interface ITipoVialidad {
  id?: number;
  nombre?: string;
}

export class TipoVialidad implements ITipoVialidad {
  constructor(
    public id?: number,
    public nombre?: string,
  ) {}
}
