import { ref, type Ref } from 'vue';
import axios from 'axios';
import type { IPaciente } from '@/shared/model/pacientems/paciente.model';

export function usePacienteSearch() {
    const searchQuery = ref('');
    const resultados: Ref<IPaciente[]> = ref([]);
    const estaCargando = ref(false);
    const error = ref<string | null>(null);

    const buscarPorEcu = async () => {
        const ecuBusqueda = searchQuery.value.trim();
        if (!ecuBusqueda) return;

        // Validar que sea un número antes de enviarlo al backend
        const ecuNumero = Number(ecuBusqueda);
        if (isNaN(ecuNumero)) {
            error.value = "Por favor, ingresa un número de ECU válido.";
            return;
        }

        estaCargando.value = true;
        error.value = null;
        resultados.value = [];

        try {
            // 1. Buscamos al paciente por su ECU
            const res = await axios.get(`services/pacientesms/api/pacientes/ecu/${ecuNumero}`);

            if (res.data) {
                // Guardamos al paciente temporalmente
                let pacienteEncontrado = res.data;

                // 2. Vamos por la dirección
                if (pacienteEncontrado.direccionId) {
                    try {
                        const dirRes = await axios.get(`services/pacientesms/api/direccions/${pacienteEncontrado.direccionId}`);
                        pacienteEncontrado.direccion = dirRes.data;
                    } catch (errorDireccion) {
                        console.warn("Se encontró el paciente, pero falló la carga de su dirección:", errorDireccion);
                    }
                }

                // 3. Vamos por la info socioeconómica
                if (pacienteEncontrado.id) {
                    try {
                        // Pedimos TODAS las respuestas asociadas a este paciente (filtrando por pacienteId)
                        const infoRes = await axios.get(`services/pacientesms/api/info-socioeconomicas?pacienteId.equals=${pacienteEncontrado.id}`);

                        const todasLasRespuestas = infoRes.data; // Esto será un arreglo con las 30 respuestas
                        console.log("TODAS LAS RESPUESTAS:", todasLasRespuestas);

                        if (todasLasRespuestas && todasLasRespuestas.length > 0) {
                            // Función espía para pescar la respuesta que queremos
                            const obtenerValor = (clavePregunta: string) => {
                                const item = todasLasRespuestas.find((r: any) => r.pregunta === clavePregunta);
                                return item ? item.respuesta : 'N/A';
                            };

                            // Armamos un objeto limpio y fácil de leer para nuestro HTML
                            pacienteEncontrado.resumenSocioeconomico = {
                                ocupacion: obtenerValor('CONDICION_LABORAL'),
                                gradoEstudios: obtenerValor('GRADO_MAXIMO_ESTUDIOS'),
                                ingresoMensual: obtenerValor('INGRESO_MENSUAL'),
                                afiliacion: obtenerValor('INSTITUCION_MEDICA')
                            };
                            pacienteEncontrado.expedienteCompleto = todasLasRespuestas;

                            // Le decimos a Vue que active el botón rojo
                            pacienteEncontrado.tieneInfoSocioeconomica = true;
                        } else {
                            pacienteEncontrado.tieneInfoSocioeconomica = false;
                        }

                    } catch (errorInfo) {
                        console.warn("Falló la carga de info socioeconómica:", errorInfo);
                        pacienteEncontrado.tieneInfoSocioeconomica = false;
                    }
                }

                // Finalmente, mandamos al paciente a la variable reactiva
                resultados.value = [pacienteEncontrado];
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