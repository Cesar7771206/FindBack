package vista;

import java.awt.CardLayout;
import modelo.Estudiante;

public class frmPrincipal extends javax.swing.JFrame {

    private CardLayout cardLayout;
    private PanelApp panelApp;
    private PanelLogin panelLogin;
    private PanelRegistro panelRegistro;

    public frmPrincipal() {
        initComponents();
        setSize(500, 800);
        setPreferredSize(new java.awt.Dimension(500, 800));
        setMinimumSize(new java.awt.Dimension(500, 800));
        setMaximumSize(new java.awt.Dimension(500, 800));
        setResizable(false);
        this.cardLayout = new CardLayout();
        contenedor.setLayout(this.cardLayout);
        contenedor.setPreferredSize(new java.awt.Dimension(500, 800));

        this.panelApp = new PanelApp(this);
        this.panelLogin = new PanelLogin(this);
        this.panelRegistro = new PanelRegistro(this);

        contenedor.add(panelLogin, "login");
        contenedor.add(panelRegistro, "registro");
        contenedor.add(panelApp, "app");
        
        mostrar("login");
        setLocationRelativeTo(null);
    }

    public void mostrar(String nombrePanel) {
        cardLayout.show(contenedor, nombrePanel);
    }

    public void iniciarSesion(Estudiante estudiante) {
        if (panelApp != null) {
            panelApp.setUsuarioActual(estudiante);
        }
        mostrar("app");
    }

    public void cerrarSesion() {
        if (panelApp != null) {
            panelApp.setUsuarioActual(null);
        }
        mostrar("login");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contenedor = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(500, 800));

        javax.swing.GroupLayout contenedorLayout = new javax.swing.GroupLayout(contenedor);
        contenedor.setLayout(contenedorLayout);
        contenedorLayout.setHorizontalGroup(
            contenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        contenedorLayout.setVerticalGroup(
            contenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contenedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contenedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contenedor;
    // End of variables declaration//GEN-END:variables
}
