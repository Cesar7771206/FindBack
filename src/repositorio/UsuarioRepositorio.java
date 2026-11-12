package repositorio;

import java.util.List;
import modelo.Usuario;

public interface UsuarioRepositorio {

    void guardar(Usuario usuario);

    List<Usuario> obtenerUsuarios();

    Usuario obtenerUsuarioPorIdentificacion(String identificacion);

    void actualizar(Usuario usuario);

    void actualizarUsuario(String identificacionBuscar, String nombreNuevo, String correoNuevo, String contrasenaNueva, String carreraNueva, String facultadNueva, int cicloNuevo);
    void eliminarUsuario(String identificacion);
    Usuario buscarPorCorreo(String correo);
    
}
