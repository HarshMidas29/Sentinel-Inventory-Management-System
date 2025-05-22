package SIMS.Assignments;

import dao.ConnectionProvider;
import SIMS.Menus.Assignment_Menu;
import dao.PDFExporter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Daham Yakandawala
 */
public class Assignment_History extends javax.swing.JFrame {

    /**
     * Creates new form ManageCustomer
     */
    public Assignment_History() {
        initComponents();
        setLocationRelativeTo(null);
        populateLastUserComboBox();
        populateLastDivComboBox();
        populateLastOfficeComboBox();
    }

    private void populateItemTypeComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT category FROM item");
            cboxItemType.removeAllItems();
            cboxItemType.addItem("All");
            while (rs.next()) {
                cboxItemType.addItem(rs.getString("category"));
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void populateLastUserComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name FROM employee");
            cboxAssignedUser.removeAllItems();
            cboxAssignedUser.addItem("All");
            while (rs.next()) {
                cboxAssignedUser.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void populateLastDivComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name FROM division_section_project");
            cboxDiv.removeAllItems();
            cboxDiv.addItem("All");
            while (rs.next()) {
                cboxDiv.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void populateLastOfficeComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name FROM office");
            cboxOffice.removeAllItems();
            cboxOffice.addItem("All");
            while (rs.next()) {
                cboxOffice.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void filterRecords() {
        DefaultTableModel model = (DefaultTableModel) AssignmentHistoryTable.getModel();
        model.setRowCount(0);

        String itemId = txtItemID.getText().trim();
        String serNo = txtSerNo.getText().trim();
        String itemType = cboxItemType.getSelectedItem() != null ? cboxItemType.getSelectedItem().toString() : "";
        String reason = txtReason.getText().trim();
        String lastUser = cboxAssignedUser.getSelectedItem() != null ? cboxAssignedUser.getSelectedItem().toString() : "";
        String lastDiv = cboxDiv.getSelectedItem() != null ? cboxDiv.getSelectedItem().toString() : "";
        String lastOffice = cboxOffice.getSelectedItem() != null ? cboxOffice.getSelectedItem().toString() : "";

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();

            StringBuilder query = new StringBuilder();
            query.append(
                    "SELECT ah.*, COALESCE(i.category, d.item_type) AS category "
                    + "FROM assignmentHistory ah "
                    + "LEFT JOIN item i ON ah.item_id = i.id "
                    + "LEFT JOIN disposal d ON ah.item_id = d.item_id "
                    + "WHERE 1=1 "
            );

            if (!itemId.isEmpty()) {
                query.append("AND ah.item_id = ").append(itemId).append(" ");
            }
            if (!serNo.isEmpty()) {
                query.append("AND ah.ser_no LIKE '%").append(serNo).append("%' ");
            }
            if (!itemType.equals("All") && !itemType.isEmpty()) {
                query.append("AND COALESCE(i.category, d.item_type) = '").append(itemType).append("' ");
            }
            if (!reason.isEmpty()) {
                query.append("AND ah.purpose LIKE '%").append(reason).append("%' ");
            }
            if (!lastUser.equals("All") && !lastUser.isEmpty()) {
                query.append("AND ah.assigned_employee = '").append(lastUser).append("' ");
            }
            if (!lastDiv.equals("All") && !lastDiv.isEmpty()) {
                query.append("AND ah.assigned_division = '").append(lastDiv).append("' ");
            }
            if (!lastOffice.equals("All") && !lastOffice.isEmpty()) {
                query.append("AND ah.assigned_office = '").append(lastOffice).append("' ");
            }

            ResultSet rs = st.executeQuery(query.toString());

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("item_id"),
                    rs.getString("ser_no"),
                    rs.getString("category"),
                    rs.getString("assigned_employee"),
                    rs.getString("assigned_division"),
                    rs.getString("assigned_office"),
                    rs.getString("purpose"),
                    rs.getDate("assignedDate"),
                    rs.getDate("returnDate")
                });
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtItemID = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtReason = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtSerNo = new javax.swing.JTextField();
        cboxItemType = new javax.swing.JComboBox<>();
        cboxAssignedUser = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cboxDiv = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        cboxOffice = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AssignmentHistoryTable = new javax.swing.JTable();
        btnExcelExport = new javax.swing.JButton();
        btnPDF = new javax.swing.JButton();
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
        jLabel1.setText("Assignment History");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        jLabel2.setText("Filter By");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 550, -1, -1));

        txtItemID.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtItemID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtItemIDActionPerformed(evt);
            }
        });
        txtItemID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemIDKeyReleased(evt);
            }
        });
        getContentPane().add(txtItemID, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 610, 220, -1));

        btnReset.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 690, 80, -1));

        btnClose.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 690, 80, -1));

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel5.setText("Item Type");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 590, -1, -1));

        jLabel10.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel10.setText("Purpose");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 650, -1, -1));

        txtReason.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtReason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReasonActionPerformed(evt);
            }
        });
        txtReason.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtReasonKeyReleased(evt);
            }
        });
        getContentPane().add(txtReason, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 670, 220, -1));

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel8.setText("Serial / Product No");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 650, -1, -1));

        txtSerNo.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtSerNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSerNoActionPerformed(evt);
            }
        });
        txtSerNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSerNoKeyReleased(evt);
            }
        });
        getContentPane().add(txtSerNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 670, 220, -1));

        cboxItemType.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxItemType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxItemTypeItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxItemType, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 610, 220, -1));

        cboxAssignedUser.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxAssignedUser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxAssignedUserItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxAssignedUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 610, 220, -1));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel9.setText("User");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 590, -1, -1));

        cboxDiv.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxDiv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxDivItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxDiv, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 670, 220, -1));

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel12.setText("Division / Section / Project");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 650, -1, -1));

        cboxOffice.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxOffice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxOfficeItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 610, 220, -1));

        jLabel13.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel13.setText("Office");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 590, -1, -1));

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel4.setText("Item ID");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 590, -1, -1));

        AssignmentHistoryTable.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        AssignmentHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Serial / Product No", "Item Type", "Assigned User", "Assigned Div / Sec / Proj", "Assigned Office", "Purpose", "Assigned Date", "Returned Date"
            }
        ));
        AssignmentHistoryTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(AssignmentHistoryTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1060, 470));

        btnExcelExport.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnExcelExport.setText("Export CSV");
        btnExcelExport.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExcelExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelExportActionPerformed(evt);
            }
        });
        getContentPane().add(btnExcelExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 660, 70, 20));

        btnPDF.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnPDF.setText("Export PDF");
        btnPDF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 660, 70, 20));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/1.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtItemIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemIDActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:         
        populateItemTypeComboBox();
        populateLastUserComboBox();
        populateLastDivComboBox();
        populateLastOfficeComboBox();

        DefaultTableModel model = (DefaultTableModel) AssignmentHistoryTable.getModel();
        model.setRowCount(0); // Clear existing rows

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT ah.*, COALESCE(i.category, d.item_type) AS category "
                    + "FROM assignmentHistory ah "
                    + "LEFT JOIN item i ON ah.item_id = i.id "
                    + "LEFT JOIN disposal d ON ah.item_id = d.item_id"
            );
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("item_id"),
                    rs.getString("ser_no"),
                    rs.getString("category"),
                    rs.getString("assigned_employee"),
                    rs.getString("assigned_division"),
                    rs.getString("assigned_office"),
                    rs.getString("purpose"),
                    rs.getDate("assignedDate"),
                    rs.getDate("returnDate")
                });
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_formComponentShown

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        new Assignment_Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Assignment_History().setVisible(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReasonActionPerformed

    private void txtItemIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemIDKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtItemIDKeyReleased

    private void txtReasonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReasonKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtReasonKeyReleased

    private void txtSerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerNoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtSerNoActionPerformed

    private void txtSerNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerNoKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtSerNoKeyReleased

    private void cboxItemTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxItemTypeItemStateChanged
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_cboxItemTypeItemStateChanged

    private void cboxAssignedUserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxAssignedUserItemStateChanged
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_cboxAssignedUserItemStateChanged

    private void cboxDivItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxDivItemStateChanged
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_cboxDivItemStateChanged

    private void cboxOfficeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxOfficeItemStateChanged
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_cboxOfficeItemStateChanged

    private void btnExcelExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelExportActionPerformed
        // Open file chooser for saving
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setSelectedFile(new File("AssignmentHistoryRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".csv"));

        // Show save dialog
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Ensure the file has the correct extension
            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }

            try (FileWriter csvWriter = new FileWriter(filePath)) {
                DefaultTableModel model = (DefaultTableModel) AssignmentHistoryTable.getModel();

                // Write the header row
                for (int col = 0; col < model.getColumnCount(); col++) {
                    csvWriter.append(model.getColumnName(col));
                    if (col < model.getColumnCount() - 1) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");

                // Write data rows
                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = model.getValueAt(row, col);
                        csvWriter.append(value != null ? value.toString() : "");
                        if (col < model.getColumnCount() - 1) {
                            csvWriter.append(",");
                        }
                    }
                    csvWriter.append("\n");
                }

                JOptionPane.showMessageDialog(this, "Data exported successfully to " + filePath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving CSV file: " + e.getMessage());
            }
        }

    }//GEN-LAST:event_btnExcelExportActionPerformed

    private void btnPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFActionPerformed
        // Open file chooser for saving
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF File");
        fileChooser.setSelectedFile(new File("AssignmentHistoryRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".pdf"));

        // Show save dialog
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Ensure the file has the correct extension
            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }

            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String timeStr = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String footerText = "Report generated by Sentinel Inventory Management System on " + dateStr + " at " + timeStr;

            PDFExporter pdfExporter = new PDFExporter("Assignment History Records", "Human Rights Commission Sri Lanka", footerText, filePath);

            try {
                // Export the PDF
                pdfExporter.exportPDF(AssignmentHistoryTable.getModel());

                JOptionPane.showMessageDialog(this, "PDF exported successfully to " + filePath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving PDF file: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnPDFActionPerformed

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
            java.util.logging.Logger.getLogger(Assignment_History.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Assignment_History.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Assignment_History.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Assignment_History.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Assignment_History().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AssignmentHistoryTable;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExcelExport;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cboxAssignedUser;
    private javax.swing.JComboBox<String> cboxDiv;
    private javax.swing.JComboBox<String> cboxItemType;
    private javax.swing.JComboBox<String> cboxOffice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtReason;
    private javax.swing.JTextField txtSerNo;
    // End of variables declaration//GEN-END:variables
}
