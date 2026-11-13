package modelo;

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
        if (clean.toLowerCase().contains("doc")) {
            return DOCUMENTO;
        }
        if (clean.toLowerCase().contains("prend")) {
            return PRENDA;
        }
        if (clean.toLowerCase().contains("disp") || clean.toLowerCase().contains("elect")) {
            return DISPOSITIVO_ELECTRONICO;
        }
        return OBJETO_GENERICO;
    }
}
