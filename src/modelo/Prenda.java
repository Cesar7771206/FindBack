package modelo;

public class Prenda extends ObjetoReportado {

    private String talla;
    private String marca;

    public Prenda(String talla, String marca, int id, String nombre, String descripcion, Categoria categoria, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        super(id, nombre, descripcion, Categoria.PRENDA, color, fecha, ubicacion, estado, reportadoPor);
        setTalla(talla);
        setMarca(marca);
    }

    public Prenda(String talla, String marca, int id, String nombre, String descripcion, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        this(talla, marca, id, nombre, descripcion, Categoria.PRENDA, color, fecha, ubicacion, estado, reportadoPor);
    }

    public void setAtributosPrenda(String talla, String marca, String nombre, String descripcion, Categoria categoria, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        setAtributosObjetoReportado(nombre, descripcion, Categoria.PRENDA, color, fecha, ubicacion, estado, reportadoPor);
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
        return baseToTxt() + "|" + talla + ";" + marca;
    }

}
