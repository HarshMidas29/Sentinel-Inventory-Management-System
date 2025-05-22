/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Daham Yakandawala
 */
public class tables {

    public static void main(String[] args) {
        Connection con = null;
        Statement st = null;
        try {
            con = ConnectionProvider.getCon();
            st = con.createStatement();

            st.executeUpdate("DROP TABLE IF EXISTS source");
            st.executeUpdate("DROP TABLE IF EXISTS item");
            st.executeUpdate("DROP TABLE IF EXISTS employee");
            st.executeUpdate("DROP TABLE IF EXISTS office");
            st.executeUpdate("DROP TABLE IF EXISTS division_section_project");
            st.executeUpdate("DROP TABLE IF EXISTS maintenance_in");
            st.executeUpdate("DROP TABLE IF EXISTS maintenance_out");
            st.executeUpdate("DROP TABLE IF EXISTS disposal");
            st.executeUpdate("DROP TABLE IF EXISTS assignedTo");
            st.executeUpdate("DROP TABLE IF EXISTS assignmentHistory");
            st.executeUpdate("DROP TABLE IF EXISTS itemAcquired");
            /* Old Tables
            st.executeUpdate("CREATE TABLE itemAcquired (item_id INT NOT NULL, source_id INT NOT NULL, acquireDate DATE NOT NULL, warrantyExpireDate DATE NOT NULL, PRIMARY KEY (item_id, source_id), FOREIGN KEY (item_id) REFERENCES item(id), FOREIGN KEY (source_id) REFERENCES source(id));");
            st.executeUpdate("CREATE TABLE assignmentHistory (id INT AUTO_INCREMENT PRIMARY KEY, item_id INTEGER, ser_no VARCHAR(50), assigned_employee VARCHAR(50), assigned_division VARCHAR(50), assigned_office VARCHAR(50), purpose VARCHAR(50), assignedDate DATE, returnDate DATE);");
            st.executeUpdate("CREATE TABLE assignedTo (id INT AUTO_INCREMENT PRIMARY KEY, item_id INTEGER, ser_no VARCHAR(50), assigned_employee VARCHAR(50), assigned_division VARCHAR(50), assigned_office VARCHAR(50), purpose VARCHAR(50), assignedDate DATE);");
            st.executeUpdate("CREATE TABLE disposal (item_id INTEGER PRIMARY KEY,ser_no VARCHAR(50),item_type VARCHAR(50), reason VARCHAR(255), last_user_id INTEGER, last_division_id INTEGER, last_office_id INTEGER, disposalDate DATE, officerTakingOver INTEGER, FOREIGN KEY (item_id) REFERENCES item(id), FOREIGN KEY (last_user_id) REFERENCES employee(id), FOREIGN KEY (last_division_id) REFERENCES division_section_project(id), FOREIGN KEY (last_office_id) REFERENCES office(id), FOREIGN KEY (officerTakingOver) REFERENCES employee(id));");
             */
            st.executeUpdate("CREATE TABLE source (id int AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), type VARCHAR(50), contactNo INTEGER, address VARCHAR(100), email VARCHAR(50));");
            st.executeUpdate("CREATE TABLE item (id INT AUTO_INCREMENT PRIMARY KEY, ser_no VARCHAR(50), category VARCHAR(50), mainDescription VARCHAR(50), description VARCHAR(400), model VARCHAR(50), brand VARCHAR(50));");
            st.executeUpdate("CREATE TABLE employee (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), nic VARCHAR(50), contact INTEGER, designation VARCHAR(50), division VARCHAR(50), office VARCHAR(50), remarks VARCHAR(255));");
            st.executeUpdate("CREATE TABLE office (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), address VARCHAR(255));");
            st.executeUpdate("CREATE TABLE division_section_project (id INT AUTO_INCREMENT PRIMARY KEY, type VARCHAR(255) COMMENT 'Division / Section / Project', name VARCHAR(255), office int, location VARCHAR(255) COMMENT 'Floor / Room etc.', CONSTRAINT fk_office FOREIGN KEY (office) REFERENCES office(id));");
            st.executeUpdate("CREATE TABLE itemAcquired (item_id INT NOT NULL, source_id INT NOT NULL, acquireDate DATE NOT NULL, warrantyExpireDate DATE NOT NULL, PRIMARY KEY (item_id, source_id));");
            st.executeUpdate("CREATE TABLE assignmentHistory (id INT AUTO_INCREMENT PRIMARY KEY, item_id INTEGER, ser_no VARCHAR(50), assigned_employee VARCHAR(50), assigned_division VARCHAR(50), assigned_office VARCHAR(50), purpose VARCHAR(50), assignedDate DATE, returnDate DATE);");
            st.executeUpdate("CREATE TABLE assignedTo (id INT AUTO_INCREMENT PRIMARY KEY, item_id INTEGER, ser_no VARCHAR(50), assigned_employee VARCHAR(50), assigned_division VARCHAR(50), assigned_office VARCHAR(50), purpose VARCHAR(50), assignedDate DATE);");
            st.executeUpdate("CREATE TABLE disposal (item_id INTEGER PRIMARY KEY, ser_no VARCHAR(50), item_type VARCHAR(50), reason VARCHAR(255), last_user_id INTEGER, last_division_id INTEGER, last_office_id INTEGER, disposalDate DATE, officerTakingOver INTEGER);");
            st.executeUpdate("CREATE TABLE maintenance_out (maintenance_id INT AUTO_INCREMENT PRIMARY KEY, item_id INT, type VARCHAR(50) COMMENT 'Repair, Upgrade, Service', reason VARCHAR(255), company_id VARCHAR(50), sentForRepairDate DATE, remarks VARCHAR(255), FOREIGN KEY (item_id) REFERENCES item(id));");
            st.executeUpdate("CREATE TABLE maintenance_in (maintenance_id INT PRIMARY KEY, item_id INT, type VARCHAR(50) COMMENT 'Repair, Upgrade, Service', reason VARCHAR(255), company_id VARCHAR(50), sentForRepairDate DATE, RepairCompleteDate DATE, repair_warranty VARCHAR(50), remarks VARCHAR(255), price VARCHAR(50), FOREIGN KEY (item_id) REFERENCES item(id));");

            JOptionPane.showMessageDialog(null, "Table Created Successfully");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                con.close();
                st.close();
            } catch (Exception e) {
            }
        }
        //populater();
    }

    public static void populater() {
        Connection con = null;
        Statement st = null;
        try {
            con = ConnectionProvider.getCon();
            st = con.createStatement();
            /*
            // Insert dummy data into source table
            st.executeUpdate("INSERT INTO source (name, type, contactNo, address, email) VALUES "
                    + "('Tech Solutions Inc.', 'Vendor', 1234567890, '123 Tech Park, Silicon Valley', 'vendor1@example.com'),"
                    + "('Global IT Services', 'Vendor', 1234567891, '456 Innovation Drive, San Francisco', 'vendor2@example.com'),"
                    + "('Gadget World', 'Vendor', 1234567892, '789 Tech Avenue, New York', 'vendor3@example.com'),"
                    + "('Device Hub', 'Vendor', 1234567893, '1011 Tech Street, Los Angeles', 'vendor4@example.com'),"
                    + "('Hardware Pro', 'Vendor', 1234567894, '1213 Tech Lane, Chicago', 'vendor5@example.com'),"
                    + "('United Nations', 'Donor', 1234567895, '1 UN Plaza, New York', 'donor1@example.com'),"
                    + "('World Health Organization', 'Donor', 1234567896, '20 Avenue Appia, Geneva', 'donor2@example.com'),"
                    + "('Red Cross', 'Donor', 1234567897, '2020 Red Cross Road, Washington', 'donor3@example.com'),"
                    + "('UNICEF', 'Donor', 1234567898, '3 UN Plaza, New York', 'donor4@example.com'),"
                    + "('Oxfam', 'Donor', 1234567899, '1-3 Rue de Varembé, Geneva', 'donor5@example.com');");

             //Insert dummy data into item table
            st.executeUpdate("INSERT INTO item (category, mainDescription, description, model, brand) VALUES "
                    + "('Laptop', 'Intel Core i7 Processor', 'Desc1', 'Model1', 'Brand1'),"
                    + "('Laptop', 'AMD Ryzen 5 Processor', 'Desc2', 'Model2', 'Brand2'),"
                    + "('Monitor', '4K UHD Display', 'Desc3', 'Model3', 'Brand3'),"
                    + "('Monitor', 'Curved LED Display', 'Desc4', 'Model4', 'Brand4'),"
                    + "('Desktop', 'Intel Core i9 Processor', 'Desc5', 'Model5', 'Brand5'),"
                    + "('Desktop', 'AMD Ryzen 7 Processor', 'Desc6', 'Model6', 'Brand6'),"
                    + "('Printer', 'LaserJet Technology', 'Desc7', 'Model7', 'Brand7'),"
                    + "('Printer', 'Inkjet Technology', 'Desc8', 'Model8', 'Brand8'),"
                    + "('Scanner', 'High-Resolution Scanning', 'Desc9', 'Model9', 'Brand9'),"
                    + "('Scanner', 'Wireless Scanning', 'Desc10', 'Model10', 'Brand10');");

            // Insert dummy data into itemAcquired table
            st.executeUpdate("INSERT INTO itemAcquired (item_id, source_id, acquireDate, warrantyExpireDate) VALUES "
                    + "(1, 1, '2022-01-01', '2024-01-01'),"
                    + "(2, 2, '2022-02-01', '2024-02-01'),"
                    + "(3, 3, '2022-03-01', '2024-03-01'),"
                    + "(4, 4, '2022-04-01', '2024-04-01'),"
                    + "(5, 5, '2022-05-01', '2024-05-01'),"
                    + "(6, 6, '2022-06-01', '2024-06-01'),"
                    + "(7, 7, '2022-07-01', '2024-07-01'),"
                    + "(8, 8, '2022-08-01', '2024-08-01'),"
                    + "(9, 9, '2022-09-01', '2024-09-01'),"
                    + "(10, 10, '2022-10-01', '2024-10-01');");

            // Insert dummy data into employee table
            st.executeUpdate("INSERT INTO employee (name, contact, appointment) VALUES "
                + "('John Doe', '98765432', 'Manager'),"
                + "('Jane Smith', '98763211', 'Supervisor'),"
                + "('Alice Johnson', '96543212', 'Technician'),"
                + "('Bob Brown', '98765213', 'Clerk'),"
                + "('Charlie Davis', '96543214', 'Analyst'),"
                + "('Dana White', '98763215', 'Assistant'),"
                + "('Evan Green', '98763216', 'Coordinator'),"
                + "('Fiona Black', '98543217', 'Consultant'),"
                + "('George King', '98543218', 'Specialist'),"
                + "('Hannah Lee', '98743219', 'Advisor');");

            // Insert dummy data into location table
            st.executeUpdate("INSERT INTO location (type, name, address) VALUES "
                    + "('Division', 'Admin', 'Admin Division Address'),"
                    + "('Division', 'Inquiries', 'Inquiries Division Address'),"
                    + "('Division', 'Investigation', 'Investigation Division Address'),"
                    + "('Division', 'Education', 'Education Division Address'),"
                    + "('Office', 'Kandy', 'Kandy Office Address'),"
                    + "('Office', 'Anuradhapura', 'Anuradhapura Office Address'),"
                    + "('Office', 'Kurunegala', 'Kurunegala Office Address'),"
                    + "('Office', 'Trincomalee', 'Trincomalee Office Address'),"
                    + "('Division', 'Finance', 'Finance Division Address'),"
                    + "('Office', 'Galle', 'Galle Office Address');");

            // Insert dummy data into disposal table
            st.executeUpdate("INSERT INTO disposal (item_id, reason, disposalDate, officerTakingOver) VALUES "
                    + "(1, 'Obsolete', '2023-01-01', 1),"
                    + "(2, 'Damaged', '2023-02-01', 2),"
                    + "(3, 'Upgrade', '2023-03-01', 3),"
                    + "(4, 'Replaced', '2023-04-01', 4),"
                    + "(5, 'Obsolete', '2023-05-01', 5),"
                    + "(6, 'Damaged', '2023-06-01', 6),"
                    + "(7, 'Upgrade', '2023-07-01', 7),"
                    + "(8, 'Replaced', '2023-08-01', 8),"
                    + "(9, 'Obsolete', '2023-09-01', 9),"
                    + "(10, 'Damaged', '2023-10-01', 10);");
             */
            JOptionPane.showMessageDialog(null, "Dummy Data Created Successfully");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

}
