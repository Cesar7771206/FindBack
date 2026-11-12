package servicio;

import modelo.Estudiante;
import modelo.Usuario;
import repositorio.UsuarioRepositorio;

public class AutenticacionServicio {
    private final UsuarioRepositorio usuarioRepo;
    
    public AutenticacionServicio(UsuarioRepositorio usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }
    
    public boolean registrarUsuario(String identificacion, String nombre, String correo, String contrasena, String mensajes, String facultad, String carrera, int ciclo) {
        if (identificacion == null || identificacion.isBlank() || correo == null || correo.isBlank()) {
            return false;
        }
        
        if (usuarioRepo.obtenerUsuarioPorIdentificacion(identificacion) != null || usuarioRepo.buscarPorCorreo(correo) != null) {
            return false;
        }

        Estudiante nuevo = new Estudiante(facultad, carrera, ciclo, identificacion, nombre, correo, contrasena);
        if (mensajes != null && !mensajes.isBlank() && !mensajes.equals("SIN_MENSAJES")) {
            String[] msgs = mensajes.split(";");
            for (String m : msgs) {
                nuevo.recibirMensaje(m);
            }
        }

        usuarioRepo.guardar(nuevo);
        return true;
    }
    
    public Estudiante login(String correoOCodigo, String contrasena) {
        if (correoOCodigo == null || contrasena == null) {
            return null;
        }

        Usuario usuario = usuarioRepo.buscarPorCorreo(correoOCodigo);
        if (usuario == null) {
            usuario = usuarioRepo.obtenerUsuarioPorIdentificacion(correoOCodigo);
        }

        if (usuario instanceof Estudiante &&
                usuario.getContrasena().equals(contrasena)) {
            return (Estudiante) usuario;
        }

        return null;
    }
}

