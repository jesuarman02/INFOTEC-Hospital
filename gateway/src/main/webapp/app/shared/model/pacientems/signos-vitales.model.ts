import { type IPaciente } from '@/shared/model/pacientems/paciente.model';

export interface ISignosVitales {
  id?: number;
  fechaRegistro?: Date;
  frecuenciaCardiaca?: number | null;
  tensionArterial?: string | null;
  frecuenciaRespiratoria?: number | null;
  temperatura?: number | null;
  saturacionOxigeno?: number | null;

  // --- IDENTIFICACIÓN DEL PACIENTE ---
  pacienteEcu?: number | null;
  pacienteNombre?: string | null;
  pacienteApellidoPaterno?: string | null;

  // --- CONTEXTO DE LA TOMA ---
  tipo?: string | null;
  personal?: string | null;

  // --- EVALUACIONES CLÍNICAS EXTRA ---
  glucosa?: number | null;
  dolor?: number | null;
  estadoConciencia?: string | null;
  observaciones?: string | null;

  // --- RELACIÓN ORIGINAL JHIPSTER ---
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

    // --- IDENTIFICACIÓN DEL PACIENTE ---
    public pacienteEcu?: number | null,
    public pacienteNombre?: string | null,
    public pacienteApellidoPaterno?: string | null,

    // --- CONTEXTO DE LA TOMA ---
    public tipo?: string | null,
    public personal?: string | null,

    // --- EVALUACIONES CLÍNICAS EXTRA ---
    public glucosa?: number | null,
    public dolor?: number | null,
    public estadoConciencia?: string | null,
    public observaciones?: string | null,

    // --- RELACIÓN ORIGINAL JHIPSTER ---
    public paciente?: IPaciente | null,
  ) {}
}