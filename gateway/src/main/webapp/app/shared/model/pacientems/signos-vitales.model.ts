import { type IPaciente } from '@/shared/model/pacientems/paciente.model';

export interface ISignosVitales {
  id?: number;
  fechaRegistro?: Date;
  frecuenciaCardiaca?: number | null;
  tensionArterial?: string | null;
  frecuenciaRespiratoria?: number | null;
  temperatura?: number | null;
  saturacionOxigeno?: number | null;
  peso?: number | null;
  estatura?: number | null;
  imc?: number | null;
  paciente?: IPaciente | null;
}

export class SignosVitales implements ISignosVitales {
  constructor(
    public id?: number,
    public fechaRegistro?: Date,
    public frecuenciaCardiaca?: number | null,
    public tensionArterial?: string | null,
    public frecuenciaRespiratoria?: number | null,
    public temperatura?: number | null,
    public saturacionOxigeno?: number | null,
    public peso?: number | null,
    public estatura?: number | null,
    public imc?: number | null,
    public paciente?: IPaciente | null,
  ) {}
}
