import { type Sexo } from '@/shared/model/enumerations/sexo.model';

// Definimos los nuevos tipos estrictos (Enums) aquí mismo
export type Nacionalidad = 'MEXICANA' | 'EXTRANJERA';
export type EstadoCivil = 'NO_ESPECIFICADO' | 'NO_APLICA' | 'SE_IGNORA' | 'CASADO' | 'DIVORCIADO' | 'SEPARADO' | 'SOLTERO' | 'UNION_LIBRE' | 'VIUDO';

export interface IPaciente {
  id?: number;
  ecu?: number;
  nombre?: string | null;
  apellidoPaterno?: string | null;
  apellidoMaterno?: string | null;
  sexo?: Sexo | null;
  nacionalidad?: Nacionalidad | null;
  fechaNacimiento?: Date | null;
  estadoCivil?: EstadoCivil | null;
  curp?: string | null;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public ecu?: number,
    public nombre?: string | null,
    public apellidoPaterno?: string | null,
    public apellidoMaterno?: string | null,
    public sexo?: Sexo | null,
    public nacionalidad?: Nacionalidad | null,
    public fechaNacimiento?: Date | null,
    public estadoCivil?: EstadoCivil | null,
    public curp?: string | null
  ) {}
}