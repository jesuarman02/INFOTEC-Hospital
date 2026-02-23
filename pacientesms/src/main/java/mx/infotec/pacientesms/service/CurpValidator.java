package mx.infotec.pacientesms.service;

import java.util.regex.Pattern;

public class CurpValidator {

    // Expresión regular para la estructura visual
    private static final String CURP_REGEX = "[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}";

    public static boolean esValido(String curp) {
        // 1. Revisar que no sea nulo y cumpla el dibujo básico (Regex)
        if (curp == null || !Pattern.matches(CURP_REGEX, curp)) {
            return false;
        }

        // 2. Revisar la palabra inconveniente
        String primeras4 = curp.substring(0, 4);
        if (esPalabraInconveniente(primeras4)) {
            return false; 
        }

        // 3. Revisar el Dígito Verificador (Matemáticas)
        return validarDigitoVerificador(curp);
    }

    private static boolean esPalabraInconveniente(String prefijo) {
        String[] prohibidas = { "BACA", "LOCO", "BUEI", "BUEY", "CACA", "CACO", "CAGA", "CAGO", "CAKA", "CAKO", "COJA", "COJE", "COJI", "COJO", "CULO", "FETO", "GUEI", "GUEY", "JOTO", "KACA", "KACO", "KAGA", "KAGO", "KAKA", "KAKO", "KOGE", "KOGI", "KOGO", "KOJA", "KOJE", "KOJI", "KOJO", "KULO", "MAME", "MAMO", "MEAR", "MEAS", "MEON", "MIAR", "MION", "MOCO", "MULA", "PEDA", "PEDO", "PENE", "PIPI", "PITO", "POPO", "PUTA", "PUTO", "QULO", "RATA", "RUIN" };
        for (String palabra : prohibidas) {
            if (prefijo.equals(palabra)) return true;
        }
        return false;
    }

    private static boolean validarDigitoVerificador(String curp) {
        // Diccionario de valores para cada letra y número según la norma oficial
        String diccionario = "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        double suma = 0;
        
        // Recorremos los primeros 17 caracteres
        for (int i = 0; i < 17; i++) {
            char caracter = curp.charAt(i);
            int valor = diccionario.indexOf(caracter);
            // Fórmula oficial: Valor del caracter * (18 - posición)
            suma = suma + (valor * (18 - i));
        }

        // Cálculo del dígito final
        int digito = (int) (10 - (suma % 10));
        if (digito == 10) digito = 0;

        // Obtenemos el último caracter que escribió el usuario
        char ultimoCaracter = curp.charAt(17);
        int valorUltimo = Character.getNumericValue(ultimoCaracter);

        // Comparamos
        return digito == valorUltimo;
    }
}