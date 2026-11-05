
package repositorio;


import java.nio.file.Path;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    
    
    public void cargarDesdeTxt(){
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
    

}
