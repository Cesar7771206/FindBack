package repositorio;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;

public class ReclamoRepositorio {
    private static final String RUTA_ARCHIVO = "data/reclamos.csv";
    private static final String CABECERA = "id, respuestas, estado, observacion, reclamante, objeto";
    private static final int NUM_COLUMNAS = 6;
    private static final String SIN_RESPUESTAS = "SIN RESPUESTAS";
    private ArrayList<Reclamo> reclamos;
    private UsuarioRepositorio usarioRepo;
    private ObjetoRepositorio objetoRepo;
    
    public ReclamoRepositorio(UsuarioRepositorio usarioRepo, ObjetoRepositorio objetoRepo) {
        this.usarioRepo = usarioRepo;
        this.objetoRepo = objetoRepo;
        this.reclamos = new ArrayList<>();
        cargarDesdeCsv();
    }
    
    public void cargarDesdeCsv() {
        reclamos.clear();
        File archivo = new File(RUTA_ARCHIVO);
        if(!archivo.exists()) {
            crearArchivoVacio(archivo);
            return;
        }
        try {
            Scanner sc = new Scanner(archivo, StandardCharsets.UTF_8);
            boolean primera = true;
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if(linea.trim().isEmpty()) continue;
                if (primera) {
                    primera = false;
                    if (linea.startsWith("id,") || linea.startsWith("\uFEFFid,")) continue;
                }
                
                String [] p = parsearLineaCsv(linea);
                if (p.length < 6) continue;
                
                try {
                    ArrayList<String> respuestas = new ArrayList<>();
                    if (!p[1].equals(SIN_RESPUESTAS)) {
                        respuestas.addAll(Arrays.asList(p[1].split("\\|")));
                    }
                    Estudiante reclamante = usarioRepo.buscarPorIdentificacion(p[4]);
                    ObjetoReportado objeto = objetoRepo.buscarPorId(Integer.parseInt(p[5].trim()));
                    reclamos.add(new Reclamo(p[0], respuestas, p[2], p[3], reclamante, objeto));
                } catch (IllegalArgumentException e) {
                    System.out.println("REclamo ignorado(" + e.getMessage() + "): " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer reclamos.csv: " + e.getMessage());
        }
    }
    
    public void guardarEnCsv() {
        File archivo = new File(RUTA_ARCHIVO);
        crearArchivoVacio(archivo);
        try {
            PrintWriter pw = new PrintWriter(archivo, StandardCharsets.UTF_8);
            pw.println(CABECERA);
            for (Reclamo r: reclamos) {
                pw.println(armarLineaCsv(r.toCsvCampos()));
            }
        } catch (IOException e) {
            System.out.println("Error al guardar en reclamos.csv: " + e.getMessage());
        }
    }
    
    
    public void guardar(Reclamo reclamo) {
        reclamos.add(reclamo);
        guardarEnCsv();
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
    
    private String escaparCampo(String campo) {
        if (campo == null) return "";
        campo = campo.replace("\r", " ").replace("\n", " ");
        if (campo.contains(",") || campo.contains("\"")){
            return "\"" + campo.replace("\"", "\"\"") + "\"";
        }
        return campo;
    }
    
    private String armarLineaCsv(String[] campos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(escaparCampo(campos[i]));
        }
        return sb.toString();
    }
    
    private String[] parsearLineaCsv(String linea) {
        ArrayList<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean entreComillas = false;
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (entreComillas) {
                if (c == '"') {
                    if (i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                        actual.append('"');
                        i++;
                    } else{
                        entreComillas = false;
                    }
                } else {
                    actual.append(c);
                }
            } else if (c == '"') {
                entreComillas = true;
            } else if (c == ',') {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        campos.add(actual.toString());
        return campos.toArray(new String[0]);
    }
}
