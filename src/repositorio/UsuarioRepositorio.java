package repositorio;

import java.util.List;
import modelo.Usuario;

public interface UsuarioRepositorio {

    void guardar(Usuario usuario);

    List<Usuario> obtenerUsuarios();

    Usuario obtenerUsuarioPorIdentificacion(String identificacion);
}
