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

    private UsuarioRepositorio usuarioRepo;

    public ObjetoReportadoCsvRepositorio() {
        this(Paths.get("data/objetos.csv"), new UsuarioCsvRepositorio());
    }

    public ObjetoReportadoCsvRepositorio(UsuarioRepositorio usuarioRepo) {
        this(Paths.get("data/objetos.csv"), usuarioRepo);
    }

    public ObjetoReportadoCsvRepositorio(Path rutaArchivo, UsuarioRepositorio usuarioRepo) {
        this.RUTA_ARCHIVO = rutaArchivo;
        this.usuarioRepo = usuarioRepo != null ? usuarioRepo : new UsuarioCsvRepositorio();
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
            throw new RuntimeException("No se pudo escribir en el archivo", e);
        }
    }

    @Override
    public List<ObjetoReportado> obtenerObjetos() {
        List<ObjetoReportado> objetos = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8);
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea.trim().isEmpty()) {
                    continue;
                }
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
                        String tipoDoc = atributos_documento.length > 0 ? atributos_documento[0] : "NO ESPECIFICADO";
                        String titular = atributos_documento.length > 1 ? atributos_documento[1] : "NO ESPECIFICADO";
                        Documento documento = new Documento(tipoDoc, titular,
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor));
                        objetos.add(documento);
                    }
                    case "PRENDA" -> { 
                        String[] atributos_prenda = caracteristicasPropias.split(";");
                        String talla = atributos_prenda.length > 0 ? atributos_prenda[0] : "NO ESPECIFICADO";
                        String marca = atributos_prenda.length > 1 ? atributos_prenda[1] : "NO ESPECIFICADO";
                        Prenda prenda = new Prenda(talla, marca, id, nombre, descripcion, categoria, color, fecha, ubicacion, estado,
                                (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor));
                        objetos.add(prenda);
                    }
                    
                    case "DISPOSITIVO ELECTRONICO", "DISPOSITIVOELECTRONICO" -> {
                        String[] atributos_dispositivo = caracteristicasPropias.split(";");
                        String marca = atributos_dispositivo.length > 0 ? atributos_dispositivo[0] : "NO REGISTRADO";
                        String modelo = atributos_dispositivo.length > 1 ? atributos_dispositivo[1] : "NO REGISTRADO";
                        String numeroSerie = atributos_dispositivo.length > 2 ? atributos_dispositivo[2] : "NO REGISTRADO";
                        DispositivoElectronico dispositivo = new DispositivoElectronico(marca, modelo, numeroSerie,
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor));
                        objetos.add(dispositivo);
                    }
                    
                    case "OBJETO GENERICO", "OBJETOGENERICO" -> {
                        String[] atributos_ObjetoGenerico = caracteristicasPropias.split(";");
                        String detalle = atributos_ObjetoGenerico.length > 0 ? atributos_ObjetoGenerico[0] : "NO REGISTRADO";
                        ObjetoGenerico generico = new ObjetoGenerico(detalle, 
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor));
                        objetos.add(generico);
                    }
                        
                    default -> {
                        ObjetoGenerico generico = new ObjetoGenerico(caracteristicasPropias, 
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor));
                        objetos.add(generico);
                    }
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
        boolean eliminado = false;
        for (int i = 0; i < objetos.size(); i++) {
            if (objetos.get(i).getId() == id) {
                objetos.remove(i);
                eliminado = true;
                break;
            }
        }
        if (eliminado) {
            reescribir(objetos);
        }
    }
    
    @Override
    public void actualizarObjetoPrenda(int idBuscar, String nombreNuevo, String descripcionNueva, String categoriaNueva,
            String colorNuevo, String fechaNUeva, String ubicacionNueva, String estadoNuevo, Estudiante reportadoPor, String talla, String marca){
        List<ObjetoReportado> objetos = obtenerObjetos();
        boolean encontrado = false;
        for (ObjetoReportado objeto : objetos){
            if (objeto.getId() == idBuscar){
                Prenda prenda = (Prenda) objeto;
                prenda.setAtributosPrenda(talla, marca, nombreNuevo, descripcionNueva, categoriaNueva, colorNuevo, fechaNUeva, ubicacionNueva, estadoNuevo, reportadoPor);
                encontrado = true;
                break;
            }   
        }
        if (encontrado) {
            reescribir(objetos);
        }
    }
    
    @Override
    public void actualizarObjetoDocumento(int idBuscar, String nombreNuevo, String descripcionNueva, String categoriaNueva,
            String colorNuevo, String fechaNUeva, String ubicacionNueva, String estadoNuevo, Estudiante reportadoPor, String tipoDocumento, String nombreTitular){
        List<ObjetoReportado> objetos = obtenerObjetos();
        boolean encontrado = false;
        for (ObjetoReportado objeto : objetos){
            if (objeto.getId() == idBuscar){
                Documento documento = (Documento) objeto;
                documento.setAtributosDocumento(tipoDocumento, nombreTitular, nombreNuevo, descripcionNueva, categoriaNueva, colorNuevo,
                        fechaNUeva, ubicacionNueva, estadoNuevo, reportadoPor);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            reescribir(objetos);
        }    
    }
    
    @Override
    public void actualizarObjetoDispositivoElectronico(int idBuscar, String nombreNuevo, String descripcionNueva, String categoriaNueva,
            String colorNuevo, String fechaNUeva, String ubicacionNueva, String estadoNuevo, Estudiante reportadoPor, String marca, String modelo, String numeroSerie){
        List<ObjetoReportado> objetos = obtenerObjetos();
        boolean encontrado = false;
        for (ObjetoReportado objeto : objetos){
            if (objeto.getId() == idBuscar){
                DispositivoElectronico dispositivoElectronico = (DispositivoElectronico) objeto;
                dispositivoElectronico.setAtributosDispositivoElectronico(marca, modelo, numeroSerie, nombreNuevo, descripcionNueva, categoriaNueva, colorNuevo, fechaNUeva, ubicacionNueva, estadoNuevo, reportadoPor);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            reescribir(objetos); 
        }
    }
    
    @Override
    public void actualizarObjetoGenerico(int idBuscar, String nombreNuevo, String descripcionNueva, String categoriaNueva,
            String colorNuevo, String fechaNUeva, String ubicacionNueva, String estadoNuevo, Estudiante reportadoPor, String detalleExtra){
        List<ObjetoReportado> objetos = obtenerObjetos();
        boolean encontrado = false;
        for (ObjetoReportado objeto : objetos){
            if (objeto.getId() == idBuscar){
                ObjetoGenerico objetoGenerico = (ObjetoGenerico) objeto;
                objetoGenerico.setAtributosObjetoGenerico(detalleExtra, nombreNuevo, descripcionNueva, categoriaNueva, colorNuevo,
                        fechaNUeva, ubicacionNueva, estadoNuevo, reportadoPor);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            reescribir(objetos);
        }
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
            throw new RuntimeException("No se pudo escribir en el archivo: " + e.getMessage(), e);
        }
    }

}
