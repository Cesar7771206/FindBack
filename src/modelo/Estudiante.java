package modelo;

public class Estudiante extends Usuario {
    private Facultad facultad;
    private Carrera carrera;
    private int ciclo;

    public Estudiante(Facultad facultad, Carrera carrera, int ciclo, String identificacion, String nombre, String correo, String contrasena) {
        super(identificacion, nombre, correo, contrasena);
        setCarrera(carrera);
        if (facultad != null) {
            setFacultad(facultad);
        } else if (this.carrera != null) {
            setFacultad(this.carrera.getFacultad());
        }
        setCiclo(ciclo);
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public String getFacultadNombre() {
        return (facultad != null) ? facultad.getNombre() : "";
    }

    public void setFacultad(Facultad facultad) {
        if (facultad == null) {
            throw new IllegalArgumentException("El campo facultad no puede ser nulo");
        }
        this.facultad = facultad;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public String getCarreraNombre() {
        return (carrera != null) ? carrera.getNombre() : "";
    }

    public void setCarrera(Carrera carrera) {
        if (carrera == null) {
            throw new IllegalArgumentException("El campo carrera no puede ser nulo");
        }
        this.carrera = carrera;
        if (this.facultad == null) {
            this.facultad = carrera.getFacultad();
        }
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        if (ciclo < 0 || ciclo > 10) {
            throw new IllegalArgumentException("El ciclo debe estar entre 0 y 10");
        }
        this.ciclo = ciclo;
    }

    @Override
    public String getRol() {
        return "ESTUDIANTE";
    }

    @Override
    public String toTxt() {
        String mensajesTxt = mensajes.isEmpty() ? "SIN_MENSAJES" : String.join(";", mensajes);
        String facTxt = (facultad != null) ? facultad.getNombre().toUpperCase() : "";
        String carTxt = (carrera != null) ? carrera.getNombre().toUpperCase() : "";
        return identificacion + "|" + nombre + "|" + correo + "|" + contrasena + "|" + facTxt + "|" + carTxt + "|" + ciclo + "|" + mensajesTxt;
    }
}
