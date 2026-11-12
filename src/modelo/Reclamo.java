package modelo;

import java.util.ArrayList;

public class Reclamo {

    private String id;
    private ObjetoReportado objeto;
    private Estudiante reclamante;
    private ArrayList<String> respuestas;
    private String estado;
    private String observacion;
    private String codigoRevisor;

    public Reclamo(String id, ObjetoReportado objeto, Estudiante reclamante, ArrayList<String> respuestas, String estado, String observacion, String codigoRevisor) {
        setId(id);
        setRespuestas(respuestas);
        setEstado(estado);
        setObservacion(observacion);
        setCodigoRevisor(codigoRevisor);
        setReclamante(reclamante);
        setObjeto(objeto);
    }

    public Reclamo(String id, ObjetoReportado objeto, Estudiante reclamante, ArrayList<String> respuestas) {
        this(id, objeto, reclamante, respuestas, "PENDIENTE", "", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del reclamo no puede ser nulo ni estar vacío.");
        }
        this.id = id.trim();
    }

    public ArrayList<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<String> respuestas) {
        this.respuestas = (respuestas != null) ? respuestas : new ArrayList<>();
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede ser nulo ni vacío.");
        }
        String e = estado.trim().toUpperCase();
        if (!e.equals("PENDIENTE") && !e.equals("APROBADO") && !e.equals("RECHAZADO")) {
            throw new IllegalArgumentException("Estado inválido. Use PENDIENTE, APROBADO o RECHAZADO.");
        }
        this.estado = e;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = (observacion == null) ? "" : observacion.trim();
    }

    public String getCodigoRevisor() {
        return codigoRevisor;
    }

    public void setCodigoRevisor(String codigoRevisor) {
        this.codigoRevisor = (codigoRevisor == null) ? "" : codigoRevisor.trim();
    }

    public Estudiante getReclamante() {
        return reclamante;
    }

    public void setReclamante(Estudiante reclamante) {
        if (reclamante == null) {
            throw new IllegalArgumentException("El reclamante no puede ser nulo.");
        }
        this.reclamante = reclamante;
    }

    public ObjetoReportado getObjeto() {
        return objeto;
    }

    public void setObjeto(ObjetoReportado objeto) {
        if (objeto == null) {
            throw new IllegalArgumentException("El objeto reclamado no puede ser nulo.");
        }
        this.objeto = objeto;
    }

    public String toTxt() {
        String respuestasTxt = (this.respuestas != null) ? String.join(";", this.respuestas) : "";
        return String.join("|",
                this.id,
                String.valueOf(this.objeto.getId()),
                this.reclamante.getIdentificacion(),
                respuestasTxt,
                this.estado,
                this.observacion != null ? this.observacion : "",
                this.codigoRevisor != null ? this.codigoRevisor : ""
        );
    }
    
}
