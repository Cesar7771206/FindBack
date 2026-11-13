package vista;

import javax.swing.JOptionPane;
import servicio.AutenticacionServicio;

public class PanelRegistro extends javax.swing.JPanel {

    private frmPrincipal ventana;
    private AutenticacionServicio autenticacionServicio;

    public PanelRegistro() {
        initComponents();
        this.autenticacionServicio = new AutenticacionServicio();
    }

    public PanelRegistro(frmPrincipal ventana) {
        this();
        this.ventana = ventana;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jPasswordField2 = new javax.swing.JPasswordField();
        jPasswordField3 = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Identificación (DNI/Pasaporte, etc.)*");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, -1, 16));

        jTextField1.setBackground(new java.awt.Color(236, 235, 235));
        jTextField1.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(102, 102, 102));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 370, 200, 30));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Nombre completo*");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, -1, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Facultad*");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Carrera*");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 350, -1, -1));

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Correo Institucional*");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, -1, -1));

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Contraseña*");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 490, -1, -1));

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("Confirmar Contraseña*");
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 490, -1, -1));

        jButton1.setBackground(new java.awt.Color(51, 153, 255));
        jButton1.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Registrarse");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 570, 270, 40));

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("¿Ya tienes una cuenta?");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 750, 155, 31));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 153, 255));
        jButton2.setText("Inicia sesión");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 750, -1, -1));

        jTextField7.setBackground(new java.awt.Color(236, 235, 235));
        jTextField7.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jTextField7.setForeground(new java.awt.Color(102, 102, 102));
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 400, 30));

        jTextField2.setBackground(new java.awt.Color(236, 235, 235));
        jTextField2.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(102, 102, 102));
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 400, 30));

        jTextField3.setBackground(new java.awt.Color(236, 235, 235));
        jTextField3.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(102, 102, 102));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 400, 30));

        jTextField8.setBackground(new java.awt.Color(236, 235, 235));
        jTextField8.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jTextField8.setForeground(new java.awt.Color(102, 102, 102));
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, 400, 30));

        jTextField9.setBackground(new java.awt.Color(236, 235, 235));
        jTextField9.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jTextField9.setForeground(new java.awt.Color(102, 102, 102));
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 370, 180, 30));

        jPasswordField2.setBackground(new java.awt.Color(236, 235, 235));
        jPasswordField2.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jPasswordField2.setForeground(new java.awt.Color(102, 102, 102));
        jPasswordField2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 510, 180, 30));

        jPasswordField3.setBackground(new java.awt.Color(236, 235, 235));
        jPasswordField3.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jPasswordField3.setForeground(new java.awt.Color(102, 102, 102));
        jPasswordField3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        add(jPasswordField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 510, 200, 30));

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Ciclo*");
        add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 350, -1, -1));

        jPanel1.setBackground(new java.awt.Color(236, 235, 235));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(102, 102, 102));
        jButton3.setText("Registrarse");

        jButton5.setBackground(new java.awt.Color(236, 235, 235));
        jButton5.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        jButton5.setForeground(new java.awt.Color(102, 102, 102));
        jButton5.setText("IniciarSesion");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(209, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(199, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 400, 50));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String dni = jTextField7.getText().trim();
        String nombre = jTextField2.getText().trim();
        String facultad = jTextField8.getText().trim();
        String carrera = jTextField9.getText().trim();
        String cicloStr = jTextField1.getText().trim();
        String correo = jTextField3.getText().trim();
        String pass = new String(jPasswordField2.getPassword());
        String confirmPass = new String(jPasswordField3.getPassword());

        if (dni.isEmpty() || nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor completa todos los campos requeridos.",
                "Campos incompletos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,
                "Las contraseñas no coinciden.",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int ciclo = 1;
        try {
            if (!cicloStr.isEmpty()) {
                ciclo = Integer.parseInt(cicloStr);
            }
        } catch (NumberFormatException ignored) {
            ciclo = 1;
        }

        if (autenticacionServicio == null) {
            autenticacionServicio = new AutenticacionServicio();
        }

        boolean ok = autenticacionServicio.registrarUsuario(dni, nombre, correo, pass, "SIN_MENSAJES", facultad, carrera, ciclo);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Registro exitoso. Ya puedes iniciar sesión con tu correo o DNI.",
                "Registro completado",
                JOptionPane.INFORMATION_MESSAGE);
            if (ventana != null) {
                ventana.mostrar("login");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo registrar. Verifica que el DNI o correo no estén ya registrados.",
                "Error al registrar",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (ventana != null) {
            ventana.mostrar("login");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (ventana != null) {
            ventana.mostrar("login");
        }
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
