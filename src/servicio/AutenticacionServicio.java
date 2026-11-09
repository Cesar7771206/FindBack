package servicio;

import modelo.Estudiante;
import modelo.Usuario;
import repositorio.UsuarioCsvRepositorio;

public class AutenticacionServicio {
    private UsuarioCsvRepositorio usuarioRepo;
    
    public AutenticacionServicio (UsuarioCsvRepositorio usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }
    
    public boolean registrarUsuario(String identificacion, String nombre, String correo, String contrasena, String mensajes, String facultad, String carrera, int ciclo) {
        
        try {
            if (usuarioRepo.obtenerUsuarioPorIdentificacion(identificacion)!= null || usuarioRepo.buscarPorCorreo(correo) != null) {
                return false;
            }
        } catch (RuntimeException e) {
            //ya que el usuario no fue encontrado
        }
        Estudiante nuevo = new Estudiante(facultad,carrera, ciclo, identificacion, nombre, correo, contrasena);

        usuarioRepo.guardar(nuevo);
        return true;
    }
    
    public Estudiante login(String correoOCodigo, String contrasena) {

        Usuario usuario = null;

        try {
            usuario = usuarioRepo.buscarPorCorreo(correoOCodigo);
        } catch (RuntimeException e) {
            try {
                usuario = usuarioRepo.obtenerUsuarioPorIdentificacion(correoOCodigo);
            } catch (RuntimeException ex) {
                return null;
            }
        }

        if (usuario instanceof Estudiante &&
                usuario.getContrasena().equals(contrasena)) {
            return (Estudiante) usuario;
        }

        return null;
    }
}

