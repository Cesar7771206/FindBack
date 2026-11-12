package servicio;

import java.util.ArrayList;
import java.util.List;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;
import modelo.Usuario;
import repositorio.ObjetoReportadoRepositorio;
import repositorio.ReclamoRepositorio;
import repositorio.UsuarioRepositorio;

public class Reclamoservicio {
    private final ReclamoRepositorio reclamoRepo;
    private final ObjetoReportadoRepositorio objetoRepo;
    private final UsuarioRepositorio usuarioRepo;
    
    public Reclamoservicio(ReclamoRepositorio reclamoRepo,
            ObjetoReportadoRepositorio objetoRepo, UsuarioRepositorio usuarioRepo) {
        
        this.reclamoRepo = reclamoRepo;
        this.objetoRepo = objetoRepo;
        this.usuarioRepo = usuarioRepo;
    }
    
    public boolean crearReclamo(int idObjeto, String idReclamante, ArrayList<String> respuestas) {
        ObjetoReportado objeto = objetoRepo.obtenerObjetoPorId(idObjeto);
        if (objeto == null) {
            return false;
        }
        
        Usuario usuario = usuarioRepo.obtenerUsuarioPorIdentificacion(idReclamante);
        if (usuario == null || !(usuario instanceof Estudiante)) {
            return false;
        }
        
        Estudiante reclamante = (Estudiante) usuario;
        if (objeto.getReportadoPor().getIdentificacion().equals(idReclamante)){
            return false;
        }
        
        int totalPreguntas = objeto.preguntasVerificacion().length;
        if (respuestas == null || respuestas.size() != totalPreguntas) {
            return false;
        }
        
        List<Reclamo> reclamoDelEstudiante = reclamoRepo.buscarPorReclamante(idReclamante);
        for (Reclamo r : reclamoDelEstudiante) {
            if (r.getObjeto().getId() == idObjeto && r.getEstado().equals("PENDIENTE")) {
                return false;
            }
        }
        
        String nuevoId = reclamoRepo.generarSiguienteId();
        Reclamo nuevo = new Reclamo(nuevoId, objeto, reclamante, respuestas, "PENDIENTE", ", ");
        reclamoRepo.guardar(nuevo);
        return true;
    }
    
    public List<Reclamo> listarReclamos() {
        return reclamoRepo.obtenerReclamos();
    }
 
    public Reclamo buscarPorId(String idReclamo) {
        return reclamoRepo.buscarPorId(idReclamo);
    }
 
    public List<Reclamo> listarPorReclamante(String idReclamante) {
        return reclamoRepo.buscarPorReclamante(idReclamante);
    }
 
    public List<Reclamo> listarPorObjeto(int idObjeto) {
        return reclamoRepo.buscarPorObjeto(idObjeto);
    }
 
    
    public List<Reclamo> listarPendientes() {
        List<Reclamo> pendientes = new ArrayList<>();
        for (Reclamo r : reclamoRepo.obtenerReclamos()) {
            if (r.getEstado().equals("PENDIENTE")) {
                pendientes.add(r);
            }
        }
        return pendientes;
    }
    
    public int contarCoincidencias(String idReclamo) {
        Reclamo reclamo = reclamoRepo.buscarPorId(idReclamo);
        if (reclamo == null) {
            return -1;
        }
 
        String[] datosReales = reclamo.getObjeto().datosVerificables();
        ArrayList<String> respuestas = reclamo.getRespuestas();
 
        int coincidencias = 0;
        for (int i = 0; i < datosReales.length && i < respuestas.size(); i++) {
            if (datosReales[i].trim().equalsIgnoreCase(respuestas.get(i).trim())) {
                coincidencias++;
            }
        }
        return coincidencias;
    }
    
    
}