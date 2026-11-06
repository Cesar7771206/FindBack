package modelo;

import java.util.ArrayList;

public class Reclamo {
    private String id;
    private ArrayList<String> respuestas;
    private String estado;
    private String observacion;
    private Estudiante reclamante;
    private ObjetoReportado objeto;

    public Reclamo(String id, ArrayList<String> respuestas, String estado, String observacion, Estudiante reclamante, ObjetoReportado objeto) {
        setId(id);
        setRespuestas(respuestas);
        setEstado(estado);
        setObservación(observacion);
        setReclamante(reclamante);
        setObjeto(objeto);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("El id del raclamo no puede ser nulo ni vacío.");
        }
        this.id = id;
    }

    public ArrayList<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<String> respuestas) {
        if (respuestas == null) {
            throw new IllegalArgumentException("La lista de respuestas no puede ser nula.");
        }
        this.respuestas = respuestas;
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
            throw new IllegalArgumentException("Estado inválido. Use PENDIENTE, APROBADO O RECHAZADO.");
        }
        this.estado = e;
    }

    public String getObservación() {
        return observacion;
    }

    public void setObservación(String observacion) {
        if (observacion == null) {
            throw new IllegalArgumentException("La observacion no puede ser nula.");
        }
        this.observacion = observacion.trim();
    }

    public Estudiante getReclamante() {
        return reclamante;
    }

    public void setReclamante(Estudiante reclamante) {
        if (reclamante == null) {
            throw new IllegalArgumentException("El relcmante no puede ser nulo.");
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
    
    public String[] toCsvCampos() {
        String respTxt = respuestas.isEmpty() ? "SIN RESPUESTAS" : String.join(";", respuestas);
        return new String[] {
            id,
            respTxt,
            estado,
            observacion,
            reclamante.getIdentificacion(),
            String.valueOf(objeto.getId())
        };
    }
}
