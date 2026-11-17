package modelo;

import java.text.Normalizer;

public enum Categoria {
    DOCUMENTO("Documento"),
    PRENDA("Prenda"),
    DISPOSITIVO_ELECTRONICO("Dispositivo Electronico"),
    OBJETO_GENERICO("Objeto Generico");

    private final String nombre;

    Categoria(String nombre) {
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

    public static Categoria fromString(String texto) {
        if (texto == null || texto.isBlank()) {
            return OBJETO_GENERICO;
        }
        String clean = texto.trim();
        for (Categoria c : values()) {
            if (c.nombre.equalsIgnoreCase(clean) || c.name().equalsIgnoreCase(clean.replace(" ", "_"))) {
                return c;
            }
        }
        String norm = normalizar(clean);
        for (Categoria c : values()) {
            if (normalizar(c.nombre).equals(norm) || normalizar(c.name()).equals(norm.replace(" ", "_"))) {
                return c;
            }
        }
        if (norm.contains("doc")) {
            return DOCUMENTO;
        }
        if (norm.contains("prend")) {
            return PRENDA;
        }
        if (norm.contains("disp") || norm.contains("elect")) {
            return DISPOSITIVO_ELECTRONICO;
        }
        return OBJETO_GENERICO;
    }
}
