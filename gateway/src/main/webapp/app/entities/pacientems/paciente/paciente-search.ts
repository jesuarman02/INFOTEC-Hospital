import { ref, type Ref } from 'vue';
import PacienteService from './paciente.service'; // Importación directa
import type { IPaciente } from '@/shared/model/pacientems/paciente.model';

export function usePacienteSearch() {
    // Instanciamos el servicio directamente aquí
    const pacienteService = new PacienteService();

    const searchQuery = ref('');
    const resultados: Ref<IPaciente[]> = ref([]);
    const estaCargando = ref(false);
    const error = ref<string | null>(null);

    const buscarPorEcu = async () => {
        const ecuBusqueda = searchQuery.value.trim();
        if (!ecuBusqueda) return;

        estaCargando.value = true;
        error.value = null;
        resultados.value = [];

        try {
            const res = await pacienteService.retrieve({
                'ecu.equals': ecuBusqueda
            });

            if (res.data && res.data.length > 0) {
                // Le indicamos a TypeScript que p es un IPaciente
                const exacto = res.data.find((p: IPaciente) => String(p.ecu) === ecuBusqueda);

                if (exacto) {
                    resultados.value = [exacto]; // Ahora sí guardará solo a Alan (ECU 20)
                } else {
                    error.value = `No se encontró coincidencia exacta para el ECU: ${ecuBusqueda}`;
                }
            }
        } catch (err) {
            error.value = "Error de conexión";
            console.error(err);
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

        // Si el mes actual es menor al de nacimiento, 
        // o si es el mismo mes pero el día actual es menor, aún no cumple años.
        if (diferenciaMeses < 0 || (diferenciaMeses === 0 && hoy.getDate() < cumple.getDate())) {
            edad--;
        }

        return edad.toString();
    };


    return { searchQuery, resultados, estaCargando, error, buscarPorEcu, calcularEdad };
}