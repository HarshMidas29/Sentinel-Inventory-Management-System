/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SIMS.Menus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Daham Yakandawala
 */
public class Dashboard extends javax.swing.JFrame {

    public Dashboard() {
        initComponents();
        setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnMenu = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnLdBckup = new javax.swing.JButton();
        btnSvBckup = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnMenu.setBackground(new java.awt.Color(204, 204, 204));
        btnMenu.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnMenu.setForeground(new java.awt.Color(0, 0, 0));
        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Old/login.png"))); // NOI18N
        btnMenu.setText("Main Menu");
        btnMenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });
        getContentPane().add(btnMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 280, 183, 48));

        btnExit.setBackground(new java.awt.Color(204, 204, 204));
        btnExit.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnExit.setForeground(new java.awt.Color(0, 0, 0));
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Old/Exit.png"))); // NOI18N
        btnExit.setText("Exit App");
        btnExit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 340, 183, 48));

        btnLdBckup.setBackground(new java.awt.Color(204, 204, 204));
        btnLdBckup.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnLdBckup.setForeground(new java.awt.Color(0, 0, 0));
        btnLdBckup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Old/product.png"))); // NOI18N
        btnLdBckup.setText("Load Backup");
        btnLdBckup.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLdBckup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLdBckupActionPerformed(evt);
            }
        });
        getContentPane().add(btnLdBckup, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 650, 170, -1));

        btnSvBckup.setBackground(new java.awt.Color(204, 204, 204));
        btnSvBckup.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        btnSvBckup.setForeground(new java.awt.Color(0, 0, 0));
        btnSvBckup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Old/save.png"))); // NOI18N
        btnSvBckup.setText("Save Backup");
        btnSvBckup.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSvBckup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSvBckupActionPerformed(evt);
            }
        });
        getContentPane().add(btnSvBckup, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 600, 170, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/DashboardBG.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        // TODO add your handling code here:
        new Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to EXIT?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            setVisible(false);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnLdBckupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLdBckupActionPerformed
        // TODO add your handling code here:
        // Define the backup directory
        File backupDir = new File("Database Backups");

        // File chooser to select the backup file
        JFileChooser fileChooser = new JFileChooser();

        // Check if the backup directory exists
        if (backupDir.exists() && backupDir.isDirectory()) {
            fileChooser.setCurrentDirectory(backupDir);
        } else {
            fileChooser.setCurrentDirectory(new File(".")); // Default to source folder
        }

        // Set the file chooser properties
        fileChooser.setDialogTitle("Select Database Backup File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Show the file chooser dialog
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Validate selected file
            if (selectedFile != null && selectedFile.exists()) {
                // Ask the user if the current file should be saved
                int saveCurrent = JOptionPane.showConfirmDialog(this, "Do you want to save the current database file before replacing it?", "Save Current Database", JOptionPane.YES_NO_OPTION);
                if (saveCurrent == JOptionPane.YES_OPTION) {
                    // Save the current database file
                    btnSvBckupActionPerformed(evt);
                }

                // Replace the current database file with the selected one
                File currentDatabase = new File("database.db");
                try {
                    Files.copy(selectedFile.toPath(), currentDatabase.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(this, "Database replaced successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error occurred while loading backup: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid file selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnLdBckupActionPerformed

    private void btnSvBckupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSvBckupActionPerformed
        // TODO add your handling code here:
        // Define the backup directory
        File backupDir = new File("Database Backups");

        // Check if the directory exists, if not, create it
        if (!backupDir.exists()) {
            if (backupDir.mkdir()) {
                System.out.println("Backup directory created successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create backup directory.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Define the source database file and backup file path
        File sourceFile = new File("database.db"); // The current database file name
        if (!sourceFile.exists()) {
            JOptionPane.showMessageDialog(this, "No database file found to back up.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
        File backupFile = new File(backupDir, "Sentinel Database Backup as at " + timestamp + ".db");

        // Copy the database file to the backup folder
        try {
            Files.copy(sourceFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(this, "Backup saved successfully: " + backupFile.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while saving backup: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSvBckupActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLdBckup;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnSvBckup;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
