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
        this.RUTA_ARCHIVO = Paths.get("data/usuarios.csv");
        inicializar();
    }

    private void inicializar() {
        try {
            if (RUTA_ARCHIVO.getParent() != null) {
                Files.createDirectories(RUTA_ARCHIVO.getParent());
            }
            if (Files.notExists(RUTA_ARCHIVO) || Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8).isEmpty()) {
                Files.write(RUTA_ARCHIVO, List.of("identificacion|nombre|correo|contrasena|mensajes|facultad|carrera|ciclo"), StandardCharsets.UTF_8);
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
            System.out.println("Error al guardar en estudiantes.csv: " + e.getMessage());
        }
    }


    @Override
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8);
            for (String linea : lineas) {

                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] atributos = linea.split("\\|");

                String identificacion = atributos[0];
                String nombre = atributos[1];
                String correo = atributos[2];
                String contrasena = atributos[3];

                String[] mensajes_estatico = atributos[4].isEmpty() ? new String[0] : atributos[4].split(";");

                String facultad = atributos[5];
                String carrerar = atributos[6];
                int ciclo = Integer.parseInt(atributos[7]);

                ArrayList<String> mensajes;
                mensajes = new ArrayList<>(List.of(mensajes_estatico));

                Estudiante estudiante = new Estudiante(facultad, carrerar, ciclo, identificacion, nombre, correo, contrasena);
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
        List<Usuario> usuarios = obtenerUsuarios();
        for (Usuario u : usuarios) {
            if (u != null && u.getIdentificacion() != null) {
                if (u.getIdentificacion().equalsIgnoreCase(identificacion)) {
                    return u; // Retorna el usuario encontrado (sea Estudiante o cualquier otro tipo de Usuario)
                }
            }
        }
        throw new RuntimeException("No se encontró ningún usuario con la identificación: " + identificacion);
    }
    public void reescribir (List<Usuario> lista_nueva) {
        List <String> lineas = new ArrayList<>();
        lineas.add("identificacion|nombre|correo|contrasena|mensajes|facultad|carrera|ciclo");
        for (Usuario u: lista_nueva){
            lineas.add(u.toTxt());
        }
        try {
            Files.write(RUTA_ARCHIVO, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException ("No se pudo reescribir en el archivo" + e); 
        }
            
    }
    
    @Override
    public void actualizarUsuario(String identificacionBuscar, String nombreNuevo, String correoNuevo, String contrasenaNueva) {
        List<Usuario> usuarios = obtenerUsuarios();
        for (Usuario u: usuarios) {
            if (u.getIdentificacion().equals(identificacionBuscar)) {
                u.setNombre(nombreNuevo);
                u.setCorreo(correoNuevo);
                u.setContrasena(contrasenaNueva);
                break;
                
            }
        }
        reescribir(usuarios);
        
    }
    
    @Override
    public void eliminarUsuario(String identificacion) {
        List <Usuario> usuarios = obtenerUsuarios();
        for (int i = 0; i<usuarios.size(); i++) {
            if (usuarios.get(i).getIdentificacion().equalsIgnoreCase(identificacion)){
                usuarios.remove(i);
                break;
            }
            
        }
    }

}
