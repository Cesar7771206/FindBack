package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import servicio.ObjetoServicio;

public class PanelInicio extends javax.swing.JPanel {

    private frmPrincipal ventana;
    private PanelApp panelApp;
    private Estudiante usuarioActual;
    private ObjetoServicio objetoServicio;

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
        actualizarBienvenida();
        cargarObjetos();
    }

    private void actualizarBienvenida() {
        if (lblSaludo == null) {
            return;
        }
        if (usuarioActual != null && usuarioActual.getNombre() != null && !usuarioActual.getNombre().isBlank()) {
            String nombreCompleto = usuarioActual.getNombre().trim();
            String[] partes = nombreCompleto.split("\\s+");
            String primerNombre = partes[0];
            primerNombre = primerNombre.substring(0, 1).toUpperCase() + primerNombre.substring(1).toLowerCase();
            lblSaludo.setText("¡Hola, " + primerNombre + "!");
            String car = usuarioActual.getCarrera();
            if (car != null && !car.isBlank()) {
                lblSubSaludo.setText("Estudiante de " + car);
            } else {
                lblSubSaludo.setText("Estudiante de Campus UDEP");
            }
        } else {
            lblSaludo.setText("¡Bienvenido a FindBack!");
            lblSubSaludo.setText("Portal de Objetos Perdidos del Campus");
        }
    }

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

    private JPanel crearTarjetaObjeto(ObjetoReportado obj) {
        JPanel subpanel = new JPanel();
        subpanel.setBackground(Color.WHITE);
        subpanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(225, 230, 235), 1, true),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        subpanel.setLayout(new BorderLayout(14, 0));
        subpanel.setMaximumSize(new Dimension(32767, 82));
        subpanel.setPreferredSize(new Dimension(440, 82));

        JPanel pnlInfo = new JPanel();
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));

        JLabel lblNombre = new JLabel(obj.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNombre.setForeground(new Color(30, 35, 45));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        String ubicacion = (obj.getUbicacion() != null && !obj.getUbicacion().isBlank()) ? obj.getUbicacion() : "Campus";
        JLabel lblUbicacion = new JLabel("Ubicación: " + ubicacion);
        lblUbicacion.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblUbicacion.setForeground(new Color(110, 120, 135));
        lblUbicacion.setAlignmentX(Component.LEFT_ALIGNMENT);

        String fecha = (obj.getFecha() != null && !obj.getFecha().isBlank()) ? obj.getFecha() : "Reciente";
        JLabel lblFecha = new JLabel("Fecha: " + fecha);
        lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFecha.setForeground(new Color(140, 150, 160));
        lblFecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlInfo.add(lblNombre);
        pnlInfo.add(Box.createVerticalStrut(3));
        pnlInfo.add(lblUbicacion);
        pnlInfo.add(Box.createVerticalStrut(2));
        pnlInfo.add(lblFecha);

        JPanel pnlBtn = new JPanel(new java.awt.GridBagLayout());
        pnlBtn.setBackground(Color.WHITE);

        JButton btnVer = new JButton("Ver");
        btnVer.setBackground(new Color(236, 235, 235));
        btnVer.setForeground(new Color(51, 51, 51));
        btnVer.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnVer.setFocusPainted(false);
        btnVer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVer.setPreferredSize(new Dimension(65, 30));
        btnVer.addActionListener(e -> {
            if (panelApp != null) {
                if (panelApp.getPanelReclamar() != null) {
                    panelApp.getPanelReclamar().setObjetoSeleccionado(obj);
                }
                panelApp.mostrar("reclamar");
            }
        });
        pnlBtn.add(btnVer);

        subpanel.add(pnlInfo, BorderLayout.CENTER);
        subpanel.add(pnlBtn, BorderLayout.EAST);

        return subpanel;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        panelContenedor = new javax.swing.JPanel();
        panelBienvenida = new javax.swing.JPanel();
        panelInfoUsuario = new javax.swing.JPanel();
        lblSaludo = new javax.swing.JLabel();
        lblSubSaludo = new javax.swing.JLabel();
        panelAccionesUsuario = new javax.swing.JPanel();
        btnCerrarSesion = new javax.swing.JButton();
        espacioBienvenida = new javax.swing.JPanel();
        panelHeaderOpciones = new javax.swing.JPanel();
        lblOpciones = new javax.swing.JLabel();
        espacio1 = new javax.swing.JPanel();
        cardReclamar = new javax.swing.JPanel();
        panelInfoReclamar = new javax.swing.JPanel();
        lblTitReclamar = new javax.swing.JLabel();
        lblDescReclamar = new javax.swing.JLabel();
        panelBtnReclamar = new javax.swing.JPanel();
        btnReclamar = new javax.swing.JButton();
        espacio2 = new javax.swing.JPanel();
        cardNotificar = new javax.swing.JPanel();
        panelInfoNotificar = new javax.swing.JPanel();
        lblTitNotificar = new javax.swing.JLabel();
        lblDescNotificar = new javax.swing.JLabel();
        panelBtnNotificar = new javax.swing.JPanel();
        btnNotificar = new javax.swing.JButton();
        espacio3 = new javax.swing.JPanel();
        panelHeaderObjetos = new javax.swing.JPanel();
        lblObjetosCampus = new javax.swing.JLabel();
        btnBuscarTodos = new javax.swing.JButton();
        espacio4 = new javax.swing.JPanel();
        panelListaObjetos = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 750));
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelContenedor.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedor.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));
        panelContenedor.setLayout(new javax.swing.BoxLayout(panelContenedor, javax.swing.BoxLayout.Y_AXIS));

        panelBienvenida.setBackground(new java.awt.Color(255, 255, 255));
        panelBienvenida.setMaximumSize(new java.awt.Dimension(32767, 48));
        panelBienvenida.setPreferredSize(new java.awt.Dimension(440, 48));
        panelBienvenida.setLayout(new java.awt.BorderLayout());

        panelInfoUsuario.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoUsuario.setLayout(new javax.swing.BoxLayout(panelInfoUsuario, javax.swing.BoxLayout.Y_AXIS));

        lblSaludo.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        lblSaludo.setForeground(new java.awt.Color(17, 24, 39));
        lblSaludo.setText("¡Hola, Estudiante!");
        panelInfoUsuario.add(lblSaludo);

        lblSubSaludo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblSubSaludo.setForeground(new java.awt.Color(107, 114, 128));
        lblSubSaludo.setText("Portal de Objetos Perdidos del Campus");
        panelInfoUsuario.add(lblSubSaludo);

        panelBienvenida.add(panelInfoUsuario, java.awt.BorderLayout.CENTER);

        panelAccionesUsuario.setBackground(new java.awt.Color(255, 255, 255));
        panelAccionesUsuario.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 4));

        btnCerrarSesion.setBackground(new java.awt.Color(254, 226, 226));
        btnCerrarSesion.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnCerrarSesion.setForeground(new java.awt.Color(220, 38, 38));
        btnCerrarSesion.setText("Cerrar sesión");
        btnCerrarSesion.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(254, 202, 202), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        btnCerrarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(this::btnCerrarSesionActionPerformed);
        panelAccionesUsuario.add(btnCerrarSesion);

        panelBienvenida.add(panelAccionesUsuario, java.awt.BorderLayout.EAST);

        panelContenedor.add(panelBienvenida);

        espacioBienvenida.setBackground(new java.awt.Color(255, 255, 255));
        espacioBienvenida.setMaximumSize(new java.awt.Dimension(32767, 16));
        espacioBienvenida.setPreferredSize(new java.awt.Dimension(440, 16));
        panelContenedor.add(espacioBienvenida);

        panelHeaderOpciones.setBackground(new java.awt.Color(255, 255, 255));
        panelHeaderOpciones.setMaximumSize(new java.awt.Dimension(32767, 24));
        panelHeaderOpciones.setPreferredSize(new java.awt.Dimension(440, 24));
        panelHeaderOpciones.setLayout(new java.awt.BorderLayout());

        lblOpciones.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblOpciones.setForeground(new java.awt.Color(102, 112, 128));
        lblOpciones.setText("OPCIONES PRINCIPALES");
        panelHeaderOpciones.add(lblOpciones, java.awt.BorderLayout.WEST);

        panelContenedor.add(panelHeaderOpciones);

        espacio1.setBackground(new java.awt.Color(255, 255, 255));
        espacio1.setMaximumSize(new java.awt.Dimension(32767, 10));
        espacio1.setPreferredSize(new java.awt.Dimension(440, 10));
        panelContenedor.add(espacio1);

        cardReclamar.setBackground(new java.awt.Color(255, 255, 255));
        cardReclamar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 196, 255), 1, true));
        cardReclamar.setMaximumSize(new java.awt.Dimension(32767, 75));
        cardReclamar.setPreferredSize(new java.awt.Dimension(440, 75));
        cardReclamar.setLayout(new java.awt.BorderLayout(12, 0));

        panelInfoReclamar.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoReclamar.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 0));
        panelInfoReclamar.setLayout(new javax.swing.BoxLayout(panelInfoReclamar, javax.swing.BoxLayout.Y_AXIS));

        lblTitReclamar.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblTitReclamar.setForeground(new java.awt.Color(0, 102, 255));
        lblTitReclamar.setText("RECLAMAR");
        panelInfoReclamar.add(lblTitReclamar);

        lblDescReclamar.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblDescReclamar.setForeground(new java.awt.Color(102, 102, 102));
        lblDescReclamar.setText("Perdí un objeto y quiero buscarlo o registrarlo");
        panelInfoReclamar.add(lblDescReclamar);

        cardReclamar.add(panelInfoReclamar, java.awt.BorderLayout.CENTER);

        panelBtnReclamar.setBackground(new java.awt.Color(255, 255, 255));
        panelBtnReclamar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 16));
        panelBtnReclamar.setLayout(new java.awt.GridBagLayout());

        btnReclamar.setBackground(new java.awt.Color(51, 153, 255));
        btnReclamar.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnReclamar.setForeground(new java.awt.Color(255, 255, 255));
        btnReclamar.setText("Reclamar");
        btnReclamar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
        btnReclamar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReclamar.setPreferredSize(new java.awt.Dimension(100, 34));
        btnReclamar.addActionListener(this::btnReclamarActionPerformed);
        panelBtnReclamar.add(btnReclamar, new java.awt.GridBagConstraints());

        cardReclamar.add(panelBtnReclamar, java.awt.BorderLayout.EAST);

        panelContenedor.add(cardReclamar);

        espacio2.setBackground(new java.awt.Color(255, 255, 255));
        espacio2.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio2.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio2);

        cardNotificar.setBackground(new java.awt.Color(255, 255, 255));
        cardNotificar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 224, 230), 1, true));
        cardNotificar.setMaximumSize(new java.awt.Dimension(32767, 75));
        cardNotificar.setPreferredSize(new java.awt.Dimension(440, 75));
        cardNotificar.setLayout(new java.awt.BorderLayout(12, 0));

        panelInfoNotificar.setBackground(new java.awt.Color(255, 255, 255));
        panelInfoNotificar.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 0));
        panelInfoNotificar.setLayout(new javax.swing.BoxLayout(panelInfoNotificar, javax.swing.BoxLayout.Y_AXIS));

        lblTitNotificar.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblTitNotificar.setForeground(new java.awt.Color(33, 37, 41));
        lblTitNotificar.setText("NOTIFICAR");
        panelInfoNotificar.add(lblTitNotificar);

        lblDescNotificar.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblDescNotificar.setForeground(new java.awt.Color(102, 102, 102));
        lblDescNotificar.setText("Encontré un objeto en el campus y quiero reportarlo");
        panelInfoNotificar.add(lblDescNotificar);

        cardNotificar.add(panelInfoNotificar, java.awt.BorderLayout.CENTER);

        panelBtnNotificar.setBackground(new java.awt.Color(255, 255, 255));
        panelBtnNotificar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 16));
        panelBtnNotificar.setLayout(new java.awt.GridBagLayout());

        btnNotificar.setBackground(new java.awt.Color(236, 235, 235));
        btnNotificar.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnNotificar.setForeground(new java.awt.Color(51, 51, 51));
        btnNotificar.setText("Notificar");
        btnNotificar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        btnNotificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNotificar.setPreferredSize(new java.awt.Dimension(100, 34));
        btnNotificar.addActionListener(this::btnNotificarActionPerformed);
        panelBtnNotificar.add(btnNotificar, new java.awt.GridBagConstraints());

        cardNotificar.add(panelBtnNotificar, java.awt.BorderLayout.EAST);

        panelContenedor.add(cardNotificar);

        espacio3.setBackground(new java.awt.Color(255, 255, 255));
        espacio3.setMaximumSize(new java.awt.Dimension(32767, 18));
        espacio3.setPreferredSize(new java.awt.Dimension(440, 18));
        panelContenedor.add(espacio3);

        panelHeaderObjetos.setBackground(new java.awt.Color(255, 255, 255));
        panelHeaderObjetos.setMaximumSize(new java.awt.Dimension(32767, 30));
        panelHeaderObjetos.setPreferredSize(new java.awt.Dimension(440, 30));
        panelHeaderObjetos.setLayout(new java.awt.BorderLayout());

        lblObjetosCampus.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblObjetosCampus.setForeground(new java.awt.Color(102, 112, 128));
        lblObjetosCampus.setText("OBJETOS REPORTADOS EN CAMPUS");
        panelHeaderObjetos.add(lblObjetosCampus, java.awt.BorderLayout.WEST);

        btnBuscarTodos.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscarTodos.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        btnBuscarTodos.setForeground(new java.awt.Color(0, 102, 255));
        btnBuscarTodos.setText("Buscar todos");
        btnBuscarTodos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 196, 255), 1, true));
        btnBuscarTodos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarTodos.setPreferredSize(new java.awt.Dimension(110, 28));
        btnBuscarTodos.addActionListener(this::btnBuscarTodosActionPerformed);
        panelHeaderObjetos.add(btnBuscarTodos, java.awt.BorderLayout.EAST);

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

    private void btnReclamarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReclamarActionPerformed
        if (panelApp != null) {
            panelApp.mostrar("reclamar");
        }
    }//GEN-LAST:event_btnReclamarActionPerformed

    private void btnNotificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificarActionPerformed
        if (panelApp != null) {
            panelApp.mostrar("notificar");
        }
    }//GEN-LAST:event_btnNotificarActionPerformed

    private void btnBuscarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarTodosActionPerformed
        if (panelApp != null) {
            panelApp.mostrar("reclamar");
        }
    }//GEN-LAST:event_btnBuscarTodosActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        if (ventana != null) {
            ventana.cerrarSesion();
        }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarTodos;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnNotificar;
    private javax.swing.JButton btnReclamar;
    private javax.swing.JPanel cardNotificar;
    private javax.swing.JPanel cardReclamar;
    private javax.swing.JPanel espacio1;
    private javax.swing.JPanel espacio2;
    private javax.swing.JPanel espacio3;
    private javax.swing.JPanel espacio4;
    private javax.swing.JPanel espacioBienvenida;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDescNotificar;
    private javax.swing.JLabel lblDescReclamar;
    private javax.swing.JLabel lblObjetosCampus;
    private javax.swing.JLabel lblOpciones;
    private javax.swing.JLabel lblSaludo;
    private javax.swing.JLabel lblSubSaludo;
    private javax.swing.JLabel lblTitNotificar;
    private javax.swing.JLabel lblTitReclamar;
    private javax.swing.JPanel panelAccionesUsuario;
    private javax.swing.JPanel panelBienvenida;
    private javax.swing.JPanel panelBtnNotificar;
    private javax.swing.JPanel panelBtnReclamar;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelHeaderObjetos;
    private javax.swing.JPanel panelHeaderOpciones;
    private javax.swing.JPanel panelInfoNotificar;
    private javax.swing.JPanel panelInfoReclamar;
    private javax.swing.JPanel panelInfoUsuario;
    private javax.swing.JPanel panelListaObjetos;
    // End of variables declaration//GEN-END:variables
}
