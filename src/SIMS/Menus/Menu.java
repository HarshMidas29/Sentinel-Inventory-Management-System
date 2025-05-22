/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SIMS.Menus;

import SIMS.Item.Acquisition_Records;
import SIMS.Entities.Employee;
import SIMS.Entities.Sources;
import dao.ConnectionProvider;

/**
 *
 * @author Daham Yakandawala
 */
public class Menu extends javax.swing.JFrame {

    public Menu() {
        initComponents();
        setLocationRelativeTo(null);
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnItemMenu = new javax.swing.JButton();
        btnSources = new javax.swing.JButton();
        btnEmployees = new javax.swing.JButton();
        btnAcquisitions = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnDisposals = new javax.swing.JButton();
        btnLocations = new javax.swing.JButton();
        btnAssignment = new javax.swing.JButton();
        btnMaintenance = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(700, 465));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnItemMenu.setBackground(new java.awt.Color(51, 51, 255));
        btnItemMenu.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnItemMenu.setForeground(new java.awt.Color(255, 255, 255));
        btnItemMenu.setText("Items");
        btnItemMenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnItemMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnItemMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemMenuActionPerformed(evt);
            }
        });
        getContentPane().add(btnItemMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, 183, 48));

        btnSources.setBackground(new java.awt.Color(51, 51, 255));
        btnSources.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnSources.setForeground(new java.awt.Color(255, 255, 255));
        btnSources.setText("Third Parties");
        btnSources.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSources.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSourcesActionPerformed(evt);
            }
        });
        getContentPane().add(btnSources, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 183, 48));

        btnEmployees.setBackground(new java.awt.Color(51, 51, 255));
        btnEmployees.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnEmployees.setForeground(new java.awt.Color(255, 255, 255));
        btnEmployees.setText("Employees");
        btnEmployees.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEmployees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeesActionPerformed(evt);
            }
        });
        getContentPane().add(btnEmployees, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 180, 48));

        btnAcquisitions.setBackground(new java.awt.Color(51, 51, 255));
        btnAcquisitions.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnAcquisitions.setForeground(new java.awt.Color(255, 255, 255));
        btnAcquisitions.setText("Acquisitions");
        btnAcquisitions.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAcquisitions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcquisitionsActionPerformed(evt);
            }
        });
        getContentPane().add(btnAcquisitions, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 183, 48));

        btnExit.setBackground(new java.awt.Color(51, 51, 255));
        btnExit.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("Back to Dash");
        btnExit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 520, 183, 48));

        btnDisposals.setBackground(new java.awt.Color(51, 51, 255));
        btnDisposals.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnDisposals.setForeground(new java.awt.Color(255, 255, 255));
        btnDisposals.setText("Disposals");
        btnDisposals.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDisposals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisposalsActionPerformed(evt);
            }
        });
        getContentPane().add(btnDisposals, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 330, 183, 48));

        btnLocations.setBackground(new java.awt.Color(51, 51, 255));
        btnLocations.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnLocations.setForeground(new java.awt.Color(255, 255, 255));
        btnLocations.setText("Locations");
        btnLocations.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLocations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocationsActionPerformed(evt);
            }
        });
        getContentPane().add(btnLocations, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, 183, 48));

        btnAssignment.setBackground(new java.awt.Color(51, 51, 255));
        btnAssignment.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnAssignment.setForeground(new java.awt.Color(255, 255, 255));
        btnAssignment.setText("Assignment");
        btnAssignment.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAssignment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignmentActionPerformed(evt);
            }
        });
        getContentPane().add(btnAssignment, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 390, 183, 48));

        btnMaintenance.setBackground(new java.awt.Color(51, 51, 255));
        btnMaintenance.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnMaintenance.setForeground(new java.awt.Color(255, 255, 255));
        btnMaintenance.setText("Maintenance");
        btnMaintenance.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaintenanceActionPerformed(evt);
            }
        });
        getContentPane().add(btnMaintenance, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 270, 183, 48));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/2.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAcquisitionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcquisitionsActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Acquisition_Records().setVisible(true);
    }//GEN-LAST:event_btnAcquisitionsActionPerformed

    private void btnItemMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemMenuActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Item_Menu().setVisible(true);
    }//GEN-LAST:event_btnItemMenuActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnSourcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSourcesActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Sources().setVisible(true);
    }//GEN-LAST:event_btnSourcesActionPerformed

    private void btnEmployeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeesActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Employee().setVisible(true);

    }//GEN-LAST:event_btnEmployeesActionPerformed

    private void btnDisposalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisposalsActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Disposal_Menu().setVisible(true);
    }//GEN-LAST:event_btnDisposalsActionPerformed

    private void btnLocationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocationsActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Locations_Menu().setVisible(true);
    }//GEN-LAST:event_btnLocationsActionPerformed

    private void btnAssignmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignmentActionPerformed
        // TODO add your handling code here:
        new Assignment_Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnAssignmentActionPerformed

    private void btnMaintenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaintenanceActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Maintenance_Menu().setVisible(true);
    }//GEN-LAST:event_btnMaintenanceActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Initialize database schema
                ConnectionProvider.initializeDatabase();
                new Dashboard().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcquisitions;
    private javax.swing.JButton btnAssignment;
    private javax.swing.JButton btnDisposals;
    private javax.swing.JButton btnEmployees;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnItemMenu;
    private javax.swing.JButton btnLocations;
    private javax.swing.JButton btnMaintenance;
    private javax.swing.JButton btnSources;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
