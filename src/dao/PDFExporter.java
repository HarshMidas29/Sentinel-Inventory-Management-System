package dao;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;

public class PDFExporter {

    private String titleText;
    private String subtitleText;
    private String footerText;
    private String filePath;

    public PDFExporter(String titleText, String subtitleText, String footerText, String filePath) {
        this.titleText = titleText;
        this.subtitleText = subtitleText;
        this.footerText = footerText;
        this.filePath = filePath;
    }

    public void exportPDF(TableModel tableModel) {
        try {
            Document document = new Document(PageSize.A4, 36, 36, 100, 70); // Top and bottom margins set for header and footer


            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Set the header and footer events
            writer.setPageEvent(new Header(titleText, subtitleText));
            writer.setPageEvent(new Footer(footerText));

            document.open();

            // Add padding below the header
            document.add(new Paragraph(" ")); // Add space below header
            document.add(new Paragraph(" ")); // Additional space for clarity

            // Add table data
            addTable(document, tableModel);

            document.close();
            System.out.println("PDF exported successfully to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error exporting PDF: " + e.getMessage());
        }
    }

    private void addTable(Document document, TableModel tableModel) throws DocumentException {
        PdfPTable table = new PdfPTable(tableModel.getColumnCount());
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        // Set table header
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        for (int col = 0; col < tableModel.getColumnCount(); col++) {
            PdfPCell headerCell = new PdfPCell(new Phrase(tableModel.getColumnName(col), headerFont));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }

        // Set table data rows
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                Object cellValue = tableModel.getValueAt(row, col);
                PdfPCell cell = new PdfPCell(new Phrase(cellValue != null ? cellValue.toString() : "", cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }

        document.add(table);
    }
}
