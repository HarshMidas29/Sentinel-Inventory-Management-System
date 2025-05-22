/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SIMS.Item;

import SIMS.Menus.Item_Menu;
import dao.ConnectionProvider;
import java.io.File;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.io.IOException;
import dao.PDFExporter;
import dao.PasswordDialog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author Daham Yakandawala
 */
public class View_Item extends javax.swing.JFrame {

    /**
     * Creates new form Item_Main
     */
    public View_Item() {
        initComponents();
        setLocationRelativeTo(null);
        populateCategoryComboBox();
        populateBrandComboBox();
        filterRecords();
    }

    private void populateCategoryComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT category FROM item");
            cboxCategory.removeAllItems(); // Clear existing items
            cboxCategory.addItem("All"); // Add "All" once
            while (rs.next()) {
                cboxCategory.addItem(rs.getString("category"));
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void populateBrandComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT brand FROM item");
            cboxBrand.removeAllItems(); // Clear existing items
            cboxBrand.addItem("All"); // Add "All" once
            while (rs.next()) {
                cboxBrand.addItem(rs.getString("brand"));
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void filterRecords() {
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();
        model.setRowCount(0); // Clear existing rows

        String itemId = txtId.getText().trim(); // Assuming txtId is the JTextField for item ID
        String serNo = txtSerNo.getText().trim();
        String category = cboxCategory.getSelectedItem() != null ? cboxCategory.getSelectedItem().toString() : "";
        String mainDescription = txtMainDescription.getText().trim();
        String description = txtDescription.getText().trim();
        String modelTxt = txtModel.getText().trim();
        String brand = cboxBrand.getSelectedItem() != null ? cboxBrand.getSelectedItem().toString() : "";
        String warrantyEnd = txtWarrantyEnd.getText().trim();
        String sortBy = cboxSortBy.getSelectedItem() != null ? cboxSortBy.getSelectedItem().toString() : "ID";

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT i.id, i.ser_no, i.category, i.mainDescription, i.description, i.model, i.brand, ia.warrantyExpireDate ")
                    .append("FROM item i ")
                    .append("LEFT JOIN itemAcquired ia ON i.id = ia.item_id ")
                    .append("WHERE 1=1 ");

            if (!itemId.isEmpty()) {
                query.append("AND i.id LIKE '%").append(itemId).append("%' ");
            }
            if (!serNo.isEmpty()) {
                query.append("AND i.ser_no LIKE '%").append(serNo).append("%' ");
            }
            if (!category.equals("All")) {
                query.append("AND i.category = '").append(category).append("' ");
            }
            if (!mainDescription.isEmpty()) {
                query.append("AND i.mainDescription LIKE '%").append(mainDescription).append("%' ");
            }
            if (!description.isEmpty()) {
                query.append("AND i.description LIKE '%").append(description).append("%' ");
            }
            if (!modelTxt.isEmpty()) {
                query.append("AND i.model LIKE '%").append(modelTxt).append("%' ");
            }
            if (!brand.equals("All")) {
                query.append("AND i.brand = '").append(brand).append("' ");
            }
            if (!warrantyEnd.isEmpty()) {
                query.append("AND ia.warrantyExpireDate LIKE '%").append(warrantyEnd).append("%' ");
            }

            query.append("ORDER BY ").append(sortBy.equals("Serial / Product No") ? "ser_no" : sortBy.replace(" ", "")).append(" ASC");

            ResultSet rs = st.executeQuery(query.toString());

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("ser_no"), // Added ser_no to the displayed columns
                    rs.getString("category"),
                    rs.getString("mainDescription"),
                    rs.getString("description"),
                    rs.getString("model"),
                    rs.getString("brand"),
                    rs.getDate("warrantyExpireDate")
                });
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblInventory = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtSerNo = new javax.swing.JTextField();
        txtWarrantyEnd = new javax.swing.JTextField();
        txtMainDescription = new javax.swing.JTextField();
        txtModel = new javax.swing.JTextField();
        cboxCategory = new javax.swing.JComboBox<>();
        cboxBrand = new javax.swing.JComboBox<>();
        btnExcelExport = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        cboxSortBy = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        btnClose1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        btnPDF = new javax.swing.JButton();
        btnEdir = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblInventory.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        tblInventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Serial / Product No", "Category", "Main Description", "Description", "Model", "Brand", "Warranty End "
            }
        ));
        tblInventory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInventoryMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblInventory);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 1010, 400));

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Book", 1, 36)); // NOI18N
        jLabel1.setText("INVENTORY");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel3.setText("Serial / Product No");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, -1, -1));

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel5.setText("Category");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, -1, -1));

        jLabel6.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel6.setText("Main Description");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 670, -1, 20));

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel7.setText("Model");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 580, -1, -1));

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel8.setText("Brand");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 630, -1, -1));

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel11.setText("Warranty End [YYYY-MM-DD]");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 670, -1, -1));

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
        getContentPane().add(txtSerNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 570, 280, -1));

        txtWarrantyEnd.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtWarrantyEnd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtWarrantyEndKeyReleased(evt);
            }
        });
        getContentPane().add(txtWarrantyEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 670, 280, -1));

        txtMainDescription.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtMainDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMainDescriptionKeyReleased(evt);
            }
        });
        getContentPane().add(txtMainDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 670, 280, -1));

        txtModel.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtModel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtModelKeyReleased(evt);
            }
        });
        getContentPane().add(txtModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 570, 280, -1));

        cboxCategory.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxCategoryItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 620, 280, -1));

        cboxBrand.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxBrand.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxBrandItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 620, 280, -1));

        btnExcelExport.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnExcelExport.setText("Export CSV");
        btnExcelExport.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExcelExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelExportActionPerformed(evt);
            }
        });
        getContentPane().add(btnExcelExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 600, 70, 20));

        jLabel14.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel14.setText("Description");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 530, -1, -1));

        txtDescription.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescriptionKeyReleased(evt);
            }
        });
        getContentPane().add(txtDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 520, 280, -1));

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        jLabel15.setText("Filter By");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 470, -1, -1));

        cboxSortBy.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxSortBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Serial / Product No", "Category", "Main Description", "Description", "Model", "Brand" }));
        cboxSortBy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxSortByItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxSortBy, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 510, 140, -1));

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel16.setText("Sort By");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 480, -1, -1));

        btnClose1.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClose1.setText("Close");
        btnClose1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 670, 70, 20));

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel4.setText("ID");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, -1, -1));

        txtId.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });
        txtId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdKeyReleased(evt);
            }
        });
        getContentPane().add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 520, 280, -1));

        btnClear.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClear.setText("Reset");
        btnClear.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        getContentPane().add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 670, 70, 20));

        btnPDF.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnPDF.setText("Export PDF");
        btnPDF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 600, 70, 20));

        btnEdir.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnEdir.setText("Edit");
        btnEdir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEdir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdirActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdir, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 570, 70, 20));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/1.png"))); // NOI18N
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExcelExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelExportActionPerformed
        // Open file chooser for saving
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setSelectedFile(new File("InventoryData_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".csv"));

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
                // Get the table model to access table data
                DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();

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
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving CSV file: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnExcelExportActionPerformed

    private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Item_Menu().setVisible(true);
    }//GEN-LAST:event_btnClose1ActionPerformed

    private void txtSerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerNoActionPerformed

    private void txtSerNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerNoKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtSerNoKeyReleased

    private void cboxCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxCategoryItemStateChanged
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_cboxCategoryItemStateChanged

    private void txtMainDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMainDescriptionKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtMainDescriptionKeyReleased

    private void txtDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescriptionKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtDescriptionKeyReleased

    private void txtModelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModelKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtModelKeyReleased

    private void txtWarrantyEndKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWarrantyEndKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtWarrantyEndKeyReleased

    private void cboxBrandItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxBrandItemStateChanged
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_cboxBrandItemStateChanged

    private void cboxSortByItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxSortByItemStateChanged
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_cboxSortByItemStateChanged

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        filterRecords();
        populateCategoryComboBox();
        populateBrandComboBox();
    }//GEN-LAST:event_formComponentShown

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void txtIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtIdKeyReleased

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        new View_Item().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFActionPerformed
        // Open file chooser for saving
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF File");
        fileChooser.setSelectedFile(new File("InventoryData_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".pdf"));

        // Show save dialog
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Ensure the file has the correct extension
            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }

            // Define variables for the PDF
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String timeStr = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String footerText = "Report generated by Sentinel Inventory Management System on " + dateStr + " at " + timeStr;

            // Initialize the PDFExporter and export without filters
            PDFExporter pdfExporter = new PDFExporter("Inventory Data Report", "Human Rights Commission Sri Lanka", footerText, filePath);

            try {
                // Attempt to export the PDF
                pdfExporter.exportPDF(tblInventory.getModel());

                // Show success message
                JOptionPane.showMessageDialog(this, "PDF Report saved successfully at:\n" + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                // Show error message if there's an issue
                JOptionPane.showMessageDialog(this, "Failed to save PDF Report.\nError: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnPDFActionPerformed

    private void btnEdirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdirActionPerformed
        // TODO add your handling code here:

        if (tblInventory.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Open PasswordDialog
        PasswordDialog passwordDialog = new PasswordDialog(this, this::proceedWithEdit);
        passwordDialog.setVisible(true);
    }//GEN-LAST:event_btnEdirActionPerformed

    // Proceed with editing after password validation
    private void proceedWithEdit() {
        int selectedRow = tblInventory.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();

        int id = (int) model.getValueAt(selectedRow, 0);
        String serNo = (String) model.getValueAt(selectedRow, 1);
        String category = (String) model.getValueAt(selectedRow, 2);
        String mainDescription = (String) model.getValueAt(selectedRow, 3);
        String description = (String) model.getValueAt(selectedRow, 4);
        String modelTxt = (String) model.getValueAt(selectedRow, 5);
        String brand = (String) model.getValueAt(selectedRow, 6);
        java.util.Date warrantyEnd = (java.util.Date) model.getValueAt(selectedRow, 7);

        EditWindow editWindow = new EditWindow(id, serNo, category, mainDescription, description, modelTxt, brand, warrantyEnd, null);
        editWindow.setVisible(true);
    }

    private int selectedId;
    private String selectedSerNo;
    private String selectedCategory;
    private String selectedMainDescription;
    private String selectedDescription;
    private String selectedModel;
    private String selectedBrand;
    private Date selectedWarrantyEnd;


    private void tblInventoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInventoryMouseClicked
        // TODO add your handling code here:
        int row = tblInventory.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tblInventory.getModel();

        selectedId = (int) model.getValueAt(row, 0);
        selectedSerNo = model.getValueAt(row, 1).toString();
        selectedCategory = model.getValueAt(row, 2).toString();
        selectedMainDescription = model.getValueAt(row, 3).toString();
        selectedDescription = model.getValueAt(row, 4).toString();
        selectedModel = model.getValueAt(row, 5).toString();
        selectedBrand = model.getValueAt(row, 6).toString();
        selectedWarrantyEnd = (Date) model.getValueAt(row, 7);
    }//GEN-LAST:event_tblInventoryMouseClicked

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
            java.util.logging.Logger.getLogger(View_Item.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View_Item.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View_Item.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View_Item.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new View_Item().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClose1;
    private javax.swing.JButton btnEdir;
    private javax.swing.JButton btnExcelExport;
    private javax.swing.JButton btnPDF;
    private javax.swing.JComboBox<String> cboxBrand;
    private javax.swing.JComboBox<String> cboxCategory;
    private javax.swing.JComboBox<String> cboxSortBy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblInventory;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtMainDescription;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtSerNo;
    private javax.swing.JTextField txtWarrantyEnd;
    // End of variables declaration//GEN-END:variables
}
