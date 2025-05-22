package dao;

import java.io.*;

public class PasswordManager {

    private static final String PASSWORD_FILE = "password.txt";
    private static final String DEFAULT_PASSWORD = "SENTINEL";

    // Get the current password from the file
    public static String getPassword() {
        File file = new File(PASSWORD_FILE);
        if (!file.exists()) {
            setPassword(DEFAULT_PASSWORD); // Initialize with default password if file doesn't exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return DEFAULT_PASSWORD; // Fallback to default
        }
    }

    // Set a new password
    public static void setPassword(String newPassword) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PASSWORD_FILE))) {
            bw.write(newPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reset password to default
    public static void resetToDefault() {
        setPassword(DEFAULT_PASSWORD);
    }

    // Validate password
    public static boolean validatePassword(String enteredPassword) {
        return getPassword().equals(enteredPassword);
    }
}
