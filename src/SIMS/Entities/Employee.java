package SIMS.Entities;

import SIMS.Menus.Menu;
import dao.ConnectionProvider;
import dao.PDFExporter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Daham Yakandawala
 */
public class Employee extends javax.swing.JFrame {

    private int employeePk = 0;

    /**
     * Creates new form ManageCustomer
     */
    public Employee() {
        initComponents();
        setLocationRelativeTo(null);
        populateComboBoxes();
    }

    private void populateComboBoxes() {
        populateOfficeComboBox();
        populateDivisionComboBox();
    }

    private void populateOfficeComboBox() {
        try (Connection con = ConnectionProvider.getCon(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT name FROM office")) {
            cboxOffice.removeAllItems();
            cboxOffice.addItem("All");
            while (rs.next()) {
                cboxOffice.addItem(rs.getString("name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void populateDivisionComboBox() {
        try (Connection con = ConnectionProvider.getCon(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT name FROM division_section_project")) {
            cboxDiv.removeAllItems();
            cboxDiv.addItem("All");
            while (rs.next()) {
                cboxDiv.addItem(rs.getString("name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private boolean validateFields(String formType) {
        boolean fieldsFilled = !txtName.getText().isEmpty()
                && !txtMobileNumber.getText().isEmpty()
                && !txtAppointment.getText().isEmpty();
        if (formType.equals("edit")) {
            return employeePk != 0 && fieldsFilled;
        } else if (formType.equals("new")) {
            return fieldsFilled;
        }
        return false;
    }

    private void populateEmployeeTable() {
        DefaultTableModel model = (DefaultTableModel) EmployeeTable.getModel();
        model.setRowCount(0);

        try (Connection con = ConnectionProvider.getCon(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM employee")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("nic"),
                    rs.getString("designation"),
                    rs.getString("division"),
                    rs.getString("office"),
                    rs.getString("contact"),
                    rs.getString("remarks")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void filterEmployees() {
        DefaultTableModel model = (DefaultTableModel) EmployeeTable.getModel();
        model.setRowCount(0);

        String name = txtName.getText().trim();
        String mobileNumber = txtMobileNumber.getText().trim();
        String appointment = txtAppointment.getText().trim();
        String nic = txtNic.getText().trim();
        String division = cboxDiv.getSelectedItem().toString();
        String office = cboxOffice.getSelectedItem().toString();
        String remarks = txtRemarks.getText().trim();

        try (Connection con = ConnectionProvider.getCon()) {
            StringBuilder query = new StringBuilder("SELECT * FROM employee WHERE 1=1");
            if (!name.isEmpty()) {
                query.append(" AND name LIKE ?");
            }
            if (!mobileNumber.isEmpty()) {
                query.append(" AND contact LIKE ?");
            }
            if (!appointment.isEmpty()) {
                query.append(" AND designation LIKE ?");
            }
            if (!nic.isEmpty()) {
                query.append(" AND nic LIKE ?");
            }
            if (!division.equals("All")) {
                query.append(" AND division = ?");
            }
            if (!office.equals("All")) {
                query.append(" AND office = ?");
            }
            if (!remarks.isEmpty()) {
                query.append(" AND remarks LIKE ?");
            }

            try (PreparedStatement ps = con.prepareStatement(query.toString())) {
                int paramIndex = 1;
                if (!name.isEmpty()) {
                    ps.setString(paramIndex++, "%" + name + "%");
                }
                if (!mobileNumber.isEmpty()) {
                    ps.setString(paramIndex++, "%" + mobileNumber + "%");
                }
                if (!appointment.isEmpty()) {
                    ps.setString(paramIndex++, "%" + appointment + "%");
                }
                if (!nic.isEmpty()) {
                    ps.setString(paramIndex++, "%" + nic + "%");
                }
                if (!division.equals("All")) {
                    ps.setString(paramIndex++, division);
                }
                if (!office.equals("All")) {
                    ps.setString(paramIndex++, office);
                }
                if (!remarks.isEmpty()) {
                    ps.setString(paramIndex++, "%" + remarks + "%");
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("nic"),
                            rs.getString("designation"),
                            rs.getString("division"),
                            rs.getString("office"),
                            rs.getString("contact"),
                            rs.getString("remarks")
                        });
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void sortEmployees(String sortBy) {
        DefaultTableModel model = (DefaultTableModel) EmployeeTable.getModel();
        model.setRowCount(0);

        String sortOrder = switch (sortBy) {
            case "Name" ->
                "name";
            case "Mobile Number" ->
                "contact";
            case "Appointment" ->
                "designation";
            default ->
                "id";
        };

        try (Connection con = ConnectionProvider.getCon(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM employee ORDER BY " + sortOrder)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("nic"),
                    rs.getString("designation"),
                    rs.getString("division"),
                    rs.getString("office"),
                    rs.getString("contact"),
                    rs.getString("remarks")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        EmployeeTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtMobileNumber = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        txtAppointment = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cboxOffice = new javax.swing.JComboBox<>();
        txtNic = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cboxSortBy = new javax.swing.JComboBox<>();
        cboxDiv = new javax.swing.JComboBox<>();
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
        jLabel1.setText("Manage Employee Records");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, -1, -1));

        EmployeeTable.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        EmployeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "NIC Number", "Designation", "Div / Sec / Proj", "Office", "Mobile Number", "Remarks"
            }
        ));
        EmployeeTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        EmployeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmployeeTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(EmployeeTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 990, 450));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel2.setText("Sort By");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 650, -1, -1));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel3.setText("Mobile Number");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 630, -1, -1));

        txtName.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        getContentPane().add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 330, -1));

        txtMobileNumber.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        getContentPane().add(txtMobileNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 650, 330, -1));

        btnSave.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnSave.setText("Save");
        btnSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        getContentPane().add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 590, 80, -1));

        btnUpdate.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        getContentPane().add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 590, 80, -1));

        btnClose.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 690, 70, -1));

        txtAppointment.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        getContentPane().add(txtAppointment, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 650, 330, -1));

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel7.setText("Designation");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 630, -1, -1));

        btnReset.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 590, 80, -1));

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel4.setText("Name");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, -1, -1));

        cboxOffice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        cboxOffice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxOfficeItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 590, 330, -1));

        txtNic.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtNic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNicActionPerformed(evt);
            }
        });
        getContentPane().add(txtNic, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 330, -1));

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel5.setText("NIC No");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, -1, -1));

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel8.setText("Division / Section / Project");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 520, -1, -1));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel9.setText("Office");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 570, -1, -1));

        txtRemarks.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        getContentPane().add(txtRemarks, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 540, 330, -1));

        jLabel10.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel10.setText("Remarks");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 520, -1, -1));

        cboxSortBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Name", "NIC Number", "Designation", "Div / Sec / Proj", "Office" }));
        cboxSortBy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxSortByItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxSortBy, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 640, 260, -1));

        cboxDiv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        cboxDiv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxDivItemStateChanged(evt);
            }
        });
        cboxDiv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboxDivActionPerformed(evt);
            }
        });
        getContentPane().add(cboxDiv, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 330, -1));

        btnExcelExport.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnExcelExport.setText("Export CSV");
        btnExcelExport.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExcelExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelExportActionPerformed(evt);
            }
        });
        getContentPane().add(btnExcelExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 690, 70, 20));

        btnPDF.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnPDF.setText("Export PDF");
        btnPDF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 690, 70, 20));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/1.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:

        populateEmployeeTable();
        btnUpdate.setEnabled(false);


    }//GEN-LAST:event_formComponentShown

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        new Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        String name = txtName.getText();
        String contact = txtMobileNumber.getText();
        String appointment = txtAppointment.getText();
        String nic = txtNic.getText();
        String division = cboxDiv.getSelectedItem().toString();
        String office = cboxOffice.getSelectedItem().toString();
        String remarks = txtRemarks.getText();

        if (validateFields("new")) {
            JOptionPane.showMessageDialog(null, "Please enter all fields");
        } else {
            try {
                Connection con = ConnectionProvider.getCon();
                PreparedStatement ps = con.prepareStatement("INSERT INTO employee (name, contact, designation, nic, division, office, remarks) VALUES (?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, appointment);
                ps.setString(4, nic);
                ps.setString(5, division);
                ps.setString(6, office);
                ps.setString(7, remarks);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Employee Added Successfully");
                setVisible(false);
                new Employee().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }


    }//GEN-LAST:event_btnSaveActionPerformed

    private void EmployeeTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmployeeTableMouseClicked
        // TODO add your handling code here:                                        
        int index = EmployeeTable.getSelectedRow();
        TableModel model = EmployeeTable.getModel();

        employeePk = Integer.parseInt(model.getValueAt(index, 0).toString());
        txtName.setText(model.getValueAt(index, 1).toString());
        txtNic.setText(model.getValueAt(index, 2).toString());
        txtAppointment.setText(model.getValueAt(index, 3).toString());
        cboxDiv.setSelectedItem(model.getValueAt(index, 4).toString());
        cboxOffice.setSelectedItem(model.getValueAt(index, 5).toString());
        txtMobileNumber.setText(model.getValueAt(index, 6).toString());
        txtRemarks.setText(model.getValueAt(index, 7).toString());

        btnSave.setEnabled(false);
        btnUpdate.setEnabled(true);
    }//GEN-LAST:event_EmployeeTableMouseClicked

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:                                      
        String name = txtName.getText();
        String contact = txtMobileNumber.getText();
        String appointment = txtAppointment.getText();
        String nic = txtNic.getText();
        String division = cboxDiv.getSelectedItem().toString();
        String office = cboxOffice.getSelectedItem().toString();
        String remarks = txtRemarks.getText();

        if (validateFields("edit")) {
            JOptionPane.showMessageDialog(null, "Please enter all fields");
        } else {
            try {
                Connection con = ConnectionProvider.getCon();
                PreparedStatement ps = con.prepareStatement("UPDATE employee SET name=?, contact=?, designation=?, nic=?, division=?, office=?, remarks=? WHERE id=?");
                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, appointment);
                ps.setString(4, nic);
                ps.setString(5, division);
                ps.setString(6, office);
                ps.setString(7, remarks);
                ps.setInt(8, employeePk);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Employee Updated Successfully");
                setVisible(false);
                new Employee().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Employee().setVisible(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void cboxOfficeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxOfficeItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            //filterEmployees();
        }
    }//GEN-LAST:event_cboxOfficeItemStateChanged

    private void txtNicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNicActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNicActionPerformed

    private void cboxSortByItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxSortByItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String sortBy = cboxSortBy.getSelectedItem().toString();
            sortEmployees(sortBy);
        }

    }//GEN-LAST:event_cboxSortByItemStateChanged

    private void cboxDivItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxDivItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            //filterEmployees();
        }
    }//GEN-LAST:event_cboxDivItemStateChanged

    private void cboxDivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxDivActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboxDivActionPerformed

    private void btnExcelExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelExportActionPerformed
        // Open file chooser for saving
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setSelectedFile(new File("EmployeeRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".csv"));

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
                DefaultTableModel model = (DefaultTableModel) EmployeeTable.getModel();

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
        fileChooser.setSelectedFile(new File("EmployeeRecords_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".pdf"));

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

            PDFExporter pdfExporter = new PDFExporter("Employee Records", "Human Rights Commission Sri Lanka", footerText, filePath);

            try {
                // Export the PDF
                pdfExporter.exportPDF(EmployeeTable.getModel());

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
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Employee().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable EmployeeTable;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExcelExport;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboxDiv;
    private javax.swing.JComboBox<String> cboxOffice;
    private javax.swing.JComboBox<String> cboxSortBy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtAppointment;
    private javax.swing.JTextField txtMobileNumber;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtNic;
    private javax.swing.JTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
}
