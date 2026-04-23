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
                        // Pedimos las respuestas al backend (esperando que nos haga caso)
                        const infoRes = await axios.get(`services/pacientesms/api/info-socioeconomicas?pacienteId.equals=${pacienteEncontrado.id}`);

                        // 🔥 EL ESCUDO ANTI-FANTASMAS: 
                        // Filtramos manualmente la lista por si el backend nos mandó respuestas de otros pacientes
                        const todasLasRespuestas = infoRes.data.filter((respuesta: any) => {
                            // Buscamos si la respuesta tiene el ID del paciente actual
                            const idDueño = respuesta.paciente?.id || respuesta.pacienteId;
                            return idDueño === pacienteEncontrado.id;
                        });



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

                            // Le decimos a Vue que active la pestaña
                            pacienteEncontrado.tieneInfoSocioeconomica = true;
                        } else {
                            // Si el arreglo quedó vacío después de nuestro filtro, no tiene info
                            pacienteEncontrado.tieneInfoSocioeconomica = false;
                        }

                    } catch (errorInfo) {
                        console.warn("Falló la carga de info socioeconómica:", errorInfo);
                        pacienteEncontrado.tieneInfoSocioeconomica = false;
                    }
                }

                // 4. Vamos por el Historial Médico Completo (El endpoint maestro) 🩺
                if (pacienteEncontrado.id) {
                    try {
                        // Hacemos UNA sola petición al endpoint que viste en tu red (ajusta la URL si es necesario)
                        const medicosRes = await axios.get(`services/pacientesms/api/historial-medicos?pacienteId.equals=${pacienteEncontrado.id}`);

                        if (medicosRes.data && medicosRes.data.length > 0) {
                            const rawData = medicosRes.data[0]; // Tomamos el primer registro

                            // 🔥 HERRAMIENTA MAGICA: Función para convertir el texto raro a Objetos JSON
                            const safeParse = (jsonString: any) => {
                                if (!jsonString) return { tiene: false, detalles: [] };
                                try {
                                    return JSON.parse(jsonString);
                                } catch (e) {
                                    console.error("Error al traducir el JSON:", jsonString);
                                    return { tiene: false, detalles: [] };
                                }
                            };

                            // Armamos nuestro objeto limpio y estructurado
                            pacienteEncontrado.datosMedicos = {
                                observacionesGenerales: rawData.observacionesGenerales,
                                alergias: safeParse(rawData.alergias),
                                enfermedadesCronicas: safeParse(rawData.enfermedadesCronicas),
                                medicamentosActuales: safeParse(rawData.medicamentosActuales),
                                cirugiasPrevias: safeParse(rawData.cirugiasPrevias),
                                antecedentesFamiliares: safeParse(rawData.antecedentesFamiliaresHereditarios),
                                antecedentesPatologicos: safeParse(rawData.antecedentesPersonalesPatologicos),
                                habitos: safeParse(rawData.habitosConsumoOtros),
                                biometricos: safeParse(rawData.datosBiometricosSanguineos)
                            };
                        } else {
                            pacienteEncontrado.datosMedicos = null;
                        }

                    } catch (errorMedicos) {
                        console.warn("Falló la carga del historial médico:", errorMedicos);
                        pacienteEncontrado.datosMedicos = null;
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