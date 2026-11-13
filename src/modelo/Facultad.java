package modelo;

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

    public static Facultad fromString(String texto) {
        if (texto == null || texto.isBlank()) {
            return INGENIERIA;
        }
        for (Facultad f : values()) {
            if (f.nombre.equalsIgnoreCase(texto.trim()) || f.name().equalsIgnoreCase(texto.trim())) {
                return f;
            }
        }
        return INGENIERIA;
    }
}
