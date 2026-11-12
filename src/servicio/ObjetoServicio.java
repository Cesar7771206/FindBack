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
    
    private final ObjetoReportadoRepositorio objetoRepo;

    public ObjetoServicio(ObjetoReportadoRepositorio objetoRepo) {
        this.objetoRepo= objetoRepo;
    }
    
    public void registrarObjeto(ObjetoReportado objeto) {
    objetoRepo.guardar(objeto);
    }
    
    
    public void actualizarEstado(int idObjeto, String nuevoEstado){
        
        ObjetoReportado obj= objetoRepo.obtenerObjetoPorId(idObjeto);
        
        
        if (obj != null) {

            Estudiante estudiante = obj.getReportadoPor();

            if (obj instanceof Prenda) {
                Prenda prenda = (Prenda) obj;

                objetoRepo.actualizarObjetoPrenda(idObjeto, obj.getNombre(), obj.getDescripcion(), obj.getCategoria(), obj.getColor(), obj.getFecha(),
                        obj.getUbicacion(), nuevoEstado, estudiante, prenda.getTalla(), prenda.getMarca());

            } else if (obj instanceof Documento) {
                Documento documento = (Documento) obj;

                objetoRepo.actualizarObjetoDocumento(idObjeto, obj.getNombre(), obj.getDescripcion(), obj.getCategoria(), obj.getColor(), obj.getFecha(),
                        obj.getUbicacion(), nuevoEstado, estudiante, documento.getTipoDocumento(), documento.getNombreTitular());

            } else if (obj instanceof DispositivoElectronico) {
                DispositivoElectronico dispositivo = (DispositivoElectronico) obj;

                objetoRepo.actualizarObjetoDispositivoElectronico(
                        idObjeto, obj.getNombre(), obj.getDescripcion(), obj.getCategoria(), obj.getColor(), obj.getFecha(), obj.getUbicacion(), nuevoEstado, estudiante,
                        dispositivo.getMarca(), dispositivo.getModelo(), dispositivo.getNumeroSerie());

            } else if (obj instanceof ObjetoGenerico) {
                ObjetoGenerico generico = (ObjetoGenerico) obj;

                objetoRepo.actualizarObjetoGenerico(idObjeto, obj.getNombre(), obj.getDescripcion(),obj.getCategoria(), obj.getColor(), obj.getFecha(),
                        obj.getUbicacion(), nuevoEstado, estudiante,generico.getDetalleExtra());
            }
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

        int mayor = 0;

        for (ObjetoReportado objeto : objetoRepo.obtenerObjetos()) {
            if (objeto.getId() > mayor) {
                mayor = objeto.getId();
            }
        }

        return mayor + 1;
    }
 
    
}
