package repositorio;

import java.util.List;
import modelo.*;

public interface ObjetoReportadoRepositorio {

    void guardar(ObjetoReportado objeto);

    List<ObjetoReportado> obtenerObjetos();

    ObjetoReportado obtenerObjetoPorId(int id);

    void actualizar(ObjetoReportado objeto);

    void eliminarObjeto(int id);

    int generarSiguienteId();

}
