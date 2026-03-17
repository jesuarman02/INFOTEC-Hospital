import { ref, type Ref } from 'vue';
import axios from 'axios'; // Aseguramos el import de axios
import type { IPaciente } from '@/shared/model/pacientems/paciente.model';

export function usePacienteSearch() {
    const searchQuery = ref('');
    const resultados: Ref<IPaciente[]> = ref([]);
    const estaCargando = ref(false);
    const error = ref<string | null>(null);

    const buscarPorEcu = async () => {
        const ecuBusqueda = searchQuery.value.trim();
        if (!ecuBusqueda) return;

        // NUEVO: Validar que sea un número antes de enviarlo al backend
        const ecuNumero = Number(ecuBusqueda);
        if (isNaN(ecuNumero)) {
            error.value = "Por favor, ingresa un número de ECU válido.";
            return;
        }

        estaCargando.value = true;
        error.value = null;
        resultados.value = [];

        try {
            // Mandamos la variable limpia al backend
            const res = await axios.get(`services/pacientesms/api/pacientes/ecu/${ecuNumero}`);

            if (res.data) {
                resultados.value = [res.data];
            }
        } catch (err: any) {
            if (err.response?.status === 404) {
                error.value = `No se encontró paciente con el ECU: ${ecuNumero}`;
            } else {
                error.value = "Error de conexión con el backend";
                console.error(err);
            }
        } finally {
            estaCargando.value = false;
        }
    };

    const calcularEdad = (fechaNacimiento: Date | string | null | undefined): string => {
        if (!fechaNacimiento) return '0';

        const hoy = new Date();
        const cumple = new Date(fechaNacimiento);

        let edad = hoy.getFullYear() - cumple.getFullYear();
        const diferenciaMeses = hoy.getMonth() - cumple.getMonth();

        if (diferenciaMeses < 0 || (diferenciaMeses === 0 && hoy.getDate() < cumple.getDate())) {
            edad--;
        }

        return edad.toString();
    };

    return { searchQuery, resultados, estaCargando, error, buscarPorEcu, calcularEdad };
}