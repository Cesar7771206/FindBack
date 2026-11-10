/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositorio;

import java.util.List;
import modelo.*;

/**
 *
 * @author david
 */
public interface ObjetoReportadoRepositorio {

    void guardar(ObjetoReportado objeto);

    List<ObjetoReportado> obtenerObjetos();

    ObjetoReportado obtenerObjetoPorId(int id);

    void eliminarObjeto(int id);

    void actualizarObjeto(int idBuscar, String nombreNuevo, String descripcionNueva, String categoriaNueva,
            String colorNuevo, String fechaNUeva, String ubicacionNueva, String estadoNuevo, Estudiante reportadoPor, String talla, String marca);

}
