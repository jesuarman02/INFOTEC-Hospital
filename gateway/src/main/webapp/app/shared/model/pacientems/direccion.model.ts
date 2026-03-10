import { type ITipoVialidad } from '@/shared/model/pacientesms/tipo-vialidad.model';
import { type ICodigoPostal } from '@/shared/model/pacientesms/codigo-postal.model';
import { type IEntidadFederativa } from '@/shared/model/pacientesms/entidad-federativa.model';

export interface IDireccion {
  id?: number;
  nombreVialidad?: string | null;
  numExterior?: string | null;
  numInterior?: string | null;
  telefono?: string | null;
  tipoVialidad?: ITipoVialidad | null;
  codigoPostalInfo?: ICodigoPostal | null;
  entidadFederativa?: IEntidadFederativa | null;

  // --- NUEVOS CAMPOS DEL PACIENTE ---
  pacienteId?: number | null;
  pacienteEcu?: number | null;
  pacienteNombre?: string | null;
  pacienteApellidoPaterno?: string | null;
}

export class Direccion implements IDireccion {
  constructor(
    public id?: number,
    public nombreVialidad?: string | null,
    public numExterior?: string | null,
    public numInterior?: string | null,
    public telefono?: string | null,
    public tipoVialidad?: ITipoVialidad | null,
    public codigoPostalInfo?: ICodigoPostal | null,
    public entidadFederativa?: IEntidadFederativa | null,
    // --- NUEVOS CAMPOS DEL PACIENTE ---
    public pacienteId?: number | null,
    public pacienteEcu?: number | null,
    public pacienteNombre?: string | null,
    public pacienteApellidoPaterno?: string | null
  ) {}
}