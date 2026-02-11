export interface ICodigoPostal {
  id?: number;
  codigo?: string;
  asentamiento?: string | null;
  tipoAsentamiento?: string | null;
  municipio?: string | null;
  estado?: string | null;
  ciudad?: string | null;
  codigoPostalAdministracion?: string | null;
  claveEstado?: string | null;
  claveOficina?: string | null;
  claveCP?: string | null;
  claveTipoAsentamiento?: string | null;
  claveMunicipio?: string | null;
  idAsentamientoCons?: string | null;
  zona?: string | null;
  claveCiudad?: string | null;
}

export class CodigoPostal implements ICodigoPostal {
  constructor(
    public id?: number,
    public codigo?: string,
    public asentamiento?: string | null,
    public tipoAsentamiento?: string | null,
    public municipio?: string | null,
    public estado?: string | null,
    public ciudad?: string | null,
    public codigoPostalAdministracion?: string | null,
    public claveEstado?: string | null,
    public claveOficina?: string | null,
    public claveCP?: string | null,
    public claveTipoAsentamiento?: string | null,
    public claveMunicipio?: string | null,
    public idAsentamientoCons?: string | null,
    public zona?: string | null,
    public claveCiudad?: string | null,
  ) {}
}
