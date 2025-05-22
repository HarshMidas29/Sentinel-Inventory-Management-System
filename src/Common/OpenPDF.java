/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Common;

import java.io.File;
import javax.swing.JOptionPane;
import dao.InventoryUtils;

/**
 *
 * @author Daham Yakandawala
 */
public class OpenPDF {

    public static void OpenById(String id) {
        try {
            if (new File(InventoryUtils.billPath + id + ".pdf").exists()) {
                Process p = Runtime
                        .getRuntime()
                        .exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", InventoryUtils.billPath + id + ".pdf"});
            } else {
                JOptionPane.showMessageDialog(null, "File Does Not Exist");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
