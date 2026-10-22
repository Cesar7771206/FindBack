/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


/**
 *
 * @author cesar
 */
public class Estudiante extends Usuario{
    private String facultad;
    private String carrera;
    private int ciclo;

    public Estudiante(String facultad, String carrera, int ciclo, String identificacion, String nombre, String correo, String contrasena) {
        super(identificacion, nombre, correo, contrasena);
        this.facultad = facultad;
        this.carrera = carrera;
        this.ciclo = ciclo;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        if (facultad == null || facultad.isBlank()){
            throw new IllegalArgumentException("El campo facultad no puede estar vacío ni contener solo espacios"); 
        }
        this.facultad = facultad.trim().toUpperCase();
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        if (carrera == null || carrera.isBlank()){
            throw new IllegalArgumentException("El campo carrera no puede estar vacío ni contener solo espacios"); 
        }
        this.carrera = carrera;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        if (ciclo < 0 || ciclo > 10){
            throw new IllegalArgumentException("El ciclo debe estar entre 0 y 10"); 
        }
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
