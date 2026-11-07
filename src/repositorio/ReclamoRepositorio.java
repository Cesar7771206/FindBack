package repositorio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;

public class ReclamoRepositorio {
    private static final String CABECERA = "id|idObjeto|codigoReclamante|respuestas|estado|observacion|codigoRevisor";
    private final Path archivo;
    private final UsuarioRepositorio usuarioRepo;
    private final ObjetoRepositorio objetoRepo;
    
    public ReclamoRepositorio(UsuarioRepositorio usuarioRepo, ObjetoRepositorio objetoRepo) {
        this.usuarioRepo = usuarioRepo;
        this.objetoRepo = objetoRepo;
        this.archivo = Paths.get("data/reclamos.csv");
        inicializar();
    }
    
    private void inicializar() {
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            if (Files.notExists(archivo)) {
                Files.write(archivo,
                        List.of(CABECERA),
                        StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el CSV", e);
        }
    }
    
    public List<Reclamo> listar(){
        List<Reclamo> lista = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i).trim();
                if (linea.isEmpty()) continue;
                String[] c = linea.split("\\|", -1);
                ArrayList<String> respuestas = new ArrayList<>();
                if (!c[3].isEmpty()) {
                    respuestas.addAll(Arrays.asList(c[3].split(";")));
                }
                Estudiante reclamante = usuarioRepo.buscarPorCodigo(c[2]);
                ObjetoReportado objeto = objetoRepo.buscarPorId(Integer.parseInt(c[1].replace("OBJ-", "")));
                lista.add(new Reclamo(c[0], respuestas, c[4], c[5], c[6], reclamante, objeto));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el Csv", e);
        }
        return lista;
    }
    
    public void guardar(Reclamo r) {
        try {
            Files.write(archivo,
                    List.of(armarLinea(r)),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo en el CSV", e);
        }
    }
    
    public Optional<Reclamo> buscarPorId(String id) {
        return listar().stream().filter(r -> r.getId().equals(id)).findFirst();
    }
    
    public void actualizar (String id, String estado, String observacion, String codigoRevisor) {
        List<Reclamo> reclamos = listar();
        for (Reclamo r : reclamos) {
            if (r.getId().equals(id)){
                r.setEstado(estado);
                r.setObservacion(observacion);
                r.setCodigoRevisor(codigoRevisor);
                reescribir(reclamos);
                return;
            }
        }
    }
    
    public void eliminar(String id) {
        List<Reclamo> restantes = listar();
        restantes.removeIf(r -> r.getId().equals(id));
        reescribir(restantes);
    }
    
    private void reescribir(List<Reclamo> reclamos) {
        List<String> lineas = new ArrayList<>();
        lineas.add(CABECERA);
        for (Reclamo r : reclamos) {
            lineas.add(armarLinea(r));
        }
        try {
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reescribiendo el CSV", e);
        }
    }
    
    private String armarLinea(Reclamo r) {
        String idObjeto = "OBJ-" + String.format("%04d", r.getObjeto().getId());
        return r.getId() + "|" + idObjeto + "|" + r.getReclamante().getIdentificacion() + "|"
                + String.join(";", r.getRespuestas()) + "|" + r.getEstado() + "|"
                + r.getObservacion() + "|" + r.getCodigoRevisor();
                
    }
}
