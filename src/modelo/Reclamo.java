package modelo;

import java.util.ArrayList;

public class Reclamo {
    private String id;
    private ArrayList<String> respuestas;
    private String estado;
    private String observación;
    private Estudiante reclamante;
    private ObjetoReportado objeto;

    public Reclamo(String id, ArrayList<String> respuestas, String estado, String observación, Estudiante reclamante, ObjetoReportado objeto) {
        this.id = id;
        this.respuestas = respuestas;
        this.estado = estado;
        this.observación = observación;
        this.reclamante = reclamante;
        this.objeto = objeto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<String> respuestas) {
        this.respuestas = respuestas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservación() {
        return observación;
    }

    public void setObservación(String observación) {
        this.observación = observación;
    }

    public Estudiante getReclamante() {
        return reclamante;
    }

    public void setReclamante(Estudiante reclamante) {
        this.reclamante = reclamante;
    }

    public ObjetoReportado getObjeto() {
        return objeto;
    }

    public void setObjeto(ObjetoReportado objeto) {
        this.objeto = objeto;
    }
    
    
}
