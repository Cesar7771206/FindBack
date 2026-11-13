package vista;

import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import modelo.Categoria;
import modelo.DispositivoElectronico;
import modelo.Documento;
import modelo.Estudiante;
import modelo.ObjetoGenerico;
import modelo.ObjetoReportado;
import modelo.Prenda;
import modelo.Ubicacion;
import servicio.CoincidenciaServicio;
import servicio.ObjetoServicio;

public class PanelNotificar extends javax.swing.JPanel {

    private frmPrincipal ventana;
    private PanelApp panelApp;
    private Estudiante usuarioActual;
    private ObjetoServicio objetoServicio;
    private CoincidenciaServicio coincidenciaServicio;

    private javax.swing.JTextField txtTalla;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtSerie;
    private javax.swing.JTextField txtTipoDoc;
    private javax.swing.JTextField txtTitular;
    private javax.swing.JTextField txtDetalleExtra;

    public PanelNotificar() {
        initComponents();
        if (jScrollPane1 != null && jScrollPane1.getVerticalScrollBar() != null) {
            jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        }
        this.objetoServicio = new ObjetoServicio();
        this.coincidenciaServicio = new CoincidenciaServicio();
        this.txtFecha.setText(LocalDate.now().toString());

        cargarCategorias();
        cargarUbicaciones();
        inicializarCamposDinamicos();
        actualizarCamposDinamicos();
        cbxCategoria.addActionListener(evt -> actualizarCamposDinamicos());
    }

    public PanelNotificar(frmPrincipal ventana, PanelApp panelApp) {
        this();
        this.ventana = ventana;
        this.panelApp = panelApp;
    }

    public void setUsuarioActual(Estudiante estudiante) {
        this.usuarioActual = estudiante;
    }

    private void cargarCategorias() {
        cbxCategoria.removeAllItems();
        for (Categoria c : Categoria.values()) {
            cbxCategoria.addItem(c.getNombre());
        }
    }

    private void cargarUbicaciones() {
        cbxLugar.removeAllItems();
        for (Ubicacion u : Ubicacion.values()) {
            cbxLugar.addItem(u.getNombre());
        }
    }

    private JTextField crearTextField() {
        JTextField txt = new JTextField();
        txt.setAlignmentX(0.0f);
        txt.setBackground(new java.awt.Color(249, 250, 251));
        txt.setFont(new java.awt.Font("SansSerif", 0, 13));
        txt.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new java.awt.Color(209, 213, 219), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        txt.setMaximumSize(new java.awt.Dimension(32767, 36));
        txt.setPreferredSize(new java.awt.Dimension(440, 36));
        return txt;
    }

    private JLabel crearEtiquetaCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setAlignmentX(0.0f);
        lbl.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        lbl.setForeground(new java.awt.Color(75, 85, 99));
        return lbl;
    }

    private JPanel crearEspacio(int h) {
        JPanel p = new JPanel();
        p.setAlignmentX(0.0f);
        p.setBackground(new java.awt.Color(255, 255, 255));
        p.setMaximumSize(new java.awt.Dimension(32767, h));
        p.setPreferredSize(new java.awt.Dimension(440, h));
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
                pnlCamposDinamicos.add(crearEtiquetaCampo("Talla de la prenda (S, M, L, XL, etc.) *"));
                pnlCamposDinamicos.add(txtTalla);
                pnlCamposDinamicos.add(crearEspacio(12));
                pnlCamposDinamicos.add(crearEtiquetaCampo("Marca de la prenda *"));
                pnlCamposDinamicos.add(txtMarca);
                pnlCamposDinamicos.add(crearEspacio(12));
            }
            case "Dispositivo Electronico" -> {
                pnlCamposDinamicos.add(crearEtiquetaCampo("Marca del dispositivo *"));
                pnlCamposDinamicos.add(txtMarca);
                pnlCamposDinamicos.add(crearEspacio(12));
                pnlCamposDinamicos.add(crearEtiquetaCampo("Modelo exacto del dispositivo *"));
                pnlCamposDinamicos.add(txtModelo);
                pnlCamposDinamicos.add(crearEspacio(12));
                pnlCamposDinamicos.add(crearEtiquetaCampo("Número de serie / Código o detalle único *"));
                pnlCamposDinamicos.add(txtSerie);
                pnlCamposDinamicos.add(crearEspacio(12));
            }
            case "Documento" -> {
                pnlCamposDinamicos.add(crearEtiquetaCampo("Tipo de documento (DNI, Carné, etc.) *"));
                pnlCamposDinamicos.add(txtTipoDoc);
                pnlCamposDinamicos.add(crearEspacio(12));
                pnlCamposDinamicos.add(crearEtiquetaCampo("Nombre completo del titular en el documento *"));
                pnlCamposDinamicos.add(txtTitular);
                pnlCamposDinamicos.add(crearEspacio(12));
            }
            default -> {
                pnlCamposDinamicos.add(crearEtiquetaCampo("Característica o detalle distintivo *"));
                pnlCamposDinamicos.add(txtDetalleExtra);
                pnlCamposDinamicos.add(crearEspacio(12));
            }
        }

        pnlCamposDinamicos.revalidate();
        pnlCamposDinamicos.repaint();
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    public void limpiarFormulario() {
        txtObjeto.setText("");
        cbxCategoria.setSelectedIndex(0);
        cbxLugar.setSelectedIndex(0);
        txtFecha.setText(LocalDate.now().toString());
        txtColor.setText("");
        txtDescripcion.setText("");
        if (txtTalla != null) txtTalla.setText("");
        if (txtMarca != null) txtMarca.setText("");
        if (txtModelo != null) txtModelo.setText("");
        if (txtSerie != null) txtSerie.setText("");
        if (txtTipoDoc != null) txtTipoDoc.setText("");
        if (txtTitular != null) txtTitular.setText("");
        if (txtDetalleExtra != null) txtDetalleExtra.setText("");
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
        lblObjeto = new javax.swing.JLabel();
        txtObjeto = new javax.swing.JTextField();
        espacio2 = new javax.swing.JPanel();
        lblCategoria = new javax.swing.JLabel();
        cbxCategoria = new javax.swing.JComboBox<>();
        espacio3 = new javax.swing.JPanel();
        pnlCamposDinamicos = new javax.swing.JPanel();
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
        espacio7 = new javax.swing.JPanel();
        btnNotificar = new javax.swing.JButton();
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

        panelHeader.setAlignmentX(0.0f);
        panelHeader.setBackground(new java.awt.Color(255, 255, 255));
        panelHeader.setMaximumSize(new java.awt.Dimension(32767, 50));
        panelHeader.setPreferredSize(new java.awt.Dimension(440, 50));
        panelHeader.setLayout(new javax.swing.BoxLayout(panelHeader, javax.swing.BoxLayout.Y_AXIS));

        lblTitulo.setAlignmentX(0.0f);
        lblTitulo.setFont(new java.awt.Font("SansSerif", 1, 22)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(33, 37, 41));
        lblTitulo.setText("Notificar Hallazgo");
        panelHeader.add(lblTitulo);

        lblSubtitulo.setAlignmentX(0.0f);
        lblSubtitulo.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblSubtitulo.setForeground(new java.awt.Color(102, 102, 102));
        lblSubtitulo.setText("Reporta un objeto encontrado en el campus");
        panelHeader.add(lblSubtitulo);

        panelContenedor.add(panelHeader);

        espacio1.setAlignmentX(0.0f);
        espacio1.setBackground(new java.awt.Color(255, 255, 255));
        espacio1.setMaximumSize(new java.awt.Dimension(32767, 16));
        espacio1.setPreferredSize(new java.awt.Dimension(440, 16));
        panelContenedor.add(espacio1);

        lblObjeto.setAlignmentX(0.0f);
        lblObjeto.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblObjeto.setForeground(new java.awt.Color(75, 85, 99));
        lblObjeto.setText("¿Qué objeto encontraste? *");
        panelContenedor.add(lblObjeto);

        txtObjeto.setAlignmentX(0.0f);
        txtObjeto.setBackground(new java.awt.Color(249, 250, 251));
        txtObjeto.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        txtObjeto.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(209, 213, 219), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtObjeto.setMaximumSize(new java.awt.Dimension(32767, 36));
        txtObjeto.setPreferredSize(new java.awt.Dimension(440, 36));
        panelContenedor.add(txtObjeto);

        espacio2.setAlignmentX(0.0f);
        espacio2.setBackground(new java.awt.Color(255, 255, 255));
        espacio2.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio2.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio2);

        lblCategoria.setAlignmentX(0.0f);
        lblCategoria.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblCategoria.setForeground(new java.awt.Color(75, 85, 99));
        lblCategoria.setText("Categoría *");
        panelContenedor.add(lblCategoria);

        cbxCategoria.setAlignmentX(0.0f);
        cbxCategoria.setBackground(new java.awt.Color(255, 255, 255));
        cbxCategoria.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        cbxCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Documento", "Prenda", "Dispositivo Electronico", "Objeto Generico" }));
        cbxCategoria.setMaximumSize(new java.awt.Dimension(32767, 36));
        cbxCategoria.setPreferredSize(new java.awt.Dimension(440, 36));
        panelContenedor.add(cbxCategoria);

        espacio3.setAlignmentX(0.0f);
        espacio3.setBackground(new java.awt.Color(255, 255, 255));
        espacio3.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio3.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio3);

        pnlCamposDinamicos.setAlignmentX(0.0f);
        pnlCamposDinamicos.setBackground(new java.awt.Color(255, 255, 255));
        pnlCamposDinamicos.setLayout(new javax.swing.BoxLayout(pnlCamposDinamicos, javax.swing.BoxLayout.Y_AXIS));
        panelContenedor.add(pnlCamposDinamicos);

        lblLugar.setAlignmentX(0.0f);
        lblLugar.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblLugar.setForeground(new java.awt.Color(75, 85, 99));
        lblLugar.setText("Lugar donde lo hallaste en el campus *");
        panelContenedor.add(lblLugar);

        cbxLugar.setAlignmentX(0.0f);
        cbxLugar.setBackground(new java.awt.Color(255, 255, 255));
        cbxLugar.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        cbxLugar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Biblioteca Central", "Edificio Principal", "Edificio 80", "Cafetería Central / Comedor", "Edificio B", "Edificio E", "CA 101", "CA 102", "CA 103", "Facultad de Educacion", "Derecho", "Polideportivo", "Fcom", "Hidraulica", "Otro lugar del campus" }));
        cbxLugar.setMaximumSize(new java.awt.Dimension(32767, 36));
        cbxLugar.setPreferredSize(new java.awt.Dimension(440, 36));
        panelContenedor.add(cbxLugar);

        espacio4.setAlignmentX(0.0f);
        espacio4.setBackground(new java.awt.Color(255, 255, 255));
        espacio4.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio4.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio4);

        panelFilaFechaColor.setAlignmentX(0.0f);
        panelFilaFechaColor.setBackground(new java.awt.Color(255, 255, 255));
        panelFilaFechaColor.setMaximumSize(new java.awt.Dimension(32767, 60));
        panelFilaFechaColor.setPreferredSize(new java.awt.Dimension(440, 60));
        panelFilaFechaColor.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        panelColFecha.setBackground(new java.awt.Color(255, 255, 255));
        panelColFecha.setLayout(new javax.swing.BoxLayout(panelColFecha, javax.swing.BoxLayout.Y_AXIS));

        lblFecha.setAlignmentX(0.0f);
        lblFecha.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblFecha.setForeground(new java.awt.Color(75, 85, 99));
        lblFecha.setText("Fecha (YYYY-MM-DD) *");
        panelColFecha.add(lblFecha);

        txtFecha.setAlignmentX(0.0f);
        txtFecha.setBackground(new java.awt.Color(249, 250, 251));
        txtFecha.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        txtFecha.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(209, 213, 219), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtFecha.setMaximumSize(new java.awt.Dimension(32767, 36));
        txtFecha.setPreferredSize(new java.awt.Dimension(210, 36));
        panelColFecha.add(txtFecha);

        panelFilaFechaColor.add(panelColFecha);

        panelColColor.setBackground(new java.awt.Color(255, 255, 255));
        panelColColor.setLayout(new javax.swing.BoxLayout(panelColColor, javax.swing.BoxLayout.Y_AXIS));

        lblColor.setAlignmentX(0.0f);
        lblColor.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblColor.setForeground(new java.awt.Color(75, 85, 99));
        lblColor.setText("Color Principal *");
        panelColColor.add(lblColor);

        txtColor.setAlignmentX(0.0f);
        txtColor.setBackground(new java.awt.Color(249, 250, 251));
        txtColor.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        txtColor.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(209, 213, 219), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtColor.setMaximumSize(new java.awt.Dimension(32767, 36));
        txtColor.setPreferredSize(new java.awt.Dimension(210, 36));
        panelColColor.add(txtColor);

        panelFilaFechaColor.add(panelColColor);

        panelContenedor.add(panelFilaFechaColor);

        espacio5.setAlignmentX(0.0f);
        espacio5.setBackground(new java.awt.Color(255, 255, 255));
        espacio5.setMaximumSize(new java.awt.Dimension(32767, 12));
        espacio5.setPreferredSize(new java.awt.Dimension(440, 12));
        panelContenedor.add(espacio5);

        lblDescripcion.setAlignmentX(0.0f);
        lblDescripcion.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(75, 85, 99));
        lblDescripcion.setText("Observaciones o notas adicionales");
        panelContenedor.add(lblDescripcion);

        txtDescripcion.setAlignmentX(0.0f);
        txtDescripcion.setBackground(new java.awt.Color(249, 250, 251));
        txtDescripcion.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        txtDescripcion.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(209, 213, 219), 1, true), javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtDescripcion.setMaximumSize(new java.awt.Dimension(32767, 36));
        txtDescripcion.setPreferredSize(new java.awt.Dimension(440, 36));
        panelContenedor.add(txtDescripcion);

        espacio7.setAlignmentX(0.0f);
        espacio7.setBackground(new java.awt.Color(255, 255, 255));
        espacio7.setMaximumSize(new java.awt.Dimension(32767, 20));
        espacio7.setPreferredSize(new java.awt.Dimension(440, 20));
        panelContenedor.add(espacio7);

        btnNotificar.setAlignmentX(0.0f);
        btnNotificar.setBackground(new java.awt.Color(51, 153, 255));
        btnNotificar.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        btnNotificar.setForeground(new java.awt.Color(255, 255, 255));
        btnNotificar.setText("Notificar Hallazgo");
        btnNotificar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 153, 255), 1, true));
        btnNotificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNotificar.setMaximumSize(new java.awt.Dimension(32767, 42));
        btnNotificar.setPreferredSize(new java.awt.Dimension(440, 42));
        btnNotificar.addActionListener(this::btnNotificarActionPerformed);
        panelContenedor.add(btnNotificar);

        espacio8.setAlignmentX(0.0f);
        espacio8.setBackground(new java.awt.Color(255, 255, 255));
        espacio8.setMaximumSize(new java.awt.Dimension(32767, 24));
        espacio8.setPreferredSize(new java.awt.Dimension(440, 24));
        panelContenedor.add(espacio8);

        jScrollPane1.setViewportView(panelContenedor);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNotificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificarActionPerformed
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this,
                "Debes iniciar sesión con tu cuenta estudiantil para notificar un objeto hallado.",
                "Sesión Requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtObjeto.getText().trim();
        String categoria = (String) cbxCategoria.getSelectedItem();
        String lugar = (String) cbxLugar.getSelectedItem();
        String fecha = txtFecha.getText().trim();
        String color = txtColor.getText().trim();
        String desc = txtDescripcion.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor escribe el nombre del objeto encontrado.",
                "Campo requerido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (fecha.isEmpty()) {
            fecha = LocalDate.now().toString();
        }

        if (color.isEmpty()) {
            color = "No especificado";
        }

        if (desc.isEmpty()) {
            desc = "Objeto hallado en campus";
        }

        if (objetoServicio == null) {
            objetoServicio = new ObjetoServicio();
        }
        if (coincidenciaServicio == null) {
            coincidenciaServicio = new CoincidenciaServicio();
        }

        int nuevoId = objetoServicio.generarNuevoId();
        ObjetoReportado nuevoObj;

        if ("Prenda".equalsIgnoreCase(categoria)) {
            String talla = txtTalla.getText().trim();
            String marca = txtMarca.getText().trim();
            if (talla.isEmpty()) {
                talla = "Estándar";
            }
            if (marca.isEmpty()) {
                marca = "Sin marca";
            }
            nuevoObj = new Prenda(talla, marca, nuevoId, nombre, desc, "PRENDA", color, fecha, lugar, "ENCONTRADO", usuarioActual);
        } else if ("Dispositivo Electronico".equalsIgnoreCase(categoria)) {
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String serie = txtSerie.getText().trim();
            if (marca.isEmpty()) {
                marca = "Genérico";
            }
            if (modelo.isEmpty()) {
                modelo = "No especificado";
            }
            if (serie.isEmpty()) {
                serie = "No registrado";
            }
            nuevoObj = new DispositivoElectronico(marca, modelo, serie, nuevoId, nombre, desc, "DISPOSITIVO ELECTRONICO", color, fecha, lugar, "ENCONTRADO", usuarioActual);
        } else if ("Documento".equalsIgnoreCase(categoria)) {
            String tipoDoc = txtTipoDoc.getText().trim();
            String titular = txtTitular.getText().trim();
            if (tipoDoc.isEmpty()) {
                tipoDoc = "Documento";
            }
            if (titular.isEmpty()) {
                titular = "No especificado";
            }
            nuevoObj = new Documento(tipoDoc, titular, nuevoId, nombre, desc, "DOCUMENTO", color, fecha, lugar, "ENCONTRADO", usuarioActual);
        } else {
            String det = txtDetalleExtra.getText().trim();
            if (det.isEmpty()) {
                det = desc;
            }
            nuevoObj = new ObjetoGenerico(det, nuevoId, nombre, desc, "OBJETO GENERICO", color, fecha, lugar, "ENCONTRADO", usuarioActual);
        }

        boolean ok = objetoServicio.registrarObjeto(nuevoObj);
        if (ok) {
            List<ObjetoReportado> matches = coincidenciaServicio.buscarCoincidencias(nuevoObj);
            if (!matches.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "¡Objeto notificado con éxito!\nSe detectó coincidencia con un reporte previo en el campus: " + matches.get(0).getNombre(),
                    "Notificación Registrada",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "El objeto '" + nombre + "' fue publicado exitosamente en el campus.",
                    "Notificación Registrada",
                    JOptionPane.INFORMATION_MESSAGE);
            }

            limpiarFormulario();
            if (panelApp != null) {
                if (panelApp.getPanelInicio() != null) {
                    panelApp.getPanelInicio().cargarObjetos();
                }
                panelApp.mostrar("inicio");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo registrar el objeto. Revisa los datos ingresados.",
                "Error al notificar",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNotificarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNotificar;
    private javax.swing.JComboBox<String> cbxCategoria;
    private javax.swing.JComboBox<String> cbxLugar;
    private javax.swing.JPanel espacio1;
    private javax.swing.JPanel espacio2;
    private javax.swing.JPanel espacio3;
    private javax.swing.JPanel espacio4;
    private javax.swing.JPanel espacio5;
    private javax.swing.JPanel espacio7;
    private javax.swing.JPanel espacio8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCategoria;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblLugar;
    private javax.swing.JLabel lblObjeto;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelColColor;
    private javax.swing.JPanel panelColFecha;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelFilaFechaColor;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel pnlCamposDinamicos;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtObjeto;
    // End of variables declaration//GEN-END:variables
}
