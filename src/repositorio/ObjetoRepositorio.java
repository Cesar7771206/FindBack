
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
public class ObjetoRepositorio implements ObjetosInterfaz{
    
    private final Path RUTA_ARCHIVO;
    
    private UsuarioRepositorio usuarioRepo;

    public ObjetoRepositorio() {
        this.RUTA_ARCHIVO = Paths.get("data/objetos.csv");
        cargarDesdeTxt();
    }
    
    
    private void cargarDesdeTxt(){
        try{
            if(RUTA_ARCHIVO.getParent()!=null){
                Files.createDirectories(RUTA_ARCHIVO.getParent());
            }
            if (Files.notExists(RUTA_ARCHIVO)|| Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8).isEmpty()){
                Files.write(RUTA_ARCHIVO, List.of("id|nombre|descripcion|categoria|color|fecha|ubicacion|estado"), StandardCharsets.UTF_8);
            }
        }catch(IOException e){
            throw new RuntimeException("No se pudo crear el archivo", e);
        }
    }
    @Override
    public void guardar(ObjetoReportado objeto) {
        String linea= objeto.getId() + "|" + objeto.getNombre()+ "|" + objeto.getDescripcion()
                + "|" + objeto.getCategoria()+ "|" + objeto.getColor() + "|" + objeto.getFecha() + "|" + objeto.getUbicacion() + "|" + objeto.getEstado();
        try{
            Files.write(RUTA_ARCHIVO, List.of(linea), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e){
            throw new RuntimeException("No se pudo leer el archivo", e);
        }

    }
    
    @Override
    public List<ObjetoReportado> obtenerObjetos(){
        List<ObjetoReportado> objetos = new ArrayList<>();
        try{
            List<String> lineas = Files.readAllLines(RUTA_ARCHIVO, StandardCharsets.UTF_8 );
            for(String linea : lineas){
                String[] atributos = linea.split("\\|");
                ObjetoReportado objeto = new ObjetoReportado(Integer.parseInt(atributos[0]), atributos[1], atributos[2],
                        atributos[3], atributos[4], atributos[5], atributos[6], atributos[7]);   
            }
        }catch(IOException e){
            throw new RuntimeException("No se pudo leer el archivo", e);
        }
        return objetos;
    }
    
    @Override 
    
    public ObjetoReportado obtenerObjetoPorId(int id){
        List<ObjetoReportado> objetos = obtenerObjetos();
        for(ObjetoReportado O : objetos){
            if (O.getId()==id){
                return O;
            }
        }
        return null;
    }
    
    @Override
    
    public void eliminarObjeto (int id){
        List<ObjetoReportado> objetos = obtenerObjetos();
        for(int i=0; i<objetos.size();i++){
            if (objetos.get(i).getId()==id){
                objetos.remove(i);
                break;
            }
        }
        reescribir(objetos);
    }
    
    public void reescribir(List <ObjetoReportado> lista_nueva){
        List<String> lineas = new ArrayList<>();
        lineas.add("id|nombre|descripcion|categoria|color|fecha|ubicacion|estado");
        for(ObjetoReportado objeto : lista_nueva){
            String linea = objeto.getId() + "|" + objeto.getNombre()+ "|" + objeto.getDescripcion()
                + "|" + objeto.getCategoria()+ "|" + objeto.getColor() + "|" + objeto.getFecha() + "|" + 
                    objeto.getUbicacion() + "|" + objeto.getEstado();
            lineas.add(linea);
        }
        try{
            Files.write(RUTA_ARCHIVO, lineas, StandardCharsets.UTF_8);
        }catch(IOException e){
            throw new RuntimeException("No se pudo escribir en el archivo"+ e);
        }
    }

}
