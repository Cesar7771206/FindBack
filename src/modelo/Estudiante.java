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
public class Estudiante extends Usuario{
    private String facultad;
    private String carrera;
    private String ciclo;

    public Estudiante(String facultad, String carrera, String ciclo, String identificacion, String nombre, String correo, String contrasena) {
        super(identificacion, nombre, correo, contrasena);
        this.facultad = facultad;
        this.carrera = carrera;
        this.ciclo = ciclo;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }
    
    @Override
    public String getRol(){
        return "ESTUDIANTE";
    }
    
    @Override
    public String toTxt(){
        String mensajesTxt = mensajes.isEmpty() ? "SIN_MENSAJES" : String.join(";", mensajes); 
        return  identificacion + "|" + nombre + "|" + correo + "|" + contrasena + "|" + facultad + "|" + carrera + "|" + ciclo + "|" + mensajesTxt;
    }
}
