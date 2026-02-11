export interface IInfoSocioeconomica {
  id?: number;
  tipoVivienda?: string | null;
  materialVivienda?: string | null;
  numeroHabitaciones?: number | null;
  numeroHabitantes?: number | null;
  serviciosDisponibles?: string | null;
  ingresoMensual?: number | null;
  ingresoMensualHogar?: number | null;
  gastoMensual?: number | null;
  personasDependientes?: number | null;
  apoyosGubernamentales?: string | null;
  ocupacionActual?: string | null;
  condicionLaboral?: string | null;
  tipoEmpleo?: string | null;
  lugarTrabajo?: string | null;
  tiempoEmpleado?: string | null;
  gradoMaximoEstudios?: string | null;
  aniosEstudio?: number | null;
  estudia?: boolean | null;
  institucionMedica?: string | null;
  tipoAfiliacion?: string | null;
  numeroAfiliacion?: string | null;
  medioTransporte?: string | null;
  tiempoTraslado?: number | null;
  fechaActualizacion?: Date | null;
}

export class InfoSocioeconomica implements IInfoSocioeconomica {
  constructor(
    public id?: number,
    public tipoVivienda?: string | null,
    public materialVivienda?: string | null,
    public numeroHabitaciones?: number | null,
    public numeroHabitantes?: number | null,
    public serviciosDisponibles?: string | null,
    public ingresoMensual?: number | null,
    public ingresoMensualHogar?: number | null,
    public gastoMensual?: number | null,
    public personasDependientes?: number | null,
    public apoyosGubernamentales?: string | null,
    public ocupacionActual?: string | null,
    public condicionLaboral?: string | null,
    public tipoEmpleo?: string | null,
    public lugarTrabajo?: string | null,
    public tiempoEmpleado?: string | null,
    public gradoMaximoEstudios?: string | null,
    public aniosEstudio?: number | null,
    public estudia?: boolean | null,
    public institucionMedica?: string | null,
    public tipoAfiliacion?: string | null,
    public numeroAfiliacion?: string | null,
    public medioTransporte?: string | null,
    public tiempoTraslado?: number | null,
    public fechaActualizacion?: Date | null,
  ) {
    this.estudia = this.estudia ?? false;
  }
}
