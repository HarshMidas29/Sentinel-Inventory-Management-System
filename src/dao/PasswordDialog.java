package dao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PasswordDialog extends JDialog {

    private boolean isPasswordValid = false; // To indicate if the password is valid
    private Runnable onPasswordValidated;   // Callback for successful password validation

    public PasswordDialog(JFrame parent, Runnable onPasswordValidated) {
        super(parent, "Enter Password", true);
        this.onPasswordValidated = onPasswordValidated;

        // Set dialog properties
        setSize(500, 250); // Increased size for better alignment
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        // Create main panel for input and buttons
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Password Label
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblPassword, gbc);

        // Add Password Field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(passwordField, gbc);

        // Add Buttons
        JButton btnOk = new JButton("OK");
        JButton btnChangePassword = new JButton("Change Password");
        JButton btnResetPassword = new JButton("Set Default");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(btnOk, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(btnChangePassword, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        mainPanel.add(btnResetPassword, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners
        btnOk.addActionListener((ActionEvent e) -> {
            String enteredPassword = new String(passwordField.getPassword());
            if (PasswordManager.validatePassword(enteredPassword)) {
                isPasswordValid = true;
                dispose(); // Close the dialog
                if (onPasswordValidated != null) {
                    onPasswordValidated.run(); // Trigger the callback
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Password!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnChangePassword.addActionListener((ActionEvent e) -> changePassword());
        btnResetPassword.addActionListener((ActionEvent e) -> {
            PasswordManager.resetToDefault();
            JOptionPane.showMessageDialog(this, "Password reset to default.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // Change password functionality
    private void changePassword() {
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmNewPasswordField = new JPasswordField();

        Object[] message = {
            "Current Password:", currentPasswordField,
            "New Password:", newPasswordField,
            "Confirm New Password:", confirmNewPasswordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmNewPassword = new String(confirmNewPasswordField.getPassword());

            if (!PasswordManager.validatePassword(currentPassword)) {
                JOptionPane.showMessageDialog(this, "Current password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newPassword.isEmpty() || !newPassword.equals(confirmNewPassword)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PasswordManager.setPassword(newPassword);
            JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public boolean isPasswordValid() {
        return isPasswordValid;
    }
}
