/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import servicio.ObjetoServicio;

/**
 * Panel de Inicio minimalista compatible con NetBeans GUI Builder.
 * Ofrece accesos directos a Reclamar y Notificar, y lista dinámicamente los
 * objetos reportados en el campus mediante un bucle for.
 * 
 * @author cesar
 */
public class PanelInicio extends javax.swing.JPanel {

    private frmPrincipal ventana;
    private PanelApp panelApp;
    private Estudiante usuarioActual;
    private ObjetoServicio objetoServicio;

    /**
     * Creates new form PanelInicio
     */
    public PanelInicio() {
        this(null, null);
    }

    public PanelInicio(frmPrincipal ventana) {
        this(ventana, null);
    }

    public PanelInicio(frmPrincipal ventana, PanelApp panelApp) {
        this.ventana = ventana;
        this.panelApp = panelApp;
        initComponents();
        if (jScrollPane1 != null && jScrollPane1.getVerticalScrollBar() != null) {
            jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        }
        this.objetoServicio = new ObjetoServicio();
        cargarObjetos();
    }

    public void setPanelApp(PanelApp panelApp) {
        this.panelApp = panelApp;
    }

    public void setVentana(frmPrincipal ventana) {
        this.ventana = ventana;
    }

    public void setUsuarioActual(Estudiante estudiante) {
        this.usuarioActual = estudiante;
        cargarObjetos();
    }

    /**
     * Carga dinámicamente los objetos reportados en el campus desde la arquitectura
     * de servicios (ObjetoServicio / ObjetoReportadoCsvRepositorio).
     */
    public void cargarObjetos() {
        if (panelListaObjetos == null) {
            return;
        }

        panelListaObjetos.removeAll();

        this.objetoServicio = new ObjetoServicio();
        List<ObjetoReportado> listaObjetos = null;
        try {
            listaObjetos = objetoServicio.listarTodos();
        } catch (Exception ignored) {
        }

        if (listaObjetos == null || listaObjetos.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay objetos reportados en el campus por el momento.");
            lblVacio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblVacio.setForeground(new Color(153, 153, 153));
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelListaObjetos.add(Box.createVerticalStrut(24));
            panelListaObjetos.add(lblVacio);
        } else {
            for (ObjetoReportado obj : listaObjetos) {
                if (obj == null) {
                    continue;
                }
                JPanel tarjeta = crearTarjetaObjeto(obj);
                panelListaObjetos.add(tarjeta);
                panelListaObjetos.add(Box.createVerticalStrut(10));
            }
        }

        panelListaObjetos.revalidate();
        panelListaObjetos.repaint();
    }

    /**
     * Construye una tarjeta minimalista para cada objeto reportado usando componentes
     * básicos estándar de Swing (sin HTML).
     */
    private JPanel crearTarjetaObjeto(ObjetoReportado obj) {
        JPanel subpanel = new JPanel();
        subpanel.setBackground(Color.WHITE);
        subpanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(225, 230, 235), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        subpanel.setLayout(new BorderLayout(12, 0));
        subpanel.setMaximumSize(new Dimension(32767, 78));
        subpanel.setPreferredSize(new Dimension(440, 78));
        subpanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Determinar icono y color de fondo según categoría/nombre
        String cat = (obj.getCategoria() != null) ? obj.getCategoria().toUpperCase() : "";
        String nom = (obj.getNombre() != null) ? obj.getNombre().toUpperCase() : "";

        String icono = "📦";
        Color fondoIcono = new Color(243, 244, 246);
        if (cat.contains("DISPOSITIVO") || cat.contains("ELECTRONICO") || nom.contains("AUDIFON") || nom.contains("CELULAR")) {
            icono = "🎧";
            fondoIcono = new Color(254, 243, 199);
        } else if (cat.contains("PRENDA") || cat.contains("ROPA") || nom.contains("MOCHILA") || nom.contains("CASACA")) {
            icono = "📦";
            fondoIcono = new Color(241, 245, 249);
        } else if (nom.contains("LLAVE") || nom.contains("LLAVERO")) {
            icono = "🗝";
            fondoIcono = new Color(243, 244, 246);
        } else if (cat.contains("DOCUMENTO") || nom.contains("DNI") || nom.contains("CARNET")) {
            icono = "📋";
            fondoIcono = new Color(224, 242, 254);
        }

        // Cuadro de icono a la izquierda
        JPanel pnlIcono = new JPanel();
        pnlIcono.setBackground(fondoIcono);
        pnlIcono.setPreferredSize(new Dimension(42, 42));
        pnlIcono.setMaximumSize(new Dimension(42, 42));
        pnlIcono.setBorder(new LineBorder(new Color(220, 225, 230), 1, true));
        pnlIcono.setLayout(new GridBagLayout());

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 18));
        pnlIcono.add(lblIcono);

        // Textos al centro (JLabels estándar sin HTML)
        JPanel pnlInfo = new JPanel();
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));

        JLabel lblNombre = new JLabel(obj.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNombre.setForeground(new Color(30, 35, 45));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        String ubicacion = (obj.getUbicacion() != null && !obj.getUbicacion().isBlank()) ? obj.getUbicacion() : "Campus";
        JLabel lblUbicacion = new JLabel("📍 " + ubicacion);
        lblUbicacion.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblUbicacion.setForeground(new Color(110, 120, 135));
        lblUbicacion.setAlignmentX(Component.LEFT_ALIGNMENT);

        String fecha = (obj.getFecha() != null && !obj.getFecha().isBlank()) ? obj.getFecha() : "Reciente";
        JLabel lblFecha = new JLabel("🕒 " + fecha);
        lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFecha.setForeground(new Color(140, 150, 160));
        lblFecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlInfo.add(lblNombre);
        pnlInfo.add(Box.createVerticalStrut(3));
        pnlInfo.add(lblUbicacion);
        pnlInfo.add(Box.createVerticalStrut(2));
        pnlInfo.add(lblFecha);

        // Flecha a la derecha
        JLabel lblFlecha = new JLabel("›");
        lblFlecha.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblFlecha.setForeground(new Color(180, 190, 200));

        subpanel.add(pnlIcono, BorderLayout.WEST);
        subpanel.add(pnlInfo, BorderLayout.CENTER);
        subpanel.add(lblFlecha, BorderLayout.EAST);

        // Al hacer clic, navega a la sección Reclamar para buscarlo
        subpanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (panelApp != null) {
                    panelApp.mostrar("reclamar");
                }
            }
        });

        return subpanel;
    }

    /**
     * Método main para ejecutar y previsualizar este panel directamente desde NetBeans (Shift + F6).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("FindBack - Inicio");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 750);
            frame.setLocationRelativeTo(null);

            PanelInicio panel = new PanelInicio();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        panelContenedor = new javax.swing.JPanel();
        lblOpciones = new javax.swing.JLabel();
        espacio1 = new javax.swing.JPanel();
        cardReclamar = new javax.swing.JPanel();
        panelIconoReclamar = new javax.swing.JPanel();
        lblIconoReclamar = new javax.swing.JLabel();
        panelInfoReclamar = new javax.swing.JPanel();
        lblTitReclamar = new javax.swing.JLabel();
        lblDescReclamar = new javax.swing.JLabel();
        lblFlechaReclamar = new javax.swing.JLabel();
        espacio2 = new javax.swing.JPanel();
        cardNotificar = new javax.swing.JPanel();
        panelIconoNotificar = new javax.swing.JPanel();
        lblIconoNotificar = new javax.swing.JLabel();
        panelInfoNotificar = new javax.swing.JPanel();
        lblTitNotificar = new javax.swing.JLabel();
        lblDescNotificar = new javax.swing.JLabel();
        lblFlechaNotificar = new javax.swing.JLabel();
        espacio3 = new javax.swing.JPanel();
        panelHeaderObjetos = new javax.swing.JPanel();
        lblObjetosCampus = new javax.swing.JLabel();
        lblBuscarTodos = new javax.swing.JLabel();
        espacio4 = new javax.swing.JPanel();
        panelListaObjetos = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 750));
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelContenedor.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedor.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 24, 24));
        panelContenedor.setLayout(new javax.swing.BoxLayout(panelContenedor, javax.swing.BoxLayout.Y_AXIS));

        lblOpciones.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblOpciones.setForeground(new java.awt.Color(102, 112, 128));
        lblOpciones.setText("OPCIONES PRINCIPALES");
        panelContenedor.add(lblOpciones);

        espacio1.setBackground(new java.awt.Color(255, 255, 255));
        espacio1.setMaximumSize(new java.awt.Dimension(32767, 10));
        espacio1.setPreferredSize(new java.awt.Dimension(440, 10));
        panelContenedor.add(espacio1);

        cardReclamar.setBackground(new java.awt.Color(255, 255, 255));
        cardReclamar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 196, 255), 1, true));
        cardReclamar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cardReclamar.setMaximumSize(new java.awt.Dimension(32767, 75));
        cardReclamar.setPreferredSize(new java.awt.Dimension(440, 75));
        cardReclamar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardReclamarMouseClicked(evt);
            }
        });
        cardReclamar.setLayout(new java.awt.BorderLayout(14, 0));

        panelIconoReclamar.setBackground(new java.awt.Color(225, 239, 255));
        panelIconoReclamar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(210, 230, 255), 1, true));
        panelIconoReclamar.setPreferredSize(new java.awt.Dimension(42, 42));
        panelIconoReclamar.setLayout(new java.awt.GridBagLayout());

        lblIconoReclamar.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        lblIconoReclamar.setText("🔍");
        panelIconoReclamar.add(lblIconoReclamar, new java.awt.GridBagConstraints());

        cardReclamar.add(panelIconoReclamar, java.awt.BorderLayout.WEST);

        panelInfoReclamar.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoReclamar.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 4, 0));
        panelInfoReclamar.setLayout(new javax.swing.BoxLayout(panelInfoReclamar, javax.swing.BoxLayout.Y_AXIS));

        lblTitReclamar.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        lblTitReclamar.setForeground(new java.awt.Color(0, 102, 255));
        lblTitReclamar.setText("RECLAMAR");
        panelInfoReclamar.add(lblTitReclamar);

        lblDescReclamar.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblDescReclamar.setForeground(new java.awt.Color(102, 102, 102));
        lblDescReclamar.setText("Perdí un objeto y quiero buscarlo o registrarlo");
        panelInfoReclamar.add(lblDescReclamar);

        cardReclamar.add(panelInfoReclamar, java.awt.BorderLayout.CENTER);

        lblFlechaReclamar.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        lblFlechaReclamar.setForeground(new java.awt.Color(0, 102, 255));
        lblFlechaReclamar.setText("›");
        cardReclamar.add(lblFlechaReclamar, java.awt.BorderLayout.EAST);

        panelContenedor.add(cardReclamar);

        espacio2.setBackground(new java.awt.Color(255, 255, 255));
        espacio2.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio2.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio2);

        cardNotificar.setBackground(new java.awt.Color(255, 255, 255));
        cardNotificar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(225, 230, 235), 1, true));
        cardNotificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cardNotificar.setMaximumSize(new java.awt.Dimension(32767, 75));
        cardNotificar.setPreferredSize(new java.awt.Dimension(440, 75));
        cardNotificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardNotificarMouseClicked(evt);
            }
        });
        cardNotificar.setLayout(new java.awt.BorderLayout(14, 0));

        panelIconoNotificar.setBackground(new java.awt.Color(220, 248, 232));
        panelIconoNotificar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(200, 240, 218), 1, true));
        panelIconoNotificar.setPreferredSize(new java.awt.Dimension(42, 42));
        panelIconoNotificar.setLayout(new java.awt.GridBagLayout());

        lblIconoNotificar.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        lblIconoNotificar.setForeground(new java.awt.Color(26, 155, 90));
        lblIconoNotificar.setText("➕");
        panelIconoNotificar.add(lblIconoNotificar, new java.awt.GridBagConstraints());

        cardNotificar.add(panelIconoNotificar, java.awt.BorderLayout.WEST);

        panelInfoNotificar.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoNotificar.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 4, 0));
        panelInfoNotificar.setLayout(new javax.swing.BoxLayout(panelInfoNotificar, javax.swing.BoxLayout.Y_AXIS));

        lblTitNotificar.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        lblTitNotificar.setForeground(new java.awt.Color(40, 45, 55));
        lblTitNotificar.setText("NOTIFICAR");
        panelInfoNotificar.add(lblTitNotificar);

        lblDescNotificar.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblDescNotificar.setForeground(new java.awt.Color(102, 102, 102));
        lblDescNotificar.setText("Encontré un objeto en el campus y quiero reportarlo");
        panelInfoNotificar.add(lblDescNotificar);

        cardNotificar.add(panelInfoNotificar, java.awt.BorderLayout.CENTER);

        lblFlechaNotificar.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        lblFlechaNotificar.setForeground(new java.awt.Color(180, 190, 200));
        lblFlechaNotificar.setText("›");
        cardNotificar.add(lblFlechaNotificar, java.awt.BorderLayout.EAST);

        panelContenedor.add(cardNotificar);

        espacio3.setBackground(new java.awt.Color(255, 255, 255));
        espacio3.setMaximumSize(new java.awt.Dimension(32767, 24));
        espacio3.setPreferredSize(new java.awt.Dimension(440, 24));
        panelContenedor.add(espacio3);

        panelHeaderObjetos.setBackground(new java.awt.Color(255, 255, 255));
        panelHeaderObjetos.setMaximumSize(new java.awt.Dimension(32767, 24));
        panelHeaderObjetos.setPreferredSize(new java.awt.Dimension(440, 24));
        panelHeaderObjetos.setLayout(new java.awt.BorderLayout());

        lblObjetosCampus.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblObjetosCampus.setForeground(new java.awt.Color(102, 112, 128));
        lblObjetosCampus.setText("OBJETOS REPORTADOS EN CAMPUS");
        panelHeaderObjetos.add(lblObjetosCampus, java.awt.BorderLayout.WEST);

        lblBuscarTodos.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblBuscarTodos.setForeground(new java.awt.Color(0, 102, 255));
        lblBuscarTodos.setText("Buscar todos");
        lblBuscarTodos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBuscarTodos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBuscarTodosMouseClicked(evt);
            }
        });
        panelHeaderObjetos.add(lblBuscarTodos, java.awt.BorderLayout.EAST);

        panelContenedor.add(panelHeaderObjetos);

        espacio4.setBackground(new java.awt.Color(255, 255, 255));
        espacio4.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio4.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio4);

        panelListaObjetos.setBackground(new java.awt.Color(255, 255, 255));
        panelListaObjetos.setLayout(new javax.swing.BoxLayout(panelListaObjetos, javax.swing.BoxLayout.Y_AXIS));
        panelContenedor.add(panelListaObjetos);

        jScrollPane1.setViewportView(panelContenedor);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cardReclamarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardReclamarMouseClicked
        if (panelApp != null) {
            panelApp.mostrar("reclamar");
        }
    }//GEN-LAST:event_cardReclamarMouseClicked

    private void cardNotificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardNotificarMouseClicked
        if (panelApp != null) {
            panelApp.mostrar("notificar");
        }
    }//GEN-LAST:event_cardNotificarMouseClicked

    private void lblBuscarTodosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBuscarTodosMouseClicked
        if (panelApp != null) {
            panelApp.mostrar("reclamar");
        }
    }//GEN-LAST:event_lblBuscarTodosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cardNotificar;
    private javax.swing.JPanel cardReclamar;
    private javax.swing.JPanel espacio1;
    private javax.swing.JPanel espacio2;
    private javax.swing.JPanel espacio3;
    private javax.swing.JPanel espacio4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscarTodos;
    private javax.swing.JLabel lblDescNotificar;
    private javax.swing.JLabel lblDescReclamar;
    private javax.swing.JLabel lblFlechaNotificar;
    private javax.swing.JLabel lblFlechaReclamar;
    private javax.swing.JLabel lblIconoNotificar;
    private javax.swing.JLabel lblIconoReclamar;
    private javax.swing.JLabel lblObjetosCampus;
    private javax.swing.JLabel lblOpciones;
    private javax.swing.JLabel lblTitNotificar;
    private javax.swing.JLabel lblTitReclamar;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelHeaderObjetos;
    private javax.swing.JPanel panelIconoNotificar;
    private javax.swing.JPanel panelIconoReclamar;
    private javax.swing.JPanel panelInfoNotificar;
    private javax.swing.JPanel panelInfoReclamar;
    private javax.swing.JPanel panelListaObjetos;
    // End of variables declaration//GEN-END:variables
}
