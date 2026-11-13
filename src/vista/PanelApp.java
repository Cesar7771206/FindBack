package vista;

import java.awt.CardLayout;
import modelo.Estudiante;

public class PanelApp extends javax.swing.JPanel {

    private CardLayout cardLayout;
    private PanelInicio panelInicio;
    private PanelReclamar panelReclamar;
    private PanelNotificar panelNotificar;
    private PanelAvisos panelAvisos;
    private Estudiante usuarioActual;
    
    public PanelApp() {
        initComponents();
        this.cardLayout = new CardLayout();
        contenido.setLayout(cardLayout);
        
        this.panelInicio = new PanelInicio(null, this);
        this.panelReclamar = new PanelReclamar(null, this);
        this.panelNotificar = new PanelNotificar(null, this);
        this.panelAvisos = new PanelAvisos();
        
        contenido.add(panelInicio, "inicio");
        contenido.add(panelReclamar, "reclamar");
        contenido.add(panelNotificar, "notificar");
        contenido.add(panelAvisos, "avisos");
    }

    public PanelApp(frmPrincipal ventana) {
        initComponents();
        
        this.cardLayout = new CardLayout();
        contenido.setLayout(cardLayout);
        
        this.panelInicio = new PanelInicio(ventana, this);
        this.panelReclamar = new PanelReclamar(ventana, this);
        this.panelNotificar = new PanelNotificar(ventana, this);
        this.panelAvisos = new PanelAvisos();
        
        contenido.add(panelInicio, "inicio");
        contenido.add(panelReclamar, "reclamar");
        contenido.add(panelNotificar, "notificar");
        contenido.add(panelAvisos, "avisos");
    }
    
    public void setUsuarioActual(Estudiante estudiante) {
        this.usuarioActual = estudiante;
        if (panelAvisos != null) {
            panelAvisos.setUsuarioActual(estudiante);
        }
        if (panelInicio != null) {
            panelInicio.setUsuarioActual(estudiante);
        }
        if (panelReclamar != null) {
            panelReclamar.setUsuarioActual(estudiante);
        }
        if (panelNotificar != null) {
            panelNotificar.setUsuarioActual(estudiante);
        }
    }
    
    public PanelAvisos getPanelAvisos() {
        return panelAvisos;
    }
    
    public PanelInicio getPanelInicio() {
        return panelInicio;
    }

    public PanelReclamar getPanelReclamar() {
        return panelReclamar;
    }

    public PanelNotificar getPanelNotificar() {
        return panelNotificar;
    }
    
    public void mostrar(String nombre){
        cardLayout.show(contenido, nombre);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barraNav = new javax.swing.JPanel();
        btnPanelInicio = new javax.swing.JButton();
        btnPanelReclamar = new javax.swing.JButton();
        btnPanelNotificar = new javax.swing.JButton();
        btnPanelAvisos = new javax.swing.JButton();
        contenido = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        barraNav.setBackground(new java.awt.Color(255, 255, 255));
        barraNav.setMaximumSize(new java.awt.Dimension(32767, 100));
        barraNav.setMinimumSize(new java.awt.Dimension(356, 100));
        barraNav.setPreferredSize(new java.awt.Dimension(500, 50));
        barraNav.setLayout(new java.awt.GridLayout(1, 0));

        btnPanelInicio.setBackground(new java.awt.Color(204, 204, 204));
        btnPanelInicio.setForeground(new java.awt.Color(102, 102, 102));
        btnPanelInicio.setText("Inicio");
        btnPanelInicio.addActionListener(this::btnPanelInicioActionPerformed);
        barraNav.add(btnPanelInicio);

        btnPanelReclamar.setBackground(new java.awt.Color(204, 204, 204));
        btnPanelReclamar.setForeground(new java.awt.Color(102, 102, 102));
        btnPanelReclamar.setText("Reclamar");
        btnPanelReclamar.addActionListener(this::btnPanelReclamarActionPerformed);
        barraNav.add(btnPanelReclamar);

        btnPanelNotificar.setBackground(new java.awt.Color(204, 204, 204));
        btnPanelNotificar.setForeground(new java.awt.Color(102, 102, 102));
        btnPanelNotificar.setText("Notificar");
        btnPanelNotificar.addActionListener(this::btnPanelNotificarActionPerformed);
        barraNav.add(btnPanelNotificar);

        btnPanelAvisos.setBackground(new java.awt.Color(204, 204, 204));
        btnPanelAvisos.setForeground(new java.awt.Color(102, 102, 102));
        btnPanelAvisos.setText("Avisos");
        btnPanelAvisos.setMargin(new java.awt.Insets(2, 16, 4, 16));
        btnPanelAvisos.addActionListener(this::btnPanelAvisosActionPerformed);
        barraNav.add(btnPanelAvisos);

        add(barraNav, java.awt.BorderLayout.PAGE_END);

        contenido.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contenidoLayout = new javax.swing.GroupLayout(contenido);
        contenido.setLayout(contenidoLayout);
        contenidoLayout.setHorizontalGroup(
            contenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        contenidoLayout.setVerticalGroup(
            contenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );

        add(contenido, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPanelInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanelInicioActionPerformed
        if (panelInicio != null) {
            panelInicio.cargarObjetos();
        }
        mostrar("inicio");
    }//GEN-LAST:event_btnPanelInicioActionPerformed

    private void btnPanelReclamarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanelReclamarActionPerformed
        if (panelReclamar != null) {
            panelReclamar.cargarDatos();
        }
        mostrar("reclamar");
    }//GEN-LAST:event_btnPanelReclamarActionPerformed

    private void btnPanelNotificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanelNotificarActionPerformed
        mostrar("notificar");
    }//GEN-LAST:event_btnPanelNotificarActionPerformed

    private void btnPanelAvisosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanelAvisosActionPerformed
        if (panelAvisos != null) {
            panelAvisos.cargarAvisos();
        }
        mostrar("avisos");
    }//GEN-LAST:event_btnPanelAvisosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barraNav;
    private javax.swing.JButton btnPanelAvisos;
    private javax.swing.JButton btnPanelInicio;
    private javax.swing.JButton btnPanelNotificar;
    private javax.swing.JButton btnPanelReclamar;
    private javax.swing.JPanel contenido;
    // End of variables declaration//GEN-END:variables
}
