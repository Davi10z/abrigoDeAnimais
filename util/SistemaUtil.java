package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SistemaUtil {
    // Constantes estáticas
    public static final String NOME_SISTEMA = "Abrigo Seguro - UFC";
    public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static int totalLogins = 0;

    // Métodos estáticos pra faciliar pra gente
    public static void registrarLogin() {
        totalLogins++;
    }

    public static int getTotalLogins() {
        return totalLogins;
    }

    public static String formatarData(LocalDate data) {
        if (data == null) return "N/A";
        return data.format(FORMATO_DATA);
    }
}