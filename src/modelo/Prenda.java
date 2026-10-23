/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author cesar
 */
public class Prenda extends ObjetoReportado {

    private String talla;
    private String marca;

    public Prenda(String talla, String marca, int id, String nombre, String descripcion, String categoria, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        super(id, nombre, descripcion, "PRENDA", color, fecha, ubicacion, estado, reportadoPor);
        setTalla(talla);
        setMarca(marca);
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        if (talla == null || talla.isBlank()) {
            throw new IllegalArgumentException("La talla no puede ser nula, estar vacía ni contener solo espacios");
        }
        this.talla = talla;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("La marca no puede ser nula, estar vacía ni contener solo espacios");
        }
        this.marca = marca;
    }

    @Override
    public String[] preguntasVerificacion() {
        return new String[]{
            "¿Qué talla es la prenda?",
            "¿De qué marca es la prenda?"
        };
    }

    @Override
    public String[] datosVerificables() {
        return new String[]{
            talla,
            marca
        };
    }

    @Override
    public String toTxt() {
        return String.join("|",
                baseToTxt(),
                talla,
                marca
        );
    }

}
