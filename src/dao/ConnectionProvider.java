package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionProvider {

    private static final String DATABASE_URL = "jdbc:sqlite:database.db";

    /**
     * Get a new database connection. Each call to this method creates a new
     * connection instance.
     *
     * @return A new Connection object.
     */
    public static Connection getCon() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish and return a new connection
            return DriverManager.getConnection(DATABASE_URL);

        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initialize the database by creating necessary tables and inserting
     * default values.
     */
    public static void initializeDatabase() {
        String[] queries = {
            "CREATE TABLE IF NOT EXISTS source ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT, "
            + "type TEXT, "
            + "contactNo INTEGER, "
            + "address TEXT, "
            + "email TEXT"
            + ");",
            "CREATE TABLE IF NOT EXISTS item ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "ser_no TEXT, "
            + "category TEXT, "
            + "mainDescription TEXT, "
            + "description TEXT, "
            + "model TEXT, "
            + "brand TEXT"
            + ");",
            "CREATE TABLE IF NOT EXISTS employee ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT, "
            + "nic TEXT, "
            + "contact INTEGER, "
            + "designation TEXT, "
            + "division TEXT, "
            + "office TEXT, "
            + "remarks TEXT"
            + ");",
            "CREATE TABLE IF NOT EXISTS office ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT, "
            + "address TEXT"
            + ");",
            "CREATE TABLE IF NOT EXISTS division_section_project ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "type TEXT, " // COMMENT 'Division / Section / Project'
            + "name TEXT, "
            + "office INTEGER, "
            + "location TEXT, " // COMMENT 'Floor / Room etc.'
            + "FOREIGN KEY (office) REFERENCES office(id)"
            + ");",
            "CREATE TABLE IF NOT EXISTS itemAcquired ("
            + "item_id INTEGER NOT NULL, "
            + "source_id INTEGER NOT NULL, "
            + "acquireDate DATE NOT NULL, "
            + "warrantyExpireDate DATE NOT NULL, "
            + "PRIMARY KEY (item_id, source_id)"
            + ");",
            "CREATE TABLE IF NOT EXISTS assignmentHistory ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "item_id INTEGER, "
            + "ser_no TEXT, "
            + "assigned_employee TEXT, "
            + "assigned_division TEXT, "
            + "assigned_office TEXT, "
            + "purpose TEXT, "
            + "assignedDate DATE, "
            + "returnDate DATE"
            + ");",
            "CREATE TABLE IF NOT EXISTS assignedTo ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "item_id INTEGER, "
            + "ser_no TEXT, "
            + "assigned_employee TEXT, "
            + "assigned_division TEXT, "
            + "assigned_office TEXT, "
            + "purpose TEXT, "
            + "assignedDate DATE"
            + ");",
            "CREATE TABLE IF NOT EXISTS disposal ("
            + "item_id INTEGER PRIMARY KEY, "
            + "ser_no TEXT, "
            + "item_type TEXT, "
            + "reason TEXT, "
            + "last_user_id INTEGER, "
            + "last_division_id INTEGER, "
            + "last_office_id INTEGER, "
            + "disposalDate DATE, "
            + "officerTakingOver INTEGER"
            + ");",
            "CREATE TABLE IF NOT EXISTS maintenance_out ("
            + "maintenance_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "item_id INTEGER, "
            + "type TEXT, " // COMMENT 'Repair, Upgrade, Service'
            + "reason TEXT, "
            + "company_id TEXT, "
            + "sentForRepairDate DATE, "
            + "remarks TEXT, "
            + "FOREIGN KEY (item_id) REFERENCES item(id)"
            + ");",
            "CREATE TABLE IF NOT EXISTS maintenance_in ("
            + "maintenance_id INTEGER PRIMARY KEY, "
            + "item_id INTEGER, "
            + "type TEXT, " // COMMENT 'Repair, Upgrade, Service'
            + "reason TEXT, "
            + "company_id TEXT, "
            + "sentForRepairDate DATE, "
            + "RepairCompleteDate DATE, "
            + "repair_warranty TEXT, "
            + "remarks TEXT, "
            + "price TEXT, "
            + "FOREIGN KEY (item_id) REFERENCES item(id)"
            + ");",
            "CREATE TABLE IF NOT EXISTS item_types ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "type_name TEXT NOT NULL UNIQUE"
            + ");",
            "INSERT OR IGNORE INTO item_types (type_name) VALUES "
            + "('Laptop'), "
            + "('Monitor'), "
            + "('CPU'), "
            + "('UPS'), "
            + "('Printer'), "
            + "('Scanner'), "
            + "('Photocopy');"
        };

        try (Connection con = getCon(); Statement st = con.createStatement()) {

            for (String query : queries) {
                st.executeUpdate(query);
            }

        } catch (SQLException e) {
            System.err.println("Failed to initialize the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
