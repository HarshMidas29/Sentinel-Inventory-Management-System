package SIMS.Maintenance;

import SIMS.Menus.Menu;
import dao.ConnectionProvider;
import SIMS.Menus.Maintenance_Menu;
import dao.PDFExporter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
public class Maintenance_Records extends javax.swing.JFrame {

    /**
     * Creates new form ManageCustomer
     */
    public Maintenance_Records() {
        initComponents();
        setLocationRelativeTo(null);
        populateCompanyComboBox();

    }

    private void populateCompanyComboBox() {
        cboxCompany.removeAllItems();
        cboxCompany.addItem("All"); // Add an option to view all companies

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();

            // Get distinct company IDs from maintenance_in table
            ResultSet rs = st.executeQuery("SELECT DISTINCT company_id FROM maintenance_in WHERE company_id IS NOT NULL");

            while (rs.next()) {
                String companyId = rs.getString("company_id");
                cboxCompany.addItem(companyId); // Add company_id to the combo box
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error populating company combo box: " + e.getMessage());
        }
    }

    private void filterAcquisitions() {
        DefaultTableModel model = (DefaultTableModel) AcquisitionTable.getModel();
        model.setRowCount(0); // Clear existing rows

        String itemId = txtMID.getText().trim();
        String serNo = txtItemID.getText().trim();
        String itemType = cboxMType.getSelectedItem() != null ? cboxMType.getSelectedItem().toString().trim() : "";
        String mainDescription = txtReason.getText().trim();
        String sourceName = cboxCompany.getSelectedItem() != null ? cboxCompany.getSelectedItem().toString().trim() : "";

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();

            // Construct query with dynamic filtering for non-date fields
            StringBuilder query = new StringBuilder();
            query.append("SELECT mi.maintenance_id, mi.item_id, mi.type, mi.reason, mi.company_id AS company_id, ")
                    .append("mi.sentForRepairDate, mi.repairCompleteDate AS returnDate, mi.price, mi.remarks, mi.repair_warranty AS warrantyEnd ")
                    .append("FROM maintenance_in mi WHERE 1=1 ");

            // Apply filters if the fields are not empty
            if (!itemId.isEmpty()) {
                query.append("AND mi.item_id LIKE '%").append(itemId).append("%' ");
            }
            if (!serNo.isEmpty()) {
                query.append("AND mi.maintenance_id LIKE '%").append(serNo).append("%' ");
            }
            if (!itemType.equals("All") && !itemType.isEmpty()) {
                query.append("AND mi.type LIKE '%").append(itemType).append("%' ");
            }
            if (!mainDescription.isEmpty()) {
                query.append("AND mi.reason LIKE '%").append(mainDescription).append("%' ");
            }
            if (!sourceName.equals("All") && !sourceName.isEmpty()) {
                query.append("AND mi.company_id LIKE '%").append(sourceName).append("%' ");
            }

            // Execute query
            ResultSet rs = st.executeQuery(query.toString());

            // Populate table with filtered results
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("maintenance_id"),
                    rs.getInt("item_id"),
                    rs.getString("type"),
                    rs.getString("reason"),
                    rs.getString("company_id"),
                    rs.getDate("sentForRepairDate"),
                    rs.getDate("returnDate"),
                    rs.getDate("warrantyEnd"),
                    rs.getDouble("price"),
                    rs.getString("remarks")
                });
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error applying filters: " + e.getMessage());
        }
    }

    private void filterByDate() {
        DefaultTableModel model = (DefaultTableModel) AcquisitionTable.getModel();
        model.setRowCount(0); // Clear existing rows

        String sentDateFrom = txtSentDateFrom.getText().trim();
        String sentDateTo = txtSentDateTo.getText().trim();
        String returnDateFrom = txtReturnDateFrom.getText().trim();
        String returnDateTo = txtReturnDateTo.getText().trim();

        // Check if at least one date is valid for filtering
        boolean hasValidDate = false;
        if (isValidDate(sentDateFrom) && !sentDateFrom.isEmpty()) {
            hasValidDate = true;
        }
        if (isValidDate(sentDateTo) && !sentDateTo.isEmpty()) {
            hasValidDate = true;
        }
        if (isValidDate(returnDateFrom) && !returnDateFrom.isEmpty()) {
            hasValidDate = true;
        }
        if (isValidDate(returnDateTo) && !returnDateTo.isEmpty()) {
            hasValidDate = true;
        }

        if (!hasValidDate) {
            // Skip filtering if no valid dates are entered
            return;
        }

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();

            // Construct query with date filtering
            StringBuilder query = new StringBuilder();
            query.append("SELECT mi.maintenance_id, mi.item_id, mi.type, mi.reason, mi.company_id AS company_id, ")
                    .append("mi.sentForRepairDate, mi.repairCompleteDate AS returnDate, mi.price, mi.remarks, mi.repair_warranty AS warrantyEnd ")
                    .append("FROM maintenance_in mi WHERE 1=1 ");

            // Apply date filters if fields are filled and valid
            if (isValidDate(sentDateFrom) && !sentDateFrom.isEmpty()) {
                query.append("AND mi.sentForRepairDate >= '").append(sentDateFrom).append("' ");
            }
            if (isValidDate(sentDateTo) && !sentDateTo.isEmpty()) {
                query.append("AND mi.sentForRepairDate <= '").append(sentDateTo).append("' ");
            }
            if (isValidDate(returnDateFrom) && !returnDateFrom.isEmpty()) {
                query.append("AND mi.repairCompleteDate >= '").append(returnDateFrom).append("' ");
            }
            if (isValidDate(returnDateTo) && !returnDateTo.isEmpty()) {
                query.append("AND mi.repairCompleteDate <= '").append(returnDateTo).append("' ");
            }

            // Execute query
            ResultSet rs = st.executeQuery(query.toString());

            // Populate table with filtered results
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("maintenance_id"),
                    rs.getInt("item_id"),
                    rs.getString("type"),
                    rs.getString("reason"),
                    rs.getString("company_id"),
                    rs.getDate("sentForRepairDate"),
                    rs.getDate("returnDate"),
                    rs.getDate("warrantyEnd"),
                    rs.getDouble("price"),
                    rs.getString("remarks")
                });
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error applying date filters: " + e.getMessage());
        }
    }

// Helper function to validate date format (YYYY-MM-DD)
    private boolean isValidDate(String date) {
        if (date.isEmpty()) {
            return false; // If date is empty, it’s not valid for filtering
        }
        try {
            // Define expected date format
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, dateFormat); // Parse to check format
            return true;
        } catch (DateTimeParseException e) {
            return false;
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
        txtReason = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cboxMType = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtItemID = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtSentDateTo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtMID = new javax.swing.JTextField();
        cboxCompany = new javax.swing.JComboBox<>();
        txtReturnDateFrom = new javax.swing.JTextField();
        txtReturnDateTo = new javax.swing.JTextField();
        txtSentDateFrom = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
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
        jLabel1.setText("Maintenance Records");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

        AcquisitionTable.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        AcquisitionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Maintenance ID", "Item ID", "Maintenance Type", "Reason", "Company", "Sent Date", "Return Date", "Warranty End", "Price", "Remarks"
            }
        ));
        AcquisitionTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        AcquisitionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AcquisitionTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(AcquisitionTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 1050, 420));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel2.setText("Reason");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 520, -1, -1));

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
        getContentPane().add(txtReason, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 540, 260, -1));

        btnReset.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 670, 80, -1));

        btnClose.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 670, 80, -1));

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel5.setText("Company");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 580, -1, -1));

        cboxMType.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxMType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Repair", "Upgrade", "Service" }));
        cboxMType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxMTypeItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxMType, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 260, -1));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel9.setText("Item ID");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 580, -1, -1));

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
        getContentPane().add(txtItemID, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 600, 260, -1));

        jLabel10.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel10.setText("To");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 540, -1, -1));

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel11.setText("Maintenance Type");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 640, -1, -1));

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel4.setText("Return Date ");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 600, -1, -1));

        txtSentDateTo.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtSentDateTo.setText("[YYYY-MM-DD]");
        txtSentDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSentDateToActionPerformed(evt);
            }
        });
        txtSentDateTo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSentDateToKeyReleased(evt);
            }
        });
        getContentPane().add(txtSentDateTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 560, 100, -1));

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel12.setText("Maintenance ID");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, -1, -1));

        txtMID.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtMID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMIDActionPerformed(evt);
            }
        });
        txtMID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMIDKeyReleased(evt);
            }
        });
        getContentPane().add(txtMID, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 260, -1));

        cboxCompany.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxCompany.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxCompanyItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxCompany, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 600, 260, -1));

        txtReturnDateFrom.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtReturnDateFrom.setText("[YYYY-MM-DD]");
        txtReturnDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReturnDateFromActionPerformed(evt);
            }
        });
        txtReturnDateFrom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtReturnDateFromKeyReleased(evt);
            }
        });
        getContentPane().add(txtReturnDateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 650, 100, -1));

        txtReturnDateTo.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtReturnDateTo.setText("[YYYY-MM-DD]");
        txtReturnDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReturnDateToActionPerformed(evt);
            }
        });
        txtReturnDateTo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtReturnDateToKeyReleased(evt);
            }
        });
        getContentPane().add(txtReturnDateTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 650, 100, -1));

        txtSentDateFrom.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtSentDateFrom.setText("[YYYY-MM-DD]");
        txtSentDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSentDateFromActionPerformed(evt);
            }
        });
        txtSentDateFrom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSentDateFromKeyReleased(evt);
            }
        });
        getContentPane().add(txtSentDateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 560, 100, -1));

        jLabel13.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel13.setText("Sent Date ");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 520, -1, -1));

        jLabel14.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel14.setText("From");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 540, -1, -1));

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel15.setText("From");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 630, -1, -1));

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel16.setText("To");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 630, -1, -1));

        btnExcelExport.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnExcelExport.setText("Export CSV");
        btnExcelExport.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExcelExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelExportActionPerformed(evt);
            }
        });
        getContentPane().add(btnExcelExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 630, 80, 20));

        btnPDF.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnPDF.setText("Export PDF");
        btnPDF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 630, 80, 20));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/1.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReasonActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        DefaultTableModel model = (DefaultTableModel) AcquisitionTable.getModel();
        model.setRowCount(0); // Clear existing rows

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();

            // SQL query to retrieve data only from maintenance_in and related tables
            String query = "SELECT mi.maintenance_id, mi.item_id, mi.type, mi.reason, "
                    + "mi.company_id AS company_id, mi.sentForRepairDate, mi.repairCompleteDate AS returnDate, "
                    + "mi.repair_warranty AS warrantyEnd, mi.price, mi.remarks "
                    + "FROM maintenance_in mi";

            ResultSet rs = st.executeQuery(query);

            // Populate the table with the result set
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("maintenance_id"),
                    rs.getInt("item_id"),
                    rs.getString("type"),
                    rs.getString("reason"),
                    rs.getString("company_id"), // Use company_id directly
                    rs.getDate("sentForRepairDate"),
                    rs.getDate("returnDate"),
                    rs.getDate("warrantyEnd"),
                    rs.getDouble("price"),
                    rs.getString("remarks")
                });

            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading maintenance records: " + e.getMessage());
        }


    }//GEN-LAST:event_formComponentShown

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        new Maintenance_Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Maintenance_Records().setVisible(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void AcquisitionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AcquisitionTableMouseClicked

    }//GEN-LAST:event_AcquisitionTableMouseClicked

    private void txtItemIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemIDActionPerformed

    private void txtItemIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemIDKeyReleased
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_txtItemIDKeyReleased

    private void txtReasonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReasonKeyReleased
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_txtReasonKeyReleased

    private void txtSentDateToKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSentDateToKeyReleased
        // TODO add your handling code here:
        filterByDate();
    }//GEN-LAST:event_txtSentDateToKeyReleased

    private void cboxMTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxMTypeItemStateChanged
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_cboxMTypeItemStateChanged

    private void txtMIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMIDActionPerformed

    private void txtMIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMIDKeyReleased
        // TODO add your handling code here:
        filterAcquisitions();
    }//GEN-LAST:event_txtMIDKeyReleased

    private void cboxCompanyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxCompanyItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            filterAcquisitions();
        }
    }//GEN-LAST:event_cboxCompanyItemStateChanged

    private void txtSentDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSentDateToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSentDateToActionPerformed

    private void txtReturnDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReturnDateFromActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReturnDateFromActionPerformed

    private void txtReturnDateFromKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReturnDateFromKeyReleased
        // TODO add your handling code here:
        filterByDate();
    }//GEN-LAST:event_txtReturnDateFromKeyReleased

    private void txtReturnDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReturnDateToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReturnDateToActionPerformed

    private void txtReturnDateToKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReturnDateToKeyReleased
        // TODO add your handling code here:
        filterByDate();
    }//GEN-LAST:event_txtReturnDateToKeyReleased

    private void txtSentDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSentDateFromActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSentDateFromActionPerformed

    private void txtSentDateFromKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSentDateFromKeyReleased
        // TODO add your handling code here:
        filterByDate();
    }//GEN-LAST:event_txtSentDateFromKeyReleased

    private void btnExcelExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelExportActionPerformed
        // Open file chooser for saving
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setSelectedFile(new File("MaintenanceRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".csv"));

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
                DefaultTableModel model = (DefaultTableModel) AcquisitionTable.getModel(); // Assuming the table is named AcquisitionTable for maintenance records

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
        fileChooser.setSelectedFile(new File("MaintenanceRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".pdf"));

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

            PDFExporter pdfExporter = new PDFExporter("Maintenance Records", "Human Rights Commission Sri Lanka", footerText, filePath);

            try {
                // Export the PDF
                pdfExporter.exportPDF(AcquisitionTable.getModel()); // Assuming the table is named AcquisitionTable for maintenance records

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
            java.util.logging.Logger.getLogger(Maintenance_Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Maintenance_Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Maintenance_Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Maintenance_Records.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Maintenance_Records().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AcquisitionTable;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExcelExport;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cboxCompany;
    private javax.swing.JComboBox<String> cboxMType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtMID;
    private javax.swing.JTextField txtReason;
    private javax.swing.JTextField txtReturnDateFrom;
    private javax.swing.JTextField txtReturnDateTo;
    private javax.swing.JTextField txtSentDateFrom;
    private javax.swing.JTextField txtSentDateTo;
    // End of variables declaration//GEN-END:variables
}
