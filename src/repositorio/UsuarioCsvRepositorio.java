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
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import modelo.Estudiante;

/**
 *
 * @author cesar
 */
public class UsuarioCsvRepositorio implements UsuarioRepositorio {

    private final Path RUTA_ARCHIVO;

    public UsuarioCsvRepositorio() {
        this(Paths.get("data/usuarios.csv"));
    }

    public UsuarioCsvRepositorio(Path rutaArchivo) {
        this.RUTA_ARCHIVO = rutaArchivo;
        inicializar();
    }

    private void inicializar() {
        try {
            if (RUTA_ARCHIVO.getParent() != null) {
                Files.createDirectories(RUTA_ARCHIVO.getParent());
            }
            if (Files.notExists(RUTA_ARCHIVO) || Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8).isEmpty()) {
                Files.write(RUTA_ARCHIVO, List.of("identificacion|nombre|correo|contrasena|facultad|carrera|ciclo|mensajes"), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo", e);
        }
    }
    
    @Override
    public void guardar(Usuario usuario) {
        try {
            Files.write(RUTA_ARCHIVO, List.of(usuario.toTxt()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo escribir en el archivo: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8);
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] atributos = linea.split("\\|");

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
                usuarios.add(estudiante);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el CSV", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error al parsear el ciclo del estudiante. Verifica que sea un número.", e);
        }
        return usuarios;
    }
    
    @Override
    public Usuario obtenerUsuarioPorIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.isBlank()) {
            return null;
        }
        List<Usuario> usuarios = obtenerUsuarios();
        for (Usuario u : usuarios) {
            if (u != null && u.getIdentificacion() != null) {
                if (u.getIdentificacion().equalsIgnoreCase(identificacion.trim())) {
                    return u;
                }
            }
        }
        return null;
    }

    public void reescribir(List<Usuario> lista_nueva) {
        List<String> lineas = new ArrayList<>();
        lineas.add("identificacion|nombre|correo|contrasena|facultad|carrera|ciclo|mensajes");
        for (Usuario u : lista_nueva) {
            lineas.add(u.toTxt());
        }
        try {
            Files.write(RUTA_ARCHIVO, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo reescribir en el archivo: " + e.getMessage(), e); 
        }
    }
    
    @Override
    public void actualizarUsuario(String identificacionBuscar, String nombreNuevo, String correoNuevo, String contrasenaNueva, String carreraNueva, String facultadNueva, int cicloNuevo) {
        List<Usuario> usuarios = obtenerUsuarios();
        boolean encontrado = false;
        for (Usuario u : usuarios) {
            if (u.getIdentificacion().equalsIgnoreCase(identificacionBuscar)) {
                u.setNombre(nombreNuevo);
                u.setCorreo(correoNuevo);
                u.setContrasena(contrasenaNueva);
                if (u instanceof Estudiante) {
                    Estudiante e = (Estudiante) u;
                    e.setFacultad(facultadNueva);
                    e.setCarrera(carreraNueva);
                    e.setCiclo(cicloNuevo);
                }
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            reescribir(usuarios);
        }
    }
    
    @Override
    public void eliminarUsuario(String identificacion) {
        List<Usuario> usuarios = obtenerUsuarios();
        boolean eliminado = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getIdentificacion().equalsIgnoreCase(identificacion)) {
                usuarios.remove(i);
                eliminado = true;
                break;
            }
        }
        if (eliminado) {
            reescribir(usuarios);
        }
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return null;
        }
        List<Usuario> usuarios = obtenerUsuarios();
        for (Usuario u : usuarios) {
            if (u != null && u.getCorreo() != null) {
                if (u.getCorreo().equalsIgnoreCase(correo.trim())) {
                    return u;
                }
            }
        }
        return null;
    }

}
