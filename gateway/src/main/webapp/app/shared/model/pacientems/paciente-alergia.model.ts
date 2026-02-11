import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import { type IAlergia } from '@/shared/model/pacientems/alergia.model';

import { type GradoAlergia } from '@/shared/model/enumerations/grado-alergia.model';
export interface IPacienteAlergia {
  id?: number;
  fechaDeteccion?: Date | null;
  reaccion?: string | null;
  gravedad?: keyof typeof GradoAlergia;
  paciente?: IPaciente | null;
  alergia?: IAlergia | null;
}

export class PacienteAlergia implements IPacienteAlergia {
  constructor(
    public id?: number,
    public fechaDeteccion?: Date | null,
    public reaccion?: string | null,
    public gravedad?: keyof typeof GradoAlergia,
    public paciente?: IPaciente | null,
    public alergia?: IAlergia | null,
  ) {}
}
