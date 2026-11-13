package modelo;

public enum Ubicacion {
    BIBLIOTECA_CENTRAL("Biblioteca Central"),
    EDIFICIO_PRINCIPAL("Edificio Principal"),
    EDIFICIO_80("Edificio 80"),
    CAFETERIA_CENTRAL("Cafetería Central / Comedor"),
    EDIFICIO_B("Edificio B"),
    EDIFICIO_E("Edificio E"),
    CA_101("CA 101"),
    CA_102("CA 102"),
    CA_103("CA 103"),
    FACULTAD_EDUCACION("Facultad de Educación"),
    DERECHO("Facultad de Derecho"),
    POLIDEPORTIVO("Polideportivo"),
    FCOM("Facultad de Comunicación (FCom)"),
    HIDRAULICA("Laboratorio de Hidráulica"),
    OTRO("Otro lugar del campus");

    private final String nombre;

    Ubicacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public static Ubicacion fromString(String texto) {
        if (texto == null || texto.isBlank()) {
            return OTRO;
        }
        for (Ubicacion u : values()) {
            if (u.nombre.equalsIgnoreCase(texto.trim()) || u.name().equalsIgnoreCase(texto.trim())) {
                return u;
            }
        }
        return OTRO;
    }
}
