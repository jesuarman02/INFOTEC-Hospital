export interface IInfoSocioeconomica {
  id?: number;
  pacienteId?: number;
  pregunta?: string;
  respuesta?: string | null;
  respuestaAbierta?: string | null;
}

export class InfoSocioeconomica implements IInfoSocioeconomica {
  constructor(
    public id?: number,
    public pacienteId?: number,
    public pregunta?: string,
    public respuesta?: string | null,
    public respuestaAbierta?: string | null
  ) {}
}