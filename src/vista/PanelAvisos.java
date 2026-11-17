package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Reclamo;
import modelo.Usuario;
import repositorio.UsuarioCsvRepositorio;
import servicio.CoincidenciaServicio;
import servicio.ObjetoServicio;
import servicio.ReclamoServicio;

public class PanelAvisos extends javax.swing.JPanel {

    private Estudiante usuarioActual;
    private ObjetoServicio objetoServicio;
    private CoincidenciaServicio coincidenciaServicio;
    private ReclamoServicio reclamoServicio;

    public PanelAvisos() {
        initComponents();
        if (jScrollPane1 != null && jScrollPane1.getVerticalScrollBar() != null) {
            jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        }
        this.objetoServicio = new ObjetoServicio();
        this.coincidenciaServicio = new CoincidenciaServicio();
        this.reclamoServicio = new ReclamoServicio();
        configurarHeaderAvisos();
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

    private void configurarHeaderAvisos() {
        if (panelSuperior == null) return;
        panelSuperior.removeAll();

        JPanel pnlHeaderFila = new JPanel(new BorderLayout());
        pnlHeaderFila.setBackground(Color.WHITE);

        JPanel pnlTitulos = new JPanel();
        pnlTitulos.setBackground(Color.WHITE);
        pnlTitulos.setLayout(new BoxLayout(pnlTitulos, BoxLayout.Y_AXIS));
        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(Box.createVerticalStrut(2));
        pnlTitulos.add(lblSubtitulo);

        JButton btnRefrescar = new JButton("🔄 Actualizar");
        btnRefrescar.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnRefrescar.setBackground(new Color(241, 245, 249));
        btnRefrescar.setForeground(new Color(37, 99, 235));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        btnRefrescar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> cargarAvisos());

        pnlHeaderFila.add(pnlTitulos, BorderLayout.CENTER);
        pnlHeaderFila.add(btnRefrescar, BorderLayout.EAST);

        panelSuperior.add(pnlHeaderFila);
        panelSuperior.revalidate();
        panelSuperior.repaint();
    }

    public void cargarAvisos() {
        if (panelContenedor == null) {
            return;
        }

        panelContenedor.removeAll();

        this.objetoServicio = new ObjetoServicio();
        this.reclamoServicio = new ReclamoServicio();
        this.coincidenciaServicio = new CoincidenciaServicio();

        int totalAvisos = 0;

        if (usuarioActual != null) {
            Usuario uFresh = new UsuarioCsvRepositorio().obtenerUsuarioPorIdentificacion(usuarioActual.getIdentificacion());
            if (uFresh instanceof Estudiante) {
                this.usuarioActual = (Estudiante) uFresh;
            }
            String miId = usuarioActual.getIdentificacion();

            // 1. RECLAMOS EN TUS OBJETOS REPORTADOS
            try {
                List<ObjetoReportado> misObjetos = objetoServicio.listarPorEstudiante(miId);
                for (ObjetoReportado obj : misObjetos) {
                    if (obj == null) continue;
                    String estado = obj.getEstado() != null ? obj.getEstado().trim().toUpperCase() : "ENCONTRADO";

                    if ("PENDIENTE".equals(estado)) {
                        List<Reclamo> reclamos = reclamoServicio.listarPorObjeto(obj.getId());
                        Reclamo recPendiente = null;
                        for (Reclamo r : reclamos) {
                            if ("PENDIENTE".equalsIgnoreCase(r.getEstado())) {
                                recPendiente = r;
                                break;
                            }
                        }

                        String infoReclamante = (recPendiente != null && recPendiente.getReclamante() != null)
                            ? " por el estudiante " + recPendiente.getReclamante().getNombre()
                            : "";

                        JPanel card = crearTarjetaAvisoInteractivo(
                            "⏳ RECLAMO PENDIENTE EN TU OBJETO HALLADO",
                            "Tu objeto notificado '" + obj.getNombre() + "' (" + obj.getCategoriaNombre() + ") tiene una solicitud de reclamo pendiente" + infoReclamante + ". Haz clic en 'Revisar' para validar sus respuestas.",
                            "PENDIENTE",
                            new Color(254, 243, 199), // Amber-100 bg
                            new Color(253, 230, 138), // Amber-200 border
                            new Color(180, 83, 9),    // Amber-700 fg
                            "Revisar Reclamo",
                            () -> abrirDialogoRevision(obj)
                        );
                        panelContenedor.add(card);
                        panelContenedor.add(Box.createVerticalStrut(12));
                        totalAvisos++;
                    } else if ("ENTREGADO".equals(estado)) {
                        JPanel card = crearTarjetaAvisoInteractivo(
                            "✅ OBJETO ENTREGADO CON ÉXITO",
                            "El objeto '" + obj.getNombre() + "' que reportaste en " + obj.getUbicacion() + " fue entregado a su dueño legítimo.",
                            "ENTREGADO",
                            new Color(243, 244, 246),
                            new Color(229, 231, 235),
                            new Color(75, 85, 99),
                            null, null
                        );
                        panelContenedor.add(card);
                        panelContenedor.add(Box.createVerticalStrut(12));
                        totalAvisos++;
                    } else { // ENCONTRADO
                        JPanel card = crearTarjetaAvisoInteractivo(
                            "🟢 OBJETO PUBLICADO EN CAMPUS",
                            "Tu reporte de '" + obj.getNombre() + "' (" + obj.getCategoriaNombre() + ") está disponible en el campus para que su dueño lo reclame.",
                            "DISPONIBLE",
                            new Color(240, 253, 244),
                            new Color(187, 247, 208),
                            new Color(21, 128, 61),
                            null, null
                        );
                        panelContenedor.add(card);
                        panelContenedor.add(Box.createVerticalStrut(12));
                        totalAvisos++;
                    }

                    // Coincidencias detectadas para este objeto
                    List<ObjetoReportado> matches = coincidenciaServicio.buscarCoincidencias(obj);
                    for (ObjetoReportado match : matches) {
                        String desc = "Se detectó coincidencia con: " + match.getNombre() + " (" + match.getCategoriaNombre() + ") reportado en " + match.getUbicacion() + ".";
                        JPanel card = crearTarjetaAvisoInteractivo(
                            "🎯 ¡COINCIDENCIA DETECTADA!",
                            desc,
                            "COINCIDENCIA",
                            new Color(238, 242, 255),
                            new Color(199, 210, 254),
                            new Color(67, 56, 202),
                            null, null
                        );
                        panelContenedor.add(card);
                        panelContenedor.add(Box.createVerticalStrut(12));
                        totalAvisos++;
                    }
                }
            } catch (Exception ignored) {
            }

            // 2. TUS RECLAMOS REALIZADOS
            try {
                List<Reclamo> misReclamos = reclamoServicio.listarPorReclamante(miId);
                for (Reclamo r : misReclamos) {
                    if (r == null) continue;
                    String nomObj = (r.getObjeto() != null) ? r.getObjeto().getNombre() : "Objeto";
                    String estado = (r.getEstado() != null) ? r.getEstado().trim().toUpperCase() : "PENDIENTE";

                    if ("PENDIENTE".equals(estado)) {
                        JPanel card = crearTarjetaAvisoInteractivo(
                            "⏳ TU RECLAMO ESTÁ PENDIENTE DE REVISIÓN",
                            "Tu reclamo " + r.getId() + " para el objeto '" + nomObj + "' está en estado PENDIENTE. Quien halló el objeto está revisando tus respuestas de comprobación.",
                            "PENDIENTE",
                            new Color(254, 243, 199),
                            new Color(253, 230, 138),
                            new Color(180, 83, 9),
                            null, null
                        );
                        panelContenedor.add(card);
                        panelContenedor.add(Box.createVerticalStrut(12));
                        totalAvisos++;
                    } else if ("APROBADO".equals(estado)) {
                        JPanel card = crearTarjetaAvisoInteractivo(
                            "🎉 ¡TU RECLAMO FUE APROBADO!",
                            "¡Felicidades! Tu reclamo " + r.getId() + " para el objeto '" + nomObj + "' fue APROBADO. El objeto ha sido validado como tuyo y está listo para entrega.",
                            "APROBADO",
                            new Color(240, 253, 244),
                            new Color(187, 247, 208),
                            new Color(21, 128, 61),
                            null, null
                        );
                        panelContenedor.add(card);
                        panelContenedor.add(Box.createVerticalStrut(12));
                        totalAvisos++;
                    } else if ("RECHAZADO".equals(estado)) {
                        String mot = (r.getObservacion() != null && !r.getObservacion().isBlank()) ? " Motivo: " + r.getObservacion() : "";
                        JPanel card = crearTarjetaAvisoInteractivo(
                            "❌ RECLAMO RECHAZADO",
                            "Tu reclamo " + r.getId() + " para el objeto '" + nomObj + "' fue RECHAZADO." + mot,
                            "RECHAZADO",
                            new Color(254, 242, 242),
                            new Color(254, 202, 202),
                            new Color(185, 28, 28),
                            null, null
                        );
                        panelContenedor.add(card);
                        panelContenedor.add(Box.createVerticalStrut(12));
                        totalAvisos++;
                    }
                }
            } catch (Exception ignored) {
            }

            // 3. MENSAJES DEL SISTEMA
            ArrayList<String> mensajes = usuarioActual.getMensajes();
            if (mensajes != null && !mensajes.isEmpty()) {
                for (int i = mensajes.size() - 1; i >= 0; i--) {
                    String msg = mensajes.get(i);
                    if (msg != null && !msg.isBlank() && !msg.equalsIgnoreCase("SIN_MENSAJES")) {
                        String tit = "📬 Mensaje del Sistema";
                        Color bg = new Color(248, 250, 252);
                        Color border = new Color(226, 232, 240);
                        Color fg = new Color(51, 65, 85);

                        if (msg.contains("APROBADO")) {
                            tit = "🎉 Notificación de Aprobación";
                            bg = new Color(240, 253, 244);
                            border = new Color(187, 247, 208);
                            fg = new Color(21, 128, 61);
                        } else if (msg.contains("RECHAZADO")) {
                            tit = "❌ Notificación de Rechazo";
                            bg = new Color(254, 242, 242);
                            border = new Color(254, 202, 202);
                            fg = new Color(185, 28, 28);
                        } else if (msg.contains("PENDIENTE")) {
                            tit = "⏳ Notificación de Reclamo Pendiente";
                            bg = new Color(254, 243, 199);
                            border = new Color(253, 230, 138);
                            fg = new Color(180, 83, 9);
                        }

                        JPanel card = crearTarjetaAvisoInteractivo(tit, msg, "MENSAJE", bg, border, fg, null, null);
                        panelContenedor.add(card);
                        panelContenedor.add(Box.createVerticalStrut(12));
                        totalAvisos++;
                    }
                }
            }
        }

        if (totalAvisos == 0) {
            JLabel lblVacio = new JLabel("No hay avisos pendientes por el momento.");
            lblVacio.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblVacio.setForeground(new Color(153, 153, 153));
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenedor.add(Box.createVerticalStrut(40));
            panelContenedor.add(lblVacio);
        }

        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    private JPanel crearTarjetaAvisoInteractivo(String titulo, String descripcion, String badgeTexto, Color bg, Color border, Color fg, String btnTexto, Runnable btnAccion) {
        JPanel subpanel = new JPanel();
        subpanel.setBackground(bg != null ? bg : Color.WHITE);
        subpanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(border != null ? border : new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        subpanel.setLayout(new BorderLayout(12, 0));
        subpanel.setMaximumSize(new Dimension(32767, 85));
        subpanel.setPreferredSize(new Dimension(440, 85));

        JPanel pnlInfo = new JPanel();
        pnlInfo.setBackground(bg != null ? bg : Color.WHITE);
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTit.setForeground(fg != null ? fg : new Color(51, 51, 51));
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><body style='width: 300px;'>" + descripcion + "</body></html>");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(75, 85, 99));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlInfo.add(lblTit);
        pnlInfo.add(Box.createVerticalStrut(4));
        pnlInfo.add(lblDesc);

        subpanel.add(pnlInfo, BorderLayout.CENTER);

        if (btnTexto != null && btnAccion != null) {
            JPanel pnlBtn = new JPanel(new java.awt.GridBagLayout());
            pnlBtn.setBackground(bg != null ? bg : Color.WHITE);

            JButton btn = new JButton(btnTexto);
            btn.setFont(new Font("SansSerif", Font.BOLD, 11));
            btn.setBackground(new Color(217, 119, 6)); // Amber-600
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 83, 9), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> btnAccion.run());

            pnlBtn.add(btn);
            subpanel.add(pnlBtn, BorderLayout.EAST);
        }

        return subpanel;
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
        sb.append("Objeto: ").append(obj.getNombre()).append(" (").append(obj.getCategoriaNombre()).append(")\n");
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
                cargarAvisos();
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
                cargarAvisos();
            }
        }
    }

    public void agregarAviso(String titulo, String descripcion, String tiempo) {
        if (panelContenedor == null) {
            return;
        }
        panelContenedor.add(crearTarjetaAvisoInteractivo(titulo, descripcion, "AVISO", Color.WHITE, new Color(220, 220, 220), new Color(51, 51, 51), null, null), 0);
        panelContenedor.add(Box.createVerticalStrut(12), 1);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

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
