package SIMS.Item;

import dao.ConnectionProvider;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;

public class EditWindow extends JFrame {

    private int itemId;
    private JTextField txtSerNo, txtCategory, txtMainDescription, txtDescription, txtModel, txtBrand, txtWarrantyEndDate, txtAcquiredDate;
    private JButton btnSave;
    private static final String DEFAULT_DATE_TEXT = "[YYYY-MM-DD]";

    public EditWindow(int itemId, String serNo, String category, String mainDescription, String description, String model, String brand, java.util.Date warrantyEndDate, java.util.Date acquiredDate) {
        this.itemId = itemId;

        setTitle("Edit Item");
        setSize(450, 500);
        setLayout(null);

        JLabel lblSerNo = new JLabel("Serial No:");
        lblSerNo.setBounds(30, 30, 100, 25);
        add(lblSerNo);

        txtSerNo = new JTextField(serNo != null ? serNo : "");
        txtSerNo.setBounds(150, 30, 250, 25);
        add(txtSerNo);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(30, 70, 100, 25);
        add(lblCategory);

        txtCategory = new JTextField(category != null ? category : "");
        txtCategory.setBounds(150, 70, 250, 25);
        add(txtCategory);

        JLabel lblMainDescription = new JLabel("Main Description:");
        lblMainDescription.setBounds(30, 110, 120, 25);
        add(lblMainDescription);

        txtMainDescription = new JTextField(mainDescription != null ? mainDescription : "");
        txtMainDescription.setBounds(150, 110, 250, 25);
        add(txtMainDescription);

        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setBounds(30, 150, 100, 25);
        add(lblDescription);

        txtDescription = new JTextField(description != null ? description : "");
        txtDescription.setBounds(150, 150, 250, 25);
        add(txtDescription);

        JLabel lblModel = new JLabel("Model:");
        lblModel.setBounds(30, 190, 100, 25);
        add(lblModel);

        txtModel = new JTextField(model != null ? model : "");
        txtModel.setBounds(150, 190, 250, 25);
        add(txtModel);

        JLabel lblBrand = new JLabel("Brand:");
        lblBrand.setBounds(30, 230, 100, 25);
        add(lblBrand);

        txtBrand = new JTextField(brand != null ? brand : "");
        txtBrand.setBounds(150, 230, 250, 25);
        add(txtBrand);

        JLabel lblWarrantyEndDate = new JLabel("Warranty End Date:");
        lblWarrantyEndDate.setBounds(30, 270, 120, 25);
        add(lblWarrantyEndDate);

        txtWarrantyEndDate = new JTextField(warrantyEndDate != null ? warrantyEndDate.toString() : DEFAULT_DATE_TEXT);
        txtWarrantyEndDate.setBounds(150, 270, 250, 25);
        add(txtWarrantyEndDate);

        JLabel lblAcquiredDate = new JLabel("Acquired Date:");
        lblAcquiredDate.setBounds(30, 310, 120, 25);
        add(lblAcquiredDate);

        txtAcquiredDate = new JTextField(acquiredDate != null ? acquiredDate.toString() : DEFAULT_DATE_TEXT);
        txtAcquiredDate.setBounds(150, 310, 250, 25);
        add(txtAcquiredDate);

        btnSave = new JButton("Save Changes");
        btnSave.setBounds(150, 350, 150, 30);
        add(btnSave);

        btnSave.addActionListener(e -> saveChanges());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void saveChanges() {
        String serialNo = txtSerNo.getText().trim();
        String category = txtCategory.getText().trim();
        String mainDescription = txtMainDescription.getText().trim();
        String description = txtDescription.getText().trim();
        String model = txtModel.getText().trim();
        String brand = txtBrand.getText().trim();
        String warrantyEndDateStr = txtWarrantyEndDate.getText().trim();
        String acquiredDateStr = txtAcquiredDate.getText().trim();

        if (serialNo.isEmpty() || category.isEmpty() || mainDescription.isEmpty() || model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = ConnectionProvider.getCon()) {
            con.setAutoCommit(false);

            // Replace default date text with null
            Date warrantyEndDate = DEFAULT_DATE_TEXT.equals(warrantyEndDateStr) || warrantyEndDateStr.isEmpty() ? null : Date.valueOf(warrantyEndDateStr);
            Date acquiredDate = DEFAULT_DATE_TEXT.equals(acquiredDateStr) || acquiredDateStr.isEmpty() ? null : Date.valueOf(acquiredDateStr);

            // Update the `item` table
            String updateItemQuery = "UPDATE item SET category = ?, mainDescription = ?, description = ?, model = ?, brand = ?, ser_no = ? WHERE id = ?";
            try (PreparedStatement psItem = con.prepareStatement(updateItemQuery)) {
                psItem.setString(1, category);
                psItem.setString(2, mainDescription);
                psItem.setString(3, description);
                psItem.setString(4, model);
                psItem.setString(5, brand);
                psItem.setString(6, serialNo);
                psItem.setInt(7, itemId);
                psItem.executeUpdate();
            }

            // Update the `itemAcquired` table
            String updateItemAcquiredQuery = "UPDATE itemAcquired SET acquireDate = ?, warrantyExpireDate = ? WHERE item_id = ?";
            try (PreparedStatement psItemAcquired = con.prepareStatement(updateItemAcquiredQuery)) {
                psItemAcquired.setDate(1, acquiredDate);
                psItemAcquired.setDate(2, warrantyEndDate);
                psItemAcquired.setInt(3, itemId);
                psItemAcquired.executeUpdate();
            }

            con.commit();
            JOptionPane.showMessageDialog(this, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
