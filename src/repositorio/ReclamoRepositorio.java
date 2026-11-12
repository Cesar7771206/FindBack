/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositorio;

import java.util.List;
import modelo.Reclamo;

/**
 *
 * @author cesar
 */
public interface ReclamoRepositorio {

    void guardar(Reclamo reclamo);

    List<Reclamo> obtenerReclamos();

    Reclamo buscarPorId(String id);

    void eliminar(String id);

    void actualizar(String id, String estado, String observacion, String codigoRevisor);

    void actualizar(Reclamo reclamo);

    List<Reclamo> buscarPorReclamante(String identificacionReclamante);

    List<Reclamo> buscarPorObjeto(int idObjeto);

    String generarSiguienteId();

}
