package servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.ObjetoReportado;
import repositorio.ObjetoReportadoCsvRepositorio;
import repositorio.ObjetoReportadoRepositorio;

/**
 * Servicio encargado de buscar coincidencias entre objetos perdidos y encontrados.
 * 
 * @author david
 */
public class CoincidenciaServicio {
    
    private final ObjetoReportadoRepositorio objetoRepo;

    public CoincidenciaServicio() {
        this(new ObjetoReportadoCsvRepositorio());
    }
    
    public CoincidenciaServicio(ObjetoReportadoRepositorio objetoRepo) {
        this.objetoRepo = objetoRepo;
    }

    public List<ObjetoReportado> buscarCoincidencias(ObjetoReportado base) {
        if (base == null || base.getEstado() == null) {
            return Collections.emptyList();
        }

        List<ObjetoReportado> coincidencias = new ArrayList<>();
        String estadoBuscado = base.getEstado().equalsIgnoreCase("PERDIDO") ? "ENCONTRADO" : "PERDIDO";

        for (ObjetoReportado candidato : objetoRepo.obtenerObjetos()) {
            if (candidato == null || candidato.getId() == base.getId()) {
                continue;
            }

            if (!candidato.getEstado().equalsIgnoreCase(estadoBuscado)) {
                continue;
            }

            if (!candidato.getCategoria().equalsIgnoreCase(base.getCategoria())) {
                continue;
            }

            boolean coincideNombre = false;
            try {
                coincideNombre = candidato.coincideCon(base.getNombre()) || base.coincideCon(candidato.getNombre());
            } catch (Exception ignored) {
            }

            boolean coincideColor = candidato.getColor() != null && candidato.getColor().equalsIgnoreCase(base.getColor());
            
            if (coincideNombre || coincideColor) {
                coincidencias.add(candidato);
            }
        }

        return coincidencias;
    }
}
