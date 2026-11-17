package vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
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
import modelo.Categoria;
import modelo.Estudiante;
import modelo.DispositivoElectronico;
import modelo.Documento;
import modelo.ObjetoGenerico;
import modelo.ObjetoReportado;
import modelo.Prenda;
import repositorio.UsuarioCsvRepositorio;
import servicio.CoincidenciaServicio;
import servicio.ObjetoServicio;
import servicio.ReclamoServicio;

public class PanelReclamar extends javax.swing.JPanel {

    private frmPrincipal ventana;
    private PanelApp panelApp;
    private Estudiante usuarioActual;
    private ObjetoServicio objetoServicio;
    private ReclamoServicio reclamoServicio;
    private CoincidenciaServicio coincidenciaServicio;
    private ObjetoReportado objetoObjetivo;

    private javax.swing.JPanel pnlCamposDinamicos;
    private javax.swing.JTextField txtTalla;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtSerie;
    private javax.swing.JTextField txtTipoDoc;
    private javax.swing.JTextField txtTitular;
    private javax.swing.JTextField txtDetalleExtra;

    public PanelReclamar() {
        initComponents();
        if (jScrollPane1 != null && jScrollPane1.getVerticalScrollBar() != null) {
            jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        }
        this.objetoServicio = new ObjetoServicio();
        this.reclamoServicio = new ReclamoServicio();
        this.coincidenciaServicio = new CoincidenciaServicio();
        this.txtFecha.setText(LocalDate.now().toString());
        cargarCategorias();
        cargarUbicaciones();
        inicializarCamposDinamicos();
        actualizarCamposDinamicos();
        cbxCategoria.addActionListener(evt -> actualizarCamposDinamicos());
    }

    private void cargarCategorias() {
        cbxCategoria.removeAllItems();
        for (modelo.Categoria c : modelo.Categoria.values()) {
            cbxCategoria.addItem(c.getNombre());
        }
    }

    private void cargarUbicaciones() {
        cbxLugar.removeAllItems();
        for (modelo.Ubicacion u : modelo.Ubicacion.values()) {
            cbxLugar.addItem(u.getNombre());
        }
    }

    private JTextField crearTextField() {
        JTextField txt = new JTextField();
        txt.setAlignmentX(0.0f);
        txt.setBackground(new Color(249, 250, 251));
        txt.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(209, 213, 219), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        txt.setMaximumSize(new Dimension(32767, 36));
        txt.setPreferredSize(new Dimension(440, 36));
        return txt;
    }

    private JLabel crearEtiquetaCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setAlignmentX(0.0f);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(75, 85, 99));
        return lbl;
    }

    private JPanel crearEspacio(int h) {
        JPanel p = new JPanel();
        p.setAlignmentX(0.0f);
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(32767, h));
        p.setPreferredSize(new Dimension(440, h));
        return p;
    }

    private void inicializarCamposDinamicos() {
        txtTalla = crearTextField();
        txtMarca = crearTextField();
        txtModelo = crearTextField();
        txtSerie = crearTextField();
        txtTipoDoc = crearTextField();
        txtTitular = crearTextField();
        txtDetalleExtra = crearTextField();

        pnlCamposDinamicos = new JPanel();
        pnlCamposDinamicos.setAlignmentX(0.0f);
        pnlCamposDinamicos.setBackground(Color.WHITE);
        pnlCamposDinamicos.setLayout(new BoxLayout(pnlCamposDinamicos, BoxLayout.Y_AXIS));

        if (lblDatoPrivado != null) lblDatoPrivado.setVisible(false);
        if (txtDatoPrivado != null) txtDatoPrivado.setVisible(false);

        int idx = panelContenedor.getComponentZOrder(lblDatoPrivado);
        if (idx >= 0) {
            panelContenedor.add(pnlCamposDinamicos, idx);
        } else {
            panelContenedor.add(pnlCamposDinamicos);
        }

        JButton btnCancelar = new JButton("Cancelar y Volver al Inicio");
        btnCancelar.setAlignmentX(0.0f);
        btnCancelar.setBackground(new Color(248, 250, 252));
        btnCancelar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnCancelar.setForeground(new Color(100, 116, 139));
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setMaximumSize(new Dimension(32767, 36));
        btnCancelar.setPreferredSize(new Dimension(440, 36));
        btnCancelar.addActionListener(e -> {
            limpiarFormulario();
            if (panelApp != null) {
                panelApp.mostrar("inicio");
            }
        });

        int btnIdx = panelContenedor.getComponentZOrder(btnReclamar);
        if (btnIdx >= 0) {
            panelContenedor.add(Box.createVerticalStrut(8), btnIdx + 1);
            panelContenedor.add(btnCancelar, btnIdx + 2);
        } else {
            panelContenedor.add(btnCancelar);
        }
    }

    private void actualizarCamposDinamicos() {
        if (pnlCamposDinamicos == null) {
            return;
        }
        pnlCamposDinamicos.removeAll();

        String cat = (String) cbxCategoria.getSelectedItem();
        if (cat == null) {
            cat = "Documento";
        }

        switch (cat) {
            case "Prenda" -> {
                pnlCamposDinamicos.add(crearEtiquetaCampo("Pregunta 1: Talla de la prenda (S, M, L, XL, etc.) *"));
                pnlCamposDinamicos.add(txtTalla);
                pnlCamposDinamicos.add(crearEspacio(12));
                pnlCamposDinamicos.add(crearEtiquetaCampo("Pregunta 2: Marca de la prenda *"));
                pnlCamposDinamicos.add(txtMarca);
                pnlCamposDinamicos.add(crearEspacio(12));
            }
            case "Dispositivo Electronico" -> {
                pnlCamposDinamicos.add(crearEtiquetaCampo("Pregunta 1: Marca del dispositivo *"));
                pnlCamposDinamicos.add(txtMarca);
                pnlCamposDinamicos.add(crearEspacio(12));
                pnlCamposDinamicos.add(crearEtiquetaCampo("Pregunta 2: Modelo exacto del dispositivo *"));
                pnlCamposDinamicos.add(txtModelo);
                pnlCamposDinamicos.add(crearEspacio(12));
                pnlCamposDinamicos.add(crearEtiquetaCampo("Pregunta 3: Número de serie o detalle único identificable *"));
                pnlCamposDinamicos.add(txtSerie);
                pnlCamposDinamicos.add(crearEspacio(12));
            }
            case "Documento" -> {
                pnlCamposDinamicos.add(crearEtiquetaCampo("Pregunta 1: Tipo de documento (DNI, Carné, Licencia, etc.) *"));
                pnlCamposDinamicos.add(txtTipoDoc);
                pnlCamposDinamicos.add(crearEspacio(12));
                pnlCamposDinamicos.add(crearEtiquetaCampo("Pregunta 2: Nombre completo del titular en el documento *"));
                pnlCamposDinamicos.add(txtTitular);
                pnlCamposDinamicos.add(crearEspacio(12));
            }
            default -> {
                pnlCamposDinamicos.add(crearEtiquetaCampo("Pregunta: Característica o detalle distintivo (prueba de dueño) *"));
                pnlCamposDinamicos.add(txtDetalleExtra);
                pnlCamposDinamicos.add(crearEspacio(12));
            }
        }

        pnlCamposDinamicos.revalidate();
        pnlCamposDinamicos.repaint();
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    public PanelReclamar(frmPrincipal ventana, PanelApp panelApp) {
        this();
        this.ventana = ventana;
        this.panelApp = panelApp;
    }

    public void setUsuarioActual(Estudiante estudiante) {
        this.usuarioActual = estudiante;
    }

    public void setObjetoSeleccionado(ObjetoReportado obj) {
        limpiarFormulario();
        this.objetoObjetivo = obj;
        if (reclamoServicio == null) {
            reclamoServicio = new ReclamoServicio();
        }
        if (obj != null) {
            txtNombre.setText(obj.getNombre());
            for (int i = 0; i < cbxCategoria.getItemCount(); i++) {
                if (cbxCategoria.getItemAt(i).equalsIgnoreCase(obj.getCategoriaNombre())) {
                    cbxCategoria.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < cbxLugar.getItemCount(); i++) {
                if (cbxLugar.getItemAt(i).equalsIgnoreCase(obj.getUbicacion())) {
                    cbxLugar.setSelectedIndex(i);
                    break;
                }
            }
            if (obj.getColor() != null && !obj.getColor().isBlank()) {
                txtColor.setText(obj.getColor());
            } else {
                txtColor.setText("");
            }
            if (obj.getFecha() != null && !obj.getFecha().isBlank()) {
                txtFecha.setText(obj.getFecha());
            } else {
                txtFecha.setText(LocalDate.now().toString());
            }
            txtDescripcion.setText("");

            String st = (obj.getEstado() != null) ? obj.getEstado().trim().toUpperCase() : "ENCONTRADO";
            if ("PENDIENTE".equals(st) || reclamoServicio.tieneReclamoPendiente(obj.getId())) {
                lblSubtitulo.setText("⚠️ Objeto en revisión: ya cuenta con un reclamo pendiente. No es posible reclamarlo nuevamente.");
                lblSubtitulo.setForeground(new Color(180, 83, 9));
                btnReclamar.setEnabled(false);
                btnReclamar.setText("Reclamo ya en proceso");
            } else if (usuarioActual != null && obj.getReportadoPor() != null &&
                usuarioActual.getIdentificacion().equalsIgnoreCase(obj.getReportadoPor().getIdentificacion())) {
                lblSubtitulo.setText("⚠️ Tú reportaste este objeto como hallado. No puedes reclamarlo con tu propia cuenta.");
                lblSubtitulo.setForeground(new Color(220, 38, 38));
                btnReclamar.setEnabled(false);
                btnReclamar.setText("No puedes reclamar tu propio reporte");
            } else if (usuarioActual != null && reclamoServicio.existeReclamoPendiente(obj.getId(), usuarioActual.getIdentificacion())) {
                lblSubtitulo.setText("ℹ️ Ya registraste un reclamo para este objeto y está PENDIENTE. Consulta la pestaña Avisos.");
                lblSubtitulo.setForeground(new Color(37, 99, 235));
                btnReclamar.setEnabled(false);
                btnReclamar.setText("Reclamo ya registrado");
            } else {
                lblSubtitulo.setText("Reclamando objeto hallado: " + obj.getNombre() + " (" + obj.getCategoriaNombre() + " en " + obj.getUbicacion() + ")");
                lblSubtitulo.setForeground(new Color(37, 99, 235));
                btnReclamar.setEnabled(true);
                btnReclamar.setText("Confirmar Reclamo");
            }
            actualizarCamposDinamicos();
        } else {
            lblSubtitulo.setText("Registro de datos del objeto perdido o reclamo de hallazgo");
            lblSubtitulo.setForeground(new Color(102, 102, 102));
            btnReclamar.setEnabled(true);
            btnReclamar.setText("Confirmar Reclamo");
            actualizarCamposDinamicos();
        }
    }

    public void cargarDatos() {
        if (objetoObjetivo != null) {
            setObjetoSeleccionado(objetoObjetivo);
        } else {
            limpiarFormulario();
        }
    }

    public void limpiarFormulario() {
        this.objetoObjetivo = null;
        if (txtNombre != null) txtNombre.setText("");
        if (cbxCategoria != null && cbxCategoria.getItemCount() > 0) cbxCategoria.setSelectedIndex(0);
        if (cbxLugar != null && cbxLugar.getItemCount() > 0) cbxLugar.setSelectedIndex(0);
        if (txtFecha != null) txtFecha.setText(LocalDate.now().toString());
        if (txtColor != null) txtColor.setText("");
        if (txtDescripcion != null) txtDescripcion.setText("");
        if (txtDatoPrivado != null) txtDatoPrivado.setText("");
        if (txtTalla != null) txtTalla.setText("");
        if (txtMarca != null) txtMarca.setText("");
        if (txtModelo != null) txtModelo.setText("");
        if (txtSerie != null) txtSerie.setText("");
        if (txtTipoDoc != null) txtTipoDoc.setText("");
        if (txtTitular != null) txtTitular.setText("");
        if (txtDetalleExtra != null) txtDetalleExtra.setText("");
        if (lblSubtitulo != null) {
            lblSubtitulo.setText("Registro de datos del objeto perdido o reclamo de hallazgo");
            lblSubtitulo.setForeground(new Color(102, 102, 102));
        }
        if (btnReclamar != null) {
            btnReclamar.setEnabled(true);
            btnReclamar.setText("Confirmar Reclamo");
        }
        actualizarCamposDinamicos();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        panelContenedor = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        espacio1 = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        espacio2 = new javax.swing.JPanel();
        lblCategoria = new javax.swing.JLabel();
        cbxCategoria = new javax.swing.JComboBox<>();
        espacio3 = new javax.swing.JPanel();
        lblLugar = new javax.swing.JLabel();
        cbxLugar = new javax.swing.JComboBox<>();
        espacio4 = new javax.swing.JPanel();
        panelFilaFechaColor = new javax.swing.JPanel();
        panelColFecha = new javax.swing.JPanel();
        lblFecha = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        panelColColor = new javax.swing.JPanel();
        lblColor = new javax.swing.JLabel();
        txtColor = new javax.swing.JTextField();
        espacio5 = new javax.swing.JPanel();
        lblDescripcion = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        espacio6 = new javax.swing.JPanel();
        lblDatoPrivado = new javax.swing.JLabel();
        txtDatoPrivado = new javax.swing.JTextField();
        espacio7 = new javax.swing.JPanel();
        btnReclamar = new javax.swing.JButton();
        espacio8 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 750));
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelContenedor.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedor.setBorder(javax.swing.BorderFactory.createEmptyBorder(24, 28, 24, 28));
        panelContenedor.setLayout(new javax.swing.BoxLayout(panelContenedor, javax.swing.BoxLayout.Y_AXIS));

        panelHeader.setAlignmentX(0.0F);
        panelHeader.setBackground(new java.awt.Color(255, 255, 255));
        panelHeader.setMaximumSize(new java.awt.Dimension(32767, 50));
        panelHeader.setPreferredSize(new java.awt.Dimension(440, 50));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.Y_AXIS));

        lblTitulo.setFont(new java.awt.Font("SansSerif", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(51, 37, 33));
        lblTitulo.setText("Reclamar Objeto");
        panelHeader.add(lblTitulo);

        lblSubtitulo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblSubtitulo.setForeground(new java.awt.Color(102, 102, 102));
        lblSubtitulo.setText("Registro de datos del objeto perdido o reclamo de hallazgo");
        panelHeader.add(lblSubtitulo);

        panelContenedor.add(panelHeader);

        espacio1.setAlignmentX(0.0F);
        espacio1.setBackground(new java.awt.Color(255, 255, 255));
        espacio1.setMaximumSize(new java.awt.Dimension(32767, 16));
        espacio1.setPreferredSize(new java.awt.Dimension(440, 16));
        panelContenedor.add(espacio1);

        lblNombre.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(75, 85, 99));
        lblNombre.setText("Nombre del Objeto *");
        panelContenedor.add(lblNombre);

        txtNombre.setAlignmentX(0.0F);
        txtNombre.setBackground(new java.awt.Color(249, 250, 251));
        txtNombre.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        txtNombre.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txtNombre.setMaximumSize(new java.awt.Dimension(32767, 36));
        txtNombre.setPreferredSize(new java.awt.Dimension(440, 36));
        panelContenedor.add(txtNombre);

        espacio2.setAlignmentX(0.0F);
        espacio2.setBackground(new java.awt.Color(255, 255, 255));
        espacio2.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio2.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio2);

        lblCategoria.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblCategoria.setForeground(new java.awt.Color(75, 85, 99));
        lblCategoria.setText("Categoría *");
        panelContenedor.add(lblCategoria);

        cbxCategoria.setAlignmentX(0.0F);
        cbxCategoria.setBackground(new java.awt.Color(255, 255, 255));
        cbxCategoria.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        cbxCategoria.setMaximumSize(new java.awt.Dimension(32767, 36));
        cbxCategoria.setPreferredSize(new java.awt.Dimension(440, 36));
        panelContenedor.add(cbxCategoria);

        espacio3.setAlignmentX(0.0F);
        espacio3.setBackground(new java.awt.Color(255, 255, 255));
        espacio3.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio3.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio3);

        lblLugar.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblLugar.setForeground(new java.awt.Color(75, 85, 99));
        lblLugar.setText("Lugar donde se perdió o halló *");
        panelContenedor.add(lblLugar);

        cbxLugar.setAlignmentX(0.0F);
        cbxLugar.setBackground(new java.awt.Color(255, 255, 255));
        cbxLugar.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        cbxLugar.setMaximumSize(new java.awt.Dimension(32767, 36));
        cbxLugar.setPreferredSize(new java.awt.Dimension(440, 36));
        panelContenedor.add(cbxLugar);

        espacio4.setAlignmentX(0.0F);
        espacio4.setBackground(new java.awt.Color(255, 255, 255));
        espacio4.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio4.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio4);

        panelFilaFechaColor.setAlignmentX(0.0F);
        panelFilaFechaColor.setBackground(new java.awt.Color(255, 255, 255));
        panelFilaFechaColor.setMaximumSize(new java.awt.Dimension(32767, 60));
        panelFilaFechaColor.setPreferredSize(new java.awt.Dimension(440, 60));
        panelFilaFechaColor.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelColFecha.setBackground(new java.awt.Color(255, 255, 255));
        panelColFecha.setLayout(new javax.swing.BoxLayout(panelColFecha, javax.swing.BoxLayout.Y_AXIS));

        lblFecha.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(75, 85, 99));
        lblFecha.setText("Fecha (YYYY-MM-DD) *");
        panelColFecha.add(lblFecha);

        txtFecha.setBackground(new java.awt.Color(249, 250, 251));
        txtFecha.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        txtFecha.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txtFecha.setMaximumSize(new java.awt.Dimension(32767, 36));
        txtFecha.setPreferredSize(new java.awt.Dimension(210, 36));
        panelColFecha.add(txtFecha);

        panelFilaFechaColor.add(panelColFecha);

        panelColColor.setBackground(new java.awt.Color(255, 255, 255));
        panelColColor.setLayout(new javax.swing.BoxLayout(panelColColor, javax.swing.BoxLayout.Y_AXIS));

        lblColor.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblColor.setForeground(new java.awt.Color(75, 85, 99));
        lblColor.setText("Color Principal *");
        panelColColor.add(lblColor);

        txtColor.setBackground(new java.awt.Color(249, 250, 251));
        txtColor.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        txtColor.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txtColor.setMaximumSize(new java.awt.Dimension(32767, 36));
        txtColor.setPreferredSize(new java.awt.Dimension(210, 36));
        panelColColor.add(txtColor);

        panelFilaFechaColor.add(panelColColor);

        panelContenedor.add(panelFilaFechaColor);

        espacio5.setAlignmentX(0.0F);
        espacio5.setBackground(new java.awt.Color(255, 255, 255));
        espacio5.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio5.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio5);

        lblDescripcion.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(75, 85, 99));
        lblDescripcion.setText("Descripción y detalles");
        panelContenedor.add(lblDescripcion);

        txtDescripcion.setAlignmentX(0.0F);
        txtDescripcion.setBackground(new java.awt.Color(249, 250, 251));
        txtDescripcion.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        txtDescripcion.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txtDescripcion.setMaximumSize(new java.awt.Dimension(32767, 36));
        txtDescripcion.setPreferredSize(new java.awt.Dimension(440, 36));
        panelContenedor.add(txtDescripcion);

        espacio6.setAlignmentX(0.0F);
        espacio6.setBackground(new java.awt.Color(255, 255, 255));
        espacio6.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio6.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio6);

        lblDatoPrivado.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblDatoPrivado.setForeground(new java.awt.Color(75, 85, 99));
        lblDatoPrivado.setText("Dato privado de comprobación (prueba de dueño) *");
        panelContenedor.add(lblDatoPrivado);

        txtDatoPrivado.setAlignmentX(0.0F);
        txtDatoPrivado.setBackground(new java.awt.Color(249, 250, 251));
        txtDatoPrivado.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        txtDatoPrivado.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txtDatoPrivado.setMaximumSize(new java.awt.Dimension(32767, 36));
        txtDatoPrivado.setPreferredSize(new java.awt.Dimension(440, 36));
        panelContenedor.add(txtDatoPrivado);

        espacio7.setAlignmentX(0.0F);
        espacio7.setBackground(new java.awt.Color(255, 255, 255));
        espacio7.setMaximumSize(new java.awt.Dimension(32767, 20));
        espacio7.setPreferredSize(new java.awt.Dimension(440, 20));
        panelContenedor.add(espacio7);

        btnReclamar.setBackground(new java.awt.Color(51, 153, 255));
        btnReclamar.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        btnReclamar.setForeground(new java.awt.Color(255, 255, 255));
        btnReclamar.setText("Confirmar Reclamo");
        btnReclamar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
        btnReclamar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnReclamar.setMaximumSize(new java.awt.Dimension(32767, 42));
        btnReclamar.setPreferredSize(new java.awt.Dimension(440, 42));
        btnReclamar.addActionListener(this::btnReclamarActionPerformed);
        panelContenedor.add(btnReclamar);

        espacio8.setAlignmentX(0.0F);
        espacio8.setBackground(new java.awt.Color(255, 255, 255));
        espacio8.setMaximumSize(new java.awt.Dimension(32767, 24));
        espacio8.setPreferredSize(new java.awt.Dimension(440, 24));
        panelContenedor.add(espacio8);

        jScrollPane1.setViewportView(panelContenedor);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnReclamarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReclamarActionPerformed
        String nombre = txtNombre.getText().trim();
        String categoria = (String) cbxCategoria.getSelectedItem();
        String lugar = (String) cbxLugar.getSelectedItem();
        String fecha = txtFecha.getText().trim();
        String color = txtColor.getText().trim();
        String desc = txtDescripcion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor indica el nombre del objeto.",
                "Campo requerido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Recolectar respuestas de verificación según los inputs dinámicos de la categoría
        List<String> respuestas = new ArrayList<>();
        String datoPrivado = "";

        if ("Prenda".equalsIgnoreCase(categoria)) {
            String talla = (txtTalla != null) ? txtTalla.getText().trim() : "";
            String marca = (txtMarca != null) ? txtMarca.getText().trim() : "";
            if (talla.isEmpty() || marca.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Debes completar ambas preguntas de verificación para Prenda:\n1. Talla\n2. Marca",
                    "Respuestas Requeridas",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            respuestas.add(talla);
            respuestas.add(marca);
            datoPrivado = talla + ";" + marca;
        } else if ("Dispositivo Electronico".equalsIgnoreCase(categoria)) {
            String marca = (txtMarca != null) ? txtMarca.getText().trim() : "";
            String modelo = (txtModelo != null) ? txtModelo.getText().trim() : "";
            String serie = (txtSerie != null) ? txtSerie.getText().trim() : "";
            if (marca.isEmpty() || modelo.isEmpty() || serie.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Debes completar las preguntas de verificación para Dispositivo Electrónico:\n1. Marca\n2. Modelo\n3. N° de Serie o Detalle único",
                    "Respuestas Requeridas",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            respuestas.add(marca);
            respuestas.add(modelo);
            respuestas.add(serie);
            datoPrivado = marca + ";" + modelo + ";" + serie;
        } else if ("Documento".equalsIgnoreCase(categoria)) {
            String tipoDoc = (txtTipoDoc != null) ? txtTipoDoc.getText().trim() : "";
            String titular = (txtTitular != null) ? txtTitular.getText().trim() : "";
            if (tipoDoc.isEmpty() || titular.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Debes completar las preguntas de verificación para Documento:\n1. Tipo de Documento\n2. Nombre completo del Titular",
                    "Respuestas Requeridas",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            respuestas.add(tipoDoc);
            respuestas.add(titular);
            datoPrivado = tipoDoc + ";" + titular;
        } else {
            String det = (txtDetalleExtra != null) ? txtDetalleExtra.getText().trim() : "";
            if (det.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Debes ingresar la característica o detalle distintivo para comprobar tu pertenencia.",
                    "Respuesta Requerida",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            respuestas.add(det);
            datoPrivado = det;
        }

        if (txtDatoPrivado != null) {
            txtDatoPrivado.setText(datoPrivado);
        }

        if (fecha.isEmpty()) {
            fecha = LocalDate.now().toString();
        }

        if (color.isEmpty()) {
            color = "No especificado";
        }

        if (desc.isEmpty()) {
            desc = "Objeto reportado en campus";
        }

        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this,
                "Debes iniciar sesión para registrar un reclamo o reporte de pérdida.",
                "Sesión Requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (objetoServicio == null) {
            objetoServicio = new ObjetoServicio();
        }
        if (reclamoServicio == null) {
            reclamoServicio = new ReclamoServicio();
        }

        ObjetoReportado match = objetoObjetivo;
        if (match == null) {
            List<ObjetoReportado> todos = objetoServicio.listarTodos();
            for (ObjetoReportado o : todos) {
                if (o != null && "ENCONTRADO".equalsIgnoreCase(o.getEstado())) {
                    if (o.getNombre().equalsIgnoreCase(nombre) || o.coincideCon(nombre)) {
                        match = o;
                        break;
                    }
                }
            }
        }

        if (match != null) {
            String estadoMatch = (match.getEstado() != null) ? match.getEstado().trim().toUpperCase() : "ENCONTRADO";
            if ("PENDIENTE".equals(estadoMatch) || reclamoServicio.tieneReclamoPendiente(match.getId())) {
                JOptionPane.showMessageDialog(this,
                    "Este objeto ya tiene un reclamo en estado PENDIENTE en proceso de validación.\nNo es posible reclamarlo nuevamente hasta que sea evaluado.",
                    "Objeto en Revisión",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if ("ENTREGADO".equals(estadoMatch)) {
                JOptionPane.showMessageDialog(this,
                    "Este objeto ya fue entregado a su dueño legítimo.",
                    "Objeto no disponible",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (match.getReportadoPor() != null && usuarioActual != null &&
                match.getReportadoPor().getIdentificacion().equalsIgnoreCase(usuarioActual.getIdentificacion())) {
                JOptionPane.showMessageDialog(this,
                    "No puedes reclamar un objeto que tú mismo reportaste como hallado.\nPara probar el reclamo, inicia sesión con otra cuenta de estudiante.",
                    "Acción no permitida",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (reclamoServicio.existeReclamoPendiente(match.getId(), usuarioActual.getIdentificacion())) {
                JOptionPane.showMessageDialog(this,
                    "Ya existe un reclamo pendiente registrado por ti para este objeto.\nPuedes consultar su avance en el panel de Avisos.",
                    "Reclamo Existente",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int totalPreguntas = match.preguntasVerificacion().length;
            while (respuestas.size() < totalPreguntas) {
                respuestas.add("N/A");
            }

            boolean ok = reclamoServicio.crearReclamo(match.getId(), usuarioActual, respuestas);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                    "¡Reclamo enviado exitosamente para '" + match.getNombre() + "'!\n\n" +
                    "• Estado: PENDIENTE de revisión\n" +
                    "• El objeto ahora aparece como PENDIENTE para ambas cuentas.\n" +
                    "• Quien lo reportó ha sido notificado para evaluar tus respuestas.\n" +
                    "• Puedes consultar las actualizaciones en la pestaña Avisos.",
                    "Reclamo Registrado",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo registrar el reclamo. Verifica que el objeto siga disponible y no tenga un reclamo pendiente en curso.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            int nuevoId = objetoServicio.generarNuevoId();
            ObjetoReportado objPerdido;
            Categoria catEnum = Categoria.fromString(categoria);
            switch (catEnum) {
                case PRENDA -> objPerdido = new Prenda(respuestas.get(0), respuestas.get(1), nuevoId, nombre, desc, Categoria.PRENDA, color, fecha, lugar, "PERDIDO", usuarioActual);
                case DISPOSITIVO_ELECTRONICO -> objPerdido = new DispositivoElectronico(respuestas.get(0), respuestas.get(1), respuestas.get(2), nuevoId, nombre, desc, Categoria.DISPOSITIVO_ELECTRONICO, color, fecha, lugar, "PERDIDO", usuarioActual);
                case DOCUMENTO -> objPerdido = new Documento(respuestas.get(0), respuestas.get(1), nuevoId, nombre, desc, Categoria.DOCUMENTO, color, fecha, lugar, "PERDIDO", usuarioActual);
                default -> objPerdido = new ObjetoGenerico(respuestas.get(0), nuevoId, nombre, desc, Categoria.OBJETO_GENERICO, color, fecha, lugar, "PERDIDO", usuarioActual);
            }
            objetoServicio.registrarObjeto(objPerdido);

            JOptionPane.showMessageDialog(this,
                "Se registró tu reporte de objeto perdido '" + nombre + "'.\nTe avisaremos en cuanto se detecte una coincidencia en el campus.",
                "Reporte de Pérdida",
                JOptionPane.INFORMATION_MESSAGE);
        }

        limpiarFormulario();
        if (panelApp != null) {
            if (panelApp.getPanelAvisos() != null) {
                panelApp.getPanelAvisos().cargarAvisos();
            }
            if (panelApp.getPanelInicio() != null) {
                panelApp.getPanelInicio().cargarObjetos();
            }
            panelApp.mostrar("avisos");
        }
    }//GEN-LAST:event_btnReclamarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReclamar;
    private javax.swing.JComboBox<String> cbxCategoria;
    private javax.swing.JComboBox<String> cbxLugar;
    private javax.swing.JPanel espacio1;
    private javax.swing.JPanel espacio2;
    private javax.swing.JPanel espacio3;
    private javax.swing.JPanel espacio4;
    private javax.swing.JPanel espacio5;
    private javax.swing.JPanel espacio6;
    private javax.swing.JPanel espacio7;
    private javax.swing.JPanel espacio8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblDatoPrivado;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblLugar;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelColColor;
    private javax.swing.JPanel panelColFecha;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelFilaFechaColor;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtDatoPrivado;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
