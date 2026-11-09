package repositorio;


import java.util.List;
import modelo.Usuario;

public interface UsuarioRepositorio {
    List<Usuario> obtenerUsuarios();
    Usuario obtenerUsuarioPorIdentificacion(String identificacion);
}
