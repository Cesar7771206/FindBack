/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
/**
 *
 * @author cesar
 */
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
        if (nombre != null || !nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo, estar vacío ni contener solo espacios");
        }
        this.nombre = nombre.trim().toUpperCase();
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        String patronCorreo = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!correo.matches(patronCorreo) || correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo debe tener el formato correcto, no estar vacío ni contener solo espacios");
        }
        
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        String patronContrasena = "^\\S+$";
        if (!contrasena.matches(patronContrasena) ||contrasena == null || contrasena.isBlank()){
            throw new IllegalArgumentException("La contraseña debe tener al menos un caracter y no puede tener espacios en blanco");
        }
        this.contrasena = contrasena;
    }

    public ArrayList<String> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<String> mensajes) {
        this.mensajes = mensajes;
    }    
}
