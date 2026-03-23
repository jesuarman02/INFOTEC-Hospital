import { type IPaciente } from '@/shared/model/pacientems/paciente.model';

export interface IHistorialMedico {
  id?: number;
  datosBiometricosSanguineos?: string | null;
  alergias?: string | null;
  enfermedadesCronicas?: string | null;
  cirugiasPrevias?: string | null;
  medicamentosActuales?: string | null;
  antecedentesFamiliaresHereditarios?: string | null;
  antecedentesPersonalesPatologicos?: string | null;
  habitosConsumoOtros?: string | null;
  observacionesGenerales?: string | null;
  estado?: string | null;
// 🔥 Las variables aplanadas igual que en Dirección
  pacienteId?: number | null;
  pacienteEcu?: number | null;
  pacienteNombre?: string | null;
  pacienteApellidoPaterno?: string | null;
}

export class HistorialMedico implements IHistorialMedico {
  constructor(
    public id?: number,
    public datosBiometricosSanguineos?: string | null,
    public alergias?: string | null,
    public enfermedadesCronicas?: string | null,
    public cirugiasPrevias?: string | null,
    public medicamentosActuales?: string | null,
    public antecedentesFamiliaresHereditarios?: string | null,
    public antecedentesPersonalesPatologicos?: string | null,
    public habitosConsumoOtros?: string | null,
    public observacionesGenerales?: string | null,
    public estado?: string | null,
    public pacienteId?: number | null,
    public pacienteEcu?: number | null,
    public pacienteNombre?: string | null,
    public pacienteApellidoPaterno?: string | null
  ) {}
}