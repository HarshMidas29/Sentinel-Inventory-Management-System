package dao;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class Header extends PdfPageEventHelper {

    private String titleText;
    private String subtitleText;

    public Header(String titleText, String subtitleText) {
        this.titleText = titleText;
        this.subtitleText = subtitleText;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();

        // Sky blue background for header
        cb.setColorFill(new BaseColor(135, 206, 250)); // Sky blue color
        cb.rectangle(document.leftMargin() - 36, document.top() +12,
                document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() + 72, 60);
        cb.fill();

        // Title and subtitle
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(titleText, titleFont),
                (document.right() + document.left()) / 2, document.top() + 40, 0);

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(subtitleText, subtitleFont),
                (document.right() + document.left()) / 2, document.top() + 20, 0);
    }

}
