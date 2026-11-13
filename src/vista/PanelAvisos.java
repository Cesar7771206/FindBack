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
 * Panel de Avisos para la visualización de notificaciones, coincidencias detectadas
 * y cambios de estado en objetos o reclamos.
 * 
 * Diseñado con estilo minimalista compatible con NetBeans GUI Builder.
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
    }

    public PanelAvisos(Estudiante estudiante) {
        this();
        setUsuarioActual(estudiante);
    }

    public void setUsuarioActual(Estudiante estudiante) {
        this.usuarioActual = estudiante;
        actualizarAvisos();
    }

    public Estudiante getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Carga y renderiza dinámicamente los avisos del usuario actual:
     * - Coincidencias detectadas para objetos reportados
     * - Mensajes directos de reclamos aprobados/rechazados o avisos del sistema
     */
    public void actualizarAvisos() {
        if (panelListaAvisos == null) {
            return;
        }

        // Si no hay sesión activa aún (por ejemplo en previsualización de NetBeans),
        // se mantienen las tarjetas de muestra del diseño.
        if (usuarioActual == null) {
            return;
        }

        panelListaAvisos.removeAll();
        int totalAvisos = 0;

        // 1. Detectar coincidencias para objetos que el estudiante haya reportado
        try {
            if (objetoServicio != null && coincidenciaServicio != null) {
                List<ObjetoReportado> misObjetos = objetoServicio.listarPorEstudiante(usuarioActual.getIdentificacion());
                for (ObjetoReportado miObj : misObjetos) {
                    List<ObjetoReportado> coincidencias = coincidenciaServicio.buscarCoincidencias(miObj);
                    for (ObjetoReportado c : coincidencias) {
                        String msg = "Se encontró un objeto '" + c.getNombre() + "' ("
                                + c.getCategoria() + ", " + c.getColor() + ") en "
                                + c.getUbicacion() + " que coincide con tu reporte de '" + miObj.getNombre() + "'.";
                        String tiempo = (c.getFecha() != null && !c.getFecha().isBlank()) ? c.getFecha() : "Reciente";
                        panelListaAvisos.add(crearTarjetaAviso("¡Coincidencia Detectada!", msg, tiempo));
                        panelListaAvisos.add(crearSeparador());
                        totalAvisos++;
                    }
                }
            }
        } catch (Exception ignored) {
        }

        // 2. Avisos y notificaciones en el buzón del usuario (reclamos aprobados, rechazados, entregas)
        ArrayList<String> mensajes = usuarioActual.getMensajes();
        if (mensajes != null && !mensajes.isEmpty()) {
            for (int i = mensajes.size() - 1; i >= 0; i--) {
                String msg = mensajes.get(i);
                if (msg == null || msg.isBlank() || msg.equalsIgnoreCase("SIN_MENSAJES")) {
                    continue;
                }
                String titulo = deducirTituloAviso(msg);
                panelListaAvisos.add(crearTarjetaAviso(titulo, msg, "Reciente"));
                panelListaAvisos.add(crearSeparador());
                totalAvisos++;
            }
        }

        // 3. Estado vacío minimalista si no hay avisos
        if (totalAvisos == 0) {
            panelListaAvisos.add(crearPanelVacio());
        }

        panelListaAvisos.revalidate();
        panelListaAvisos.repaint();
    }

    /**
     * Agrega un aviso individual dinámicamente en la parte superior.
     */
    public void agregarAviso(String titulo, String mensaje, String tiempo) {
        if (panelListaAvisos == null) {
            return;
        }
        panelListaAvisos.add(crearTarjetaAviso(titulo, mensaje, tiempo), 0);
        panelListaAvisos.add(crearSeparador(), 1);
        panelListaAvisos.revalidate();
        panelListaAvisos.repaint();
    }

    private JPanel crearTarjetaAviso(String titulo, String mensaje, String tiempo) {
        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(new Color(255, 255, 255));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 230, 234), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 14)
        ));
        tarjeta.setLayout(new BorderLayout(12, 0));
        tarjeta.setMaximumSize(new Dimension(32767, 95));
        tarjeta.setPreferredSize(new Dimension(430, 90));

        // Icono minimalista
        JPanel panelIcono = new JPanel();
        panelIcono.setBackground(new Color(240, 243, 246));
        panelIcono.setPreferredSize(new Dimension(40, 40));
        panelIcono.setMaximumSize(new Dimension(40, 40));
        panelIcono.setLayout(new GridBagLayout());

        JLabel lblIcono = new JLabel("🔔");
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblIcono.setForeground(new Color(102, 102, 102));
        panelIcono.add(lblIcono);

        // Contenedor de textos
        JPanel panelContenido = new JPanel();
        panelContenido.setBackground(new Color(255, 255, 255));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 4));
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTit.setForeground(new Color(45, 55, 72));
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);

        String textoHtml = "<html><body style='width: 290px;'>" + mensaje + "</body></html>";
        JLabel lblDesc = new JLabel(textoHtml);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(113, 128, 150));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        String t = (tiempo != null && !tiempo.isBlank()) ? tiempo : "Reciente";
        JLabel lblT = new JLabel(t);
        lblT.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblT.setForeground(new Color(160, 174, 192));
        lblT.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelContenido.add(lblTit);
        panelContenido.add(Box.createVerticalStrut(3));
        panelContenido.add(lblDesc);
        panelContenido.add(Box.createVerticalStrut(4));
        panelContenido.add(lblT);

        tarjeta.add(panelIcono, BorderLayout.WEST);
        tarjeta.add(panelContenido, BorderLayout.CENTER);

        return tarjeta;
    }

    private JPanel crearSeparador() {
        JPanel sep = new JPanel();
        sep.setBackground(new Color(255, 255, 255));
        sep.setMaximumSize(new Dimension(32767, 10));
        sep.setPreferredSize(new Dimension(430, 10));
        return sep;
    }

    private JPanel crearPanelVacio() {
        JPanel vacio = new JPanel();
        vacio.setBackground(new Color(255, 255, 255));
        vacio.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 233, 238), 1, true),
            BorderFactory.createEmptyBorder(40, 20, 40, 20)
        ));
        vacio.setLayout(new BoxLayout(vacio, BoxLayout.Y_AXIS));
        vacio.setMaximumSize(new Dimension(32767, 160));
        vacio.setPreferredSize(new Dimension(430, 160));

        JLabel icono = new JLabel("🔔");
        icono.setFont(new Font("SansSerif", Font.PLAIN, 28));
        icono.setForeground(new Color(180, 185, 195));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("No tienes avisos por el momento");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(new Color(74, 85, 104));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Aquí aparecerán las alertas de coincidencias y reclamos.");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(140, 150, 165));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        vacio.add(icono);
        vacio.add(Box.createVerticalStrut(10));
        vacio.add(titulo);
        vacio.add(Box.createVerticalStrut(4));
        vacio.add(subtitulo);

        return vacio;
    }

    private String deducirTituloAviso(String mensaje) {
        if (mensaje == null) {
            return "Aviso";
        }
        String m = mensaje.toLowerCase();
        if (m.contains("aprobado")) {
            return "Estado de Reclamo Actualizado";
        } else if (m.contains("rechazado")) {
            return "Estado de Reclamo Actualizado";
        } else if (m.contains("coincidencia")) {
            return "¡Coincidencia Detectada!";
        } else if (m.contains("entregado exitosamente") || m.contains("entregado a")) {
            return "Objeto Entregado";
        } else if (m.contains("registrado tu reclamo")) {
            return "Reclamo Registrado";
        } else if (m.contains("publicado") || m.contains("notificado")) {
            return "Objeto Notificado Exitosamente";
        }
        return "Aviso del Campus";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCabecera = new javax.swing.JPanel();
        panelTitulos = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        scrollAvisos = new javax.swing.JScrollPane();
        panelListaAvisos = new javax.swing.JPanel();
        cardAviso1 = new javax.swing.JPanel();
        panelIcono1 = new javax.swing.JPanel();
        lblIcono1 = new javax.swing.JLabel();
        panelContenido1 = new javax.swing.JPanel();
        lblTitulo1 = new javax.swing.JLabel();
        lblDesc1 = new javax.swing.JLabel();
        lblTiempo1 = new javax.swing.JLabel();
        separador1 = new javax.swing.JPanel();
        cardAviso2 = new javax.swing.JPanel();
        panelIcono2 = new javax.swing.JPanel();
        lblIcono2 = new javax.swing.JLabel();
        panelContenido2 = new javax.swing.JPanel();
        lblTitulo2 = new javax.swing.JLabel();
        lblDesc2 = new javax.swing.JLabel();
        lblTiempo2 = new javax.swing.JLabel();
        separador2 = new javax.swing.JPanel();
        cardAviso3 = new javax.swing.JPanel();
        panelIcono3 = new javax.swing.JPanel();
        lblIcono3 = new javax.swing.JLabel();
        panelContenido3 = new javax.swing.JPanel();
        lblTitulo3 = new javax.swing.JLabel();
        lblDesc3 = new javax.swing.JLabel();
        lblTiempo3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 750));
        setLayout(new java.awt.BorderLayout());

        panelCabecera.setBackground(new java.awt.Color(255, 255, 255));
        panelCabecera.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 26, 12, 26));
        panelCabecera.setLayout(new java.awt.BorderLayout());

        panelTitulos.setBackground(new java.awt.Color(255, 255, 255));
        panelTitulos.setLayout(new javax.swing.BoxLayout(panelTitulos, javax.swing.BoxLayout.Y_AXIS));

        lblTitulo.setFont(new java.awt.Font("SansSerif", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(33, 37, 41));
        lblTitulo.setText("Avisos");
        panelTitulos.add(lblTitulo);

        lblSubtitulo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblSubtitulo.setForeground(new java.awt.Color(108, 117, 125));
        lblSubtitulo.setText("Centro de notificaciones y coincidencias");
        panelTitulos.add(lblSubtitulo);

        panelCabecera.add(panelTitulos, java.awt.BorderLayout.CENTER);

        btnActualizar.setBackground(new java.awt.Color(245, 247, 250));
        btnActualizar.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(74, 85, 104));
        btnActualizar.setText("Actualizar");
        btnActualizar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(218, 222, 229)));
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnActualizar.setFocusable(false);
        btnActualizar.setPreferredSize(new java.awt.Dimension(90, 32));
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);
        panelCabecera.add(btnActualizar, java.awt.BorderLayout.LINE_END);

        add(panelCabecera, java.awt.BorderLayout.PAGE_START);

        scrollAvisos.setBackground(new java.awt.Color(255, 255, 255));
        scrollAvisos.setBorder(null);
        scrollAvisos.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelListaAvisos.setBackground(new java.awt.Color(255, 255, 255));
        panelListaAvisos.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 24, 20, 24));
        panelListaAvisos.setLayout(new javax.swing.BoxLayout(panelListaAvisos, javax.swing.BoxLayout.Y_AXIS));

        cardAviso1.setBackground(new java.awt.Color(255, 255, 255));
        cardAviso1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 230, 234), 1, true));
        cardAviso1.setMaximumSize(new java.awt.Dimension(32767, 95));
        cardAviso1.setPreferredSize(new java.awt.Dimension(430, 90));
        cardAviso1.setLayout(new java.awt.BorderLayout(12, 0));

        panelIcono1.setBackground(new java.awt.Color(240, 243, 246));
        panelIcono1.setPreferredSize(new java.awt.Dimension(40, 40));
        panelIcono1.setLayout(new java.awt.GridBagLayout());

        lblIcono1.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        lblIcono1.setForeground(new java.awt.Color(102, 102, 102));
        lblIcono1.setText("🔔");
        panelIcono1.add(lblIcono1, new java.awt.GridBagConstraints());

        cardAviso1.add(panelIcono1, java.awt.BorderLayout.WEST);

        panelContenido1.setBackground(new java.awt.Color(255, 255, 255));
        panelContenido1.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 4, 8, 8));
        panelContenido1.setLayout(new javax.swing.BoxLayout(panelContenido1, javax.swing.BoxLayout.Y_AXIS));

        lblTitulo1.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        lblTitulo1.setForeground(new java.awt.Color(45, 55, 72));
        lblTitulo1.setText("Objeto Notificado Exitosamente");
        panelContenido1.add(lblTitulo1);

        lblDesc1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblDesc1.setForeground(new java.awt.Color(113, 128, 150));
        lblDesc1.setText("Tu reporte de \"Mochila North Face Gris\" fue publicado en el campus.");
        panelContenido1.add(lblDesc1);

        lblTiempo1.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        lblTiempo1.setForeground(new java.awt.Color(160, 174, 192));
        lblTiempo1.setText("Hace 2 horas");
        panelContenido1.add(lblTiempo1);

        cardAviso1.add(panelContenido1, java.awt.BorderLayout.CENTER);

        panelListaAvisos.add(cardAviso1);

        separador1.setBackground(new java.awt.Color(255, 255, 255));
        separador1.setMaximumSize(new java.awt.Dimension(32767, 10));
        separador1.setPreferredSize(new java.awt.Dimension(430, 10));
        panelListaAvisos.add(separador1);

        cardAviso2.setBackground(new java.awt.Color(255, 255, 255));
        cardAviso2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 230, 234), 1, true));
        cardAviso2.setMaximumSize(new java.awt.Dimension(32767, 95));
        cardAviso2.setPreferredSize(new java.awt.Dimension(430, 90));
        cardAviso2.setLayout(new java.awt.BorderLayout(12, 0));

        panelIcono2.setBackground(new java.awt.Color(240, 243, 246));
        panelIcono2.setPreferredSize(new java.awt.Dimension(40, 40));
        panelIcono2.setLayout(new java.awt.GridBagLayout());

        lblIcono2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        lblIcono2.setForeground(new java.awt.Color(102, 102, 102));
        lblIcono2.setText("🔔");
        panelIcono2.add(lblIcono2, new java.awt.GridBagConstraints());

        cardAviso2.add(panelIcono2, java.awt.BorderLayout.WEST);

        panelContenido2.setBackground(new java.awt.Color(255, 255, 255));
        panelContenido2.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 4, 8, 8));
        panelContenido2.setLayout(new javax.swing.BoxLayout(panelContenido2, javax.swing.BoxLayout.Y_AXIS));

        lblTitulo2.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        lblTitulo2.setForeground(new java.awt.Color(45, 55, 72));
        lblTitulo2.setText("Coincidencia Detectada");
        panelContenido2.add(lblTitulo2);

        lblDesc2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblDesc2.setForeground(new java.awt.Color(113, 128, 150));
        lblDesc2.setText("Se encontró un objeto que coincide con tu reporte de \"Audífonos Sony\".");
        panelContenido2.add(lblDesc2);

        lblTiempo2.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        lblTiempo2.setForeground(new java.awt.Color(160, 174, 192));
        lblTiempo2.setText("Hace 10 min");
        panelContenido2.add(lblTiempo2);

        cardAviso2.add(panelContenido2, java.awt.BorderLayout.CENTER);

        panelListaAvisos.add(cardAviso2);

        separador2.setBackground(new java.awt.Color(255, 255, 255));
        separador2.setMaximumSize(new java.awt.Dimension(32767, 10));
        separador2.setPreferredSize(new java.awt.Dimension(430, 10));
        panelListaAvisos.add(separador2);

        cardAviso3.setBackground(new java.awt.Color(255, 255, 255));
        cardAviso3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 230, 234), 1, true));
        cardAviso3.setMaximumSize(new java.awt.Dimension(32767, 95));
        cardAviso3.setPreferredSize(new java.awt.Dimension(430, 90));
        cardAviso3.setLayout(new java.awt.BorderLayout(12, 0));

        panelIcono3.setBackground(new java.awt.Color(240, 243, 246));
        panelIcono3.setPreferredSize(new java.awt.Dimension(40, 40));
        panelIcono3.setLayout(new java.awt.GridBagLayout());

        lblIcono3.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        lblIcono3.setForeground(new java.awt.Color(102, 102, 102));
        lblIcono3.setText("🔔");
        panelIcono3.add(lblIcono3, new java.awt.GridBagConstraints());

        cardAviso3.add(panelIcono3, java.awt.BorderLayout.WEST);

        panelContenido3.setBackground(new java.awt.Color(255, 255, 255));
        panelContenido3.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 4, 8, 8));
        panelContenido3.setLayout(new javax.swing.BoxLayout(panelContenido3, javax.swing.BoxLayout.Y_AXIS));

        lblTitulo3.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        lblTitulo3.setForeground(new java.awt.Color(45, 55, 72));
        lblTitulo3.setText("Estado de Reclamo Actualizado");
        panelContenido3.add(lblTitulo3);

        lblDesc3.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblDesc3.setForeground(new java.awt.Color(113, 128, 150));
        lblDesc3.setText("Tu reclamo REC-001 para \"DNI Azul\" fue Aprobado.");
        panelContenido3.add(lblDesc3);

        lblTiempo3.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        lblTiempo3.setForeground(new java.awt.Color(160, 174, 192));
        lblTiempo3.setText("Ayer");
        panelContenido3.add(lblTiempo3);

        cardAviso3.add(panelContenido3, java.awt.BorderLayout.CENTER);

        panelListaAvisos.add(cardAviso3);

        scrollAvisos.setViewportView(panelListaAvisos);

        add(scrollAvisos, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizarAvisos();
    }//GEN-LAST:event_btnActualizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JPanel cardAviso1;
    private javax.swing.JPanel cardAviso2;
    private javax.swing.JPanel cardAviso3;
    private javax.swing.JLabel lblDesc1;
    private javax.swing.JLabel lblDesc2;
    private javax.swing.JLabel lblDesc3;
    private javax.swing.JLabel lblIcono1;
    private javax.swing.JLabel lblIcono2;
    private javax.swing.JLabel lblIcono3;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTiempo1;
    private javax.swing.JLabel lblTiempo2;
    private javax.swing.JLabel lblTiempo3;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTitulo2;
    private javax.swing.JLabel lblTitulo3;
    private javax.swing.JPanel panelCabecera;
    private javax.swing.JPanel panelContenido1;
    private javax.swing.JPanel panelContenido2;
    private javax.swing.JPanel panelContenido3;
    private javax.swing.JPanel panelIcono1;
    private javax.swing.JPanel panelIcono2;
    private javax.swing.JPanel panelIcono3;
    private javax.swing.JPanel panelListaAvisos;
    private javax.swing.JPanel panelTitulos;
    private javax.swing.JScrollPane scrollAvisos;
    private javax.swing.JPanel separador1;
    private javax.swing.JPanel separador2;
    // End of variables declaration//GEN-END:variables
}
