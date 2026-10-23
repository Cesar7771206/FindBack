/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author cesar
 */
public class ObjetoGenerico extends ObjetoReportado {

    private String detalleExtra;

    public ObjetoGenerico(String detalleExtra, int id, String nombre, String descripcion, String categoria, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        super(id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, reportadoPor);
        this.detalleExtra = detalleExtra;
    }

    public String getDetalleExtra() {
        return detalleExtra;
    }

    public void setDetalleExtra(String detalleExtra) {
        if (detalleExtra == null || detalleExtra.isBlank()) {
            this.detalleExtra = "NO REGISTRADO";
        } else {
            this.detalleExtra = detalleExtra;
        }
    }

    @Override
    public String[] preguntasVerificacion() {
        return new String[]{
            "Mencione el detalle específico o marca distintiva del objeto:"
        };
    }

    @Override
    public String[] datosVerificables() {
        return new String[]{detalleExtra};
    }

    @Override
    public String toTxt() {
        return String.join("|",
                baseToTxt(),
                detalleExtra
        );
    }

}
