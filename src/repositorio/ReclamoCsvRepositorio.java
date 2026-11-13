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
    private List<Reclamo> reclamos;
    private int ultimoId;

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
        this.reclamos = new ArrayList<>();
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
        this.reclamos = cargarReclamos();
        int max = 0;
        for (Reclamo r : this.reclamos) {
            try {
                String numStr = r.getId().replaceAll("[^0-9]", "");
                if (!numStr.isEmpty()) {
                    int num = Integer.parseInt(numStr);
                    if (num > max) {
                        max = num;
                    }
                }
            } catch (NumberFormatException ignored) {}
        }
        this.ultimoId = max;
    }

    private List<Reclamo> cargarReclamos() {
        List<Reclamo> lista = new ArrayList<>();
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

                String id = atributos[0];

                int idObjeto;
                try {
                    idObjeto = Integer.parseInt(atributos[1]);
                } catch (NumberFormatException e) {
                    continue;
                }

                ObjetoReportado objeto = objetoRepo.obtenerObjetoPorId(idObjeto);
                if (objeto == null) {
                    continue;
                }

                String idReclamante = atributos[2];
                Usuario usuario = usuarioRepo.obtenerUsuarioPorIdentificacion(idReclamante);
                if (!(usuario instanceof Estudiante)) {
                    continue;
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
                lista.add(reclamo);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el CSV: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void guardar(Reclamo reclamo) {
        if (reclamo == null) {
            throw new IllegalArgumentException("El reclamo no puede ser nulo.");
        }
        if (reclamo.getId() == null || reclamo.getId().isBlank() || buscarPorId(reclamo.getId()) != null) {
            reclamo.setId(generarSiguienteId());
        } else {
            try {
                String numStr = reclamo.getId().replaceAll("[^0-9]", "");
                if (!numStr.isEmpty()) {
                    int num = Integer.parseInt(numStr);
                    if (num > this.ultimoId) {
                        this.ultimoId = num;
                    }
                }
            } catch (NumberFormatException ignored) {}
        }
        reclamos.add(reclamo);
        reescribir();
    }

    @Override
    public List<Reclamo> obtenerReclamos() {
        return new ArrayList<>(reclamos);
    }

    @Override
    public Reclamo buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        for (Reclamo r : reclamos) {
            if (r.getId().equalsIgnoreCase(id.trim())) {
                return r;
            }
        }
        return null;
    }

    @Override
    public void eliminar(String id) {
        if (id == null || id.isBlank()) {
            return;
        }
        boolean eliminado = false;
        for (int i = 0; i < reclamos.size(); i++) {
            if (reclamos.get(i).getId().equalsIgnoreCase(id.trim())) {
                reclamos.remove(i);
                eliminado = true;
                break;
            }
        }
        if (eliminado) {
            reescribir();
        }
    }

    @Override
    public void actualizar(String id, String estado, String observacion, String codigoRevisor) {
        if (id == null || id.isBlank()) {
            return;
        }
        for (Reclamo r : reclamos) {
            if (r.getId().equalsIgnoreCase(id.trim())) {
                if (estado != null && !estado.isBlank()) {
                    r.setEstado(estado);
                }
                r.setObservacion(observacion);
                r.setCodigoRevisor(codigoRevisor);
                reescribir();
                return;
            }
        }
    }

    @Override
    public void actualizar(Reclamo reclamoActualizado) {
        if (reclamoActualizado == null) {
            return;
        }
        for (int i = 0; i < reclamos.size(); i++) {
            if (reclamos.get(i).getId().equalsIgnoreCase(reclamoActualizado.getId())) {
                reclamos.set(i, reclamoActualizado);
                reescribir();
                return;
            }
        }
    }

    @Override
    public List<Reclamo> buscarPorReclamante(String identificacionReclamante) {
        if (identificacionReclamante == null || identificacionReclamante.isBlank()) {
            return new ArrayList<>();
        }
        List<Reclamo> resultado = new ArrayList<>();
        for (Reclamo r : reclamos) {
            if (r.getReclamante() != null && r.getReclamante().getIdentificacion().equalsIgnoreCase(identificacionReclamante.trim())) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    @Override
    public List<Reclamo> buscarPorObjeto(int idObjeto) {
        List<Reclamo> resultado = new ArrayList<>();
        for (Reclamo r : reclamos) {
            if (r.getObjeto() != null && r.getObjeto().getId() == idObjeto) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    @Override
    public String generarSiguienteId() {
        return "REC-" + String.format("%03d", ++this.ultimoId);
    }

    public void reescribir() {
        reescribir(this.reclamos);
    }

    public void reescribir(List<Reclamo> lista) {
        List<String> lineas = new ArrayList<>();
        lineas.add(CABECERA);
        if (lista != null) {
            for (Reclamo r : lista) {
                lineas.add(r.toTxt());
            }
        }
        try {
            Files.write(RUTA_ARCHIVO, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reescribiendo el CSV: " + e.getMessage(), e);
        }
    }

}
