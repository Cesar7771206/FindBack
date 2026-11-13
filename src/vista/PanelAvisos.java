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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import servicio.CoincidenciaServicio;
import servicio.ObjetoServicio;

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

    /**
     * Creates new form PanelAvisos
     */
    public PanelAvisos() {
        initComponents();
        this.objetoServicio = new ObjetoServicio();
        this.coincidenciaServicio = new CoincidenciaServicio();
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
     * Recarga dinámicamente los avisos mediante un for que genera subpaneles.
     * Muestra datos de prueba para visualizar el diseño mientras no haya datos reales cargados.
     */
    public void cargarAvisos() {
        if (panelContenedor == null) {
            return;
        }

        panelContenedor.removeAll();

        List<String[]> listaParaMostrar = new ArrayList<>();

        // 1. Si hay un usuario en sesión, buscamos sus coincidencias y mensajes reales
        if (usuarioActual != null) {
            // Coincidencias de objetos reportados
            try {
                if (objetoServicio != null && coincidenciaServicio != null) {
                    List<ObjetoReportado> misObjetos = objetoServicio.listarPorEstudiante(usuarioActual.getIdentificacion());
                    for (ObjetoReportado obj : misObjetos) {
                        List<ObjetoReportado> matches = coincidenciaServicio.buscarCoincidencias(obj);
                        for (ObjetoReportado match : matches) {
                            String detalle = "Coincidencia con " + match.getNombre() + " (" + match.getCategoria() + ") en " + match.getUbicacion() + ".";
                            String fecha = (match.getFecha() != null) ? match.getFecha() : "Reciente";
                            listaParaMostrar.add(new String[]{"¡Coincidencia Detectada!", detalle, fecha});
                        }
                    }
                }
            } catch (Exception ignored) {
            }

            // Mensajes recibidos en el buzón del estudiante
            ArrayList<String> mensajes = usuarioActual.getMensajes();
            if (mensajes != null && !mensajes.isEmpty()) {
                for (int i = mensajes.size() - 1; i >= 0; i--) {
                    String msg = mensajes.get(i);
                    if (msg != null && !msg.isBlank() && !msg.equalsIgnoreCase("SIN_MENSAJES")) {
                        String titulo = "Notificación de Reclamo";
                        if (msg.contains("APROBADO")) {
                            titulo = "Estado de Reclamo Actualizado";
                        } else if (msg.contains("RECHAZADO")) {
                            titulo = "Estado de Reclamo Actualizado";
                        } else if (msg.contains("entregado")) {
                            titulo = "Objeto Entregado";
                        }
                        listaParaMostrar.add(new String[]{titulo, msg, "Reciente"});
                    }
                }
            }
        }

        // 2. Si aún no hay datos reales cargados, colocamos avisos de ejemplo para ver cómo queda el diseño
        if (listaParaMostrar.isEmpty()) {
            listaParaMostrar.add(new String[]{
                "¡Coincidencia Detectada!",
                "Se encontró un objeto que coincide con tu reporte de Mochila en Pabellón B.",
                "Hace 10 min"
            });
            listaParaMostrar.add(new String[]{
                "Objeto Notificado",
                "Tu reporte de Audífonos Sony fue publicado en la base de datos del campus.",
                "Hace 2 horas"
            });
            listaParaMostrar.add(new String[]{
                "Estado de Reclamo",
                "Tu reclamo REC-001 para DNI Azul cambió automáticamente a: APROBADO.",
                "Hace 5 horas"
            });
            listaParaMostrar.add(new String[]{
                "Objeto Entregado",
                "El termo metálico que reportaste ha sido devuelto exitosamente a su dueño.",
                "Ayer"
            });
        }

        // 3. Bucle for para generar y agregar los subpaneles dentro del panel con scroll
        for (String[] aviso : listaParaMostrar) {
            JPanel tarjeta = crearSubpanelAviso(aviso[0], aviso[1], aviso[2]);
            panelContenedor.add(tarjeta);
            panelContenedor.add(Box.createVerticalStrut(12));
        }

        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    /**
     * Crea un subpanel minimalista con componentes básicos estándar de Swing (sin HTML).
     */
    private JPanel crearSubpanelAviso(String titulo, String descripcion, String tiempo) {
        JPanel subpanel = new JPanel();
        subpanel.setBackground(Color.WHITE);
        subpanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        subpanel.setLayout(new BorderLayout(12, 0));
        subpanel.setMaximumSize(new Dimension(32767, 85));
        subpanel.setPreferredSize(new Dimension(440, 85));

        // Cuadro de icono a la izquierda
        JPanel pnlIcono = new JPanel();
        pnlIcono.setBackground(new Color(242, 242, 242));
        pnlIcono.setPreferredSize(new Dimension(38, 38));
        pnlIcono.setMaximumSize(new Dimension(38, 38));
        pnlIcono.setLayout(new GridBagLayout());

        JLabel lblIcono = new JLabel("🔔");
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblIcono.setForeground(new Color(102, 102, 102));
        pnlIcono.add(lblIcono);

        // Textos al centro (JLabels estándar sin HTML)
        JPanel pnlInfo = new JPanel();
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));

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

        pnlInfo.add(lblTit);
        pnlInfo.add(Box.createVerticalStrut(4));
        pnlInfo.add(lblDesc);
        pnlInfo.add(Box.createVerticalStrut(4));
        pnlInfo.add(lblHora);

        subpanel.add(pnlIcono, BorderLayout.WEST);
        subpanel.add(pnlInfo, BorderLayout.CENTER);

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
        card1 = new javax.swing.JPanel();
        panelIcono1 = new javax.swing.JPanel();
        lblIcono1 = new javax.swing.JLabel();
        panelInfo1 = new javax.swing.JPanel();
        lblTit1 = new javax.swing.JLabel();
        lblDesc1 = new javax.swing.JLabel();
        lblHora1 = new javax.swing.JLabel();
        espacio1 = new javax.swing.JPanel();
        card2 = new javax.swing.JPanel();
        panelIcono2 = new javax.swing.JPanel();
        lblIcono2 = new javax.swing.JLabel();
        panelInfo2 = new javax.swing.JPanel();
        lblTit2 = new javax.swing.JLabel();
        lblDesc2 = new javax.swing.JLabel();
        lblHora2 = new javax.swing.JLabel();

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

        card1.setBackground(new java.awt.Color(255, 255, 255));
        card1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        card1.setMaximumSize(new java.awt.Dimension(32767, 85));
        card1.setPreferredSize(new java.awt.Dimension(440, 85));
        card1.setLayout(new java.awt.BorderLayout(12, 0));

        panelIcono1.setBackground(new java.awt.Color(242, 242, 242));
        panelIcono1.setPreferredSize(new java.awt.Dimension(38, 38));
        panelIcono1.setLayout(new java.awt.GridBagLayout());

        lblIcono1.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        lblIcono1.setForeground(new java.awt.Color(102, 102, 102));
        lblIcono1.setText("🔔");
        panelIcono1.add(lblIcono1, new java.awt.GridBagConstraints());

        card1.add(panelIcono1, java.awt.BorderLayout.WEST);

        panelInfo1.setBackground(new java.awt.Color(255, 255, 255));
        panelInfo1.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 2, 4, 4));
        panelInfo1.setLayout(new javax.swing.BoxLayout(panelInfo1, javax.swing.BoxLayout.Y_AXIS));

        lblTit1.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        lblTit1.setForeground(new java.awt.Color(51, 51, 51));
        lblTit1.setText("¡Coincidencia Detectada!");
        panelInfo1.add(lblTit1);

        lblDesc1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblDesc1.setForeground(new java.awt.Color(102, 102, 102));
        lblDesc1.setText("Se encontró un objeto que coincide con tu reporte de Mochila en Pabellón B.");
        panelInfo1.add(lblDesc1);

        lblHora1.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        lblHora1.setForeground(new java.awt.Color(153, 153, 153));
        lblHora1.setText("Hace 10 min");
        panelInfo1.add(lblHora1);

        card1.add(panelInfo1, java.awt.BorderLayout.CENTER);

        panelContenedor.add(card1);

        espacio1.setBackground(new java.awt.Color(255, 255, 255));
        espacio1.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio1.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio1);

        card2.setBackground(new java.awt.Color(255, 255, 255));
        card2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        card2.setMaximumSize(new java.awt.Dimension(32767, 85));
        card2.setPreferredSize(new java.awt.Dimension(440, 85));
        card2.setLayout(new java.awt.BorderLayout(12, 0));

        panelIcono2.setBackground(new java.awt.Color(242, 242, 242));
        panelIcono2.setPreferredSize(new java.awt.Dimension(38, 38));
        panelIcono2.setLayout(new java.awt.GridBagLayout());

        lblIcono2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        lblIcono2.setForeground(new java.awt.Color(102, 102, 102));
        lblIcono2.setText("🔔");
        panelIcono2.add(lblIcono2, new java.awt.GridBagConstraints());

        card2.add(panelIcono2, java.awt.BorderLayout.WEST);

        panelInfo2.setBackground(new java.awt.Color(255, 255, 255));
        panelInfo2.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 2, 4, 4));
        panelInfo2.setLayout(new javax.swing.BoxLayout(panelInfo2, javax.swing.BoxLayout.Y_AXIS));

        lblTit2.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        lblTit2.setForeground(new java.awt.Color(51, 51, 51));
        lblTit2.setText("Objeto Notificado");
        panelInfo2.add(lblTit2);

        lblDesc2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblDesc2.setForeground(new java.awt.Color(102, 102, 102));
        lblDesc2.setText("Tu reporte de Audífonos Sony fue publicado en la base de datos.");
        panelInfo2.add(lblDesc2);

        lblHora2.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        lblHora2.setForeground(new java.awt.Color(153, 153, 153));
        lblHora2.setText("Hace 2 horas");
        panelInfo2.add(lblHora2);

        card2.add(panelInfo2, java.awt.BorderLayout.CENTER);

        panelContenedor.add(card2);

        jScrollPane1.setViewportView(panelContenedor);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel card1;
    private javax.swing.JPanel card2;
    private javax.swing.JPanel espacio1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDesc1;
    private javax.swing.JLabel lblDesc2;
    private javax.swing.JLabel lblHora1;
    private javax.swing.JLabel lblHora2;
    private javax.swing.JLabel lblIcono1;
    private javax.swing.JLabel lblIcono2;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTit1;
    private javax.swing.JLabel lblTit2;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelIcono1;
    private javax.swing.JPanel panelIcono2;
    private javax.swing.JPanel panelInfo1;
    private javax.swing.JPanel panelInfo2;
    private javax.swing.JPanel panelSuperior;
    // End of variables declaration//GEN-END:variables
}
