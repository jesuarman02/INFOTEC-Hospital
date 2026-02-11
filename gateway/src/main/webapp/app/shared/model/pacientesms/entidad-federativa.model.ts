export interface IEntidadFederativa {
  id?: number;
  clave?: string;
  nombre?: string;
  abreviatura?: string | null;
}

export class EntidadFederativa implements IEntidadFederativa {
  constructor(
    public id?: number,
    public clave?: string,
    public nombre?: string,
    public abreviatura?: string | null,
  ) {}
}
