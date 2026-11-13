package modelo;

import java.util.ArrayList;

public abstract class Usuario implements Notificable{
    protected String identificacion;
    protected String nombre;
    protected String correo;
    protected String contrasena;
    protected ArrayList<String> mensajes;

    public Usuario(String identificacion, String nombre, String correo, String contrasena) {
        setIdentificacion(identificacion);
        setNombre(nombre);
        setCorreo(correo);
        setContrasena(contrasena);
        this.mensajes = new ArrayList<>();
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.isBlank()){
            throw new IllegalArgumentException("La identificación no puede ser nula, estar vacía ni contener solo espacios");
        }
        this.identificacion = identificacion.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo, estar vacío ni contener solo espacios");
        }
        this.nombre = nombre.trim().toUpperCase();
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        String patronCorreo = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (correo == null || correo.isBlank() || !correo.trim().matches(patronCorreo)) {
            throw new IllegalArgumentException("El correo debe tener el formato correcto (ej: cesar.villegas@udep.edu.pe), no estar vacío ni contener solo espacios");
        }
        
        this.correo = correo.trim().toLowerCase();
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        String patronContrasena = "^\\S+$";
        if (contrasena == null || contrasena.isBlank() || !contrasena.matches(patronContrasena)){
            throw new IllegalArgumentException("La contraseña debe tener al menos un caracter y no puede tener espacios en blanco");
        }
        this.contrasena = contrasena;
    }

    public ArrayList<String> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<String> mensajes) {
        this.mensajes = (mensajes != null) ? mensajes : new ArrayList<>();
    } 
    
    public abstract String getRol();
    public abstract String toTxt();
    
    @Override
    public void recibirMensaje(String mensaje){
        if (mensaje != null && !mensaje.isBlank()) {
            this.mensajes.add(mensaje.trim());
        }
    }
}
