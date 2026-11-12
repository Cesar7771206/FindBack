
package servicio;

import modelo.*;
import repositorio.ObjetoReportadoRepositorio;
import java.util.ArrayList;

public class ObjetoServicio {

    private final ObjetoReportadoRepositorio objetoRepo;

    public ObjetoServicio(ObjetoReportadoRepositorio objetoRepo) {
        this.objetoRepo = objetoRepo;
    }

    public void registrarObjeto(ObjetoReportado objeto) {
        objetoRepo.guardar(objeto);
    }

    public void actualizarEstado(int idObjeto, String nuevoEstado) {
        ObjetoReportado obj = objetoRepo.obtenerObjetoPorId(idObjeto);

        if (obj != null) {
            obj.setEstado(nuevoEstado);
            objetoRepo.actualizar(obj);
        }
    }

    public ArrayList<ObjetoReportado> listarPorEstado(String estado) {
        ArrayList<ObjetoReportado> resultado = new ArrayList<>();

        for (ObjetoReportado objeto : objetoRepo.obtenerObjetos()) {
            if (objeto.getEstado().equalsIgnoreCase(estado)) {
                resultado.add(objeto);
            }
        }

        return resultado;
    }

    public ArrayList<ObjetoReportado> listarTodos() {
        return new ArrayList<>(objetoRepo.obtenerObjetos());
    }

    public int generarNuevoId() {
        return objetoRepo.generarSiguienteId();
    }
}

