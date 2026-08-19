package com.portfolio.coursesapi.validation.rut;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilidades para validar y normalizar RUT chileno usando el algoritmo
 * de digito verificador modulo 11.
 * <p>
 * Formatos de entrada aceptados: "12345678-5", "12.345.678-5", "123456785".
 * Formato normalizado de salida: "12345678-5" (sin puntos, con guion, DV en mayuscula).
 */
public final class RutUtils {

    private static final Pattern RUT_PATTERN = Pattern.compile("^(\\d{1,8})-?([0-9kK])$");

    private RutUtils() {
        // utility class
    }

    public static boolean isValid(String rawRut) {
        if (rawRut == null || rawRut.isBlank()) {
            return false;
        }
        Matcher matcher = RUT_PATTERN.matcher(clean(rawRut));
        if (!matcher.matches()) {
            return false;
        }
        String body = matcher.group(1);
        char providedDv = Character.toUpperCase(matcher.group(2).charAt(0));
        return computeVerifier(body) == providedDv;
    }

    /**
     * Normaliza un RUT valido al formato "cuerpo-dv". Lanza IllegalArgumentException
     * si el RUT no es valido; se espera invocarla solo despues de isValid(...) == true,
     * o de que la validacion @ValidRut ya haya pasado.
     */
    public static String normalize(String rawRut) {
        Matcher matcher = RUT_PATTERN.matcher(clean(rawRut));
        if (!matcher.matches()) {
            throw new IllegalArgumentException("RUT invalido: " + rawRut);
        }
        String body = matcher.group(1);
        char dv = Character.toUpperCase(matcher.group(2).charAt(0));
        return body + "-" + dv;
    }

    private static String clean(String rawRut) {
        return rawRut.trim().replace(".", "").replace(" ", "").toUpperCase();
    }

    private static char computeVerifier(String body) {
        int sum = 0;
        int multiplier = 2;
        for (int i = body.length() - 1; i >= 0; i--) {
            sum += Character.getNumericValue(body.charAt(i)) * multiplier;
            multiplier = (multiplier == 7) ? 2 : multiplier + 1;
        }
        int remainder = 11 - (sum % 11);
        if (remainder == 11) {
            return '0';
        }
        if (remainder == 10) {
            return 'K';
        }
        return Character.forDigit(remainder, 10);
    }
}
