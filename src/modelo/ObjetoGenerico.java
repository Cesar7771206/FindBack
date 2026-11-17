package modelo;

public class ObjetoGenerico extends ObjetoReportado {

    private String detalleExtra;

    public ObjetoGenerico(String detalleExtra, int id, String nombre, String descripcion, Categoria categoria, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        super(id, nombre, descripcion, (categoria != null ? categoria : Categoria.OBJETO_GENERICO), color, fecha, ubicacion, estado, reportadoPor);
        setDetalleExtra(detalleExtra);
    }

    public ObjetoGenerico(String detalleExtra, int id, String nombre, String descripcion, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        this(detalleExtra, id, nombre, descripcion, Categoria.OBJETO_GENERICO, color, fecha, ubicacion, estado, reportadoPor);
    }
    
    public void setAtributosObjetoGenerico(String detalleExtra, String nombre, String descripcion, Categoria categoria, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor){
        setAtributosObjetoReportado(nombre, descripcion, (categoria != null ? categoria : Categoria.OBJETO_GENERICO), color, fecha, ubicacion, estado, reportadoPor);
        setDetalleExtra(detalleExtra);
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
        return baseToTxt() + "|" + detalleExtra;
    }

}
