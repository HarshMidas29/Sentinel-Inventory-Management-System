package SIMS.Assignments;

import dao.ConnectionProvider;
import SIMS.Menus.Assignment_Menu;
import dao.PDFExporter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
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
public class Assign_Item extends javax.swing.JFrame {

    private int disposePk = 0;

    /**
     * Creates new form ManageCustomer
     */
    public Assign_Item() {
        initComponents();
        setLocationRelativeTo(null);
        populateCategoryComboBox();
        populateOfficerComboBox();
        populateLastDivComboBox();
        populateLastOfficeComboBox();
    }

    private void populateCurrentAssignmentTable() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            // Join the assignedTo table with the item table to get the item_type
            ResultSet rs = st.executeQuery(
                    "SELECT assignedTo.item_id, assignedTo.ser_no, item.category AS item_type, "
                    + "assignedTo.assigned_employee, assignedTo.assigned_division, "
                    + "assignedTo.assigned_office, assignedTo.purpose, assignedTo.assignedDate "
                    + "FROM assignedTo "
                    + "JOIN item ON assignedTo.item_id = item.id"
            );

            DefaultTableModel model = (DefaultTableModel) CurrentAssignmentTable.getModel();
            model.setRowCount(0);  // Clear existing rows

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String serNo = rs.getString("ser_no");
                String itemType = rs.getString("item_type");  // Get item_type from the item table
                String assignedEmployee = rs.getString("assigned_employee");
                String assignedDivision = rs.getString("assigned_division");
                String assignedOffice = rs.getString("assigned_office");
                String purpose = rs.getString("purpose");
                Date assignedDate = rs.getDate("assignedDate");

                model.addRow(new Object[]{itemId, serNo, itemType, assignedEmployee, assignedDivision, assignedOffice, purpose, assignedDate});
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void populateCategoryComboBox() {
        try {
            cboxCategory.removeAllItems(); // Clear existing items
            cboxCategory.addItem("Select"); // Add default "Select" option

            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT category FROM item");

            // Debugging step: Print categories to console/log
            boolean hasData = false; // Flag to check if data exists
            while (rs.next()) {
                String category = rs.getString("category");
                System.out.println("Fetched category: " + category); // Debugging
                cboxCategory.addItem(category);
                hasData = true;
            }

            if (!hasData) {
                System.out.println("No categories found in the database."); // Debugging
            }

            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching categories: " + e.getMessage());
            e.printStackTrace(); // Debugging step
        }
    }

    private void populateOfficerComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name FROM employee");
            while (rs.next()) {
                cboxEmployee.addItem(rs.getInt("id") + " - " + rs.getString("name"));
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

    private void populateSerNoComboBox(String category) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("SELECT id, ser_no FROM item WHERE category = ?");
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            cboxSerNo.removeAllItems();
            cboxItemID.removeAllItems();
            cboxSerNo.addItem("Select");
            cboxItemID.addItem("Select");
            while (rs.next()) {
                cboxSerNo.addItem(rs.getString("ser_no"));
                cboxItemID.addItem(rs.getString("id"));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void populateDivisionAndOfficeComboBoxes(String employeeId) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("SELECT division, office FROM employee WHERE id = ?");
            ps.setString(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String division = rs.getString("division");
                String office = rs.getString("office");

                if (division != null && !division.isEmpty()) {
                    // Select the relevant item in the division combo box
                    for (int i = 0; i < cboxDiv.getItemCount(); i++) {
                        if (cboxDiv.getItemAt(i).contains(division)) {
                            cboxDiv.setSelectedIndex(i);
                            break;
                        }
                    }
                } else {
                    cboxDiv.setSelectedItem("Select");
                }
                cboxDiv.repaint();

                if (office != null && !office.isEmpty()) {
                    // Select the relevant item in the office combo box
                    for (int i = 0; i < cboxOffice.getItemCount(); i++) {
                        if (cboxOffice.getItemAt(i).contains(office)) {
                            cboxOffice.setSelectedIndex(i);
                            break;
                        }
                    }
                } else {
                    cboxOffice.setSelectedItem("Select");
                }
                cboxOffice.repaint();
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void syncItemIDAndSerNo(String selectedItem, boolean isSerNo) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps;
            if (isSerNo) {
                ps = con.prepareStatement("SELECT id FROM item WHERE ser_no = ?");
            } else {
                ps = con.prepareStatement("SELECT ser_no FROM item WHERE id = ?");
            }
            ps.setString(1, selectedItem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (isSerNo) {
                    cboxItemID.setSelectedItem(rs.getString("id"));
                } else {
                    cboxSerNo.setSelectedItem(rs.getString("ser_no"));
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void checkIfAssigned(int itemId, String assignedDate) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM assignedTo WHERE item_id = ?");
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String assignedEmployee = rs.getString("assigned_employee");
                String assignedDivision = rs.getString("assigned_division");
                String assignedOffice = rs.getString("assigned_office");
                String purpose = rs.getString("purpose");

                String message = "Item " + itemId + " is already assigned to ";
                if (assignedEmployee != null) {
                    message += "employee " + assignedEmployee + ". Do you wish to change this?";
                } else if (assignedDivision != null) {
                    message += "division / section / project " + assignedDivision + ". Do you wish to change this?";
                } else if (assignedOffice != null) {
                    message += "office " + assignedOffice + ". Do you wish to change this?";
                }
                int response = JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    /*
                    saveToAssignmentHistory(itemId, assignedEmployee, assignedDivision, assignedOffice, rs.getString("assignedDate"), purpose);
                    deleteFromAssignedTo(itemId);
                     */

                    PreparedStatement ps1 = con.prepareStatement(
                            "INSERT INTO assignmentHistory (item_id, ser_no, assigned_employee, assigned_division, assigned_office, assignedDate, returnDate, purpose) "
                            + "SELECT item_id, ser_no, assigned_employee, assigned_division, assigned_office, assignedDate, ?, purpose "
                            + "FROM assignedTo WHERE item_id = ?");
                    ps1.setDate(1, Date.valueOf(assignedDate));
                    ps1.setInt(2, itemId);

                    ps1.executeUpdate();
                    ps1.close();

                    PreparedStatement ps2 = con.prepareStatement("DELETE FROM assignedTo WHERE item_id = ?");
                    ps2.setInt(1, itemId);
                    ps2.executeUpdate();
                    ps2.close();

                } else {
                    return;
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void saveToAssignmentHistory(int itemId, String employee, String division, String office, String assignedDate, String purpose) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO assignmentHistory (item_id, ser_no, assigned_employee, assigned_division, assigned_office, assignedDate, returnDate, purpose) "
                    + "SELECT item_id, ser_no, assigned_employee, assigned_division, assigned_office, assignedDate, CURRENT_DATE, purpose "
                    + "FROM assignedTo WHERE item_id = ?");
            ps.setInt(1, itemId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void deleteFromAssignedTo(int itemId) {
        try {
            Connection con = ConnectionProvider.getCon();
            PreparedStatement ps = con.prepareStatement("DELETE FROM assignedTo WHERE item_id = ?");
            ps.setInt(1, itemId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void assignItem(int itemId, String serialNumber, String assignmentDate, String employee, String division, String office, String purpose) {
        try {
            Connection con = ConnectionProvider.getCon();
            con.setAutoCommit(false);
            checkIfAssigned(itemId, assignmentDate);

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO assignedTo (item_id, ser_no, assigned_employee, assigned_division, assigned_office, assignedDate, purpose) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, itemId);
            ps.setString(2, serialNumber);
            ps.setString(3, employee);
            ps.setString(4, division);
            ps.setString(5, office);
            ps.setDate(6, Date.valueOf(assignmentDate));
            ps.setString(7, purpose);
            ps.executeUpdate();
            con.commit();
            ps.close();
            con.setAutoCommit(true);
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private boolean validateFields(String formType) {
        if (formType.equals("new") && !cboxItemID.getSelectedItem().toString().equals("Select")
                && !txtAssignmentDate.getText().equals("") && !txtPurpose.getText().equals("")) {
            if (cboxDiv.getSelectedItem().toString().equals("Select")
                    && cboxEmployee.getSelectedItem().toString().equals("Select")
                    && cboxOffice.getSelectedItem().toString().equals("Select")) {
                JOptionPane.showMessageDialog(null, "Please select at least one of the Assigned Division / Section / Project, Assigned Employee, or Assigned Office.");
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnAddItem = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        cboxOffice = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtAssignmentDate = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cboxItemID = new javax.swing.JComboBox<>();
        cboxCategory = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cboxEmployee = new javax.swing.JComboBox<>();
        cboxDiv = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cboxSerNo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        CurrentAssignmentTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtPurpose = new javax.swing.JTextField();
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
        jLabel1.setText("Current Assignments");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel2.setText("Category");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 130, -1, -1));

        btnAddItem.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnAddItem.setText("Assign Item");
        btnAddItem.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 610, 80, -1));

        btnReset.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 610, 80, -1));

        btnClose.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 690, 80, -1));

        cboxOffice.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxOffice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        cboxOffice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxOfficeItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 450, 240, -1));

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel8.setText("Item ID");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 190, -1, -1));

        txtAssignmentDate.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtAssignmentDate.setText("[YYYY-MM-DD]");
        getContentPane().add(txtAssignmentDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 570, 240, -1));

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel12.setText("Assignment Date");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 550, -1, -1));

        cboxItemID.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxItemID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Laptop", "Monitor", "Desktop", "Printer", "Scanner", "Photocopy" }));
        cboxItemID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxItemIDItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxItemID, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 210, 240, -1));

        cboxCategory.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Laptop", "Monitor", "Desktop", "Printer", "Scanner", "Photocopy" }));
        cboxCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxCategoryItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 150, 240, -1));

        jLabel13.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel13.setText("Assigned Employee");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 310, -1, -1));

        jLabel14.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel14.setText("Assigned Division / Section / Project");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 370, -1, -1));

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel15.setText("Assigned Office");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 430, -1, -1));

        cboxEmployee.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxEmployee.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        cboxEmployee.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxEmployeeItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxEmployee, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 330, 240, -1));

        cboxDiv.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxDiv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        cboxDiv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxDivItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxDiv, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 390, 240, -1));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel9.setText("Serial No");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 250, -1, -1));

        cboxSerNo.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxSerNo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Laptop", "Monitor", "Desktop", "Printer", "Scanner", "Photocopy" }));
        cboxSerNo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxSerNoItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxSerNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 270, 240, -1));

        CurrentAssignmentTable.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        CurrentAssignmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Serial / Product No", "Item Type", "Assigned User", "Assigned Div / Sec / Proj", "Assigned Office", "Purpose", "Assigned Date"
            }
        ));
        CurrentAssignmentTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        CurrentAssignmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CurrentAssignmentTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(CurrentAssignmentTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 780, 610));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 1, 36)); // NOI18N
        jLabel3.setText("Assign Item");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 70, -1, -1));

        txtPurpose.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtPurpose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPurposeActionPerformed(evt);
            }
        });
        getContentPane().add(txtPurpose, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 510, 240, -1));

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel16.setText("Purpose");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 490, -1, -1));

        btnExcelExport.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnExcelExport.setText("Export CSV");
        btnExcelExport.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExcelExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelExportActionPerformed(evt);
            }
        });
        getContentPane().add(btnExcelExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 640, 80, 20));

        btnPDF.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnPDF.setText("Export PDF");
        btnPDF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFActionPerformed(evt);
            }
        });
        getContentPane().add(btnPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 640, 80, 20));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/1.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        populateCurrentAssignmentTable();
    }//GEN-LAST:event_formComponentShown

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        new Assignment_Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Assign_Item().setVisible(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed

        // TODO add your handling code here:
        if (!validateFields("new")) {
            JOptionPane.showMessageDialog(null, "Please enter all fields");
        } else {
            int itemId = Integer.parseInt(cboxItemID.getSelectedItem().toString());

            try {
                Connection con = ConnectionProvider.getCon();
                String checkMaintenanceQuery = "SELECT * FROM assignedTo WHERE item_id = ? AND assigned_employee = 'Maintenance Out'";
                PreparedStatement checkMaintenanceStmt = con.prepareStatement(checkMaintenanceQuery);
                checkMaintenanceStmt.setInt(1, itemId);
                ResultSet rs = checkMaintenanceStmt.executeQuery();

                if (rs.next()) {
                    // If the item is assigned to "Maintenance Out", show an error message
                    JOptionPane.showMessageDialog(this, "Item sent for maintenance. Please fill Maintenance In form for this item before assigning it to a user.");
                    rs.close();
                    checkMaintenanceStmt.close();
                    con.close();
                    return;
                }

                rs.close();
                checkMaintenanceStmt.close();

                // Proceed with assignment if not in maintenance
                String assignmentDate = txtAssignmentDate.getText();
                String serialNumber = cboxSerNo.getSelectedItem().toString();
                String purpose = txtPurpose.getText();

                String selectedEmployee = cboxEmployee.getSelectedItem().toString();
                String employee = selectedEmployee.equals("Select") ? null : selectedEmployee;

                String selectedDivision = cboxDiv.getSelectedItem().toString();
                String division = selectedDivision.equals("Select") ? null : selectedDivision;

                String selectedOffice = cboxOffice.getSelectedItem().toString();
                String office = selectedOffice.equals("Select") ? null : selectedOffice;

                assignItem(itemId, serialNumber, assignmentDate, employee, division, office, purpose);

                JOptionPane.showMessageDialog(null, "Item Assigned Successfully");
                setVisible(false);
                new Assign_Item().setVisible(true);

                con.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error checking maintenance status: " + e.getMessage());
            }
        }


    }//GEN-LAST:event_btnAddItemActionPerformed

    private void cboxOfficeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxOfficeItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_cboxOfficeItemStateChanged

    private void cboxCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxCategoryItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String category = cboxCategory.getSelectedItem().toString();
            populateSerNoComboBox(category);
        }
    }//GEN-LAST:event_cboxCategoryItemStateChanged

    private void cboxEmployeeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxEmployeeItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && !cboxEmployee.getSelectedItem().toString().equals("Select")) {
            String selectedEmployee = cboxEmployee.getSelectedItem().toString().split(" - ")[0];
            populateDivisionAndOfficeComboBoxes(selectedEmployee);
        }

    }//GEN-LAST:event_cboxEmployeeItemStateChanged

    private void cboxDivItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxDivItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboxDivItemStateChanged

    private void cboxItemIDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxItemIDItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && !cboxItemID.getSelectedItem().toString().equals("Select")) {
            String selectedItem = cboxItemID.getSelectedItem().toString();
            syncItemIDAndSerNo(selectedItem, false);
        }
    }//GEN-LAST:event_cboxItemIDItemStateChanged

    private void cboxSerNoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxSerNoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED && !cboxSerNo.getSelectedItem().toString().equals("Select")) {
            String selectedItem = cboxSerNo.getSelectedItem().toString();
            syncItemIDAndSerNo(selectedItem, true);
        }
    }//GEN-LAST:event_cboxSerNoItemStateChanged

    private void CurrentAssignmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CurrentAssignmentTableMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_CurrentAssignmentTableMouseClicked

    private void txtPurposeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurposeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurposeActionPerformed

    private void btnExcelExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelExportActionPerformed
        // Open file chooser for saving
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setSelectedFile(new File("CurrentAssignments_.csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }

            try (FileWriter csvWriter = new FileWriter(filePath)) {
                DefaultTableModel model = (DefaultTableModel) CurrentAssignmentTable.getModel();

                // Write header
                for (int col = 0; col < model.getColumnCount(); col++) {
                    csvWriter.append(model.getColumnName(col));
                    if (col < model.getColumnCount() - 1) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");

                // Write rows
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF File");
        fileChooser.setSelectedFile(new File("CurrentAssignments_.pdf"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }

            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            String timeStr = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            String footerText = "Report generated by Sentinel Inventory Management System on " + dateStr + " at " + timeStr;

            PDFExporter pdfExporter = new PDFExporter("Current Assignments", "SIMS Assignments", footerText, filePath);

            try {
                pdfExporter.exportPDF(CurrentAssignmentTable.getModel());
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
            java.util.logging.Logger.getLogger(Assign_Item.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Assign_Item.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Assign_Item.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Assign_Item.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Assign_Item().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable CurrentAssignmentTable;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnExcelExport;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cboxCategory;
    private javax.swing.JComboBox<String> cboxDiv;
    private javax.swing.JComboBox<String> cboxEmployee;
    private javax.swing.JComboBox<String> cboxItemID;
    private javax.swing.JComboBox<String> cboxOffice;
    private javax.swing.JComboBox<String> cboxSerNo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtAssignmentDate;
    private javax.swing.JTextField txtPurpose;
    // End of variables declaration//GEN-END:variables
}
