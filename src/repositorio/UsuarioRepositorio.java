/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

import java.util.List;
import modelo.Usuario;

/**
 *
 * @author cesar
 */
public interface UsuarioRepositorio {
    List<Usuario> obtenerUsuarios();
    Usuario obtenerUsuarioPorIdentificacion(String identificacion);
}
