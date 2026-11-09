package repositorio;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import modelo.*;

/**
 *
 * @author david
 */
public class ObjetoReportadoCsvRepositorio implements ObjetoReportadoRepositorio {

    private final Path RUTA_ARCHIVO;

    private UsuarioCsvRepositorio usuarioRepo;

    public ObjetoReportadoCsvRepositorio() {
        this.RUTA_ARCHIVO = Paths.get("data/objetos.csv");
        inicializar();
    }

    private void inicializar() {
        try {
            if (RUTA_ARCHIVO.getParent() != null) {
                Files.createDirectories(RUTA_ARCHIVO.getParent());
            }
            if (Files.notExists(RUTA_ARCHIVO) || Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8).isEmpty()) {
                Files.write(RUTA_ARCHIVO, List.of("id|nombre|descripcion|categoria|color|fecha|ubicacion|estado|reportado|caracteristicasPropias"), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo", e);
        }
    }

    @Override
    public void guardar(ObjetoReportado objeto) {
        
        try {
            Files.write(RUTA_ARCHIVO, List.of(objeto.toTxt()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo", e);
        }
    }

    @Override
    public List<ObjetoReportado> obtenerObjetos() {
        List<ObjetoReportado> objetos = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                String[] atributos = linea.split("\\|");
                int id = Integer.parseInt(atributos[0]);
                String nombre = atributos[1];
                String descripcion = atributos[2];
                String categoria = atributos[3];
                String color = atributos[4];
                String fecha = atributos[5];
                String ubicacion = atributos[6];
                String estado = atributos[7];
                String reportadoPor = atributos[8];
                String caracteristicasPropias = atributos[9];
                
                switch (categoria) {
                    case "DOCUMENTO" -> {
                        String[] atributos_documento = caracteristicasPropias.split(";");
                        Documento documento = new Documento(atributos_documento[0], atributos_documento[1],
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor));
                        objetos.add(documento);
                    }
                    case "PRENDA" -> { 
                        String[] atributos_prenda = caracteristicasPropias.split(";");
                        Prenda prenda = new Prenda(atributos_prenda[0], atributos_prenda[1],id, nombre, descripcion, categoria, color, fecha, ubicacion, estado,
                                (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor));
                        objetos.add(prenda);
                    }
                    
                    case "DISPOSITIVOELECTRONICO" -> {
                        String[] atributos_dispositivo = caracteristicasPropias.split(";");
                        DispositivoElectronico dispositivo = new DispositivoElectronico(atributos_dispositivo[0], atributos_dispositivo[1], atributos_dispositivo[2],
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor));
                        objetos.add(dispositivo);
                    }
                    
                    case "OBJETOGENERICO" -> {
                        String[] atributos_ObjetoGenerico = caracteristicasPropias.split(";");
                        ObjetoGenerico generico = new ObjetoGenerico(atributos_ObjetoGenerico[0], 
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor));
                        objetos.add(generico);
                    }
                        
                    default -> throw new AssertionError();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo", e);
        }
        return objetos;
    }

    @Override

    public ObjetoReportado obtenerObjetoPorId(int id) {
        List<ObjetoReportado> objetos = obtenerObjetos();
        for (ObjetoReportado O : objetos) {
            if (O.getId() == id) {
                return O;
            }
        }
        return null;
    }

    @Override
    public void eliminarObjeto(int id) {
        List<ObjetoReportado> objetos = obtenerObjetos();
        for (int i = 0; i < objetos.size(); i++) {
            if (objetos.get(i).getId() == id) {
                objetos.remove(i);
                break;
            }
        }
        reescribir(objetos);
    }

    public void reescribir(List<ObjetoReportado> lista_nueva) {
        List<String> lineas = new ArrayList<>();
        lineas.add("id|nombre|descripcion|categoria|color|fecha|ubicacion|estado|reportado|caracteristicasPropias");
        for (ObjetoReportado objeto : lista_nueva) {
            lineas.add(objeto.toTxt());
        }
        try {
            Files.write(RUTA_ARCHIVO, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo escribir en el archivo" + e);
        }
    }

}
