package SIMS.Item;

import SIMS.Menus.Item_Menu;
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
public class Add_Item extends javax.swing.JFrame {

    private int itemPk = 0;
    private java.util.List<String> itemTypes = new java.util.ArrayList<>(java.util.Arrays.asList("Select", "Laptop", "Monitor", "CPU", "UPS", "Printer", "Scanner", "Photocopy"));

    public Add_Item() {
        initComponents();
        loadItemTypes();
        cboxItemType.setModel(new javax.swing.DefaultComboBoxModel<>(itemTypes.toArray(new String[0])));
        setLocationRelativeTo(null);
    }

    private void loadItemTypes() {
        itemTypes.clear();
        itemTypes.add("Select"); // Add default "Select" option
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT type_name FROM item_types ORDER BY type_name ASC");
            while (rs.next()) {
                itemTypes.add(rs.getString("type_name"));
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading item types: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getSourceId(String sourceName, Connection con) {
        if (sourceName == null || sourceName.equals("Select")) {
            return 0; // Default to 0 if no valid source is selected
        }
        int sourceId = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id FROM source WHERE name = ?");
            ps.setString(1, sourceName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                sourceId = rs.getInt("id");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error retrieving source ID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return sourceId;
    }

    private void getAllSources() {
        try {
            Connection con = ConnectionProvider.getCon();
            Statement st = con.createStatement();
            if (cboxSourceType.getSelectedItem().toString() == "Vendor") {
                ResultSet rs = st.executeQuery("select * from source where type = \"Vendor\"");
                cboxSource.removeAllItems();
                cboxSource.addItem("Select");
                while (rs.next()) {
                    cboxSource.addItem((rs.getString("name")));
                }
            } else if (cboxSourceType.getSelectedItem().toString() == "Donor") {
                ResultSet rs = st.executeQuery("select * from source where type = \"Donor\"");
                cboxSource.removeAllItems();
                cboxSource.addItem("Select");
                while (rs.next()) {
                    cboxSource.addItem(rs.getString("name"));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private boolean validateFields(String formType) {
        boolean isValid = true;

        // Reset all label colors to default (black)
        jLabel2.setForeground(java.awt.Color.BLACK); // Item Type
        jLabel9.setForeground(java.awt.Color.BLACK); // Serial No
        jLabel8.setForeground(java.awt.Color.BLACK); // Main Description
        jLabel10.setForeground(java.awt.Color.BLACK); // Model

        // Validate only compulsory fields
        if (cboxItemType.getSelectedItem().toString().equals("Select")) {
            jLabel2.setForeground(java.awt.Color.RED);
            isValid = false;
        }
        if (txtSerNo.getText().trim().isEmpty()) {
            jLabel9.setForeground(java.awt.Color.RED);
            isValid = false;
        }
        if (txtMainDescription.getText().trim().isEmpty()) {
            jLabel8.setForeground(java.awt.Color.RED);
            isValid = false;
        }
        if (txtModel.getText().trim().isEmpty()) {
            jLabel10.setForeground(java.awt.Color.RED);
            isValid = false;
        }

        // Show error message if any compulsory field is invalid
        if (!isValid) {
            JOptionPane.showMessageDialog(this, "Please fill all compulsory fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }

        return isValid;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        txtBrand = new javax.swing.JTextField();
        btnAddItem = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cboxSource = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtMainDescription = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtWarrantyEndDate = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtAcquiredDate = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cboxItemType = new javax.swing.JComboBox<>();
        cboxSourceType = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtSerNo = new javax.swing.JTextField();
        btnAddType = new javax.swing.JButton();
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
        jLabel1.setText("Add New Item");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel2.setText("Item Type");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 120, -1, -1));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel3.setText("Brand");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 360, -1, -1));

        txtModel.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModelActionPerformed(evt);
            }
        });
        getContentPane().add(txtModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 320, 334, -1));

        txtBrand.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        getContentPane().add(txtBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 380, 330, -1));

        btnAddItem.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnAddItem.setText("Add Item");
        btnAddItem.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 680, 70, -1));

        btnReset.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 680, 70, -1));

        btnClose.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 690, 80, -1));

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel5.setText("Description");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, -1, -1));

        cboxSource.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxSource.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        cboxSource.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxSourceItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxSource, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 500, 330, -1));

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel8.setText("Main Description");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 180, -1, -1));

        txtMainDescription.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtMainDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMainDescriptionActionPerformed(evt);
            }
        });
        getContentPane().add(txtMainDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 200, 334, -1));

        jLabel10.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel10.setText("Model");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 300, -1, -1));

        txtDescription.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescriptionActionPerformed(evt);
            }
        });
        getContentPane().add(txtDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, 334, -1));

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel4.setText("Source Type");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, -1, -1));

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel7.setText("Source");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 480, -1, -1));

        txtWarrantyEndDate.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtWarrantyEndDate.setText("[YYYY-MM-DD]");
        getContentPane().add(txtWarrantyEndDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 620, 330, -1));

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel11.setText("Warranty End Date");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 600, -1, -1));

        txtAcquiredDate.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtAcquiredDate.setText("[YYYY-MM-DD]");
        txtAcquiredDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcquiredDateActionPerformed(evt);
            }
        });
        getContentPane().add(txtAcquiredDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 560, 330, -1));

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel12.setText("Acquired Date");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 540, -1, -1));

        cboxItemType.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxItemType.setModel(new javax.swing.DefaultComboBoxModel<>(itemTypes.toArray(new String[0]))
        );
        getContentPane().add(cboxItemType, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 290, -1));

        cboxSourceType.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        cboxSourceType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Vendor", "Donor" }));
        cboxSourceType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxSourceTypeItemStateChanged(evt);
            }
        });
        getContentPane().add(cboxSourceType, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 440, 330, -1));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        jLabel9.setText("Serial No ");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, -1, -1));

        txtSerNo.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        txtSerNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSerNoActionPerformed(evt);
            }
        });
        getContentPane().add(txtSerNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 334, -1));

        btnAddType.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnAddType.setText("+");
        btnAddType.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAddType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTypeActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddType, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 140, 30, 20));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/New/1.png"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModelActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_formComponentShown

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        new Item_Menu().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        new Add_Item().setVisible(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtMainDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMainDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMainDescriptionActionPerformed

    private void txtDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescriptionActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        // TODO add your handling code here:
        // Validate fields
        if (!validateFields("new")) {
            return;
        }

        // Retrieve values from form fields
        String acquiredDate = txtAcquiredDate.getText().trim().isEmpty() || txtAcquiredDate.getText().trim().equals("[YYYY-MM-DD]")
                ? "1970-01-01" : txtAcquiredDate.getText();
        String warrantyEndDate = txtWarrantyEndDate.getText().trim().isEmpty() || txtWarrantyEndDate.getText().trim().equals("[YYYY-MM-DD]")
                ? "1970-01-01" : txtWarrantyEndDate.getText();
        String brand = txtBrand.getText().trim();
        String description = txtDescription.getText().trim();
        String mainDescription = txtMainDescription.getText().trim();
        String model = txtModel.getText().trim();
        String category = cboxItemType.getSelectedItem().toString();
        String sourceName = cboxSource.getSelectedItem() != null ? cboxSource.getSelectedItem().toString() : "Select";
        String serialNo = txtSerNo.getText().trim();

        Connection con = null;
        try {
            // Database connection
            con = ConnectionProvider.getCon();
            con.setAutoCommit(false);

            // Check if serial number already exists
            PreparedStatement psCheck = con.prepareStatement("SELECT COUNT(*) FROM item WHERE ser_no = ?");
            psCheck.setString(1, serialNo);
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "The Serial Number / Product Number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            rsCheck.close();
            psCheck.close();

            // Get source ID
            int sourceId = getSourceId(sourceName, con);

            // Insert item into `item` table
            PreparedStatement psItem = con.prepareStatement(
                    "INSERT INTO item (category, mainDescription, description, model, brand, ser_no) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            psItem.setString(1, category);
            psItem.setString(2, mainDescription);
            psItem.setString(3, description);
            psItem.setString(4, model);
            psItem.setString(5, brand);
            psItem.setString(6, serialNo);
            psItem.executeUpdate();

            // Get the generated item ID
            ResultSet rsItem = psItem.getGeneratedKeys();
            int itemId = 0;
            if (rsItem.next()) {
                itemId = rsItem.getInt(1);
            }
            rsItem.close();
            psItem.close();

            // Insert item acquisition details into `itemAcquired` table only if sourceId is valid
            if (sourceId > 0) {
                PreparedStatement psItemAcquired = con.prepareStatement(
                        "INSERT INTO itemAcquired (item_id, source_id, acquireDate, warrantyExpireDate) VALUES (?, ?, ?, ?)"
                );
                psItemAcquired.setInt(1, itemId);
                psItemAcquired.setInt(2, sourceId);
                psItemAcquired.setDate(3, Date.valueOf(acquiredDate));
                psItemAcquired.setDate(4, Date.valueOf(warrantyEndDate));
                psItemAcquired.executeUpdate();
                psItemAcquired.close();
            }

            // Commit transaction
            con.commit();

            // Show success message and reset the form
            JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
            new Add_Item().setVisible(true);

        } catch (IllegalArgumentException e) {
            // Handle invalid date format
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD for dates.", "Error", JOptionPane.ERROR_MESSAGE);
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_btnAddItemActionPerformed

    private void cboxSourceTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxSourceTypeItemStateChanged
        // TODO add your handling code here:
        getAllSources();
    }//GEN-LAST:event_cboxSourceTypeItemStateChanged

    private void cboxSourceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxSourceItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_cboxSourceItemStateChanged

    private void txtAcquiredDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcquiredDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAcquiredDateActionPerformed

    private void txtSerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerNoActionPerformed

    private void btnAddTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTypeActionPerformed
        // Show input dialog to add a new item type
        String newItemType = JOptionPane.showInputDialog(this, "Enter new item type:", "Add Item Type", JOptionPane.PLAIN_MESSAGE);

        // Validate user input
        if (newItemType != null && !newItemType.trim().isEmpty()) {
            newItemType = newItemType.trim();
            try {
                Connection con = ConnectionProvider.getCon();
                PreparedStatement ps = con.prepareStatement("INSERT INTO item_types (type_name) VALUES (?)");
                ps.setString(1, newItemType);
                ps.executeUpdate();
                ps.close();
                con.close();

                // Reload the item types to include the newly added type
                loadItemTypes();
                cboxItemType.setModel(new javax.swing.DefaultComboBoxModel<>(itemTypes.toArray(new String[0])));
                JOptionPane.showMessageDialog(this, "New item type added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                if (e.getMessage().toLowerCase().contains("unique constraint failed")) {
                    JOptionPane.showMessageDialog(this, "This item type already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error adding item type: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid item type.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddTypeActionPerformed

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
            java.util.logging.Logger.getLogger(Add_Item.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Add_Item.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Add_Item.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Add_Item.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Add_Item().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnAddType;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cboxItemType;
    private javax.swing.JComboBox<String> cboxSource;
    private javax.swing.JComboBox<String> cboxSourceType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtAcquiredDate;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtMainDescription;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtSerNo;
    private javax.swing.JTextField txtWarrantyEndDate;
    // End of variables declaration//GEN-END:variables
}
