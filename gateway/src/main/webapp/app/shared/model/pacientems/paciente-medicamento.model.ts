import { type IPaciente } from '@/shared/model/pacientems/paciente.model';
import { type IMedicamento } from '@/shared/model/pacientems/medicamento.model';

export interface IPacienteMedicamento {
  id?: number;
  dosis?: string | null;
  frecuencia?: string | null;
  fechaInicio?: Date;
  fechaFin?: Date | null;
  activo?: boolean | null;
  paciente?: IPaciente | null;
  medicamento?: IMedicamento | null;
}

export class PacienteMedicamento implements IPacienteMedicamento {
  constructor(
    public id?: number,
    public dosis?: string | null,
    public frecuencia?: string | null,
    public fechaInicio?: Date,
    public fechaFin?: Date | null,
    public activo?: boolean | null,
    public paciente?: IPaciente | null,
    public medicamento?: IMedicamento | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
