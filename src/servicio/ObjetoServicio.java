/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.List;
import modelo.*;
import repositorio.ObjetoReportadoRepositorio;
/**
 *
 * @author david
 */
public class ObjetoServicio {
    
    private  ObjetoReportadoRepositorio objetoRepo;


    public ObjetoServicio(ObjetoReportadoRepositorio objetoRepo) {
        this.objetoRepo= objetoRepo;
    }
    
    public void registrarObjeto(ObjetoReportado objeto) {
    objetoRepo.guardar(objeto);
    }
    
    
    public void actualizarEstado(int idObjeto, String nuevoEstado){
        
        ObjetoReportado obj= objetoRepo.obtenerObjetoPorId(idObjeto);
        
        if (obj !=  null) {
            obj.setEstado(nuevoEstado);
            objetoRepo.actualizar(obj);
        }
    }
    
    public List<ObjetoReportado> listarPorEstado(String estado) {

        List<ObjetoReportado> resultado = new ArrayList<>();

        for (ObjetoReportado objeto : objetoRepo.obtenerObjetos()) {
            if (objeto.getEstado().equalsIgnoreCase(estado)) {
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
