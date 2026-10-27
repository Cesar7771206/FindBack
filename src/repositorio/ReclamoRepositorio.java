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
}
