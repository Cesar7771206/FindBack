package servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.ObjetoReportado;
import repositorio.ObjetoReportadoCsvRepositorio;
import repositorio.ObjetoReportadoRepositorio;

/**
 * Servicio encargado de la lógica de negocio para la gestión de objetos reportados.
 * Provee operaciones para registrar, consultar, actualizar, buscar y filtrar objetos.
 * 
 * @author david
 */
public class ObjetoServicio {
    
    private final ObjetoReportadoRepositorio objetoRepo;

    public ObjetoServicio() {
        this(new ObjetoReportadoCsvRepositorio());
    }

    public ObjetoServicio(ObjetoReportadoRepositorio objetoRepo) {
        this.objetoRepo = objetoRepo;
    }
    
    public boolean registrarObjeto(ObjetoReportado objeto) {
        if (objeto == null) {
            return false;
        }
        objetoRepo.guardar(objeto);
        return true;
    }
    
    public ObjetoReportado obtenerPorId(int idObjeto) {
        return objetoRepo.obtenerObjetoPorId(idObjeto);
    }
    
    public boolean actualizarEstado(int idObjeto, String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return false;
        }
        ObjetoReportado obj = objetoRepo.obtenerObjetoPorId(idObjeto);
        if (obj != null) {
            obj.setEstado(nuevoEstado);
            objetoRepo.actualizar(obj);
            return true;
        }
        return false;
    }
    
    public boolean actualizarObjeto(ObjetoReportado objeto) {
        if (objeto == null) {
            return false;
        }
        objetoRepo.actualizar(objeto);
        return true;
    }
    
    public boolean eliminarObjeto(int idObjeto) {
        if (objetoRepo.obtenerObjetoPorId(idObjeto) == null) {
            return false;
        }
        objetoRepo.eliminarObjeto(idObjeto);
        return true;
    }
    
    public List<ObjetoReportado> buscar(String criterio) {
        if (criterio == null || criterio.isBlank()) {
            return listarTodos();
        }
        List<ObjetoReportado> resultado = new ArrayList<>();
        for (ObjetoReportado objeto : objetoRepo.obtenerObjetos()) {
            try {
                if (objeto.coincideCon(criterio)) {
                    resultado.add(objeto);
                }
            } catch (Exception ignored) {
            }
        }
        return resultado;
    }
    
    public List<ObjetoReportado> listarPorEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return Collections.emptyList();
        }
        List<ObjetoReportado> resultado = new ArrayList<>();
        for (ObjetoReportado objeto : objetoRepo.obtenerObjetos()) {
            if (objeto.getEstado().equalsIgnoreCase(estado.trim())) {
                resultado.add(objeto);
            }
        }
        return resultado;
    }
    
    public List<ObjetoReportado> listarPorCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return Collections.emptyList();
        }
        List<ObjetoReportado> resultado = new ArrayList<>();
        for (ObjetoReportado objeto : objetoRepo.obtenerObjetos()) {
            if (objeto.getCategoria().equalsIgnoreCase(categoria.trim())) {
                resultado.add(objeto);
            }
        }
        return resultado;
    }
    
    public List<ObjetoReportado> listarPorEstudiante(String identificacion) {
        if (identificacion == null || identificacion.isBlank()) {
            return Collections.emptyList();
        }
        List<ObjetoReportado> resultado = new ArrayList<>();
        for (ObjetoReportado objeto : objetoRepo.obtenerObjetos()) {
            if (objeto.getReportadoPor() != null &&
                objeto.getReportadoPor().getIdentificacion().equalsIgnoreCase(identificacion.trim())) {
                resultado.add(objeto);
            }
        }
        return resultado;
    }
    
    public List<ObjetoReportado> listarTodos() {
        return new ArrayList<>(objetoRepo.obtenerObjetos());
    }
    
    public int generarNuevoId() {
        return objetoRepo.generarSiguienteId();
    }
}
