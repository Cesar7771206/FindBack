/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;
import servicio.CoincidenciaServicio;
import servicio.ObjetoServicio;
import servicio.ReclamoServicio;

/**
 * Panel de Avisos con diseño minimalista para NetBeans GUI Builder.
 * Genera tarjetas de avisos dinámicamente con un bucle for dentro de un panel con scroll.
 * 
 * @author cesar
 */
public class PanelAvisos extends javax.swing.JPanel {

    private Estudiante usuarioActual;
    private ObjetoServicio objetoServicio;
    private CoincidenciaServicio coincidenciaServicio;
    private ReclamoServicio reclamoServicio;

    /**
     * Creates new form PanelAvisos
     */
    public PanelAvisos() {
        initComponents();
        if (jScrollPane1 != null && jScrollPane1.getVerticalScrollBar() != null) {
            jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        }
        this.objetoServicio = new ObjetoServicio();
        this.coincidenciaServicio = new CoincidenciaServicio();
        this.reclamoServicio = new ReclamoServicio();
        cargarAvisos();
    }

    public PanelAvisos(Estudiante estudiante) {
        this();
        setUsuarioActual(estudiante);
    }

    public void setUsuarioActual(Estudiante estudiante) {
        this.usuarioActual = estudiante;
        cargarAvisos();
    }

    public Estudiante getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Recarga dinámicamente los avisos mediante un for que genera subpaneles
     * usando los datos reales de la arquitectura (Modelo / Repositorio / Servicio).
     */
    public void cargarAvisos() {
        if (panelContenedor == null) {
            return;
        }

        panelContenedor.removeAll();

        List<String[]> listaParaMostrar = new ArrayList<>();

        // Si hay un usuario en sesión, consultamos sus datos reales en los servicios
        if (usuarioActual != null) {
            String miId = usuarioActual.getIdentificacion();

            // 1. Coincidencias y reportes de objetos del usuario
            try {
                if (objetoServicio != null && coincidenciaServicio != null) {
                    List<ObjetoReportado> misObjetos = objetoServicio.listarPorEstudiante(miId);
                    for (ObjetoReportado obj : misObjetos) {
                        // Aviso de objeto reportado exitosamente
                        String fechaObj = (obj.getFecha() != null && !obj.getFecha().isBlank()) ? obj.getFecha() : "Reciente";
                        listaParaMostrar.add(new String[]{
                            "Objeto Notificado",
                            "Tu reporte de '" + obj.getNombre() + "' (" + obj.getCategoria() + ") se encuentra registrado.",
                            fechaObj
                        });

                        // Buscar coincidencias detectadas con objetos reportados en el campus
                        List<ObjetoReportado> matches = coincidenciaServicio.buscarCoincidencias(obj);
                        for (ObjetoReportado match : matches) {
                            String fechaMatch = (match.getFecha() != null && !match.getFecha().isBlank()) ? match.getFecha() : "Reciente";
                            String desc = "Se detectó coincidencia con: " + match.getNombre() + " (" + match.getCategoria() + ") en " + match.getUbicacion() + ".";
                            listaParaMostrar.add(new String[]{
                                "¡Coincidencia Detectada!",
                                desc,
                                fechaMatch
                            });
                        }
                    }
                }
            } catch (Exception ignored) {
            }

            // 2. Estado de reclamos realizados por el estudiante
            try {
                if (reclamoServicio != null) {
                    List<Reclamo> misReclamos = reclamoServicio.listarPorReclamante(miId);
                    for (Reclamo r : misReclamos) {
                        String nomObj = (r.getObjeto() != null) ? r.getObjeto().getNombre() : "Objeto";
                        String estado = (r.getEstado() != null) ? r.getEstado() : "PENDIENTE";
                        String desc = "Tu reclamo " + r.getId() + " para '" + nomObj + "' está en estado: " + estado + ".";
                        listaParaMostrar.add(new String[]{"Estado de Reclamo", desc, "Reciente"});
                    }
                }
            } catch (Exception ignored) {
            }

            // 3. Notificaciones del buzón del estudiante (mensajes recibidos de ReclamoServicio / Notificable)
            ArrayList<String> mensajes = usuarioActual.getMensajes();
            if (mensajes != null && !mensajes.isEmpty()) {
                for (int i = mensajes.size() - 1; i >= 0; i--) {
                    String msg = mensajes.get(i);
                    if (msg != null && !msg.isBlank() && !msg.equalsIgnoreCase("SIN_MENSAJES")) {
                        String titulo = "Aviso del Sistema";
                        if (msg.contains("APROBADO") || msg.contains("RECHAZADO") || msg.contains("reclamo")) {
                            titulo = "Estado de Reclamo Actualizado";
                        } else if (msg.contains("entregado") || msg.contains("devuelto")) {
                            titulo = "Objeto Entregado";
                        }
                        listaParaMostrar.add(new String[]{titulo, msg, "Reciente"});
                    }
                }
            }
        }

        // Bucle for que genera dinámicamente los subpaneles dentro del contenedor con scroll
        if (listaParaMostrar.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay avisos pendientes por el momento.");
            lblVacio.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblVacio.setForeground(new Color(153, 153, 153));
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenedor.add(Box.createVerticalStrut(40));
            panelContenedor.add(lblVacio);
        } else {
            for (String[] aviso : listaParaMostrar) {
                JPanel tarjeta = crearSubpanelAviso(aviso[0], aviso[1], aviso[2]);
                panelContenedor.add(tarjeta);
                panelContenedor.add(Box.createVerticalStrut(12));
            }
        }

        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    /**
     * Crea un subpanel minimalista con componentes básicos estándar de Swing (sin HTML ni iconos).
     */
    private JPanel crearSubpanelAviso(String titulo, String descripcion, String tiempo) {
        JPanel subpanel = new JPanel();
        subpanel.setBackground(Color.WHITE);
        subpanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.Y_AXIS));
        subpanel.setMaximumSize(new Dimension(32767, 80));
        subpanel.setPreferredSize(new Dimension(440, 80));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTit.setForeground(new Color(51, 51, 51));
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(102, 102, 102));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHora = new JLabel(tiempo);
        lblHora.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblHora.setForeground(new Color(153, 153, 153));
        lblHora.setAlignmentX(Component.LEFT_ALIGNMENT);

        subpanel.add(lblTit);
        subpanel.add(Box.createVerticalStrut(4));
        subpanel.add(lblDesc);
        subpanel.add(Box.createVerticalStrut(4));
        subpanel.add(lblHora);

        return subpanel;
    }

    /**
     * Permite agregar un aviso dinámico en caliente a la lista.
     */
    public void agregarAviso(String titulo, String descripcion, String tiempo) {
        if (panelContenedor == null) {
            return;
        }
        panelContenedor.add(crearSubpanelAviso(titulo, descripcion, tiempo), 0);
        panelContenedor.add(Box.createVerticalStrut(12), 1);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    /**
     * Método main para poder ejecutar y probar este panel directamente desde NetBeans (Shift + F6).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FindBack - Avisos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 750);
            frame.setLocationRelativeTo(null);

            PanelAvisos panel = new PanelAvisos();
            frame.add(panel);
            frame.setVisible(true);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelSuperior = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelContenedor = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 750));
        setLayout(new java.awt.BorderLayout());

        panelSuperior.setBackground(new java.awt.Color(255, 255, 255));
        panelSuperior.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 14, 28));
        panelSuperior.setLayout(new javax.swing.BoxLayout(panelSuperior, javax.swing.BoxLayout.Y_AXIS));

        lblTitulo.setFont(new java.awt.Font("SansSerif", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(51, 51, 51));
        lblTitulo.setText("Avisos");
        panelSuperior.add(lblTitulo);

        lblSubtitulo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblSubtitulo.setForeground(new java.awt.Color(102, 102, 102));
        lblSubtitulo.setText("Alertas de objetos y coincidencias");
        panelSuperior.add(lblSubtitulo);

        add(panelSuperior, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelContenedor.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedor.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 28, 20, 28));
        panelContenedor.setLayout(new javax.swing.BoxLayout(panelContenedor, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(panelContenedor);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelSuperior;
    // End of variables declaration//GEN-END:variables
}
