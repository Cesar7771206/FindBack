package modelo;

import java.text.Normalizer;

public enum Facultad {
    INGENIERIA("Ingeniería"),
    CIENCIAS_ECONOMICAS("Ciencias Económicas y Empresariales"),
    COMUNICACION("Comunicación"),
    DERECHO("Derecho"),
    HUMANIDADES("Humanidades"),
    EDUCACION("Educación"),
    MEDICINA("Medicina");

    private final String nombre;

    Facultad(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    private static String normalizar(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }

    public static Facultad fromString(String texto) {
        if (texto == null || texto.isBlank()) {
            return INGENIERIA;
        }
        String clean = texto.trim();
        for (Facultad f : values()) {
            if (f.nombre.equalsIgnoreCase(clean) || f.name().equalsIgnoreCase(clean)) {
                return f;
            }
        }
        String norm = normalizar(clean);
        for (Facultad f : values()) {
            if (normalizar(f.nombre).equals(norm) || normalizar(f.name()).equals(norm)) {
                return f;
            }
        }
        for (Facultad f : values()) {
            if (normalizar(f.nombre).contains(norm) || norm.contains(normalizar(f.nombre))) {
                return f;
            }
        }
        return INGENIERIA;
    }
}
