package SIMS.Item;

import SIMS.Menus.Menu;
import dao.ConnectionProvider;
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
public class Acquisition_Records extends javax.swing.JFrame {

    /**
     * Creates new form ManageCustomer
     */
    public Acquisition_Records() {
        initComponents();
        setLocationRelativeTo(null);

    }

    private void populateSourceComboBox(String sourceType) {
        cboxSource.removeAllItems();
        cboxSource.addItem("All");

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT name FROM source WHERE type = '" + sourceType + "'");

            while (rs.next()) {
                cboxSource.addItem(rs.getString("name"));
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void filterAcquisitions() {
        DefaultTableModel model = (DefaultTableModel) AcquisitionTable.getModel();
        model.setRowCount(0); // Clear existing rows

        String itemId = txtItemId.getText().trim();
        String serNo = txtSerNo.getText().trim();
        String itemType = cboxItemType.getSelectedItem() != null ? cboxItemType.getSelectedItem().toString().trim() : "";
        String mainDescription = txtMainDescription.getText().trim();
        String sourceType = cboxSourceType1.getSelectedItem() != null ? cboxSourceType1.getSelectedItem().toString().trim() : "";
        String sourceName = cboxSource.getSelectedItem() != null ? cboxSource.getSelectedItem().toString().trim() : "";
        String acquisitionDateFrom = txtAcquisitionDateFrom.getText().trim();
        String acquisitionDateTo = txtAcquisitionDateTo.getText().trim();

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT ia.item_id, COALESCE(i.ser_no, d.ser_no, 'Unavailable') AS ser_no, "
                    + "COALESCE(i.category, d.item_type, 'Unavailable') AS item_type, "
                    + "COALESCE(i.mainDescription, 'Unavailable') AS mainDescription, "
                    + "ia.source_id, s.type AS source_type, s.name AS source_name, ia.acquireDate "
                    + "FROM itemAcquired ia "
                    + "LEFT JOIN item i ON ia.item_id = i.id "
                    + "LEFT JOIN disposal d ON ia.item_id = d.item_id "
                    + "JOIN source s ON ia.source_id = s.id "
                    + "WHERE 1=1 ");

            if (!itemId.isEmpty()) {
                query.append("AND ia.item_id LIKE '%").append(itemId).append("%' ");
            }
            if (!serNo.isEmpty()) {
                query.append("AND COALESCE(i.ser_no, d.ser_no) LIKE '%").append(serNo).append("%' ");
            }
            if (!itemType.equals("All") && !itemType.isEmpty()) {
                query.append("AND COALESCE(i.category, d.item_type) LIKE '%").append(itemType).append("%' ");
            }
            if (!mainDescription.isEmpty()) {
                query.append("AND COALESCE(i.mainDescription, 'Unavailable') LIKE '%").append(mainDescription).append("%' ");
            }
            if (!sourceType.equals("All") && !sourceType.isEmpty()) {
                query.append("AND s.type = '").append(sourceType).append("' ");
            }
            if (!sourceName.equals("All") && !sourceName.isEmpty()) {
                query.append("AND s.name LIKE '%").append(sourceName).append("%' ");
            }
            if (!acquisitionDateFrom.isEmpty() && !acquisitionDateTo.isEmpty()) {
                query.append("AND ia.acquireDate BETWEEN '").append(acquisitionDateFrom).append("' AND '").append(acquisitionDateTo).append("' ");
            } else if (!acquisitionDateFrom.isEmpty()) {
                query.append("AND ia.acquireDate = '").append(acquisitionDateFrom).append("' ");
            } else if (!acquisitionDateTo.isEmpty()) {
                query.append("AND ia.acquireDate = '").append(acquisitionDateTo).append("' ");
            }

            ResultSet rs = st.executeQuery(query.toString());

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("item_id"),
                    rs.getString("ser_no"),
                    rs.getString("item_type"),
                    rs.getString("mainDescription"),
                    rs.getString("source_type"),
                    rs.getString("source_name"),
                    rs.getDate("acquireDate")
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
        jScrollPane1 = new javax.swing.JScrollPane();
        AcquisitionTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtMainDescription = new javax.swing.JTextField();
        txtAcquisitionDateTo = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cboxItemType = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtSerNo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAcquisitionDateFrom = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtItemId = new javax.swing.JTextField();
        cboxSource = new javax.swing.JComboBox<>();
        cboxSourceType1 = new javax.swing.JComboBox<>();
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
        jLabel1.setText("Acquisition Records");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));

        AcquisitionTable.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        AcquisitionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Serial / Product No", "Item Type", "Main Description", "Source Type", "Source", "Acquisition Date"
            }
        ));
        AcquisitionTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        AcquisitionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AcquisitionTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(AcquisitionTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 65, 1030, 460));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel2.setText("Main Description");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 530, -1, -1));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel3.setText("Acquisition Date To [YYYY-MM-DD]");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 590, -1, -1));

        txtMainDescription.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtMainDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMainDescriptionActionPerformed(evt);
            }
        });
        txtMainDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMainDescriptionKeyReleased(evt);
            }
        });
        getContentPane().add(txtMainDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 550, 260, -1));

        txtAcquisitionDateTo.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtAcquisitionDateTo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAcquisitionDateToKeyReleased(evt);
            }
        });
        getContentPane().add(txtAcquisitionDateTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 610, 260, -1));

        btnReset.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 640, 90, -1));

        btnClose.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 670, 90, 20));

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel5.setText("Source Type");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 590, -1, -1));

        cboxItemType.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxItemType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        cboxItemType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxItemTypeItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxItemType, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 670, 260, -1));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel9.setText("Serial / Product No");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 590, -1, -1));

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
        getContentPane().add(txtSerNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 610, 260, -1));

        jLabel10.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel10.setText("Source");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 650, -1, -1));

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel11.setText("Item Type");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 650, -1, -1));

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel4.setText("Acquisition Date From [YYYY-MM-DD]");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 530, -1, -1));

        txtAcquisitionDateFrom.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtAcquisitionDateFrom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAcquisitionDateFromKeyReleased(evt);
            }
        });
        getContentPane().add(txtAcquisitionDateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 550, 260, -1));

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel12.setText("Item ID");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, -1, -1));

        txtItemId.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtItemId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtItemIdActionPerformed(evt);
            }
        });
        txtItemId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemIdKeyReleased(evt);
            }
        });
        getContentPane().add(txtItemId, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, 260, -1));

        cboxSource.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxSource.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Vendor", "Donor" }));
        cboxSource.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxSourceItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxSource, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 670, 260, -1));

        cboxSourceType1.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxSourceType1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Vendor", "Donor" }));
        cboxSourceType1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxSourceType1ItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxSourceType1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 610, 260, -1));

        btnExcelExport.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnExcelExport.setText("Export CSV");
        btnExcelExport.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExcelExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelExportActionPerformed(evt);
            }
        });
        getContentPane().add(btnExcelExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 570, 70, -1));

        btnPDF.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnPDF.setText("Export PDF");
        btnPDF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 570, 70, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/1.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMainDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMainDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMainDescriptionActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:                                 
        DefaultTableModel model = (DefaultTableModel) AcquisitionTable.getModel();
        model.setRowCount(0); // Clear existing rows

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            String query = "SELECT ia.item_id, COALESCE(i.ser_no, d.ser_no, 'Unavailable') AS ser_no, "
                    + "COALESCE(i.category, d.item_type, 'Unavailable') AS item_type, "
                    + "COALESCE(i.mainDescription, 'Unavailable') AS mainDescription, "
                    + "ia.source_id, s.type AS source_type, s.name AS source_name, ia.acquireDate "
                    + "FROM itemAcquired ia "
                    + "LEFT JOIN item i ON ia.item_id = i.id "
                    + "LEFT JOIN disposal d ON ia.item_id = d.item_id "
                    + "JOIN source s ON ia.source_id = s.id";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("item_id"),
                    rs.getString("ser_no"),
                    rs.getString("item_type"),
                    rs.getString("mainDescription"),
                    rs.getString("source_type"),
                    rs.getString("source_name"),
                    rs.getDate("acquireDate")
                });
            }

            // Populate the item type combo box with distinct item types from the item table
            ResultSet rsItemType = st.executeQuery("SELECT DISTINCT category AS item_type FROM item");
            cboxItemType.removeAllItems(); // Clear existing items
            cboxItemType.addItem("All"); // Add "All" once
            while (rsItemType.next()) {
                cboxItemType.addItem(rsItemType.getString("item_type"));
            }

            // Populate the source type combo box
            populateSourceComboBox("Vendor");

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_formComponentShown

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        new Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Acquisition_Records().setVisible(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void AcquisitionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AcquisitionTableMouseClicked

    }//GEN-LAST:event_AcquisitionTableMouseClicked

    private void txtSerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerNoActionPerformed

    private void txtSerNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerNoKeyReleased
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_txtSerNoKeyReleased

    private void txtMainDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMainDescriptionKeyReleased
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_txtMainDescriptionKeyReleased

    private void txtAcquisitionDateFromKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAcquisitionDateFromKeyReleased
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_txtAcquisitionDateFromKeyReleased

    private void txtAcquisitionDateToKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAcquisitionDateToKeyReleased
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_txtAcquisitionDateToKeyReleased

    private void cboxItemTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxItemTypeItemStateChanged
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_cboxItemTypeItemStateChanged

    private void txtItemIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemIdActionPerformed

    private void txtItemIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemIdKeyReleased
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_txtItemIdKeyReleased

    private void cboxSourceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxSourceItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            filterAcquisitions();
        }
    }//GEN-LAST:event_cboxSourceItemStateChanged

    private void cboxSourceType1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxSourceType1ItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String selectedSourceType = cboxSourceType1.getSelectedItem().toString();
            populateSourceComboBox(selectedSourceType);
            filterAcquisitions();
        }
    }//GEN-LAST:event_cboxSourceType1ItemStateChanged

    private void btnExcelExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelExportActionPerformed
        // Open file chooser for saving
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setSelectedFile(new File("AcquisitionRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".csv"));

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
                DefaultTableModel model = (DefaultTableModel) AcquisitionTable.getModel();

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
        fileChooser.setSelectedFile(new File("AcquisitionRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".pdf"));

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

            PDFExporter pdfExporter = new PDFExporter("Acquisition Records", "Human Rights Commission Sri Lanka", footerText, filePath);

            try {
                // Export the PDF
                pdfExporter.exportPDF(AcquisitionTable.getModel());

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
            java.util.logging.Logger.getLogger(Acquisition_Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Acquisition_Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Acquisition_Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Acquisition_Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Acquisition_Records().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AcquisitionTable;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExcelExport;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cboxItemType;
    private javax.swing.JComboBox<String> cboxSource;
    private javax.swing.JComboBox<String> cboxSourceType1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtAcquisitionDateFrom;
    private javax.swing.JTextField txtAcquisitionDateTo;
    private javax.swing.JTextField txtItemId;
    private javax.swing.JTextField txtMainDescription;
    private javax.swing.JTextField txtSerNo;
    // End of variables declaration//GEN-END:variables
}
