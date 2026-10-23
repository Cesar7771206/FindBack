/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author cesar
 */
public class Documento extends ObjetoReportado {

    private String tipoDocumento;
    private String nombreTitular;

    public Documento(String tipoDocumento, String nombreTitular, int id, String nombre, String descripcion, String categoria, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        super(id, nombre, descripcion, "DOCUMENTO", color, fecha, ubicacion, estado, reportadoPor);
        setTipoDocumento(tipoDocumento);
        setNombreTitular(nombreTitular);
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        if (tipoDocumento == null || tipoDocumento.isBlank()) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo, estar vacío ni contener solo espacios");
        }
        this.tipoDocumento = tipoDocumento;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        if (nombreTitular == null || nombreTitular.isBlank()) {
            throw new IllegalArgumentException("El nombre del titular no puede ser nulo, estar vacío ni contener solo espacios");
        }
        this.nombreTitular = nombreTitular;
    }

    @Override
    public String[] preguntasVerificacion() {
        return new String[]{
            "¿Qué tipo de documento es (DNI, CARNÉT, LICENCIA, etc.)?",
            "¿Cuál es el nombre completo del titular que figura en el documento?"
        };
    }

    @Override
    public String[] datosVerificables() {
        return new String[]{
            tipoDocumento,
            nombreTitular
        };
    }

    @Override
    public String toTxt() {
        return String.join("|",
                baseToTxt(),
                tipoDocumento,
                nombreTitular
        );
    }

}
