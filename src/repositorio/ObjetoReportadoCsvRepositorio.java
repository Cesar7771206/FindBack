package repositorio;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import modelo.*;

public class ObjetoReportadoCsvRepositorio implements ObjetoReportadoRepositorio {

    private static final String CABECERA = "id|nombre|descripcion|categoria|color|fecha|ubicacion|estado|reportado|caracteristicasPropias";
    private final Path RUTA_ARCHIVO;

    private UsuarioRepositorio usuarioRepo;
    private List<ObjetoReportado> objetos;
    private int ultimoId;

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
                Files.write(RUTA_ARCHIVO, List.of(CABECERA), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el archivo", e);
        }
        this.objetos = cargarObjetos();
        int max = 0;
        for (ObjetoReportado obj : this.objetos) {
            if (obj.getId() > max) {
                max = obj.getId();
            }
        }
        this.ultimoId = max;
    }

    private List<ObjetoReportado> cargarObjetos() {
        List<ObjetoReportado> lista = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8);
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] atributos = linea.split("\\|", -1);
                if (atributos.length < 9) {
                    continue;
                }
                int id;
                try {
                    id = Integer.parseInt(atributos[0]);
                } catch (NumberFormatException e) {
                    continue;
                }
                String nombre = atributos[1];
                String descripcion = atributos[2];
                String categoria = atributos[3] != null ? atributos[3].trim().toUpperCase() : "";
                String color = atributos[4];
                String fecha = atributos[5];
                String ubicacion = atributos[6];
                String estado = atributos[7];
                String reportadoPor = atributos[8];
                String caracteristicasPropias = atributos.length > 9 ? atributos[9] : "";

                Usuario usuario = usuarioRepo.obtenerUsuarioPorIdentificacion(reportadoPor);
                if (!(usuario instanceof Estudiante)) {
                    continue;
                }
                Estudiante estudiante = (Estudiante) usuario;

                switch (categoria) {
                    case "DOCUMENTO" -> {
                        String[] atributos_documento = caracteristicasPropias.split(";");
                        String tipoDoc = atributos_documento.length > 0 && !atributos_documento[0].isBlank() ? atributos_documento[0] : "NO ESPECIFICADO";
                        String titular = atributos_documento.length > 1 && !atributos_documento[1].isBlank() ? atributos_documento[1] : "NO ESPECIFICADO";
                        Documento documento = new Documento(tipoDoc, titular,
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, estudiante);
                        lista.add(documento);
                    }
                    case "PRENDA" -> {
                        String[] atributos_prenda = caracteristicasPropias.split(";");
                        String talla = atributos_prenda.length > 0 && !atributos_prenda[0].isBlank() ? atributos_prenda[0] : "NO ESPECIFICADO";
                        String marca = atributos_prenda.length > 1 && !atributos_prenda[1].isBlank() ? atributos_prenda[1] : "NO ESPECIFICADO";
                        Prenda prenda = new Prenda(talla, marca, id, nombre, descripcion, categoria, color, fecha, ubicacion, estado,
                                estudiante);
                        lista.add(prenda);
                    }
                    
                    case "DISPOSITIVO ELECTRONICO", "DISPOSITIVOELECTRONICO" -> {
                        String[] atributos_dispositivo = caracteristicasPropias.split(";");
                        String marca = atributos_dispositivo.length > 0 && !atributos_dispositivo[0].isBlank() ? atributos_dispositivo[0] : "NO REGISTRADO";
                        String modelo = atributos_dispositivo.length > 1 && !atributos_dispositivo[1].isBlank() ? atributos_dispositivo[1] : "NO REGISTRADO";
                        String numeroSerie = atributos_dispositivo.length > 2 && !atributos_dispositivo[2].isBlank() ? atributos_dispositivo[2] : "NO REGISTRADO";
                        DispositivoElectronico dispositivo = new DispositivoElectronico(marca, modelo, numeroSerie,
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, estudiante);
                        lista.add(dispositivo);
                    }
                    
                    case "OBJETO GENERICO", "OBJETOGENERICO" -> {
                        String[] atributos_ObjetoGenerico = caracteristicasPropias.split(";");
                        String detalle = atributos_ObjetoGenerico.length > 0 && !atributos_ObjetoGenerico[0].isBlank() ? atributos_ObjetoGenerico[0] : "NO REGISTRADO";
                        ObjetoGenerico generico = new ObjetoGenerico(detalle, 
                                id, nombre, descripcion, categoria, color, fecha, ubicacion, estado, estudiante);
                        lista.add(generico);
                    }
                        
                    default -> {
                        ObjetoGenerico generico = new ObjetoGenerico(caracteristicasPropias, 
                                id, nombre, descripcion, "OBJETO GENERICO", color, fecha, ubicacion, estado, estudiante);
                        lista.add(generico);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo", e);
        }
        return lista;
    }

    @Override
    public void guardar(ObjetoReportado objeto) {
        if (objeto == null) {
            throw new IllegalArgumentException("El objeto no puede ser nulo.");
        }
        if (objeto.getId() <= 0 || obtenerObjetoPorId(objeto.getId()) != null) {
            objeto.setId(generarSiguienteId());
        } else if (objeto.getId() > this.ultimoId) {
            this.ultimoId = objeto.getId();
        }
        objetos.add(objeto);
        reescribir();
    }

    @Override
    public List<ObjetoReportado> obtenerObjetos() {
        return objetos;
    }

    @Override
    public ObjetoReportado obtenerObjetoPorId(int id) {
        for (ObjetoReportado o : objetos) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    @Override
    public void actualizar(ObjetoReportado objetoActualizado) {
        if (objetoActualizado == null) {
            return;
        }
        for (int i = 0; i < objetos.size(); i++) {
            if (objetos.get(i).getId() == objetoActualizado.getId()) {
                objetos.set(i, objetoActualizado);
                reescribir();
                return;
            }
        }
    }

    @Override
    public void eliminarObjeto(int id) {
        boolean eliminado = false;
        for (int i = 0; i < objetos.size(); i++) {
            if (objetos.get(i).getId() == id) {
                objetos.remove(i);
                eliminado = true;
                break;
            }
        }
        if (eliminado) {
            reescribir();
        }
    }

    @Override
    public int generarSiguienteId() {
        return ++this.ultimoId;
    }

    public void reescribir() {
        reescribir(this.objetos);
    }


    public void reescribir(List<ObjetoReportado> lista_nueva) {
        List<String> lineas = new ArrayList<>();
        lineas.add(CABECERA);
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
