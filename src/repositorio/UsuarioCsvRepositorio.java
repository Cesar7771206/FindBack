/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

import modelo.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import modelo.Estudiante;

/**
 *
 * @author cesar
 */
public class UsuarioCsvRepositorio implements UsuarioRepositorio {

    private static final String CABECERA = "identificacion|nombre|correo|contrasena|facultad|carrera|ciclo|mensajes";
    private final Path RUTA_ARCHIVO;
    private List<Usuario> usuarios;

    public UsuarioCsvRepositorio() {
        this(Paths.get("data/usuarios.csv"));
    }

    public UsuarioCsvRepositorio(Path rutaArchivo) {
        this.RUTA_ARCHIVO = rutaArchivo;
        this.usuarios = new ArrayList<>();
        inicializar();
    }

    private void inicializar() {
        try {
            if (RUTA_ARCHIVO.getParent() != null) {
                Files.createDirectories(RUTA_ARCHIVO.getParent());
            }
            if (Files.notExists(RUTA_ARCHIVO) || Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8).isEmpty()) {
                Files.write(RUTA_ARCHIVO, List.of(CABECERA), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo", e);
        }
        this.usuarios = cargarUsuarios();
    }

    private List<Usuario> cargarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        if (Files.notExists(RUTA_ARCHIVO)) {
            return lista;
        }
        try {
            List<String> lineas = Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8);
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] atributos = linea.split("\\|", -1);
                if (atributos.length < 7) {
                    continue;
                }

                String identificacion = atributos[0];
                String nombre = atributos[1];
                String correo = atributos[2];
                String contrasena = atributos[3];

                String facultad = atributos[4];
                String carrera = atributos[5];
                int ciclo = Integer.parseInt(atributos[6]);

                String[] mensajes_estatico = (atributos.length > 7 && !atributos[7].isEmpty() && !atributos[7].equals("SIN_MENSAJES"))
                        ? atributos[7].split(";")
                        : new String[0];

                ArrayList<String> mensajes = new ArrayList<>(List.of(mensajes_estatico));

                Estudiante estudiante = new Estudiante(facultad, carrera, ciclo, identificacion, nombre, correo, contrasena);
                estudiante.setMensajes(mensajes);
                lista.add(estudiante);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el CSV", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error al parsear el ciclo del estudiante. Verifica que sea un número.", e);
        }
        return lista;
    }
    
    @Override
    public void guardar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        usuarios.add(usuario);
        reescribir();
    }

    @Override
    public List<Usuario> obtenerUsuarios() {
        return usuarios;
    }
    
    @Override
    public Usuario obtenerUsuarioPorIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.isBlank()) {
            return null;
        }
        for (Usuario u : usuarios) {
            if (u != null && u.getIdentificacion() != null) {
                if (u.getIdentificacion().equalsIgnoreCase(identificacion.trim())) {
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public void actualizar(Usuario usuarioActualizado) {
        if (usuarioActualizado == null) {
            return;
        }
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getIdentificacion().equalsIgnoreCase(usuarioActualizado.getIdentificacion())) {
                usuarios.set(i, usuarioActualizado);
                reescribir();
                return;
            }
        }
    }

    @Override
    public void actualizarUsuario(String identificacionBuscar, String nombreNuevo, String correoNuevo, String contrasenaNueva, String carreraNueva, String facultadNueva, int cicloNuevo) {
        for (Usuario u : usuarios) {
            if (u.getIdentificacion().equalsIgnoreCase(identificacionBuscar)) {
                u.setNombre(nombreNuevo);
                u.setCorreo(correoNuevo);
                u.setContrasena(contrasenaNueva);
                if (u instanceof Estudiante e) {
                    e.setFacultad(facultadNueva);
                    e.setCarrera(carreraNueva);
                    e.setCiclo(cicloNuevo);
                }
                reescribir();
                return;
            }
        }
    }
    
    @Override
    public void eliminarUsuario(String identificacion) {
        boolean eliminado = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getIdentificacion().equalsIgnoreCase(identificacion)) {
                usuarios.remove(i);
                eliminado = true;
                break;
            }
        }
        if (eliminado) {
            reescribir();
        }
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return null;
        }
        for (Usuario u : usuarios) {
            if (u != null && u.getCorreo() != null) {
                if (u.getCorreo().equalsIgnoreCase(correo.trim())) {
                    return u;
                }
            }
        }
        return null;
    }

    public void reescribir() {
        reescribir(this.usuarios);
    }

    public void reescribir(List<Usuario> lista_nueva) {
        List<String> lineas = new ArrayList<>();
        lineas.add(CABECERA);
        if (lista_nueva != null) {
            for (Usuario u : lista_nueva) {
                lineas.add(u.toTxt());
            }
        }
        try {
            Files.write(RUTA_ARCHIVO, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo reescribir en el archivo: " + e.getMessage(), e); 
        }
    }

}
