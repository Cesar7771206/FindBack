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
                Files.write(RUTA_ARCHIVO, List.of("identificacion|nombre|correo|contrasena|facultad|carrera|ciclo|mensajes"), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo", e);
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

}
