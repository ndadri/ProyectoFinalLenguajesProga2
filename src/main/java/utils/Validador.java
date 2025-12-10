package utils;

import models.Paciente;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Validador {

    /**
     * Valida si una cadena es nula o vacía
     */
    public static boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /**
     * Valida Email básico sin Regex (busca @ y un punto después)
     */
    public static boolean esEmailValido(String email) {
        if (esVacio(email)) return false;
        int arroba = email.indexOf('@');
        int punto = email.lastIndexOf('.');
        // Debe tener @, tener punto, y el punto debe estar después del @
        return arroba > 0 && punto > arroba;
    }

    /**
     * ALGORITMO DE CÉDULA ECUATORIANA
     * Retorna true si es válida, false si es falsa
     */
    public static boolean esCedulaValida(String cedula) {
        if (esVacio(cedula) || cedula.length() != 10) return false;

        // Verificar que solo sean números
        try {
            Long.parseLong(cedula);
        } catch (NumberFormatException e) {
            return false;
        }

        // Región válida (Primeros 2 dígitos entre 01 y 24)
        int region = Integer.parseInt(cedula.substring(0, 2));
        if (region < 1 || region > 24) return false;

        // Tercer dígito menor a 6 (Persona natural)
        int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
        if (tercerDigito >= 6) return false; // Nota: RUCs usan 6 o 9, pero esto es cédula

        // Algoritmo de validación (Módulo 10)
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int total = 0;
        int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));

        for (int i = 0; i < coeficientes.length; i++) {
            int valor = Integer.parseInt(cedula.substring(i, i + 1));
            int resultado = valor * coeficientes[i];

            if (resultado >= 10) {
                resultado = resultado - 9;
            }
            total += resultado;
        }

        int decenaSuperior = (total % 10 == 0) ? total : (total - (total % 10)) + 10;
        int calculado = decenaSuperior - total;

        return calculado == digitoVerificador;
    }

    /**
     * Valida todos los datos del paciente en bloque
     * Retorna NULL si todo está bien, o un MENSAJE DE ERROR si falla algo.
     */
    public static String validarDatosPaciente(Paciente p) {
        if (esVacio(p.getNombres())) return "El nombre es obligatorio.";
        if (esVacio(p.getApellidos())) return "El apellido es obligatorio.";

        if (!esCedulaValida(p.getCedula())) {
            return "La cédula ingresada es incorrecta o no es válida en Ecuador.";
        }

        if (!esVacio(p.getEmail()) && !esEmailValido(p.getEmail())) {
            return "El formato del correo electrónico no es válido.";
        }

        // Validar que no haya nacido en el futuro
        if (p.getFechaNacimiento() != null) {
            Date hoy = new Date(System.currentTimeMillis());
            if (p.getFechaNacimiento().after(hoy)) {
                return "La fecha de nacimiento no puede ser futura.";
            }
        }

        return null; // Todo OK
    }
}