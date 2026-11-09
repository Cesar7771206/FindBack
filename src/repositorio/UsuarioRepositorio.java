package repositorio;

//ahora se usa java.nio (Files, Path, Paths) en lugar de BufferedReader/PrintWriter, igual que en el ejemplo.
import java.io.IOException;//excepcion q se puede producir cuando falla una operacion de entrada o salida (leer o escribir el archivo).
import java.nio.charset.StandardCharsets;//para leer y escribir el archivo en UTF-8 (tildes y ñ se ven bien).
import java.nio.file.Files;//trae los metodos para leer, escribir y crear archivos/carpetas.
import java.nio.file.Path;//representa la ruta del archivo.
import java.nio.file.Paths;//convierte un String en un Path.
import java.util.ArrayList;//se crea porq va a almacenar objetos Estudiante.
import java.util.Arrays; //convierte un arreglo de mensajes en una lista. lo usa con Arrays.asList()
import java.util.List;//Files.readAllLines devuelve una List<String>.
import modelo.Estudiante; //lo exporta d la clase estudiante q se encuentra en el pakete modelo.

public class UsuarioRepositorio {
    private static final String RUTA_ARCHIVO = "data/estudiantes.csv"; //Rutaarchivo nombre de la constante que guarda la ruta del archivo. data/estudiantes.txt se interpreta respeco al directorio del trabajo desde el que se ejecuta el programa.
    private ArrayList<Estudiante> estudiantes; //declara una lista destinada a almacenar estudiantes.
    
    public UsuarioRepositorio() {
        this.estudiantes = new ArrayList<>();
        cargarDesdeTxt(); 
    }
    
    public void cargarDesdeTxt() {
        estudiantes.clear();
        Path archivo = Paths.get(RUTA_ARCHIVO);
        
        if (Files.notExists(archivo)) {
            crearArchivoVacio(archivo);
            return;
        }
        
        try {
            //aquí eeee Files.readAllLines abre el csv, 
            //lee todas las lineas, las guarda en una 
            //lista y cierra el archivo solo.
                List<String> lineas = Files.readAllLines(
                        archivo,StandardCharsets.UTF_8);
                for (String linea : lineas) {
                    if(linea.trim().isEmpty()) {
                    continue;
                }
                
                String[] partes = linea.split("\\|", -1);
                //ej. si el txt tiene 3 lineas, readLine() devuelve linea 1>2>3>null.cicloterminaconull.
                if (partes.length >= 7){
                    String identificacion = partes[0];
                    String nombre = partes [1];
                    String correo = partes [2];
                    String contrasena = partes[3];
                    String facultad = partes [4];
                    String carrera = partes [5];
                    int ciclo = Integer.parseInt(partes[6]);
                    
                    Estudiante est = new Estudiante(facultad, carrera, ciclo, identificacion, nombre, correo, contrasena);
                    if (partes.length >= 8 &&!partes[7].equals("SIN_MENSAJES")){
                        String[] msgs = partes[7].split(";");
                        est.setMensajes(new ArrayList<>(Arrays.asList(msgs)));
                    }
                    estudiantes.add(est);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al leer estudiantes.csv: "+ e.getMessage());
        }
    }
    
    public void guardarEnTxt() {
        Path archivo = Paths.get(RUTA_ARCHIVO);
        crearArchivoVacio(archivo);
        
        List<String> lineas = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            lineas.add(e.toTxt()); ////toTxt ya arma
            /////la linea con "|" y ";" > esto eran para mensajes
            /////(sirve igual para el csv).
        }
        try {
            //sin APPEND: ya q borra lo anterior y escribe
            //la lista completa al igual q cuando se usaba el filewriter
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error al guardar en estudiantes.csv: " + e.getMessage());
        }
    }
    
    public void guardar (Estudiante estudiante) {
        estudiantes.add(estudiante);
        guardarEnTxt();
    }
    public Estudiante buscarPorCorreo(String correo) {
        for (Estudiante e : estudiantes) {
            if (e.getCorreo().equalsIgnoreCase(correo)) {
                return e;
            }
        }
        return null;
    }
    
    public ArrayList<Estudiante> listarTodos() {
        return estudiantes;
        
    }
    
    private void crearArchivoVacio(Path archivo) {
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent()); //crea la carpeta data si no existe.
            }
            if (Files.notExists(archivo)) {
                Files.createFile(archivo);
            }
        } catch (IOException e) {
            System.out.println("No se pudo crear el archivo: " + archivo.toString());
        }
    } 
}
