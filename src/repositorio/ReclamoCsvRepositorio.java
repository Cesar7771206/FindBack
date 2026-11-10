package repositorio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;
import modelo.Usuario;

public class ReclamoCsvRepositorio implements ReclamoRepositorio {

    private static final String CABECERA = "id|idObjeto|codigoReclamante|respuestas|estado|observacion|codigoRevisor";
    private final Path RUTA_ARCHIVO;

    private final UsuarioRepositorio usuarioRepo;
    private final ObjetoReportadoRepositorio objetoRepo;

    public ReclamoCsvRepositorio() {
        this(Paths.get("data/reclamos.csv"), new UsuarioCsvRepositorio(), new ObjetoReportadoCsvRepositorio());
    }

    public ReclamoCsvRepositorio(UsuarioRepositorio usuarioRepo, ObjetoReportadoRepositorio objetoRepo) {
        this(Paths.get("data/reclamos.csv"), usuarioRepo, objetoRepo);
    }

    public ReclamoCsvRepositorio(Path rutaArchivo, UsuarioRepositorio usuarioRepo, ObjetoReportadoRepositorio objetoRepo) {
        this.RUTA_ARCHIVO = rutaArchivo;
        this.usuarioRepo = usuarioRepo != null ? usuarioRepo : new UsuarioCsvRepositorio();
        this.objetoRepo = objetoRepo != null ? objetoRepo : new ObjetoReportadoCsvRepositorio();
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
        if (reclamo == null) {
            throw new IllegalArgumentException("El reclamo no puede ser nulo.");
        }
        try {
            Files.write(RUTA_ARCHIVO, List.of(reclamo.toTxt()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo en el CSV: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reclamo> obtenerReclamos() {
        List<Reclamo> reclamos = new ArrayList<>();
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

                String id = atributos[0];

                int idObjeto;
                try {
                    idObjeto = Integer.parseInt(atributos[1]);
                } catch (NumberFormatException e) {
                    continue;
                }

                ObjetoReportado objeto = objetoRepo.obtenerObjetoPorId(idObjeto);
                if (objeto == null) {
                    continue; // Objeto no encontrado o eliminado
                }

                String idReclamante = atributos[2];
                Usuario usuario = usuarioRepo.obtenerUsuarioPorIdentificacion(idReclamante);
                if (!(usuario instanceof Estudiante)) {
                    continue; // Reclamante no encontrado o no es estudiante
                }
                Estudiante reclamante = (Estudiante) usuario;

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
            throw new RuntimeException("Error leyendo el CSV: " + e.getMessage(), e);
        }
        return reclamos;
    }

    @Override
    public Reclamo buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        List<Reclamo> reclamos = obtenerReclamos();
        for (Reclamo r : reclamos) {
            if (r.getId().equalsIgnoreCase(id.trim())) {
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
            throw new RuntimeException("Error reescribiendo el CSV: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(String id) {
        if (id == null || id.isBlank()) {
            return;
        }
        List<Reclamo> reclamos = obtenerReclamos();
        boolean eliminado = false;
        for (int i = 0; i < reclamos.size(); i++) {
            if (reclamos.get(i).getId().equalsIgnoreCase(id.trim())) {
                reclamos.remove(i);
                eliminado = true;
                break;
            }
        }
        if (eliminado) {
            reescribir(reclamos);
        }
    }

    @Override
    public void actualizar(String id, String estado, String observacion, String codigoRevisor) {
        if (id == null || id.isBlank()) {
            return;
        }
        List<Reclamo> reclamos = obtenerReclamos();
        boolean actualizado = false;
        for (Reclamo r : reclamos) {
            if (r.getId().equalsIgnoreCase(id.trim())) {
                r.setEstado(estado);
                r.setObservacion(observacion);
                r.setCodigoRevisor(codigoRevisor);
                actualizado = true;
                break;
            }
        }
        if (actualizado) {
            reescribir(reclamos);
        }
    }

    @Override
    public List<Reclamo> buscarPorReclamante(String identificacionReclamante) {
        if (identificacionReclamante == null || identificacionReclamante.isBlank()) {
            return new ArrayList<>();
        }
        List<Reclamo> resultado = new ArrayList<>();
        for (Reclamo r : obtenerReclamos()) {
            if (r.getReclamante() != null && r.getReclamante().getIdentificacion().equalsIgnoreCase(identificacionReclamante.trim())) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    @Override
    public List<Reclamo> buscarPorObjeto(int idObjeto) {
        List<Reclamo> resultado = new ArrayList<>();
        for (Reclamo r : obtenerReclamos()) {
            if (r.getObjeto() != null && r.getObjeto().getId() == idObjeto) {
                resultado.add(r);
            }
        }
        return resultado;
    }

}
