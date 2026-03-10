export interface IInfoSocioeconomica {
  id?: number;
  tipoVivienda?: string | null;
  materialVivienda?: string | null;
  numeroHabitaciones?: number | null;
  numeroHabitantes?: number | null;

  numHombres?: number | null;
  numMujeres?: number | null;

  serviciosDisponibles?: string | null;

  tieneTelefono?: boolean | null;
  numeroFijo?: string | null;
  numeroRecados?: string | null;
  numeroCelular?: string | null;
  padeceEnfermedad?: boolean | null;
  enfermedadCual?: string | null;

  hablaLenguaIndigena?: boolean | null;
  lenguaIndigenaCual?: string | null;
  hablaEspanol?: boolean | null;
  consideraIndigena?: boolean | null;
  grupoIndigenaCual?: string | null;

  sabeLeerEscribir?: boolean | null;

  mayorFuenteIngreso?: string | null;

  esJubiladoPensionado?: boolean | null;
  montoPension?: number | null;

  cuartosDormir?: number | null;

  materialTecho?: string | null;
  materialParedes?: string | null;

  tipoBano?: string | null;
  tipoDrenaje?: string | null;

  manejoBasura?: string | null;
  fuenteLuzElectrica?: string | null;

  combustibleCocina?: string | null;
  tenenciaVivienda?: string | null;

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

    public numHombres?: number | null,
    public numMujeres?: number | null,

    public serviciosDisponibles?: string | null,

    public tieneTelefono?: boolean | null,
    public numeroFijo?: string | null,
    public numeroRecados?: string | null,
    public numeroCelular?: string | null,

    public padeceEnfermedad?: boolean | null,
    public enfermedadCual?: string | null,

    public  hablaLenguaIndigena?: boolean | null,
    public  lenguaIndigenaCual?: string | null,
    public  hablaEspanol?: boolean | null,
    public  consideraIndigena?: boolean | null,
    public  grupoIndigenaCual?: string | null,

    public sabeLeerEscribir?: boolean | null,

    public mayorFuenteIngreso?: string | null,

    public esJubiladoPensionado?: boolean | null,
    public montoPension?: number | null,

    public cuartosDormir?: number | null,

    public materialTecho?: string | null,
    public materialParedes?: string | null,

    public tipoBano?: string | null,
    public tipoDrenaje?: string | null,

    public manejoBasura?: string | null,
    public fuenteLuzElectrica?: string | null,

    public combustibleCocina?: string | null,
    public tenenciaVivienda?: string | null,

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
    this.tieneTelefono = this.tieneTelefono ?? false;
  }
}