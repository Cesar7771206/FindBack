package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;
import servicio.ObjetoServicio;
import servicio.ReclamoServicio;

public class PanelInicio extends javax.swing.JPanel {

    private frmPrincipal ventana;
    private PanelApp panelApp;
    private Estudiante usuarioActual;
    private ObjetoServicio objetoServicio;
    private ReclamoServicio reclamoServicio;

    private String filtroActual = "TODOS";
    private String textoBusqueda = "";
    private JTextField txtBusqueda;
    private JButton btnFiltroTodos;
    private JButton btnFiltroDisponibles;
    private JButton btnFiltroPendientes;
    private JPanel pnlBusquedaFiltros;

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
        this.reclamoServicio = new ReclamoServicio();
        inicializarBusquedaYFiltros();
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

    private void inicializarBusquedaYFiltros() {
        if (panelContenedor == null) return;

        pnlBusquedaFiltros = new JPanel();
        pnlBusquedaFiltros.setBackground(Color.WHITE);
        pnlBusquedaFiltros.setLayout(new BoxLayout(pnlBusquedaFiltros, BoxLayout.Y_AXIS));
        pnlBusquedaFiltros.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlBusquedaFiltros.setMaximumSize(new Dimension(32767, 80));
        pnlBusquedaFiltros.setPreferredSize(new Dimension(440, 80));

        txtBusqueda = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(new Color(156, 163, 175));
                    g2.setFont(getFont().deriveFont(Font.ITALIC, 12f));
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("🔍 Buscar por nombre, categoría, lugar o color...", getInsets().left + 4, y);
                    g2.dispose();
                }
            }
        };
        txtBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) { txtBusqueda.repaint(); }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) { txtBusqueda.repaint(); }
        });
        txtBusqueda.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtBusqueda.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtBusqueda.setForeground(new Color(51, 65, 85));
        txtBusqueda.setBackground(new Color(248, 250, 252));
        txtBusqueda.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        txtBusqueda.setMaximumSize(new Dimension(32767, 34));
        txtBusqueda.setPreferredSize(new Dimension(440, 34));
        txtBusqueda.setToolTipText("Escribe para filtrar por nombre, lugar, categoría o color");

        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
            private void filtrar() {
                textoBusqueda = txtBusqueda.getText().trim();
                cargarObjetos();
            }
        });

        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        pnlFiltros.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlFiltros.setBackground(Color.WHITE);
        pnlFiltros.setMaximumSize(new Dimension(32767, 36));
        pnlFiltros.setPreferredSize(new Dimension(440, 36));
        pnlFiltros.setBorder(BorderFactory.createEmptyBorder(0, -6, 0, 0));

        btnFiltroTodos = crearBotonFiltro("Todos", "TODOS");
        btnFiltroDisponibles = crearBotonFiltro("🟢 Disponibles", "DISPONIBLES");
        btnFiltroPendientes = crearBotonFiltro("⏳ En Reclamo (Pendientes)", "PENDIENTES");

        pnlFiltros.add(btnFiltroTodos);
        pnlFiltros.add(btnFiltroDisponibles);
        pnlFiltros.add(btnFiltroPendientes);

        pnlBusquedaFiltros.add(txtBusqueda);
        pnlBusquedaFiltros.add(Box.createVerticalStrut(6));
        pnlBusquedaFiltros.add(pnlFiltros);

        int idx = panelContenedor.getComponentZOrder(panelListaObjetos);
        if (idx >= 0) {
            panelContenedor.add(pnlBusquedaFiltros, idx);
            panelContenedor.add(Box.createVerticalStrut(10), idx + 1);
        } else {
            panelContenedor.add(pnlBusquedaFiltros);
        }

        actualizarEstiloFiltros();
    }

    private JButton crearBotonFiltro(String texto, String filtro) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            this.filtroActual = filtro;
            actualizarEstiloFiltros();
            cargarObjetos();
        });
        return btn;
    }

    private void actualizarEstiloFiltros() {
        configurarBotonPill(btnFiltroTodos, "TODOS".equalsIgnoreCase(filtroActual));
        configurarBotonPill(btnFiltroDisponibles, "DISPONIBLES".equalsIgnoreCase(filtroActual));
        configurarBotonPill(btnFiltroPendientes, "PENDIENTES".equalsIgnoreCase(filtroActual));
    }

    private void configurarBotonPill(JButton btn, boolean seleccionado) {
        if (btn == null) return;
        if (seleccionado) {
            btn.setBackground(new Color(37, 99, 235));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 11));
            btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(37, 99, 235), 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
            ));
        } else {
            btn.setBackground(new Color(241, 245, 249));
            btn.setForeground(new Color(71, 85, 105));
            btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
            btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
            ));
        }
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
            String car = usuarioActual.getCarreraNombre();
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
        this.reclamoServicio = new ReclamoServicio();

        List<ObjetoReportado> listaObjetos = null;
        try {
            listaObjetos = objetoServicio.listarTodos();
        } catch (Exception ignored) {
        }

        int mostrados = 0;
        if (listaObjetos != null) {
            for (ObjetoReportado obj : listaObjetos) {
                if (obj == null) {
                    continue;
                }

                String st = (obj.getEstado() != null) ? obj.getEstado().trim().toUpperCase() : "ENCONTRADO";
                if ("DISPONIBLES".equals(filtroActual) && !"ENCONTRADO".equals(st)) {
                    continue;
                }
                if ("PENDIENTES".equals(filtroActual) && !"PENDIENTE".equals(st)) {
                    continue;
                }

                if (textoBusqueda != null && !textoBusqueda.isBlank()) {
                    String q = textoBusqueda.toLowerCase().trim();
                    String nom = obj.getNombre() != null ? obj.getNombre().toLowerCase() : "";
                    String cat = obj.getCategoria() != null ? obj.getCategoria().toLowerCase() : "";
                    String ubi = obj.getUbicacion() != null ? obj.getUbicacion().toLowerCase() : "";
                    String col = obj.getColor() != null ? obj.getColor().toLowerCase() : "";
                    if (!nom.contains(q) && !cat.contains(q) && !ubi.contains(q) && !col.contains(q)) {
                        continue;
                    }
                }

                JPanel tarjeta = crearTarjetaObjeto(obj);
                panelListaObjetos.add(tarjeta);
                panelListaObjetos.add(Box.createVerticalStrut(10));
                mostrados++;
            }
        }

        if (mostrados == 0) {
            JLabel lblVacio = new JLabel(
                (textoBusqueda != null && !textoBusqueda.isBlank()) 
                    ? "No se encontraron objetos que coincidan con \"" + textoBusqueda + "\"."
                    : "No hay objetos reportados en esta sección por el momento."
            );
            lblVacio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblVacio.setForeground(new Color(153, 153, 153));
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelListaObjetos.add(Box.createVerticalStrut(24));
            panelListaObjetos.add(lblVacio);
        }

        panelListaObjetos.revalidate();
        panelListaObjetos.repaint();
    }

    private JPanel crearTarjetaObjeto(ObjetoReportado obj) {
        JPanel subpanel = new JPanel();
        subpanel.setBackground(Color.WHITE);
        subpanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        subpanel.setLayout(new BorderLayout(12, 0));
        subpanel.setMaximumSize(new Dimension(32767, 98));
        subpanel.setPreferredSize(new Dimension(440, 98));

        String estado = (obj.getEstado() != null) ? obj.getEstado().trim().toUpperCase() : "ENCONTRADO";
        boolean esMio = (usuarioActual != null && obj.getReportadoPor() != null &&
            usuarioActual.getIdentificacion().equalsIgnoreCase(obj.getReportadoPor().getIdentificacion()));
        boolean yoLoReclame = false;
        if (usuarioActual != null && reclamoServicio != null) {
            yoLoReclame = reclamoServicio.existeReclamoPendiente(obj.getId(), usuarioActual.getIdentificacion());
        }

        JPanel pnlInfo = new JPanel();
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));

        // Badges row
        JPanel pnlBadges = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pnlBadges.setBackground(Color.WHITE);
        pnlBadges.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Category Badge
        String catNom = obj.getCategoria() != null ? obj.getCategoria().trim().toUpperCase() : "GENERAL";
        JLabel lblCat = crearBadgeCategoria(catNom);
        pnlBadges.add(lblCat);

        // Status Badge
        JLabel lblStatus = crearBadgeEstado(estado);
        pnlBadges.add(lblStatus);

        if (esMio) {
            JLabel lblMio = crearBadgePill("👤 Notificado por ti", new Color(238, 242, 255), new Color(55, 48, 163), new Color(199, 210, 254));
            pnlBadges.add(lblMio);
        } else if (yoLoReclame) {
            JLabel lblReclamado = crearBadgePill("🙋 Tu reclamo pendiente", new Color(252, 231, 243), new Color(157, 23, 77), new Color(251, 207, 232));
            pnlBadges.add(lblReclamado);
        }

        // Title
        JLabel lblNombre = new JLabel(obj.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNombre.setForeground(new Color(17, 24, 39));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Details
        String ubicacion = (obj.getUbicacion() != null && !obj.getUbicacion().isBlank()) ? obj.getUbicacion() : "Campus";
        String fecha = (obj.getFecha() != null && !obj.getFecha().isBlank()) ? obj.getFecha() : "Reciente";
        String color = (obj.getColor() != null && !obj.getColor().isBlank() && !"No especificado".equalsIgnoreCase(obj.getColor())) ? obj.getColor() : "";
        String detalleTxt = "📍 " + ubicacion + "  •  📅 " + fecha + (color.isEmpty() ? "" : "  •  🎨 " + color);

        JLabel lblDetalle = new JLabel(detalleTxt);
        lblDetalle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDetalle.setForeground(new Color(100, 116, 139));
        lblDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlInfo.add(pnlBadges);
        pnlInfo.add(Box.createVerticalStrut(3));
        pnlInfo.add(lblNombre);
        pnlInfo.add(Box.createVerticalStrut(3));
        pnlInfo.add(lblDetalle);

        // Button Panel
        JPanel pnlBtn = new JPanel(new java.awt.GridBagLayout());
        pnlBtn.setBackground(Color.WHITE);

        JButton btnAccion = new JButton();
        btnAccion.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnAccion.setFocusPainted(false);
        btnAccion.setPreferredSize(new Dimension(116, 32));

        if ("PENDIENTE".equals(estado)) {
            if (esMio) {
                btnAccion.setText("Revisar Reclamo");
                btnAccion.setBackground(new Color(217, 119, 6)); // Amber-600
                btnAccion.setForeground(Color.WHITE);
                btnAccion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnAccion.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(180, 83, 9), 1, true),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)
                ));
                btnAccion.addActionListener(e -> abrirDialogoRevision(obj));
            } else if (yoLoReclame) {
                btnAccion.setText("Tu Reclamo");
                btnAccion.setBackground(new Color(254, 243, 199));
                btnAccion.setForeground(new Color(146, 64, 14));
                btnAccion.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(253, 230, 138), 1, true),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)
                ));
                btnAccion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnAccion.addActionListener(e -> JOptionPane.showMessageDialog(this,
                    "Tu reclamo para este objeto está en estado PENDIENTE de revisión.\nPuedes seguir las novedades en la pestaña Avisos.",
                    "Reclamo Pendiente",
                    JOptionPane.INFORMATION_MESSAGE));
            } else {
                btnAccion.setText("En Revisión");
                btnAccion.setBackground(new Color(241, 245, 249));
                btnAccion.setForeground(new Color(148, 163, 184));
                btnAccion.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(226, 232, 240), 1, true),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)
                ));
                btnAccion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnAccion.addActionListener(e -> JOptionPane.showMessageDialog(this,
                    "Este objeto ya cuenta con un reclamo en proceso de revisión y validación de propiedad.\nNo puede ser reclamado por otro estudiante mientras esté pendiente.",
                    "Objeto en Revisión",
                    JOptionPane.WARNING_MESSAGE));
            }
        } else if ("ENTREGADO".equals(estado)) {
            btnAccion.setText("Entregado");
            btnAccion.setBackground(new Color(243, 244, 246));
            btnAccion.setForeground(new Color(156, 163, 175));
            btnAccion.setBorder(new LineBorder(new Color(229, 231, 235), 1, true));
            btnAccion.setEnabled(false);
        } else { // ENCONTRADO
            if (esMio) {
                btnAccion.setText("Tú lo hallaste");
                btnAccion.setBackground(new Color(248, 250, 252));
                btnAccion.setForeground(new Color(100, 116, 139));
                btnAccion.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
                btnAccion.setEnabled(false);
            } else {
                btnAccion.setText("Reclamar");
                btnAccion.setBackground(new Color(37, 99, 235));
                btnAccion.setForeground(Color.WHITE);
                btnAccion.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(29, 78, 216), 1, true),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
                btnAccion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnAccion.addActionListener(e -> {
                    if (panelApp != null) {
                        if (panelApp.getPanelReclamar() != null) {
                            panelApp.getPanelReclamar().setObjetoSeleccionado(obj);
                        }
                        panelApp.mostrar("reclamar");
                    }
                });
            }
        }

        pnlBtn.add(btnAccion);

        subpanel.add(pnlInfo, BorderLayout.CENTER);
        subpanel.add(pnlBtn, BorderLayout.EAST);

        return subpanel;
    }

    private JLabel crearBadgeCategoria(String categoria) {
        String texto;
        Color bg, fg, border;
        switch (categoria) {
            case "PRENDA" -> {
                texto = "👕 Prenda";
                bg = new Color(243, 232, 255);
                fg = new Color(107, 33, 168);
                border = new Color(233, 213, 255);
            }
            case "DISPOSITIVO ELECTRONICO", "DISPOSITIVOELECTRONICO" -> {
                texto = "📱 Dispositivo";
                bg = new Color(219, 234, 254);
                fg = new Color(30, 64, 175);
                border = new Color(191, 219, 254);
            }
            case "DOCUMENTO" -> {
                texto = "📄 Documento";
                bg = new Color(255, 237, 213);
                fg = new Color(194, 65, 12);
                border = new Color(254, 215, 170);
            }
            default -> {
                texto = "📦 General";
                bg = new Color(241, 245, 249);
                fg = new Color(51, 65, 85);
                border = new Color(226, 232, 240);
            }
        }
        return crearBadgePill(texto, bg, fg, border);
    }

    private JLabel crearBadgeEstado(String estado) {
        if ("PENDIENTE".equalsIgnoreCase(estado)) {
            return crearBadgePill("⏳ En Revisión (Pendiente)", new Color(254, 243, 199), new Color(180, 83, 9), new Color(253, 230, 138));
        } else if ("ENTREGADO".equalsIgnoreCase(estado)) {
            return crearBadgePill("✅ Entregado", new Color(243, 244, 246), new Color(75, 85, 99), new Color(229, 231, 235));
        } else {
            return crearBadgePill("🟢 Disponible", new Color(220, 252, 231), new Color(21, 128, 61), new Color(134, 239, 172));
        }
    }

    private JLabel crearBadgePill(String texto, Color bg, Color fg, Color border) {
        JLabel lbl = new JLabel(" " + texto + " ");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(fg);
        lbl.setBackground(bg);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(border, 1, true),
            BorderFactory.createEmptyBorder(1, 3, 1, 3)
        ));
        return lbl;
    }

    public void abrirDialogoRevision(ObjetoReportado obj) {
        if (obj == null) return;
        if (reclamoServicio == null) {
            reclamoServicio = new ReclamoServicio();
        }
        List<Reclamo> reclamos = reclamoServicio.listarPorObjeto(obj.getId());
        Reclamo pendiente = null;
        for (Reclamo r : reclamos) {
            if ("PENDIENTE".equalsIgnoreCase(r.getEstado())) {
                pendiente = r;
                break;
            }
        }

        if (pendiente == null) {
            JOptionPane.showMessageDialog(this,
                "No hay reclamos pendientes activos para este objeto.",
                "Sin Reclamos Pendientes",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Reclamo r = pendiente;
        Estudiante reclamante = r.getReclamante();
        String nombreReclamante = (reclamante != null) ? reclamante.getNombre() : "Estudiante";
        String dniReclamante = (reclamante != null) ? reclamante.getIdentificacion() : "No especificado";
        String carreraReclamante = (reclamante != null && !reclamante.getCarreraNombre().isBlank()) ? reclamante.getCarreraNombre() : "Campus UDEP";

        String[] preguntas = obj.preguntasVerificacion();
        String[] datosReales = obj.datosVerificables();
        ArrayList<String> respuestas = r.getRespuestas();

        StringBuilder sb = new StringBuilder();
        sb.append("📋 VALIDACIÓN DE RECLAMO: ").append(r.getId()).append("\n");
        sb.append("────────────────────────────────────────\n");
        sb.append("Objeto: ").append(obj.getNombre()).append(" (").append(obj.getCategoria()).append(")\n");
        sb.append("Lugar hallado: ").append(obj.getUbicacion()).append(" | Color: ").append(obj.getColor()).append("\n\n");
        sb.append("👤 DATOS DEL RECLAMANTE:\n");
        sb.append("• Nombre: ").append(nombreReclamante).append("\n");
        sb.append("• DNI: ").append(dniReclamante).append("\n");
        sb.append("• Carrera: ").append(carreraReclamante).append("\n\n");
        sb.append("🔍 COMPROBACIÓN DE PREGUNTAS:\n");

        if (preguntas != null && datosReales != null && respuestas != null) {
            for (int i = 0; i < preguntas.length; i++) {
                String preg = preguntas[i];
                String real = (i < datosReales.length) ? datosReales[i] : "N/A";
                String resp = (i < respuestas.size()) ? respuestas.get(i) : "N/A";
                boolean match = real.trim().equalsIgnoreCase(resp.trim()) ||
                                real.toLowerCase().contains(resp.toLowerCase().trim()) ||
                                resp.toLowerCase().contains(real.toLowerCase().trim());
                sb.append((i + 1)).append(") ").append(preg).append("\n");
                sb.append("    • Respuesta dada: \"").append(resp).append("\"\n");
                sb.append("    • Dato registrado: \"").append(real).append("\"\n");
                sb.append("    • Coincidencia: ").append(match ? "✅ COINCIDE" : "❌ NO COINCIDE").append("\n\n");
            }
        }

        int coincidencias = reclamoServicio.contarCoincidencias(r.getId());
        double pct = reclamoServicio.calcularPorcentajeCoincidencia(r.getId());
        sb.append("📊 Resumen: ").append(String.format("%.0f%% de acierto (%d coincidencias)", pct, Math.max(0, coincidencias))).append("\n");
        sb.append("────────────────────────────────────────\n");
        sb.append("¿Deseas APROBAR la entrega del objeto o RECHAZAR el reclamo?");

        Object[] opciones = {"Aprobar y Entregar", "Rechazar Reclamo", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(
            this,
            sb.toString(),
            "Revisar Reclamo - FindBack",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        String revisorId = (usuarioActual != null) ? usuarioActual.getIdentificacion() : "USUARIO";
        if (seleccion == 0) { // Aprobar
            boolean ok = reclamoServicio.aprobarReclamo(r.getId(), revisorId, "Validado y aprobado por quien reportó el hallazgo.");
            if (ok) {
                JOptionPane.showMessageDialog(this,
                    "¡Reclamo " + r.getId() + " APROBADO con éxito!\nEl objeto ha sido marcado como ENTREGADO y se notificó al reclamante.",
                    "Reclamo Aprobado",
                    JOptionPane.INFORMATION_MESSAGE);
                cargarObjetos();
                if (panelApp != null && panelApp.getPanelAvisos() != null) {
                    panelApp.getPanelAvisos().cargarAvisos();
                }
            }
        } else if (seleccion == 1) { // Rechazar
            String motivo = JOptionPane.showInputDialog(this,
                "Ingresa el motivo del rechazo del reclamo:",
                "Motivo de rechazo",
                JOptionPane.QUESTION_MESSAGE);
            if (motivo == null) {
                return;
            }
            if (motivo.isBlank()) {
                motivo = "Las respuestas de verificación no coinciden con las características del objeto.";
            }
            boolean ok = reclamoServicio.rechazarReclamo(r.getId(), revisorId, motivo);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                    "El reclamo " + r.getId() + " ha sido RECHAZADO.\nEl objeto vuelve a estar DISPONIBLE para reclamos en el campus.",
                    "Reclamo Rechazado",
                    JOptionPane.INFORMATION_MESSAGE);
                cargarObjetos();
                if (panelApp != null && panelApp.getPanelAvisos() != null) {
                    panelApp.getPanelAvisos().cargarAvisos();
                }
            }
        }
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
            if (panelApp.getPanelReclamar() != null) {
                panelApp.getPanelReclamar().limpiarFormulario();
            }
            panelApp.mostrar("reclamar");
        }
    }//GEN-LAST:event_btnReclamarActionPerformed

    private void btnNotificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificarActionPerformed
        if (panelApp != null) {
            if (panelApp.getPanelNotificar() != null) {
                panelApp.getPanelNotificar().limpiarFormulario();
            }
            panelApp.mostrar("notificar");
        }
    }//GEN-LAST:event_btnNotificarActionPerformed

    private void btnBuscarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarTodosActionPerformed
        if (txtBusqueda != null) {
            txtBusqueda.setText("");
        }
        textoBusqueda = "";
        filtroActual = "TODOS";
        actualizarEstiloFiltros();
        cargarObjetos();
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
