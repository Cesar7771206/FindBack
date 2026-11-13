package servicio;

import modelo.Estudiante;
import modelo.Usuario;
import repositorio.UsuarioCsvRepositorio;
import repositorio.UsuarioRepositorio;

public class AutenticacionServicio {
    private final UsuarioRepositorio usuarioRepo;
    
    public AutenticacionServicio() {
        this(new UsuarioCsvRepositorio());
    }

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

        String query = correoOCodigo.trim();
        String pass = contrasena.trim();

        Usuario usuario = usuarioRepo.buscarPorCorreo(query);
        if (usuario == null) {
            usuario = usuarioRepo.obtenerUsuarioPorIdentificacion(query);
        }

        if (usuario instanceof Estudiante &&
                usuario.getContrasena().trim().equals(pass)) {
            return (Estudiante) usuario;
        }

        return null;
    }
}


