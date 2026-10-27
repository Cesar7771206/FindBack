package repositorio;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;

public class ReclamoRepositorio {
    private static final String RUTA_ARCHIVO = "data/reclamos.txt";
    private static final String SIN_REPUESTAS = "SIN RESPUESTAS";
    private ArrayList<Reclamo> reclamos;
    private UsuarioRepositorio usarioRepo;
    private ObjetoRepositorio objetoRepo;
    
    public ReclamoRepositorio(UsuarioRepositorio usarioRepo, ObjetoRepositorio objetoRepo) {
        this.usarioRepo = usarioRepo;
        this.objetoRepo = objetoRepo;
        this.reclamos = new ArrayList<>();
        cargarDesdeTxt();
    }
    
    public void cargarDesdeTxt(){
        reclamos.clear();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            crearArchivoVacio(archivo);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
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
                    Estudiante reclamante = usarioRepo.buscarPorIdentificación(p[4]);
                    ObjetoReportado obejto = objetoRepo.buscarPorId(Integer.parseInt(p[5]));
                    reclamos.add(new Reclamo(p[0]; respuestas, p[2], p[3], reclamante, objeto));
                
                } catch (IllegalArgumentException e) {
                    
                    System.out.println("Reclamo ignorado (" + e.getMessage() + "): " + linea);
                }
            }
        }catch (IOException e) {
            System.out.println("Error al leer reclamos.txt: " + e.getMessage());
        }
    }
}
