package modelo;

import java.text.Normalizer;

public enum Carrera {
    SISTEMAS("Ingeniería de Sistemas (ISIA)", Facultad.INGENIERIA),
    INDUSTRIAL("Ingeniería Industrial y de Sistemas", Facultad.INGENIERIA),
    CIVIL("Ingeniería Civil", Facultad.INGENIERIA),
    MECANICO_ELECTRICA("Ingeniería Mecánico-Eléctrica", Facultad.INGENIERIA),
    ADMINISTRACION("Administración de Empresas", Facultad.CIENCIAS_ECONOMICAS),
    ECONOMIA("Economía", Facultad.CIENCIAS_ECONOMICAS),
    CONTABILIDAD("Contabilidad y Auditoría", Facultad.CIENCIAS_ECONOMICAS),
    DERECHO("Derecho", Facultad.DERECHO),
    COMUNICACION("Comunicación Audiovisual / Periodismo", Facultad.COMUNICACION),
    EDUCACION("Educación", Facultad.EDUCACION),
    PSICOLOGIA("Psicología", Facultad.HUMANIDADES),
    MEDICINA("Medicina Humana", Facultad.MEDICINA);

    private final String nombre;
    private final Facultad facultad;

    Carrera(String nombre, Facultad facultad) {
        this.nombre = nombre;
        this.facultad = facultad;
    }

    public String getNombre() {
        return nombre;
    }

    public Facultad getFacultad() {
        return facultad;
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

    public static Carrera fromString(String texto) {
        if (texto == null || texto.isBlank()) {
            return SISTEMAS;
        }
        String clean = texto.trim();
        for (Carrera c : values()) {
            if (c.nombre.equalsIgnoreCase(clean) || c.name().equalsIgnoreCase(clean)) {
                return c;
            }
        }
        String norm = normalizar(clean);
        for (Carrera c : values()) {
            if (normalizar(c.nombre).equals(norm) || normalizar(c.name()).equals(norm)) {
                return c;
            }
        }
        for (Carrera c : values()) {
            if (normalizar(c.nombre).contains(norm) || norm.contains(normalizar(c.nombre))) {
                return c;
            }
        }
        return SISTEMAS;
    }
}
