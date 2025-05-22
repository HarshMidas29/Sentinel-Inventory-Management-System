package SIMS.Disposals;

import SIMS.Menus.Disposal_Menu;
import dao.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Daham Yakandawala
 */
public class Dispose_Item extends javax.swing.JFrame {

    private int disposePk = 0;

    /**
     * Creates new form ManageCustomer
     */
    public Dispose_Item() {
        initComponents();
        setLocationRelativeTo(null);
        populateComboBoxes();
    }

    private void populateComboBoxes() {
        try (Connection con = ConnectionProvider.getCon(); Statement st = con.createStatement()) {

            // Populate Category combo box
            ResultSet rs = st.executeQuery("SELECT DISTINCT category FROM item");
            while (rs.next()) {
                cboxCategory.addItem(rs.getString("category"));
            }

            // Clear Item ID and Serial Number combo boxes
            cboxItemID.removeAllItems();
            cboxSerNo.removeAllItems();
            cboxItemID.addItem("Select");
            cboxSerNo.addItem("Select");

            // Populate Officer Taking Over combo box
            rs = st.executeQuery("SELECT id, name FROM employee");
            while (rs.next()) {
                cboxOfficerTO.addItem(rs.getString("id") + " - " + rs.getString("name"));
            }

            // Populate Item ID and Serial Number combo boxes with all items
            rs = st.executeQuery("SELECT id, ser_no FROM item");
            while (rs.next()) {
                cboxItemID.addItem(rs.getString("id"));
                cboxSerNo.addItem(rs.getString("ser_no"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void filterItemsByCategory() {
        String selectedCategory = (String) cboxCategory.getSelectedItem();

        cboxItemID.removeAllItems();
        cboxSerNo.removeAllItems();
        cboxItemID.addItem("Select");
        cboxSerNo.addItem("Select");

        if (selectedCategory.equals("Select")) {
            // Populate Item ID and Serial Number combo boxes with all items
            try (Connection con = ConnectionProvider.getCon(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT id, ser_no FROM item")) {
                while (rs.next()) {
                    cboxItemID.addItem(rs.getString("id"));
                    cboxSerNo.addItem(rs.getString("ser_no"));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            return;
        }

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement("SELECT id, ser_no FROM item WHERE category = ?")) {
            ps.setString(1, selectedCategory);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cboxItemID.addItem(rs.getString("id"));
                cboxSerNo.addItem(rs.getString("ser_no"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void selectItemByID() {
        String selectedID = (String) cboxItemID.getSelectedItem();
        if (selectedID.equals("Select")) {
            return;
        }

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement("SELECT ser_no FROM item WHERE id = ?")) {
            ps.setString(1, selectedID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cboxSerNo.setSelectedItem(rs.getString("ser_no"));
            }
            updateAssignmentDetails(selectedID);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void selectItemBySerNo() {
        String selectedSerNo = (String) cboxSerNo.getSelectedItem();
        if (selectedSerNo.equals("Select")) {
            return;
        }

        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement("SELECT id FROM item WHERE ser_no = ?")) {
            ps.setString(1, selectedSerNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cboxItemID.setSelectedItem(rs.getString("id"));
            }
            updateAssignmentDetails(rs.getString("id"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void updateAssignmentDetails(String itemID) {
        try (Connection con = ConnectionProvider.getCon(); PreparedStatement ps = con.prepareStatement("SELECT assigned_employee, assigned_division, assigned_office FROM assignedTo WHERE item_id = ?")) {
            ps.setString(1, itemID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jLabelLastEmp.setText(rs.getString("assigned_employee") != null ? rs.getString("assigned_employee") : "0 - Not Currently Assigned");
                jLabelLastDiv.setText(rs.getString("assigned_division") != null ? rs.getString("assigned_division") : "0 - Not Currently Assigned");
                jLabelLastOffice.setText(rs.getString("assigned_office") != null ? rs.getString("assigned_office") : "0 - Not Currently Assigned");
            } else {
                jLabelLastEmp.setText("0 - Not Currently Assigned");
                jLabelLastDiv.setText("0 - Not Currently Assigned");
                jLabelLastOffice.setText("0 - Not Currently Assigned");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private boolean validateInputs() {
        if (cboxCategory.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please select a category.");
            return false;
        }
        if (cboxItemID.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please select an item ID.");
            return false;
        }
        if (cboxSerNo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please select a serial number.");
            return false;
        }
        if (txtReason.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a reason for disposal.");
            return false;
        }
        if (txtDisposalDate.getText().trim().isEmpty() || !isValidDate(txtDisposalDate.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Please enter a valid disposal date (YYYY-MM-DD).");
            return false;
        }
        if (cboxOfficerTO.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please select an officer taking over.");
            return false;
        }
        return true;
    }

    private boolean isValidDate(String date) {
        try {
            Date.valueOf(date);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void disposeItem() {

        if (!validateInputs()) {
            return; // Stop if validation fails
        }
        String selectedItemID = (String) cboxItemID.getSelectedItem();
        String selectedSerNo = (String) cboxSerNo.getSelectedItem();
        String selectedCategory = (String) cboxCategory.getSelectedItem();
        String reason = txtReason.getText();
        String disposalDate = txtDisposalDate.getText();
        String lastEmp = jLabelLastEmp.getText();
        String lastDiv = jLabelLastDiv.getText();
        String lastOffice = jLabelLastOffice.getText();
        String officerTO = (String) cboxOfficerTO.getSelectedItem();

        Connection con = null;

        try {
            con = ConnectionProvider.getCon();
            con.setAutoCommit(false);

            // Insert records into assignmentHistory table, saving disposal date as return date
            try (PreparedStatement psInsertAssignmentHistory = con.prepareStatement(
                    "INSERT INTO assignmentHistory (item_id, ser_no, assigned_employee, assigned_division, assigned_office, purpose, assignedDate, returnDate) "
                    + "SELECT item_id, ser_no, assigned_employee, assigned_division, assigned_office, purpose, assignedDate, ? FROM assignedTo WHERE item_id = ?")) {
                psInsertAssignmentHistory.setDate(1, Date.valueOf(disposalDate));
                psInsertAssignmentHistory.setString(2, selectedItemID);
                psInsertAssignmentHistory.executeUpdate();
            }

            // Insert records into disposal table
            try (PreparedStatement psInsertDisposal = con.prepareStatement(
                    "INSERT INTO disposal (item_id, ser_no, item_type, reason, last_user_id, last_division_id, last_office_id, disposalDate, officerTakingOver) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                psInsertDisposal.setString(1, selectedItemID);
                psInsertDisposal.setString(2, selectedSerNo);
                psInsertDisposal.setString(3, selectedCategory);
                psInsertDisposal.setString(4, reason);
                psInsertDisposal.setString(5, lastEmp.split(" - ")[0]); // Assuming lastEmp is in the format "[id] - [name]"
                psInsertDisposal.setString(6, lastDiv.split(" - ")[0]); // Assuming lastDiv is in the format "[id] - [name]"
                psInsertDisposal.setString(7, lastOffice.split(" - ")[0]); // Assuming lastOffice is in the format "[id] - [name]"
                psInsertDisposal.setDate(8, Date.valueOf(disposalDate));
                psInsertDisposal.setString(9, officerTO.split(" - ")[0]); // Assuming officerTO is in the format "[id] - [name]"
                psInsertDisposal.executeUpdate();
            }

            // Delete related records in assignedTo table
            try (PreparedStatement psDeleteAssignedTo = con.prepareStatement("DELETE FROM assignedTo WHERE item_id = ?")) {
                psDeleteAssignedTo.setString(1, selectedItemID);
                psDeleteAssignedTo.executeUpdate();
            }

            // Delete the item from item table
            try (PreparedStatement psDeleteItem = con.prepareStatement("DELETE FROM item WHERE id = ?")) {
                psDeleteItem.setString(1, selectedItemID);
                psDeleteItem.executeUpdate();
            }

            con.commit();
            JOptionPane.showMessageDialog(null, "Item disposed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(null, e);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnDisposeItem = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtReason = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtDisposalDate = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cboxItemID = new javax.swing.JComboBox<>();
        cboxCategory = new javax.swing.JComboBox<>();
        jLabelLastOffice = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cboxOfficerTO = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cboxSerNo = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabelLastEmp = new javax.swing.JLabel();
        jLabelLastDiv = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Book", 1, 36)); // NOI18N
        jLabel1.setText("Dispose Item");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel2.setText("Category");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, -1, -1));

        btnDisposeItem.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnDisposeItem.setText("Dispose Item");
        btnDisposeItem.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnDisposeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisposeItemActionPerformed(evt);
            }
        });
        getContentPane().add(btnDisposeItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 640, 90, -1));

        btnReset.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 640, 90, -1));

        btnClose.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 680, 90, -1));

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel8.setText("Item ID");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, -1, -1));

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel7.setText("Officer Taking Over");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 390, -1, -1));

        txtReason.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReasonActionPerformed(evt);
            }
        });
        getContentPane().add(txtReason, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 290, 330, -1));

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel11.setText("Reason");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 270, -1, -1));

        txtDisposalDate.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtDisposalDate.setText("[YYYY-MM-DD]");
        getContentPane().add(txtDisposalDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 350, 330, -1));

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel12.setText("Disposal Date");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 330, -1, -1));

        cboxItemID.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxItemID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        cboxItemID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxItemIDItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxItemID, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 170, 330, -1));

        cboxCategory.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        cboxCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxCategoryItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, 330, -1));

        jLabelLastOffice.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabelLastOffice.setText("Select Item");
        getContentPane().add(jLabelLastOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 600, 330, -1));

        jLabel14.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel14.setText("Last Assigned Division / Section / Project");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 510, -1, -1));

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel15.setText("Last Assigned Office");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 570, -1, -1));

        cboxOfficerTO.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxOfficerTO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        cboxOfficerTO.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxOfficerTOItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxOfficerTO, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 410, 330, -1));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel9.setText("Serial No");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, -1, -1));

        cboxSerNo.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxSerNo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        cboxSerNo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxSerNoItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxSerNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 230, 330, -1));

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel16.setText("Last Assigned Employee");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 450, -1, -1));

        jLabelLastEmp.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabelLastEmp.setText("Select Item");
        getContentPane().add(jLabelLastEmp, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 480, 330, -1));

        jLabelLastDiv.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabelLastDiv.setText("Select Item");
        getContentPane().add(jLabelLastDiv, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 540, 330, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/1.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_formComponentShown

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        new Disposal_Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Dispose_Item().setVisible(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnDisposeItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisposeItemActionPerformed
        // TODO add your handling code here:
        disposeItem();
    }//GEN-LAST:event_btnDisposeItemActionPerformed

    private void txtReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReasonActionPerformed

    private void cboxCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxCategoryItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            filterItemsByCategory();
        }
    }//GEN-LAST:event_cboxCategoryItemStateChanged

    private void cboxOfficerTOItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxOfficerTOItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboxOfficerTOItemStateChanged

    private void cboxItemIDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxItemIDItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            selectItemByID();
        }
    }//GEN-LAST:event_cboxItemIDItemStateChanged

    private void cboxSerNoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxSerNoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            selectItemBySerNo();
        }
    }//GEN-LAST:event_cboxSerNoItemStateChanged

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
            java.util.logging.Logger.getLogger(Dispose_Item.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dispose_Item.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dispose_Item.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dispose_Item.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dispose_Item().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDisposeItem;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cboxCategory;
    private javax.swing.JComboBox<String> cboxItemID;
    private javax.swing.JComboBox<String> cboxOfficerTO;
    private javax.swing.JComboBox<String> cboxSerNo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelLastDiv;
    private javax.swing.JLabel jLabelLastEmp;
    private javax.swing.JLabel jLabelLastOffice;
    private javax.swing.JTextField txtDisposalDate;
    private javax.swing.JTextField txtReason;
    // End of variables declaration//GEN-END:variables
}
