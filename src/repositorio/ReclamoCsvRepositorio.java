package repositorio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;

public class ReclamoCsvRepositorio implements ReclamoRepositorio {

    private static final String CABECERA = "id|idObjeto|codigoReclamante|respuestas|estado|observacion|codigoRevisor";
    private final Path RUTA_ARCHIVO;

    private final UsuarioCsvRepositorio usuarioRepo;
    private final ObjetoReportadoCsvRepositorio objetoRepo;

    public ReclamoCsvRepositorio() {
        this.usuarioRepo = new UsuarioCsvRepositorio();
        this.objetoRepo = new ObjetoReportadoCsvRepositorio();
        this.RUTA_ARCHIVO = Paths.get("data/reclamos.csv");
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
            throw new RuntimeException("No se pudo crear el CSV", e);
        }
    }

    @Override
    public void guardar(Reclamo reclamo) {
        try {
            Files.write(RUTA_ARCHIVO, List.of(reclamo.toTxt()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo en el CSV", e);
        }
    }

    @Override
    public List<Reclamo> obtenerReclamos() {
        List<Reclamo> reclamos = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                if (linea.isEmpty()) {
                    continue;
                }
                String[] atributos = linea.split("\\|", -1);

                String id = atributos[0];

                int idObjeto = Integer.parseInt(atributos[1]);
                ObjetoReportado objeto = objetoRepo.obtenerObjetoPorId(idObjeto);

                String idReclamante = atributos[2];
                Estudiante reclamante = (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion(idReclamante);

                ArrayList<String> respuestas = new ArrayList<>();
                if (!atributos[3].isEmpty()) {
                    respuestas.addAll(Arrays.asList(atributos[3].split(";")));
                }

                String estado = atributos[4];
                String observacion = atributos[5];
                String codigoRevisor = atributos[6];

                Reclamo reclamo = new Reclamo(id, objeto, reclamante, respuestas, estado, observacion, codigoRevisor);
                reclamos.add(reclamo);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el Csv", e);
        }
        return reclamos;
    }

    @Override
    public Reclamo buscarPorId(String id) {
        List<Reclamo> reclamos = obtenerReclamos();
        for (Reclamo r : reclamos) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    private void reescribir(List<Reclamo> reclamos) {
        List<String> lineas = new ArrayList<>();
        lineas.add(CABECERA);
        for (Reclamo r : reclamos) {
            lineas.add(r.toTxt());
        }
        try {
            Files.write(RUTA_ARCHIVO, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reescribiendo el CSV", e);
        }
    }

    @Override
    public void eliminar(String id) {
        List<Reclamo> reclamos = obtenerReclamos();
        for (int i = 0; i < reclamos.size(); i++) {
            if(reclamos.get(i).getId().equalsIgnoreCase(id)){
                reclamos.remove(i);
                break;
            }
        }
        reescribir(reclamos);
    }
    
    @Override
    public void actualizar(String id, String estado, String observacion, String codigoRevisor) {
        List<Reclamo> reclamos = obtenerReclamos();
        for (Reclamo r : reclamos) {
            if (r.getId().equalsIgnoreCase(id)) {
                r.setEstado(estado);
                r.setObservacion(observacion);
                r.setCodigoRevisor(codigoRevisor);
                reescribir(reclamos);
                return;
            }
        }
    }

}
