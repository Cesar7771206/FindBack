package servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;
import modelo.Usuario;
import repositorio.ObjetoReportadoCsvRepositorio;
import repositorio.ObjetoReportadoRepositorio;
import repositorio.ReclamoCsvRepositorio;
import repositorio.ReclamoRepositorio;
import repositorio.UsuarioCsvRepositorio;
import repositorio.UsuarioRepositorio;

public class ReclamoServicio {

    private final ReclamoRepositorio reclamoRepo;
    private final ObjetoReportadoRepositorio objetoRepo;
    private final UsuarioRepositorio usuarioRepo;

    public ReclamoServicio() {
        this(new ReclamoCsvRepositorio(), new ObjetoReportadoCsvRepositorio(), new UsuarioCsvRepositorio());
    }

    public ReclamoServicio(ReclamoRepositorio reclamoRepo,
                           ObjetoReportadoRepositorio objetoRepo,
                           UsuarioRepositorio usuarioRepo) {
        this.reclamoRepo = reclamoRepo;
        this.objetoRepo = objetoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public boolean crearReclamo(int idObjeto, String idReclamante, List<String> respuestas) {
        if (idReclamante == null || idReclamante.isBlank()) {
            return false;
        }

        Usuario usuario = usuarioRepo.obtenerUsuarioPorIdentificacion(idReclamante.trim());
        if (!(usuario instanceof Estudiante)) {
            return false;
        }

        return crearReclamo(idObjeto, (Estudiante) usuario, respuestas);
    }

    public boolean crearReclamo(int idObjeto, Estudiante reclamante, List<String> respuestas) {
        if (reclamante == null || respuestas == null) {
            return false;
        }

        ObjetoReportado objeto = objetoRepo.obtenerObjetoPorId(idObjeto);
        if (objeto == null) {
            return false;
        }

        if ("ENTREGADO".equalsIgnoreCase(objeto.getEstado())) {
            return false;
        }

        if (objeto.getReportadoPor() != null &&
            objeto.getReportadoPor().getIdentificacion().equalsIgnoreCase(reclamante.getIdentificacion())) {
            return false;
        }

        int totalPreguntas = objeto.preguntasVerificacion().length;
        if (respuestas.size() != totalPreguntas) {
            return false;
        }

        if (existeReclamoPendiente(idObjeto, reclamante.getIdentificacion())) {
            return false;
        }

        String nuevoId = reclamoRepo.generarSiguienteId();
        ArrayList<String> listaRespuestas = new ArrayList<>(respuestas);
        Reclamo nuevo = new Reclamo(nuevoId, objeto, reclamante, listaRespuestas, "PENDIENTE", "", "");
        reclamoRepo.guardar(nuevo);

        notificarReclamante(nuevo, "Se ha registrado tu reclamo " + nuevoId + " para el objeto '" + objeto.getNombre() + "'.");
        return true;
    }

    public boolean existeReclamoPendiente(int idObjeto, String idReclamante) {
        if (idReclamante == null || idReclamante.isBlank()) {
            return false;
        }
        List<Reclamo> reclamosEstudiante = reclamoRepo.buscarPorReclamante(idReclamante.trim());
        for (Reclamo r : reclamosEstudiante) {
            if (r.getObjeto() != null &&
                r.getObjeto().getId() == idObjeto &&
                "PENDIENTE".equalsIgnoreCase(r.getEstado())) {
                return true;
            }
        }
        return false;
    }

    public List<Reclamo> listarReclamos() {
        return reclamoRepo.obtenerReclamos();
    }

    public List<Reclamo> listarTodos() {
        return reclamoRepo.obtenerReclamos();
    }

    public Reclamo buscarPorId(String idReclamo) {
        if (idReclamo == null || idReclamo.isBlank()) {
            return null;
        }
        return reclamoRepo.buscarPorId(idReclamo.trim());
    }

    public Reclamo obtenerPorId(String idReclamo) {
        return buscarPorId(idReclamo);
    }

    public List<Reclamo> listarPorReclamante(String idReclamante) {
        if (idReclamante == null || idReclamante.isBlank()) {
            return Collections.emptyList();
        }
        return reclamoRepo.buscarPorReclamante(idReclamante.trim());
    }

    public List<Reclamo> listarPorObjeto(int idObjeto) {
        return reclamoRepo.buscarPorObjeto(idObjeto);
    }

    public List<Reclamo> listarPendientes() {
        return listarPorEstado("PENDIENTE");
    }

    public List<Reclamo> listarPorEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return Collections.emptyList();
        }
        List<Reclamo> resultado = new ArrayList<>();
        for (Reclamo r : reclamoRepo.obtenerReclamos()) {
            if (r.getEstado() != null && r.getEstado().equalsIgnoreCase(estado.trim())) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public int contarCoincidencias(String idReclamo) {
        Reclamo reclamo = buscarPorId(idReclamo);
        if (reclamo == null || reclamo.getObjeto() == null) {
            return -1;
        }

        String[] datosReales = reclamo.getObjeto().datosVerificables();
        ArrayList<String> respuestas = reclamo.getRespuestas();
        if (datosReales == null || respuestas == null) {
            return 0;
        }

        int coincidencias = 0;
        for (int i = 0; i < datosReales.length && i < respuestas.size(); i++) {
            String real = datosReales[i];
            String resp = respuestas.get(i);
            if (real != null && resp != null && real.trim().equalsIgnoreCase(resp.trim())) {
                coincidencias++;
            }
        }
        return coincidencias;
    }

    public double calcularPorcentajeCoincidencia(String idReclamo) {
        Reclamo reclamo = buscarPorId(idReclamo);
        if (reclamo == null || reclamo.getObjeto() == null) {
            return 0.0;
        }
        String[] datosReales = reclamo.getObjeto().datosVerificables();
        if (datosReales == null || datosReales.length == 0) {
            return 0.0;
        }
        int coincidencias = contarCoincidencias(idReclamo);
        if (coincidencias < 0) {
            return 0.0;
        }
        return ((double) coincidencias / datosReales.length) * 100.0;
    }

    public boolean aprobarReclamo(String idReclamo, String codigoRevisor, String observacion) {
        Reclamo reclamo = buscarPorId(idReclamo);
        if (reclamo == null) {
            return false;
        }

        if (!"PENDIENTE".equalsIgnoreCase(reclamo.getEstado())) {
            return false;
        }

        reclamo.setEstado("APROBADO");
        reclamo.setCodigoRevisor(codigoRevisor != null ? codigoRevisor.trim() : "");
        reclamo.setObservacion(observacion != null ? observacion.trim() : "");
        reclamoRepo.actualizar(reclamo);

        ObjetoReportado objeto = reclamo.getObjeto();
        if (objeto != null) {
            objeto.setEstado("ENTREGADO");
            objetoRepo.actualizar(objeto);

            if (objeto.getReportadoPor() != null) {
                objeto.getReportadoPor().recibirMensaje("El objeto '" + objeto.getNombre() + "' que reportaste ha sido entregado exitosamente a su dueño.");
                usuarioRepo.actualizar(objeto.getReportadoPor());
            }

            List<Reclamo> otros = reclamoRepo.buscarPorObjeto(objeto.getId());
            for (Reclamo otro : otros) {
                if (!otro.getId().equalsIgnoreCase(reclamo.getId()) && "PENDIENTE".equalsIgnoreCase(otro.getEstado())) {
                    otro.setEstado("RECHAZADO");
                    otro.setCodigoRevisor(codigoRevisor != null ? codigoRevisor.trim() : "");
                    otro.setObservacion("Objeto entregado a otro reclamante verificado.");
                    reclamoRepo.actualizar(otro);
                    notificarReclamante(otro, "Tu reclamo " + otro.getId() + " fue RECHAZADO porque el objeto fue entregado a otro reclamante.");
                }
            }
        }

        notificarReclamante(reclamo, "Tu reclamo " + reclamo.getId() + " para el objeto '" + (objeto != null ? objeto.getNombre() : "") + "' fue APROBADO.");
        return true;
    }

    public boolean rechazarReclamo(String idReclamo, String codigoRevisor, String observacion) {
        Reclamo reclamo = buscarPorId(idReclamo);
        if (reclamo == null) {
            return false;
        }

        if (!"PENDIENTE".equalsIgnoreCase(reclamo.getEstado())) {
            return false;
        }

        reclamo.setEstado("RECHAZADO");
        reclamo.setCodigoRevisor(codigoRevisor != null ? codigoRevisor.trim() : "");
        reclamo.setObservacion(observacion != null ? observacion.trim() : "");
        reclamoRepo.actualizar(reclamo);

        String motivo = (observacion != null && !observacion.isBlank()) ? " Motivo: " + observacion.trim() : "";
        notificarReclamante(reclamo, "Tu reclamo " + reclamo.getId() + " fue RECHAZADO." + motivo);
        return true;
    }

    public boolean eliminarReclamo(String idReclamo) {
        Reclamo reclamo = buscarPorId(idReclamo);
        if (reclamo == null) {
            return false;
        }
        reclamoRepo.eliminar(idReclamo);
        return true;
    }

    public String generarNuevoId() {
        return reclamoRepo.generarSiguienteId();
    }

    private void notificarReclamante(Reclamo reclamo, String mensaje) {
        if (reclamo == null || mensaje == null || mensaje.isBlank()) {
            return;
        }
        Estudiante estudiante = reclamo.getReclamante();
        if (estudiante != null) {
            estudiante.recibirMensaje(mensaje);
            usuarioRepo.actualizar(estudiante);
        }
    }
}
