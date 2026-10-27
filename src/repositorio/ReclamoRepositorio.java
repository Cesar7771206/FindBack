package repositorio;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;

public class ReclamoRepositorio {
    private static final String RUTA_ARCHIVO = "data/reclamos.txt";
    private static final String SIN_RESPUESTAS = "SIN RESPUESTAS";
    private ArrayList<Reclamo> reclamos;
    private UsuarioRepositorio usarioRepo;
    private ObjetoRepositorio objetoRepo;
    
    public ReclamoRepositorio(UsuarioRepositorio usarioRepo, ObjetoRepositorio objetoRepo) {
        this.usarioRepo = usarioRepo;
        this.objetoRepo = objetoRepo;
        this.reclamos = new ArrayList<>();
        cargarDesdeTxt();
    }
    
    public void cargarDesdeTxt() {
        reclamos.clear();
        File archivo = new File(RUTA_ARCHIVO);
        if(!archivo.exists()) {
            crearArchivoVacio(archivo);
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                
                String[] p = linea.split("\\|", -1);
                if (p.length < 6) continue;
                try {
                    ArrayList<String> respuestas = new ArrayList<>();
                    if (!p[1].equals(SIN_RESPUESTAS)) {
                        respuestas.addAll(Arrays.asList(p[1].split(";")));
                    }
                    Estudiante reclamante = usarioRepo.buscarPorIdentificacion(p[4]);
                    ObjetoReportado objeto = objetoRepo.buscarPorId(Integer.parseInt(p[5]));
                    reclamos.add(new Reclamo(p[0], respuestas, p[2], p[3], reclamante, objeto));
                } catch (IllegalArgumentException e) {
                    System.out.println("Reclamo ignorado(" + e.getMessage() + "): " + linea);
                }
            }
            br.close();
        }catch (IOException e) {
            System.out.println("Error al leer reclamos.txt: " + e.getMessage());
        }
    }
    
    public void guardarEnTxt() {
        File archivo = new File(RUTA_ARCHIVO);
        crearArchivoVacio(archivo);
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, false))) {
            for (Reclamo r : reclamos) {
                pw.println(r.toTxt());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar en reclamos.txt: " + e.getMessage());
        }
    }
    
    public void guardar(Reclamo reclamo) {
        reclamos.add(reclamo);
        guardarEnTxt();
    }
    
    public ArrayList<Reclamo> listarPorEstudiante(String identificacionEstudiante) {
        ArrayList<Reclamo> resultado = new ArrayList<>();
        for (Reclamo r : reclamos) {
            if (r.getReclamante().getIdentificacion().equalsIgnoreCase(identificacionEstudiante)){
                resultado.add(r);
            }
        }
        return resultado;
    }
    
    public ArrayList<Reclamo> listarTodos() {
        return reclamos;
    }
    
    private void crearArchivoVacio(File archivo) {
        try {
            if (archivo.getParentFile() != null) {
                archivo.getParentFile().mkdirs();
            }
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("No se pudo crear el archivo: " + archivo.getPath());
        }
    }
}
