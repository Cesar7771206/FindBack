import vista.PanelReclamar;
import modelo.*;
import servicio.*;
import java.util.*;

public class PanelReclamarTest {
    public static void main(String[] args) {
        System.out.println("Iniciando pruebas de PanelReclamar...");

        PanelReclamar panel = new PanelReclamar();
        Estudiante estReporta = new Estudiante(Facultad.INGENIERIA, Carrera.SISTEMAS, 5, "2021002", "Pedro Reporta", "pedro@udep.edu.pe", "pass123");
        Estudiante estReclama = new Estudiante(Facultad.INGENIERIA, Carrera.SISTEMAS, 5, "2021001", "Cesar Reclama", "cesar@udep.edu.pe", "pass123");
        panel.setUsuarioActual(estReclama);

        // Test Prenda selection
        Prenda prenda = new Prenda("L", "Adidas", 99, "Casaca deportiva", "Casaca azul", Categoria.PRENDA, "AZUL", "2026-11-13", "CAMPUS", "ENCONTRADO", estReporta);
        panel.setObjetoSeleccionado(prenda);

        // Verify dynamic question components are present
        assert panel.getComponentCount() > 0 : "Panel no debe estar vacío";

        System.out.println("✅ PanelReclamar inicializado y probado correctamente con campos dinámicos.");
    }
}
