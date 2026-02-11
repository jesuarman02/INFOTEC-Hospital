import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import { type IEnfermedad } from '@/shared/model/pacientems/enfermedad.model';

export interface IPacienteEnfermedad {
  id?: number;
  fechaDiagnostico?: Date;
  estado?: string | null;
  notas?: string | null;
  paciente?: IPaciente | null;
  enfermedad?: IEnfermedad | null;
}

export class PacienteEnfermedad implements IPacienteEnfermedad {
  constructor(
    public id?: number,
    public fechaDiagnostico?: Date,
    public estado?: string | null,
    public notas?: string | null,
    public paciente?: IPaciente | null,
    public enfermedad?: IEnfermedad | null,
  ) {}
}
