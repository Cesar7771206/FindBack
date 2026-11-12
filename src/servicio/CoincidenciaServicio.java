/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.List;
import modelo.ObjetoReportado;
import repositorio.ObjetoReportadoRepositorio;


/**
 *
 * @author david
 */
public class CoincidenciaServicio {
    
    private final ObjetoReportadoRepositorio objetoRepo;
    
     public CoincidenciaServicio(ObjetoReportadoRepositorio objetoRepo) {
        this.objetoRepo = objetoRepo;
    }

    public List<ObjetoReportado> buscarCoincidencias(ObjetoReportado base) {
        List<ObjetoReportado> coincidencias = new ArrayList<>();

 
        String estadoBuscado = base.getEstado().equalsIgnoreCase("PERDIDO")
                ? "ENCONTRADO" : "PERDIDO";

        
        for (ObjetoReportado candidato : objetoRepo.obtenerObjetos()) {

           
            if (!candidato.getEstado().equalsIgnoreCase(estadoBuscado)) {
                continue;
            }

     
            if (!candidato.getCategoria().equalsIgnoreCase(base.getCategoria())) {
                continue;
            }

 
            boolean coincideNombre = candidato.coincideCon(base.getNombre()) || base.coincideCon(candidato.getNombre());

            boolean coincideColor = candidato.getColor().equalsIgnoreCase(base.getColor());
            
            if (coincideNombre || coincideColor) {
                coincidencias.add(candidato);
            }
        }

        return coincidencias;
    }
}
