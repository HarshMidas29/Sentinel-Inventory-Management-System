package SIMS.Disposals;

import SIMS.Menus.Disposal_Menu;
import dao.ConnectionProvider;
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
public class Disposal_History extends javax.swing.JFrame {

    /**
     * Creates new form ManageCustomer
     */
    public Disposal_History() {
        initComponents();
        setLocationRelativeTo(null);
        populateOfficerTakingOverComboBox();
        populateLastUserComboBox();
        populateLastDivComboBox();
        populateLastOfficeComboBox();
    }

    private void populateItemTypeComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT item_type FROM disposal");
            cboxItemType1.removeAllItems(); // Clear existing items
            cboxItemType1.addItem("All");
            while (rs.next()) {
                cboxItemType1.addItem(rs.getString("item_type"));
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void populateOfficerTakingOverComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT e.id, e.name "
                    + "FROM disposal d "
                    + "JOIN employee e ON d.officerTakingOver = e.id");
            cboxOfficerTakingOver.removeAllItems();
            cboxOfficerTakingOver.addItem("All");
            while (rs.next()) {
                cboxOfficerTakingOver.addItem(rs.getInt("id") + " - " + rs.getString("name"));
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
            ResultSet rs = st.executeQuery("SELECT DISTINCT e.id, e.name "
                    + "FROM disposal d "
                    + "JOIN employee e ON d.last_user_id = e.id");
            cboxItemType2.removeAllItems(); // Assuming cboxItemType2 is for Last User
            cboxItemType2.addItem("All");
            while (rs.next()) {
                cboxItemType2.addItem(rs.getInt("id") + " - " + rs.getString("name"));
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
            ResultSet rs = st.executeQuery("SELECT DISTINCT d.id, d.name "
                    + "FROM disposal dp "
                    + "JOIN division_section_project d ON dp.last_division_id = d.id");
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
            ResultSet rs = st.executeQuery("SELECT DISTINCT o.id, o.name "
                    + "FROM disposal d "
                    + "JOIN office o ON d.last_office_id = o.id");
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
        DefaultTableModel model = (DefaultTableModel) DisposalsTable.getModel();
        model.setRowCount(0); // Clear existing rows

        String itemId = txtItemID.getText().trim();
        String serNo = txtSerNo.getText().trim();
        String itemType = cboxItemType1.getSelectedItem() != null ? cboxItemType1.getSelectedItem().toString() : "";
        String reason = txtReason.getText().trim();
        String disposalDateFrom = txtDisposalDateFrom.getText().trim();
        String disposalDateTo = txtDisposalDateTo.getText().trim();
        String officerTakingOver = cboxOfficerTakingOver.getSelectedItem() != null ? cboxOfficerTakingOver.getSelectedItem().toString() : "";
        String lastUser = cboxItemType2.getSelectedItem() != null ? cboxItemType2.getSelectedItem().toString() : "";
        String lastDiv = cboxDiv.getSelectedItem() != null ? cboxDiv.getSelectedItem().toString() : "";
        String lastOffice = cboxOffice.getSelectedItem() != null ? cboxOffice.getSelectedItem().toString() : "";

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();

            StringBuilder query = new StringBuilder();
            query.append("SELECT d.item_id, d.ser_no, d.item_type, d.reason, d.disposalDate, d.officerTakingOver, e.name AS officer_name, ")
                    .append("lu.name AS last_user_name, ld.name AS last_div_name, lo.name AS last_office_name ")
                    .append("FROM disposal d ")
                    .append("LEFT JOIN employee e ON d.officerTakingOver = e.id ")
                    .append("LEFT JOIN employee lu ON d.last_user_id = lu.id ")
                    .append("LEFT JOIN division_section_project ld ON d.last_division_id = ld.id ")
                    .append("LEFT JOIN office lo ON d.last_office_id = lo.id ")
                    .append("WHERE 1=1 ");

            if (!itemId.isEmpty()) {
                query.append("AND d.item_id = ").append(itemId).append(" ");
            }
            if (!serNo.isEmpty()) {
                query.append("AND d.ser_no LIKE '%").append(serNo).append("%' ");
            }
            if (!itemType.equals("All") && !itemType.isEmpty()) {
                query.append("AND d.item_type = '").append(itemType).append("' ");
            }
            if (!reason.isEmpty()) {
                query.append("AND d.reason LIKE '%").append(reason).append("%' ");
            }
            if (!disposalDateFrom.isEmpty() && !disposalDateTo.isEmpty()) {
                query.append("AND d.disposalDate BETWEEN '").append(disposalDateFrom).append("' AND '").append(disposalDateTo).append("' ");
            } else if (!disposalDateFrom.isEmpty()) {
                query.append("AND d.disposalDate >= '").append(disposalDateFrom).append("' ");
            } else if (!disposalDateTo.isEmpty()) {
                query.append("AND d.disposalDate <= '").append(disposalDateTo).append("' ");
            }
            if (!officerTakingOver.equals("All") && !officerTakingOver.isEmpty()) {
                query.append("AND d.officerTakingOver = ").append(officerTakingOver.split(" - ")[0]).append(" ");
            }
            if (!lastUser.equals("All") && !lastUser.isEmpty()) {
                query.append("AND d.last_user_id = ").append(lastUser.split(" - ")[0]).append(" ");
            }
            if (!lastDiv.equals("All") && !lastDiv.isEmpty()) {
                query.append("AND d.last_division_id = ").append(lastDiv.split(" - ")[0]).append(" ");
            }
            if (!lastOffice.equals("All") && !lastOffice.isEmpty()) {
                query.append("AND d.last_office_id = ").append(lastOffice.split(" - ")[0]).append(" ");
            }

            ResultSet rs = st.executeQuery(query.toString());

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("item_id"),
                    rs.getString("ser_no"),
                    rs.getString("item_type"),
                    rs.getString("reason"),
                    rs.getDate("disposalDate"),
                    rs.getString("officer_name"),
                    rs.getString("last_user_name"),
                    rs.getString("last_div_name"),
                    rs.getString("last_office_name")
                });
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
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
        DisposalsTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtItemID = new javax.swing.JTextField();
        txtDisposalDateFrom = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cboxOfficerTakingOver = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtReason = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtDisposalDateTo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtSerNo = new javax.swing.JTextField();
        cboxItemType1 = new javax.swing.JComboBox<>();
        cboxItemType2 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cboxDiv = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        cboxOffice = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
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
        jLabel1.setText("Disposal History");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, -1));

        DisposalsTable.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        DisposalsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Serial / Product No", "Item Type", "Reason", "Date", "Officer Taking Over", "Last User", "Last Div / Sec / Proj", "Last Office"
            }
        ));
        DisposalsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        DisposalsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DisposalsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(DisposalsTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 1060, 440));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        jLabel2.setText("Filter By");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 510, -1, -1));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel3.setText("Disposal Date From [YYYY-MM-DD]");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 590, -1, -1));

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
        getContentPane().add(txtItemID, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, 220, -1));

        txtDisposalDateFrom.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtDisposalDateFrom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDisposalDateFromKeyReleased(evt);
            }
        });
        getContentPane().add(txtDisposalDateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 610, 220, -1));

        btnReset.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 680, 80, -1));

        btnClose.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 680, 80, -1));

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel5.setText("Item Type");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 640, -1, -1));

        cboxOfficerTakingOver.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxOfficerTakingOver.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxOfficerTakingOverItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxOfficerTakingOver, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 560, 220, -1));

        jLabel10.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel10.setText("Reason");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 540, -1, -1));

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
        getContentPane().add(txtReason, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 560, 220, -1));

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel7.setText("Officer Taking Over");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 540, -1, -1));

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel11.setText("Disposal Date To [YYYY-MM-DD]");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 640, -1, -1));

        txtDisposalDateTo.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtDisposalDateTo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDisposalDateToKeyReleased(evt);
            }
        });
        getContentPane().add(txtDisposalDateTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 660, 220, -1));

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel8.setText("Serial / Product No");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, -1, -1));

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
        getContentPane().add(txtSerNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, 220, -1));

        cboxItemType1.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxItemType1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxItemType1ItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxItemType1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, 220, -1));

        cboxItemType2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxItemType2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxItemType2ItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxItemType2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 560, 220, -1));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel9.setText("Last User");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 540, -1, -1));

        cboxDiv.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxDiv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxDivItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxDiv, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 610, 220, -1));

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel12.setText("Last Division / Section / Project");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 590, -1, -1));

        cboxOffice.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxOffice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxOfficeItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 660, 220, -1));

        jLabel13.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel13.setText("Last Office");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 640, -1, -1));

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel4.setText("Item ID");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, -1, -1));

        btnExcelExport.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnExcelExport.setText("Export CSV");
        btnExcelExport.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExcelExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelExportActionPerformed(evt);
            }
        });
        getContentPane().add(btnExcelExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 650, 70, 20));

        btnPDF.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnPDF.setText("Export PDF");
        btnPDF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 650, 70, 20));

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
        populateOfficerTakingOverComboBox();
        populateLastUserComboBox();
        populateLastDivComboBox();
        populateLastOfficeComboBox();
        DefaultTableModel model = (DefaultTableModel) DisposalsTable.getModel();
        model.setRowCount(0); // Clear existing rows

        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT d.item_id, d.ser_no, d.item_type, d.reason, d.disposalDate, d.officerTakingOver, e.name AS officer_name, "
                    + "lu.name AS last_user_name, ld.name AS last_div_name, lo.name AS last_office_name "
                    + "FROM disposal d "
                    + "LEFT JOIN employee e ON d.officerTakingOver = e.id "
                    + "LEFT JOIN employee lu ON d.last_user_id = lu.id "
                    + "LEFT JOIN division_section_project ld ON d.last_division_id = ld.id "
                    + "LEFT JOIN office lo ON d.last_office_id = lo.id"
            );
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("item_id"),
                    rs.getString("ser_no"),
                    rs.getString("item_type"),
                    rs.getString("reason"),
                    rs.getDate("disposalDate"),
                    rs.getString("officer_name"),
                    rs.getString("last_user_name"),
                    rs.getString("last_div_name"),
                    rs.getString("last_office_name")
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
        new Disposal_Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Disposal_History().setVisible(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void DisposalsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DisposalsTableMouseClicked
        // TODO add your handling code here:                                         

    }//GEN-LAST:event_DisposalsTableMouseClicked

    private void txtReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReasonActionPerformed

    private void cboxOfficerTakingOverItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxOfficerTakingOverItemStateChanged
        // TODO add your handling code here:                       
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            filterRecords();
        }
    }//GEN-LAST:event_cboxOfficerTakingOverItemStateChanged

    private void txtItemIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemIDKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtItemIDKeyReleased

    private void txtReasonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReasonKeyReleased
        // TODO add your handling code here:
        filterRecords();
    }//GEN-LAST:event_txtReasonKeyReleased

    private void txtDisposalDateFromKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDisposalDateFromKeyReleased
        // TODO add your handling code here:
        if (isValidDate(txtDisposalDateFrom.getText().trim())) {
            filterRecords();
        }
    }//GEN-LAST:event_txtDisposalDateFromKeyReleased

    private void txtDisposalDateToKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDisposalDateToKeyReleased
        // TODO add your handling code here:
        if (isValidDate(txtDisposalDateTo.getText().trim())) {
            filterRecords();
        }
    }//GEN-LAST:event_txtDisposalDateToKeyReleased

    private void txtSerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerNoActionPerformed

    private void txtSerNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerNoKeyReleased
        // TODO add your handling code here:
        filterRecords();

    }//GEN-LAST:event_txtSerNoKeyReleased

    private void cboxItemType1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxItemType1ItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            filterRecords();
        }
    }//GEN-LAST:event_cboxItemType1ItemStateChanged

    private void cboxItemType2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxItemType2ItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            filterRecords();
        }
    }//GEN-LAST:event_cboxItemType2ItemStateChanged

    private void cboxDivItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxDivItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            filterRecords();
        }
    }//GEN-LAST:event_cboxDivItemStateChanged

    private void cboxOfficeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxOfficeItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            filterRecords();
        }
    }//GEN-LAST:event_cboxOfficeItemStateChanged

    private void btnExcelExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelExportActionPerformed
        // Open file chooser for saving
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setSelectedFile(new File("DisposalRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".csv"));

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
                DefaultTableModel model = (DefaultTableModel) DisposalsTable.getModel();

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
        fileChooser.setSelectedFile(new File("DisposalRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".pdf"));

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

            PDFExporter pdfExporter = new PDFExporter("Disposal Records", "Human Rights Commission Sri Lanka", footerText, filePath);

            try {
                // Export the PDF
                pdfExporter.exportPDF(DisposalsTable.getModel());

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
            java.util.logging.Logger.getLogger(Disposal_History.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Disposal_History.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Disposal_History.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Disposal_History.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Disposal_History().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DisposalsTable;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExcelExport;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cboxDiv;
    private javax.swing.JComboBox<String> cboxItemType1;
    private javax.swing.JComboBox<String> cboxItemType2;
    private javax.swing.JComboBox<String> cboxOffice;
    private javax.swing.JComboBox<String> cboxOfficerTakingOver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtDisposalDateFrom;
    private javax.swing.JTextField txtDisposalDateTo;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtReason;
    private javax.swing.JTextField txtSerNo;
    // End of variables declaration//GEN-END:variables
}
