/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author cesar
 */
public class DispositivoElectronico extends ObjetoReportado {

    private String marca;
    private String modelo;
    private String numeroSerie;

    public DispositivoElectronico(String marca, String modelo, String numeroSerie, int id, String nombre, String descripcion, String categoria, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        super(id, nombre, descripcion, "DISPOSITIVO ELECTRONICO", color, fecha, ubicacion, estado, reportadoPor);
        setMarca(marca);
        setModelo(modelo);
        setNumeroSerie(numeroSerie);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("La marca no puede ser nula, estar vacía ni contener solo espacios");
        }
        this.marca = marca.trim().toUpperCase();
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        if (modelo == null || modelo.isBlank()) {
            this.modelo = "NO REGISTRADO";
        } else {
            this.modelo = modelo.trim().toUpperCase();
        }
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        if (numeroSerie == null || numeroSerie.isBlank()) {
            this.numeroSerie = "NO REGISTRADO";
        } else {
            this.numeroSerie = numeroSerie.trim().toUpperCase();
        }
    }

    @Override
    public String[] preguntasVerificacion() {
        return new String[]{
            "¿Cuál es la marca del dispositivo?",
            "¿Cuál es el modelo exacto?",
            "¿Cuál es el número de serie o detalle único identificable?"
        };
    }

    @Override
    public String[] datosVerificables() {
        return new String[]{
            marca,
            modelo,
            numeroSerie
        };
    }

    @Override
    public String toTxt() {
        return String.join("|",
                baseToTxt(),
                marca,
                modelo,
                numeroSerie
        );
    }

}
