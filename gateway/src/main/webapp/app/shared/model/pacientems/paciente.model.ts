import { type IDireccion } from '@/shared/model/pacientems/direccion.model';
import { type IInfoSocioeconomica } from '@/shared/model/pacientems/info-socioeconomica.model';
import { type IHistorialMedico } from '@/shared/model/pacientems/historial-medico.model';
// Ruta corregida según tu indicación (pacientesms)
import { type IEntidadFederativa } from '@/shared/model/pacientesms/entidad-federativa.model';

import { type Sexo } from '@/shared/model/enumerations/sexo.model';

export interface IPaciente {
  id?: number;
  ecu?: number;
  nombre?: string | null;
  apellidoPaterno?: string | null;
  apellidoMaterno?: string | null;
  sexo?: Sexo | null;
  nacionalidad?: string | null;
  fechaNacimiento?: Date | null;
  estadoCivil?: string | null;
  curp?: string | null;
  direccion?: IDireccion | null;
  infoSocioeconomica?: IInfoSocioeconomica | null;
  historialGeneral?: IHistorialMedico | null;
  entidadNacimiento?: IEntidadFederativa | null;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public ecu?: number,
    public nombre?: string | null,
    public apellidoPaterno?: string | null,
    public apellidoMaterno?: string | null,
    public sexo?: Sexo | null,
    public nacionalidad?: string | null,
    public fechaNacimiento?: Date | null,
    public estadoCivil?: string | null,
    public curp?: string | null,
    public direccion?: IDireccion | null,
    public infoSocioeconomica?: IInfoSocioeconomica | null,
    public historialGeneral?: IHistorialMedico | null,
    public entidadNacimiento?: IEntidadFederativa | null
  ) {}
}