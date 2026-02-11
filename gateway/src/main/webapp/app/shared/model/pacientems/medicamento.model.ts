export interface IMedicamento {
  id?: number;
  nombre?: string;
  ingredienteActivo?: string | null;
  presentacion?: string | null;
}

export class Medicamento implements IMedicamento {
  constructor(
    public id?: number,
    public nombre?: string,
    public ingredienteActivo?: string | null,
    public presentacion?: string | null,
  ) {}
}
