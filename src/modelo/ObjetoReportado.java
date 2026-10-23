/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author cesar
 */
public abstract class ObjetoReportado implements Buscable {

    protected int id;
    protected String nombre;
    protected String descripcion;
    protected String categoria;
    protected String color;
    protected String fecha;
    protected String ubicacion;
    protected String estado;
    protected Estudiante reportadoPor;

    public ObjetoReportado(int id, String nombre, String descripcion, String categoria, String color, String fecha, String ubicacion, String estado, Estudiante reportadoPor) {
        setId(id);
        setNombre(nombre);
        setDescripcion(descripcion);
        setCategoria(categoria);
        setColor(color);
        setFecha(fecha);
        setUbicacion(ubicacion);
        setEstado(estado);
        setReportadoPor(reportadoPor);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número entero positivo mayor que cero");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo, estar vacío ni contener solo espacios");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula, estar vacía ni contener solo espacios");
        }
        this.descripcion = descripcion.trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("La categoría no puede ser nula, estar vacía ni contener solo espacios");
        }
        this.categoria = categoria.trim().toUpperCase();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("El color no puede ser nulo, estar vacío ni contener solo espacios");
        }
        this.color = color.trim().toUpperCase();
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        String patronFecha = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        if (!fecha.matches(patronFecha) || fecha == null || fecha.isBlank()) {
            throw new IllegalArgumentException("La fecha debe tener el formato correcto (YYYY-MM-DD), no puede ser nula, estar vacía ni contener solo espacios");
        }
        this.fecha = fecha.trim();
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new IllegalArgumentException("La ubicación no puede ser nula, estar vacía ni contener solo espacios");
        }
        this.ubicacion = ubicacion.trim().toUpperCase();
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("El estado no puede ser nulo, estar vacío ni contener solo espacios");
        }
        this.estado = estado.trim().toUpperCase();
    }

    public Estudiante getReportadoPor() {
        return reportadoPor;
    }

    public void setReportadoPor(Estudiante reportadoPor) {
        if (reportadoPor == null) {
            throw new IllegalArgumentException("El estudiante que reporta no puede ser nulo");
        }
        this.reportadoPor = reportadoPor;
    }

    @Override
    public boolean coincideCon(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            throw new IllegalArgumentException("El criterior no puede estar vacío ni contener solo espacios");
        }
        String crit = criterio.toLowerCase().trim();
        return (nombre.toLowerCase().contains(crit))
                || (descripcion.toLowerCase().contains(crit))
                || (categoria.toLowerCase().contains(crit))
                || (color.toLowerCase().contains(crit))
                || (ubicacion.toLowerCase().contains(crit));
    }

    protected String baseToTxt() { 
        return String.join("|",
                String.valueOf(id),
                nombre,
                descripcion,
                categoria,
                color,
                fecha, 
                ubicacion,
                estado,
                reportadoPor.getIdentificacion()
        );
    }

    public abstract String[] preguntasVerificacion();

    public abstract String[] datosVerificables();

    public abstract String toTxt();

}
