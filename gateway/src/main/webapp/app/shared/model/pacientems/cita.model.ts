export interface ICita {
  id?: number;
  ecu?: number;
  nombre?: string;
  apellidoPaterno?: string;
  apellidoMaterno?: string | null;
  fechaHora?: Date;
  motivo?: string;
}

export class Cita implements ICita {
  constructor(
    public id?: number,
    public ecu?: number,
    public nombre?: string,
    public apellidoPaterno?: string,
    public apellidoMaterno?: string | null,
    public fechaHora?: Date,
    public motivo?: string,
  ) {}
}
