package dao;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class Footer extends PdfPageEventHelper {

    private String footerText;

    public Footer(String footerText) {
        this.footerText = footerText;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();

        // Sky blue background for footer
        cb.setColorFill(new BaseColor(135, 206, 250)); // Sky blue color
        cb.rectangle(document.leftMargin() - 36, document.bottom() - 60,
                document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() + 72, 50);
        cb.fill();

        // Footer text
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC, BaseColor.BLACK);
        Phrase footerPhrase = new Phrase(footerText, footerFont);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footerPhrase,
                (document.right() + document.left()) / 2, document.bottom() - 40, 0);
    }

}
